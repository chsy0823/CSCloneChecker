package com.elenore.csclonechecker;

import java.util.Vector;

public class ScratchStudentNode {
	
	private String studentName;
	private String studentNum;
	private Vector<Double> featureVector;
	private double similiarity;
	
	ScratchStudentNode(String name, String num, Vector<Double> featureVec) {
		
		this.studentName = name;
		this.studentNum = num;
		this.featureVector = featureVec;
	}
	
	ScratchStudentNode(String name, String num, double similiarity) {
		
		this.studentName = name;
		this.studentNum = num;
		this.similiarity = similiarity;
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
	
	public double getSimiliarity() {
		
		return this.similiarity;
	}
}
