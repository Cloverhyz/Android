package com.example.kangningj.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.callpackage.HttpCallBack;
import com.example.kangningj.myapplication.customview.CircleImageView;
import com.example.kangningj.myapplication.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher ,HttpCallBack{

    private String url = "http://img0.bdstatic.com/img/image/shouye/xiaoxiao/%E5%B0%8F%E6%B8%85%E6%96%B0614.jpg";
    private CircleImageView mLoginHead;
    private Button btLogin;
    private EditText etUserName;
    private EditText etPassWord;
    private boolean res = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1002) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginSuccess.class);
                intent.putExtra("msg", (String) message.obj);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLogin = (Button) findViewById(R.id.bt_login);
        etUserName = (EditText) findViewById(R.id.et_userName);
        etPassWord = (EditText) findViewById(R.id.et_passWord);
        mLoginHead = (CircleImageView) findViewById(R.id.imv_login);
        btLogin.setOnClickListener(this);
        etUserName.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                /*Message message = new Message();
                message.what = 1002;
                handler.sendMessage(message);*/
                accountLogin();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Log", "destory");
    }

    private boolean accountLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://10.2.21.73:8080/book-web/accountInfo/mlogin";
                    url = url+ "?accountName=" +etUserName.getText().toString()+"&accountPassword=" +etPassWord.getText().toString();
                    /*URL url = new URL("http://10.2.21.254:8080/book-web/accountInfo/mlogin");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    String param = "accountName=" + URLEncoder.encode(etUserName.getText().toString(), "UTF-8") + "&accountPassword=" + URLEncoder.encode(etPassWord.getText().toString(), "UTF-8");
                    httpURLConnection.setRequestProperty("Connection", "keep-alive");
                    httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    httpURLConnection.setRequestProperty("Content-Length",String.valueOf(param.getBytes().length));
                    httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK||true) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String result = null;
                        try {
                            while ((result = reader.readLine()) != null) {
                                stringBuilder.append(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            inputStream.close();
                        }
                        if ("success".equals(stringBuilder.toString())||true) {
                            res = true;
                            message.what = 1002;
                        }
                    }*/
                    OkHttpUtils.getStringResponse(MainActivity.this,url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return res;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mLoginHead.setImageResource(R.drawable.tu2);
    }

    @Override
    public void sloveString(String result) {
        Message message = new Message();
        if ("success".equals(result)) {
            res = true;
            message.what = 1002;
            message.obj = result;
            handler.sendMessage(message);
        }
    }
}












