package framework.annotation;

import android.Manifest;
import android.support.annotation.AnyRes;
import android.support.annotation.BinderThread;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.example.framework.R;

import java.util.ArrayList;
import java.util.List;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/21 11:01
 *  @描述：    Annotation注解
 */
public class AnnotationExample {
        public void add(@NonNull int a, @NonNull int b){
        }
        @StringRes
        @NonNull
        public void getStringByResId(@AnyRes int resId){

        }
        public void testIceCreamFlavourManager(){
                IceCreamFlavourManager iceCreamFlavourManager = new IceCreamFlavourManager();
                // iceCreamFlavourManager.setFlavour(8); 错误的
                //iceCreamFlavourManager.setFlavour(IceCreamFlavourManager.CHOCOLATE);
        }
        @Keep
        @WorkerThread
        @MainThread
        @BinderThread
        @UiThread
        public void doSomething(){
                red(0x000000);
                red1(R.color.red);
        }
        public void red(@ColorInt int color){

        }
        public void red1(@ColorRes int color){
        }
        public void setAlpha(@FloatRange(from = 0.0,to = 1.0) float alpha){
                List list = new ArrayList();
                size(list);
                int[] arr = {1,2,3};
                size(arr);

                //size1("hello");
        }
        public void size(@Size(2) int[] data){

        }
        public void size(@Size(min = 2) List data){
        }
        public void size1(@Size(max = 2) String data){
        }
        @RequiresPermission(Manifest.permission.CALL_PHONE)
        public void requiresPermission(){

        }
        @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
        public void requirePermission11(){

        }
        @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
        public void requirePermission1111(){

        }
}
