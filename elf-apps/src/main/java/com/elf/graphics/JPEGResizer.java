package com.elf.graphics;

import java.io.*;

class JPEGResizer {

    public static void main(String[] args) {
        if (args.length < 3) {
            usage();
        }

        if (args[0].toLowerCase().equals("-d")) {
            File f = new File(args[1]);

            if (!f.exists() || !f.isDirectory()) {
                System.out.println("Bad Directory argument: " + args[1] + "\n\n");
                usage();
            }

            File[] jpegs = f.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    String name = pathname.getName().toLowerCase();

                    if (name.endsWith("_tn.jpg")) {
                        return false;
                    }
                    if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".jpe")) {
                        return true;
                    }

                    return false;
                }
            });

            for (int i = 0; i < jpegs.length; i++) {
                String in = jpegs[i].getPath();
                String out = makeThumbnailName(in);
                System.out.println("Processing " + out);
                if (args.length == 3) {
                    JPEGUtils.createThumbnail(in, out, args[2]);
                }

                if (args.length == 4) {
                    JPEGUtils.createThumbnail(in, out, args[2], args[3]);
                }
            }
        } else if (args.length == 3) {
            JPEGUtils.createThumbnail(args[0], args[1], args[2]);
        } else if (args.length == 4) {
            JPEGUtils.createThumbnail(args[0], args[1], args[2], args[3]);
        }
    }

    //////////////////////////////////////////////////////////////	
    private static String makeThumbnailName(String in) {
        int index = in.lastIndexOf('.');

        if (index < 0) {
            System.out.println("INTERNAL ERROR 001");
            usage();
        }

        String out = in.substring(0, index);
        out += "_tn.jpg";
        return out;
    }

    //////////////////////////////////////////////////////////////	
    public static void usage() {
        System.out.println("\n\nUsage:\njava com.elf.graphics.JPEGResizer input-filename output-filename scaling-factor");
        System.out.println("\nJPEGResizer reads in the input-file, resizes it and writes the resulting image to the output-filename.");
        System.out.println("The scaling factor can be either an integer or a floating point number (i.e. contains a dot).");
        System.out.println("Integer scaling factor:  The longest dimension of the new image will be this integer pixels.");
        System.out.println("Floating point scaling factor:  The dimensions of the image will be multiplied by this factor.");
        System.out.println("\nExamples -- assume the input-file is 1800x1200 pixels:");
        System.out.println("\nJPEGResizer filename 0.5 --> creates a 900x600 image");
        System.out.println("\nJPEGResizer filename 2.0 --> creates a 3600x2400 image");
        System.out.println("\nJPEGResizer filename 450 --> creates a 450x300 image");
        System.out.println("NEW FEATURE (June 2003): JPEGResizer input output width height --> make sure either width or height is zero");
        System.out.println("NEW FEATURE (June 2003): JPEGResizer -d <dir-name> width height");
        System.out.println("NEW FEATURE (June 2003): JPEGResizer -d <dir-name> scale");
        System.exit(0);
    }

    //////////////////////////////////////////////////////////////	
}
