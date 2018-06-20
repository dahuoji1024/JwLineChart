package com.test.yjwchart.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 10732 on 2018/6/15.
 */

public class YAxis extends View {

    private TextPaint mLabelsPaint;
    private final int axisColor = Color.parseColor("#FFFFFF");
    private final float labelTextSize = 30f;
    private float maxValue = 1.0f;
    private int width;
    private float offsetLeft = 26;
    private float offsetAxis = 20;
    private int height;
    private int labelTextWidth;
    private Typeface labelsTypeFace;
    private LineChartConfig lineChartConfig;
    private int labelsCount = 5;

    public void setLabelsCount(int labelsCount) {
        this.labelsCount = labelsCount;
    }

    public void setLineChartConfig(LineChartConfig lineChartConfig) {
        this.lineChartConfig = lineChartConfig;
    }

    public void setLabelsTypeFace(Typeface labelsTypeFace) {
        this.labelsTypeFace = labelsTypeFace;
        if (labelsTypeFace != null) {
            mLabelsPaint.setTypeface(labelsTypeFace);
        }
    }

    public YAxis(Context context) {
        super(context);
        init();
    }

    public YAxis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YAxis(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //Y 轴 labels画笔
        mLabelsPaint = new TextPaint();
        mLabelsPaint.setColor(axisColor);
        mLabelsPaint.setAntiAlias(true);
        mLabelsPaint.setTextSize(labelTextSize);
        mLabelsPaint.setTextAlign(Paint.Align.CENTER);
    }

    public TextPaint getYAxisPaint() {

        return mLabelsPaint;
    }

    public float getOffsetLeft() {
        return offsetLeft;
    }

    public float getOffsetAxis() {
        return offsetAxis;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        labelTextWidth = (int) mLabelsPaint.measureText(String.valueOf(maxValue));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float itemSpace = maxValue / 5;

        //Y轴
        for (int i = 0; i <= labelsCount; i++) {

            float itemY = 1.0f * (height - lineChartConfig.getOffsetTop() - lineChartConfig.getOffsetBottom()) / labelsCount * i + lineChartConfig.getOffsetTop();

            canvas.drawText(MathUtil.getFormatNumber(itemSpace * (labelsCount - i), 1), offsetLeft + labelTextWidth / 2, itemY + labelTextSize / 2, mLabelsPaint);
        }
    }
}
