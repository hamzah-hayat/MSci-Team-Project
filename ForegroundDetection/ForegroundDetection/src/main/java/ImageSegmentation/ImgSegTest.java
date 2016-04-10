/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageSegmentation;

/**
 *
 * @author Reetoo
 */
public class ImgSegTest {
    
    
    public static void main(String[] args){
        
        ImageSegmentation ig = new ImageSegmentation("assets/apple.jpg");
        //ig.addSeed(25, 25);
        //ig.addSeed(46,27);
        //ig.addSeed(400, 400);
       // ig.addSeed(500,400);
        //ig.addSeed(500, 500);
        //ig.addSeed(600,600);
        ig.generateSeeds(10);
        ig.growRegion();
        ig.testImage();
    
    }
    
    
}
