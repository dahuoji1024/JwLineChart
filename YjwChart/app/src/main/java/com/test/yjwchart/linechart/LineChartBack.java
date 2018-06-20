package com.test.yjwchart.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10732 on 2018/6/14.
 */

public class LineChartBack extends View {

    private int width;
    private int height;
    private final int axisColor = Color.parseColor("#FFFFFF");
    private Paint mXAxisPaint;

    private final float labelTextSize = 34f;
    private List<String> xAxisLabels;
    private final int xMaxVisibleCount = 24;
    private TextPaint mLabelsPaint;
    private final int labelsOffsetAxis = 20;
    private Paint mXGridLinePaint;
    private Paint mYGridLinePaint;
    private int contentHeight;
    private int contentWidth;
    private Typeface labelsTypeFace;

    private LineChartConfig lineChartConfig;

    public void setLineChartConfig(LineChartConfig lineChartConfig) {
        this.lineChartConfig = lineChartConfig;
    }

    public void setLabelsTypeFace(Typeface labelsTypeFace) {
        this.labelsTypeFace = labelsTypeFace;
        if (labelsTypeFace != null) {
            mLabelsPaint.setTypeface(labelsTypeFace);
        }
    }

    public void setXAxisLabels(List<String> xAxisLabels) {
        this.xAxisLabels = xAxisLabels;
        invalidate();
    }

    public LineChartBack(Context context) {
        super(context);
        init();
    }

    public LineChartBack(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartBack(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        initList();

        // X轴画笔
        mXAxisPaint = new Paint();
        mXAxisPaint.setStrokeWidth(1f);
        mXAxisPaint.setColor(axisColor);

        // labels 画笔
        mLabelsPaint = new TextPaint();
        mLabelsPaint.setColor(axisColor);
        mLabelsPaint.setAntiAlias(true);
        mLabelsPaint.setTextSize(labelTextSize);
        mLabelsPaint.setTextAlign(Paint.Align.CENTER);

        // X轴 GridLines
        mXGridLinePaint = new Paint();
        mXGridLinePaint.setStrokeWidth(1f);

        // Y轴 GridLines
        mYGridLinePaint = new Paint();
        mYGridLinePaint.setColor(Color.WHITE);
        mYGridLinePaint.setStrokeWidth(1f);
    }

    private void initList() {
        xAxisLabels = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            xAxisLabels.add(i + ":00");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;


        LinearGradient xGirdLineBack = new LinearGradient(0, height - lineChartConfig.getOffsetBottom(), 0, 0, Color.parseColor("#77FFFFFF"),
                Color.parseColor("#00FFFFFF"), Shader.TileMode.CLAMP);
        mXGridLinePaint.setShader(xGirdLineBack);

        contentHeight = height - lineChartConfig.getOffsetTop() - lineChartConfig.getOffsetBottom();
        contentWidth = width - lineChartConfig.getOffsetLeft() - lineChartConfig.getOffsetRight();

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //X轴
        //canvas.drawLine(lineChartConfig.getOffsetLeft(), height - lineChartConfig.getOffsetBottom(), width - lineChartConfig.getOffsetRight(), height - lineChartConfig.getOffsetBottom(), mXAxisPaint);

        //X轴坐标
        for (int i = 0; i < xAxisLabels.size(); i++) {

            float itemX = lineChartConfig.getOffsetLeft() + 1.0f * i * (width - lineChartConfig.getOffsetLeft() - lineChartConfig.getOffsetRight()) / xMaxVisibleCount;
            float itemY = height - lineChartConfig.getOffsetBottom() + labelTextSize + labelsOffsetAxis;

            if (i % 2 != 0) {

                canvas.drawText(xAxisLabels.get(i),
                        itemX,
                        itemY,
                        mLabelsPaint);
            }

            //X轴gridLines
            canvas.drawLine(itemX, height - lineChartConfig.getOffsetBottom(), itemX, lineChartConfig.getOffsetTop(), mXGridLinePaint);
        }

        //Y轴 gridLines
        for (int i = 0; i <= 5; i++) {
            float itemY = 1.0f * (contentHeight) / 5 * i + lineChartConfig.getOffsetTop();

            mYGridLinePaint.setAlpha((int) (itemY / height * 80 + 20));
            canvas.drawLine(lineChartConfig.getOffsetLeft(), itemY, width - lineChartConfig.getOffsetRight(), itemY, mYGridLinePaint);
        }
    }

}
