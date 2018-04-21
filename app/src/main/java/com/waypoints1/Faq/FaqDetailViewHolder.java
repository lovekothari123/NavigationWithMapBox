package com.waypoints1.Faq;

import android.view.View;
import android.widget.TextView;

import com.waypoints1.R;


public class FaqDetailViewHolder extends ChildViewHolder {

    private TextView tv_faq_detail;

    public FaqDetailViewHolder(View itemView) {
        super(itemView);
        tv_faq_detail = (TextView) itemView.findViewById(R.id.tv_faq_detail);
    }

    public void bind(FaqDetails faqDetails) {
        tv_faq_detail.setText(faqDetails.getTitle());
    }

}