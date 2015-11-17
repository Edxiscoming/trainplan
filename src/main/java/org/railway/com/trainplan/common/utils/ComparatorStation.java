package org.railway.com.trainplan.common.utils;

import java.util.Comparator;

public class ComparatorStation implements Comparator<Station> {

	@Override
	public int compare(Station o1, Station o2) {
	
		return o1.getMinites().compareTo(o2.getMinites());
	}

}
