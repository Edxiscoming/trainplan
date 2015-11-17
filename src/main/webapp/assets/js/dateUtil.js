function getDateCha(beginDate,endDate){  
    var res={D:0,H:0,M:0,S:0,abs:true,error:false};  
    //属性形式验证：第一次参数必须是Date类型，第二个参数可以为空，默认为new Date()  
    if(typeof(endDate)=="undefined" || null== endDate||""==endDate ){endDate = new Date();}  
    if( !(beginDate instanceof (Date)) ||  !(endDate instanceof (Date))){  
        res.error=true;//"非法时间字符串";  
        return res;  
    }  
  
    //比较大小，保证差值一定是正数。  
    if(beginDate>endDate){  
        var tempDate = beginDate;  
        beginDate = endDate;  
        endDate=tempDate;  
        res.abs=false;//表示beginDate大于endDate  
    }  
    var chaTime =(endDate.getTime()-beginDate.getTime());  
      
    var Day_Param  =1000*60*60*24;//一天等于毫秒数  
    var Hour_Param = 1000*60*60;//一小时等于毫秒数  
    res.D =Math.floor(chaTime/(Day_Param));//  
  
    chaTime = chaTime-res.D*Day_Param;//减去天的毫秒数。再求小时个数  
    res.H = Math.floor(chaTime/(Hour_Param));  
    chaTime = chaTime-res.H*Hour_Param;//减去小时的毫秒数。再求分钟个数  
    res.M = Math.floor(chaTime/(1000*60));  
    res.S=(chaTime-res.M*1000*60)/1000;//减去分钟的毫秒数。再求秒的个数  
    //alert(res.S);  
  
    res.toString=function(){  
        //return this.D+"日"+this.H+"小时"+this.M+"分钟"; 
    	return this.D;
    };  
    return res;  
      
}  