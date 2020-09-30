/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import com.elf.io.JpegFileFilter;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class JpegReader {

    private static final int ERR = 1;
    private static boolean debug = false;
    private static boolean verbose = false;
    private boolean isIntelFormat;
    private String rawTimestamp; // 2020:07:02 10:17:00
    private Dimension dimensions;

    public JpegReader(File file) {
        try {
            read(file);
        } catch (Exception ex) {
            Logger.getLogger(JpegReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setDebug(boolean chattyKathy) {
        debug = chattyKathy;
    }

    public static void setVerbose(boolean superChattyKathy) {
        verbose = superChattyKathy;
    }

    public Calendar getTimestamp() {
        if (rawTimestamp == null || rawTimestamp.length() != 19) {
            return null;
        }
        String[] strings = rawTimestamp.split("[: \"]");
        if (strings.length != 6) {
            return null;
        }
        int[] ints = convertDateStringsToInts(strings);

        if (ints == null) {
            return null;
        }
        // e.g. August will be recovered as "08", but Calendar wants zero-based
        // so we need to subtract 1
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(ints[0], ints[1] - 1, ints[2], ints[3], ints[4], ints[5]);
        return c;
    }

    public String getFilenameFromTimestamp(boolean useSeconds) {
        Calendar c = getTimestamp();
        String fname;
        if (c == null) {
            return null;
        }

        if (useSeconds) {
            String format = "%04d_%02d%02d_%02d%02d%02d.jpg";
            fname = String.format(format,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY), // IMPORTANT!  "HOUR" is 12-hour
                    c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND));

        } else {
            String format = "%04d_%02d%02d_%02d%02d.jpg";
            fname = String.format(format,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY), // IMPORTANT!  "HOUR" is 12-hour
                    c.get(Calendar.MINUTE));
        }

        return fname;
    }

    private void setDimensions(File f) {
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(f);
            dimensions = new Dimension(bimg.getWidth(), bimg.getHeight());
            debug("Dimensions: " + dimensions);
        } catch (IOException ex) {
            dimensions = new Dimension(0, 0);
        }
    }

    private final void read(File file) throws IOException {
        setDimensions(file);
        rawTimestamp = null;
        debug("\n\nFilename: " + file.getAbsolutePath());
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        // Check if JPEG
        if (raf.readShort() == (short) 0xFFD8) {
            verbose("Looks like a JPEG file");
        } else {
            raf.close();
            throw new RuntimeException("Not a JPEG File");
        }

        // Find Exif marker
        boolean exifFound = false;
        // FFC0 is segment for image dimensions
        for (long i = raf.getFilePointer(); i < raf.length() - 1; i++) {
            short current = raf.readShort(); // Read next two bytes
            if (current == (short) 0xFFE1) {
                exifFound = true;
                verbose("Found Exif application marker");
                break;
            }
            // Move only one byte per iteration
            raf.seek(raf.getFilePointer() - 1);
        }

        if (!exifFound) {
            raf.close();
            throw new RuntimeException("Couldn't find Exif application marker");
        }
        raf.skipBytes(8); // Skip data size and Exif\0\0
        // Check if Intel format
        isIntelFormat = raf.readShort() == (short) 0x4949;
        verbose("Format: " + (isIntelFormat ? "Intel" : "Not Intel"));

        // Check tag 0x2a00
        if (raf.readShort() == (short) 0x2A00) {
            verbose("Confirmed Intel format");
        }
// Get offset of IFD
        byte[] offsetBytes = new byte[4];
        raf.readFully(offsetBytes);
        int offset = convert(offsetBytes) - 8;
        raf.skipBytes(offset);
        // Get number of directory entries
        int nEntries = convert(raf.readByte(), raf.readByte());

        long resetPoint = raf.getFilePointer() - 4;
        List<DirectoryDataPointer> directoryDataPointers = new ArrayList<>();
        for (int i = 0; i < nEntries; i++) {
            byte[] entry = new byte[12]; // Each entry is always 12 bytes
            raf.readFully(entry);

            int tag = convert(entry[0], entry[1]);
            int length = convert(entry[4], entry[5], entry[6], entry[7]);
            int dataOffset = convert(entry[8], entry[9], entry[10], entry[11]) - 6;

            // Make
            if (tag == (short) 0x010f) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Make"));
            } // Model
            else if (tag == (short) 0x0110) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Model"));
            } // Software
            else if (tag == (short) 0x0131) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Software"));
            } // Copyright
            else if (tag == (short) 0x8298) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Copyright"));
            } // Date/Time
            else if (tag == (short) 0x0132) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Date/Time"));
            } else {
                verbose("Unknown Tag: " + tag);
            }
        }
        verbose("\n===START EXIF DATA===");

        for (DirectoryDataPointer ddp : directoryDataPointers) {
            raf.seek(resetPoint);
            raf.skipBytes(ddp.getOffset());
            byte[] data = new byte[ddp.getLength()];
            raf.readFully(data);

            // the last byte read is a zero.  Not "0" but actual zero!!!
            String sData = new String(data, 0, data.length - 1);
            String type = ddp.getType().toLowerCase();
            verbose(type + ": " + sData);
            if (type.equals("date/time")) {
                rawTimestamp = sData;
                debug("TIMESTAMP TAG FOUND: " + sData);
            }
        }
        verbose("===END EXIF DATA===");
        raf.close();
    }

    private int[] convertDateStringsToInts(String[] strings) throws NumberFormatException {
        if (strings.length != 6) {
            return null;
        }

        int[] ints = new int[6];

        for (int i = 0; i < 6; i++) {
            ints[i] = Integer.parseInt(strings[i]);
        }
        return ints;
    }

    private int convert(byte... bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        if (isIntelFormat) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        if (bytes.length == 2) {
            return buffer.getShort();
        } else {
            return buffer.getInt();

        }
    }

    private static final void debug(String s) {
        if (debug) {
            System.out.println(s);
        }
    }

    private static final void verbose(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            debug("Enter a dirname argument");
            System.exit(1);
        }
        File dir = new File(args[0]);
        if (!dir.exists() || !dir.isDirectory()) {
            debug("Enter a VALID dirname argument");
            System.exit(1);
        }
        File[] files = dir.listFiles(new JpegFileFilter());

        for (File f : files) {
            JpegReader reader = new JpegReader(f);

            System.out.print("Filename: " + f + "  Timestamp: " + reader.getTimestamp().getTime());
            System.out.println("   Suggested Filename: " + "BB_" + reader.getFilenameFromTimestamp(false));

        }
    }

    private static class DirectoryDataPointer {

        private int length;
        private String type;
        private int offset;

        public DirectoryDataPointer(int offset, int length, String type) {
            super();
            this.length = length;
            this.type = type;
            this.offset = offset;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

}
