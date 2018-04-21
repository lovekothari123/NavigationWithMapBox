package com.waypoints1.Faq;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.waypoints1.R;


public class FaqTitleViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView mArrowExpandImageView;
    private TextView faqTitleTextView;
    private View view;

    public FaqTitleViewHolder(View itemView) {
        super(itemView);
        faqTitleTextView= (TextView) itemView.findViewById(R.id.tv_faq_title);

        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.iv_expand);

        view= (View) itemView.findViewById(R.id.divider);
    }

    public void bind(FaqTitle faqTitle) {
        faqTitleTextView.setText(faqTitle.getFaqTitle());
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            view.setVisibility(View.GONE);
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            view.setVisibility(View.VISIBLE);
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);

    }
}