package com.denglibin.tencentqq.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 侧滑面板
 * Created by DengLibin on 2016/3/7.
 */
public class DragLayout extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mHeight,mWidth;//当前布局（DragLayout）的宽高。
    private int mRange;
    private Status mStatus=Status.Close;
    OnDragStatusChangeListenser mListener;

    private ViewDragHelper.Callback mCallback=new ViewDragHelper.Callback(){
        //1、根据返回结果决定当前child是否可以拖拽
        //child:当前被拖拽的View
        // pointerId:区分多点触摸的id
        //尝试捕获child时调用
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
           return child==mMainContent;//只有当child是mMainContent时，才可以拖拽
        }
        //当Child被捕获时调用，如果tryCaptureView()返回false则该child不能被捕获
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }
        //返回拖拽的范围，不对拖拽进行真正的限制，仅仅决定了动画执行速度
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;//mWidth*0.6f:布局宽度的百分之60
        }

        //2、根据建议值修正将要移动（水平移动）到的位置，此时还没有发生真正的移动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //child:当前拖拽的View
            //left:新的位置的建议值，dx:位置的变化值
            //left=getLeft()+dx;当前位置+变化值
            //限制移动的范围是:0-mRange
            if(child==mMainContent){
                left=fixLeft(left);//调用修正滑动范围的方法
            }
            return left;
        }
        //上下移动，该方法不需要复写
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);//返回的是0，不移动
        }

        //3、位置发生变化时，处理要做的事情（更新状态，伴随动画（缩放渐变等），重绘界面）
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //为了兼容低版本（2.3），每次修改值之后进行重绘
            int newLeft=left;
            if(mMainContent==changedView){
                newLeft=mMainContent.getLeft()+dx;//dx根据拖动的动作在不断变化
                dispatchDragEvent(newLeft);//mLeftContent展示动画效果
            }
            newLeft=fixLeft(newLeft);//修正
            if(mLeftContent==changedView){//拖拽左面板
                //当左面板移动后，强制放回去
                //layout(int l,int t,int r,int b);l，t: 左上角距离左边和上边的距离;r,b:右上角距离左边和下边的距离
                mLeftContent.layout(0,0,0+mWidth,0+mHeight);//控制左面板不动
                mMainContent.layout(newLeft,0,newLeft+mWidth,0+mHeight);//滑动左边面板时，更新主面板的位置（根据dx的改变）
            }
            invalidate();
        }
        //4、当View被释放的时候，处理的事情（执行动画）
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //View releaseChild:被释放的View
            //xvel:水平方向的速度，向右为+
            //yvel:垂直方向的速度，向下为+

            super.onViewReleased(releasedChild, xvel, yvel);
            //判断应该开启还是关闭
            if(xvel==0&&mMainContent.getLeft()>mRange/2.0f){
                open();
            }else if(xvel>0){
                open();
            }else{
                close();
            }
        }
    };
    /**
     * 状态枚举
     */
    public static enum Status{
        Close,Open,Draging;
    }
    //状态改变监听器
    public interface  OnDragStatusChangeListenser{
        void onClose();
        void onOpen();
        void onDragging(float percent);
    }
    public void setDragStatusListener(OnDragStatusChangeListenser mListener){
        this.mListener=mListener;
    }

    //构造方法
    public DragLayout(Context context) {
        this(context, null);
    }
    //构造方法
    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //构造方法
    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //a、初始化操作
        mViewDragHelper=ViewDragHelper.create(this,mCallback);
    }
    //b、传递触摸事件

    //拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //传递给mDrag
        return  mViewDragHelper.shouldInterceptTouchEvent(ev);
        //return super.onInterceptTouchEvent(ev);
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            mViewDragHelper.processTouchEvent(event);
        }catch(Exception e){
            e.printStackTrace();
        }
       //返回true持续接收事件
        return true;
    }

    //布局加载完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //容错性检查，至少有俩子view,并且必须时ViewGroup的子类
        if(getChildCount()<2){
            throw new IllegalStateException("布局至少有俩孩子，Your ViewGroup must have 2 children at least");
    }
    if(!(getChildAt(0) instanceof ViewGroup &&getChildAt(1) instanceof ViewGroup)){
        throw new IllegalArgumentException("子View必须是ViewGroup的子类，Your childre must be an instanceof ViewGroup");
    }
    //FrameLayout拿到子view的方法
        mLeftContent= (ViewGroup) this.getChildAt(0);//下面一层view
        mMainContent= (ViewGroup) this.getChildAt(1);//上面一层view
    }

    //当尺寸有变化的时候调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=this.getMeasuredHeight();
        mWidth=this.getMeasuredWidth();
        mRange=(int)(mWidth*0.6f);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //A2、持续平滑动画（高频率调用）,true:动画还需要继续执行
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //根据滑动范围的建议值修正滑动的范围
    public int fixLeft(int left){
        if(left<0){
            return 0;
        }else if(left>mRange){
            return mRange;
        }
        return left;
    }
   //打开左面板，将主面板右移一部分
    public void open(){
       open(true);
    }
    //关闭左面板
    public void close(){
        close(true);
    }
    //是否有平移动画效果
    public void open(boolean isSmooth){
        if(isSmooth){
            //A1、触发一个平滑动画，返回true表示还没有移动到指定位置，需要刷新界面
            if(mViewDragHelper.smoothSlideViewTo(mMainContent,mRange,0)){
                //this:child所在的ViewGroup
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            mMainContent.layout(mRange,0,mRange+mWidth,mHeight);
        }
    }
    //是否有平移效果动画
    public void close(boolean isSmooth){
        if(isSmooth){
            //A1、触发一个平滑动画，返回true表示还没有移动到指定位置，需要刷新界面
            if(mViewDragHelper.smoothSlideViewTo(mMainContent,0,0)){
                //this:child所在的ViewGroup
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            mMainContent.layout(0,0,mWidth,mHeight);
        }

    }
    //给mLeftContent和mMainContent设置动画效果，newLeft的值根据滑动会改变，所以可以根据该值来设置view的位置
    protected void dispatchDragEvent(int newLeft){//newLeft初始值为0，mLeftContent初始规模为0.5，最大为1
        float percent=newLeft*1.0f/mRange;
        //mLeftContent.setScaleX(0.5f+0.5f*percent);
        //mLeftContent.setScaleY(0.5f+0.5f*percent);


        //更新状态，执行回调
        Status preStatus=mStatus;
        mStatus=updateStatus(percent);
        if(mStatus!=preStatus){
            //状态发生变化
            if(mStatus==Status.Close){
                if(mListener!=null){//只有当创建了监听器对象后猜才不为null（要复写onOpen，onClose,onDraging方法）
                    mListener.onClose();
                }
            }else if(mStatus==Status.Open){
                if(mListener!=null){
                    mListener.onOpen();
                }
            }else if(mStatus==Status.Draging){
                if(mListener!=null){
                    mListener.onDragging(percent);
                }
            }
        }
 //左面板：
        //缩放动画
        ViewHelper.setScaleX(mLeftContent,evaluate(percent,0.5f,1.0f));
        ViewHelper.setScaleY(mLeftContent, 0.5f + 0.5f * percent);

        //平移动画,以左上角的点为基准，从-mRange/2.0f移动0这个位置
        ViewHelper.setTranslationX(mLeftContent, evaluate(percent, -mRange / 2.0f, 0));
        //ViewHelper.setTranslationY(mLeftContent,0.5f-0.5f*percent);

        //渐变动画,透明度0.5f-1.0f
        ViewHelper.setAlpha(mLeftContent,evaluate(percent,0.5f,1.0f));

 //主面板:缩放动画
        ViewHelper.setScaleX(mMainContent,evaluate(percent,1.0f,0.8f));
        ViewHelper.setScaleY(mMainContent,evaluate(percent,1.0f,0.8f));

 //背景动画：亮度变化（颜色变化）
        //黑色到透明
        getBackground().setColorFilter((Integer)evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    //动画过度
    public Float evaluate(float fraction,Number startValue,Number endValue){
        float startFloat=startValue.floatValue();
        return startFloat+fraction*(endValue.floatValue()-startFloat);
    }
    //更新状态
    private Status updateStatus(float percent){
        if(percent==0f){
            return Status.Close;
        }else if(percent==1.0f){
            return Status.Open;
        }else{
            return Status.Draging;
        }
    }

    //颜色变化过度，不需要理解，能用就行
    private int evaluateColor(float fraction, Object start, Object end) {

        int startInt = (Integer) start;
        int startIntA = startInt >> 24 & 0xff;
        int startIntR = startInt >> 16 & 0xff;
        int startIntG = startInt >> 8 & 0xff;
        int startIntB = startInt & 0xff;

        int endInt = (Integer) end;
        int endIntA = endInt >> 24 & 0xff;
        int endIntR = endInt >> 16 & 0xff;
        int endIntG = endInt >> 8 & 0xff;
        int endIntB = endInt & 0xff;

        return ((int) (startIntA + (endIntA - startIntA) * fraction)) << 24
                | ((int) (startIntR + (endIntR - startIntR) * fraction)) << 16
                | ((int) (startIntG + (endIntG - startIntG) * fraction)) << 8
                | ((int) (startIntB + (endIntB - startIntB) * fraction));
    }
}
