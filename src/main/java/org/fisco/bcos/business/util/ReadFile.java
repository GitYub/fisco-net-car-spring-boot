package org.fisco.bcos.business.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author ginyu
 * @date 2019-07-27 0:10
 **/

public class ReadFile {

    public static String[] toArrayByRandomAccessFile(String resourceLocation) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            File file = ResourceUtils.getFile(resourceLocation);
            // File file = new File(name);
            RandomAccessFile fileR = new RandomAccessFile(file,"r");
            // 按行读取字符串
            String str = null;
            while ((str = fileR.readLine())!= null) {
                arrayList.add(new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            fileR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理
        int length = arrayList.size();
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            String s = arrayList.get(i);
            array[i] = s;
        }
        // 返回数组
        return array;
    }

}
