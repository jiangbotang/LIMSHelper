package com.alliancepharmaco.LIMSHelper.files;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LimsSampleListXls extends Xls {
	//[subject + visit + time] uniquely identify a sample in LIMS
	//Store the identifier of each sample with map
	//key = subject, visit, time concatenated as a string, value = row index
	protected Map<String, Integer> sampleIdentifier = new HashMap<String, Integer>();
	
	/*
	 * Constructor
	 * @param fileAddr absolute address of .xls file exported from LIMS using "Sample Check in" template 
	 */
	public LimsSampleListXls(String fileAddr) {
		super(fileAddr);
		init();
	}
	
	private void init() {
		//Sample starts at row index 3
		//column index for subject: 3
		Map<Integer, Object> subjectCol = getColumn(3, 3);
		//column index for visit: 11
		Map<Integer, Object> visitCol = getColumn(11, 3);
		//column index for time: 14
		Map<Integer, Object> timeCol = getColumn(14, 3);
		
		//get the identifier from subject, visit, time
		Set<Integer> keyset = subjectCol.keySet();
		for (Integer key : keyset) {
			Object subjectObj = subjectCol.get(key);
			Object visitObj = visitCol.get(key);
			Object timeObj = timeCol.get(key);
			if((subjectObj !=null) && (visitObj !=null) && (timeObj !=null)) {
				String idString = subjectObj.toString() + visitObj.toString() + timeObj.toString();
				sampleIdentifier.put(idString, key);
			}
		}
	}
	
////////////////////////////////Test Methods/////////////////////////////////////////////////////////
	public static void constructorTest() {
		LimsSampleListXls sampleXls = new LimsSampleListXls("C:\\Eclipse\\eclipse-standard-luna-R-win32\\eclipse\\workplace\\LIMSHelper\\resrc\\out_97.xls");
		Set<String> keyset = sampleXls.sampleIdentifier.keySet();
		for (String key : keyset) {
			System.out.println(key + "\t" + sampleXls.sampleIdentifier.get(key));
		}
	}
	
	public static void test() {
		constructorTest();
	}
	
}
