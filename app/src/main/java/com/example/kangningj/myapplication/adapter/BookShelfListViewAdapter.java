package com.example.kangningj.myapplication.adapter;

/**
 * Created by H-Clover on 2017/4/18.
 */

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.bean.PaperBookMd;
import com.example.kangningj.myapplication.cart.utils.CartProvider;
import com.example.kangningj.myapplication.utils.Constant;
import com.example.kangningj.myapplication.utils.OkHttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;


public class BookShelfListViewAdapter extends BaseAdapter {

    private List<PaperBookMd> bookshelfList = null;
    private Context context = null;

    /**
     * 构造函数,初始化Adapter,将数据传入
     *
     * @param bookshelfList
     * @param context
     */
    public BookShelfListViewAdapter(List<PaperBookMd> bookshelfList, Context context) {
        this.bookshelfList = bookshelfList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bookshelfList == null ? 0 : bookshelfList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookshelfList == null ? null : bookshelfList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //装载view
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.bookshelf, null);

        //获取控件
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.book_image);
        TextView bookNameView = (TextView) view.findViewById(R.id.book_name);
        TextView bookDescView = (TextView) view.findViewById(R.id.book_description);
        TextView bookPriceView = (TextView) view.findViewById(R.id.book_price);
        ImageButton bookAddtoCart = (ImageButton) view.findViewById(R.id.add_btn);
        bookAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaperBookMd bookDataAdd = (PaperBookMd) getItem(position);
                CartProvider cart = CartProvider.getInstance();
                cart.add(bookDataAdd);
            }
        });
        //对控件赋值
        PaperBookMd bookData = (PaperBookMd) getItem(position);
        if (bookData != null) {
            Uri uri = Uri.parse(Constant.BOOK_PIC_URL + bookData.getPaperBookId());
            draweeView.setImageURI(uri);
//            bookImageView.setImageBitmap(OkHttpUtils.getHttpBitmap(Constant.BOOK_PIC_URL + bookData.getPaperBookId()));
            bookNameView.setText(bookData.getBookName());
            bookPriceView.setText(bookData.getBookPrice()+"");
            bookDescView.setText(bookData.getDescription());
        }
        return view;
    }
}
