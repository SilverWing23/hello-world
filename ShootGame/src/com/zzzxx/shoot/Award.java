package com.zzzxx.shoot;
//奖励都有类型
public interface Award {
    //接口当中能定义的变量,默认都是public static final
    int DOUBLE_FIRE=0;//双倍火力
    int LIFE=1;//生命加1

    int getType();


}
