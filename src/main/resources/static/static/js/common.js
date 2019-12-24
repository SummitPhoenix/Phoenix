//导航栏
layui.use('element', function(){
    //导航的hover效果、二级菜单等功能，需要依赖element模块
    var element = layui.element;
    //监听导航点击
    element.on('nav(demo)', function(elem){
        //console.log(elem)
        layer.msg(elem.text());
    });
});

