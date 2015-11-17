package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.MsgReceive;
import org.railway.com.trainplan.entity.MsgSend;
import org.railway.com.trainplan.entity.MsgType;

/**
 * 消息dao.
 * 
 * @author zhangPengDong
 *
 *         2015年6月5日 下午4:04:22
 */
 
public interface MessageDao {

	/**
	 * @return
	 */
	List<MsgType> getMsgType();

	/**
	 * @return
	 */
	List<MsgReceive> getReceiveMsg(Map<String, Object> map);

	/**
	 * @param map
	 * @return
	 */
	public Integer getReceiveMsgCount(Map<String, Object> map);

	/**
	 * @return
	 */
	List<MsgSend> getSendMsg(Map<String, Object> map);

	/**
	 * 发送表，新增数据.
	 * 
	 * @param map
	 * @return
	 */
	Integer insertMsgSent(Map<String, Object> map);

	/**
	 * 接收表.
	 * 
	 * @param map
	 * @return
	 */
	Integer insertMsgReceive(List<MsgReceive> list);

	/**
	 * 修改接收表.
	 * 
	 * @param map
	 * @return
	 */
	Integer updateReceiveByMap(Map<String, Object> map);

	/**
	 * 修改发送表.
	 * 
	 * @param map
	 * @return
	 */
	Integer updateSendByMap(Map<String, Object> map);
	
}
