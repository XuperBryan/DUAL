/**
 * @author Bryan Xu, Brian Wu 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
public class Bullet extends JComponent
{
    public Rectangle body;
    public int angle;
    public int velocity; 
    public ArrayList<Integer> xVelocity;
    public ArrayList<Integer> yVelocity;

    public int constant = 1000;

    public Bullet(int rX, int rY, int rWidth, int rHeight, int ang, int vel){
        body = new Rectangle(rX, rY, rWidth, rHeight);
        angle = ang;
        velocity = vel;
        xVelocity = new ArrayList<Integer>();
        yVelocity = new ArrayList<Integer>();
    }

    public void start(){
        int totalX = (int)(constant*velocity*Math.cos(Math.toRadians(angle)));
        int aX = (int)(totalX/constant);
        int bX = aX+1;
        int numOfaX;
        int numOfbX;
        numOfbX = (totalX-aX*constant);
        numOfaX = constant-numOfbX;
        for(int i = 0; i<constant; i++){
            if(i<numOfaX){
                xVelocity.add(aX);
            }else{
                xVelocity.add(bX);
            }
        }
        int totalY = (int)(constant*velocity*Math.sin(Math.toRadians(angle)));
        int aY = (int)(totalY/constant);
        int bY = aY+1;
        int numOfaY;
        int numOfbY;
        numOfbY = (totalY-aY*constant);
        numOfaY = constant-numOfbY;
        for(int i = 0; i<constant; i++){
            if(i<numOfaY){
                yVelocity.add(aY);
            }else{
                yVelocity.add(bY);
            }
        }
    }

    public void move(){
        int ranIndex = (int)(Math.random()*xVelocity.size());
        if(xVelocity.size()>0){
            int xMove = xVelocity.get(ranIndex);
            xVelocity.remove(ranIndex);
            int yMove = yVelocity.get(ranIndex);
            yVelocity.remove(ranIndex);
            body.translate(xMove, yMove);
        }
    }
}
