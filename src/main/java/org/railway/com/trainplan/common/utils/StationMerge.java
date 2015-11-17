package org.railway.com.trainplan.common.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StationMerge {
private final static String formartStr = "yyyy-MM-dd HH:mm:ss";
	
	public static void main(String[] args) throws ParseException, IOException{ 
	    BufferedReader br = new BufferedReader(new FileReader("E:\\githubbase\\trainplan\\trainplan\\src\\main\\java\\org\\railway\\com\\trainplan\\common\\utils\\test.txt"));  
	    String data = br.readLine();//一次读入一行，直 到读入null为文件结束  
	    LinkedList<LinkedList<Station>> list = new LinkedList<LinkedList<Station>>();
	    LinkedList<Station> temp = null; 
	    while(data != null){   
	    	   System.out.println(data);
	    	  if(data.startsWith("**********")){
	    		  if(temp != null){ 
	    			  list.add(temp); 
	    		  }
	    		  temp = new LinkedList<Station>();
	    	  }else{
	    		  String[] strs = data.split("#");
	    		  temp.add(new Station(strs[0], strs[1]));
	    	  }
	          data = br.readLine(); //接着读下一行  
	    }
	    list.add(temp); 
	    LinkedList<Station> result = maergeList(list);  
	    System.out.println("-----------------------------------reslt---------------------------------");
	    for(Station s : result){
	    	System.out.println(s.toString());
	    }
	    br.close();
//		List<String> list = new ArrayList<String>();
//		list.add(0, "a");
//		list.add(1, "b");
//		list.add(1, "c");
//		System.out.println(list);
	}
	
	public static LinkedList<Station> maergeList(List<LinkedList<Station>> list) throws ParseException{
		LinkedList<Station> temp = list.get(0); 
		for(LinkedList<Station> l : list){
			if(temp != l){
				if(isNeedReverse(temp, l)){ 
					Collections.reverse(l);  
				}
				System.out.println("-------------------------------------当前排序temp：-------------------------------------");
				for(Station s : temp){
					System.out.println(s.toString());
				}
				System.out.println("-------------------------------------合并数组：-------------------------------------");
				for(Station s : l){
					System.out.println(s.toString());
				} 
				System.out.println("------------------------------------合并完成-----------------------------------");
				temp = merge(temp, l);  
				for(Station s : temp){
					System.out.println(s.toString());
				}
				System.out.println("--------------------------------------1-------------------------------------");
			}
		} 
		return temp;
	} 
	private static LinkedList<Station> merge(LinkedList<Station> list1, LinkedList<Station> list2) throws ParseException{
		Station tempLasthitStation = null;
		int lastTempindex = 0;
		Station list1LasthitStation = null;
		Station list2LasthitStation = null;
		LinkedList<Station> result = new LinkedList<Station>();
		LinkedList<Station> temp = new LinkedList<Station>(); 
		for(int i = 0; i < list1.size(); i++){ 
				boolean findFlag = false; 
				for(int j = 0; j < list2.size(); j++){ 
					if(list1.get(i).getStnName().equals(list2.get(j).getStnName())){  
						//System.out.println("***************************************************" + list1.get(i).getStnName());
						temp = new LinkedList<Station>(list2.subList(0, j)); 
						if(!temp.isEmpty()){
							//System.out.println("------------********---!temp.isEmpty()--------------");
							if(result.isEmpty()){
								result.addAll(temp);
							}else{  
								for(Station station : temp){ 
//									System.out.println("---------temp----------------" +  list2LasthitStation.toString());
//									System.out.println("---------temp----------------" +  station.toString());
									 long currentMjg = getMjg(station, list2LasthitStation);
									 boolean hitFlag = false;
									 for(int s = lastTempindex; s <  result.size(); s++){
										 if(currentMjg < getMjg(result.get(s), tempLasthitStation)){  
											 station.setDptTime(getDptTimeForResult(currentMjg, tempLasthitStation)); 
											 result.add(s, station);
											 hitFlag = true;
											 break;
										 } 
									 } 
									 if(!hitFlag){//最后一个都未被命中就直接加入到result中 
//										 System.out.println("---------nohit----------------" +  station.toString());
										 station.setDptTime(getDptTimeForResult(currentMjg, tempLasthitStation));  
										 result.add(station);
									 }
								} 
							}
						}
						//判断最后命中站插入哪一个
						Station a = new Station();
						if(getMjg(list2.get(j), list2LasthitStation) > getMjg(list1.get(i), list1LasthitStation)){
							 
							a.setDptTime(getDptTimeForResult(getMjg(list2.get(j), list2LasthitStation), tempLasthitStation));
							a.setStationType(list2.get(j).getStationType());
							a.setMinites(list2.get(j).getMinites());
							a.setStnName(list2.get(j).getStnName()); 
						}else{ 
							if(tempLasthitStation == null){
								a.setDptTime(list1.get(i).getDptTime());
							}else{
								a.setDptTime(getDptTimeForResult(getMjg(list1.get(i), list1LasthitStation), tempLasthitStation));
							}
							a.setStationType(list1.get(i).getStationType());
							a.setMinites(list1.get(i).getMinites());
							a.setStnName(list1.get(i).getStnName()); 
						}
						tempLasthitStation = a;
						result.add(a); 
						lastTempindex = result.size() - 1;
						list2LasthitStation = list2.get(j);
						list1LasthitStation = list1.get(i); 
						list2 = new LinkedList<Station>(list2.subList(j + 1, list2.size()));  
						findFlag = true;
						break;
					} 
				}
				if(!findFlag){//如果在第2个列表中未找到 
					 long currentMjg = getMjg(list1.get(i), list1LasthitStation);
					 list1.get(i).setDptTime(getDptTimeForResult(currentMjg, tempLasthitStation));
					result.add(list1.get(i));
				} 
//				System.out.println("合并完：" + list1.get(i).toString());
//				for(Station s : result){
//					System.out.println(s.toString());
//				}
//				System.out.println("合并完：" + list1.get(i).toString());
//				System.out.println("----------------------------end-1--------------------------------------" + i);
				
			}
//		System.out.println("----------------------------list2 剩下-------------------------------------");
		for(Station station : list2){  
			 long currentMjg = getMjg(station, list2LasthitStation);
			 boolean hitFlag = false;
			 for(int s = lastTempindex; s <  result.size(); s++){
				 if(currentMjg < getMjg(result.get(s), tempLasthitStation)){  
					 station.setDptTime(getDptTimeForResult(currentMjg, tempLasthitStation)); 
					 result.add(s, station);
					 hitFlag = true;
					 break;
				 } 
			 } 
			 if(!hitFlag){//最后一个都未被命中就直接加入到result中 
				 station.setDptTime(getDptTimeForResult(currentMjg, tempLasthitStation));  
				 result.add(station);
			 }
		} 
//		System.out.println("----------------------------end---------------------------------------");
		return result;
	}
	
	/**
	 * 判断列车是否是同一个方向
	 * @return
	 */
	public static boolean isNeedReverse(List<Station> stationList1,List<Station> stationList2){
		int firstHit = -1;
		int lastHit = -1;  
		for(int i = 0;i < stationList1.size();i++){ 
			for(int j = 0;j<stationList2.size();j++){ 
				if(stationList1.get(i).getStnName().equals(stationList2.get(j).getStnName())){
					if(firstHit == -1){
						firstHit = j;
					}else if(lastHit == -1){
						lastHit = j;
						break;
					} 
				}
			}
		}
		 
		return firstHit > lastHit;
	}
	
	private static String getDptTimeForResult(long currentMjg, Station tempLasthitStation) throws ParseException{
		return DateUtil.format(new Date(DateUtil.parseDate(tempLasthitStation.getDptTime(), formartStr).getTime() + currentMjg), formartStr);
	}
	
	private static long getMjg(Station station1, Station station2) throws ParseException{
		if(station1 == null || station2 == null){
			return 0;
		}
		return Math.abs(DateUtil.parseDate(station1.getDptTime(), formartStr).getTime() - DateUtil.parseDate(station2.getDptTime(), formartStr).getTime());
	}
}
