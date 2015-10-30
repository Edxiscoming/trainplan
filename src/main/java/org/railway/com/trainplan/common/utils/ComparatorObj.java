package org.railway.com.trainplan.common.utils;

import java.util.Comparator;

import org.railway.com.trainplan.entity.HighLineCrewInfo;

public class ComparatorObj  implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		HighLineCrewInfo h1=(HighLineCrewInfo)o1;
		HighLineCrewInfo h2=(HighLineCrewInfo)o2;

		   //首先比较年龄，如果年龄相同，则比较名字

		  int flag=h1.getIndex().compareTo(h2.getIndex());
		  if(flag==0){
		   return h1.getName1().compareTo(h2.getName1());
		  }else{
		   return flag;
		  }  
	}
	

}
