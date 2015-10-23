package com.example.jessejohnson.cardboardtext;

/**
 * Created by Jesse Johnson on 10/19/2015.
 */
public class AsciiActivity {

    //private char[] ascii = "  .-',;^+\"|)>iv%xcr{*I?![1t7juT#wfy325Fp6mSghd4EgXZkA&8$@W0".toCharArray();
    private char[] ascii = "#@%$X&VU*+>})/:,.^- ".toCharArray();
    //private char[] ascii = "#X&U+C/:- ".toCharArray();
    //private char[] ascii = "#/- ".toCharArray();

    private int thresh = 220;

    private int resY = 16;
    private int resX = 15;
    private int x = 0;
    private int y = 0;
    private int k = 0;

    public void setResX(int x){
        resX = x;
    }
    public int getResY(){
        return resY;
    }
    public int getResX(){
        return resX;
    }
    private char get (float l){
        // Log.i("l", Float.toString(l));
        int i = (int)((l / 255) * ascii.length);
        if ( i == ascii.length)
            i -= ascii.length;
        return ascii[i];
    }

    public String convert(int[] l, int image_width, int image_height){
        float pixel_chunk;

        String output = "";
        int[] temp = new int[resX*resY];
        for (int v = 0; v < (image_height/resY); ++v){
            for (int u = 0; u < (image_width/resX); ++u) {
                for (int j = 0; j < resY; ++j) {
                    for (int i = 0; i < resX; ++i) {

                        temp[k] = l[(y + j) * image_width + (x + i)];
                        ++k;
                    }
                }
                x += resX;
                k = 0;
                pixel_chunk = average(temp);
                output += get(pixel_chunk);
            }
            x = 0;
            y += resY;
            // output = output.substring(0, output.length() - 2) + "|\n";
            output += "\n";
        }
        x = 0;
        y = 0;
        k = 0;
        //System.out.println(output);
        //output += "END\n";
        return output;
    }

    private float average(int array[])
    {
        float total = 0.0f;
        for(int i = 0; i < array.length; ++i)
        {
            total += array[i];
        }

        return total / array.length;
    }
}
