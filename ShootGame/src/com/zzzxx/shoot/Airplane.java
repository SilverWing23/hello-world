package com.zzzxx.shoot;
/**
 *敌机  大 中 小 score 血量
 */

public class Airplane extends FlyingObject implements Enemy{
    private int blood;

    public Airplane(){
        image = Main.airplane;
        width=image.getWidth();
        height=image.getHeight();
        x=(int)(Math.random()*400);
        y=-height;
        speed=5;


    }
    public void move(){
        //向下移动
        y+=speed;


    }
    @Override
    public int getScore(){
        return 5;
    }
}
