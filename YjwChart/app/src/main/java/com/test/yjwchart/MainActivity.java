package com.test.yjwchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.test.yjwchart.linechart.LineChart;
import com.test.yjwchart.linechart.LineChartConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private List<String> xValues;
    private List<Float> yValues;
    private View markerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.lineChart);

        LineChartConfig lineChartConfig = new LineChartConfig();
        lineChartConfig.setContentHeight(800);
        lineChartConfig.setContentWidth(2000);
        lineChartConfig.setOffsetBottom(70);
        lineChartConfig.setOffsetTop(40);
        lineChartConfig.setOffsetLeft(0);
        lineChartConfig.setOffsetRight(0);

        lineChart.setLineChartConfig(lineChartConfig);

        //MarkerView
        //markerView = LayoutInflater.from(this).inflate(R.layout.marker_view, null);
        //lineChart.setMarkerView(markerView);

        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            xValues.add("11:00");
            yValues.add((float) (40 + Math.random() * 10));
        }

        //3秒钟后执行刷新动画
        lineChart.postDelayed(new Runnable() {
            @Override
            public void run() {
                lineChart.updateData(xValues, yValues);
            }
        }, 3000);
    }
}
