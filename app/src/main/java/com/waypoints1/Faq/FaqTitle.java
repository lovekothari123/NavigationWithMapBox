package com.waypoints1.Faq;

import android.os.Parcel;

import java.util.List;


public class FaqTitle  implements ParentListItem {

    private String faqTitle;
    private List<FaqDetails> faqDetails;

    protected FaqTitle(Parcel in) {
        faqTitle = in.readString();
    }



    @Override
    public List<?> getChildItemList() {
        return faqDetails;
    }

    public FaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;

    }

    public FaqTitle(String faqTitle, List<FaqDetails> faqDetails) {
        this.faqTitle= faqTitle;
        this.faqDetails = faqDetails;
    }


    public String getFaqTitle() {
        return faqTitle;
    }

    public void setFaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;

    }

    public List<FaqDetails> getFaqDetails() {
        return faqDetails;
    }

    public void setFaqDetails(List<FaqDetails> faqDetails) {
        this.faqDetails = faqDetails;
    }



}