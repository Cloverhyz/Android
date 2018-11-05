package com.example.kangningj.myapplication.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.activity.MainActivity;
import com.example.kangningj.myapplication.activity.RegisterActivity;
import com.example.kangningj.myapplication.callpackage.HttpCallBack;
import com.example.kangningj.myapplication.popupWindow.SelectPicPopupWindow;
import com.example.kangningj.myapplication.utils.Constant;
import com.example.kangningj.myapplication.utils.OkHttpUtils;
import com.example.kangningj.myapplication.utils.UploadUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Tfx on 2016/12/1.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {

    private TextView textView;
    private TextView tv_address;
    private TextView tv_email;
    private TextView tv_phone;
    private EditText et_address;
    private EditText et_phone;
    private EditText et_email;
    private Button bt_update;
    private SelectPicPopupWindow menuWindow;
    private SimpleDraweeView headPicView;
    private String accountId;
    private String accountName;

    @Override
    protected View initView() {
        MainActivity mainActivity = (MainActivity) getActivity();
        final View view = View.inflate(mContext, R.layout.fragment_user, null);
        textView = (TextView) view.findViewById(R.id.tv_user_success);
        tv_address = (TextView) view.findViewById(R.id.tv_user_address);
        tv_email = (TextView) view.findViewById(R.id.tv_user_email);
        tv_phone = (TextView) view.findViewById(R.id.tv_user_phone);
        tv_address.setOnClickListener(UserFragment.this);
        tv_email.setOnClickListener(UserFragment.this);
        tv_phone.setOnClickListener(UserFragment.this);

        et_address = (EditText) view.findViewById(R.id.et_user_address);
        et_email = (EditText) view.findViewById(R.id.et_user_email);
        et_phone = (EditText) view.findViewById(R.id.et_user_phone);

        bt_update = (Button) view.findViewById(R.id.bt_user_update);
        bt_update.setOnClickListener(UserFragment.this);

        headPicView = (SimpleDraweeView) view.findViewById(R.id.imv_user_head);
        accountId = mainActivity.getAccountId();
        accountName = mainActivity.getAccountName();
        Uri uri = Uri.parse(Constant.USER_HEAD_URL + accountId);
        headPicView.setImageURI(uri);
        headPicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                menuWindow.showAtLocation(view.findViewById(R.id.fragment_user_layout), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        return view;
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.btn2:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg")));
                    startActivityForResult(takeIntent, 2);
                    break;
                // 相册选择图片
                case R.id.btn1:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, 1);
                    break;
                case R.id.tv_user_email:
                    tv_email.setVisibility(v.GONE);
                    et_email.setText(tv_email.getText().toString());
                    et_email.setVisibility(v.VISIBLE);
                    displayView(bt_update);
                    break;
                case R.id.tv_user_phone:
                    tv_phone.setVisibility(v.GONE);
                    et_phone.setText(tv_phone.getText().toString());
                    et_phone.setVisibility(v.VISIBLE);
                    displayView(bt_update);
                    break;
                case R.id.tv_user_address:
                    tv_address.setVisibility(v.GONE);
                    et_address.setText(tv_address.getText().toString());
                    et_address.setVisibility(v.VISIBLE);
                    displayView(bt_update);
                    break;
                case R.id.bt_user_update:
                    updateUserInfo();
                    break;
                default:
                    break;
            }
        }
    };

    private void doNotDisplayView(View view) {
        if (view.getVisibility()!=View.GONE){
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        textView.setText(accountName);
        System.out.println(this.getClass().getSimpleName() + "初始化了");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1://从相册获取
                try{
                    startPhotoZoom(data.getData());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 2://调用相机
                File temp = new File(Environment.getExternalStorageDirectory()+"/"+"image.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case 3:
                if (data!=null){
                    setPicToView(data);
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (data != null) {
            Bitmap photo = extras.getParcelable("data");
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/head");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/head", accountName + ".jpg");

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            UploadUtils.uploadFile(f, Constant.HEAD_UPLOAD_URL+"?accountId="+accountId, new HttpCallBack() {
                @Override
                public void sloveString(String result) {
                }
            });
            headPicView.setImageURI(Uri.fromFile(f));
//            headPicView.setImageBitmap(photo);
        }
    }

        private void startPhotoZoom(Uri uri) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri,"image/*");
            // aspectX , aspectY :宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX , outputY : 裁剪图片宽高
            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 3);
        }

        private void displayView(View view){
            if (view.getVisibility()!=view.VISIBLE){
                view.setVisibility(view.VISIBLE);
            }
        }

        private void updateUserInfo(){

            doNotDisplayView(et_address);
            tv_address.setText(et_address.getText().toString());
            displayView(tv_address);
            doNotDisplayView(et_email);
            tv_email.setText(et_email.getText().toString());
            displayView(tv_email);
            doNotDisplayView(et_phone);
            tv_phone.setText(et_phone.getText().toString());
            displayView(tv_phone);
            bt_update.setVisibility(View.GONE);

            String email = tv_email.getText().toString();
            String phone = tv_phone.getText().toString();
            String address = tv_address.getText().toString();
            final MainActivity mainActivity = (MainActivity) getActivity();
            final Handler mHandler = mainActivity.mHandler;
                String url = Constant.UPDATE_URL + "?accountName="+accountName+"&accountId="+accountId+"&bindEmail="+email+"&bindPhone="+phone+"&address="+address;
                try {
                    OkHttpUtils.getStringResponse(new HttpCallBack() {
                        @Override
                        public void sloveString(String result) {
                            Message msg = new Message();
                            msg.what = 3;
                            if(result.equals("success")){
                                msg.obj = "更新成功";
                            }else{
                                msg.obj = "更新失败";
                            }
                            mHandler.sendMessage(msg);
                        }
                    }, url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_email:
                tv_email.setVisibility(v.GONE);
                et_email.setText(tv_email.getText().toString());
                et_email.setVisibility(v.VISIBLE);
                displayView(bt_update);
                break;
            case R.id.tv_user_phone:
                tv_phone.setVisibility(v.GONE);
                et_phone.setText(tv_phone.getText().toString());
                et_phone.setVisibility(v.VISIBLE);
                displayView(bt_update);
                break;
            case R.id.tv_user_address:
                tv_address.setVisibility(v.GONE);
                et_address.setText(tv_address.getText().toString());
                et_address.setVisibility(v.VISIBLE);
                displayView(bt_update);
                break;
            case R.id.bt_user_update:
                updateUserInfo();
                break;
            default:
                break;
        }
    }
}
