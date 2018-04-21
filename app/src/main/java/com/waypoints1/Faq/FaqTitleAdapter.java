package com.waypoints1.Faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.waypoints1.R;

import java.util.List;

public class FaqTitleAdapter extends ExpandableRecyclerAdapter<FaqTitleViewHolder, FaqDetailViewHolder> {

    private LayoutInflater mInflator;

    public FaqTitleAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
    }



    @Override
    public FaqTitleViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View titleView = mInflator.inflate(R.layout.row_faq_title, parentViewGroup, false);
        return new FaqTitleViewHolder(titleView);
    }

    @Override
    public FaqDetailViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View detailsView = mInflator.inflate(R.layout.row_faq_detail, childViewGroup, false);
        return new FaqDetailViewHolder(detailsView);
    }

    @Override
    public void onBindParentViewHolder(FaqTitleViewHolder faqTitleViewHolder, int position, ParentListItem parentListItem) {
        FaqTitle faqTitle= (FaqTitle) parentListItem;
        faqTitleViewHolder.bind(faqTitle);
    }

    @Override
    public void onBindChildViewHolder(FaqDetailViewHolder faqDetailViewHolder, int position, Object childListItem) {
        FaqDetails faqDetails1= (FaqDetails) childListItem;
        faqDetailViewHolder.bind(faqDetails1);
    }
}