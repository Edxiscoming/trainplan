package org.railway.com.trainplan.repository.mybatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;



/**
 * 基础框架的数据访问层抽象实现类myBatis orm框架)，所有模块的数据访问层实现类均继承该类。<br>
 *
 * JDK版本：JDK1.6
 * @author GGR
 * @version 1.0 
 */
@Repository
public class BaseDao {
	private static final Logger log = Logger.getLogger(BaseDao.class);
	
	@Autowired
	public SqlSessionTemplate sqlSession;
	
	
	/**
	 * 获取数据库连接对象
	 * @return
	 */
	private Connection getConnection(){
		Connection connection = SqlSessionUtils.getSqlSession(
				sqlSession.getSqlSessionFactory(), sqlSession.getExecutorType(),
				sqlSession.getPersistenceExceptionTranslator()).getConnection();
		return connection;
	}
	
	/**
	 * 
	 * @Description: 根据对数表以及车次信息获取基本图中的车次信息
	 * @param @param ocross
	 * @param @param trainNbr   
	 * @return void  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	@SuppressWarnings("unchecked")
	public List<BaseTrainInfo> getBaseTrain(String schemeId,String trainNbr){
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("trainNbr", trainNbr);
		params.put("chartId", schemeId);
		return this.selectListBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM, params);
	}
	
	/**
	 * 
	 * @Description: 根据baseTrain的主键ID获取单条的baseTrain信息
	 * @param @param train
	 * @param @return   
	 * @return BaseTrainInfo  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	public BaseTrainInfo getBaseTrain(String baseTrainId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("baseTrainId", baseTrainId);
		return (BaseTrainInfo) this.selectOneBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_ONE_FOR_PARAM, param);
	}
	
	
    //=================================以下代码为myBatis实现的常用方法(后缀加BySql)===========================//


    /**
     * 往库表中插入记录
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param value       要操作的对象
     * @return 插入成功的记录数
     */
    public int insertBySql(String statementId, Object value) {
    	
    	
        return sqlSession.insert(statementId, value);
    }

    /**
     * 删除库表中的记录（可批量删除），返回删除成功的记录数。
     *
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param value       删除条件值
     * @return 删除成功的记录数
     */
    public int deleteBySql(String statementId, Object value) {
        return sqlSession.delete(statementId, value);
    }

    /**
     * 更新库表中的记录（可批量更新），返回更新成功的记录数。
     *
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param value       更新条件值值
     * @return 更新成功的记录数
     */
    public int updateBySql(String statementId, Object value) {
        return sqlSession.update(statementId, value);
    }

    /**
     * 查询符合条件的记录，生成List返回。
     *
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param value       查询条件值
     * @return list 找到的记录
     */
    public List selectListBySql(String statementId, Object value) {
        List list = sqlSession.selectList(statementId, value);
        //log.logDebug("Execute queryForListBySql time:" + (l2 - l1) + "ms");
        //数据库含有空格的字段值(char类型)，装载到myBatis生成的持久对象属性中,是否自动去掉属性值左右两边的空格
        //如果需要去掉空格，则使用如下方式
        //list = Config.getPropertyBool("po.propertyvalue.trimable", true) ? BeanUtil.trim(list) : list
        return list;
    }


    
    /**
     * 查询单个符合条件的记录，生成Object返回</br>
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param parameter   查询条件值
     * @return list 找到的记录
     */
    public Object selectOneBySql(String statementId, Object parameter) {
        Object bean = sqlSession.selectOne(statementId, parameter);
        //log.logDebug("Execute queryForObjectBySql time:" + (l2 - l1) + "ms");
        //数据库含有空格的字段值(char类型)，装载到myBatis生成的持久对象属性中,是否自动去掉属性值左右两边的空格
        //如果需要去掉空格，则使用如下方式
        //bean = Config.getPropertyBool("po.propertyvalue.trimable", true) ? BeanUtil.trim(bean) : bean
        return bean;
    }
    
    /**
     * 查询单个符合条件的记录，生成Object返回</br>
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该mapper文件某片段的id
     * @param parameter   查询条件值
     * @return list 找到的记录
     */
    public Object selectOneBySql2(String statementId, Object parameter) {
    	Object bean = sqlSession.selectOne(statementId, parameter);
    	//log.logDebug("Execute queryForObjectBySql time:" + (l2 - l1) + "ms");
    	//数据库含有空格的字段值(char类型)，装载到myBatis生成的持久对象属性中,是否自动去掉属性值左右两边的空格
    	//如果需要去掉空格，则使用如下方式
    	//bean = Config.getPropertyBool("po.propertyvalue.trimable", true) ? BeanUtil.trim(bean) : bean
    	return bean;
    }
    
    
    /**
     * myBatis分页查询符合条件的记录
     * <p>
     * 注意：调用该方法，必须在myBatis的mapper文件中存在statementId_COUNT片段</br>
     * <b>特别说明：由于该命令采用的分页技术不是数据库本身的分页技术，而是采用ResultSet的absolute定位技术，<br>
     * 需要把查询结果全部装入ResultSet再定位。如果查询结果较大（1万条记录以上)，效率会很低。<br>
     * 建议使用Hibernate的query方法或在mapper的XML中使用数据库内部分页技术<br>
     * （即把pageId,pageSize作为参数传入SQL语句的类似limit n,m中）来查询。
     * </b>
     * </p>
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该sqlMap文件某片段的id
     * @param parameter   查询条件对象
     * @param offset	      返回查询结果的起始行，从0开始
     * @param pageSize	 	  返回查询结果的最大行数
     * @throws com.fbd.crm.exception.BaseUncheckedException
     * @return com.fbd.crm.common.QueryResult
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public QueryResult selectListForPagingBySql(String statementId, Object parameter) throws Exception{
        Assert.hasText(statementId, "传入的SQL配置ID不能为空.");
        
        
    	//计算总页数 
    	Integer count = null;
        try {
        	count = (Integer)sqlSession.selectOne(statementId + "_COUNT", parameter);
        } catch (Exception e) {
        	String msg = "配置文件中不存在COUNT语句或发生其它例外,无法执行总数统计. SqlMap id:" + statementId;
        	log.error(msg, e);
        	throw new Exception(e);
        }
        
        if ((count == null) || (count.intValue() <= 0)) {
        	log.info("执行COUNT后,返回的结果数为0,表示该SQL执行无结果数据返回.因此提前终止其数据查询并立即返回空集.");
        	return new QueryResult(new ArrayList(), 0);
        }
        
        
        
    	
        List resultList = sqlSession.selectList(statementId, parameter);
        QueryResult result = new QueryResult(resultList, count);
        return result;
    }
    
    
    
    
    /**
     * myBatis带汇总查询
     * </b>
     * </p>
     * @param statementId 调用myBatis的mapper文件的声明段名，规则名：mapper的namespace+"." + 该sqlMap文件某片段的id
     * @param parameter   查询条件对象
     * @throws com.fbd.crm.exception.BaseUncheckedException
     * @return com.fbd.crm.common.QueryResult
     */
    public QueryResult selectListWithTotal(String statementId, Object parameter) throws Exception{
        Assert.hasText(statementId, "传入的SQL配置ID不能为空.");
        
        
    	//计算总页数 
    	Integer count = null;
        try {
        	count = (Integer)sqlSession.selectOne(statementId + "_COUNT", parameter);
        } catch (Exception e) {
        	String msg = "配置文件中不存在COUNT语句或发生其它例外,无法执行总数统计. SqlMap id:" + statementId;
        	log.error(msg, e);
        	throw new Exception(e);
        }
        
        if ((count == null) || (count.intValue() <= 0)) {
        	log.info("执行COUNT后,返回的结果数为0,表示该SQL执行无结果数据返回.因此提前终止其数据查询并立即返回空集.");
        	return new QueryResult(new ArrayList(),new ArrayList(), 0);
        }
        
        
        
    	
        List resultList = sqlSession.selectList(statementId, parameter);
        
        List totalList = sqlSession.selectList(statementId + "_TOTAL", parameter);
        
        
        QueryResult result = new QueryResult(resultList, totalList, count);
        return result;
    }
    
    
   /* public int executeUpdate(String sql) throws SQLException{
    	Connection con = null;
    	Statement stmt = null;
    	PreparedStatement pstmt= null;
    	try {
    		con = this.getConnection();//sqlSession.getConnection();
    		con.setAutoCommit(false);
    		pstmt = con.prepareStatement(sql);
    		pstmt.
    		
    	
    		con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("sql执行失败");
		}finally {
			if (stmt != null) {
				stmt.close();
			}
			// 此处不关闭，连接关闭由org.springframework.jdbc.datasource.DataSourceTransactionManager自动完成
			if (con != null) {
				con.close();
			}
		}
    }*/
    
    /**
     * 执行sql
     * @category 该事务不在spring事务管理机制内
     * @param sql
     * @author 邓柳江
     * @return List
     * @throws SQLException
     */
    public List executeQueryBySql(String sql) throws SQLException{
    	Connection con = null;
    	Statement stmt = null;
    	
    	List list = new ArrayList();
    	try {
    		con = this.getConnection();//sqlSession.getConnection();
    		con.setAutoCommit(false);
    		stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    		ResultSet rSet = stmt.executeQuery(sql);
    		
    	
    		ResultSetMetaData md = rSet.getMetaData();
    		int num = md.getColumnCount();
    		while (rSet.next()) {
    			Map mapOfColValues = new HashMap(num);
	    		for (int i = 1; i <= num; i++) {
		    		mapOfColValues.put(md.getColumnName(i), rSet.getObject(i));
	    		}
	    		list.add(mapOfColValues);
    		}
    		
    		con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("sql执行失败");
		}finally {
			if (stmt != null) {
				stmt.close();
			}
			// 此处不关闭，连接关闭由org.springframework.jdbc.datasource.DataSourceTransactionManager自动完成
			if (con != null) {
				con.close();
			}
		}
    	return list;
	}
}
