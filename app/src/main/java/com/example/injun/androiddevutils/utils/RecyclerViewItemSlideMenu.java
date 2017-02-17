package com.example.injun.androiddevutils.utils;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by injun on 2017/2/17.
 */

public class RecyclerViewItemSlideMenu extends LinearLayoutCompat
{
    private android.support.v4.widget.ViewDragHelper mViewDragHelper;
    private View mDragView;
    private View contentView;

    public RecyclerViewItemSlideMenu(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setOrientation(LinearLayoutCompat.HORIZONTAL);
        mViewDragHelper = android.support.v4.widget.ViewDragHelper.create(this, 1.0f, new DragHelperCallBack());
    }

    public RecyclerViewItemSlideMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RecyclerViewItemSlideMenu(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }


    private final class DragHelperCallBack extends android.support.v4.widget.ViewDragHelper.Callback
    {

        /**
         * 当Down时候回调
         *
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId)
        {
            return child == contentView || child == mDragView;
        }


        /**
         * 该方法在Move的时候回调
         *
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            // 两个if主要是为了让viewViewGroup里
            int newLeft = 0;
            if (child == contentView)
            {
                int leftBound = -mDragView.getWidth() - 300;
                int right = 300;
                newLeft = Math.min(Math.max(left, leftBound), right);
            }
            if (child == mDragView)
            {
                int leftBound = getWidth() - mDragView.getWidth() - 300;
                int right = getWidth() + mDragView.getWidth() + 300;
                newLeft = Math.min(Math.max(left, leftBound), right);
            }
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy)
        {
            return child.getTop();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
        {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView)
            {
                mDragView.offsetLeftAndRight(dx);
            } else
            {
                contentView.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state)
        {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel)
        {

            if (releasedChild == contentView)
            {
                if (xvel > 400 || contentView.getLeft() >= -mDragView.getMeasuredWidth() / 2)
                {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
                } else if (xvel < -400 || contentView.getLeft() < -mDragView.getMeasuredWidth() / 2)
                {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, -mDragView.getWidth(), 0);
                }
            }

            if (releasedChild == mDragView)
            {
                if (xvel > 400 || contentView.getLeft() >= -mDragView.getMeasuredWidth() / 2)
                {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, contentView.getWidth(), 0);
                } else if (xvel < -400 || contentView.getLeft() < -mDragView.getMeasuredWidth() / 2)
                {
                    mViewDragHelper.smoothSlideViewTo(releasedChild, getWidth() - mDragView.getWidth(), 0);
                }
            }
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child)
        {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        int action = MotionEventCompat.getActionMasked(ev);
        // 如果触摸时间取消 或者 手指抬起 表示不被拦截
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
        {
            mViewDragHelper.cancel();
            return false;
        }
        if (mViewDragHelper.shouldInterceptTouchEvent(ev))
        {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll()
    {
        if (mViewDragHelper.continueSettling(true))
        {
            ViewCompat.postInvalidateOnAnimation(RecyclerViewItemSlideMenu.this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        mDragView.layout(contentView.getMeasuredWidth(), 0, contentView.getMeasuredWidth() + mDragView.getMeasuredWidth(), contentView.getMeasuredHeight());


    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        contentView = getChildAt(0);
        mDragView = getChildAt(1);
    }

    public interface OnDeleteLinstener
    {
        void funcDelete();
    }

    private OnDeleteLinstener mOnDeleteLinstener;

    public void setOnDeleteLinstener(OnDeleteLinstener onDeleteLinstener)
    {
        this.mOnDeleteLinstener = onDeleteLinstener;
    }

}
