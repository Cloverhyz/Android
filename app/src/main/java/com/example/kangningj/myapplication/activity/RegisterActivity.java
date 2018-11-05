package com.example.kangningj.myapplication.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.callpackage.HttpCallBack;
import com.example.kangningj.myapplication.utils.Constant;
import com.example.kangningj.myapplication.utils.OkHttpUtils;

import java.io.IOException;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private TextView mAccounTextView;
    private TextView mPassword;
    private TextView mConfirmRassword;
    private TextView mMail;
    private TextView mPhone;
    private Button regBtn;
    private Dialog dialog;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==200){
                dialog.dismiss();
                RegisterActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAccounTextView = (TextView) findViewById(R.id.m_reg_account);
        mPassword = (TextView) findViewById(R.id.m_reg_password);
        mConfirmRassword = (TextView) findViewById(R.id.m_infirm_password);
        mMail = (TextView) findViewById(R.id.m_reg_email);
        mPhone = (TextView) findViewById(R.id.m_reg_phone);
        regBtn = (Button) findViewById(R.id.m_reg_btn);
        regBtn.setOnClickListener(RegisterActivity.this);
    }

    @Override
    public void onClick(View v) {
        String account = mAccounTextView.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmRassword.getText().toString();
        String email = mMail.getText().toString();
        String phone = mPhone.getText().toString();
        if (!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
        }else if(account==null||account.isEmpty()||password==null||password.isEmpty()){
            Toast.makeText(RegisterActivity.this,"账号密码不为空，请重新输入",Toast.LENGTH_SHORT).show();
        }else{
            dialog = new Dialog(RegisterActivity.this, R.style.Theme_AppCompat_Dialog_MinWidth);
            LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
            View waiting = inflater.inflate(R.layout.dialog_progress,null);
//            dialog.setView(waiting);
            dialog.setContentView(R.layout.dialog_progress);
//            dialog.setCancelable(false);
            dialog.show();
            String url = Constant.REGISTER_URL + "?accountName="+account+"&accountPassword="+password+"&bindEmail="+email+"&bindPhone="+phone;
            try {
                OkHttpUtils.getStringResponse(new HttpCallBack() {
                    @Override
                    public void sloveString(String result) {
                        if(result.equals("success")){
                            Message msg = new Message();
                            msg.what = 200;
                            mHandler.sendMessage(msg);
                        }else{
                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
