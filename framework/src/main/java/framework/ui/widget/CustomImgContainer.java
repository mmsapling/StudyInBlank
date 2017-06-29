package framework.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxw on 2017/4/7.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/4/7
 * @描述: TODO
 */
public class CustomImgContainer extends ViewGroup {
    public CustomImgContainer(Context context) {
        this(context, null);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 计算所有ChildView的宽度和高度，然后根据ChildView的计算结果，设置自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        //用于计算左边两个childView的高度
        int lHeight = 0;
        //用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;
        //用于计算上边两个childView的宽度
        int tWidth = 0;
        //用于计算下面两个childView的宽度，最终宽度取二者之间大值
        int bWidth = 0;
        /**
         * 根据childView计算的出宽和高，以及设置的margin计算容器的宽和高，主要用于容器是
         * wrap_content时
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            //上面两个childView
            if (i == 0 || i == 1) {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3) {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if(i == 0 || i == 2){
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if(i == 2 || i == 3){
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }
    }
}
