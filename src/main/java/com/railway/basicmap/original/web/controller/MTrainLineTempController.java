package com.railway.basicmap.original.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.railway.basicmap.original.entity.MTrainlineTemp;
import com.railway.basicmap.original.service.impl.MTrainLineTempServiceImpl;
import com.railway.common.entity.Result;

@Controller
@RequestMapping(value = "/trainLine")
public class MTrainLineTempController {

	@Autowired
	private MTrainLineTempServiceImpl mTrainlineTempImpl;
	
	/**
	 * 基本图查询
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllTrainLine", method = RequestMethod.POST)
	public Result<MTrainlineTemp> getAllTrainLine(@RequestBody Map<String, Object> map){
		Result<MTrainlineTemp> result = mTrainlineTempImpl.GetTrainLineInfo(map);
		return result;			
	}
	
}
