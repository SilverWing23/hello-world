package com.zzzxx.shoot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Main extends JPanel{
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage background;
    public static BufferedImage bigplane;
    public static BufferedImage start;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    //定义四种游戏状态
    public final int START=0;
    public final int RUNNING=1;
    public final int PAUSE=2;
    public final int GAME_OVER=3;
    public int state=START;//游戏状态 start running pause gameover

    //静态代码块，是在主方法之前执行（加载类时执行）的，加载
    static{
        try {
            bullet=ImageIO.read(Main.class.getResource("pic/bullet.png" ));
            airplane=ImageIO.read(Main.class.getResource("pic/airplane.png" ));
             hero0=ImageIO.read(Main.class.getResource("pic/hero0.png" ));
             hero1=ImageIO.read(Main.class.getResource("pic/hero1.png" ));
             bee=ImageIO.read(Main.class.getResource("pic/bee.png" ));
            background=ImageIO.read(Main.class.getResource("pic/background.png" ));
            bigplane=ImageIO.read(Main.class.getResource("pic/bigplane.png" ));
            start=ImageIO.read(Main.class.getResource("pic/start.png" ));
            pause=ImageIO.read(Main.class.getResource("pic/pause.png" ));
            gameover=ImageIO.read(Main.class.getResource("pic/gameover.png" ));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //一个英雄机
    Hero hero = new Hero();
    //好多子弹
    List<Bullet> bullets=new ArrayList<Bullet>();
    List<FlyingObject> flys=new ArrayList<FlyingObject>();
    Timer timer = new Timer();

    //控制定时器启动，出现多个物体
    public void action() {
        //设置周期任务，timetask任务，要执行的代码放入/run方法中即可
          //delay是延迟 1000ms
        //period周期性 时间间隔1000ms
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == START) {
                    getGraphics().drawImage(start, 0, 0, null);
                }
                if(state==RUNNING) {
                    //不断生成敌机/小蜜蜂/大敌机
                    createFlyObject();
                    //移动的方法
                    stepAction();
                    new Airplane();
                    new Bee();
                    // 不断生成子弹由英雄机发射
                    shootBullet();
                    //敌机/小蜜蜂/大敌机/飞行物/移动

                    //判断子弹和飞行物的碰撞
                    bangAcition();

                    //判断游戏结束英雄机生命为0
                    gameoverAction();
                    //判断子弹/飞行物越界
                    outOfBounds();
                    //判断英雄机和飞行物的碰撞
                    //界面刷新
                    repaint();//实际上重新调用paint方法

                }
                if (state == PAUSE) {
                    getGraphics().drawImage(pause, 0, 0, null);
                }
                if (state == GAME_OVER) {
                    getGraphics().drawImage(gameover, 0, 0, null);
                }

            }
        }, 10 , 20);//20控制移动的速度
       MouseAdapter adapter=new MouseAdapter(){
           //可以监视到鼠标的单击动作，并且可以执行对应代码
           public void mouseClicked(MouseEvent e) {
               if(state==START){
                   state=RUNNING;
               }
               if(state==GAME_OVER){
                   score = 0;
                   hero = new Hero() {

                       public void step() {

                       }


                       public boolean outOfBounds() {
                           return false;
                       }
                   };
                   state = START;
                   repaint();

               }
           }
           //可以检测鼠标移动动作
           public void mouseMoved(MouseEvent E){
               if(state==RUNNING) {
                   //英雄机和鼠标坐标连在一起
                   hero.setX(E.getX() - hero.getWidth() / 2);
                   hero.setY(E.getY() - hero.getHeight() / 2);
                   //刷新界面
                    repaint();
               }
           }
            //鼠标移出窗口触发
           @Override
           public void mouseExited(MouseEvent e) {
               if(state==RUNNING){
                   state=PAUSE;

               }

           }
           //鼠标进入窗口触发
           @Override
           public void mouseEntered(MouseEvent e) {
               if(state==PAUSE){
                   state=RUNNING;

               }
           }

       };
        //添加鼠标事件监听器
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
        //添加键盘监听器
        KeyAdapter keyAdapter=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //如何判断键盘敲击的是不是p按键
                char c=e.getKeyChar();//q
                if(c=='q'){
                    //游戏退出
                    System.exit(0);
                }

            }
        };
        this.requestFocus();
        this.addKeyListener(keyAdapter);
    }

    //定时器结束
    public void ganmeOver(){
        timer.cancel();

    }
    //下面是生成飞行物的方法
    private int index=0;
    public void createFlyObject(){
        index++;//控制飞行物生成频率
        if(index%50==0){
            int random =(int)(Math.random()*20);
            switch (random){
                case 0:
                    flys.add(new Bee());
                    break;
                case 1:
                case 2:
                case 3:
                    flys.add(new BigPlane());
                default:
                    flys.add(new Airplane());
            }
        }

    }
    public void stepAction(){
        //敌机小蜜蜂大敌机飞行物移动
        for(FlyingObject fly :flys){
            fly.move();
        }
        //子弹移动
        for (Bullet b : bullets){
            b.move();
        }
        //英雄机移动
        hero.move();
    }
    //下面是判断越界
    public void outOfBounds() {
        //for (FlyingObject fly:flys){
        // if (fly.getY()>800){
        //从集合中移除fly
        // flys.remove(fly);
        //下面是迭代器
        Iterator<FlyingObject> flysIterator = flys.iterator();
        while (flysIterator.hasNext()) {
            FlyingObject fly = flysIterator.next();
            if (fly.getY() > 800) {
                //从集合中移除fly对象
                flysIterator.remove();
            }
        }
        Iterator<Bullet> bulletsIterator = bullets.iterator();
        while (bulletsIterator.hasNext()) {
            Bullet bullet = bulletsIterator.next();
            if (bullet.getY() < 0) {
                bulletsIterator.remove();
            }
        }
    }



        




    //下面是发射子弹
    int indexBullet =0;//控制子弹发射频率的计数器
    public void shootBullet(){
        indexBullet++;
        if(indexBullet%16==0){
            bullets.addAll(hero.shoot());
        }

    }
    //判断子弹撞击飞行物
    private void bangAcition(){
        for(int i=0;i<bullets.size();i++){
            bang(bullets.get(i));
        }
    }
    //判断子弹传进去
int score = 0;
private void bang(Bullet bullet){
    for(int i=0;i<flys.size();i++){
        FlyingObject fly=flys.get(i);//airplane bee bigplane→Award Enemy
       boolean isShoot =fly.shootBy(bullet);
       if( isShoot ){
       //撞击到飞行物移除
       flys.remove(i);
       bullets.remove(bullet);

           //1.敌机 加分
    if (fly instanceof Enemy){
        score+=((Enemy)fly).getScore();

    }
           // 2.奖励 加生命值，双倍子弹
    if(fly instanceof  Award){
        int awardType=((Award) fly).getType();
        switch(awardType){
                case Award.DOUBLE_FIRE:
                    hero.doubleFire();
                    break;
                case Award.LIFE:
                    hero.addLife();
                    break;
        }

    }

       break;


       }
        boolean isCrash = fly.crashBy(hero);
        if (isCrash) {
            hero.reduceLife();
            flys.remove(i);
            score += 10;
            repaint();
            break;
        }
    }
}

    //下面时判断游戏是否结束
    private void gameOverAction(){
        timer.cancel();
    }


    private void gameoverAction(){
        if(hero.getLife()==0){
            state=GAME_OVER;
        }
        if (state == GAME_OVER) {
            score = 0;
        }


    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);//调用父类的paint方法
     //自定义绘画内容
        g.drawImage(background,0,0,null);
        //画英雄机
        g.drawImage(hero.getImage(),hero.getX(),hero.getY(),null);
       //画多个飞行物
        for(FlyingObject fly :flys){
            g.drawImage(fly.getImage(),fly.getX(),fly.getY(),null);
        }
        //画好多子弹
        for (Bullet bullet:bullets){
            g.drawImage(bullet.getImage(),bullet.getX(),bullet.getY(),null);
        //设置字体及大小
            g.setColor(new Color(16711680));
            g.setFont(new Font("SansSerif", 1, 24));

            //画分数和生命值
            g.drawString("生命值"+hero.getLife(),10,20);
            g.drawString("分数值"+score,10,40);
         //画游戏状态图片
        switch(state){
            case  START:
                g.drawImage(start,0,0,null);
                break;
            case PAUSE:
                g.drawImage(pause,0,0,null);
                break;
            case GAME_OVER:;
                g.drawImage(gameover,0,0,null);
                break;
        }


        }
    }
    //用来窗口显示
    public void showMe(){
        //1.创建窗口对象
        JFrame window =new JFrame("飞机大战");
        //3.设置窗口大小
        window.setSize(400,600);
        //4.设置窗口显示位置
        window.setLocationRelativeTo(null);
        //5.设置关闭窗口时退出程序(默认关闭选项)
        //特定选项 静态常量
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //6.新建画板对象 JPanel 原声jdk的jpanael 不能在使用时加载，需要提前读取
       // Main panel=new Main();
        //7.将画板对象添加到窗口对象上
        window.add(this);
        //2.显示窗口
        window.setVisible(true);

        this.action();
        
    }

    public static void main(String[] args){
        Main main= new Main();
        main.showMe();
        main.action();




    }
}
