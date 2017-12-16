package com.elf.practice;

public class Billions {
    /*
     14175, 15055, 16616, 17495, 18072, 19390, 19731, 22161, 23320, 23717, 26343, 28725, 29127, 32257, 40020, 41867, 43155, 46298, 56734, 57176, 58306, 61848, 65825, 66042, 68634, 69189, 72936, 74287, 74537, 81942, 82027, 82623, 82802, 82988, 90467, 97042, 97507, 99564
     */

    private final static int[] dudes = {14175, 15055, 16616, 17495, 18072, 19390, 19731, 22161, 23320, 23717, 26343, 28725, 29127, 32257, 40020, 41867, 43155, 46298, 56734, 57176, 58306, 61848, 65825, 66042, 68634, 69189, 72936, 74287, 74537, 81942, 82027, 82623, 82802, 82988, 90467, 97042, 97507, 99564};

    public static void main(String[] args) {
        if (dudes.length != 38) {
            throw new RuntimeException("length is bad");
        }
        int sum = 0;
        for (int num : dudes) {
            sum += num;
        }
        if(sum != 2000000)
            throw new RuntimeException("bad sum");
        
        System.out.printf("Sum is %d\n", sum);
    }
}
