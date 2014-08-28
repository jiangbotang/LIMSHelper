package com.alliancepharmaco.LIMSHelper.files;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alliancepharmaco.LIMSHelper.exceptions.DuplicateException;

public class SampleListXls extends Xls{
	/*
	 * key = concatenate of 'subject', 'visit', 'time' of a sample
	 * value = {apId, exId}
	 */
	protected Map<String, String[]> sampleInfo = new HashMap<String, String[]>();
	
	/*
	 * key = concatenate of 'subject', 'visit', 'time' of a sample
	 * value = apId of a samples that have the same concatenate of 'subject', 'visit', 'time'
	 */
	protected Map<String, Set<String>> duplicates = new HashMap<String, Set<String>>();
	
	/*
	 * Constructor
	 * @param fileAddr absolute address of .xls file exported from LIMS using "Sample Check in" template 
	 */
	public SampleListXls(String fileAddr, int subIndex, int visitIndex, int timeIndex,
			int apIdIndex, int exIdIndex) {
		super(fileAddr);
		try {
			init(subIndex, visitIndex, timeIndex, apIdIndex, exIdIndex);
		} catch (DuplicateException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/*
	 * Extract sample information and store in field 'sampleInfo'
	 * @Throws DuplicateException when there are samples have the same subject, visit, and time
	 */
	private void init(int subIndex, int visitIndex, int timeIndex,
			int apIdIndex, int exIdIndex) throws DuplicateException {
		//Sample starts at row index 1 (0: header)
		Map<Integer, Object> subjectCol = getColumn(subIndex, 1);
		Map<Integer, Object> visitCol = getColumn(visitIndex, 1);
		Map<Integer, Object> timeCol = getColumn(timeIndex, 1);
		Map<Integer, Object> apIdCol = getColumn(apIdIndex, 1);
		Map<Integer, Object> exIdCol = getColumn(exIdIndex, 1);
		
		//extract sample information, store in 'sampleInfo', record any duplicates
		Set<Integer> keyset = subjectCol.keySet();
		for (Integer key : keyset) {
			Object subjectObj = subjectCol.get(key);
			Object visitObj = visitCol.get(key);
			Object timeObj = timeCol.get(key);
			if((subjectObj !=null) && (visitObj !=null) && (timeObj !=null)) {
				String idString = subjectObj.toString() + visitObj.toString() + timeObj.toString();
				String apId = apIdCol.get(key).toString();
				String exId = exIdCol.get(key).toString();
				String[] value = {apId, exId};
				//Map.put return the previous value associated the key
				//return null if there is no value associated with the key
				String[] previousValue = sampleInfo.put(idString, value);
				if (previousValue != null) {
					if (duplicates.containsKey(idString)) {
						duplicates.get(idString).add(apId);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(apId);
						set.add(previousValue[0]);
						duplicates.put(idString, set);
					}
				}
			}
		}
		//Throw DuplicateException if there is duplicates
		if(!duplicates.isEmpty()) {
			String message = "duplicate samples (AP #:\n";
			Set<String> dupKeyset = duplicates.keySet();
			for (String key : dupKeyset) {
				message += duplicates.get(key).toString() + "\n";
			}
			throw (new DuplicateException(message));
		}
	}
	
////////////////////////////////////////Test Methods//////////////////////////////////////////////
	public static void constructorTest() {
		SampleListXls xls = new SampleListXls("C:\\Eclipse\\eclipse-standard-luna-R-win32\\eclipse\\workplace\\LIMSHelper\\resrc\\manifest.xls",
				8, 9, 11, 2, 13);
		Set<String> keyset = xls.sampleInfo.keySet();
		for (String key : keyset) {
			System.out.println(key + "\t" + xls.sampleInfo.get(key)[0]);
		}
	}
	
	public static void test() {
		constructorTest();
	}
}
