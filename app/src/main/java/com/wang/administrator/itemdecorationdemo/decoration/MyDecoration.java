package com.wang.administrator.itemdecorationdemo.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.wang.administrator.itemdecorationdemo.entity.CityBean;

import java.util.List;

/**
 * 作者： WangWei.
 * 时间： 2016/12/5 0005 上午 11:14
 * 描述：
 */
public class MyDecoration extends RecyclerView.ItemDecoration {
    private List<CityBean> mDatas;
    private Paint mPaint;
    private Rect mRect ;
    private int mTitleHeight;
    private int mDevicerHeight;
    private static int  COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int  COLOR_TITLE_FONT = Color.parseColor("#FF000000");
    private int mFontSize;
    public MyDecoration(Context context, List<CityBean> datas){
        mRect = new Rect();
        mDatas = datas;
        mTitleHeight= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,context.getResources().getDisplayMetrics());
        mDevicerHeight= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,context.getResources().getDisplayMetrics());
        mFontSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics());
        mPaint=new Paint();
        mPaint.setTextSize(mFontSize);
        mPaint.setAntiAlias(true);
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //最先调用，获取item的left和right值
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth()- parent.getPaddingRight();
        final int count = parent.getChildCount();
        for(int i=0;i<count;i++){
            final View child = parent.getChildAt(i);
            //得到当前item的layoutparams
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int position = params.getViewLayoutPosition();
            //rv有时候会将position重置为0
            if(position>-1){
                if(position==0){//item为第一个item
                    drawTitleArea(c,params,child,left,right,position);
                }else{//其他情况
                    if(mDatas.get(position).getTag()!=null&&!mDatas.get(position).getTag().equals(mDatas.get(position-1).getTag())){
                        drawTitleArea(c,params,child,left,right,position);
                    }else {
                        //none
                        drawDeviderArea(c,params,child,left,right,position);
                    }
                }
            }
        }

    }

    private void drawDeviderArea(Canvas c, RecyclerView.LayoutParams params, View child, int left, int right, int position) {
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left,child.getTop()-params.topMargin,right,child.getTop()-params.topMargin+mDevicerHeight,mPaint);
    }

    /**
     * 画出标题
     * @param c 画布
     * @param params item的layoutparams
     * @param child item
     * @param left rv的paddingLeft
     * @param right rv的paddingRight
     * @param position 当前item的position
     */
    private void drawTitleArea(Canvas c, RecyclerView.LayoutParams params, View child, int left, int right, int position) {
        mPaint.setColor(COLOR_TITLE_BG);
        //绘制背景
        c.drawRect(left,child.getTop()-params.topMargin-mTitleHeight,right,child.getTop()-params.topMargin,mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(mDatas.get(position).getTag(),0,mDatas.get(position).getTag().length(),mRect);
        c.drawText(mDatas.get(position).getTag(),child.getPaddingLeft(),child.getTop()-params.topMargin-(mTitleHeight/2-mRect.height()/2),mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int pos=((LinearLayoutManager)parent.getLayoutManager()).findFirstVisibleItemPosition();

        String tag = mDatas.get(pos).getTag();
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;
        boolean flag = false;
        if(pos+1<mDatas.size()){//防止数组越界
            if(tag!=null&&!tag.equals(mDatas.get(pos+1).getTag())){//当前可见的第一个item与下一个的tag不同，表示悬浮的view需要切换了
                Log.e("TAG", "onDrawOver: getTop"+child.getTop());//当getTop开始为负数，它的绝对值为item移除屏幕的距离
                if(child.getHeight()+child.getTop()<mTitleHeight){//当第一个可见的item在屏幕中剩余的可见高度小于mTitleHeight时，需要做悬浮框的切换“动画”
                    c.save();//在对画布做操作前需要保存当前状态
                    flag=true;
                    c.translate(0,child.getHeight()+child.getTop()-mTitleHeight);
                }
            }
        }
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(parent.getPaddingLeft(),parent.getPaddingTop(),parent.getRight()-parent.getPaddingRight(),parent.getPaddingTop()+mTitleHeight,mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(tag,0,tag.length(),mRect);
        c.drawText(tag,child.getPaddingLeft(),parent.getPaddingTop()+mTitleHeight-(mTitleHeight/2-mRect.height()/2),mPaint);

//        if(flag)
//            c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position= ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        //rv的position在重置时可能为-1，保险起见，需要做个判断
        if(position>-1){
            if(position==0){
                outRect.set(0,mTitleHeight,0,0);
            }else{//判断其他情况
                if(null!=mDatas.get(position).getTag()&&!mDatas.get(position).getTag()
                        .equals(mDatas.get(position-1).getTag())){
                    outRect.set(0,mTitleHeight,0,0);
                }else {
                    outRect.set(0,1,0,0);
                }
            }
        }
    }
}
