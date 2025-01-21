package pers.adlered.picuang.tool;

import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * <h3>picuang</h3>
 * <p>工具箱</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-11-06 11:09
 **/
@Component
public class ToolBox {
    public static final Set<String> SUFFIXSET;
    public static final String CONFIG_PATH = "/opt/config.ini";

    public static final String STORAGE_PATH = "/opt/img";

    static {
        SUFFIXSET = new HashSet<>();
        SUFFIXSET.add(".jpeg");
        SUFFIXSET.add(".jpg");
        SUFFIXSET.add(".png");
        SUFFIXSET.add(".gif");
        SUFFIXSET.add(".svg");
        SUFFIXSET.add(".bmp");
        SUFFIXSET.add(".ico");
        SUFFIXSET.add(".tiff");
    }
    
    public static String getSuffixName(String filename) {
        String suffixName = filename.substring(filename.lastIndexOf("."));
        suffixName = suffixName.toLowerCase();
        return suffixName;
    }

    public static boolean isPic(String suffixName) {
        return SUFFIXSET.contains(suffixName);
    }

    public static String getPicStoreDir() {
        return STORAGE_PATH;
    }

    public static File generatePicFile(String suffixName, String time) {
        return new File(getPicStoreDir() + "/" + time + UUID.randomUUID() + suffixName);
    }

    public static String getDirByTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        return simpleDateFormat.format(date);
    }

    public static String getINIDir() {
        return new File(CONFIG_PATH).getAbsolutePath();
    }
}
