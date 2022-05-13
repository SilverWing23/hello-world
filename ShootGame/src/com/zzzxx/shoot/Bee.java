package com.zzzxx.shoot;
/**
 *小蜜蜂 奖励 不同类型
 */
public class Bee extends FlyingObject implements Award{
    private int xSpeed;
    private int awardType;//奖励类型

    public Bee() {
        image= Main.bee;
        width=image.getWidth();
        height=image.getHeight();
        x=(int)(Math.random()*400);
        y=-height;

        speed= 5;
        xSpeed = 2;
        awardType = (int)(Math.random()*2);

    }

    public void move(){
        x+=xSpeed;
        y+=speed;

    }
    @Override
    public int getType(){
        return awardType;
       }
    }



