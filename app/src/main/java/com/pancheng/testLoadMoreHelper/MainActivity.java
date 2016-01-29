package com.pancheng.testLoadMoreHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rootView;
    List<String> dataList;

    final static String MSG_TAG = "clumsypanda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        dataList = new ArrayList<>();

        for(int i =0 ; i< 20 ; ++i){
            dataList.add("0");
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setEnabled(false);

        rootView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rootView.setLayoutManager(mLayoutManager);

        final TestAdapter flipAdapter = new TestAdapter(this, dataList);
        rootView.setAdapter(flipAdapter);


        final RelativeLayout loadmoreLayout = (RelativeLayout)findViewById(R.id.loadmore_bar);

        final LoadMoreHelper loadMoreHelper = new LoadMoreHelper();
        LoadMoreHelper.OnLoadMoreListener onLoadMoreListener = new LoadMoreHelper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                loadmoreLayout.setVisibility(View.VISIBLE);
                Handler handler=new Handler();

                Runnable runnable=new Runnable(){
                    @Override
                    public void run() {
                        for(int i =0 ; i< 5 ; ++i){
                            dataList.add("0");
                        }

                        flipAdapter.notifyDataSetChanged();
                        loadmoreLayout.setVisibility(View.GONE);
                        loadMoreHelper.completeLoading();
                    }
                };
                handler.postDelayed(runnable, 2000);


            }


            @Override
            public void onScrollDown() {
                Log.d(MSG_TAG,"onScrollDown");
            }

            @Override
            public void onScrollUp() {
                Log.d(MSG_TAG,"onScrollUp");
            }

            @Override
            public void onScrollStopped() {
                Log.d(MSG_TAG,"onScrollStopped");
            }

            @Override
            public void onShowNoMoreToast() {
                Log.d(MSG_TAG,"onShowNoMoreToast");
            }
        };
        loadMoreHelper.addLoadMoreToViewGroup(rootView,onLoadMoreListener);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
