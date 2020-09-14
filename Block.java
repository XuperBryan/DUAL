
/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Block extends JComponent
{
    public Rectangle body;
    public int bodyWidth;
    public int bodyHeight;
    public int frameWidth;
    public int frameHeight;
    public int player;//player1 is top, player2 is bottom, 
    public int speed = 7;
    //public int boundary;
    //public int lives = 10;
    
    public int angle;
    public int bulletSpeed = 6;
    public ArrayList<Bullet> bullets;
    //public ArrayList<Integer> rotations;
    
    
    
    public int MAX_BLADDER = 300;
    public double bladder = MAX_BLADDER;
    public Rectangle peeCapacity ;
    
    public int MAX_CLEANNESS = 100000;
    public int cleanness = MAX_CLEANNESS;
    public Rectangle peeCleanness ;
    
    public boolean isABot;
    public boolean botCanShoot;
    public boolean shot = false;
    public boolean shot2 = false; 
    public Block(int frameWidth, int frameHeight, int bodyWidth, int bodyHeight, int player){
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.bodyWidth = bodyWidth;
        this.bodyHeight = bodyHeight;
        this.player = player;
        bullets = new ArrayList<Bullet>();
        //rotations = new ArrayList<Integer>();
        int space = frameHeight/4;
        if(player==1){
            space = -space;
        }
        
        body = new Rectangle(frameWidth/2,frameHeight/2+space,bodyWidth,bodyHeight);
        peeCapacity = new Rectangle((int)body.getX(),(int)(body.getY()+bodyHeight-(bladder*bodyHeight/MAX_BLADDER)),(int)bodyWidth,(int)(bladder*bodyHeight/MAX_BLADDER));
        
        
        if(player==1){
            peeCleanness = new Rectangle(0,0,(int)(cleanness*frameWidth/MAX_CLEANNESS),50);
            angle = 90;
            //cleanness = 550000;
        }
        if(player==2){
            peeCleanness = new Rectangle(0,frameHeight-50,(int)(cleanness*frameWidth/MAX_CLEANNESS),50);
            angle = 270;
        }
        
        isABot = false;
        botCanShoot = true;
    }

    public void moveUp(){
        if(player==1){
            if(body.getY()>=0){
                body.translate(0,-speed);
            }
        }
        if(player==2){
            if(body.getY()>=0+frameHeight/2){
                body.translate(0,-speed);
            }
        }
        repaint();
    }

    public void moveDown(){
        if(player==1){
            if(body.getY()+bodyHeight<=frameHeight/2){
                body.translate(0,speed);
            }
        }
        if(player==2){
            if(body.getY()+bodyHeight<=0+frameHeight){
                body.translate(0,speed);
            }
        }
        repaint();
    }

    public void moveLeft(){
        if(body.getX()>=0){
            body.translate(-speed,0);
        } 
        repaint();
    }

    public void moveRight(){
        if(body.getX()+bodyWidth<=frameWidth){
            body.translate(speed,0);
        } 
        repaint();
    }

    public void shoot(){
        int bulletSize = 4;
        if(bladder>0){
            //Rectangle rect = new Rectangle(body.x+bodyWidth/2,body.y+bodyHeight/2,bulletSize,3*bulletSize); 
            Bullet bul = new Bullet(body.x+bodyWidth/2,body.y+bodyHeight/2,bulletSize,3*bulletSize, angle, bulletSpeed);
            bullets.add(bul);
            bul.start();
            //rotations.add(angle);
            bladder -= 1;
            //peeCapacity = new Rectangle((int)body.getX(),(int)body.getY(),(int)bodyWidth,(int)(bladder*bodyHeight/100));
        }
    }

    public void replenishBladder(double num){ 
        bladder += num;
    }

    public void shootBullets(){
        //bulletSpeed = 4;
        //angle = 30;
        
        for(int i = 0; i<bullets.size(); i++){
            if(player==1){
                bullets.get(i).move();
            }else{
                bullets.get(i).move();
                //rects.get(i).translate((int)(bulletSpeed*Math.sin(Math.toRadians(rotations.get(i)))),(int)(bulletSpeed*Math.cos(Math.toRadians(rotations.get(i)))));
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g ;
        if(player==1){
            g2.setColor(Color.orange);
        }else{
            g2.setColor(Color.green);
        }
        g2.fill(body);
        g2.setColor(Color.yellow);
        for(Bullet b:bullets){
            g2.fill(b.body);
        }
        
    }

    public void updatePee(){
        peeCapacity = new Rectangle((int)body.getX(),(int)(body.getY()+bodyHeight-(bladder*bodyHeight/MAX_BLADDER*9/10)),(int)bodyWidth,(int)(bladder*bodyHeight/MAX_BLADDER*9/10));
        if(player==1){
            peeCleanness = new Rectangle(0,0,(int)(cleanness*frameWidth/MAX_CLEANNESS),50);
        }
        if(player==2){
            peeCleanness = new Rectangle(0,frameHeight-60,(int)(cleanness*frameWidth/MAX_CLEANNESS),50);
        }
    }
    
    public void changeAngleBy(int ang){
        angle += ang;
        //System.out.println(angle);
    }
}






