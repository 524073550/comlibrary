package com.zhuke.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 15653 on 2018/5/18.
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {
    private Context mContext;
    private List<T> mDataList;
    private int mLayoutId;
    private MultiTypeSupport mMultiTypeSupport;
    public BaseRecycleViewAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mDataList = data;
        this.mLayoutId = layoutId;
    }

    public BaseRecycleViewAdapter(Context context, List<T> data, MultiTypeSupport multiTypeSupport) {
        this(context,data,-1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport!=null){
            return mMultiTypeSupport.getLayoutId(mDataList.get(position),position);
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(position);
                }
            });
        }
        convert(holder, mDataList.get(position));
    }

    public abstract void convert(RecyclerView.ViewHolder holder, T data);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {
        // 用来存放子View减少findViewById的次数
        private SparseArray<View> mViews;


        public BaseViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }


        public BaseViewHolder setText(int viewId,CharSequence text ){
            TextView view = getView(viewId);
            if (!TextUtils.isEmpty(text)){
                view.setText(text);
            }
            return this;
        }

        /**
         * 设置ImageView的资源
         */
        public BaseViewHolder setImageResource(int viewId, int resourceId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(resourceId);
            return this;
        }

        /**
         * 通过id获取view
         */
        public <T extends View> T getView(int viewId) {
            // 先从缓存中找
            View view = mViews.get(viewId);
            if (view == null) {
                // 直接从ItemView中找
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
        /**
         * 设置条目点击事件
         */
        public void setOnIntemClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        /**
         * 设置条目长按事件
         */
        public void setOnIntemLongClickListener(View.OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }
    }

    /**
     * Created by Darren on 2016/12/28.
     * Email: 240336124@qq.com
     * Description:  多布局支持接口
     */
    public interface MultiTypeSupport<T> {
        // 根据当前位置或者条目数据返回布局
         int getLayoutId(T item, int position);
    }


    /***************
     * 设置条目点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    public interface OnLongClickListener{
        boolean onLongClick(int postion);
    }
    public interface OnItemClickListener{
        void onItemClick(int postion);
    }

}
