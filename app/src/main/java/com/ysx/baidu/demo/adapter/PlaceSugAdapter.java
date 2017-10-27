package com.ysx.baidu.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysx.baidu.demo.R;
import com.ysx.baidu.demo.bean.BaiduPlaceSugBean.ResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ysx
 *         date 2017/10/27
 *         description
 */

public class PlaceSugAdapter extends RecyclerView.Adapter<PlaceSugAdapter.ViewHolder> {

    private List<ResultBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public PlaceSugAdapter(List<ResultBean> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_place_sug_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ResultBean bean = mData.get(position);
        holder.mTvPoiName.setText(bean.getName());
        holder.mTvCityName.setText(bean.getCity());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_poi_name)
        TextView mTvPoiName;
        @BindView(R.id.tv_city_name)
        TextView mTvCityName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
