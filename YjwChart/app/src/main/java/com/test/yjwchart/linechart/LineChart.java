package com.test.yjwchart.linechart;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.yjwchart.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 10732 on 2018/6/15.
 */

public class LineChart extends FrameLayout implements LineChartData.HighlightPosition {

    private Context context;
    private float maxValue = 1.0f;
    private List<Float> yValues;
    private List<String> xValues;
    private YAxis yAxis;
    private FrameLayout layoutCutDown;
    private LineChartData lineChartData;
    private LineChartBack lineChartBack;
    private View markerView;
    private LineChartRightHalf rightHalf;
    private Typeface labelsTypeFace;
    private LineChartConfig lineChartConfig;
    private TextView markerText;
    private MeasuredListener measuredListener;

    public void setMeasuredListener(MeasuredListener measuredListener) {
        this.measuredListener = measuredListener;
    }

    public interface MeasuredListener {
        void measuredHeight(int height);
    }

    public void setLineChartConfig(LineChartConfig lineChartConfig) {
        this.lineChartConfig = lineChartConfig;
        addViews();
    }

    public void setLabelsTypeFace(Typeface labelsTypeFace) {
        this.labelsTypeFace = labelsTypeFace;
    }

    public void setMarkerView(View markerView) {
        this.markerView = markerView;
        markerView.setAlpha(0f);
    }

    public LineChart(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LineChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measuredListener != null) {
            measuredListener.measuredHeight(getMeasuredHeight());
        }
    }

    private void addViews() {
        if (null == lineChartConfig) {
            lineChartConfig = new LineChartConfig();
            lineChartConfig.setContentWidth(WindowUtil.getScreenValues()[0] * 2);
            lineChartConfig.setContentHeight(getMeasuredHeight());
            lineChartConfig.setOffsetBottom(60);
            lineChartConfig.setOffsetTop(10);
            lineChartConfig.setOffsetLeft(0);
            lineChartConfig.setOffsetRight(0);
        }
        if (null == yValues) {
            yValues = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                yValues.add(0f);
            }
        }

        yAxis = new YAxis(context);
        yAxis.setLabelsTypeFace(labelsTypeFace);
        yAxis.setLineChartConfig(lineChartConfig);
        yAxis.setMaxValue(calculateMaxValue(maxValue));
        int labelTextWidth = (int) yAxis.getYAxisPaint().measureText(String.valueOf(maxValue));
        int yAxisWidth = (int) (yAxis.getOffsetLeft() + labelTextWidth + yAxis.getOffsetAxis());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(yAxisWidth, lineChartConfig.getContentHeight());
        addView(yAxis, layoutParams);

        layoutCutDown = new FrameLayout(context);

        rightHalf = new LineChartRightHalf(context);
        ViewGroup.LayoutParams rightHalfParams = new ViewGroup.LayoutParams(lineChartConfig.getContentWidth(), lineChartConfig.getContentHeight());
        rightHalf.setLineChartConfig(lineChartConfig);
        rightHalf.setLabelsTypeFace(labelsTypeFace);
        if (markerView == null) {
            markerView = LayoutInflater.from(context).inflate(R.layout.marker_view, null);
            markerText = markerView.findViewById(R.id.markerText);
            markerView.setAlpha(0f);
        }
        rightHalf.setMarkerView(markerView);

        layoutCutDown.addView(rightHalf, rightHalfParams);

        ViewGroup.LayoutParams layoutCutDownParams = new ViewGroup.LayoutParams(WindowUtil.getScreenValues()[0] - yAxisWidth, lineChartConfig.getContentHeight());
        addView(layoutCutDown, layoutCutDownParams);
        layoutCutDown.setX(yAxisWidth);

        lineChartData = rightHalf.getLineChartData();
        lineChartData.setHighlightPosition(this);
        lineChartBack = rightHalf.getLineChartBack();

        lineChartData.setValuesList(yValues);
    }

    public void updateData(List<String> xValues, List<Float> yValues) {

        this.xValues = xValues;
        this.yValues = yValues;

        //更新Y轴数据
        float yMaxValue = calculateMaxValue(Collections.max(yValues));
        yAxis.setMaxValue(yMaxValue);
        int labelTextWidth = (int) yAxis.getYAxisPaint().measureText(String.valueOf(yMaxValue));
        int yAxisWidth = (int) (yAxis.getOffsetLeft() + labelTextWidth + yAxis.getOffsetAxis());
        ViewGroup.LayoutParams yAXisParams = yAxis.getLayoutParams();
        yAXisParams.width = yAxisWidth;
        yAxis.setLayoutParams(yAXisParams);

        //更新 右侧位置 随Y轴的宽度
        ViewGroup.LayoutParams cutDownParams = layoutCutDown.getLayoutParams();
        cutDownParams.width = WindowUtil.getScreenValues()[0] - yAxisWidth;
        layoutCutDown.setLayoutParams(cutDownParams);
        layoutCutDown.setX(yAxisWidth);
        rightHalf.setParentWidth(cutDownParams.width);

        //更新折线
        lineChartData.setMaxValue(yMaxValue);
        lineChartData.setValuesList(yValues);

        //更新X轴
        lineChartBack.setXAxisLabels(xValues);

        lineChartData.setHighLight(-1);
        rightHalf.setX(0);
        rightHalf.animateLine();
        markerView.setAlpha(0);

    }

    private float calculateMaxValue(float maxValue) {

        float step = maxValue / 9.5f;
        float trueStep;
        float multiple = 1.0f;

        if (step == 0) {
            step = 0.1f;
        }
        if (step < 1) {
            do {
                step = step * 10;
                multiple = multiple / 10;
            } while (step < 1);
        } else {
            if (step > 1000) {
                while (step > 100) {
                    step = step / 10;
                    multiple = multiple * 10;
                }
            } else {
                while (step > 10) {
                    step = step / 10;
                    multiple = multiple * 10;
                }
            }
        }
        int intStep = (int) step;
        if (step > intStep) {
            intStep++;
        }
        trueStep = intStep * multiple;

        return trueStep * 10;
    }

    @Override
    public void highLightPosition(int valueIndex, float x, float y) {

        if (null == markerView) return;

        markerText.setText(String.valueOf(yValues.get(valueIndex)));

        if (x > 2000 - markerView.getWidth()) {
            x = 2000 - markerView.getWidth();
        }

        markerView.setX(x);
        markerView.setY(y - markerView.getHeight() - 30);
        markerView.setAlpha(1f);

    }
}
