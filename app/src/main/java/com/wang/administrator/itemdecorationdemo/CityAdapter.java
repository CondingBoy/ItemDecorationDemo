package com.wang.administrator.itemdecorationdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.administrator.itemdecorationdemo.entity.CityBean;

import java.util.List;

/**
 * 作者： WangWei.
 * 时间： 2016/12/6 0006 上午 10:06
 * 描述：
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>{

    private final LayoutInflater inflater;
    private final List<CityBean> mDatas;

    public CityAdapter(Context context, List<CityBean> data){
        inflater = LayoutInflater.from(context);
        mDatas = data;
    }
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(inflater.inflate(R.layout.item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        CityBean cityBean = mDatas.get(position);
        holder.mCity.setText(cityBean.getCityName());
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder{
        TextView mCity;
        public CityViewHolder(View itemView) {
            super(itemView);
            mCity= (TextView) itemView.findViewById(R.id.tv_city);
        }
    }
}
