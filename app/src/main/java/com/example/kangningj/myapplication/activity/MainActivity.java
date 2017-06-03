package com.example.kangningj.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.adapter.BookShelfListViewAdapter;
import com.example.kangningj.myapplication.fragment.BaseFragment;
import com.example.kangningj.myapplication.fragment.CartFragment;
import com.example.kangningj.myapplication.fragment.UserFragment;
import com.example.kangningj.myapplication.fragment.HomeFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    RadioGroup rgmain;
    private ArrayList<BaseFragment> mFragments;
    private BaseFragment mContext;
    private String accountName;
    private String accountId;
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                BookShelfListViewAdapter bookShelfListViewAdapter = (BookShelfListViewAdapter) msg.obj;
                bookShelfListViewAdapter.notifyDataSetChanged();
            }else if(msg.what==2){
                initEvent();
            }else if (msg.what==3){
                Toast.makeText(MainActivity.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mIntent = getIntent();
        accountName = mIntent.getStringExtra("accountName");
        accountId = mIntent.getStringExtra("accountId");
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        //创建5个fragment并添加到容器
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new CartFragment());
        mFragments.add(new UserFragment());
    }

    private void initEvent() {
        //radioGroup选中事件
        rgmain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                int curPosition = 0; //viewGroup当前选中索引
                switch (checkId) {
                    case R.id.rb_home:
                        curPosition = 0;
                        break;
                    case R.id.rb_cart:
                        curPosition = 1;
                        break;
                    case R.id.rb_user:
                        curPosition = 2;
                        break;
                }
                //switchFragment(mFragments.get(curPosition));
                switchFragment(mContext, mFragments.get(curPosition));
            }
        });
        rgmain.check(R.id.rb_home);//默认选中首页
    }

    //切换fragment 每次都会初始化frgament数据
    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_home, fragment).commit();
    }

    //切换fragment fragment只初始化一次
    private void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        //判断当前fragment和目标fragment是否是同一个
        if (mContext != nextFragment) {
            //不是同一个 当前要显示的fragment就是nextfragment
            mContext = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加过
                if (!nextFragment.isAdded()) {
                    //nextFragment没被添加过

                    //隐藏当前Fragment 添加nextFragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.fl_home, nextFragment).commit();
                } else {
                    //nextFragment被添加过

                    //隐藏当前Fragment  显示nextFragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    private void initView() {
        rgmain = (RadioGroup) findViewById(R.id.rg_main);
    }

    public String getAccountName() {
        return accountName;
    }public String getAccountId() {
        return accountId;
    }
}
