package com.zhuozheng.sawyerdiyview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private List<View> mLineViews = new ArrayList<>();//每一行的子View
    private List<List<View>> mViews = new ArrayList<>();//所有的行 一行一行的存储
    private List<Integer> mHeights = new ArrayList<>();//每一行的高度

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void clear() {
        //内存优化：由于此方法在onMeasure中会被多次执行，从而引发内存抖动
        //可以参考FrameLayout的实现
        /*mLineViews = new ArrayList<>();
        mViews = new ArrayList<>();
        mHeights = new ArrayList<>();*/
        mLineViews.clear();
        mViews.clear();
        mHeights.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取限制的值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 记录当前行的宽度和高度
        int lineWidth = 0;// 宽度是当前行子view的宽度之和
        int lineHeight = 0;// 高度是当前行所有子View中高度的最大值

        //整个流式布局的宽度和高度
        int flowLayoutWidth = 0;//所有行中宽度的最大值
        int flowLayoutHeight = 0;// 所以行的高度的累加

        //关键：onMeasure会被父类执行多次，需要在此进行清零
        clear();

        int childCount = this.getChildCount();

        // 先测量子View，再根据子View尺寸，计算自己的
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);

            //先进行测量
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //测量之后，获取到当前子View的测量的宽度/高度
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 已经放入的孩子的宽度 + 准备放入的孩子的宽度 > 总宽度  就换行
            if (lineWidth + childWidth > widthSize) {
                mViews.add(mLineViews);
                mLineViews = new ArrayList<>();//创建新的一行
                //所有行中,最宽的一行作为流式布局的宽
                flowLayoutWidth = Math.max(flowLayoutWidth, lineWidth);
                // 流式布局的高度为所有行的高度相加
                flowLayoutHeight += lineHeight;
                mHeights.add(lineHeight);
                lineWidth = 0;
                lineHeight = 0;
            }

            mLineViews.add(child);
            lineWidth += childWidth;
            // 获取行中最高的子View
            lineHeight = Math.max(lineHeight, childHeight);

            //处理最后一行不显示的问题
            if(i == childCount-1){
                mViews.add(mLineViews);
                mHeights.add(lineHeight);
                flowLayoutWidth = Math.max(flowLayoutWidth, lineWidth);
                flowLayoutHeight += lineHeight;
            }
        }

        // 保存尺寸给后面用
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : flowLayoutWidth
                , heightMode == MeasureSpec.EXACTLY ? heightSize : flowLayoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currX = 0;
        int currY = 0;
        int lineCount = mViews.size();
        // 处理每一行
        for (int i = 0; i < lineCount; i++) {
            List<View> lineViews = mViews.get(i);
            int lineHeight = mHeights.get(i);
            int size = lineViews.size();
            // 处理每一行中的View
            for (int j = 0; j < size; j++) {
                View child = lineViews.get(j);
                // 子View的左上右下
                int left = currX;
                int top = currY;
                int right = left + child.getMeasuredWidth();
                int bottom = top + child.getMeasuredHeight();
                // 布局子View
                child.layout(left, top, right, bottom);
                currX += child.getMeasuredWidth();
            }
            currY += lineHeight;
            currX = 0;
        }
    }
}
