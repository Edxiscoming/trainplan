/**
 * @param context
 *            (必选参数)画布对象
 * @param xDateArray
 *            (必选参数)日期数组 用于绘制x轴 //日期段，x轴刻度 [ {runDate:"2014-05-10"},//（必选）
 *            {runDate:"2014-05-11"} ]
 * @param stnArray
 *            (必选参数)交路经由站数组 用于绘制y轴 [
 *            {stnName:"成都",stationType:'0'},//stnName（必选）
 *            {stnName:"遂宁",isCurrentBureau:1},//isCurrentBureau：（可选）是否为当前局
 *            0：否，1：是 {stnName:"南充",stationType:'TZ',isCurrentBureau:1},
 *            {stnName:"蓬安",stationType:'FJK'}, {stnName:"营山",stationType:'BT'} ]
 * @param expandObj
 *            (可选参数) 缩放比例参数对象 { startX : 120,
 *            //(可选参数)x起始位置（注意是指第一条竖线位置，站名位置在该位置左移） startY : 50, //(可选参数)y起始位置
 *            xScale : 10, //(可选参数)x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数 （建议整数：1、2、10等）
 *            xScaleCount : 1, //(可选参数)x轴放大总倍数 yScale : 1, //(可选参数)y轴缩放比例 除数
 *            stationTypeArray:['0','TZ','FJK','BT'], //绘图条件数组 0:始发站或终到站 TZ：停站
 *            FJK：分界口站 BT:不停的经由站 isDrawTrainTime : true //是否绘制列车经由站到达及出发时间 }
 */

var MyCanvasComponent = function(context, xDateArray, stnArray, expandObj) {
	var _canvasComponentSelf = this;
	var _context = context;
	var _xDateArray = xDateArray;
	// var _stationTypeArray = [];//['0','TZ','FJK','BT'] 绘图条件数组 0:始发站或终到站 TZ：停站
	// FJK：分界口站 BT:不停的经由站
	var _stnArray = [];
	var _stnOffsetY = 45; // 第一个站横虚线y对于_startY（画布y起始位置）的偏移量
	var _startX = 150; // 默认100 x起始位置
	var _startY = 50; // 默认100 y起始位置
	var _stepX = 1; // 默认1 x步长 每一分钟X轴步长为1px
	var _stepY = 100; // 默认100 y步长
	var _xScale = 10; // 默认10 x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数 （建议整数：1、2、10等）
	var _xScaleCount = 1; // 默认1 用于确定每天时刻竖线条数
	var _yScale = 1; // 默认10 x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数
	var _drawYMoreFlag = false; // 默认false 每一天x长度范围内是否绘制更多竖线 true/false
	var _endX = 1000; // x轴 日期范围最大刻度值
	var _endY = 1000; // y轴 结束刻度
	var _groupSerialNbrArray = [ "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨" ];// 列车所属组号①②③④⑤⑥⑥⑦⑧⑨
	var _isDrawTrainTime = false; // 是否绘制列车经由站到达及出发时间 默认false
	var _isChangeColorByUser = false; // 是否根据当前用户是否为总公司用户而改变网格X线颜色 总公司用户不限制
	// false，路局用户则非管内站线变暗 true

	var _fjkFlagStr = "FJK"; // 分界口标识字符串变量，定义为变量 方便后期维护更改 用于drawGridX

	initVariables();// 初始化

	// ================ 初始化赋值 ================
	/**
	 * private
	 */
	function initVariables() {
		// 解析生成新的纵坐标数组 判断是否要屏蔽部分车站数据，如：只绘制起止及分界口、绘制起止及停靠站等
		if (expandObj && expandObj.stationTypeArray
				&& expandObj.stationTypeArray != "undefine"
				&& expandObj.stationTypeArray.length > 0) {
			for (var i = 0, _len = stnArray.length; i < _len; i++) {
				if ($.inArray(stnArray[i].stationType,
						expandObj.stationTypeArray) > -1) {
					_stnArray.push(stnArray[i]);
				}
			}

			// _stationTypeArray = expandObj.stationTypeArray;
		} else {
			_stnArray = stnArray;
		}

		// 当纵坐标数量大于3时， 纵向步长设置为504
		if (_stnArray.length > 3) {
			_stepY = 40;
		}

		if (expandObj && expandObj.xScale && !isNaN(expandObj.xScale)) {
			_xScale = expandObj.xScale;
		}
		if (expandObj && expandObj.xScaleCount && !isNaN(expandObj.xScaleCount)) {
			_xScaleCount = expandObj.xScaleCount;
		}
		if (expandObj && expandObj.yScale && !isNaN(expandObj.yScale)) {
			_yScale = expandObj.yScale;
		}
		if (expandObj && expandObj.startX && !isNaN(expandObj.startX)) {
			_startX = expandObj.startX;
		}
		if (expandObj && expandObj.startY && !isNaN(expandObj.startY)) {
			_startY = expandObj.startY;
		}

		if (expandObj && typeof expandObj.isZgsUser == "boolean") {
			_isChangeColorByUser = !expandObj.isZgsUser;
		}

		if (expandObj && typeof expandObj.isDrawTrainTime != null
				&& expandObj.isDrawTrainTime != "undefine"
				&& typeof expandObj.isDrawTrainTime == "boolean") {
			_isDrawTrainTime = expandObj.isDrawTrainTime;
		}

		if (_xDateArray.length <= 2) {
			_xScale = 4 / _xScaleCount; // x轴缩放比例 x轴时间（分钟）转换为长度像素px的除数
			_drawYMoreFlag = true; // 每一天x长度范围内需要绘制更多竖线
		}

		_endX = _stepX * _xDateArray.length * 24 * 60 / _xScale + _startX; // x轴
		// 日期范围最大刻度值
		_endY = _stnArray.length * _stepY / _yScale + _startY + _stnOffsetY;
		// 设置画布x、y长度 +100是为了方便显示边缘线
		_context.canvas.width = _endX + 100;
		_context.canvas.height = _endY + 100;

	}
	;

	/**
	 * 获取站点在经由站数组中的index private
	 * 
	 * @param stnName
	 *            站点名称
	 * 
	 * return -1时表示经由站中不存在该站名
	 */
	function getStnArcYIndex(stnName) {
		for (var i = 0, _len = _stnArray.length; i < _len; i++) {
			if (_stnArray[i].stnName == stnName) {
				return i;
			}
		}

		return -1;
	}
	;

	/**
	 * 获取站点x标宽度 oneDayWidth倍数 private
	 * 
	 * @param runDate
	 *            运行日期 yyyy-MM-dd
	 */
	function getStnArcXIndex(runDate) {
		for (var i = 0, _len = _xDateArray.length; i < _len; i++) {
			if (_xDateArray[i].runDate == runDate) {
				return i;
			}
		}
	}
	;

	/**
	 * 获取x坐标 private
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm
	 */
	function getX(time) {
		var _date = moment(time).format("YYYY-MM-DD");
		var _hour = moment(time).format("HH");
		var _minute = moment(time).format("mm");

		var _oneDayWidth = _stepX * 24 * 60 / _xScale; // 一天的x宽度
		var _dayWidth = getStnArcXIndex(_date) * _oneDayWidth; // x平移天数刻度
		var _minuteWidth = (parseInt(_hour) * 60 + parseInt(_minute)) * _stepX
				/ _xScale;
		var _x = _startX + _dayWidth + _minuteWidth;
		return _x;
	}
	;

	/**
	 * 获取y坐标 private
	 * 
	 * @param stnName
	 *            站名
	 */
	function getY(stnName) {
		var _y = _startY + getStnArcYIndex(stnName) * _stepY / _yScale
				+ _stnOffsetY; // 该车站Y标
		return _y;
	}
	;

	/**
	 * 绘制网格X private
	 * 
	 * @param color
	 *            //颜色
	 */
	function drawGridX(color) {
		var _fillTextStartX = _startX - 20; // 站台名称开始点X
		var _xDashedLineEnd = _endX + 20; // 每站虚线（横向）x终点 +20是为了造成延伸效果
		var _y = 0;
		for (var i = 0, _len = _stnArray.length; i < _len; i++) {
			var _obj = _stnArray[i];
			_y = _startY + i * _stepY / _yScale + _stnOffsetY;//
			_context.lineWidth = 1;// 图形边框宽度
			myCanvasFillText(_context, {
				font : "normal 14px Arial",
				textAlign : "right",
				text : _obj.stnName,
				fromX : _fillTextStartX,
				fromY : _y + 5
			});

			if (_fjkFlagStr == _obj.stationType) {
				_color = "#000000";
				_context.lineWidth = 2;// 图形边框宽度
			} else if (!_isChangeColorByUser
					|| (_obj.isCurrentBureau && _obj.isCurrentBureau == 1)) {// 该站属于当前路局管内
				// 是否根据当前用户是否为总公司用户而改变网格X线颜色 总公司用户不限制 false，路局用户则非管内站线变暗 true
				_color = color;// #c101db
			} else {
				_color = "gray";// gray、浅绿#eefde3、#c101db
			}

			_context.strokeStyle = _color;// "green";
			_context.dashedLineTo(_startX - 10, _y, _xDashedLineEnd, _y, 10);// 横虚线
			// 10:虚线间隔10px
		}
	}
	;

	/**
	 * 
	 * @param dashFlag
	 *            是否为虚线 private
	 */
	function drawY(color, lineWidth, text, fromX, dashFlag, font) {
		if (dashFlag) {
			myCanvasDrawDashLineY(_context, lineWidth, color, fromX, _startY,
					fromX, _endY, 10);
		} else {
			myCanvasDrawFullLineY(_context, lineWidth, color, fromX, _startY,
					_endY);
		}
		// 顶端显示
		myCanvasFillText(_context, {
			font : font,
			textAlign : "center",
			text : text,// 0 6 12 18
			fromX : fromX,
			fromY : _startY - 5
		});
		// 底端显示
		myCanvasFillText(_context, {
			font : font,
			textAlign : "center",
			text : text,// 0 6 12 18
			fromX : fromX,
			fromY : _endY + 15
		});
	}
	;

	/**
	 * 绘制网格Y _xDateArray2天以上， 每天绘制5条线(包含0点、6点、12点、18点、0点)
	 * 
	 * 为避免循环中做逻辑if判断（会降低性能）。所以未与drawGridMoreY合并 private
	 * 
	 * @param color
	 *            //颜色
	 */
	function drawGridY(color) {
		// 画竖线
		var _halfHourWidth = 30 * _stepX / _xScale; // 半小时
		var _oneHourWidth = 60 * _stepX / _xScale;
		var _oclock6Width = 6 * _oneHourWidth;
		var _oclock12Width = 12 * _oneHourWidth;
		var _oclock18Width = 18 * _oneHourWidth;
		var _oneDayWidth = 24 * _oneHourWidth;

		for (var i = 0, _len = _xDateArray.length; i < _len; i++) {
			// 网格顶端显示 2014-05-12 一天范围内的 日期文本显示
			myCanvasFillText(_context, {
				font : "Bold 16px Arial",
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _startY - 35
			});
			// 网格底端显示 2014-05-12
			myCanvasFillText(_context, {
				font : "Bold 16px Arial",
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _endY + 35
			});

			//
			if (_xScaleCount <= 2) {// 0点、6点、12点、18点
				drawY(color, 3, "0", _startX + i * _oneDayWidth, false,
						"Bold 20px Arial");// 0点
				drawY(color, 0.5, "6", _startX + _oclock6Width + i
						* _oneDayWidth, true, "normal 14px Arial");// 6点
				drawY(color, 1, "12", _startX + _oclock12Width + i
						* _oneDayWidth, false, "Bold 16px Arial");// 12点
				drawY(color, 0.5, "18", _startX + _oclock18Width + i
						* _oneDayWidth, true, "normal 14px Arial");// 18点
			} else if (_xScaleCount > 2 && _xScaleCount < 8) { // 每小时一条线

				for (var j = 0; j < 24; j++) {
					var _isDashLine = false; // 是否为虚线
					var _lineWidth = false; // 线宽
					if (j % 12 == 0) {
						_isDashLine = false;
					} else {
						_isDashLine = true;
					}
					if (j == 0) {
						_lineWidth = 3;// 线宽
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"Bold 20px Arial");// 0点
					} else if (j % 12 == 0) {
						_lineWidth = 1;
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"Bold 16px Arial");// 12点
					} else {
						_lineWidth = 0.5;
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"normal 14px Arial");// 每小时
					}

				}
			} else if (_xScaleCount >= 8) { // 每半小时一条线
				for (var j = 0; j < 48; j++) {
					var _isDashLine = true; // 是否为虚线
					var _lineWidth = 0.5; // 线宽
					var _text = j / 2;

					if (j == 0) {
						_text = "0";
						_isDashLine = false;
						_lineWidth = 3;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 20px Arial");// 0点
					} else if (j % 36 == 0) {
						_isDashLine = false;
						_lineWidth = 1;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 12px Arial");// 18点
					} else if (j % 24 == 0) {
						_isDashLine = false;
						_lineWidth = 2;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 16px Arial");// 12点
					} else if (j % 12 == 0) {
						_isDashLine = false;
						_lineWidth = 1;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 12px Arial");// 6点
					} else if (j % 2 == 0) {
						_lineWidth = 0.5;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 11px Arial");// 每一小时
					} else if (j % 2 == 1) {// 半小时
						_lineWidth = 0.2;// 线宽
						_text = Math.floor(j / 2) + ":30";
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "lighter 11px Arial");// 每半小时
					}

				}
			}

			// x结束位置 再画一条竖线
			if (i == _len - 1) {
				drawY(color, 3, "0", _startX + (i + 1) * _oneDayWidth, false,
						"Bold 20px Arial");// 24点
			}
		}
	}
	;

	/**
	 * 绘制网格Y _xDateArray2天以下， 每天绘制5条线(包含0点、6点、12点、18点、0点)
	 * 
	 * 为避免循环中做逻辑if判断（会降低性能）。所以未与drawGridY合并 private
	 * 
	 * @param color
	 *            //颜色
	 */
	function drawGridMoreY(color) {
		// 画竖线
		var _halfHourWidth = 30 * _stepX / _xScale; // 半小时
		var _oneHourWidth = 60 * _stepX / _xScale;
		var _oclock3Width = 3 * _oneHourWidth;
		var _oclock6Width = 6 * _oneHourWidth;
		var _oclock9Width = 9 * _oneHourWidth;
		var _oclock12Width = 12 * _oneHourWidth;
		var _oclock15Width = 15 * _oneHourWidth;
		var _oclock18Width = 18 * _oneHourWidth;
		var _oclock21Width = 21 * _oneHourWidth;
		var _oneDayWidth = 24 * _oneHourWidth;

		for (var i = 0, _len = _xDateArray.length; i < _len; i++) {
			// 一天范围内的 日期文本显示 如：2014-05-12
			myCanvasFillText(_context, {
				font : "Bold 16px Arial",
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _startY - 30
			});
			// 网格底端显示 2014-05-12
			myCanvasFillText(_context, {
				font : "Bold 16px Arial",
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _endY + 35
			});

			if (_xScaleCount <= 2) {// 包含0点、6点、9点、12点、15点、18点、21点、0点
				drawY(color, 3, "0", _startX + i * _oneDayWidth, false,
						"Bold 20px Arial");// 0点
				drawY(color, 0.5, "3", _startX + _oclock3Width + i
						* _oneDayWidth, true, "normal 12px Arial");// 3点
				drawY(color, 1, "6",
						_startX + _oclock6Width + i * _oneDayWidth, true,
						"normal 14px Arial");// 6点
				drawY(color, 0.5, "9", _startX + _oclock9Width + i
						* _oneDayWidth, true, "normal 12px Arial");// 9点
				drawY(color, 1, "12", _startX + _oclock12Width + i
						* _oneDayWidth, false, "Bold 16px Arial");// 12点
				drawY(color, 0.5, "15", _startX + _oclock15Width + i
						* _oneDayWidth, true, "normal 12px Arial");// 15点
				drawY(color, 1, "18", _startX + _oclock18Width + i
						* _oneDayWidth, true, "normal 14px Arial");// 18点
				drawY(color, 0.5, "21", _startX + _oclock21Width + i
						* _oneDayWidth, true, "normal 12px Arial");// 21点
			} else if (_xScaleCount > 2 && _xScaleCount < 8) { // 每小时一条线

				for (var j = 0; j < 24; j++) {
					var _isDashLine = false; // 是否为虚线
					var _lineWidth = false; // 线宽
					if (j % 12 == 0) {
						_isDashLine = false;
					} else {
						_isDashLine = true;
					}
					if (j == 0) {
						_lineWidth = 3;// 线宽
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"Bold 20px Arial");// 0点
					} else if (j % 12 == 0) {
						_lineWidth = 1;
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"Bold 16px Arial");// 12点
					} else {
						_lineWidth = 0.5;
						drawY(color, _lineWidth, j, _startX + i * _oneDayWidth
								+ j * _oneHourWidth, _isDashLine,
								"normal 14px Arial");// 每小时
					}

				}
			} else if (_xScaleCount >= 8) { // 每半小时一条线
				for (var j = 0; j < 48; j++) {
					var _isDashLine = true; // 是否为虚线
					var _lineWidth = 0.5; // 线宽
					var _text = j / 2;
					if (j == 0) {
						_text = "0";
						_isDashLine = false;
						_lineWidth = 3;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 20px Arial");// 0点
					} else if (j % 36 == 0) {
						_isDashLine = false;
						_lineWidth = 1;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 12px Arial");// 18点
					} else if (j % 24 == 0) {
						_isDashLine = false;
						_lineWidth = 2;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 16px Arial");// 12点
					} else if (j % 12 == 0) {
						_isDashLine = false;
						_lineWidth = 1;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 12px Arial");// 6点
					} else if (j % 2 == 0) {
						_lineWidth = 0.5;// 线宽
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "Bold 11px Arial");// 每一小时
					} else if (j % 2 == 1) {// 半小时
						_lineWidth = 0.2;// 线宽
						_text = Math.floor(j / 2) + ":30";
						drawY(color, _lineWidth, _text, _startX + i
								* _oneDayWidth + j * _halfHourWidth,
								_isDashLine, "lighter 11px Arial");// 每半小时
					}

				}
			}

			// x结束位置 再画一条竖线
			if (i == _len - 1) {
				drawY(color, 3, "0", _startX + (i + 1) * _oneDayWidth, false,
						"Bold 20px Arial");// 24点
			}
		}
	}
	;

	/**
	 * 绘制网格 public
	 * 
	 * @param color
	 *            网格颜色
	 */
	this.drawGrid = function(color) {
		drawGridX(color); // 绘制x轴
		if (_drawYMoreFlag) {// 默认false 每一天x长度范围内是否绘制更多竖线 true/false
			drawGridMoreY(color);
		} else {
			drawGridY(color); // 绘制y轴
		}

	};

	/**
	 * 将列车所属组号转换为特殊字符 1=① 2=② 3=③ 4=④ 5=⑤ public
	 * 
	 * @param groupSerialNbr
	 *            列车所属组号 如："1" "2" "3" "4" return 匹配成功返回:①②③④⑤⑥⑥⑦⑧⑨ 无任何匹配数据：no
	 *            match
	 */
	this.convertGroupSerialNbr = function(groupSerialNbr) {
		for (var i = 1; i <= 9; i++) {
			if (i.toString() == groupSerialNbr) {
				return _groupSerialNbrArray[i - 1];
			}
		}

		return "no match";// 无任何匹配数据
	};

	/**
	 * 绘制列车线 public
	 * 
	 * @param flag
	 *            //是否画起止箭头标识 true/flase
	 * @param color
	 *            //线条颜色 #ffffff
	 * @param paramObj
	 *            单个车次信息 { trainName:"K818", startStn:"成都", //始发站名
	 *            为null时填入""（空串） startDate: "2014-06-11 13:30:00", //始发时间
	 *            endStn:"北京西", //终到站名为null时填入""（空串） endDate: "2014-06-11
	 *            13:35:00", //终到时间 groupSerialNbr:'1', //车底组号①②③④⑤⑥⑥⑦⑧⑨
	 *            trainStns:[ {runDays:0,stnName:"成都",arrTime:"2014-05-11
	 *            19:18",dptTime:"2014-05-11 19:18",stayTime:0},
	 *            {runDays:0,stnName:"遂宁",arrTime:"2014-05-11
	 *            21:19",dptTime:"2014-05-11 21:33",stayTime:14},
	 *            {runDays:0,stnName:"南充",arrTime:"2014-05-11
	 *            22:15",dptTime:"2014-05-11 22:22",stayTime:7},
	 *            {runDays:0,stnName:"蓬安",arrTime:"2014-05-11
	 *            22:49",dptTime:"2014-05-11 22:54",stayTime:5},
	 *            {runDays:0,stnName:"营山",arrTime:"2014-05-11
	 *            23:06",dptTime:"2014-05-11 23:11",stayTime:5},
	 *            {runDays:1,stnName:"达州",arrTime:"2014-05-12
	 *            00:13",dptTime:"2014-05-12 00:23",stayTime:10},
	 *            {runDays:1,stnName:"安康",arrTime:"2014-05-12
	 *            03:12",dptTime:"2014-05-12 03:32",stayTime:20},
	 *            {runDays:1,stnName:"华山",arrTime:"2014-05-12
	 *            07:45",dptTime:"2014-05-12 07:51",stayTime:6},
	 *            {runDays:1,stnName:"北京西",arrTime:"2014-05-12
	 *            21:07",dptTime:"2014-05-12 21:07",stayTime:0} ] }
	 */
	this.drawTrainRunLine = function(flagParam, colorParam, paramObj) {
		var line_self = this;
		this.flag = flagParam; // 是否绘制起止标记
		this.color = colorParam;// 初始颜色
		this.obj = paramObj; // 单个车次及其经由信息
		this.isCurrent = false;
		this.lineWidth = 2;// 线宽
		this.firstX = 0;
		this.firstY = 0;

		this.showTrainName = function(ctx, color, groupSerialNbr) {
			var offsetY = 0;
			var trainNameOffsetX = 0;
			var directionY = getDirectionY(this.obj.trainStns[0].stnName);
			var groupSerialNbrY = 0;
			if (groupSerialNbr != null && groupSerialNbr != "undefine") {
				groupSerialNbrY = parseInt(groupSerialNbr) * 5;
			} else {
				trainNameOffsetX = -20; // 当组号不存在时，因不需要绘制组号，所以列车名位置需左移
			}

			if ("up" == directionY) {
				offsetY = -25 - groupSerialNbrY;
			} else if ("down" == directionY) {
				offsetY = 25 + groupSerialNbrY;
			}

			// 绘制列车车次
			myCanvasFillTextWithColor(ctx, color, {
				font : "normal 14px Arial",
				textAlign : "left",
				text : this.obj.trainName,
				fromX : this.firstX + trainNameOffsetX + 10,
				fromY : this.firstY + offsetY
			});

			if (groupSerialNbr != null && groupSerialNbr != ""
					&& groupSerialNbr != "undefine") {
				myCanvasFillTextWithColor(ctx, color, {
					font : "Bold 18px Arial",
					textAlign : "left",
					text : _canvasComponentSelf
							.convertGroupSerialNbr(groupSerialNbr),// 绘制列车所属组号①②③④⑤⑥⑥⑦⑧⑨
					fromX : this.firstX - 10,
					fromY : this.firstY + offsetY
				});
			}

		};
		this.draw = function() {
			var _y = 0; // 到达站和出发站的y坐标
			var _arrTimeX = 0;// 计算到达点x标
			var _dptTimeX = 0;// 计算出发点x标
			var _parentDeptStn = {}; // 上一站出发点x y坐标
			var _len = this.obj.trainStns.length;

			// 绘制起止标记,单车的，列车运行线
			if (this.flag && _len > 0) {

				// if(this.obj.startStn == this.obj.trainStns[0].stnName)
				// {//列车经由第一站==始发站 则绘制开始标记
				// drawTrainStartArrows(colorParam, this.obj.trainStns[0], 0,
				// this.obj.trainStns[1]);
				// }
				//				
				// if(this.obj.endStn == this.obj.trainStns[_len-1].stnName)
				// {//列车经由最后一站==终到站 则绘制终到标记
				// drawTrainEndArrows(colorParam, this.obj.trainStns[_len-1], 0,
				// this.obj.trainStns[_len-2]);
				// }

				if (this.obj.startStn == this.obj.trainStns[0].stnName) {// 列车经由第一站==始发站
					// 则绘制开始标记
					drawTrainStartArrows(colorParam, this.obj.trainStns[0], 0,
							this.obj.endStn);
				}

				if (this.obj.endStn == this.obj.trainStns[_len - 1].stnName) {// 列车经由最后一站==终到站
					// 则绘制终到标记
					drawTrainEndArrows(colorParam,
							this.obj.trainStns[_len - 1], 0, this.obj.startStn);
				}
			}

			// 绘制列车运行线
			for (var i = 0; i < _len; i++) {
				var _obj = this.obj.trainStns[i];

				// 屏蔽不在显示要求范围内的数据
				// if (getStnArcYIndex(_obj.stnName) < 0 ||
				// $.inArray(_obj.stationType, _stationTypeArray) < 0)
				// {//该站不存在于纵坐标数组中或站类型不在显示范围内 当数据字典完善后，重新启用上面的注释条件
				if (getStnArcYIndex(_obj.stnName) < 0) {// 该站不存在于纵坐标数组中或站类型不在显示范围内
					continue;
				}

				_y = getY(_obj.stnName); // 该车站Y标
				_arrTimeX = getX(_obj.arrTime);// 计算到达点x标
				_dptTimeX = getX(_obj.dptTime);// 计算出发点x标

				// 绘制到达点
				_context.moveTo(_arrTimeX, _y);
				_context.arc(_arrTimeX, _y, 2, 0, Math.PI * 2, false);

				// 绘制出发点
				_context.moveTo(_dptTimeX, _y);
				_context.arc(_dptTimeX, _y, 2, 0, Math.PI * 2, false);

				// 是否绘制列车经由站到达及出发时间
				if (_isDrawTrainTime) {
					if (_obj.arrTime == _obj.dptTime) {// 始发 or 终到 or 经过站
						// 始发 or 经过站
						if (_obj.stnName != this.obj.endStn) {
							myCanvasFillText(_context, {
								font : "lighter 10px Arial",
								textAlign : "center",
								text : moment(_obj.dptTime).format("HH:mm"),// 0 6
								// 12
								// 18
								fromX : _dptTimeX + 20,
								fromY : _y - 8
							});
						} else {// 终到站
							myCanvasFillText(_context, {
								font : "lighter 10px Arial",
								textAlign : "center",
								text : moment(_obj.arrTime).format("HH:mm"),// 0 6
								// 12
								// 18
								fromX : _arrTimeX - 20,
								fromY : _y + 10
							});
						}
					} else {
						// 显示到达时间
						myCanvasFillText(_context, {
							font : "lighter 10px Arial",
							textAlign : "center",
							text : moment(_obj.arrTime).format("HH:mm"),// 0 6
							// 12 18
							fromX : _arrTimeX - 20,
							fromY : _y + 10
						});

						// 显示出发时间
						myCanvasFillText(_context, {
							font : "lighter 10px Arial",
							textAlign : "center",
							text : moment(_obj.dptTime).format("HH:mm"),// 0 6
							// 12 18
							fromX : _dptTimeX + 20,
							fromY : _y - 8
						});
					}
				}

				if (i == 0) {
					// 绘制列车名称
					this.firstX = _dptTimeX;
					this.firstY = _y;
					line_self.showTrainName(_context, colorParam,
							this.obj.groupSerialNbr);
				} else {
					// 连接上一站出发点到本站到达点
					myCanvasDrawLine(_context, this.lineWidth, colorParam,
							_parentDeptStn.x, _parentDeptStn.y, _arrTimeX, _y);
				}

				// 连接本站到达点和出发点
				myCanvasDrawLine(_context, this.lineWidth, colorParam,
						_arrTimeX, _y, _dptTimeX, _y);

				// 保存上一站出发点x y坐标
				_parentDeptStn = {
					x : _dptTimeX,
					y : _y
				};
			}
		};
		this.isPointInStroke = function(ctx, x, y) {
			ctx.save();
			ctx.beginPath();

			line_self.draw();

			if (x && y
					&& (ctx.isPointInStroke(x, y) || ctx.isPointInPath(x, y))) {// 是判断某一点是否是在某个路径内
				return true;
			} else {
				return false;
			}
		};
		/**
		 * @param ctx
		 *            必选参数
		 * @param expandObj
		 *            可选参数 扩展对象 { x : 247, //可选参数 当前鼠标x坐标 y : 458, //可选参数
		 *            当前鼠标y坐标 trainNbr : "K818", //可选参数
		 *            车次号，外部传入条件，以便绘图时该车次高亮显示，类是鼠标选中效果 booleanShowTrainDetail :
		 *            true, //可选参数 是否显示该车次详细信息 groupSerialNbr : "1" //可选参数 列车组号 }
		 * 
		 */
		this.drawLine = function(ctx, expandObj) {
			ctx.save();
			ctx.beginPath();

			line_self.draw();
			if (expandObj != null
					&& ((expandObj.x != "undefined"
							&& expandObj.y != "undefined" && (ctx
							.isPointInStroke(expandObj.x, expandObj.y) || ctx
							.isPointInPath(expandObj.x, expandObj.y)))
							|| (expandObj.trainNbr != "undefined" && expandObj.trainNbr == this.obj.trainName) || (typeof expandObj.groupSerialNbr == "string" && expandObj.groupSerialNbr == this.obj.groupSerialNbr))) {
				var currentColor = "#ff0000";
				if ("#ff0000" == colorParam) {
					currentColor = getRandomColor();// 当前线条底色为红色时，鼠标选中颜色则重新随机产生
				}

				ctx.strokeStyle = currentColor;// 边框样式
				ctx.fillStyle = currentColor;// 填充的样式
				this.isCurrent = true;
				line_self.showTrainName(_context, currentColor,
						this.obj.groupSerialNbr);

				// 查看选中线 列车运行时刻信息
				if (expandObj.booleanShowTrainDetail) {
					runTimeModel.loadData(this.obj);
				}
			} else {
				this.isCurrent = false;
				line_self.showTrainName(_context, colorParam,
						this.obj.groupSerialNbr);
				ctx.strokeStyle = colorParam;// 边框样式
				ctx.fillStyle = colorParam;// //填充的样式
			}

			ctx.closePath();
			ctx.stroke();// 绘画
			ctx.fill();

		};

		// 根据车次和日期改变线的颜色
		this.drawLineByTrainNbrAndDay = function(c, ii, ctx, expandObject) {
			ctx.save();
			ctx.beginPath();
			line_self.draw();
			var day = c.obj.startDate;// 线的日期
			day = day.substring(0, 10);
			var trainNbr = c.obj.trainName;// 线的车次

			if (expandObject != null
					&& ((expandObject.x != "undefined"
							&& expandObject.y != "undefined" && (ctx
							.isPointInStroke(expandObject.x, expandObject.y) || ctx
							.isPointInPath(expandObject.x, expandObject.y)))
							|| (expandObject.trainNbr != "undefined" && expandObject.trainNbr == this.obj.trainName) || (typeof expandObject.groupSerialNbr == "string" && expandObject.groupSerialNbr == this.obj.groupSerialNbr))
					&& day == expandObject.day
					&& trainNbr == expandObject.trainNbr) {
				var currentColor = "#ff0000";
				if ("#ff0000" == colorParam) {
					currentColor = getRandomColor();// 当前线条底色为红色时，鼠标选中颜色则重新随机产生
				}

				console.log(expandObj.groupSerialNbr + ","
						+ this.obj.groupSerialNbr);
				console.log("进行描绘");

				ctx.strokeStyle = currentColor;// 边框样式
				ctx.fillStyle = currentColor;// 填充的样式
				this.isCurrent = true;
				line_self.showTrainName(_context, currentColor,
						this.obj.groupSerialNbr);

				// 查看选中线 列车运行时刻信息
				if (expandObj.booleanShowTrainDetail) {
					runTimeModel.loadData(this.obj);
				}
			} else {
				this.isCurrent = false;
				line_self.showTrainName(_context, colorParam,
						this.obj.groupSerialNbr);
				ctx.strokeStyle = colorParam;// 边框样式
				ctx.fillStyle = colorParam;// //填充的样式
			}

			ctx.closePath();
			ctx.stroke();// 绘画
			ctx.fill();

		};

		// 根据车次和日期改变线的颜色
		this.drawLineByList = function(c, ctx, expandObj, isEqu) {
			ctx.save();
			ctx.beginPath();

			line_self.draw();
			var day = c.obj.startDate;// 线的日期
			day = day.substring(0, 10);
			var trainNbr = c.obj.trainName;// 线的车次
			if (expandObj != null
					&& ((expandObj.x != "undefined"
							&& expandObj.y != "undefined" && (ctx
							.isPointInStroke(expandObj.x, expandObj.y) || ctx
							.isPointInPath(expandObj.x, expandObj.y)))
							|| (expandObj.trainNbr != "undefined" && expandObj.trainNbr == this.obj.trainName) || (typeof expandObj.groupSerialNbr == "string" && expandObj.groupSerialNbr == this.obj.groupSerialNbr))
					&& isEqu) {

				var currentColor = "#ff0000";
				if ("#ff0000" == colorParam) {
					currentColor = getRandomColor();// 当前线条底色为红色时，鼠标选中颜色则重新随机产生
				}
				ctx.strokeStyle = currentColor;// 边框样式
				ctx.fillStyle = currentColor;// 填充的样式
				this.isCurrent = true;
				line_self.showTrainName(_context, currentColor,
						this.obj.groupSerialNbr);

				// 查看选中线 列车运行时刻信息
				if (expandObj.booleanShowTrainDetail) {
					runTimeModel.loadData(this.obj);
				}
			} else {
				this.isCurrent = false;
				line_self.showTrainName(_context, colorParam,
						this.obj.groupSerialNbr);
				ctx.strokeStyle = colorParam;// 边框样式
				ctx.fillStyle = colorParam;// //填充的样式
			}

			ctx.stroke();// 绘画
			ctx.fill();
			ctx.closePath();

		};

	};

	/**
	 * 绘制开始标记 开始使用dptTime private
	 * 
	 * @param stnObj
	 *            列车某个站点信息 {runDays:0,stnName:"北京西",arrTime:"2014-05-12
	 *            08:35",dptTime:"2014-05-12 08:35",stayTime:0}
	 * @param groupSerialNbrIndex
	 *            （可选参数）组循环序号，避免与其他线重合
	 */
	function drawTrainStartArrows(colorParam, stnObj, groupSerialNbrIndex,
			nextEndStnObj) {
		var offsetY = 0;
		var directionY = null;

		if (typeof nextEndStnObj == "string") {
			directionY = getDirectionY(stnObj.stnName, null, nextEndStnObj);
		} else if (null != nextEndStnObj && 'undefind' != nextEndStnObj) {
			directionY = getDirectionY(stnObj.stnName, null,
					nextEndStnObj.stnName);
		} else {
			directionY = getDirectionY(stnObj.stnName);
		}

		var groupSerialNbrY = 0;
		if (groupSerialNbrIndex && typeof groupSerialNbrIndex == "number") {
			groupSerialNbrY = groupSerialNbrIndex * 5;
		}

		// console.log("directionY，," + directionY);

		if ("up" == directionY) {
			offsetY = -10 - groupSerialNbrY;
		} else if ("down" == directionY) {
			offsetY = 10 + groupSerialNbrY;
		}

		// 坐标定位
		fromX = getX(stnObj.dptTime);
		fromY = getY(stnObj.stnName);

		// 绘线
		myCanvasDrawLine(_context, 2, colorParam, fromX - 5, fromY + offsetY,
				fromX + 5, fromY + offsetY);// 横线
		myCanvasDrawLine(_context, 2, colorParam, fromX, fromY + offsetY,
				fromX, fromY);// 竖线
	}
	;

	/**
	 * 绘制终到标记 三角形 终到使用arrTime private
	 * 
	 * @param 车站对象
	 *            {runDays:0,stnName:"北京西",arrTime:"2014-05-12
	 *            08:35",dptTime:"2014-05-12 08:35",stayTime:0}
	 * @param groupSerialNbrIndex
	 *            （可选参数）组循环序号，避免与其他线重合
	 * @param 车站对象
	 *            {runDays:0,stnName:"北京西",arrTime:"2014-05-12
	 *            08:35",dptTime:"2014-05-12 08:35",stayTime:0} （可选参数）
	 */
	function drawTrainEndArrows(colorParam, endStnObj, groupSerialNbrIndex,
			preEndStnObj) {
		// 坐标定位
		fromX = getX(endStnObj.arrTime);
		fromY = getY(endStnObj.stnName);

		var offsetY = 0;
		var directionY = null;

		if (typeof preEndStnObj == "string") {
			directionY = getDirectionY(endStnObj.stnName, null, preEndStnObj);
		} else if (null != preEndStnObj && 'undefind' != preEndStnObj) {
			directionY = getDirectionY(endStnObj.stnName, null,
					preEndStnObj.stnName);
		} else {
			directionY = getDirectionY(endStnObj.stnName);
		}

		var groupSerialNbrY = 0;
		if (groupSerialNbrIndex && typeof groupSerialNbrIndex == "number") {
			groupSerialNbrY = groupSerialNbrIndex * 5;
		}

		if ("up" == directionY) {
			offsetY = -15 - groupSerialNbrY;
			// 绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX,
					fromY + offsetY);// 竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX - 5, fromY
					+ offsetY, fromX + 5, fromY + offsetY);// 三角形横线
			myCanvasDrawLine(_context, 2, colorParam, fromX - 5, fromY
					+ offsetY, fromX, fromY + offsetY - 10);// 三角形左斜线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY + offsetY
					- 10, fromX + 5, fromY + offsetY);// 三角形右斜线
		} else if ("down" == directionY) {
			offsetY = 15 + groupSerialNbrY;

			// 绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX,
					fromY + offsetY);// 竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX - 5, fromY
					+ offsetY, fromX + 5, fromY + offsetY);// 三角形横线
			myCanvasDrawLine(_context, 2, colorParam, fromX - 5, fromY
					+ offsetY, fromX, fromY + offsetY + 10);// 三角形左斜线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY + offsetY
					+ 10, fromX + 5, fromY + offsetY);// 三角形右斜线
		}

		_context.stroke();// 绘画
	}
	;

	/**
	 * 列车起止站标记 y坐标方向 2个站确定方向 规则说明：当currentStnName = 交路经由站数组中第一个站时，y向上
	 * 当currentStnName = 交路经由站数组中最后一个站时，y向下
	 * 
	 * @param currentStnName
	 *            当前站
	 * @param preStnName
	 *            当前站的上一个站 private
	 */
	function getDirectionY(currentStnName, preStnName, nextStnName) {
		var _len = _stnArray.length;
		if (_len == 0)
			return ""; // 经由站为空

		// console.log("~~~ currentStnName="+currentStnName +"
		// getStnArcYIndex(currentStnName)="+getStnArcYIndex(currentStnName));
		// console.log("~~~ preStnName="+preStnName +"
		// getStnArcYIndex(preStnName)="+getStnArcYIndex(preStnName));

		var _currentStnNameIndex = getStnArcYIndex(currentStnName);
		var _preStnNameIndex = getStnArcYIndex(preStnName);
		var _nextStnNameIndex = getStnArcYIndex(nextStnName);

		if (_currentStnNameIndex >= 0 && _nextStnNameIndex >= 0
				&& _currentStnNameIndex < _nextStnNameIndex) {
			return "up";
		} else if (_currentStnNameIndex >= 0 && _nextStnNameIndex >= 0
				&& _currentStnNameIndex >= _nextStnNameIndex) {
			return "down";
		} else if (_currentStnNameIndex >= 0 && _preStnNameIndex >= 0
				&& _currentStnNameIndex < _preStnNameIndex) {// up
			return "up";// down
		} else if (_currentStnNameIndex >= 0 && _preStnNameIndex >= 0
				&& _currentStnNameIndex >= _preStnNameIndex) {// down
			return "down";// down
		} else {
			return "down";// down
		}
		// if(_stnArray[0].stnName == currentStnName) {//站名为纵坐标数组
		// return "up";//up
		// } else if(_stnArray[_len-1].stnName == currentStnName) {
		// return "down";//down
		// } else if(_currentStnNameIndex>=0 && _preStnNameIndex>=0 &&
		// _currentStnNameIndex<_preStnNameIndex){//up
		// return "up";//down
		// } else if(_currentStnNameIndex>=0 && _preStnNameIndex>=0 &&
		// _currentStnNameIndex>=_preStnNameIndex){//down
		// return "down";//down
		// } else {
		// return "down";//down
		// }
		return "";
	}
	;

	/**
	 * 交路接续关系线 y坐标方向
	 * 
	 * @param jxgxObj {
	 *            fromStnName:"北京西", //前车接续站 fromTime:"2014-05-11
	 *            21:07",//前车接续站到达时间 fromStartStnName:"成都", //前车始发站
	 *            toStnName:"北京西", //后车接续站 toTime:"2014-05-12 08:35",
	 *            //后车接续站出发时间 toEndStnName:"成都" //后车终到站 }
	 * 
	 * 规则说明：if当接续站 = 交路经由站数组中第一个站时，y向上 else if 当接续站 = 交路经由站数组中最后一个站时，y向下 else if
	 * 前车始发站在经由站数组中index <= 接续站在经由站数组中index && 后车终到站在经由站数组中index <=
	 * 接续站在经由站数组中index，y向下 else if 前车始发站在经由站数组中index > 接续站在经由站数组中index &&
	 * 后车终到站在经由站数组中index > 接续站在经由站数组中index，y向上 else y向上
	 * 
	 * private
	 */
	function getDirectionYByJxgx(jxgxObj) {
		var _len = _stnArray.length;
		if (_len == 0)
			return ""; // 经由站为空

		var jxzIndex = getStnArcYIndex(jxgxObj.fromStnName); // 接续站在经由数组中的index
		var fromStartStnIndex = getStnArcYIndex(jxgxObj.fromStartStnName); // 前车始发站在经由站数组中index
		var toEndStnIndex = getStnArcYIndex(jxgxObj.toEndStnName); // 后车终到站在经由站数组中index

		if (_stnArray[0].stnName == jxgxObj.fromStnName) {
			return "up";// up
		} else if (_stnArray[_len - 1].stnName == jxgxObj.fromStnName) {
			return "down";// down
		} else if (fromStartStnIndex <= jxzIndex && toEndStnIndex <= jxzIndex) {
			return "down";// down
		} else if (fromStartStnIndex > jxzIndex && toEndStnIndex > jxzIndex) {
			return "up";
		} else if (fromStartStnIndex > jxzIndex && toEndStnIndex < jxzIndex) {
			return "up";
		} else if (fromStartStnIndex < jxzIndex && toEndStnIndex > jxzIndex) {
			return "down";
		}
		return "";
	}
	;

	/**
	 * 绘制交路接续关系 public
	 * 
	 * @param color
	 * @param 接续关系数组 [
	 *            {fromStnName:"北京西",fromTime:"2014-05-11
	 *            21:07",fromStartStnName:"成都",toStnName:"北京西",toTime:"2014-05-12
	 *            08:35",toEndStnName:"成都"},
	 *            {fromStnName:"成都",fromTime:"2014-05-13
	 *            12:30",fromStartStnName:"北京西",toStnName:"成都",toTime:"2014-05-13
	 *            19:18",toEndStnName:"北京西"},
	 *            {fromStnName:"北京西",fromTime:"2014-05-13
	 *            21:07",fromStartStnName:"成都",toStnName:"北京西",toTime:"2014-05-14
	 *            08:35",toEndStnName:"成都"} ]
	 * @param groupSerialNbrIndex
	 *            (可选参数)组循环序号，用于计算接续线高度，以达到接续线高低错误显示目的
	 * 
	 */
	this.drawJxgx = function(colorParam, jxgxArray, groupSerialNbrIndex) {
		var fromX = 0;
		var fromY = 0;
		var toX = 0;
		var toY = 0;
		var offsetY = 0; // Y偏移量 用于绘制交路接续方向 down:10px、up:-10px
		var groupSerialNbrY = 0;
		if (groupSerialNbrIndex && typeof groupSerialNbrIndex == "number") {
			groupSerialNbrY = groupSerialNbrIndex * 5;
		}

		for (var i = 0, _len = jxgxArray.length; i < _len; i++) {
			_context.save();
			_context.beginPath();

			var jxgxObj = jxgxArray[i];
			var directionY = getDirectionYByJxgx(jxgxObj);
			if ("up" == directionY) {
				offsetY = -10 - groupSerialNbrY;
			} else if ("down" == directionY) {
				offsetY = 10 + groupSerialNbrY;
			}

			// 坐标定位
			fromX = getX(jxgxObj.fromTime);
			fromY = getY(jxgxObj.fromStnName);
			toX = getX(jxgxObj.toTime);
			toY = getY(jxgxObj.toStnName);

			// 绘线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY, fromX,
					fromY + offsetY);// 接续左竖线
			myCanvasDrawLine(_context, 2, colorParam, fromX, fromY + offsetY,
					toX, fromY + offsetY);// 接续横线
			myCanvasDrawLine(_context, 2, colorParam, toX, fromY + offsetY,
					toX, toY);// 接续右竖线

			_context.strokeStyle = colorParam;
			_context.stroke();// 绘画
		}
	};

	/**
	 * 绘制每条交路起止标记 public
	 * 
	 * @param jlTrains
	 *            每条交路包含的所有车次信息 [//该交路下所有车次数组 { trainName:"K818",
	 *            startStn:"成都",//始发站名 为null时填入""（空串）
	 *            endStn:"北京西",//终到站名为null时填入""（空串） trainStns:[
	 *            {isCurrentBureau:1,stationType:'0',runDays:0,stnName:"成都",arrTime:"2014-05-11
	 *            19:18",dptTime:"2014-05-11 19:18",stayTime:0},
	 *            {isCurrentBureau:1,stationType:'TZ',runDays:0,stnName:"遂宁",arrTime:"2014-05-11
	 *            21:19",dptTime:"2014-05-11 21:33",stayTime:14},
	 *            {isCurrentBureau:1,stationType:'FJK',runDays:0,stnName:"南充",arrTime:"2014-05-11
	 *            22:15",dptTime:"2014-05-11 22:22",stayTime:7},
	 *            {stationType:'BT',runDays:0,stnName:"蓬安",arrTime:"2014-05-11
	 *            22:49",dptTime:"2014-05-11 22:54",stayTime:5},
	 *            {runDays:0,stnName:"营山",arrTime:"2014-05-11
	 *            23:06",dptTime:"2014-05-11 23:11",stayTime:5},
	 *            {runDays:1,stnName:"达州",arrTime:"2014-05-12
	 *            00:13",dptTime:"2014-05-12 00:23",stayTime:10},
	 *            {runDays:1,stnName:"安康",arrTime:"2014-05-12
	 *            03:12",dptTime:"2014-05-12 03:32",stayTime:20},
	 *            {runDays:1,stnName:"华山",arrTime:"2014-05-12
	 *            07:45",dptTime:"2014-05-12 07:51",stayTime:6},
	 *            {runDays:1,stnName:"北京西",arrTime:"2014-05-12
	 *            21:07",dptTime:"2014-05-12 21:07",stayTime:0} ] }, {
	 *            trainName:"K817", startStn:"北京西",//始发站名 为null时填入""（空串）
	 *            endStn:"成都",//终到站名为null时填入""（空串） trainStns:[
	 *            {isCurrentBureau:1,runDays:0,stnName:"北京西",arrTime:"2014-05-13
	 *            08:35",dptTime:"2014-05-13 08:35",stayTime:0},
	 *            {isCurrentBureau:1,runDays:0,stnName:"华山",arrTime:"2014-05-13
	 *            23:07",dptTime:"2014-05-13 23:13",stayTime:6},
	 *            {runDays:1,stnName:"安康",arrTime:"2014-05-14
	 *            03:59",dptTime:"2014-05-14 04:13",stayTime:14},
	 *            {runDays:1,stnName:"达州",arrTime:"2014-05-14
	 *            07:29",dptTime:"2014-05-14 07:35",stayTime:6},
	 *            {runDays:1,stnName:"土溪",arrTime:"2014-05-14
	 *            08:09",dptTime:"2014-05-14 08:13",stayTime:4},
	 *            {runDays:1,stnName:"营山",arrTime:"2014-05-14
	 *            08:45",dptTime:"2014-05-14 08:49",stayTime:4},
	 *            {runDays:1,stnName:"蓬安",arrTime:"2014-05-14
	 *            09:02",dptTime:"2014-05-14 09:05",stayTime:3},
	 *            {runDays:1,stnName:"南充",arrTime:"2014-05-14
	 *            09:32",dptTime:"2014-05-14 09:36",stayTime:4},
	 *            {runDays:1,stnName:"遂宁",arrTime:"2014-05-14
	 *            10:30",dptTime:"2014-05-14 10:33",stayTime:3},
	 *            {runDays:1,stnName:"成都",arrTime:"2014-05-14
	 *            12:30",dptTime:"2014-05-14 12:30",stayTime:0} ] } ]
	 * @param groupSerialNbrIndex
	 *            （可选参数）组循环序号，避免与其他线重合
	 */
	this.drawJlStartAndEnd = function(_color, jlTrains, groupSerialNbrIndex,
			diagram) {
		var _len = jlTrains.length; // 交路包含列车数
		if (jlTrains.length > 0) {
			_context.save();
			_context.beginPath();

			// 画多线的，根据是否简图，确定画开始和结束箭头的方式
			if (diagram == true) {
				if (jlTrains[0].trainStns.length > 0
						&& jlTrains[0].startStn == jlTrains[0].trainStns[0].stnName) {// 交路第一个列车经由第一站==始发站
					// 则绘制开始标记
					drawTrainStartArrows(_color, jlTrains[0].trainStns[0],
							groupSerialNbrIndex, jlTrains[0].endStn);
				}

				var _lenTrainStnLast = jlTrains[_len - 1].trainStns.length;// 该交路最后一个车次经由站长度
				if (_lenTrainStnLast > 0
						&& jlTrains[_len - 1].endStn == jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1].stnName) {// 交路最后一个车次经由最后一站==终到站
					// 则绘制终到标记
					drawTrainEndArrows(_color,
							jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1],
							groupSerialNbrIndex, jlTrains[_len - 1].startStn);
				}

			} else if (diagram == false) {
				if (jlTrains[0].trainStns.length > 0
						&& jlTrains[0].startStn == jlTrains[0].trainStns[0].stnName) {// 交路第一个列车经由第一站==始发站
					// 则绘制开始标记
					drawTrainStartArrows(_color, jlTrains[0].trainStns[0],
							groupSerialNbrIndex, jlTrains[0].endStn);
				}

				var _lenTrainStnLast = jlTrains[_len - 1].trainStns.length;// 该交路最后一个车次经由站长度
				if (_lenTrainStnLast > 0
						&& jlTrains[_len - 1].endStn == jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1].stnName) {// 交路最后一个车次经由最后一站==终到站
					// 则绘制终到标记
					drawTrainEndArrows(_color,
							jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1],
							groupSerialNbrIndex, jlTrains[_len - 1].startStn);
				}
				// if(jlTrains[0].trainStns.length>0 && jlTrains[0].startStn ==
				// jlTrains[0].trainStns[0].stnName) {//交路第一个列车经由第一站==始发站
				// 则绘制开始标记
				// drawTrainStartArrows(_color, jlTrains[0].trainStns[0],
				// groupSerialNbrIndex, jlTrains[0].trainStns[1]);
				// }
				//				
				// var _lenTrainStnLast =
				// jlTrains[_len-1].trainStns.length;//该交路最后一个车次经由站长度
				// if(_lenTrainStnLast>0 && jlTrains[_len-1].endStn ==
				// jlTrains[_len-1].trainStns[_lenTrainStnLast-1].stnName)
				// {//交路最后一个车次经由最后一站==终到站 则绘制终到标记
				// drawTrainEndArrows(_color,
				// jlTrains[_len-1].trainStns[_lenTrainStnLast-1],
				// groupSerialNbrIndex,
				// jlTrains[_len-1].trainStns[_lenTrainStnLast-2]);
				// }

			} else {
				if (jlTrains[0].trainStns.length > 0
						&& jlTrains[0].startStn == jlTrains[0].trainStns[0].stnName) {// 交路第一个列车经由第一站==始发站
					// 则绘制开始标记
					drawTrainStartArrows(_color, jlTrains[0].trainStns[0],
							groupSerialNbrIndex, jlTrains[0].trainStns[1]);
				}

				var _lenTrainStnLast = jlTrains[_len - 1].trainStns.length;// 该交路最后一个车次经由站长度
				if (_lenTrainStnLast > 0
						&& jlTrains[_len - 1].endStn == jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1].stnName) {// 交路最后一个车次经由最后一站==终到站
					// 则绘制终到标记
					drawTrainEndArrows(_color,
							jlTrains[_len - 1].trainStns[_lenTrainStnLast - 1],
							groupSerialNbrIndex,
							jlTrains[_len - 1].trainStns[_lenTrainStnLast - 2]);
				}

			}

			_context.strokeStyle = _color;
			_context.stroke();// 绘画
		}

	};

	/**
	 * 绘制时间刻度线 _xDateArray2天以上， 根据x轴放大倍数（_xScaleCount）绘制线条多少(包含0点、6点、12点、18点、0点)
	 * public 计划用于图形滚动条 暂时未被调用
	 * 
	 * 为避免循环中做逻辑if判断（会降低性能）。所以未与drawGridMoreY合并 private
	 * 
	 * @param color
	 *            //颜色
	 */
	this.drawTimeLine = function(color) {
		// 画竖线
		var _halfHourWidth = 30 * _stepX / _xScale; // 半小时
		var _oneHourWidth = 60 * _stepX / _xScale;
		var _oclock6Width = 6 * _oneHourWidth;
		var _oclock12Width = 12 * _oneHourWidth;
		var _oclock18Width = 18 * _oneHourWidth;
		var _oneDayWidth = 24 * _oneHourWidth;

		for (var i = 0, _len = _xDateArray.length; i < _len; i++) {
			// 网格顶端显示 2014-05-12 一天范围内的 日期文本显示
			myCanvasFillText(_context, {
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _startY - 35
			});
			// 网格底端显示 2014-05-12
			myCanvasFillText(_context, {
				textAlign : "center",
				text : _xDateArray[i].runDate,
				fromX : (_startX + i * _oneDayWidth + _oclock12Width),
				fromY : _endY + 35
			});

			//
			if (_xScaleCount <= 2) {// 0点、6点、12点、18点
				drawY(color, 2, "0", _startX + i * _oneDayWidth, false);// 0点
				drawY(color, 0.5, "6", _startX + _oclock6Width + i
						* _oneDayWidth, true);// 6点
				drawY(color, 1, "12", _startX + _oclock12Width + i
						* _oneDayWidth, false);// 12点
				drawY(color, 0.5, "18", _startX + _oclock18Width + i
						* _oneDayWidth, true);// 18点
			} else if (_xScaleCount > 2 && _xScaleCount < 16) { // 每小时一条线
				for (var j = 0; j < 24; j++) {
					var _isDashLine = false; // 是否为虚线
					var _lineWidth = false; // 线宽
					if (j % 12 == 0) {
						_isDashLine = false;
					} else {
						_isDashLine = true;
					}
					if (j == 0) {
						_lineWidth = 2;
					} else if (j % 12 == 0) {
						_lineWidth = 1;
					} else {
						_lineWidth = 0.5;
					}

					drawY(color, _lineWidth, j, _startX + i * _oneDayWidth + j
							* _oneHourWidth, _isDashLine);// 0点
				}
			} else if (_xScaleCount >= 16) { // 每半小时一条线
				for (var j = 0; j < 48; j++) {
					var _isDashLine = true; // 是否为虚线
					var _lineWidth = 0.5; // 线宽
					var _text = j / 2;
					if (j == 0) {
						_text = "0";
						_isDashLine = false;
						_lineWidth = 2;
					} else if (j % 24 == 0) {
						_isDashLine = false;
						_lineWidth = 1;
					} else if (j % 2 == 1) {// 半小时
						_lineWidth = 0.2;
						_text = Math.floor(j / 2) + ":30";
					}

					drawY(color, _lineWidth, _text, _startX + i * _oneDayWidth
							+ j * _halfHourWidth, _isDashLine);// 0点
				}
			}

			// x结束位置 再画一条竖线
			if (i == _len - 1) {
				drawY(color, 2, "0", _startX + (i + 1) * _oneDayWidth, false);// 24点
			}
		}
	};

};
