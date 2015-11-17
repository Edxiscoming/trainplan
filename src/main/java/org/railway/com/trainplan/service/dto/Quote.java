/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.railway.com.trainplan.service.dto;

public class Quote {

	/**
	 * rundate
crosscount
traincount
dayindex

	 */
	private  String rundate;
    private  int crosscount ;
    private  int traincount ;
    private  String dayindex;
    //刷新时间
	private  String refreshtime;
	public String getRundate() {
		return rundate;
	}

	

	public String getDayindex() {
		return dayindex;
	}

	public void setRundate(String rundate) {
		this.rundate = rundate;
	}

	

	public void setDayindex(String dayindex) {
		this.dayindex = dayindex;
	}



	public int getCrosscount() {
		return crosscount;
	}



	public void setCrosscount(int crosscount) {
		this.crosscount = crosscount;
	}



	public int getTraincount() {
		return traincount;
	}



	public void setTraincount(int traincount) {
		this.traincount = traincount;
	}



	public String getRefreshtime() {
		return refreshtime;
	}



	public void setRefreshtime(String refreshtime) {
		this.refreshtime = refreshtime;
	}




	
}
