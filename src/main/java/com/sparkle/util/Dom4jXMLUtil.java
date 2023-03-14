package com.sparkle.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Dom4jXMLUtil {
    public static void main(String[] args) {

    }

    public static void parseXml(String message) throws DocumentException {
        //报文字符串转输入流
        InputStream inputStream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
        // 要创建一个 Document 对象，需要我们先创建一个 SAXReader 对象
        SAXReader reader = new SAXReader();
        // 通过创建 SAXReader 对象。来读取 xml 文件，获取 Document 对象
        Document document = reader.read(inputStream);
        //通过 Document 对象。拿到 XML 的根元素对象
        Element root = document.getRootElement();
        //通过根元素对象。获取所有的 book 标签对象,Element.elements(标签名)它可以拿到当前元素下的指定的子元素的集合
        List<Element> students = root.elements("student");
        //遍历每个 student 标签对象。然后获取到 student 标签对象内的每一个元素。
        for (Element student : students) {
            //获取student的id属性
            String id = student.attributeValue("id");
            //拿到 student 下面的 name 元素对象
            Element nameElement = student.element("name");
            //拿到 student 下面的 age 元素对象
            Element ageElement = student.element("age");
            //拿到 student 下面的 gender 元素对象
            Element genderElement = student.element("gender");
            //再通过 getText() 方法拿到起始标签和结束标签之间的文本内容
            System.out.println("学号：" + id);
            System.out.println("姓名:" + nameElement.getText());
            System.out.println("年龄:" + ageElement.getText());
            System.out.println("性别:" + genderElement.getText());
            System.out.println("*****************************");
        }
    }
}