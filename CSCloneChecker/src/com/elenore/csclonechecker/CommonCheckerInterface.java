package com.elenore.csclonechecker;

public interface CommonCheckerInterface {
	
	//Check option mask bit
	public static final int CHECKOPTCHANGESEQUENCE 	= 0x00000001;
	public static final int CHECKOPTCHANGENAMES 	= 0x00000010;
	
	//======================================================================================================================
	//	Preprocessing methods
	//======================================================================================================================
	
	/**
	 * Load files from input directory and store proper file to file array list.
	 * You can check sub directory too by setting checkSubDir parameter with true.
	 * @param directoryPath
	 * @param checkSubDir
	 * @autor Suhyong-Choi (Elenore)
	 */
	public void readInputFilesInDirectory(String directoryPath, boolean checkSubDir);
	
	/**
	 * If stored file type is needed to convert with proper object to acquire destination file.
	 * you need to implement this method and process file preprocessing
	 * @param directoryPath
	 * @autor Suhyong-Choi (Elenore)
	 */
	public void convertProperFileObjectType(String directoryPath);
	
	
	//======================================================================================================================
	//	Checking methods 
	//======================================================================================================================
	
	/**
	 * Check if source file and destination file is same. (without changing of source file)
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */ 
	public int checkWithEquelFile();
	
	/**
	 * Check destination file which blocks in scripts are jumbled up from source file.
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int checkWithChangeSequence();
	
	/**
	 * Check destination file which variable names or object names are changed from source file.
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int checkWithChangeNames();
	
	/**
	 * Check destination file which useless sprite or code block is added from source file.
	 * For example, You can figure out whether useless data is added without changing of source file from 
	 * searching same pattern of script blocks regardless of count of blocks.
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int checkAddDummyData();
	
	/**
	 * Check destination file by check option mask bit
	 * Eg) if option is CHECKOPTCHANGESEQUENCE | CHECKOPTCHANGENAMES, 
	 * you have to check destination file which variable names or object names are changed and 
	 * which useless sprite or code block is added from source file.
	 * @param option
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int checkWithOption(int option);
	
	
	//======================================================================================================================
	//	Result handling
	//======================================================================================================================
	
	/**
	 *  Show visual checking result and provide result handling option.
	 *  @autor Suhyong-Choi (Elenore) 
	 */
	public void showResult();
	
	/**
	 * Save result file to specific database.
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int saveResultFileToDatabase();
	
	/**
	 * Save result file to path.
	 * @param destinationPath
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int saveResultFileToPath(String destinationPath);
	
	/**
	 * Send result file to email. 
	 * @param email
	 * @return
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int sendResultFileToEmail(String email);
	
}
