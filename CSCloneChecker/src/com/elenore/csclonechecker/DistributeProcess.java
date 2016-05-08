package com.elenore.csclonechecker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Observable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DistributeProcess extends Observable implements Runnable {
	
	String jobID;
	String language;
	String serverIP;
	int port;
	Object sendData;
	
	Socket socket;
	
	public DistributeProcess(String jobID, String language, String ip, int port, Object sendData) {
		this.jobID = jobID;
		this.language = language;
		this.serverIP = ip;
		this.port = port;
		this.sendData = sendData;
	}
	
	private int startScratchServerHandler() {
		
		try {
           
            System.out.println("Connecting to server IP : " + this.serverIP);
             
            // 소켓을 생성하여 연결을 요청한다.
            socket = new Socket(this.serverIP, this.port);
            
            System.out.println("connected..");
            
            JSONObject sendDataJson = (JSONObject)sendData;
            
   
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(sendDataJson.toString());
		    
		    System.out.println(sendDataJson.toString());
		   
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String read = dis.readUTF();
		    
		    JSONParser jsonParser = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) jsonParser.parse(read);
		    
		    this.notifyResult(false, "success", resultJsonObject);
		    
		    System.out.println("ok!");
		    
		    return 0;
    		
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return -1;
	}
	
	private void notifyResult(boolean error, String message, JSONObject returnJson) {
		
		JSONObject returnObject = new JSONObject();
		returnObject.put("error", error);
		returnObject.put("message", message);
		returnObject.put("data", returnJson);
		
		setChanged();
        notifyObservers(returnObject);
	}
	private void scratchDistributeJobHandler() {
		
		if(this.startScratchServerHandler() == -1) {
			
			this.notifyResult(true, "connect server error!", null);
		}
	}
	public void run() {
        
		if(language.equals("scratch")) {
			this.scratchDistributeJobHandler();
		}
    }

}
