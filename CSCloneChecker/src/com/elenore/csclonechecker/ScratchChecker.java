package com.elenore.csclonechecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ScratchChecker implements CommonCheckerInterface,Observer {
	
	private ArrayList<JSONObject> inputFileList;
	private ArrayList<ScratchResultNode> resultObjectList;
	private ArrayList<ScratchStudentNode> studentNodeCollection;

	private boolean distributeJob;	
	private String rootDir;
	private String thisLanguage;
	
	private DistributeProcess distProcess;
	
	private final String serverIp = "52.79.150.170";
	private final int port = 10000;
	private ScratchGUI scGUIInterface;
	
	public ScratchChecker() {
		this.inputFileList = new ArrayList<JSONObject>();
		this.studentNodeCollection = new ArrayList<ScratchStudentNode>();
		this.rootDir = null;
		this.distributeJob = false;
		this.thisLanguage = "scratch";
		this.resultObjectList = new ArrayList<ScratchResultNode>();
		this.scGUIInterface = new ScratchGUI();
		
		ScratchGUI.ExecuteCallback exCallback = new ScratchGUI.ExecuteCallback() {
			
			@Override
			public void excuteCallbackMethod() {
				// TODO Auto-generated method stub
				String inputPath = scGUIInterface.getInputPath();
				int checkingOption = scGUIInterface.getCheckingOption();
				
				if(inputPath != null) {
					
					if(readInputFilesInDirectory(inputPath,true) !=-1) {
					
						switch(checkingOption) {
						case EQUALFILE:
							//checkWithEqualFile();
							
							try  {
								String data = "{\"error\" : \"false\", "
										+ "\"message\" : \"test\", "
										+ "\"jobID\" : \"1223134\","
										+ "\"checkingType\" : \"4\","
										+ "\"runningTime\" : \"2154\","
										+ "\"result\" : ["
										+ "{\"num\" : \"201111394\",\"name\" : \"수용최\",\"compareList\" : ["
										+ "{\"num\" : \"201311343\",\"name\" : \"현택\",\"similiarity\" : \"95\"},"
										+ "{\"num\" : \"201311342\",\"name\" : \"민우\",\"similiarity\" : \"72\"},"
										+ "{\"num\" : \"201311321\",\"name\" : \"수용\",\"similiarity\" : \"5\"}"
										+ "]},"
										+ "{\"num\" : \"201111391\",\"name\" : \"가나\",\"compareList\" : ["
										+ "{\"num\" : \"201311343\",\"name\" : \"이오삼\",\"similiarity\" : \"91\"},"
										+ "{\"num\" : \"201311342\",\"name\" : \"민우2\",\"similiarity\" : \"33\"},"
										+ "{\"num\" : \"201311321\",\"name\" : \"수용12\",\"similiarity\" : \"51\"}"
										+ "]}"
										+ "]}";
								
								JSONObject temp = (JSONObject) new JSONParser().parse(data);
								
								parseResult(temp);
								showResult();
							}
							catch(Exception e) {
								e.printStackTrace();
	
							}
							
							break;
						}
					}
				}	
			}
		};
		
		this.scGUIInterface.setExCallback(exCallback);
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
	private String[] checkDirNameFormat(String name) {
		
		return name.split("_");
	}
	
	private int searchFileRecursive(File dirFile,boolean checkSubDir) {
		
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
							
							if(obj != null) {
								String[] parentDir = tempFile.getParent().split("/");
								String parentDirName = parentDir[parentDir.length-1];
								String[] dirFormat = this.checkDirNameFormat(parentDirName);
								if(dirFormat.length == 2) {
									
									obj.put("studentNum",dirFormat[0]);
									obj.put("studentName", dirFormat[1]);
									
									this.inputFileList.add(obj);
								}
								else {
									this.scGUIInterface.showErrorMessage("Invalid file name format! File name should be 'studentNum_studentName'");
									return -1;
								}	
							}
						
							else  {
								this.scGUIInterface.showErrorMessage("covert to json error");
								return -1;
							}
						}
					}
					
					else if(extension.equals("sb2")) {

						if(this.convertProperFileObjectType(tempFile.getCanonicalPath())==-1) {
							return -1;
						}
					}
			    }
				else if(tempFile.isDirectory()) {
					
					if(checkSubDir) {
						File subDirFile=new File(tempFile.getCanonicalPath());
						return this.searchFileRecursive(subDirFile,checkSubDir);
					}
				}
			}
		}catch(Exception e) {
			
			this.scGUIInterface.showErrorMessage("Read file error");
			//System.out.println("Read file error "+e.getStackTrace());
		}
		
		return 0;
	}
	
	private void parseResult(JSONObject data) {
		
		String jobID = (String)data.get("jobID");
		String checkType = (String)data.get("checkingType");
		String runningTime = (String)data.get("runningTime");
		
		JSONArray resultArr = (JSONArray)data.get("result");
		
		for (int i =0; i<resultArr.size(); i++) {
			
			JSONObject obj = (JSONObject)resultArr.get(i);
			
			String name = (String)obj.get("name");
			String num = (String)obj.get("num");
			JSONArray compareList = (JSONArray)obj.get("compareList");
			ArrayList<ScratchStudentNode> compareListTemp = new ArrayList<ScratchStudentNode>();
			
			for(int j=0; j<compareList.size(); j++) {
				
				JSONObject objCompare = (JSONObject)compareList.get(j);
				String nameCompare = (String)objCompare.get("name");
				String numCompare = (String)objCompare.get("num");
				float similiarity = Float.parseFloat((String) objCompare.get("similiarity"));
				
				ScratchStudentNode node = new ScratchStudentNode(nameCompare, numCompare, similiarity);
				compareListTemp.add(node);
			}
			
			ScratchResultNode resultNode = new ScratchResultNode(name,num,compareListTemp);
			this.resultObjectList.add(resultNode);
		}
		
		System.out.println("Parse result ok!");
		
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
	
	private JSONObject getChildHavingScripts(JSONArray children) {
		
		for(int i=0; i<children.size(); i++) {
			
			JSONArray script = (JSONArray) ((JSONObject)children.get(i)).get("scripts");
			
			if(script != null) {
				return (JSONObject)children.get(i);
			}
		}
		
		return null;
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
		this.scGUIInterface.startInitGUI();
		this.setDistributeJob(false);
		
//		this.readInputFilesInDirectory("/Users/Eleonore/Documents/scratch/input4",true);
//		this.setDistributeJob(true);
//		this.checkWithEqualFile();
		
	}
	
	@Override
	public void setDistributeJob(boolean flag) {
		this.distributeJob = flag;
	}
	
	@Override
	public int initDistributeJob(Object jobData, int checkingType) {
		
		JSONObject sendObj = new JSONObject();
		
		String jobID = UUID.randomUUID().toString();
		sendObj.put("checkingType", checkingType);
		sendObj.put("jobID", jobID);
		sendObj.put("language", this.thisLanguage);
		sendObj.put("dataType", "vector");
		
		JSONArray dataArray = new JSONArray();
		ArrayList<ScratchStudentNode> studentNodeCollection = (ArrayList<ScratchStudentNode>) jobData;
		
		for(ScratchStudentNode node : studentNodeCollection) {
			
			JSONObject dataDic = new JSONObject();
			dataDic.put("name", node.getStudentName());
			dataDic.put("num", node.getStudentNum());
			
			JSONArray featureVectorArr = new JSONArray();
			
			for(double feature : node.getFeatureVector()) {
				featureVectorArr.add(feature);
			}
			
			dataDic.put("featureVector", featureVectorArr);
			dataArray.add(dataDic);
		}
		
		sendObj.put("data", dataArray);
		
		distProcess = new DistributeProcess(jobID,this.thisLanguage,this.serverIp,this.port,sendObj);
		distProcess.addObserver(this);
		Thread job = new Thread(distProcess);
		job.start();
		
		return 0;
	}
	
	@Override
	public int readInputFilesInDirectory(String directoryPath, boolean checkSubDir) {
		// TODO Auto-generated method stub
		
		this.rootDir = directoryPath;
		
		File dirFile=new File(directoryPath);
		if(this.searchFileRecursive(dirFile,checkSubDir)!=-1) {
			
			if(this.inputFileList.size()<=0) {
				this.scGUIInterface.showErrorMessage("No files to read!");
			}
			else {
				this.scGUIInterface.showErrorMessage("Read files success!");
			}
			return 0;
		}
		
		return -1;
		
	}

	@Override
	public int convertProperFileObjectType(String directoryPath) {
		// TODO Auto-generated method stub
		
		File sourceFile = new File(directoryPath);
		String parentPath = sourceFile.getParent();
		String fileName = sourceFile.getName();
		String fileNameWithoutExt = "";
		
		int i = fileName.lastIndexOf(".");
		if (i >= 0) {
			
			fileNameWithoutExt = fileName.substring(0,i);
			
			String[] dirFormat = this.checkDirNameFormat(fileNameWithoutExt);
			if(dirFormat.length == 2) {
				
				String zipFilePath = parentPath+"/"+fileNameWithoutExt+".zip";
				File targetFile = new File(zipFilePath);
				
				boolean success = sourceFile.renameTo(targetFile);
				if (success) {
				    // File has been renamed
					if(this.unzipFile(zipFilePath, parentPath+"/"+fileNameWithoutExt) !=-1) {
						return this.searchFileRecursive(new File(parentPath+"/"+fileNameWithoutExt), true);
					}
					else {
						this.scGUIInterface.showErrorMessage("Unzip file error");
					}
				}
				else {
					this.scGUIInterface.showErrorMessage("file rename failed");
				}
			}
			else {
				this.scGUIInterface.showErrorMessage("Invalid file name format! File name should be 'studentNum_studentName!'");
			}
		}
		
		else {
			this.scGUIInterface.showErrorMessage("filename error");
		}
		
		return -1;
	}
	
	@Override
	public int checkWithEqualFile() {
		// TODO Auto-generated method stub

		//전체objectcount/costume count/current costume index/children count/firstchild x/firstchild y/firstchild script count/first srcipt x/first script y/
		for(JSONObject obj : inputFileList) {
			
			String studentNum = (String)obj.get("studentNum");
			String studentName = (String)obj.get("studentName");
			
			JSONArray costumes = (JSONArray) obj.get("costumes");
			JSONArray children = (JSONArray) obj.get("children");
			
			Vector<Double> featureVector = new Vector<Double>();
			
			long currentCostumeIndex = (long)obj.get("currentCostumeIndex");
			
			int objectCount = obj.size();
			int costumeCount = costumes.size();
			int childCount = children.size();
			double firstChildX = -1;
			double firstChildY = -1;
			double firstChildScriptX = -1;
			double firstChildScriptY = -1;
			int scriptCount = 0;
			
			if(childCount > 0) {
				
				JSONObject child = this.getChildHavingScripts(children);
				
				if(child != null) {
					
					JSONArray script = (JSONArray)child.get("scripts");
					
					scriptCount = script.size();
					firstChildX = Double.valueOf(String.valueOf((Object)((JSONObject)child).get("scratchX")));
					firstChildY = Double.valueOf(String.valueOf((Object)((JSONObject)child).get("scratchY")));
					firstChildScriptX = Double.valueOf(String.valueOf((Object)((JSONArray)script.get(0)).get(0)));
					firstChildScriptY = Double.valueOf(String.valueOf((Object)((JSONArray)script.get(0)).get(1)));
				}
			}
			
			featureVector.add((double)objectCount);
			featureVector.add((double)costumeCount);
			featureVector.add((double)currentCostumeIndex);
			featureVector.add((double)childCount);
			featureVector.add(firstChildX);
			featureVector.add(firstChildY);
			featureVector.add((double)scriptCount);
			featureVector.add(firstChildScriptX);
			featureVector.add(firstChildScriptY);
			
			ScratchStudentNode node = new ScratchStudentNode(studentName, studentNum, featureVector);
			
			this.studentNodeCollection.add(node);
			
			
			System.out.format("%s %s %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f %.1f\n",studentNum, studentName, (double)objectCount, (double)costumeCount,(double)currentCostumeIndex, (double)childCount, firstChildX, firstChildY, (double)scriptCount, firstChildScriptX, firstChildScriptY);
			
		}
		
		if(this.distributeJob) {
			//this.initDistributeJob(this.studentNodeCollection,EQUALFILE);
		}
		else {
			
		}
		
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
		
		this.scGUIInterface.showResult(this.resultObjectList);
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		JSONObject obj = (JSONObject)arg;
		boolean isError = (boolean)obj.get("error");
		JSONObject data = (JSONObject) obj.get("data");
		
		System.out.println("observer callback = "+obj.toString());
		
		if(!isError) {
			
			this.parseResult(data);
			this.showResult();
		}
		
	}
}