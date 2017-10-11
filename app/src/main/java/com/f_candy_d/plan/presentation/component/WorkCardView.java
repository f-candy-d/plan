package com.f_candy_d.plan.presentation.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.f_candy_d.plan.R;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: document your custom view class.
 *
 * Add the 'isChecked' attribute
 */
public class WorkCardView extends CardView {

    private int mMaxCommentsCount;
    private int mStepNumber;
    private String mTitle;
    private String mSubTitle;
    // This is a immutable list
    private List<String> mComments;

    // Colors
    private int mStepperCircleActiveColor;
    private int mStepperCircleInactiveColor;
    private int mTitleActiveColor;
    private int mTitleInActiveColor;
    private int mSubTitleActiveColor;
    private int mSubTitleInactiveColor;

    // Dimens
    private int mStepperConnectionLineTopMargin;
    private int mStepperConnectionLineBottomMargin;

    // UI
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mStepperCircleView;
    private Button mShowMoreCommentButton;
    private Button mCollapseCommentsButton;
    private LinearLayout mCommentsContainer;

    private Paint mStepperConnectionLinePaint;

    public WorkCardView(Context context) {
        super(context);
        init(null, 0);
    }

    public WorkCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WorkCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // # Colors

        mStepperCircleActiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_circle_active_color);
        mStepperCircleInactiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_circle_inactive_color);
        mTitleActiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_title_active);
        mTitleInActiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_title_inactive);
        mSubTitleActiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_sub_title_active);
        mSubTitleInactiveColor = ContextCompat.getColor(getContext(), R.color.work_card_stepper_sub_title_inactive);

        // # Dimens

        mStepperConnectionLineTopMargin = getResources().getDimensionPixelSize(R.dimen.work_card_stepper_connection_line_top_margin);
        mStepperConnectionLineBottomMargin = getResources().getDimensionPixelSize(R.dimen.work_card_stepper_connection_line_bottom_margin);

        // # Load Attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WorkCardView, defStyle, 0);

        mTitle = a.getString(R.styleable.WorkCardView_title);
        mSubTitle = a.getString(R.styleable.WorkCardView_subTitle);
        mMaxCommentsCount = a.getInteger(R.styleable.WorkCardView_maxCommentsCount, mMaxCommentsCount);
        mStepNumber = a.getInteger(R.styleable.WorkCardView_stepNumber, mStepNumber);

        a.recycle();

        // # UI

        // Load the layout file
        inflate(getContext(), R.layout.work_card_view, this);

        mTitleView = findViewById(R.id.work_title);
        mSubTitleView = findViewById(R.id.work_sub_title);
        mStepperCircleView = findViewById(R.id.stepper_circle);
        mShowMoreCommentButton = findViewById(R.id.show_more_comment_button);
        mCollapseCommentsButton = findViewById(R.id.collapse_comments_button);
        mCommentsContainer = findViewById(R.id.comments_container);

        // # Connection Line For Stepper Circle

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.work_card_stepper_connector_line));
        mStepperConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.work_card_stepper_connection_line_width));

        // # Default values

        mTitleView.setText(mTitle);
        mSubTitleView.setText(mSubTitle);
        mStepperCircleView.setText(String.valueOf(mStepNumber));
        // By default, mComments is an empty list (not a null object)
        setComments(new ArrayList<>(0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // # Connection Line

        int connectionLineX = mStepperCircleView.getLeft() + mStepperCircleView.getWidth() / 2;
        int connectionLineStartY = mStepperCircleView.getBottom() + mStepperConnectionLineTopMargin;
        int connectionLineEndY = contentHeight - mStepperConnectionLineBottomMargin;

        canvas.drawLine(connectionLineX, connectionLineStartY, connectionLineX, connectionLineEndY, mStepperConnectionLinePaint);
    }

    public void setComments(List<String> comments) {
        mComments = Collections.unmodifiableList(Preconditions.checkNotNull(comments));
        mCommentsContainer.removeAllViews();

        // TODO; Recycle a commentView if possible
        View commentView;
        for (int i = 0; i < mComments.size() && i < mMaxCommentsCount; ++i) {
            commentView = inflate(getContext(), R.layout.work_card_comment_item_view, null);
            // TODO; Set title & sub-title to a commentView
            ((TextView) commentView.findViewById(R.id.comment_title)).setText(mComments.get(0));
            mCommentsContainer.addView(commentView);
        }

        // Show more-comment button if [ mMaxCommentsCount < comments.size() ]
        if (mMaxCommentsCount < mComments.size()) {
            if (!mShowMoreCommentButton.isShown()) {
                mShowMoreCommentButton.setVisibility(VISIBLE);
            }
        } else {
            mShowMoreCommentButton.setVisibility(GONE);
        }

        // Hide collapse-comments button if [comments.size() == 0 ]
        if (mComments.size() == 0) {
            mCollapseCommentsButton.setVisibility(GONE);
        } else {
            mCollapseCommentsButton.setVisibility(VISIBLE);
        }
    }

    public void removeAllComments() {
        // Pass an empty list
        setComments(new ArrayList<>(0));
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public void setMaxCommentsCount(int maxCommentsCount) {
        mMaxCommentsCount = maxCommentsCount;
    }

    public void setStepNumber(int stepNumber) {
        mStepNumber = stepNumber;
        mStepperCircleView.setText(String.valueOf(stepNumber));
    }

    // Default is TRUE
    private boolean mIsActiveUi = true;

    public void setIsActiveUi(boolean isActiveUi) {

        if (isActiveUi == mIsActiveUi) return;

        // # Stepper Circle Color

        Drawable bg = DrawableCompat.wrap(mStepperCircleView.getBackground());
        int color = (isActiveUi) ? mStepperCircleActiveColor : mStepperCircleInactiveColor;
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        // # Title & Sub-Title Color

        color = (isActiveUi) ? mTitleActiveColor : mTitleInActiveColor;
        mTitleView.setTextColor(color);
        color = (isActiveUi) ? mSubTitleActiveColor : mSubTitleInactiveColor;
        mSubTitleView.setTextColor(color);

        // Update
        mIsActiveUi = isActiveUi;
    }
}
