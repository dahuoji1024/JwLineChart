package com.test.yjwchart.linechart;

/**
 * Created by 10732 on 2018/6/14.
 */

public class LineChartConfig {

    private int offsetTop = 100;
    private int offsetBottom = 100;
    private int offsetLeft = 0;
    private int offsetRight = 0;

    private int contentWidth;
    private int contentHeight;

    public int getOffsetTop() {
        return offsetTop;
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public int getOffsetBottom() {
        return offsetBottom;
    }

    public void setOffsetBottom(int offsetBottom) {
        this.offsetBottom = offsetBottom;
    }

    public int getOffsetLeft() {
        return offsetLeft;
    }

    public void setOffsetLeft(int offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    public int getOffsetRight() {
        return offsetRight;
    }

    public void setOffsetRight(int offsetRight) {
        this.offsetRight = offsetRight;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }
}
