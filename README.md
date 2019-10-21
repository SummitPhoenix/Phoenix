# DemoSpringBoot
SpringBoot

1. 标题:        


    # 等价于 <h1>

##<h2>

###<h3>

####<h4>

#####<h5>

        例子  :   # 标题内容(此时是一号标题)     ## 标题内容(此时是二号标题)

        

2.粗体字 ** 内容** 

  **粗体** 

   3.斜体 * 内容 * 或者 _内容 _

_内容_

4.图片链接 ![](url) 或者 <img src="url"></img>


![]("http:/wwwwwwwww.ssjsj.sjsj/1.jpg")

直接使用html的 <img src="1.jpg"> 比较方便

5.普通链接 [链接名称](url) <a href="url">链接名称</a>

[百度]("http://www.baidu.com")

<a href="http://www.baidu.com">百度</a>

6.下划线 == ---

==============

------------------------


7.表格

| id | name | age |

|:---: | :---: | :-----:| // : ---- : 居中 ：--- 居左 ----:居右

| 1 | 张三 | 李四 |   

8.引用 >

> 泰戈尔的飞鸟集 -泰戈尔   

9. 字符串 后面加2个空格 然后回车 等价于 <br/> (想要换行，可以这么做)

10. 代码段 ` 单代码段引用 ` ``` 多代码段引用 ```



` System.out.println("hello world"); `

```

System.out.println("hello world") ;

```

最后附上一个完整的包括上面内容的markdown的例子，找一下例如网页的有道比较，选择markdown文本格式，粘贴进去，

就可以预览效果和学习.


测试例子:



﻿# 一号标题

## 二号标题

### 三号标题



**粗体字**  

*斜体*  

_斜体_



<img src="https://www.baidu.com/img/baidu_jgylogo3.gif" />  

  

<a href="http://www.baidu.com"> **超链接**</a>  

[**超链接**]("http://www.baidu.com")



#### 下划线  

----



#### 表格



| id | name | age |

|:--:|:----:|:---:|

| 1 | 张三 | 20岁|

| 2 | 李四 | 30岁|

| 3 | 王麻子| 40岁|



#### 引用



> 你永远都不会叫醒一个装睡的人  -佚名



#### 单段代码生成



` echo "Hello Wolrd" `



#### 多段代码生成



```

  var $name = "张三";

  echo $name;

   

  class Test

  {

    public static void main(String [] args)

    {

       

      System.out.prinln("Hello Wordl")

    }

  }
