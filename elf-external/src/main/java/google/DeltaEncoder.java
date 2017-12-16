/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Q: Implement a simple delta-encoder for signed 32-bit integers; values given
 * may cover the full signed 32 bit range. A sample interface is given here:
 *
 * // Called once at start of encoding. 'fd' represents a pre-opened, //
 * writable file descriptor. You may opt for another I/O interface, // such as
 * FILE*, if desired. Init(int fd);
 *
 * // Add a single i32 to the output stream. Encode(int32 value);
 *
 * // Called once at the end of encoding. The output stream only needs // to be
 * fully valid after this call returns. Finish();
 *
 * @author bnevins
 */
//public class DeltaEncoder {
public class DeltaEncoder {
    public static void main(String[] args) {
        int[] input = new int[] { 100, 1000, 10000, 100000, 100001, 100023, 120023, 199111, 3, 12000 };
        byte[] out = new byte[input.length * 20];
        DeltaEncoder de = new DeltaEncoder(input, out);
        de.Encode();
        de.Finish();
    }
    //DataInputStream in;
    //DataOutputStream out;
    int[] in;
    byte[] out;
    int previous = 0;
    int outcounter = 0;

    DeltaEncoder(int[] in, byte[] out) {
        this.out = out;
        this.in = in;
        System.out.println("" + in.length*4 + " input bytes, " + out.length + " out bytes");
    }

    private void Encode() {
        outcounter = 0;
        for (int nextVal : in) {
            int delta = nextVal - previous;
            previous = nextVal;
            byte outbyte;

            if(outcounter == 198)
                System.out.println("");
            
            while (delta != 0) {
                outbyte = (byte) (delta & 0x7f);
                delta >>>= 7;

                if (delta != 0) {
                    outbyte |= 0x80;
                }
                out[outcounter++] = outbyte;
            }
        }
    }

    public void Finish() {
        System.out.printf("Total bytes in input = %d, Total delta bytes = %d\n", 
                in.length * 4, outcounter);
    }

}
