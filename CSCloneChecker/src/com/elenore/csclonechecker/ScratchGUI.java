package com.elenore.csclonechecker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;  

public class ScratchGUI extends MouseAdapter implements CommonGUIInterface, ActionListener {
	
	public static final int EQUALFILE 		= 0;
	public static final int CHANGESEQUENCE 	= 1;
	public static final int CHANGENAMES 	= 2;
	public static final int ADDDUMMYDATA 	= 3;
	public static final int WITHOPTION 		= 4;
	
	interface ExecuteCallback {
		void excuteCallbackMethod();
	}
	
	private boolean exCallback_condition;
	private ExecuteCallback exCallback;
	
	private JFrame myFrame;
	private JTable studentList;
	private JTable similiarityList;
	private JSplitPane mainSplitPane;
	private JSplitPane splitBottomRoot;
	private JSplitPane leftTableSplit;
	private ArrayList<ScratchResultNode> resultObjectList;
	
	private JButton selectFileButton;
	private JButton executeButton;
	
	private String inputPath;
	private int checkingOption;
	
	ScratchGUI() {
		this.myFrame = new JFrame();
		this.exCallback_condition = false;
		this.exCallback = null;
		this.checkingOption = EQUALFILE;
	}
	
	public void setExCallback(ExecuteCallback callback) {
		this.exCallback = callback;
		this.exCallback_condition = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//액션 리스너 재정의
		if (e.getSource().equals(selectFileButton)) {
			
			if(this.selectPath() != -1) {
				String[] dir = inputPath.split("/");
				this.selectFileButton.setText(dir[dir.length-1]);
			}
		}
		else if(e.getSource().equals(executeButton))  {
			this.execute();
		}
	}
	
	@Override
    public void mouseClicked(MouseEvent e) {
		if (e.getButton() == 1) { 
			JTable target = (JTable)e.getSource();
			
			if(target == this.studentList) {
				System.out.println("student list");
				
				int row = target.rowAtPoint(e.getPoint());
				
				this.setSimiliarityListItem(this.resultObjectList.get(row));
			}
			else {
				
			}
		} //클릭
		if (e.getClickCount() == 2) { } // 더블클릭
		if (e.getButton() == 3) { } // 오른쪽 클릭
	}
	
	public String getInputPath() {
		return this.inputPath;
	}
	
	public int getCheckingOption() {
		return this.checkingOption;
	}
    
	private void setStudentListItem(ArrayList<ScratchResultNode> resultObjectList) {
	     
		DefaultTableModel model = (DefaultTableModel) this.studentList.getModel();
		
		for(ScratchResultNode node : resultObjectList) {
			
			String num = node.getStudentNum();
			String name = node.getStudentName();
			
			Object[] obj = {num,name};
			
			model.addRow(obj);
		}
	}
	
	private void clearResult() {
			
		if(this.resultObjectList != null) {
			this.resultObjectList.clear();
			
			DefaultTableModel modelStudent = (DefaultTableModel) this.studentList.getModel();
			DefaultTableModel modelSimilarity = (DefaultTableModel) this.similiarityList.getModel();
			
			modelStudent.setRowCount(0);
			modelSimilarity.setRowCount(0);
			
		}
	}

	private void setSimiliarityListItem(ScratchResultNode node) {
		
		if(node!=null) {
			
			DefaultTableModel model = (DefaultTableModel) this.similiarityList.getModel();
			
			model.setRowCount(0);
			
			ArrayList<ScratchStudentNode> compareList = node.getCompareList();
			
			for(ScratchStudentNode stdNode : compareList) {
				
				String num = stdNode.getStudentNum();
				String name = stdNode.getStudentName();
				float similiarity = stdNode.getSimiliarity();
				
				Object[] obj = {num,name,similiarity};
				
				model.addRow(obj);
			}
			
		}
		else {
			System.out.println("null similiarity data");
		}
	}
	@Override
	public void startInitGUI() {
		// TODO Auto-generated method stub
		
	
		JLabel imageLabel1 = new JLabel(new ImageIcon("logo.png"));
		imageLabel1.setMinimumSize(new Dimension(0,0));
		
		
		//set left root component : student, similiarity table
		String studentColumn[]={"STNUM","NAME"};
		String similiarityColumn[]={"STNUM","NAME","Similiarity(%)"};
		DefaultTableModel defaultTableModel1 = new DefaultTableModel(null, studentColumn);
		DefaultTableModel defaultTableModel2 = new DefaultTableModel(null, similiarityColumn);
		
		this.studentList = new JTable(defaultTableModel1);
		this.similiarityList = new JTable(defaultTableModel2);
		
		this.studentList.addMouseListener(this);
		this.similiarityList.addMouseListener(this);
	    
	    JScrollPane sp1=new JScrollPane(this.studentList);
	    sp1.setMinimumSize(new Dimension(200,0));
	    JScrollPane sp2=new JScrollPane(this.similiarityList);
	    sp2.setMinimumSize(new Dimension(300,0));
	    //sp.setPreferredSize(new Dimension(100,100));
	    
	    this.leftTableSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sp1,sp2);
	    this.leftTableSplit.setDividerLocation(0.5); //디바이더(분리대) 위치 설정     
	    this.leftTableSplit.setDividerSize(10); //디바이더(분리대) 굵기 설정
//	    this.leftTableSplit.setMinimumSize(new Dimension());
        //set right component : menu button list
        
        JPanel rightComponent = new JPanel();
        JLabel similiarityLabel = new JLabel("Select Similiarity");
        
        JSlider similiaritySlider = new JSlider(JSlider.HORIZONTAL, 20, 100, 50);
        similiaritySlider.setMinorTickSpacing(5);  
        similiaritySlider.setMajorTickSpacing(10);
          
        similiaritySlider.setPaintTicks(true);  
        similiaritySlider.setPaintLabels(true);  
        
        JLabel selectFileLabel = new JLabel("Select input directory");
        this.selectFileButton = new JButton("Select");
        this.selectFileButton.addActionListener(this);
        
        this.executeButton = new JButton("Start!");
        this.executeButton.addActionListener(this);
        
        JPanel exbuttonPanel = new JPanel();
        exbuttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        exbuttonPanel.add(this.executeButton);
        
        
        rightComponent.add(similiarityLabel);
        rightComponent.add(similiaritySlider);
        rightComponent.add(selectFileLabel);
        rightComponent.add(selectFileButton);
        rightComponent.add(exbuttonPanel,BorderLayout.SOUTH);
        
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
	public int selectPath() {
		// TODO Auto-generated method stub
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("~/"));
		fileChooser.setDialogTitle("Select input directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {

        	System.out.println(fileChooser.getSelectedFile());
        	
        	this.inputPath = fileChooser.getSelectedFile().toString();
        	return 0;
        }
        
		return -1;
	}

	@Override
	public void showResult(Object result) {
		// TODO Auto-generated method stub
		
		if(result != null) {
			ArrayList<ScratchResultNode> resultObjectList = (ArrayList<ScratchResultNode>)result;
		
			this.resultObjectList = resultObjectList;
			this.setStudentListItem(resultObjectList);
		}
		
		else {

			this.showErrorMessage("null result data");
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		if(this.inputPath == null) {
			
			this.showErrorMessage("Please, select input directory!");
		}
		else if(!this.exCallback_condition || this.exCallback == null) {
			
			this.showErrorMessage("Please, implement excute callback method");
		}
		else {
			this.clearResult();
			this.exCallback.excuteCallbackMethod();
		}
	}
	
	@Override
	public void showErrorMessage(String message) {
		
		JOptionPane.showMessageDialog(null, message);
	}
}
