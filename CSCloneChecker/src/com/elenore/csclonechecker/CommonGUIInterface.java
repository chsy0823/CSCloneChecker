package com.elenore.csclonechecker;

public interface CommonGUIInterface {

	/**
	 * Init GUI frame and start GUI for user to perform action
	 * @author Suhyong-Choi (Eleonore)
	 * 
	 */
	public void startInitGUI();
	
	/**
	 * Method that is selecting input directory path 
	 * @return path
	 * @autor Suhyong-Choi (Elenore)
	 */
	public int selectPath();
	
	/**
	 * Show result of search properly
	 * @author Suhyong-Choi (Eleonore)
	 */
	public void showResult(Object result);
	
	/**
	 * Execute program
	 * @author Suhyong-Choi (Eleonore)
	 */
	public void execute();
	
	/**
	 * Show alert dialog with message
	 * @param message
	 */
	public void showMessage(String message, boolean error);
}
