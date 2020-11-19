package com.sparkle.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;

public class ImageUtil {
    private static final String ROOT_PATH = "C:/Users/K1181378/Desktop";
    private static final String KEY = "k1181378(轩翔辉)";

    public static void main(String[] args) throws Exception {

        //得到前一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
//        String date = new SimpleDateFormat("MM-dd").format(yesterday);
        String date = "10-19";


        List<String> textList = new ArrayList<>();
        textList.add(randomString() + "  " + KEY + " " + date + " 08:5" + getRandomTime());
        textList.add("    打卡");
        textList.add("");
        textList.add("");
        textList.add(randomString() + "  " + KEY + " " + date + " 12:5" + getRandomTime());
        textList.add("    打卡");
        textList.add("");
        textList.add("");
        textList.add(randomString() + "  " + KEY + " " + date + " 17:5" + getRandomTime());
        textList.add("    打卡");
        createImage(textList, new Font("黑体", Font.PLAIN, 25), Paths.get(ROOT_PATH, "打卡记录.png").toFile());
    }

    /**
     * 根据str,font的样式以及输出文件目录 创建PNG
     */
    public static void createImage(List<String> textList, Font font, File outFile) throws Exception {
        int width = 450;
        int height = 280;
        //创建图片画布
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g = image.createGraphics();

        // 增加下面的代码使得背景透明
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        g = image.createGraphics();

        //画出矩形区域，以便于在矩形区域内写入文字
        g.fillRect(0, 0, width, height);
        // 再换成黑色，以便于写入文字
        g.setColor(Color.blue);
        // 设置画笔字体
        g.setFont(font);

        for (int i = 0; i < textList.size(); i++) {
            boolean clock = "    打卡".equals(textList.get(i));
            if (clock) {
                g.setColor(Color.black);
            }
            //画出一行字符串，注意y轴坐标需要变动
            g.drawString(textList.get(i), 0, (i + 1) * font.getSize());
            if (clock) {
                g.setColor(Color.blue);
            }
        }
        g.dispose();
        // 输出png图片
        //ImageIO.write(image, "png", outFile);
        transferAlpha(image);
    }

    /**
     * 随机时间
     */
    private static String getRandomTime() {
        int minute = new Random().nextInt(9);
        int second = new Random().nextInt(59);
        return minute + ":" + new DecimalFormat("#00").format(second);
    }

    private static String randomString() {
        return Math.random() > 0.4 ? "" : " ";
    }

    /**
     * PNG背景变透明
     */
    public static void transferAlpha(Image image) {
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }

                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", new File(ROOT_PATH + "/打卡记录.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}