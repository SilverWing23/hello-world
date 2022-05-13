package com.zzzxx.shoot;
/**
 *子弹
 */

public class Bullet extends FlyingObject{
    public Bullet(int x,int y){
     image = Main.bullet;
        width=image.getWidth();
        height=image.getHeight();
        //游戏运行时子弹的x和y取决于飞机
     this.x=x;//和飞机的x+width/2一致
     this.y=y;//和飞机的一致
     speed=4;


    }



    public void move(){

        y-=speed;
    }
}
