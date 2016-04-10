/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageSegmentation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Reetoo
 */
public class ImageSegmentation {
    ArrayList<int[]> seeds;
    int[][] regions;
    int[][] links;
    ArrayList<int[]> colors;
    BufferedImage image;
    int threshold = 50;
    
    public ImageSegmentation(String path){
        seeds = new ArrayList<>();
        colors = new ArrayList<>();
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            System.out.println("Could not find image at "+path);
            
        }
        regions = new int[image.getWidth()][image.getHeight()];
    }
    public void setThreshold(int n){
        threshold = n;
    }
    public void addSeed(int x, int y){
        seeds.add(new int[]{x,y});
        regions[x][y] = seeds.size();
        colors.add(new int[]{image.getRGB(x, y),1});
    }
    public void generateSeeds(int x){
        Random r = new Random();
        
        for(int i=0; i<x; i++){
            int xp = r.nextInt(image.getWidth());
            int yp = r.nextInt(image.getHeight());
            addSeed(xp,yp);
        }
        links = new int[x][x];
    }
    public void modColor(int region, int color){
        int[] avg = colors.get(region-1);
        colors.set(region-1,new int[]{((avg[0]*avg[1])+color)/(avg[1]+1),avg[1]+1});
    }
    public void growRegion(){
        int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        System.out.println(regions[50][50]);
        boolean grown = true;
        
        while(grown){
        grown = false;
        ArrayList<int[]> newSeeds = new ArrayList<>();
        
        for(int[] seed: seeds){
            
            int x = seed[0];
            int y = seed[1];
            int region = regions[x][y];
            
            
            
            for(int i = -1; i<2; i++){
                for(int j = -1; j<2; j++){
                    
                    int tx = x+i;
                    int ty = y+j;
                    
                    if((tx>=0 && tx<image.getWidth())&&(ty>=0 && ty<image.getHeight())){
                       
                        if((regions[tx][ty]==0)){
                            
                            final int clr = image.getRGB(tx, ty);
                            final int red = (clr & 0x00ff0000) >> 16;
                            final int green = (clr & 0x0000ff00) >> 8;
                            final int blue = clr & 0x000000ff;
                            
                            final int regionclr = colors.get(region-1)[0];
                            final int rRed = (regionclr & 0x00ff0000) >> 16;
                            final int rGreen = (regionclr & 0x0000ff00) >> 8;
                            final int rBlue = regionclr & 0x000000ff;
                            
                            if((red<rRed+threshold&&red>rRed-threshold)&&(green<rGreen+threshold&&green>rGreen-threshold)&&(blue<rBlue+threshold&&blue>rBlue-threshold)){
                                //System.out.println("hit");
                                //modColor(region,clr);
                                regions[tx][ty] = region;
                                image.setRGB(tx, ty, colors.get(region-1)[0]);
                                newSeeds.add(new int[]{tx,ty});
                                grown = true;
                            }
                            
                        }
                        else if(regions[tx][ty]!=regions[x][y]){
                            int r = regions[tx][ty];
                            int c = regions[x][y];
                            links[r-1][c-1] = 1;
                            //System.out.println(r+" linked with "+c);
                        }
                    }
                    
                }
            }
            
            
            
        }
        seeds = newSeeds;   
        }
        
        for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){
                if(regions[x][y]==0){
                    image.setRGB(x, y, 1);
                }
            }
        
        for(int x=0; x<links.length; x++){
            for(int y=0; y<links[0].length; y++){
                if(links[x][y]!=0){
                    System.out.println(x+" linked with "+y);
                }
            }
        }
        
    }
    public int[][] returnRegions(){
        return regions;
    } 
    public void testImage(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
