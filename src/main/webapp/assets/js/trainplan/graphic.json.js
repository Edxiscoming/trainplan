/**
 * Created by star on 5/11/14.
 */
//界面测试专用
var dateArray = [];//[{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]
for(var i=0;i<8;i++) {
    dateArray.push({
//			dayIndex: i,
        runDate:"2014051"+i,
        runDateText:"2014-05-1"+i
    });
};






/*==================================================
 * x\y坐标请求接口返回数据格式
 */
var gridData = {
    code : "0",	//非0即接口内部异常
    message:"",
    data:{
        days:dateArray,//[{runDate:"20140510",runDateText:"2014-05-10"},{runDate:"20140511",runDateText:"2014-05-11"}]
        crossStns:[{stnName:"成都"},{stnName:"遂宁"},{stnName:"南充"},{stnName:"蓬安"},{stnName:"营山"},{stnName:"土溪"},{stnName:"达州"},{stnName:"安康"},{stnName:"华山"},{stnName:"北京西"}]
    }
};


/*==================================================
 * 单个车次运行线请求接口返回数据格式
 */
var myTrainData = {
    code : "0",	//非0即接口内部异常
    message:"",
    data:{
        trainName:"K817",
        startStn:"北京西",//始发站名 为null时填入""（空串）
        endStn:"成都",//终到站名为null时填入""（空串）
        trainStns:[
            {runDays:0,runDate:"20140512",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
            {runDays:0,runDate:"20140512",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
            {runDays:1,runDate:"20140513",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
            {runDays:1,runDate:"20140513",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
            {runDays:1,runDate:"20140513",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
            {runDays:1,runDate:"20140513",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
            {runDays:1,runDate:"20140513",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
            {runDays:1,runDate:"20140513",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
            {runDays:1,runDate:"20140513",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
            {runDays:1,runDate:"20140513",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
        ]
    }
};




/*==================================================
 *按交路绘图请求接口返回数据格式
 */
var myJlData = {
    code : "0",//非0即接口内部异常
    message:"",
    data:[
        {
            crossName:"K818-K817-1",
            jxgx:[//接续关系数组
                {fromStnName:"北京西",fromRunDate:"20140511",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140512",toTime:"08:35"},
                {fromStnName:"成都",fromRunDate:"20140513",fromTime:"12:30",toStnName:"成都",toRunDate:"20140513",toTime:"19:18"},
                {fromStnName:"北京西",fromRunDate:"20140514",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140515",toTime:"08:35"}

            ],
            trains:[//该交路下所有车次数组
                {
                    trainName:"K818",
                    startStn:"成都",//始发站名 为null时填入""（空串）
                    endStn:"北京西",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140510",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
                        {runDays:0,runDate:"20140510",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
                        {runDays:0,runDate:"20140510",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
                        {runDays:0,runDate:"20140510",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
                        {runDays:0,runDate:"20140510",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
                        {runDays:1,runDate:"20140511",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
                        {runDays:1,runDate:"20140511",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
                        {runDays:1,runDate:"20140511",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
                        {runDays:1,runDate:"20140511",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
                    ]
                },
                {
                    trainName:"K817",
                    startStn:"北京西",//始发站名 为null时填入""（空串）
                    endStn:"成都",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140512",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
                        {runDays:0,runDate:"20140512",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
                        {runDays:1,runDate:"20140513",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
                        {runDays:1,runDate:"20140513",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
                        {runDays:1,runDate:"20140513",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
                        {runDays:1,runDate:"20140513",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
                        {runDays:1,runDate:"20140513",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
                        {runDays:1,runDate:"20140513",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
                        {runDays:1,runDate:"20140513",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
                        {runDays:1,runDate:"20140513",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
                    ]
                },
                {
                    trainName:"K818",
                    startStn:"成都",//始发站名 为null时填入""（空串）
                    endStn:"北京西",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140513",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
                        {runDays:0,runDate:"20140513",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
                        {runDays:0,runDate:"20140513",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
                        {runDays:0,runDate:"20140513",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
                        {runDays:0,runDate:"20140513",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
                        {runDays:1,runDate:"20140514",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
                        {runDays:1,runDate:"20140514",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
                        {runDays:1,runDate:"20140514",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
                        {runDays:1,runDate:"20140514",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
                    ]
                },
                {
                    trainName:"K817",
                    startStn:"北京西",//始发站名 为null时填入""（空串）
                    endStn:"成都",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140515",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
                        {runDays:0,runDate:"20140515",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
                        {runDays:1,runDate:"20140516",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
                        {runDays:1,runDate:"20140516",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
                        {runDays:1,runDate:"20140516",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
                        {runDays:1,runDate:"20140516",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
                        {runDays:1,runDate:"20140516",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
                        {runDays:1,runDate:"20140516",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
                        {runDays:1,runDate:"20140516",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
                        {runDays:1,runDate:"20140516",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
                    ]
                }
            ]
        },
        //end 一条交路



        {
            crossName:"K818-K817-2",
            jxgx:[//接续关系数组
                {fromStnName:"北京西",fromRunDate:"20140512",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140513",toTime:"08:35"}
            ],
            trains:[//该交路下所有车次数组
                {
                    trainName:"K818",
                    startStn:"成都",//始发站名 为null时填入""（空串）
                    endStn:"北京西",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140511",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
                        {runDays:0,runDate:"20140511",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
                        {runDays:0,runDate:"20140511",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
                        {runDays:0,runDate:"20140511",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
                        {runDays:0,runDate:"20140511",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
                        {runDays:1,runDate:"20140512",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
                        {runDays:1,runDate:"20140512",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
                        {runDays:1,runDate:"20140512",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
                        {runDays:1,runDate:"20140512",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
                    ]
                },
                {
                    trainName:"K817",
                    startStn:"北京西",//始发站名 为null时填入""（空串）
                    endStn:"成都",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140513",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
                        {runDays:0,runDate:"20140513",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
                        {runDays:1,runDate:"20140514",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
                        {runDays:1,runDate:"20140514",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
                        {runDays:1,runDate:"20140514",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
                        {runDays:1,runDate:"20140514",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
                        {runDays:1,runDate:"20140514",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
                        {runDays:1,runDate:"20140514",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
                        {runDays:1,runDate:"20140514",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
                        {runDays:1,runDate:"20140514",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
                    ]
                }
            ]
        },
        //end 一条交路



        {
            crossName:"K818-K817-3",
            jxgx:[//接续关系数组
                {fromStnName:"北京西",fromRunDate:"20140513",fromTime:"21:07",toStnName:"北京西",toRunDate:"20140514",toTime:"08:35"}
            ],
            trains:[//该交路下所有车次数组
                {
                    trainName:"K818",
                    startStn:"成都",//始发站名 为null时填入""（空串）
                    endStn:"北京西",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140512",stnName:"成都",arrTime:"19:18",dptTime:"19:18",stayTime:0},
                        {runDays:0,runDate:"20140512",stnName:"遂宁",arrTime:"21:19",dptTime:"21:33",stayTime:14},
                        {runDays:0,runDate:"20140512",stnName:"南充",arrTime:"22:15",dptTime:"22:22",stayTime:7},
                        {runDays:0,runDate:"20140512",stnName:"蓬安",arrTime:"22:49",dptTime:"22:54",stayTime:5},
                        {runDays:0,runDate:"20140512",stnName:"营山",arrTime:"23:06",dptTime:"23:11",stayTime:5},
                        {runDays:1,runDate:"20140513",stnName:"达州",arrTime:"00:13",dptTime:"00:23",stayTime:10},
                        {runDays:1,runDate:"20140513",stnName:"安康",arrTime:"03:12",dptTime:"03:32",stayTime:20},
                        {runDays:1,runDate:"20140513",stnName:"华山",arrTime:"07:45",dptTime:"07:51",stayTime:6},
                        {runDays:1,runDate:"20140513",stnName:"北京西",arrTime:"21:07",dptTime:"21:07",stayTime:0}
                    ]
                },
                {
                    trainName:"K817",
                    startStn:"北京西",//始发站名 为null时填入""（空串）
                    endStn:"成都",//终到站名为null时填入""（空串）
                    trainStns:[
                        {runDays:0,runDate:"20140514",stnName:"北京西",arrTime:"08:35",dptTime:"08:35",stayTime:0},
                        {runDays:0,runDate:"20140514",stnName:"华山",arrTime:"23:07",dptTime:"23:13",stayTime:6},
                        {runDays:1,runDate:"20140515",stnName:"安康",arrTime:"03:59",dptTime:"04:13",stayTime:14},
                        {runDays:1,runDate:"20140515",stnName:"达州",arrTime:"07:29",dptTime:"07:35",stayTime:6},
                        {runDays:1,runDate:"20140515",stnName:"土溪",arrTime:"08:09",dptTime:"08:13",stayTime:4},
                        {runDays:1,runDate:"20140515",stnName:"营山",arrTime:"08:45",dptTime:"08:49",stayTime:4},
                        {runDays:1,runDate:"20140515",stnName:"蓬安",arrTime:"09:02",dptTime:"09:05",stayTime:3},
                        {runDays:1,runDate:"20140515",stnName:"南充",arrTime:"09:32",dptTime:"09:36",stayTime:4},
                        {runDays:1,runDate:"20140515",stnName:"遂宁",arrTime:"10:30",dptTime:"10:33",stayTime:3},
                        {runDays:1,runDate:"20140515",stnName:"成都",arrTime:"12:30",dptTime:"12:30",stayTime:0}
                    ]
                }
            ]
        }
        //end 一条交路


    ]
};