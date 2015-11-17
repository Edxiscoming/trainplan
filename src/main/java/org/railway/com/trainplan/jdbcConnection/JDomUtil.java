package org.railway.com.trainplan.jdbcConnection;

import itcmor.util.FileFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.railway.com.trainplan.common.utils.StringAndTimeUtil;

import config.ConfigClassLoader;

/**
 * XML文件解析工具类
 * 
 * @author ITC
 * 
 */
public class JDomUtil {

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, ConfigDataBase> parserXml() {

		HashMap<String, ConfigDataBase> hashMap = new HashMap<String, ConfigDataBase>();
		SAXBuilder builder = new SAXBuilder(false);
		URL fileUrl = FileFactory.getFileURLFromClassPath(
				ConfigClassLoader.class, "configDataBase.xml");

		try {

			Document document = builder.build(fileUrl);
			Element ddtSet = document.getRootElement();
			List<Element> ddtList = ddtSet.getChildren("DataBase");
			for (int i = 0; i < ddtList.size(); i++) {
				Element dataBase = (Element) ddtList.get(i);
				Element bureau = dataBase.getChild("bureau");
				Element url = dataBase.getChild("url");
				Element driver = dataBase.getChild("driver");
				Element user = dataBase.getChild("user");
				Element password = dataBase.getChild("password");
				Element cmdpostname = dataBase.getChild("plancmdpostname");

				ConfigDataBase dbObject = null;
				String bureauValue = bureau.getValue().trim();
				if (url != null && !url.getValue().trim().equals("")) {
					dbObject = new ConfigDataBase();
					dbObject.setBureau(bureau.getValue().trim());
					dbObject.setUrl(url.getValue().trim());
					dbObject.setDriver(driver.getValue().trim());
					dbObject.setUser(user.getValue().trim());
					dbObject.setPassword(password.getValue().trim());
					dbObject.setPlanCmdPostName(cmdpostname == null ? ""
							: cmdpostname.getValue().trim());
				}

				if (hashMap.containsKey(bureauValue)) {
					System.out.println("有重复同路局数据库配置");
				} else {
					hashMap.put(bureauValue, dbObject);
				}
			}
		} catch (JDOMException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	
		return hashMap;
	}

	/**
	 * 写入文件
	 * 
	 * @param contentStr
	 *            字符串消息内容
	 * @param path
	 *            路径
	 * @param fileName
	 *            文件名
	 * @throws IOException
	 */
	public static void writeFile(String contentStr, String path, String fileName) {
		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		FileOutputStream fos = null;
		BufferedWriter writer = null;
		File ff = null;
		try {
			ff = new File(path + fileName);
			//日志文件大小设定为3M,超出则删除
			if(ff.exists() && ff.isFile() && ff.length() > (1024*1024*3))
				ff.delete();
			fos = new FileOutputStream(ff, true);
			writer = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
			writer.write(StringAndTimeUtil.yearMonthDayHourMinuteSdf.format(Calendar.getInstance().getTime()) + " " + contentStr + "\r\n");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				writer.write(e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				writer.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String args[]) {

	}
}
