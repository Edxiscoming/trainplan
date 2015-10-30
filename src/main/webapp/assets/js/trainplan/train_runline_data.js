//界面测试专用
var dateArray = [];//[{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]
for(var i=0;i<2;i++) {
	dateArray.push({
//			dayIndex: i,
		runDate:"2014-05-1"+i
	});
};






/*==================================================
 * x\y坐标请求接口返回数据格式
 */
var gridData = {
			days:dateArray,//[{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]
			crossStns:[{stnName:"成都"},{stnName:"遂宁"},{stnName:"南充"},{stnName:"蓬安",isCurrentBureau:1},{stnName:"营山",isCurrentBureau:1},{stnName:"土溪",isCurrentBureau:1},{stnName:"达州"},{stnName:"安康"},{stnName:"华山"},{stnName:"北京西"}]
};


/*==================================================
 * 单个车次运行线请求接口返回数据格式
 */
var trainRunLineCanvasData = 
			[{
				  trainName:"K818",
				  startStn:"成都",//始发站名 为null时填入""（空串）
				  endStn:"北京西",//终到站名为null时填入""（空串）
				  trainStns:[
						{runDays:0,stnName:"成都",arrTime:"2014-05-10 19:18",dptTime:"2014-05-10 19:18",stayTime:0},
						{runDays:0,stnName:"遂宁",arrTime:"2014-05-10 21:19",dptTime:"2014-05-10 21:33",stayTime:14},
						{runDays:0,stnName:"南充",arrTime:"2014-05-10 22:15",dptTime:"2014-05-10 22:22",stayTime:7},
						{runDays:0,stnName:"蓬安",arrTime:"2014-05-10 22:49",dptTime:"2014-05-10 22:54",stayTime:5},
						{runDays:0,stnName:"营山",arrTime:"2014-05-10 23:06",dptTime:"2014-05-10 23:11",stayTime:5},
						{runDays:1,stnName:"达州",arrTime:"2014-05-11 00:13",dptTime:"2014-05-11 00:23",stayTime:10},
						{runDays:1,stnName:"安康",arrTime:"2014-05-11 03:12",dptTime:"2014-05-11 03:32",stayTime:20},
						{runDays:1,stnName:"华山",arrTime:"2014-05-11 07:45",dptTime:"2014-05-11 07:51",stayTime:6},
						{runDays:1,stnName:"北京西",arrTime:"2014-05-11 21:07",dptTime:"2014-05-11 21:07",stayTime:0}
				  ]
			}];

//模拟 靳磊 接口返回数据
var jl_trainRunLineCanvasData = 
			[{
				  trainName:"K818",
				  startStn:"成都",//始发站名 为null时填入""（空串）
				  endStn:"北京西",//终到站名为null时填入""（空串）
				  trainStns:[
						{runDays:0,stnName:"成都",arrTime:"2014-05-10 19:18",dptTime:"2014-05-10 19:18",stayTime:0},
						{runDays:0,stnName:"遂宁",arrTime:"2014-05-10 21:19",dptTime:"2014-05-10 21:33",stayTime:14},
						{runDays:0,stnName:"南充",arrTime:"2014-05-10 22:15",dptTime:"2014-05-10 22:22",stayTime:7},
						{runDays:0,stnName:"蓬安",arrTime:"2014-05-10 22:49",dptTime:"2014-05-10 22:54",stayTime:5},
						{runDays:0,stnName:"营山",arrTime:"2014-05-10 23:06",dptTime:"2014-05-10 23:11",stayTime:5},
						{runDays:1,stnName:"达州",arrTime:"2014-05-11 00:13",dptTime:"2014-05-11 00:23",stayTime:10},
						{runDays:1,stnName:"安康",arrTime:"2014-05-11 03:12",dptTime:"2014-05-11 03:32",stayTime:20},
						{runDays:1,stnName:"华山",arrTime:"2014-05-11 07:45",dptTime:"2014-05-11 07:51",stayTime:6},
						{runDays:1,stnName:"北京西",arrTime:"2014-05-11 21:07",dptTime:"2014-05-11 21:07",stayTime:0}
				  ]
			}];
//模拟 靳磊 接口返回数据    错误数据
var jl_error_trainRunLineCanvasData = 
			[{
				  trainName:"K818",
				  startStn:"成都",//始发站名 为null时填入""（空串）
				  endStn:"北京西",//终到站名为null时填入""（空串）
				  trainStns:[
						{runDays:0,stnName:"成都",arrTime:"2014-05-10 10:18",dptTime:"2014-05-10 10:18",stayTime:0},
						{runDays:0,stnName:"遂宁",arrTime:"2014-05-10 12:19",dptTime:"2014-05-10 15:33",stayTime:14},
						{runDays:0,stnName:"南充",arrTime:"2014-05-10 22:15",dptTime:"2014-05-10 22:22",stayTime:7},
						{runDays:0,stnName:"蓬安",arrTime:"2014-05-10 22:49",dptTime:"2014-05-10 22:54",stayTime:5},
						{runDays:0,stnName:"营山",arrTime:"2014-05-10 23:06",dptTime:"2014-05-10 23:11",stayTime:5},
						{runDays:1,stnName:"达州",arrTime:"2014-05-11 00:13",dptTime:"2014-05-11 00:23",stayTime:10},
						{runDays:1,stnName:"安康",arrTime:"2014-05-11 03:12",dptTime:"2014-05-11 03:32",stayTime:20},
						{runDays:1,stnName:"华山",arrTime:"2014-05-11 07:45",dptTime:"2014-05-11 07:51",stayTime:6},
						{runDays:1,stnName:"北京西",arrTime:"2014-05-11 21:07",dptTime:"2014-05-11 21:07",stayTime:0}
				  ]
			}];





