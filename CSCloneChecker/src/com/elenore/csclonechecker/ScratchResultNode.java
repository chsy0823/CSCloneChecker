package com.elenore.csclonechecker;

import java.util.ArrayList;

public class ScratchResultNode {
	
	private String studentName;
	private String studentNum;
	private ArrayList<ScratchStudentNode> compareList;
	
	ScratchResultNode(String name, String num, ArrayList<ScratchStudentNode> compareList) {
		
		this.studentName = name;
		this.studentNum = num;
		this.compareList = compareList;
	}
	
	public String getStudentName() {
		
		return this.studentName;
	}
	
	public String getStudentNum() {
		
		return this.studentNum;
	}
	
	public ArrayList<ScratchStudentNode> getCompareList() {
		
		return this.compareList;
	}

}
