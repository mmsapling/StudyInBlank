package framework.annotation;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/21 14:14
 *  @描述：    TODO
 */
public class EasyToast {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Toast.LENGTH_SHORT,Toast.LENGTH_LONG})
    public @interface Length{
    }
    public static void makeText(@NonNull Context context,String msg){

    }
    public static void makeText(@NonNull Context context,String msg, @Length int length){

    }

}
