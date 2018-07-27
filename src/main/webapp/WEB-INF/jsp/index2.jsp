<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-2.1.0.js"></script>
    <style>
        body{
            margin: 0;
            padding: 0;
            height: 100%;
            position: relative;
        }
        #container{
            width: 100%;
            min-width: 400px;
            height: 600px;
            margin: 60px auto;
        }
        .resetBtn{
            width: 100px;
            height: 40px;
            position: absolute;
            right: 150px;
            top:10px;
            z-index: 99;
        }

    </style>
</head>
<body>
<div>
    <button class="resetBtn">清空数据</button>
    <!-- ECharts 图标显示容器 -->
    <div id="container"></div>

    <!-- 引入需要的js -->
    <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script> <!-- ECharts -->
</div>

</body>
<script>
    // 定义数组接收数据
    var xAxisData = [],yAxisData = [],dataUnit = [];
    // 获取chart容器
    var domObj = document.getElementById("container");
    // 创建myChart对象
    var myChart = echarts.init(domObj);
    // 显示标题，图例和空的坐标轴(初始化)
    myChart.setOption({
        title: {
            text: '运动轨迹'
        },
        tooltip: {
        },
        legend: {
            data:['运动轨迹']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '运动轨迹',
            type: 'scatter',
            data: []
        }]
    });
    // 加载动画
    myChart.showLoading();

    function getdata(){
        $.ajax({
            url:'/rxtxtest/listj',
            dataType: "json",
            type: "POST",
            timeout: 500,//设置为20s后断开连接
            error: function (XMLHttpRequest, textStatus, errorThrown) {//请求失败
                //如果返回错误，根据错误信息进行相应的处理
                //再次发起长连接
                getdata();
            },
            success: function (response) {//请求成功
                myChart.hideLoading();
                console.log(response.data);
                if(response.data){
                    //根据后台返回的数据对页面数据进行刷新
                    refresh(response.data)
                }
                var resetButton = document.getElementsByClassName('resetBtn')[0];
                resetButton.onclick = function () {
                    //alert(dataUnit.length);
                    window.location.reload();
                }
                getdata();//刷新成功后发起新的长连接请求

            }
        });
        function refresh(data){

            // 处理成需要的数据
            for (var i = 0; i < data.length; i++) {
                var item = data[i].split(" ")[0];
                if(i%2 == 0){
                    xAxisData.push(item.substring(2,item.length));
                } else {
                    yAxisData.push(item.substring(2,item.length));
                }
            }

            // 转换为ECharts需要的数组
            for (var i = 0; i < (xAxisData.length > yAxisData.length?xAxisData.length:yAxisData.length); i++) {
                dataUnit[i] = new Array();
                dataUnit[i].push(xAxisData[i],yAxisData[i]);
                //console.log(dataUnit[i]);
            }

            // 配置图表参数
            var option = {
                title: {
                    text: '运动轨迹'
                },
                legend: {
                    data:['运动轨迹']
                },
                tooltip: {
                    trigger: 'item',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#283b56'
                        }
                    }
                },
                xAxis: { // X轴数据(根据系列的data值)
                    name:'cm',
                    type: 'value',
                    min: 0,
                    max: 1000,
                    splitNumber:50,
                    axisLabel: {
                        interval:6,
                        rotate:30,
                        margin: 12,
                        formatter: function (value, index) {
                            if (index % 2 === 0) {
                                return value;
                            } else {
                                return '';
                            }
                        }
                    }
                },
                yAxis: { // y轴数据(根据系列的data值)
                    name:'cm',
                    type: 'value',
                    min:0,
                    max: 800,
                    splitNumber:20
                },
                series: [{ // 通过使用 series[i] 来表示系列列表
                    name: '运动轨迹',
                    type: 'scatter', // 表示散点图类型
                    smooth: true, // 曲线平滑
                    coordinateSystem: 'cartesian2d',// 直角坐标系
                    symbol:'circle',
                    symbolSize:5,
                    data: dataUnit // 描述的数据
                }]
            };

            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
        }
    }
    getdata();

</script>
</html>
