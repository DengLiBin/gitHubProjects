package com.denglibin.quickindex.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.denglibin.quickindex.util.Utils;

/**
 * 将布局高度分成26格（26个字母），每一格放一个字母（循环）
 * 用画笔画字符串（由数组取），x坐标不变，y坐标每循环一次就加一个格的高度
 * Created by DengLibin on 2016/3/8.
 */
public class QuickIndexBar extends View {
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private Paint mPaint;
    private int cellWidth;
    private float cellHeight;
    private OnLetterUpdateListener listener;
    private int touchIndex;
    /**
     * 暴露一个字母监听
     */
    public interface  OnLetterUpdateListener{
        void onLetterUpdate(String letters);
    }
    //设置监听器，其他类中拿到本类对象后调用该方法，复写onLetterUpdate(String letters)方法
    public void setListener(OnLetterUpdateListener listener){
        this.listener=listener;
    }
    public OnLetterUpdateListener getListener(){
        return listener;
    }
    //构造方法
    public QuickIndexBar(Context context) {
        this(context, null);
    }
    //构造方法
    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //构造方法
    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化画笔对象
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0;i<LETTERS.length;i++){
            String text=LETTERS[i];
            //计算坐标x
            int x=(int)(cellWidth/2.0f-mPaint.measureText(text)/2.0f);

          //获取文本高度
            Rect bounds=new Rect();
            mPaint.getTextBounds(text,0,text.length(),bounds);
            int textHeight=bounds.height();
            //坐标y
            int y=(int)(cellHeight/2.0f-textHeight/2.0f+i*cellHeight);

            mPaint.setColor(touchIndex==i?Color.GRAY:Color.WHITE);
            //绘制文本A-Z
            canvas.drawText(text,x,y,mPaint);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取单元格的宽高
        cellWidth=getMeasuredWidth();
        int mHeight=getMeasuredHeight();
        cellHeight=mHeight*1.0f/LETTERS.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index=-1;
        touchIndex=-1;
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                index=(int)(event.getY()/cellHeight);//根据当前y坐标判断是在第几个格上
                if(index>=0&&index<LETTERS.length){
                    if(index!=touchIndex){
                        listener.onLetterUpdate(LETTERS[index]);//其他类拿到本类对象后，调用setListener（）方法，并复写listener的onLetterUpdate（）方法
                        //Utils.showToast(getContext(),LETTERS[index]);
                        touchIndex=index;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                index=(int)(event.getY()/cellHeight);
                if(index>=0&&index<LETTERS.length){
                    if(index!=touchIndex){
                        listener.onLetterUpdate(LETTERS[index]);
                        //Utils.showToast(getContext(),LETTERS[index]);
                        touchIndex=index;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
}
