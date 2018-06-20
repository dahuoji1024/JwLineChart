package com.test.yjwchart.linechart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by 10732 on 2018/6/14.
 */

public class LineChartRightHalf extends FrameLayout {

    private Context context;
    private float dx;
    private LineChartData lineChartData;
    private int parentWidth;
    private int parentHeight;
    private FrameLayout animateLayout;
    private View markerView;
    private LineChartBack lineChartBack;
    private Typeface labelsTypeFace;
    private LineChartConfig lineChartConfig;

    public void setLineChartConfig(LineChartConfig lineChartConfig) {
        this.lineChartConfig = lineChartConfig;

        addViews();
    }

    public void setLabelsTypeFace(Typeface labelsTypeFace) {
        this.labelsTypeFace = labelsTypeFace;
    }

    public View getMarkerView() {
        return markerView;
    }

    public LineChartBack getLineChartBack() {
        return lineChartBack;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }

    public LineChartRightHalf(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LineChartRightHalf(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChartRightHalf(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setMarkerView(View markerView) {
        this.markerView = markerView;
        RelativeLayout.LayoutParams layoutParamsMarker = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(markerView, layoutParamsMarker);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parentWidth = parent.getWidth();
            parentHeight = parent.getHeight();
        }
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addViews();
    }

    private void addViews() {
        //X轴 和 GridLines
        lineChartBack = new LineChartBack(context);
        lineChartBack.setLineChartConfig(lineChartConfig);
        lineChartBack.setLabelsTypeFace(labelsTypeFace);
        RelativeLayout.LayoutParams layoutParamsBack = new RelativeLayout.LayoutParams(lineChartConfig.getContentWidth(), lineChartConfig.getContentHeight());
        addView(lineChartBack, layoutParamsBack);

        //折线 和 填充
        animateLayout = new FrameLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(lineChartConfig.getContentWidth(), lineChartConfig.getContentHeight());
        animateLayout.setLayoutParams(params);

        lineChartData = new LineChartData(context);
        lineChartData.setLineChartConfig(lineChartConfig);
        RelativeLayout.LayoutParams layoutParamsData = new RelativeLayout.LayoutParams(lineChartConfig.getContentWidth(), lineChartConfig.getContentHeight());

        animateLayout.addView(lineChartData, layoutParamsData);
        addView(animateLayout, params);
    }

    public void animateLine() {
        animateLayout.setAlpha(0f);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, lineChartConfig.getContentWidth());
        valueAnimator.setDuration(lineChartConfig.getContentWidth());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int right = (int) animation.getAnimatedValue();
                animateLayout.layout(0, 0, (int) right, lineChartConfig.getContentHeight());
                if (right > 0) {
                    animateLayout.setAlpha(1.0f);
                }
            }
        });
        valueAnimator.start();
    }

    private float startX, lastX, lastY;
    private boolean isMoved = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /*
        * 解决和RecyclerView的滚动冲突问题
        * 这行代码的意思是告诉父控件,这次touch事件由我自己处理
        * */
        getParent().requestDisallowInterceptTouchEvent(true);

        float currentX = event.getRawX();
        float currentY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoved = false;
                startX = currentX;
                lastX = currentX;
                lastY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                dx = currentX - lastX;
                isMoved = true;
                float dy = currentY - lastY;
                updatePosition(dx, dy);
                lastX = currentX;
                lastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                fingerUp();
                isMoved = false;
                break;
            default:
                break;
        }

        return true;
    }

    private void fingerUp() {

        if (Math.abs(lastX - startX) < 10) {
            setHighLight(lastX - (WindowUtil.getScreenValues()[0] - parentWidth) - this.getX());
        } else {

            if (markerView != null) {
                markerView.setAlpha(0f);
            }
            setHighLight(-1);
            Log.d("====", "dx = " + dx);
        }


    }

    private void setHighLight(float touchedX) {

        lineChartData.setHighLight(touchedX);
    }

    private void updatePosition(float dx, float dy) {

        setX(getX() + dx);
        if (getX() + dx < parentWidth - lineChartConfig.getContentWidth()) {
            setX(parentWidth - lineChartConfig.getContentWidth());
        }
        if (getX() + dx > 0) {
            setX(0);
        }
    }
}
