package com.sparkle.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Smartisan
 */
public class TextUtil {

    public static String readFile(String fileLocation) {
        File file = new File(fileLocation);
        StringBuilder stringBuilder = new StringBuilder();
        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                InputStreamReader isr = new InputStreamReader(bis, "GBK");
                BufferedReader br = new BufferedReader(isr);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 注入${}中的Value
     */
    public static void autoInject(String valueFileLocation, String injectFileLocation, String outputFileLocation) {
        Map<String, String> keyValues = new HashMap<>();

        //提取Values
        File valueFile = new File(valueFileLocation);
        try (
                FileInputStream fis = new FileInputStream(valueFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("=");
                keyValues.put(values[0], values[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //注入Values
        File injectFile = new File(injectFileLocation);
        File outputFile = new File(outputFileLocation);
        try (
                FileInputStream fis = new FileInputStream(injectFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);

                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(osw)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("${")) {
                    String replacement = line.substring(line.indexOf("$"), line.indexOf("}") + 1);
                    String replacementValue = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
                    line = line.replace(replacement, keyValues.get(replacementValue));
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
