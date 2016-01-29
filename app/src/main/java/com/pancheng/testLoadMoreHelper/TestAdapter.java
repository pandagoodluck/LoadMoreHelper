package com.pancheng.testLoadMoreHelper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pancheng on 2016/1/25.
 */
public class TestAdapter extends RecyclerView.Adapter{
    List<String> items = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;


    public TestAdapter(Context context, List<String> dataList){
        items = dataList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.list_item,parent,false);
        return new RVItemVH(convertView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Button btn = ((RVItemVH)holder).btn;
        btn.setText("pos"+position);

    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return items ==null ? 0 : items.size();
    }


    public class RVItemVH extends RecyclerView.ViewHolder {
        Button btn;

        public RVItemVH(View itemView) {
            super(itemView);
            btn = (Button)itemView.findViewById(R.id.button);

        }
    }

}
