package pers.adlered.picuang.prop;

import org.springframework.stereotype.Component;
import pers.adlered.picuang.controller.UploadController;
import pers.adlered.picuang.log.Logger;
import pers.adlered.simplecurrentlimiter.main.SimpleCurrentLimiter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static pers.adlered.picuang.tool.ToolBox.CONFIG_PATH;

/**
 * <h3>picuang</h3>
 * <p>自动配置文件</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-11-06 21:29
 **/
@Component
public class Prop {
    // 版本号
    private static final String version = "V2.4";

    private static Properties properties = new Properties();

    static {
        put();
        Logger.log("Properties loaded.");
        reload();
    }

    public static void del() {
        File file = new File(CONFIG_PATH);
        file.delete();
    }

    public static void put() {
        try {
            properties.load(new BufferedInputStream(new FileInputStream(CONFIG_PATH)));
        } catch (FileNotFoundException e) {
            Logger.log("Generating new profile...");
            properties.put("imageUploadedCount", "0");
            properties.put("ips", "");
            properties.put("version", version);
            properties.put("password", "");
            properties.put("adminOnly", "off");
            properties.put("uploadLimit", "1:1");
            properties.put("cloneLimit", "3:1");
            try {
                properties.store(new BufferedOutputStream(Files.newOutputStream(Paths.get(CONFIG_PATH))), "Save Configs File.");
            } catch (IOException IOE) {
                IOE.printStackTrace();
            }
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        try {
            properties.setProperty(key, value);
            Logger.log("[Prop] Set key '" + key + "' to value '" + value + "'");
            PrintWriter printWriter = new PrintWriter(new FileWriter(CONFIG_PATH), true);
            Set set = properties.keySet();
            for (Object object : set) {
                String k = (String) object;
                String v = properties.getProperty(k);
                printWriter.println(k + "=" + v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!key.equals("imageUploadedCount")) {
            reload();
        }
    }

    public static String getVersion() {
        return version;
    }

    public static void reload() {
        Logger.log("Reloading profile...");
        // Reload properties
        try {
            properties.load(new BufferedInputStream(Files.newInputStream(Paths.get(CONFIG_PATH))));
        } catch (Exception e) {}
        // Upload limit
        try {
            String uploadLimitMaster = get("uploadLimit");
            if (uploadLimitMaster.contains(":")) {
                int uploadLimitTime = Integer.parseInt(uploadLimitMaster.split(":")[0]);
                int uploadLimitTimes = Integer.parseInt(uploadLimitMaster.split(":")[1]);
                UploadController.uploadLimiter = new SimpleCurrentLimiter(uploadLimitTime, uploadLimitTimes);
                Logger.log("Upload limit custom setting loaded (" + uploadLimitTimes + " times in " + uploadLimitTime + " second) .");
            }
        } catch (Exception e) {}
        // Clone limit
        try {
            String cloneLimitMaster = get("cloneLimit");
            if (cloneLimitMaster.contains(":")) {
                int cloneLimitTime = Integer.parseInt(cloneLimitMaster.split(":")[0]);
                int cloneLimitTimes = Integer.parseInt(cloneLimitMaster.split(":")[1]);
                UploadController.cloneLimiter = new SimpleCurrentLimiter(cloneLimitTime, cloneLimitTimes);
                Logger.log("Clone limit custom setting loaded (" + cloneLimitTimes + " times in " + cloneLimitTime + " second) .");
            }
        } catch (Exception e) {}
    }

    public static void renew() {
        del();
        put();
    }
}
