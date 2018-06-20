package com.test.yjwchart.linechart;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by 10732 on 2018/6/11.
 */

public class MathUtil {

    //BigDecimal bg = new BigDecimal(value);
    //float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

    public static String getFormatNumber(float value, int type) {

        DecimalFormat decimalFormat = new DecimalFormat();
        switch (type) {
            case 0:
                decimalFormat.applyPattern("######0");
                break;
            case 1:
                decimalFormat.applyPattern("######0.0");
                break;
            case 2:
                decimalFormat.applyPattern("######0.00");
                break;
            case 6:
                decimalFormat.applyPattern("######0.000000");
                break;
        }
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        return decimalFormat.format(value);
    }

    public static String getFormatNumber2(float value, int type) {

        DecimalFormat decimalFormat = new DecimalFormat();
        switch (type) {
            case 0:
                decimalFormat.applyPattern("#,###,##0");
                break;
            case 2:
                decimalFormat.applyPattern("#,###,##0.00");
                break;
            case 6:
                decimalFormat.applyPattern("#,###,##0.000000");
                break;
        }
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        return decimalFormat.format(value);
    }
}
