//初始化
$(document).ready(function(){
    $("#header").load("header.html");
    $("#footer").load("footer.html");
    document.getElementById("index").className = "layui-nav-item  layui-this";
    console.log(document.getElementById("index").className);
    /*$("#index").attr("class","layui-nav-item  layui-this");*/
});