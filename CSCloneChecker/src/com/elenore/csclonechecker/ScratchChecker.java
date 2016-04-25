package com.elenore.csclonechecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScratchChecker implements CommonCheckerInterface {
	
	ArrayList<JSONObject> inputFileList;
	String rootDir;
	
	public ScratchChecker() {
		this.inputFileList = new ArrayList<JSONObject>();
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
				if(tempFile.isFile()) {
					
					String fileName = tempFile.getName();
					String extension = "";
					int i = fileName.lastIndexOf('.');
					if (i >= 0) {
					    extension = fileName.substring(i+1);
					}
					
//				    String tempPath=tempFile.getParent();
//				    String tempFileName=tempFile.getName();
//				    System.out.println("Path="+tempPath);
//				    System.out.println("FileName="+tempFileName);

					if(extension.equals("json")) {
						if(tempFile.getName().equals("project.json")) {
							
							JSONObject obj = this.convertFileToJSON(tempFile.getCanonicalPath());
							
							if(obj != null)
								this.inputFileList.add(obj);
							else 
								System.out.println("covert to json error");
						}
					}
					
					else if(extension.equals("sb2")) {

						this.convertProperFileObjectType(tempFile.getCanonicalPath());
					}
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
	
	private int unzipFile(String zipFilePath, String outputPath) {
		
		byte[] buffer = new byte[1024];
    	
	     try{
	    	
	    	//create output directory is not exists
	    	File folder = new File(outputPath);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}
	    		
	    	//get the zip file content
	    	ZipInputStream zis = 
	    		new ZipInputStream(new FileInputStream(zipFilePath));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	    		
	    	while(ze!=null){
	    			
	    	   String fileName = ze.getName();
	           File newFile = new File(outputPath + File.separator + fileName);
	                
//	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	                
	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();
	              
	            FileOutputStream fos = new FileOutputStream(newFile);             

	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	            	fos.write(buffer, 0, len);
	            }
	        		
	            fos.close();   
	            ze = zis.getNextEntry();
	    	}
	    	
	        zis.closeEntry();
	    	zis.close();
	    	
	    	return 0;
	    		
	    }catch(IOException ex){
	       ex.printStackTrace();
	       return -1;
	    }
		
	}
	
	@Override
	public void readInputFilesInDirectory(String directoryPath, boolean checkSubDir) {
		// TODO Auto-generated method stub
		
		this.rootDir = directoryPath;
		
		File dirFile=new File(directoryPath);
		this.searchFileRecursive(dirFile,checkSubDir);
		
		System.out.println("Read File Count = "+this.inputFileList.size());
	}

	@Override
	public void convertProperFileObjectType(String directoryPath) {
		// TODO Auto-generated method stub
		
		File sourceFile = new File(directoryPath);
		String parentPath = sourceFile.getParent();
		String fileName = sourceFile.getName();
		String fileNameWithoutExt = "";
		
		int i = fileName.lastIndexOf(".");
		if (i >= 0) {
			fileNameWithoutExt = fileName.substring(0,i);
			String zipFilePath = parentPath+"/"+fileNameWithoutExt+".zip";
			File targetFile = new File(zipFilePath);
			
			boolean success = sourceFile.renameTo(targetFile);
			if (success) {
			    // File has been renamed
				if(this.unzipFile(zipFilePath, parentPath+"/"+fileNameWithoutExt) !=-1) {
					this.searchFileRecursive(new File(parentPath+"/"+fileNameWithoutExt), true);
				}
				else {
					System.out.println("unzip file error");
				}
			}
			else {
				System.out.println("file rename failed");
			}
		}
		
		else {
			System.out.println("filename error");
		}
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
