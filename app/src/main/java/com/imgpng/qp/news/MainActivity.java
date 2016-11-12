package com.imgpng.qp.news;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.imgpng.qp.constants.Constant;
import com.imgpng.qp.news.bean.XMLBean;
import com.imgpng.qp.utils.OKHttpHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.XMLFormatter;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
//    private TextView tvShow;
    private List<XMLBean> xmlBeanLists;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 123456:
                    Toast.makeText(MainActivity.this,""+msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 123:
                    xmlBeanLists = (List<XMLBean>) msg.obj;
                    recyclerView.getAdapter().notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        tvShow = (TextView) findViewById(R.id.tv_show);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new RecyclerViewAdpater());
       new Thread(new Runnable() {
           @Override
           public void run() {
               Document document = null;
               try {
                   document = Jsoup.parse(new URL(Constant.RSS_CN_CHINANEWS_RSS),1500);
                   Elements selectTitle = document.select("td[class=\"txt2\"]");//选取td标签的classtxt2属性为txt2的所有元素
                   Elements selectXML = document.select("td[class=\"STYLE2\"]");
//                String text = elementTitle.text();//获得文本类容;
                   List<XMLBean> beansXml = new ArrayList<XMLBean>();
                   for (int  i=0;i<selectTitle.size();i++) {
                       Element elementTitle = selectTitle.get(i);//根据下表获得对应的节点元素
                       Element elementXML  = selectXML.get(i);
                       beansXml.add(new XMLBean(elementTitle.text(),elementXML.text()));
                   }
                   Message message = mHandler.obtainMessage();
                   message.what = 123;
                   message.obj = beansXml;
                   mHandler.sendMessage(message);
               } catch (IOException e) {
                   e.printStackTrace();
                   Message message = mHandler.obtainMessage();
                   message.what = 123;
                   message.obj = "网络错误";
                   mHandler.sendMessage(message);
               }

           }
       }).start();
    }
    class RecyclerViewAdpater extends RecyclerView.Adapter{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.recycler_item, null);
            return new RecyclerViewHoder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RecyclerViewHoder hoder = (RecyclerViewHoder) holder;
            hoder.title.setText(xmlBeanLists.get(position).xmlTitle);

            int r =Math.abs(new Random().nextInt(255));
            int g   =Math.abs(new Random().nextInt(255));
            int b = Math.abs(new Random().nextInt(255));
            hoder.title.setTextColor(Color.rgb(r,g,b));
            hoder.title.setTag((Integer)position);
            Log.i("xml URL",xmlBeanLists.get(position).xmlUrl);
        }

        @Override
        public int getItemCount() {
            return xmlBeanLists == null ? 0 : xmlBeanLists.size();
        }
    }
    public static class RecyclerViewHoder extends RecyclerView.ViewHolder{
        public TextView title;
        public RecyclerViewHoder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.tv_title);;
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer tag = (Integer) v.getTag();
                    Toast.makeText(v.getContext(),"点击了"+tag+"项",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
