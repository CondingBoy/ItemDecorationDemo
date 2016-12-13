package com.wang.administrator.itemdecorationdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class IndexView extends View {

    private int textSize;
    private int bgColor;
    public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I","J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z", "#"};//#在最后面（默认的数据源）
    private List<String> mIndexDatas;//索引数据源
    private int mGapHeight;//每个index区域的高度
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private onIndexBarPressListener listener;
    public IndexView(Context context) {
        super(context);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mIndexDatas = Arrays.asList(INDEX_STRING);//数据源
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.IndexBar);
        //默认字体16sp
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics());
        //默认背景颜色为黑色
        bgColor = Color.BLACK;
        for (int i=0;i<typedArray.getIndexCount();i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.IndexBar_textSize:
                    textSize = typedArray.getDimensionPixelSize(R.styleable.IndexBar_textSize, textSize);
                    break;
                case R.styleable.IndexBar_pressBackground:
                    bgColor = typedArray.getColor(R.styleable.IndexBar_pressBackground, bgColor);
                    break;
            }
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);

        typedArray.recycle();
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int mesureWidth = 0,mesureHeight = 0;//最终测量的宽高
        //得到合适的宽度
        Rect indexBounds = new Rect();//存放每个Index的Rect区域
        for (int i=0;i<mIndexDatas.size();i++){
            String s = mIndexDatas.get(i);
            mPaint.getTextBounds(s,0,s.length(),indexBounds);//测量文字所在矩形，可以得到宽高
            mesureWidth=Math.max(mesureWidth,mesureWidth = indexBounds.width());
            mesureHeight=Math.max(mesureHeight,indexBounds.height());//循环后得到index的最大高度和宽度
        }
        mesureHeight *= mIndexDatas.size();
        switch (wMode){
            case MeasureSpec.EXACTLY:
                mesureWidth=wSize;
                break;
            case MeasureSpec.AT_MOST:
                mesureWidth = Math.min(mesureWidth,wSize);//此时wSize是父控件能给出的最大宽度
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (hMode){
            case MeasureSpec.EXACTLY:
                mesureHeight=hSize;
                break;
            case MeasureSpec.AT_MOST:
                mesureHeight = Math.min(mesureHeight,hSize);//此时wSize是父控件能给出的最大高度
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(mesureWidth,mesureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mGapHeight= (mHeight-getPaddingTop()-getPaddingBottom())/mIndexDatas.size();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        int panddingTop = getPaddingTop();//绘制index的基准点
        Rect indexBounds = new Rect();
        for(int i=0;i<mIndexDatas.size();i++){
            String s = mIndexDatas.get(i);
            mPaint.getTextBounds(s,0,s.length(),indexBounds);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseLine = (int) ((mGapHeight-fontMetrics.bottom-fontMetrics.top)/2);
            canvas.drawText(s,mWidth/2-indexBounds.width()/2,panddingTop+mGapHeight*i+baseLine,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundColor(bgColor);
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //通过计算判断落点在那个区域
                int posY= (int) ((y-getPaddingTop())/mGapHeight);
                if(posY<0){
                    posY=0;
                }
                if(posY>=mIndexDatas.size()){
                    posY=mIndexDatas.size()-1;
                }
                //回调监听器
                if(listener!=null){
                    listener.onIndexPress(posY,mIndexDatas.get(posY));
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                setBackgroundColor(Color.TRANSPARENT);
                if(listener!=null){
                    listener.onIndexUp();
                }
        }
        return true;
    }

    public interface onIndexBarPressListener{
        void onIndexPress(int position,String tag);
        void onIndexUp();
    }
    public void setOnIndexBarPressListener(onIndexBarPressListener listener){
        this.listener=listener;
    }
}
