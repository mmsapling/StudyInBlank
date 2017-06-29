package com.example.utils.ui;

import android.content.Context;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/20 16:42
 *  @描述：    TODO
 */
public class UIUtils {
    /**
     * dp-->px
     */
    public static int dp2Px(Context context,int dp) {
        //1.px/dp = density    ==> px和dp倍数关系
        //2.px/(ppi/160) = dp  ==>ppi

        float density = context.getResources().getDisplayMetrics().density; //1.5
        //        int ppi = getResources().getDisplayMetrics().densityDpi;//160 240 320

        int px = (int) (dp * density + .5f);
        return px;
    }

    /**
     * px-->dp
     */
    public static int px2Dp(Context context,int px) {
        //1.px/dp = density    ==> px和dp倍数关系
        float density = context.getResources().getDisplayMetrics().density; //1.5
        int dp = (int) (px / density + .5f);
        return dp;
    }

}
