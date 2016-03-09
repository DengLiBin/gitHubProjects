package com.denglibin.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
/*

       Android Studio下如何配置AIDL文件:先在main目录下新建一个文件夹，命名为aidl，
再在该目录下新建一个包，包名跟AndroidManifest中的package同名，然后在该包下创建aidl文件，
创建完之后在build/generated/source/aidl/debug下就可以见到自动生成的java文件

    在app目录下的build.gradle中指定aidl的目录，比如设为src/main/java，

    那么在该包下新建的aidl文件都会自动生成对应的java文件另外，在aidl文件中必须指定好package，
    否则会编译出错

    快捷键：
alt+enter:自动修复（实现借口或者父类的方法，创建没有的方法）

ctrl+p： 提示。

ctrl+alt+space:自动提示，类似eclipse的alt+/

ctrl+d：复制当前行；
ctrl+y：删除当前行；

ctrl+alt+o:优化导入的包

alt+insert:获取get和set，还有构造方法；

sout:打印（System.out.println("")）；

打断点:当前行左边单击，f8调试，往下跳
 */
