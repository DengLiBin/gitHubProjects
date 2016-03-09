package com.denglibin.quickindex;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.denglibin.quickindex.bean.Person;
import com.denglibin.quickindex.ui.QuickIndexBar;
import com.denglibin.quickindex.util.Cheeses;
import com.denglibin.quickindex.util.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {
    private QuickIndexBar bar;
    private ListView mListView;
    private ArrayList<Person> persons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar= (QuickIndexBar) findViewById(R.id.bar);
        bar.setListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letters) {//复写监听器的方法
                Utils.showToast(getApplicationContext(),letters);
            }
        });

        mListView= (ListView) findViewById(R.id.lv_main);
        persons=new ArrayList<Person>();
        //将对象添加到集合中,并进行排序
        for(int i=0;i< Cheeses.NAMES.length;i++){
            Person person=new Person(Cheeses.NAMES[i]);
            persons.add(person);
            person=null;
        }
        Collections.sort(persons);//排序
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return persons.size();
            }

            @Override
            public Person getItem(int position) {
                return persons.get(position);
        }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=convertView;
                ViewHolder holder=new ViewHolder();
                if(convertView==null){
                    view=View.inflate(getApplicationContext(),R.layout.list_item,null);
                    holder.mIndex= (TextView) view.findViewById(R.id.tv_index);
                    holder.mName= (TextView) findViewById(R.id.tv_name);
                    
                }
                return null;
            }
        });
    }

    static class ViewHolder{
        TextView mIndex;
        TextView mName;
    }
}
