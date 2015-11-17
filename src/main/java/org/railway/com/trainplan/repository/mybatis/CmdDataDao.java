package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.CmdInfoModel;


/**查询临客命令
 * @author suntao
 *
 */
 
public interface CmdDataDao {
	public List<CmdInfoModel> findLKMlByStartAndDate(Map map);
}
