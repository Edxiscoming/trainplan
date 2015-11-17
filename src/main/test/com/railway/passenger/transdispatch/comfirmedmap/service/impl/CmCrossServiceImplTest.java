package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.service.CrossDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railway.basicmap.original.service.IMTrainLineTempService;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmOriginalCrossService;


@ContextConfiguration(locations={"classpath*:/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//如果是true不会改变数据库数据，如果是false会改变数据
@TransactionConfiguration(transactionManager="txManager",defaultRollback=false)
public class CmCrossServiceImplTest {

//	@Test
//	public void test() {
////		fail("Not yet implemented");
//		System.out.println("ok");
//	}
	
	@Resource
	private CmCrossServiceImpl c;
	
	@Autowired
	ComfirmedmapServiceImpl comfirmedmapServiceImpl;
	
	@Autowired
	CmCrossServiceImpl cmCrossServiceImpl;
	
	@Autowired
	CmPhyCrossServiceImpl cmPhyCrossServiceImpl;
	
	@Autowired
	CmOriginalCrossServiceImpl cmOriginalCrossServiceImpl;
	@Autowired
	CmPartOriginalCrossServiceImpl partService;
	@Autowired
	private IMTrainLineTempService trainLineService;
	
	@Resource
	private CrossDictService t;
	@Resource
	private ICmOriginalCrossService os;
	
	
	public void testGenerateTcmPhyCrossInfo(){
		TCmCross cross = cmCrossServiceImpl.getTCmCross("bd8959de6db8495bad6bbfd6b77c3386");
		comfirmedmapServiceImpl.generateTcmPhyCrossInfo(cross);
	}
	@Test
	public void testPart(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crossIds", "8e6e506c-f715-4cd4-beee-a5f9fed83e73,0c5089ff-297e-494d-a7ed-0c184317ed4a");
	}
	public void testGetBaseTrain(){
		BaseTrainInfo param = new BaseTrainInfo();
		param.setBaseTrainId("1abc2fec-3d21-48ae-82ce-fb7ed3c3dc6a");
//		BaseTrainInfo train = comfirmedmapServiceImpl.getBaseTrain(param);
//		System.out.println(train.getStartBureau()); 
	}
	
	public void testInsertTCmOriginalCross(){
		TCmOriginalCross ocross = new TCmOriginalCross();
//		ocross.setCmOriginalCrossId(uuid());
//		ocross.setCrossName("D927-D928");
//		ocross.setCrossSection("北京南～上海虹桥");
//		ocross.setGroupTotalNbr((short)2);
//		ocross.setAlternateDate("20150417");
//		ocross.setTokenVehBureau("K");
//		ocross.setAlternateTrainNbr("");
//		ocross.setCommonlineRule("1");
//		ocross.setAppointWeek(null);
//		ocross.setAppointDay("");
//		ocross.setAppointPeriod("");
		
		ocross.setCmOriginalCrossId(uuid());
		ocross.setCrossName("K208/5-K206/7");
		ocross.setCrossSection("北京南～上海虹桥");
		ocross.setGroupTotalNbr((short)4);
		ocross.setAlternateDate("20150708-20150709");
		ocross.setTokenVehBureau("K");
		ocross.setAlternateTrainNbr("");
		ocross.setCommonlineRule("1");
		ocross.setAppointWeek(null);
		ocross.setAppointDay("");
		ocross.setAppointPeriod("");
		
		System.out.println(cmOriginalCrossServiceImpl.insertTCmOriginalCross(ocross, "a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3"));
	}
	
	public void testGenerateTcmCrossInfo(){
		TCmOriginalCross ocross = cmOriginalCrossServiceImpl.getTCmOriginalCross("a7afde3f396f4c0caac79485b27ceaae");
		comfirmedmapServiceImpl.generateTcmCrossInfo(ocross);
	}
	
	public void testGetTCmVersion(){
		TCmVersion version = cmOriginalCrossServiceImpl.getTCmVersion("23968584285244329d124c51c6df08cf");
		System.out.println(version.getCmVersionId());
	}
	
	public void test1() throws Exception{
		TCmOriginalCross ocross = cmOriginalCrossServiceImpl.getTCmOriginalCross("c0c0426b73204838ba1dd7b57cca2940");
		System.out.println(ocross.getCrossName());
	}
	
	private String uuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
//	@Test
	public void getTCmTrain(){
		TCmCross cross= new TCmCross();
		cross.setCmCrossId("bd8959de6db8495bad6bbfd6b77c3386");
	 List<TCmTrain>	list = cmCrossServiceImpl.getTCmTrain(cross);
	 System.out.println(list);
	}
//	@Test
	public void getTCmTrainAlternate(){
		try{
		TCmCross cross= new TCmCross();
		cross.setCmCrossId("bd8959de6db8495bad6bbfd6b77c3386");
		TCmTrainAlternate t=cmCrossServiceImpl.getTCmTrainAlternate("9e0e7d7588fb4aa9a9e51e718500be3e");
		System.out.println(t);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void getTCmTrainRule(){
		try{
			TCmCross cross= new TCmCross();
			cross.setCmCrossId("bd8959de6db8495bad6bbfd6b77c3386");
			TCmTrainRule t=cmCrossServiceImpl.getTCmTrainRule("9e0e7d7588fb4aa9a9e51e718500be3e");
			System.out.println(t);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
}
