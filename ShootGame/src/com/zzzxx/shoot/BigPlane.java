package com.zzzxx.shoot;

public class BigPlane extends FlyingObject implements Award,Enemy{
    private int awardType;
    public BigPlane(){
        image= Main.bigplane;
        width=image.getWidth();
        height=image.getHeight();

        x=(int)(Math.random()*400);
        y=-height;
        speed=3;
        awardType=(int)(Math.random()*2);
    }

    @Override
    public void move(){
        y+=speed;

    }
    @Override
    public  int getType(){
        return awardType;
    }
    @Override
    public int getScore(){
        return 10;
    }
}
