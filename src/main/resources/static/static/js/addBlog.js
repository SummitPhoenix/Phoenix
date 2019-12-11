//初始化
$(document).ready(function(){
    $("#header").load("header.html");
    $("#footer").load("footer.html");
    document.getElementById("index").className = "layui-nav-item  layui-this";
    console.log(document.getElementById("index").className);
    /*$("#index").attr("class","layui-nav-item  layui-this");*/
});

var label = "";
var title;
var author = "打上花火";
var createTime;
$("#addTitle").click(function () {
    var content = $("#content").val();
    createTime = new Date().Format("yyyy年MM月dd日 HH:mm:ss");
    $("#addBlog").append("<p id='title'>"+content+"</p></br><span id='time'>"+createTime+"</span>&nbsp&nbsp&nbsp&nbsp<a id='author'>打上花火</a><hr/>");
    title = content;
    $("#content").val("");
})
$("#addSubTitle").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<br/><br/><strong class='subTitle'>"+content+"<strong/><br/>");
    $("#content").val("");
})
$("#addTag").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<span class='tag'>"+content+"<span/>");
    label += content+",";
    $("#content").val("");
})
$("#addParaGraph").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<br/><pre class='paraGraph'>"+content+"</pre><br/>");
    $("#content").val("");
})
$("#addWeightParaGraph").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<br/><pre class='weightParaGraph'>"+content+"</pre><br/>");
    $("#content").val("");
})
$("#addJPG").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<img class='picture' src="+content+static/img/.jpg'><br/>");
    $("#content").val("");
})
$("#addPNG").click(function () {
    var content = $("#content").val();
    $("#addBlog").append("<img class='picture' src="+content+static/img/.png'><br/>");
    $("#content").val("");
})
$("#submit").click(function () {
    var content = $("#addBlog").html();
    label = label.substring(0,label.length-1);
    var json = {'label':label,'title':title,'author':author,'createTime':createTime,'content':content};
    $.ajax({
        async:false,
        url:"insertBlog",
        type:'post',
        contentType:"application/json",
        data:JSON.stringify(json),//请求的数据
        dataType:'json',//设置返回的数据类型
        cache:false,
        success:function (data) {//请求成功后返回的数据会封装在回调函数的第一个参数中
            alert("上传成功");
            return;
        },
        error:function () {
            console.log('请求失败！')
        },
    });
    return;
})
//时间格式化
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}