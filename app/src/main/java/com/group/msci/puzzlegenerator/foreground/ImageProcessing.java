/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group.msci.puzzlegenerator.foreground;


import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;


/**
 *
 * @author Reetoo
 */
public class ImageProcessing {
    

    public static Bitmap blur(Bitmap src){
        double data[][] = {{ 0.0625, 0.125, 0.0625},{ 0.125, 0.25, 0.125}, {0.0625, 0.125, 0.0625 }};
        ConvolutionMatrix cm = new ConvolutionMatrix(3);
        cm.applyConfig(data);
        return ConvolutionMatrix.computeConvolution3x3(src, cm);
    }

    
}
