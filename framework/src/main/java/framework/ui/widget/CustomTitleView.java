package framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.framework.R;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 16:25
 *  @描述：    TODO
 */
public class CustomTitleView extends View {
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;

    /**
     * 文本的大小
     */
    private int mTitleTextSize;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public CustomTitleView(Context context) {
        this(context,null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得我们所自定义的样式属性
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomTitleView,defStyleAttr,0);
        int indexCount = typedArray.getIndexCount();
        for(int i = 0; i < indexCount;i++){
            int index = typedArray.getIndex(i);
            if(index == R.styleable.CustomTitleView_custom_titleText){
                mTitleText = typedArray.getString(index);
            }else if(index == R.styleable.CustomTitleView_custom_titleTextColor){
                mTitleTextColor = typedArray.getColor(index, Color.BLACK);
            }else if(index == R.styleable.CustomTitleView_custom_titleTextSize){
                //默认设置为16sp，TypeValue也可以把sp转化为px
                int defValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics());
                mTitleTextSize = typedArray.getDimensionPixelSize(index,defValue);
            }
        }
        typedArray.recycle();
        //获得绘制文本的宽和高
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText,getWidth()/2,getHeight()/2,mPaint);
    }
}
