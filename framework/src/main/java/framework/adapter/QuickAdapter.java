package framework.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 17:25
 *  @描述：    TODO
 */
public abstract class QuickAdapter<T> extends BaseAdapter {
    protected List<T> mDataSource;
    protected Context mContext;

    public QuickAdapter(Context context) {
        mContext = context;
    }

    public QuickAdapter(Context context,List<T> data) {
        mContext = context;
        mDataSource = data;
    }
    public void addDatas(List<T> datas) {

        mDataSource = datas;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDataSource.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mDataSource || mDataSource.size() == 0) {
            return 0;
        }
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mDataSource || mDataSource.size() == 0) {
            return null;
        }
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
