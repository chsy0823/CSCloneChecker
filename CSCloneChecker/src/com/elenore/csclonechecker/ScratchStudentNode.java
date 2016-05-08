package com.elenore.csclonechecker;

import java.util.Vector;

public class ScratchStudentNode {
	
	private String studentName;
	private String studentNum;
	private Vector<Double> featureVector;
	
	ScratchStudentNode(String name, String num, Vector<Double> featureVec) {
		
		this.studentName = name;
		this.studentNum = num;
		this.featureVector = featureVec;
	}
	
	public String getStudentName() {
		
		return this.studentName;
	}
	
	public String getStudentNum() {
		
		return this.studentNum;
	}
	
	public Vector<Double> getFeatureVector() {
		
		return this.featureVector;
	}
}
