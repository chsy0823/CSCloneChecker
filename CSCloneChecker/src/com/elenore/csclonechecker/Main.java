package com.elenore.csclonechecker;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("test");
		
		ScratchChecker checker = new ScratchChecker();
		//checker.setInputFiles("/Users/Eleonore/Documents/webos/applications/applications/com.webos.app.tvguide/enyo-ilib/ilib/locale");

		checker.readInputFilesInDirectory("/Users/Eleonore/Documents/scratch/input2",true);
		
	}
}
