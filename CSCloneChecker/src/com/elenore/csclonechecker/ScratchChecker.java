package com.elenore.csclonechecker;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScratchChecker implements CommonCheckerInterface {
	
	ArrayList<File> inputFileList;
	String rootDir;
	
	public ScratchChecker() {
		this.inputFileList = new ArrayList<File>();
		this.rootDir = null;
	}
	

	private JSONObject convertFileToJSON (String dir){
		
	    // Read from File to String
		JSONParser parser = new JSONParser();
		
	    try {
	    	
	    	Object obj = parser.parse(new FileReader(dir));
	    	//Object obj = parser.parse(file.toString());
			JSONObject jsonObject = (JSONObject) obj;
			
			return jsonObject;
	        
	    } catch (Exception e) {
	       
	    }
	    
	    return null;
	}
	
	private void searchFileRecursive(File dirFile,boolean checkSubDir) {
		
		File []fileList=dirFile.listFiles();
		
		try{
			for(File tempFile : fileList) {
				
				//read only project json file
				if(tempFile.isFile() && tempFile.getName().equals("project.json")) {
					
//				    String tempPath=tempFile.getParent();
//				    String tempFileName=tempFile.getName();
//				    System.out.println("Path="+tempPath);
//				    System.out.println("FileName="+tempFileName);
				    JSONObject obj = this.convertFileToJSON(tempFile.getCanonicalPath());
				    this.inputFileList.add(tempFile);
				    /*** Do something withd tempPath and temp FileName ^^; ***/
			    }
				else if(tempFile.isDirectory()) {
					
					if(checkSubDir) {
						File subDirFile=new File(tempFile.getCanonicalPath());
						this.searchFileRecursive(subDirFile,checkSubDir);
					}
				}
			}
		}catch(Exception e) {
			
			System.out.println("Read file error "+e.getStackTrace());
		}
	}
	
	@Override
	public void readInputFilesInDirectory(String directory, boolean checkSubDir) {
		// TODO Auto-generated method stub
		
		this.rootDir = directory;
		
		File dirFile=new File(directory);
		this.searchFileRecursive(dirFile,checkSubDir);
		
		System.out.println("Read File Count = "+this.inputFileList.size());
	}

	@Override
	public void setProperFileObjectType() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int checkWithEquelFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkWithChangeSequence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkWithChangeNames() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkAddDummyData() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkWithOption(int option) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveResultFileToDatabase() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveResultFileToPath(String destinationPath) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int sendResultFileToEmail(String email) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void showResult() {
		// TODO Auto-generated method stub
		
	}
}
