/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group.msci.puzzlegenerator.foreground;





import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 *
 * @author Reetoo
 */
public class ForegroundDetection {
    int[][] regions;
    int[][] foreground;
    int background = Color.WHITE;
    boolean outline = false;
    BasicNetwork network;
    Bitmap image;
    int numberOfRegions;
    double threshold = 0.8;
    
    public ForegroundDetection(InputStream f){
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(f);
    }
    public void setImage(Bitmap b) throws IOException{
        
        image = Bitmap.createBitmap(b);
        foreground = new int[image.getWidth()][image.getHeight()];
    }
    public void setBackground(int b){
        background = b;
    }
    public void setOutline(boolean b){
        outline = b;
    }
    public Bitmap getForeground(Bitmap b) throws IOException {

        setImage(b);
        getForegroundPixels(false);
        generateRegions();
        thresholdRegions();
        return image;

    }
    public Bitmap getForegroundNoMerge(Bitmap b) throws IOException {
        setImage(b);
        getForegroundPixels(true);
        return image;
    }

    public void getForegroundPixels(boolean b){
        
        
        double[][] inputs = new double[image.getWidth()][3];
        double[][] outputs = new double[image.getWidth()][1];
        MLDataSet trainingSet = null;
        ResilientPropagation train = null;
        
        for (int y = 0; y < image.getHeight(); y++) {
           
            
            
            for (int x = 0; x < image.getWidth(); x++) {
                final int clr = image.getPixel(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                inputs[x] = new double[]{1,red, green, blue, red*red, green*green, blue*blue, red*green, red*blue, green*blue};
                    
                    //System.out.println("Hello");
                    
                
                
                
               
                
                
            }
            
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             int i = 0;
             for(MLDataPair pair: trainingSet){
                    final MLData output = network.compute(pair.getInput());
                    //System.out.println(Arrays.toString(output.getData()));
                    if(output.getData(0)>=threshold){

                        foreground[i][y] = 1;
                        if(b){
                            if(outline){
                                image.setPixel(i,y, Color.WHITE);
                            }
                        }
                        //System.out.println(Arrays.toString(output.getData()));
                    }
                    else{
                        if(b)
                        image.setPixel(i, y, background);
                        //System.out.print("O");
                    }
                    i++;
            //System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
            }
             //System.out.println();

        }
        Encog.getInstance().shutdown();
    }
    public void thresholdRegions(){
        

        int[][] regionHist = new int[numberOfRegions][2];
        
        for(int x=0; x<regions.length; x++){
            for(int y=0; y<regions[0].length; y++){
                if(foreground[x][y]!=0)
                    regionHist[regions[x][y]][1]++;
                regionHist[regions[x][y]][0]++;
            }
        }
        for(int[] i: regionHist){
            //System.out.println(Arrays.toString(i));
        }
        for(int x=0; x<regions.length; x++){
            for(int y=0; y<regions[0].length; y++){
                
                double perc = regionHist[regions[x][y]][1];
                perc = perc/regionHist[regions[x][y]][0];
                


                if(perc<0.2){
                    image.setPixel(x, y, background);
                    //System.out.println(perc);
                }
                else{
                    if(outline){
                        image.setPixel(x,y, Color.WHITE);
                    }
                }


                
            }
        }
    }
    public void generateRegions(){
        image = ImageProcessing.blur(image);
        ImageSegmentation ig = new ImageSegmentation(image);
        ig.generateSeeds(500);
        ig.setThreshold(50);
        ig.setMergeThreshold(20);
        
        numberOfRegions = ig.growRegion();
        regions = ig.returnRegions();
    }
    
    
}
