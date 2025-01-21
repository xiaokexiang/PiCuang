package pers.adlered.picuang.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.adlered.picuang.controller.api.bean.PicProp;
import pers.adlered.picuang.log.Logger;
import pers.adlered.picuang.tool.IPUtil;
import pers.adlered.picuang.tool.ToolBox;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pers.adlered.picuang.tool.ToolBox.STORAGE_PATH;

/**
 * <h3>picuang</h3>
 * <p>查看历史记录API</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-11-06 16:24
 **/
@Controller
public class History {

    private static final Pattern PATTERN = Pattern.compile("/(\\d{4})/(\\d{2})/(\\d{2})/");

    @RequestMapping("/api/list")
    @ResponseBody
    public List<PicProp> list(HttpServletRequest request, String year, String month, String day) {
        List<PicProp> list = new ArrayList<>();
        File file = new File(getHome() + year + "/" + month + "/" + day + "/");
        File[] files = listFiles(file);
        try {
            for (File k : files) {
                if (k.isFile()) {
                    PicProp picProp = new PicProp();
                    Matcher matcher = PATTERN.matcher(k.getPath());
                    if (matcher.find()) {
                        picProp.setTime(matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3));
                        picProp.setFilename(k.getName());
                        picProp.setPath(k.getPath().replace(STORAGE_PATH, "images"));
                        picProp.setIp(IPUtil.getIpAddr(request));
                        list.add(picProp);
                    }
                }
            }
        } catch (NullPointerException NPE) {
            logNpe();
        }
        Collections.reverse(list);
        return list;
    }

    @RequestMapping("/api/day")
    @ResponseBody
    public List<String> day(String year, String month) {
        File file = new File(getHome() + year + "/" + month + "/");
        File[] list = listFiles(file);
        List<String> lists = new ArrayList<>();
        try {
            for (File i : list) {
                if (i.isDirectory()) {
                    lists.add(i.getName());
                }
            }
        } catch (NullPointerException NPE) {
            logNpe();
        }
        Collections.reverse(lists);
        return lists;
    }

    @RequestMapping("/api/month")
    @ResponseBody
    public List<String> month(String year) {
        StringBuilder sb = new StringBuilder();
        File file = new File(getHome() + year + "/");
        File[] list = listFiles(file);
        List<String> lists = new ArrayList<>();
        try {
            for (File i : list) {
                if (i.isDirectory()) {
                    lists.add(i.getName());
                }
            }
        } catch (NullPointerException NPE) {
        }
        Collections.reverse(lists);
        return lists;
    }

    @RequestMapping("/api/year")
    @ResponseBody
    public List<String> year() {
        File file = new File(getHome());
        File[] list = listFiles(file);
        List<String> lists = new ArrayList<>();
        try {
            for (File i : list) {
                if (i.isDirectory()) {
                    lists.add(i.getName());
                }
            }
        } catch (NullPointerException NPE) {
        }
        Collections.reverse(lists);
        return lists;
    }

    private String getHome() {
        return ToolBox.getPicStoreDir() + "/";
    }

    private File[] listFiles(File file) {
        File[] files = file.listFiles();
        return files == null ? new File[0] : files;
    }

    private void logNpe() {
        Logger.log(String.format("A null pointer exception occurred in [%s]", this.getClass().getName()));
    }
}
