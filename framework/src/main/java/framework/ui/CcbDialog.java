package framework.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.framework.R;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/20 8:54
 *  @描述：    TODO
 */
public class CcbDialog {
    private static CcbDialog mCcbDialog;
    private TextView mTvTitle;
    private ScrollView mContentScrollView;
    private LinearLayout mContentLLContainer;
    private View mViewLine;
    private TextView mTvContent;
    private TextView mTvConsult;
    private TextView mTvLeft;
    private TextView mTvRight;
    private LinearLayout mConsultLLContainer;
    private Dialog mDialog;
    private Context mContext;

    private CcbDialog() {
    }

    public static CcbDialog getInstance() {
        if (null == mCcbDialog) {
            synchronized (CcbDialog.class) {
                if (null == mCcbDialog) {
                    mCcbDialog = new CcbDialog();
                }
            }
        }
        return mCcbDialog;
    }

    public void showDialog(final Context context,
                           String title,
                           String content,
                           String leftContent,
                           OnClickCancelListener onClickCancelListener,
                           String rightContent,
                           OnClickConfirmListener onClickConfirmListener,
                           boolean cancelable) {
        mDialog = new AlertDialog.Builder(context, R.style.Ccb_Theme_Dialog).create();
        if (null == context || ((Activity) context).isFinishing()) {
            return;
        }
        mContext = context;
        mDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.ccb_dialog_layout, null);
        mDialog.setCancelable(cancelable);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85f);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
        mDialog.setContentView(view,params);
        findViewById(view);
        initData(context, "提示", content,null, leftContent, onClickCancelListener, rightContent, onClickConfirmListener);
        // mDialog.show();

    }
    public void showDialog(Context context,String content, OnClickCancelListener onClickCancelListener, OnClickConfirmListener onClickConfirmListener) {
        mDialog = new AlertDialog.Builder(context, R.style.Ccb_Theme_Dialog).create();
        if (null == context || ((Activity) context).isFinishing()) {
            return;
        }
        mContext = context;
        mDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.ccb_dialog_layout, null);
        mDialog.setCancelable(false);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85f);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
        mDialog.setContentView(view,params);
        findViewById(view);
        initData(context, "提示", content,null, "取消", onClickCancelListener, "确定", onClickConfirmListener);
        // mDialog.show();

    }
    private void initData(final Context context,
                          String title,
                          String content,
                          final String consultCode,
                          String leftContent,
                          final OnClickCancelListener onClickCancelListener,
                          String rightContent,
                          final OnClickConfirmListener onClickConfirmListener) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(content)) {
            mTvContent.setText(content);
            mTvContent.setText(content);
        } else {
            mTvContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(consultCode)) {
            mTvConsult.setText(consultCode);
            mConsultLLContainer.setVisibility(View.VISIBLE);
        } else {
            mConsultLLContainer.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(leftContent)) {
            mTvLeft.setText(leftContent);
            mTvLeft.setVisibility(View.VISIBLE);

        } else {
            mTvLeft.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(rightContent)) {
            mTvRight.setText(rightContent);
            mTvRight.setVisibility(View.VISIBLE);

        } else {
            mTvRight.setVisibility(View.GONE);
        }
        //分割线是否隐藏
        if (TextUtils.isEmpty(rightContent) || TextUtils.isEmpty(leftContent)) {
            mViewLine.setVisibility(View.GONE);
        } else {
            mViewLine.setVisibility(View.VISIBLE);
        }
        //处理5.0以上行距不显示问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int paddingBottom = (int) context.getResources().getDimension(R.dimen.y22);
            mTvContent.setPadding(mTvContent.getPaddingLeft(), mTvContent.getPaddingTop(), mTvContent.getPaddingRight(), paddingBottom);
        } else {
            mTvContent.setPadding(mTvContent.getPaddingLeft(), mTvContent.getPaddingTop(), mTvContent.getPaddingRight(), 0);
        }
        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickCancelListener) {
                    onClickCancelListener.clickCancel(mDialog);
                }
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickConfirmListener) {
                    onClickConfirmListener.clickConfirm(mDialog);
                }
            }
        });



    }
    private void findViewById(View view) {
        mTvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        mTvContent = (TextView) view.findViewById(R.id.tv_dialog_content);
        mTvLeft = (TextView) view.findViewById(R.id.tv_dialog_left);
        mTvRight = (TextView) view.findViewById(R.id.tv_dialog_right);
        mViewLine = view.findViewById(R.id.dialog_spacing);
        mTvConsult = (TextView) view.findViewById(R.id.tv_dialog_consult);
        mContentLLContainer = (LinearLayout) view.findViewById(R.id.dialog_content_layout);
        mConsultLLContainer = (LinearLayout) view.findViewById(R.id.dialog_consult);
    }

    public interface OnClickCancelListener {
        void clickCancel(Dialog dialog);
    }

    public interface OnClickConfirmListener {
        void clickConfirm(Dialog dialog);
    }
}
