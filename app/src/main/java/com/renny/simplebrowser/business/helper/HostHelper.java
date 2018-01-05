package com.renny.simplebrowser.business.helper;

import java.util.regex.Pattern;

/**
 * Created by Renny on 2018/1/5.
 */

public class HostHelper {

    public static boolean isUrl(String url) {
        Pattern pattern = Pattern
                .compile("(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?");
        return pattern.matcher(url).matches();
    }
}
