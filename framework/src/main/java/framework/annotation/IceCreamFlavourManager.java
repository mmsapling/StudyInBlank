package framework.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/21 11:17
 *  @描述：    使用整形常量代替枚举类型
 */
public class IceCreamFlavourManager {
    private int flavour;
    public static final int VANILLA = 0;
    public static final int CHOCOLATE = 1;
    public static final int STRAWBERRY = 2;

    private  int  pigValue;
    /**
     * 新的注解 ，VANILLA,CHOCOLATE,STRAWBERRY 代表可接受的类型
     */
//    @IntDef({VANILLA,CHOCOLATE,STRAWBERRY})
//    public @interface Flavour{
//    }
//
//    @IceCreamFlavourManager.Flavour
//    public int getFlavour(){
//        return flavour;
//    }
//    public void setFlavour(@Flavour int flavour){
//        this.flavour = flavour;
//    }
    @IntDef(flag = true,value = {VANILLA,CHOCOLATE,STRAWBERRY})
    @Retention(value = RetentionPolicy.SOURCE)
     public @interface Pig{

     }
    public void setPigValue(@Pig int pigValue){
        this.pigValue = pigValue;
    }
    @Pig
    public int getPigValue(){
        return this.pigValue;
    }
}
