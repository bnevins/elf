/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * given 10 million phone numbers sort them using only 1 MB RAM
 *
 * @author bnevins
 */
public class SorterUsingFiles {

    public static void main(String[] args) {
        try {
            SorterUsingFiles sorter = new SorterUsingFiles();
            System.out.printf("hunk: %d, array: %d\n", HUNKSIZE, ARRAY_SIZE);
        }
        catch (IOException ex) {
            Logger.getLogger(SorterUsingFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final int HUNKSIZE = 1048576;
    private static final String outputFilenameRoot = "C:/temp/filesort_";
    private static final String scratchFilenameRoot = "C:/temp/filesort_interim_";
    private static final int ARRAY_SIZE = HUNKSIZE >> 2;
    private static final int[] ints = new int[ARRAY_SIZE];
    private static final String inputFilename = "C:/temp/10mphonenumbers.txt";
    private LineNumberReader reader;
    private int endIndex = ARRAY_SIZE - 1;
    private int numFiles;
    private int scratchFilecounter = 0;

    // bug -- lefotover crap in last hunk
    // should maintain a pointer to the end of the array.
    public SorterUsingFiles() throws IOException {
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(inputFilename)));

            for (numFiles = 0; true; numFiles++) {
                int numRead = readhunk();
                // off by one mistake?
                if (numRead <= 0) {
                    break;
                }

                endIndex = numRead;
                qsort();
                writehunk(numFiles);
            }
            reader.close();
            merge(numFiles);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SorterUsingFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int readhunk() throws IOException {
        int i;

        for (i = 0; i < ARRAY_SIZE; i++) {
            String numString = reader.readLine();

            if (numString == null) {
                return i;
            }

            ints[i] = Integer.parseInt(numString);
        }
        return i - 1;
    }

    // write the entire array of phone numbers to the file # filenum
    private void writehunk(int filenum) throws IOException {
        //reader = new LineNumberReader(new InputStreamReader(new FileInputStream(inputFilename)));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilenameRoot + filenum)));
        for (int i = 0; i < endIndex; i++) {
            out.println(Integer.toString(ints[i]));
        }
        out.close();
    }

    private int merge(final int prevNumberOfFiles) throws IOException {

        int currNumberOfFiles = 0;

        if (prevNumberOfFiles == 1) {
            return 1; // done!
        }
        for (int i = 0; i < prevNumberOfFiles; i += 2) {
            if (i + 1 >= prevNumberOfFiles) {
                rename(i, currNumberOfFiles);
                return currNumberOfFiles;
            }
            merge(i, i + 1, currNumberOfFiles++);
        }

        return currNumberOfFiles;
    }

    private void qsort() {
        Arrays.sort(ints, 0, endIndex);
    }

    // just rename it!
    private void rename(int oldfilenum, int newfilenum) {
        File oldfile = new File(outputFilenameRoot + oldfilenum);
        File newfile = new File(outputFilenameRoot + newfilenum);

        oldfile.renameTo(newfile);
    }

    private void merge(int first, int second, int out) throws FileNotFoundException, IOException {
        LineNumberReader firstReader = makeReader(first);
        LineNumberReader secondReader = makeReader(second);
        PrintWriter writer = makeWriter(out);
        Integer firstInt = null;
        Integer secondInt = null;
        do {
            if(firstInt == null)
                firstInt = Integer.parseInt(firstReader.readLine());
            if(secondInt == null)
                secondInt = Integer.parseInt(secondReader.readLine());
            if(firstInt < secondInt) {
                writer.println(Integer.toString(firstInt));
                firstInt = null;
            }
            else {
                writer.println(Integer.toString(secondInt));
                secondInt = null;
            }
        } while (true);
    }

    private LineNumberReader makeReader(int i) throws FileNotFoundException {
        String inputFilename = outputFilenameRoot + i;
        new File(inputFilename).renameTo(new File(scratchFilenameRoot + scratchFilecounter++));
        return new LineNumberReader(new InputStreamReader(new FileInputStream(inputFilename)));
    }

    private static PrintWriter makeWriter(int filenum) throws FileNotFoundException, IOException {
        return new PrintWriter(new BufferedWriter(new FileWriter(outputFilenameRoot + filenum)));
    }
}
