package com.pancheng.testLoadMoreHelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pancheng on 2016/1/28.
 */
public class LoadMoreHelper {
    public  int TYPE_RECYCLEVIEW = 0x0;
    public  int TYPE_EXPANDLISTVIEW = 0x1;

    //important for render
    private  int VISIBLE_THRESHOLD =  1;


    //control the loading status
    private  boolean loading;
    private  ViewGroup targetViewGroup;



    private static LinearLayoutManager mLinearLayoutManager;


    private void init(ViewGroup viewGroup){
        targetViewGroup = viewGroup;
        loading = false;

    }

    public void completeLoading(){
        loading = false;
    }

    public static void setLoadingProgressDrawable(){

    }
    public boolean addLoadMoreToViewGroup(ViewGroup viewGroup,final OnLoadMoreListener onLoadMoreListener){
        init(viewGroup);

        //if listview

        //if recycleview
        RecyclerView recyclerView = (RecyclerView)targetViewGroup;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            mLinearLayoutManager = linearLayoutManager;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD+1)
                            &&onLoadMoreListener != null) {
                            loading = true;

                            onLoadMoreListener.onLoadMore();
                   //         loading = false;
                    }

                    if (onLoadMoreListener != null && dy!=0) {
                        if(dy>0)
                            onLoadMoreListener.onScrollUp();
                        else
                            onLoadMoreListener.onScrollDown();
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //scroll stoped
                    if (newState == 0&&onLoadMoreListener != null) {
                        onLoadMoreListener.onScrollStopped();

                        //consider progress bar item
                        if (findLastCompletelyVisibleItemPosition() == linearLayoutManager.getItemCount()-1 && !loading){
                            onLoadMoreListener.onShowNoMoreToast();
                        }
                    }

                }
            });
            return true;
        }
        return false;


    }


    public int findLastCompletelyVisibleItemPosition() {
        final View child = findOneVisibleChild(mLinearLayoutManager.getChildCount() - 1, -1, true, false);
        return child == null ? RecyclerView.NO_POSITION : ((RecyclerView)targetViewGroup).getChildAdapterPosition(child);
    }

    private View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                                            boolean acceptPartiallyVisible) {
        OrientationHelper helper;
        if (mLinearLayoutManager.canScrollVertically()) {
            helper = OrientationHelper.createVerticalHelper(mLinearLayoutManager);
        } else {
            helper = OrientationHelper.createHorizontalHelper(mLinearLayoutManager);
        }

        final int start = helper.getStartAfterPadding();
        final int end = helper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = mLinearLayoutManager.getChildAt(i);
            final int childStart = helper.getDecoratedStart(child);
            final int childEnd = helper.getDecoratedEnd(child);
            if (childStart < end && childEnd >start) {
                if (completelyVisible) {
                    //  if (childStart >= start && childEnd <= end) {
                    if (childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }




    public interface OnLoadMoreListener{
        void onLoadMore();
        void onScrollDown();
        void onScrollUp();
        void onScrollStopped();
        void onShowNoMoreToast();

    }

    public interface OnLoadInfoListener{

    }
}
