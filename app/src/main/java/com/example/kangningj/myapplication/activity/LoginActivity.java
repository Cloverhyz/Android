package com.example.kangningj.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.callpackage.HttpCallBack;
import com.example.kangningj.myapplication.utils.Constant;
import com.example.kangningj.myapplication.utils.OkHttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private SimpleDraweeView simpleDraweeView;
    private TextView mRegisterTextView;
    private TextView mForgetTextView;
    private EditText mAccount;
    private EditText mPassword;
    private Button mLoginBtn;
    private String accountName;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.login_head);
        mAccount = (EditText) findViewById(R.id.m_login_account);
        mPassword = (EditText) findViewById(R.id.m_login_password);
        mLoginBtn = (Button) findViewById(R.id.m_login_btn);
        mRegisterTextView = (TextView) findViewById(R.id.reg_Text);
        mForgetTextView = (TextView) findViewById(R.id.forget_Text);
        mAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    initHead();
                }
            }
        });
        mLoginBtn.setOnClickListener(LoginActivity.this);
        mRegisterTextView.setOnClickListener(LoginActivity.this);
    }

    private void initHead() {
        try {
            accountName = mAccount.getText().toString();
            if (accountName != null && !accountName.isEmpty()) {
                File nf = new File(Environment.getExternalStorageDirectory() + "/head");
                nf.mkdir();
                //在根目录下面的ASk文件夹下 创建okkk.jpg文件
                File f = new File(Environment.getExternalStorageDirectory() +
                        "/head", accountName + ".jpg");
                simpleDraweeView.setImageURI(Uri.fromFile(f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_Text:
                Intent mIntent = new Intent();
                mIntent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(mIntent);
                break;
            case R.id.m_login_btn:
                String mPasswordStr = mPassword.getText().toString();
                if (accountName != null && !accountName.isEmpty() && mPasswordStr != null && !mPasswordStr.isEmpty()) {
                    String url = Constant.LOGIN_URL;
                    url = url + "?accountName=" + accountName + "&accountPassword=" + mPasswordStr;
                    try {
                        OkHttpUtils.getStringResponse(new HttpCallBack() {
                            @Override
                            public void sloveString(String result) {
                                if (!result.equals("error")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("accountId", result);
                                    intent.putExtra("accountName", accountName);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Message msg = new Message();
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }, url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
