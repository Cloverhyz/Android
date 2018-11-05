package com.example.kangningj.myapplication.fragment;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.activity.MainActivity;
import com.example.kangningj.myapplication.adapter.BookShelfListViewAdapter;
import com.example.kangningj.myapplication.bean.PaperBookMd;
import com.example.kangningj.myapplication.callpackage.HttpCallBack;
import com.example.kangningj.myapplication.utils.Constant;
import com.example.kangningj.myapplication.utils.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.kangningj.myapplication.R.id.ib_top;

/**
 * Created by Tfx on 2016/12/1.
 */

public class HomeFragment extends BaseFragment {
    private List<PaperBookMd> bookList = new ArrayList<>();
    private ImageButton mIbTop;
    private LinearLayout titlrBar;
    private ListView bookListView;
    private BookShelfListViewAdapter bookShelfAdapter;
    private Handler mHandler;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        titlrBar = (LinearLayout) view.findViewById(R.id.titlebar);
        mIbTop = (ImageButton) view.findViewById(ib_top);
        mIbTop.setOnClickListener(listener);
        view.findViewById(R.id.tv_message_home).setOnClickListener(listener);
        view.findViewById(R.id.tv_search_home).setOnClickListener(listener);
        bookListView = (ListView) view.findViewById(R.id.detil_listView);
        bookListView.setTop(titlrBar.getHeight());
        bookShelfAdapter = new BookShelfListViewAdapter(bookList,getActivity());
        bookListView.setAdapter(bookShelfAdapter);
        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //到顶部按钮
                case R.id.tv_message_home:
                    Toast.makeText(mContext, "消息", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_search_home:
                    Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        String url = Constant.BOOK_LIST_URL;
        MainActivity mainActivity = (MainActivity) getActivity();
        mHandler = mainActivity.mHandler;
        try {
            OkHttpUtils.getStringResponse(new HttpCallBack() {
                @Override
                public void sloveString(String result) {
                    if(result!=null&&!result.isEmpty()) {
                        List<PaperBookMd> paperBookMds = JSON.parseArray(result, PaperBookMd.class);
                        bookList.addAll(paperBookMds);
                        paperBookMds = null;
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = bookShelfAdapter;
                        mHandler.sendMessage(msg);
                    }
                }
            },url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
