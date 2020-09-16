/**
 * @author Bryan Xu, Brian Wu
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

    //player1 is top, player2 is bottom
    public int player; 
    public int speed = 7;
    
    public int angle;
    public int bulletSpeed = 6;
    public ArrayList<Bullet> bullets;
    
    
    
    public int MAX_CAPACITY = 300;
    public double currentCapacity = MAX_CAPACITY;
    public Rectangle block ;
    
    public int MAX_AMMO = 100000;
    public int cleanness = MAX_AMMO;
    public Rectangle currentAmmo;
    
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
        int space = frameHeight/4;
        if(player==1){
            space = -space;
        }
        
        body = new Rectangle(frameWidth/2,frameHeight/2+space,bodyWidth,bodyHeight);
        block = new Rectangle((int)body.getX(),(int)(body.getY()+bodyHeight-(currentCapacity*bodyHeight/MAX_CAPACITY)),(int)bodyWidth,(int)(currentCapacity*bodyHeight/MAX_CAPACITY));
        
        
        if(player==1){
            currentAmmo = new Rectangle(0,0,(int)(cleanness*frameWidth/MAX_AMMO),50);
            angle = 90;
        }
        if(player==2){
            currentAmmo = new Rectangle(0,frameHeight-50,(int)(cleanness*frameWidth/MAX_AMMO),50);
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
        if(currentCapacity>0){
            Bullet bul = new Bullet(body.x+bodyWidth/2,body.y+bodyHeight/2,bulletSize,3*bulletSize, angle, bulletSpeed);
            bullets.add(bul);
            bul.start();
            currentCapacity -= 1;
        }
    }

    public void replenish(double num){ 
        currentCapacity += num;
    }

    public void shootBullets(){    
        for(int i = 0; i<bullets.size(); i++){
            if(player==1){
                bullets.get(i).move();
            }else{
                bullets.get(i).move();
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

    public void update(){
        block = new Rectangle((int)body.getX(),(int)(body.getY()+bodyHeight-(currentCapacity*bodyHeight/MAX_CAPACITY*9/10)),(int)bodyWidth,(int)(currentCapacity*bodyHeight/MAX_CAPACITY*9/10));
        if(player==1){
            currentAmmo = new Rectangle(0,0,(int)(cleanness*frameWidth/MAX_AMMO),50);
        }
        if(player==2){
            currentAmmo = new Rectangle(0,frameHeight-60,(int)(cleanness*frameWidth/MAX_AMMO),50);
        }
    }
    
    public void changeAngleBy(int ang){
        angle += ang;
    }
}
