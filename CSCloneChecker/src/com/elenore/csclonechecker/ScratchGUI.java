package com.elenore.csclonechecker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;  

public class ScratchGUI implements CommonGUIInterface {
	
	private JFrame myFrame;
	private JTable studentList;
	private JTable similiarityList;
	private JSplitPane mainSplitPane;
	private JSplitPane splitBottomRoot;
	private JSplitPane leftTableSplit;
	ScratchGUI() {
		this.myFrame = new JFrame();
	}
	
	public void setStudentListItem(ArrayList<ScratchResultNode> resultObjectList) {
	     
		DefaultTableModel model = (DefaultTableModel) this.studentList.getModel();
		
		for(ScratchResultNode node : resultObjectList) {
			
			String num = node.getStudentNum();
			String name = node.getStudentName();
			
			Object[] obj = {num,name};
			
			model.addRow(obj);
		}
	}
	@Override
	public void startInitGUI() {
		// TODO Auto-generated method stub
		
	
		JLabel imageLabel1 = new JLabel(new ImageIcon("logo.png"));
		imageLabel1.setMinimumSize(new Dimension(0,0));
		
		
		//set left root component : student, similiarity table
		String studentColumn[]={"STNUM","NAME"};
		String similiarityColumn[]={"STNUM","NAME","Similiarity"};
		DefaultTableModel defaultTableModel1 = new DefaultTableModel(null, studentColumn);
		DefaultTableModel defaultTableModel2 = new DefaultTableModel(null, similiarityColumn);
		
		this.studentList = new JTable(defaultTableModel1);
		this.similiarityList = new JTable(defaultTableModel2);
	    
	    JScrollPane sp1=new JScrollPane(this.studentList);
	    sp1.setMinimumSize(new Dimension(200,0));
	    JScrollPane sp2=new JScrollPane(this.similiarityList);
	    //sp.setPreferredSize(new Dimension(100,100));
	    
	    this.leftTableSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sp1,sp2);
	    this.leftTableSplit.setDividerLocation(0.5); //디바이더(분리대) 위치 설정     
	    this.leftTableSplit.setDividerSize(10); //디바이더(분리대) 굵기 설정
        //set right component : menu button list
        
        JPanel rightComponent = new JPanel();
        
        this.splitBottomRoot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.leftTableSplit, rightComponent);
        this.splitBottomRoot.setDividerLocation(0.65); //디바이더(분리대) 위치 설정     
        this.splitBottomRoot.setDividerSize(15); //디바이더(분리대) 굵기 설정
		
		this.mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,imageLabel1, this.splitBottomRoot);
		this.mainSplitPane.setDividerLocation(0.2); //디바이더(분리대) 위치 설정     
		this.mainSplitPane.setDividerSize(10); //디바이더(분리대) 굵기 설정
//		this.mainSplitPane.setOneTouchExpandable(true);
		this.mainSplitPane.setBackground(Color.white);
		
		this.myFrame.add(this.mainSplitPane,"Center");
		this.myFrame.setPreferredSize(new Dimension(900,600));
		this.myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.myFrame.setResizable(true);
		this.myFrame.setVisible(true);
		this.myFrame.pack();
		this.restoreDefaults();

	}

	private void restoreDefaults() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            
                mainSplitPane.setDividerLocation(0.2);
                splitBottomRoot.setDividerLocation(0.65);
                leftTableSplit.setDividerLocation((int)splitBottomRoot.getLeftComponent().getSize().getWidth()/2);
            }
        });
    }
	
	@Override
	public String selectPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
