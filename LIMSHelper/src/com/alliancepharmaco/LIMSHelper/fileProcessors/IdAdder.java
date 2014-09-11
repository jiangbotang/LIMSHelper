package com.alliancepharmaco.LIMSHelper.fileProcessors;

import java.util.Map;
import java.util.Set;

import com.alliancepharmaco.LIMSHelper.exceptions.SplNotInLIMSException;
import com.alliancepharmaco.LIMSHelper.files.LimsSampleListXls;
import com.alliancepharmaco.LIMSHelper.files.ManifestXls;
import com.alliancepharmaco.LIMSHelper.files.Xls;

public class IdAdder {
	/*
	 * Add AP_ID and External_ID in manifestXls to limsXls. 
	 * Steps:
	 * 1. Lookup each entry of manifestXls (a sample) in limsXls,
	 * 		false	: error message
	 * 		true	: next step
	 * 2. Add AP_ID and external_ID to the found entry of limsXls
	 * 3. Return the updated limsXls  
	 */
	public static Xls addId(LimsSampleListXls limsXls, 
			ManifestXls manifestXls) throws SplNotInLIMSException {
		Xls limsImportXls = limsXls;
		// Step 1:
		Map<Integer, Map<Integer, Object>> data = limsXls.getData();		
		Map<String, Integer> limsSplIdentifier = limsXls.getSampleIdentifier();
		
		Map<String, String[]> mSampleInfo = manifestXls.getSampleInfo();
		Set<String> mKeyset = mSampleInfo.keySet();
		
		for (String key : mKeyset) {
			if (limsSplIdentifier.containsKey(key)) {
				// Get the index of the sample data
				int sampleIndex = limsSplIdentifier.get(key);
				// Get the sample data
				Map<Integer, Object> sample = data.get(sampleIndex);
				
				// Step 2:
				String apId = mSampleInfo.get(key)[0];
				String exId = mSampleInfo.get(key)[1];
				
				// Determined by .xls exported from LIMS
				// Custom ID (AP ID) at index 5; Sample User Text 1 (External ID) at index 18
				sample.put(5, apId);
				sample.put(18, exId);
				// Update entry with AP ID and External ID added
				data.put(sampleIndex, sample);
				
			}
			//error: sample in manifest, but not in LIMS
			else {
				String errorMessage = "Sample: " + key + " in manifest but not in LIMS.";
				throw(new SplNotInLIMSException(errorMessage));
			}
		}
		
		limsImportXls.setData(data);
		return limsImportXls;
	}
}
