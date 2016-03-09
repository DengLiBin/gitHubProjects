package com.denglibin.tencentqq;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.denglibin.tencentqq.domain.Cheeses;
import com.denglibin.tencentqq.drag.DragLayout;
import com.denglibin.tencentqq.util.Utils;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

public class MainActivity extends Activity {
    private DragLayout mDragLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mDragLayout= (DragLayout) this.findViewById(R.id.dl_layout);
        final ListView mLeftList= (ListView) this.findViewById(R.id.lv_left);
        final ListView mMainList= (ListView) this.findViewById(R.id.lv_main);
        final ImageView ivLeft= (ImageView) findViewById(R.id.iv_left);
        final ImageView ivMain= (ImageView) findViewById(R.id.iv_main);
        //DragLayout类定义了该方法
        mDragLayout.setDragStatusListener(new DragLayout.OnDragStatusChangeListenser() {
            @Override
            public void onClose() {
                Utils.showToast(MainActivity.this,"onClose");
                //让图标晃动
                ObjectAnimator mAnim=ObjectAnimator.ofFloat(ivMain, "translationX", 15.0f);
                mAnim.setInterpolator(new CycleInterpolator(4));//动画循环次数
                mAnim.setDuration(500);
                mAnim.start();
                mMainList.setEnabled(true);
            }

            @Override
            public void onOpen() {
                Utils.showToast(MainActivity.this,"onOpen");
                //随机设置一个条目
                Random random=new Random();
                int nextInt=random.nextInt(50);
                mLeftList.smoothScrollToPosition(nextInt);
                mMainList.setEnabled(false);
            }

            @Override
            public void onDragging(float percent) {
                Utils.showToast(MainActivity.this,"onDraging");
                //更新图标透明度
                ViewHelper.setAlpha(ivLeft,1-percent);
                mMainList.setEnabled(true);
            }
        });
        mLeftList.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Cheeses.sCheeseStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView=(TextView)super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        mMainList.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Cheeses.NAMES){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView=(TextView)super.getView(position, convertView, parent);
                textView.setTextSize(20);
                return textView;
            }
        });
    }

}
