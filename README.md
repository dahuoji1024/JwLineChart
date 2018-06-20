# JwLineChart
Android原生折线图
本来想用MPAndroidChart, 那个库确实很强大, 但很可惜对我们项目不适用, 因为它的Y轴我始终没有找到比较好的更新方法, 而且动画效果也是差强人意, 所以才有了下面的东西, 现在是初版, 好多属性没提供出来, 如果有伙伴支持后期会持续完善...

运行效果图如下
![运行效果图](https://github.com/dahuoji1024/JwLineChart/raw/master/ScreenShots/show.jpg)

项目结构
![项目结构图](https://github.com/dahuoji1024/JwLineChart/raw/master/ScreenShots/structure_linechart.png)
LineChartRightHalf监听的是滑动事件, 里面包含的是LineChartBack(绘制的是背景网格线和X轴的文字) 和 LineChartData(绘制折线和折线填充)

使用方法:
//获取xml中的View
lineChart = findViewById(R.id.lineChart);

LineChartConfig lineChartConfig = new LineChartConfig();

lineChartConfig.setContentHeight(800); //高度
lineChartConfig.setContentWidth(2000); //X轴的宽度

//设置边界(除Y轴以外的右侧可滑动部分), 类似padding
lineChartConfig.setOffsetBottom(70);
lineChartConfig.setOffsetTop(40);
lineChartConfig.setOffsetLeft(0);
lineChartConfig.setOffsetRight(0);

//开始绘制
lineChart.setLineChartConfig(lineChartConfig);

//刷新方法, 带有简单的动画效果
lineChart.updateData(xValues, yValues);
