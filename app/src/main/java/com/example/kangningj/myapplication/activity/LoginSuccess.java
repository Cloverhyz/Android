package com.example.kangningj.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.customview.CircleImageView;
import com.example.kangningj.myapplication.popupWindow.SelectPicPopupWindow;

import java.io.File;
import java.io.FileOutputStream;

public class LoginSuccess extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private SelectPicPopupWindow menuWindow;
    private CircleImageView mCircleImageView;
    private String msg;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        textView = (TextView) findViewById(R.id.tv_success);
        mCircleImageView = (CircleImageView) findViewById(R.id.imv_head);
        mCircleImageView.setOnClickListener(LoginSuccess.this);
        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        textView.setText(getString(R.string.helloname) + msg);
        mImageView = (ImageView) findViewById(R.id.iv_canvas);
        Bitmap bitmap = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawCircle(500,500,500,paint);
        canvas.save();
        mImageView.setImageBitmap(bitmap);
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
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
//        Toast.makeText(LoginSuccess.this, "111", Toast.LENGTH_SHORT).show();
        menuWindow = new SelectPicPopupWindow(LoginSuccess.this, itemsOnClick);
        menuWindow.showAtLocation(LoginSuccess.this.findViewById(R.id.activity_login_success), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (data!=null){
            Bitmap photo = extras.getParcelable("data");
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory()+"/head");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory()+"/head", msg+".jpg");

            FileOutputStream out = null;
            try{
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG,90,out);
                try {
                    out.flush();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            mCircleImageView.setImageBitmap(photo);
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
}
