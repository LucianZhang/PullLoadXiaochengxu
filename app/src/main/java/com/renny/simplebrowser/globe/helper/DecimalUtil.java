package com.renny.simplebrowser.globe.helper;

import android.text.TextUtils;

/**
 * @author Created by chenxujie on 15/6/30.
 */
public final class DecimalUtil {
    public static String format(long number) {
        String text = number + "";
        String num;
        if (number >= 0) {
            if (text.length() <= 1) {
                num = "0.00";
            } else if (text.length() <= 2) {
                num = "0.0" + text.substring(0, 1);
            } else if (text.length() <= 3) {
                num = "0." + text.substring(0, 2);
            } else {
                num = text.substring(0, text.length() - 3) + "."
                        + text.substring(text.length() - 3, text.length() - 1);
            }
        } else {
            if (text.length() <= 2) {
                num = "-0.00";
            } else if (text.length() <= 3) {
                num = "-0.0" + text.substring(1, 2);
            } else if (text.length() <= 4) {
                num = "-0." + text.substring(1, 3);
            } else {
                num = text.substring(0, text.length() - 3) + "."
                        + text.substring(text.length() - 3, text.length() - 1);
            }
        }

        return num;
    }

    public static String format(String text) {
        if (TextUtils.isEmpty(text) || "".equals(text.trim())) {
            return "0.00";
        }
        String num;
        if (text.indexOf("-") == -1) {
            if (text.length() <= 1) {
                num = "0.00";
            } else if (text.length() <= 2) {
                num = "0.0" + text.substring(0, 1);
            } else if (text.length() <= 3) {
                num = "0." + text.substring(0, 2);
            } else {
                num = text.substring(0, text.length() - 3) + "."
                        + text.substring(text.length() - 3, text.length() - 1);
            }
        } else {
            if (text.length() <= 2) {
                num = "-0.00";
            } else if (text.length() <= 3) {
                num = "-0.0" + text.substring(1, 2);
            } else if (text.length() <= 4) {
                num = "-0." + text.substring(1, 3);
            } else {
                num = text.substring(0, text.length() - 3) + "."
                        + text.substring(text.length() - 3, text.length() - 1);
            }
        }

        return num;
    }

    public static String formatFloat(float number) {
        number = number / 1000.0f;
        return String.format("%.2f", number);
    }

    public static double formatLong(long number) {
        return number / 1000.0;
    }

    public static String format2String(long number) {
        number = number / 1000;
        return String.format("%d", number);
    }

    public static String formatNub(int nub) {
        String temp;
        if (nub < 10) {
            temp = "0" + nub;
        } else {
            temp = nub + "";
        }
        return temp;
    }

    /**
     * 将字符串转化成long
     *
     * @param str
     * @return
     */
    public static long string2Long(String str) {
        long res = 0;
        if (!isEmpty(str)) {
            try {
                res = Long.parseLong(str);
            } catch (Throwable e) {
            }
        }
        return res;
    }

    /**
     * 将字符串转化成int
     *
     * @param str
     * @return
     */
    public static Integer string2Integer(String str) {
        Integer res = 0;
        try {
            if (!isEmpty(str))
                res = Integer.parseInt(str);
            else
                res = 0;
        } catch (Exception e) {
            res = 0;
        }
        return res;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
    public static boolean isEmpty(String[] strings) {
        return strings == null || strings.length == 0;
    }

    public static String formatWithThousand(double number) {
        number = number / 1000;
        return String.format("%.2f", number);
    }

    public static long walletMoney(String str) {
        long money;
        if (str.contains(".")) {
            money = Double.valueOf(Double.parseDouble(str)).longValue();

        } else {
            money = string2Long(str);
        }
        return money;
    }

    public static int format2int(String number) {
        int value = 0;
        if (number.contains(".")) {
            String[] nums = number.split("\\.");
            if (nums.length >= 1) {
                value = Integer.parseInt(nums[0]);
            }
        } else {
            value = Integer.parseInt(number);
        }

        return value;
    }

    /**
     * 将字符串转化成double
     *
     * @param str
     * @return
     */
    public static double string2Double(String str) {
        double res = 0;
        if (!isEmpty(str)) {
            try {
                res = Double.parseDouble(str);
            } catch (Throwable e) {
            }
        }
        return res;
    }

}
