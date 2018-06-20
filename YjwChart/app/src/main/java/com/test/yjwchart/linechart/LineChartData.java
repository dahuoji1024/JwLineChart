package com.test.yjwchart.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by 10732 on 2018/6/14.
 */

public class LineChartData extends View {

    private float width;
    private float height;
    private Paint mLinePaint;
    private List<Float> valuesList;
    private float maxValue = 1.0f;
    private int xMaxVisibleCount = 24;
    private Paint mFillPaint;
    private float touchedX = -1;
    private Paint mHighLightPaint;
    private HighlightPosition highlightPosition;

    private LineChartConfig lineChartConfig;
    private int offsetBottom;
    private int offsetTop;
    private int offsetLeft;
    private int offsetRight;

    public void setLineChartConfig(LineChartConfig lineChartConfig) {
        this.lineChartConfig = lineChartConfig;
        offsetBottom = lineChartConfig.getOffsetBottom();
        offsetLeft = lineChartConfig.getOffsetLeft();
        offsetRight = lineChartConfig.getOffsetRight();
        offsetTop = lineChartConfig.getOffsetTop();
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setHighlightPosition(HighlightPosition highlightPosition) {
        this.highlightPosition = highlightPosition;
    }

    public LineChartData(Context context) {
        super(context);
        init();
    }

    public LineChartData(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //折线画笔
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(6.0f);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setAntiAlias(true);

        //渐变背景画笔
        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);

        //高亮
        mHighLightPaint = new Paint();
        mHighLightPaint.setStyle(Paint.Style.FILL);
        mHighLightPaint.setColor(Color.WHITE);
        mHighLightPaint.setAntiAlias(true);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        LinearGradient xFillBack = new LinearGradient(0, 0, 0, height - offsetBottom, Color.parseColor("#99FFFFFF"),
                Color.parseColor("#00FFFFFF"), Shader.TileMode.CLAMP);
        mFillPaint.setShader(xFillBack);

        invalidate();
    }

    public void setValuesList(List<Float> valuesList) {
        this.valuesList = valuesList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (valuesList == null || valuesList.size() == 0) return;

        float per = 1.0f * (height - offsetBottom - offsetTop) / maxValue;

        Path fadePath = new Path();
        fadePath.moveTo(offsetLeft, height - offsetBottom);

        float itemXBehind = 0f;
        float itemYBehind = 0f;
        for (int i = 0; i < valuesList.size() - 1; i++) {

            float itemX = offsetLeft + 1.0f * i * (width - offsetLeft - offsetRight) / xMaxVisibleCount;
            float itemY = height - offsetBottom - valuesList.get(i) * per;
            itemXBehind = offsetLeft + 1.0f * (i + 1) * (width - offsetLeft - offsetRight) / xMaxVisibleCount;
            itemYBehind = height - offsetBottom - valuesList.get(i + 1) * per;
            //画折线
            canvas.drawLine(itemX, itemY, itemXBehind, itemYBehind, mLinePaint);

            //绘制路径做渐变
            fadePath.lineTo(itemX, itemY);
        }
        fadePath.lineTo(itemXBehind, itemYBehind);
        fadePath.lineTo(width - offsetRight, height - offsetBottom);
        fadePath.close();

        canvas.drawPath(fadePath, mFillPaint);


        drawHighlight(canvas);
    }

    private void drawHighlight(Canvas canvas) {
        if (touchedX == -1) return;

        float itemXSpace = 1.0f * (width - offsetLeft - offsetRight) / xMaxVisibleCount;
        int temp = (int) Math.floor(touchedX / itemXSpace);
        float judgeTemp = touchedX - offsetLeft - temp * itemXSpace;
        if (judgeTemp > itemXSpace / 2) {
            temp++;
        }

        float per = 1.0f * (height - offsetBottom - offsetTop) / maxValue;

        float highLightX = offsetLeft + temp * itemXSpace;
        float highLightY = height - offsetBottom - valuesList.get(temp) * per;

        mHighLightPaint.setAlpha(80);
        canvas.drawCircle(highLightX, highLightY, 24, mHighLightPaint);
        mHighLightPaint.setAlpha(255);
        canvas.drawCircle(highLightX, highLightY, 12, mHighLightPaint);

        if (highlightPosition != null) {
            highlightPosition.highLightPosition(temp, highLightX, highLightY);
        }
    }

    public void setHighLight(float touchedX) {
        this.touchedX = touchedX;

        invalidate();
    }


    public interface HighlightPosition {
        void highLightPosition(int valueIndex, float x, float y);
    }

}
