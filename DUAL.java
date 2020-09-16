/**
 * @author Bryan Xu, Brian Wu 
 */

import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
public class DUAL extends JComponent
{
    public Block p1;
    public Block p2;
    public JFrame frame;
    public int frameHeight;
    public int frameWidth;
    public int playerSize = 100;

    public static ArrayList<KeyEvent> keys = new ArrayList<KeyEvent>();
    public DUAL(int frameWidth, int frameHeight)
    {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        p1 = new Block(frameWidth,frameHeight,playerSize,playerSize,1);
        p2 = new Block(frameWidth,frameHeight,playerSize,playerSize,2);

    }

    public void shootBullets(){
        p1.shootBullets();
        p2.shootBullets();
        repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Rectangle border = new Rectangle(0,frameHeight/2,frameWidth,3);
        g2.setColor(Color.magenta);
        g2.fill(border);

        g2.setColor( new Color(224, 125, 20));
        g2.fill(p1.body);
        g2.setColor(Color.green);
        g2.fill(p2.body);

        g2.setColor(new Color(66, 134, 244));
        g2.fill(p1.block);
        g2.fill(p2.block);

        g2.setColor(Color.red);
        g2.fill(p1.currentAmmo);
        g2.setColor(Color.red);
        g2.fill(p2.currentAmmo);

        shootBullets();
        Rectangle r3 = p1.body.intersection(p2.body);
        g2.setColor(Color.MAGENTA);
        g2.fill(r3);

        if(p1.isABot==false){
            g2.setColor(Color.white);
            Rectangle aim1 = new Rectangle((int)(p1.body.getX()+p1.body.getWidth()/2),(int)(p1.body.getY()+p1.body.getHeight()/2),2, 100);
            AffineTransform transform1 = new AffineTransform();
            transform1.rotate(Math.toRadians(p1.angle+270),aim1.getX(), aim1.getY());
            Shape transformed1 = transform1.createTransformedShape(aim1);
            g2.fill(transformed1);
        }
        g2.setColor(Color.white);
        Rectangle aim2 = new Rectangle((int)(p2.body.getX()+p2.body.getWidth()/2),(int)(p2.body.getY()+p2.body.getHeight()/2),2, 100);
        AffineTransform transform2 = new AffineTransform();
        transform2.rotate(Math.toRadians(p2.angle+270),aim2.getX(), aim2.getY());
        Shape transformed2 = transform2.createTransformedShape(aim2);
        g2.fill(transformed2);

        Rectangle flash1 = new Rectangle(0,0,frameWidth, frameHeight/2);
        int f1 = 0;
        Rectangle flash2 = new Rectangle(0,frameHeight/2,frameWidth, frameHeight/2);
        int f2 = 0;

        for(Bullet bul : p1.bullets){
            g2.setColor(Color.yellow);
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(bul.angle+90), bul.body.getX() + bul.body.width/2, bul.body.getY() + bul.body.height/2);
            Shape transformed = transform.createTransformedShape(bul.body);
            g2.fill(transformed);
            if(bul.body.intersects(p2.body)){
                Rectangle bulletTouched = bul.body.intersection(p2.body); 
                int bulletCollide = (int)(bulletTouched.getWidth()*bulletTouched.getHeight());
                p2.cleanness -= bulletCollide;
                g2.draw(p2.body);
                f2++;
            }
        }
        for(Bullet bul : p2.bullets){
            g2.setColor(Color.yellow);
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(bul.angle+90), bul.body.getX() + bul.body.width/2, bul.body.getY() + bul.body.height/2);
            Shape transformed = transform.createTransformedShape(bul.body);
            g2.fill(transformed);
            if(bul.body.intersects(p1.body)){
                Rectangle bulletTouched = bul.body.intersection(p1.body); 
                int bulletCollide = (int)(bulletTouched.getWidth()*bulletTouched.getHeight());
                p1.cleanness -= bulletCollide;
                g2.draw(p1.body);

                f1++;
            }
        }
        g2.setColor(new Color(201, 22, 22, f1*4));
        g2.fill(flash1);
        g2.setColor(new Color(239, 226, 40, f2*4));
        g2.fill(flash2);
        if(getCurrentLife()==3){
            g2.setColor(Color.YELLOW);
            g2.fill(p2.body);
            g2.setColor(Color.YELLOW);
            g2.fill(p1.body);
        }
        if(getCurrentLife()==2){
            g2.setColor(Color.YELLOW);
            g2.fill(p2.body);
        }
        if(getCurrentLife()==1){
            g2.setColor(Color.YELLOW);
            g2.fill(p1.body);
        }

        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if(p1.body.intersects(p2.body)){
            g.setFont(new Font("Open Sans", Font.PLAIN, 200));
            g2.setColor(Color.RED);
        }else{
            g.setFont(new Font("Open Sans", Font.PLAIN, 200));
            g2.setColor(Color.black);
        }
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("How many players? (1/2)");
        int players = in.nextInt();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        DUAL game = new DUAL(frameWidth,frameHeight);
        if(players==1){
            game.p1.isABot = true;
        }else{
            game.p1.isABot = false;  
        }
        frame.getContentPane().setBackground(Color.black);
        frame.getContentPane().add(game);   
        frame.add(game);
        game.repaint();
        frame.addKeyListener(new KeyAdapter(){
                @Override
                public void keyPressed(KeyEvent e){
                    if(!hasKey(e)){
                        keys.add(e);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e){
                    removeKey(e);
                }
            });

        class TimerListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e){
                boolean p1fired = false;
                boolean p2fired = false;
                for(KeyEvent ee : keys){
                    if(ee.getKeyCode() == KeyEvent.VK_W){
                        game.p1.moveUp();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_S){
                        game.p1.moveDown();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_A){
                        game.p1.moveLeft();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_D){
                        game.p1.moveRight();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_UP){
                        game.p2.moveUp();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_DOWN){
                        game.p2.moveDown();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_LEFT){
                        game.p2.moveLeft();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_RIGHT){
                        game.p2.moveRight();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_3 || ee.getKeyCode() == 99){                        
                        p2fired = true;
                        game.p2.changeAngleBy(2);
                        game.p2.shoot();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_2 || ee.getKeyCode() == 98){                
                        p2fired = true;
                        game.p2.shoot();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_1 || ee.getKeyCode() == 97){            
                        p2fired = true;
                        game.p2.changeAngleBy(-2);
                        game.p2.shoot();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_V){
                        p1fired = true;
                        game.p1.changeAngleBy(2);
                        game.p1.shoot();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_B){
                        p1fired = true;
                        game.p1.shoot();
                        game.repaint();
                    }
                    if(ee.getKeyCode() == KeyEvent.VK_N){
                        p1fired = true;
                        game.p1.changeAngleBy(-2);
                        game.p1.shoot();
                        game.repaint();
                    }
                }
                game.repaint();
                frame.revalidate();
                frame.repaint();

                if(game.p1.isABot){
                    game.botMove(1);
                    if(game.p1.currentCapacity==300){
                        game.p1.botCanShoot = true;
                    }
                    if(game.p1.botCanShoot){
                        if(game.p1.currentCapacity>0){
                            game.botShoot(1);
                            p1fired = true;
                        }else{
                            game.p1.botCanShoot = false;
                            p1fired = false;
                        }
                    }
                    if(!game.p1.shot&&game.p1.cleanness<50000){
                        game.botSuperMove(1);
                        game.p1.shot = true;
                    }
                    if(!game.p1.shot2&&game.p1.cleanness<20000){
                        game.botSuperMove(1);
                        game.p1.shot2 = true;
                    }
                }
                if(game.p2.isABot){
                    game.botMove(2);
                    game.botShoot(2);
                }

                if(!p1fired&&game.p1.currentCapacity<300){
                    game.p1.replenish(1);
                }
                if(!p2fired&&game.p2.currentCapacity<300){
                    game.p2.replenish(5);
                } 

                game.p1.update();
                game.p2.update();
            }
        }
        ActionListener listener = new TimerListener();
        Timer t = new Timer(10,listener);
        t.start();

        game.repaint();
        frame.revalidate();
        frame.repaint();
    }

    private static boolean hasKey(KeyEvent e){
        for(KeyEvent ee : keys){
            if(ee.getKeyCode() == e.getKeyCode()){
                return true;
            }
        }
        return false;
    }

    private static void removeKey(KeyEvent e){
        for(int i = 0; i<keys.size(); i++){
            if(keys.get(i).getKeyCode() == e.getKeyCode()){
                keys.remove(i);
                return;
            }
        }
    }

    private int getCurrentLife(){
        if(p1.cleanness<=0&&p2.cleanness<=0){
            return 3;
        }
        if(p2.cleanness<=0){
            return 2;
        }
        if(p1.cleanness<=0){
            return 1;
        }
        return 0;
    }

    public void botMove(int player){
        if(player==1){
            int[] moveScores = new int[8];
            for(int i = 0; i<moveScores.length; i++){
                moveScores[i] = 0;
                Rectangle temp = new Rectangle((int)(p1.body.getX()),(int)(p1.body.getY()), (int)(p1.body.getWidth()), (int)(p1.body.getHeight()));
                switch(i){
                    case 0: 
                    if(temp.getX()+p1.bodyWidth<=p1.frameWidth){//right
                        temp.translate(p1.speed,0);
                    } 
                    break;
                    case 1: 
                    if(temp.getX()+p1.bodyWidth<=p1.frameWidth){//right
                        temp.translate(p1.speed,0);
                    } 
                    if(temp.getY()+p1.bodyHeight<=p1.frameHeight/2){//down
                        temp.translate(0,p1.speed);
                    }
                    break;
                    case 2: 
                    if(temp.getY()+p1.bodyHeight<=p1.frameHeight/2){//down
                        temp.translate(0,p1.speed);
                    } 
                    break;
                    case 3: 
                    if(temp.getX()>=0){                              //left
                        temp.translate(-p1.speed,0);
                    } 
                    if(temp.getY()+p1.bodyHeight<=p1.frameHeight/2){//down
                        temp.translate(0,p1.speed);
                    }
                    break;
                    case 4: 
                    if(temp.getX()>=0){                              //left
                        temp.translate(-p1.speed,0);
                    } 
                    break;
                    case 5: 
                    if(temp.getX()>=0){                              //left
                        temp.translate(-p1.speed,0);
                    } 
                    if(temp.getY()>=0){                              //up
                        temp.translate(0,-p1.speed);
                    }
                    break;
                    case 6: 
                    if(temp.getY()>=0){                              //up
                        temp.translate(0,-p1.speed);
                    } 
                    break;
                    case 7: 
                    if(temp.getX()+p1.bodyWidth<=p1.frameWidth){//right
                        temp.translate(p1.speed,0);
                    }
                    if(temp.getY()>=0){                              //up
                        temp.translate(0,-p1.speed);
                    }
                    break;
                }
                int dmg0 = getDmg(player,temp, 0);
                moveScores[i]-=dmg0;
            }
            int maxValue = moveScores[0];
            for(int i = 0; i<moveScores.length; i++){
                if(moveScores[i]>maxValue){
                    maxValue = moveScores[i];
                }
            }
            /*
            int maxes = 0;
            for(int i = 0; i<moveScores.length; i++){
            if(moveScores[i]==maxValue){
            maxes++;
            }
            }
             */
            ArrayList<Integer> maxIndexes = new ArrayList<Integer>();
            for(int i = 0; i<moveScores.length; i++){
                if(moveScores[i]==maxValue){
                    maxIndexes.add(i);
                }
            }

            int ranNum = (int)(Math.random()*maxIndexes.size());

            int maxIndex = maxIndexes.get(ranNum);

            switch(maxIndex){
                case 0: 
                p1.moveRight();
                break;
                case 1: 
                p1.moveRight();
                p1.moveDown();
                break;
                case 2: 
                p1.moveDown();
                break;
                case 3: 
                p1.moveLeft();
                p1.moveDown();
                break;
                case 4: 
                p1.moveLeft();
                break;
                case 5: 
                p1.moveLeft();
                p1.moveUp();
                break;
                case 6: 
                p1.moveUp(); 
                break;
                case 7: 
                p1.moveRight();
                p1.moveUp();
                break;
            }
        }
    }

    public int getDmg(int player, Rectangle temp, int interval){
        if(player==1){
            int dmg = 0;
            switch(interval){
                case 0: 
                for(Bullet bul : p2.bullets){
                    if(bul.body.intersects(temp)){
                        Rectangle bulletTouched = bul.body.intersection(temp); 
                        int bulletCollide = (int)(bulletTouched.getWidth()*bulletTouched.getHeight());
                        dmg += bulletCollide;
                    }
                }
                break;
                case 1: 
                for(Bullet bul : p2.bullets){
                    Rectangle bulInInt = new Rectangle(bul.getX()+bul.xVelocity.get(0), bul.getY()+bul.yVelocity.get(0), (int)bul.body.getWidth(), (int)bul.body.getHeight());
                    if(bulInInt.intersects(temp)){
                        Rectangle bulletTouched = bulInInt.intersection(temp); 
                        int bulletCollide = (int)(bulletTouched.getWidth()*bulletTouched.getHeight());
                        dmg += bulletCollide;
                    }
                }
                break;
            }
            return dmg;
        }
        return -1;
    }

    public void botShoot(int player){
        if(player ==1){
            double deltaX = (p2.body.getX()-p1.body.getX());
            double deltaY= (p2.body.getY()-p1.body.getY());
            double theta = Math.atan(deltaX/deltaY);
            theta = Math.toDegrees(theta)+270;
            p1.angle = (int)-theta;
            p1.shoot();
            repaint();
        }
    }

    public void botSuperMove(int player){
        if(player ==1){
            for(int ang = 0; ang<360; ang+=5){
                p1.angle = ang;
                p1.shoot();
            }
        }
    }
}
