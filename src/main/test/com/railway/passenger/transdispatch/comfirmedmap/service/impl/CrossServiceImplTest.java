package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.railway.com.trainplan.service.CrossDictService;
import org.railway.com.trainplan.service.OriginalCrossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.operationplan.repository.PlanTrainNewMapper;
import com.railway.passenger.transdispatch.operationplan.service.impl.OperationlanServiceImpl;
import com.railway.passenger.transdispatch.util.TimeUtils;


@ContextConfiguration(locations={"classpath*:/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//如果是true不会改变数据库数据，如果是false会改变数据
@TransactionConfiguration(transactionManager="txManager",defaultRollback=false)
public class CrossServiceImplTest {
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
	OperationlanServiceImpl operationlanServiceImpl;
	
	@Autowired
	PlanTrainNewMapper planTrainNewMapper;
	
	@Autowired
	TCmVersionServiceImpl tCmVersionServiceImpl;
	
	@Resource
	private CrossDictService t;
	@Resource
	private OriginalCrossService os;
	
	/**
	 * 
	 * @Description: 从原始对数表录入->逻辑交路所有数据生成->车底交路所有数据生成全流程单元测试
	 * @param 
	 * @return void  
	 * @throws
	 * @author qs
	 * @date 2015年10月9日
	 */
	@Test
	public void testTuding(){
		/*原始对数表数据生成*/
		TCmOriginalCross ocross = new TCmOriginalCross();
		
		ocross.setCmOriginalCrossId(uuid());
		ocross.setCrossName("05133-D638/5-D636/7-D638/5-D636/7-D5134");
		ocross.setCrossSection("成都东～上海虹桥");
		ocross.setGroupTotalNbr((short)2);
		ocross.setAlternateDate("20151007-20151007-20151008-20151009-20151010-20151010");
		ocross.setTokenVehBureau("W");
		ocross.setAlternateTrainNbr("");
		ocross.setCommonlineRule("");
		ocross.setAppointWeek(null);
		ocross.setAppointDay("");
		ocross.setAppointPeriod("1100-1100-1100-1100-1100-1100");
		ocross.setHighlineFlag("1");
		ocross.setRelevantBureau("WNH");
		
		String tcmVersionId = "cd0982b0-20bc-4b96-868e-af4a94ceeb5e";
		System.out.println(cmOriginalCrossServiceImpl.insertTCmOriginalCross(ocross, tcmVersionId));
		
		/*逻辑交路数据生成*/
		TCmCross cross = comfirmedmapServiceImpl.generateTcmCrossInfo(ocross);
		/*车底交路数据生成*/
		comfirmedmapServiceImpl.generateTcmPhyCrossInfo(cross);
		
		ocross.setCheckFlag((short)1);
		ocross.setCheckTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
		ocross.setCheckPeople("test");
		ocross.setCheckPeopleOrg("");
		ocross.setCreatCrossTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
		ocross.setCreateCrossFlag(1);
		cmOriginalCrossServiceImpl.update(ocross);
	}
	
	private String uuid(){
		return UUID.randomUUID().toString();
	}
}
