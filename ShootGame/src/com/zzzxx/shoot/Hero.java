package com.zzzxx.shoot;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *英雄机
*/
public class Hero extends FlyingObject{
    private int life;
    private int count=0;
    private int double_fire=0;//双倍子弹

    public Hero(){
        life=3;
        x=200;
        y=300;
        image =Main.hero0;
        width=image.getWidth();
        height=image.getHeight();
    }
    //英雄机发射子弹
    public List<Bullet> shoot(){
        List<Bullet> list=new ArrayList<Bullet>();
        //判断有没有双倍子弹火力
        if(double_fire>0){
            list.add(new Bullet(x+width/4,y));
            list.add(new Bullet(x+width*3/4,y));
            double_fire-=2;
        }
        else{
            list.add(new Bullet(x+width/2,y));
        }
        return list;


    }
    public void reduceLife(){
        life-=1;
    }





    //设置英雄机运动的频率

    public void move(){
        //切换图片
        BufferedImage[] images = {Main.hero0,Main.hero0};
        image = images[count++%2];

    }
    public void addLife(){
        life++;
    }
    public void doubleFire(){
        double_fire+=20;

    }
    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean hit(FlyingObject obj){
        int x1=this.x-this.width/2;
        int x2=this.x+this.width/2;
        int y1=this.y-this.height/2;
        int y2=this.y+this.height/2;
        if(obj.x<x2&&obj.x>x1&&obj.y<y2&&obj.y>y1){
            return true;
        }else{
            return false;
        }
    }


}
