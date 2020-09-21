/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import com.elf.io.JpegFileFilter;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JpegReader {

    //private File file;
    private static boolean isIntelFormat = false;
    private static final int ERR = 1;

    private JpegReader() {
    }

    public static int read(File file) throws IOException {
        System.out.println("\n\nFilename: " + file.getAbsolutePath());
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        // Check if JPEG
        if (raf.readShort() == (short) 0xFFD8) {
            System.out.println("Looks like a JPEG file");
        } else {
            System.err.println("Not a JPEG file ");
            raf.close();
            return ERR;
        }

// Find Exif marker
        boolean exifFound = false;
        for (long i = raf.getFilePointer(); i < raf.length() - 1; i++) {
            short current = raf.readShort(); // Read next two bytes
            if (current == (short) 0xFFE1) {
                exifFound = true;
                System.out.println("Found Exif application marker");
                break;
            }
            // Move only one byte per iteration
            raf.seek(raf.getFilePointer() - 1);
        }

        if (!exifFound) {
            System.err.println("Couldn't find Exif application marker");
            raf.close();
            return ERR;
        }
        raf.skipBytes(8); // Skip data size and Exif\0\0
        // Check if Intel format
        isIntelFormat = raf.readShort() == (short) 0x4949;
        System.out.println("Format: " + (isIntelFormat ? "Intel" : "Not Intel"));

        // Check tag 0x2a00
        if (raf.readShort() == (short) 0x2A00) {
            System.out.println("Confirmed Intel format");
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
            }

            // Model
            if (tag == (short) 0x0110) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Model"));
            }

            // Software
            if (tag == (short) 0x0131) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Software"));
            }

            // Copyright
            if (tag == (short) 0x8298) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Copyright"));
            }

            // Date/Time
            if (tag == (short) 0x0132) {
                directoryDataPointers.add(
                        new DirectoryDataPointer(dataOffset, length, "Date/Time"));
            }
        }
        System.out.println("\n===START EXIF DATA===");

        for (DirectoryDataPointer ddp : directoryDataPointers) {
            raf.seek(resetPoint);
            raf.skipBytes(ddp.getOffset());
            byte[] data = new byte[ddp.getLength()];
            raf.readFully(data);
            System.out.println(ddp.getType() + ": " + new String(data));
        }

        System.out.println("===END EXIF DATA===");
        raf.close();
        return 0;
    }

    private static int convert(byte... bytes) {
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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Enter a dirname argument");
            System.exit(1);
        }
        File dir = new File(args[0]);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Enter a VALID dirname argument");
            System.exit(1);
        }
        File[] files = dir.listFiles(new JpegFileFilter());

        for(File f : files) {
            try {
                JpegReader.read(f);
            } catch (IOException ex) {
                Logger.getLogger(JpegReader.class.getName()).log(Level.SEVERE, null, ex);
            }
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
