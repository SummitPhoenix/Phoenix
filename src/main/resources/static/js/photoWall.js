//初始化
$(document).ready(function(){
    $("#header").load("header.html");
    $("#footer").load("footer.html");
    document.getElementById("index").className = "layui-nav-item  layui-this";
    console.log(document.getElementById("index").className);
    /*$("#index").attr("class","layui-nav-item  layui-this");*/
});

$(document).ready(function(){
    $.ajax({
        async:false,
        url:"getPhotoList",
        type:'post',
        dataType:'json',//设置返回的数据类型
        cache:false,
        success:function (data) {
            $.each(data,function(i,json){
                $("#container").append("<div class='box'><div class='boximg'><a href='picture/"+data[i]+"' target='_blank'><img src='picture/"+data[i]+"'></a></div></div>");
            });
            return;
        },
        error:function () {
            console.log('请求失败！')
        },
    });

});

setTimeout(function () {
    waterFlow("container", "box");
}, 5000);
function waterFlow(parent, chirld){
    var wparent = document.getElementById(parent);//获取父级div, 最外级容器
    var allArr = getAllChirld(wparent,chirld);//获取到所有的class为box的容器div
    var wscreenWidth = document.documentElement.clientWidth;//获取屏幕宽度
    var wchirldWidth = wparent.getElementsByTagName("*");//获取所有的标签
    var num = Math.floor(wscreenWidth/wchirldWidth[0].offsetWidth);//这是一个Math算法, 目的是将小数转变为整数,
    // 从而可以知道每行最多容纳几张图片
    wparent.style.cssText = 'width:'+wchirldWidth[0].offsetWidth*num+'px;margin:0 auto';//固定每行摆放个数 和上下左右边距
    //获得每行的最小高度
    getMinHeightOfCols(allArr, num);
}
function getAllChirld(parent,classname){
    //获取所有的标签
    var wchirld = parent.getElementsByTagName("*");
    //创建数组
    var chirldArr = [];
    //遍历wchirld, 将其中className等于classname(传进来的参数)相同的标签放入数组chirldArr中
    for(var i = 0; i<wchirld.length; i++){
        if(wchirld[i].className==classname){
            //因为是位push所以没放进去一个, 都是在数组的最后一个
            chirldArr.push(wchirld[i]);
        }
    }
    //返回该数组
    return chirldArr;
}
function getMinHeightOfCols(chirdArr, num){
    //创建数组, 用来盛放每一行的高度
    var onlyOneColsArr = [];
    for(var i = 0; i<chirdArr.length; i++){

        if(i<num){
            //num为传进来的参数, 即为每行放图片的张数, 此步骤的目的是为了将第一行每张图片的高度遍历出来存放如新数组
            onlyOneColsArr[i]=chirdArr[i].offsetHeight;
        } else {
            //当大于每行存放的图片个数时进入该方法, Math.min.apply这个方法是为了得到数组中的最小值
            var minHeightOfCols = Math.min.apply(null, onlyOneColsArr);
            //此方法的目的是为了得到最小高度图片的下表, 也就是在每行的第几张, 具体方法见下面
            var minHeightOfindex = getminIndex(onlyOneColsArr, minHeightOfCols);
            //定义布局方式为绝对布局
            chirdArr[i].style.position = "absolute";
            //得到下一行图片应放的高度
            chirdArr[i].style.top = minHeightOfCols + "px";
            //得到下一行图片应放于那个位置
            chirdArr[i].style.left = chirdArr[minHeightOfindex].offsetLeft + "px";
            //将两张图片高度相加得到一个新的高度用来进行下一次的计算
            onlyOneColsArr[minHeightOfindex] += chirdArr[i].offsetHeight;
        }
    }

}
//此方法是为了进行最小高度下标的确定
function getminIndex(onlyOneColsArr, min){
    //遍历传进来的高度数组
    for(var i in onlyOneColsArr){
        //如果高度等于最小高度, 返回i即为该图片下标
        if(onlyOneColsArr[i] == min){
            return i;
        }
    }
}