import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;
import java.util.Iterator;
import java.io.*;

import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.json.*;

import javax.swing.JToolBar;

import java.awt.BorderLayout;

import javax.swing.JToggleButton;

public class DanaClientFrame extends JFrame {

	public class JOptionPaneMultiInput {
		JOptionPaneMultiInput(JSONObject args,String message) {
			JSONArray names;
			JPanel myPanel = new JPanel();
			try {
				names=args.getJSONArray("names");
				if (names.length()>10)
					myPanel.setLayout(new GridLayout((int)(names.length()/2)+1, 4));
				else
					myPanel.setLayout(new GridLayout(names.length(),2));
				for (int i=0;i<names.length();i++)
				{
					myPanel.add(new JLabel("   ".concat(names.getString(i))));
					myPanel.add(new JTextField(5));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int result = JOptionPane.showConfirmDialog(null, myPanel, 
					message, JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {

			}

		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int MaxBufferLength=102400;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DanaClientFrame.setDefaultLookAndFeelDecorated(true);  
					DanaClientFrame frame = new DanaClientFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static public Frame GetFrame(Component c)
	{
		if(c instanceof Frame || null==c)
			return c==null ? null : (Frame)c;
		return GetFrame(c.getParent());
	}
	/**
	 * Create the frame.
	 */
	public DanaClientFrame() {
		setTitle("Dana Virtual Laboratory");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 450);
		
		menuBar = new JMenuBar();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane();
		textPane = new JTextPane();
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar()==10)
				{
					String ss=textPane.getText();
					lastLine="";
					int i=ss.length()-1;
					while (true && i>0)
					{
						if(ss.charAt(i-1)=='\n')
						{
							break;
						}
						i--;
					}
					lastLine=ss.substring(i,ss.length()-2);	
					if (out!=null && danaSocket!=null && !danaSocket.isClosed() && !danaSocket.isOutputShutdown())
						out.println(lastLine);
				}
			}
		});
		scrollPane.setViewportView(textPane);

		toolBar_1 = new JToolBar();

		contentPane.add(toolBar_1);
		menuBar.setMaximumSize(new Dimension(4000,5));
		menuBar.setVisible(false);
		contentPane.add(menuBar);
		contentPane.add(scrollPane);
		toolBar_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		tglbtnDanamodels = new JToggleButton("DanaModels");
		tglbtnDanamodels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jsonMenuStructure==null)
				{
					createDanaModelsMenu();
					addJsonMenu(jsonMenuStructure,menuBar);
					menuBar.setVisible(true);
				}
				if (tglbtnDanamodels.isSelected())
					menuBar.setVisible(true);
				else
					menuBar.setVisible(false);
						
			}
		});
		toolBar_1.add(tglbtnDanamodels);

		doc = textPane.getStyledDocument();

		defineTextAttributres();
		mnItmActionListener=new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JMenuItem mnDanaModelItm=(JMenuItem)arg0.getSource();
				JSONObject jo = sendReqestAndgetJsonResponse(mnDanaModelItm.getName());
				// Create simple option pane
				//JOptionPaneMultiInput mop=new JOptionPaneMultiInput(jo,mnDanaModelItm.getName());
				// Create tabbed pane
				ModelDataFrame mainFrame	= new ModelDataFrame(Frame.getFrames()[0],jo,mnDanaModelItm.getName());
				mainFrame.setVisible( true );
			}
		};

	}
	private void addJsonToolbar(JSONObject jo,JToolBar toolBar)
	{
		JSONArray ja=jo.names();

		//JPopupMenu popupMenu = mnu.getPopupMenu();
		//int clmnCount=ja.length()/20+1;
		//int rowCount=(ja.length()<20) ? ja.length() : ja.length()/clmnCount+1;
		//popupMenu.setLayout(new GridLayout(rowCount, clmnCount));
		for (int i=0;i<ja.length();i++)
		{
			try {
				Object obj=jo.get(ja.getString(i));
				int j=0;
				int s=toolBar.getComponentCount(); 
				if (obj instanceof JSONObject)
				{
					JButton mnDanaModel = new JButton(ja.getString(i).substring(ja.getString(i).lastIndexOf('.')+1));               
					mnDanaModel.setName(ja.getString(i));
					while (j<s && ((JButton)toolBar.getComponentAtIndex(j)).getText().compareTo(mnDanaModel.getText())<0)
						j++;
					toolBar.add(mnDanaModel,j);
					//addJsonMenu((JSONObject)(obj),mnDanaModel);					
				}
				else
				{
					//					JMenuItem mnDanaModelItm = new JMenuItem(ja.getString(i));
					//					//TODO text for types must contains simple name
					//					mnDanaModelItm.setName(mnu.getName().concat(".").concat(ja.getString(i)));
					//					while (j<s && ((JMenuItem)mnu.getMenuComponent(j)).getText().compareTo(mnDanaModelItm.getText())<0)
					//						j++;
					//					mnu.add(mnDanaModelItm,j);
					//					mnDanaModelItm.setToolTipText(createHtmlToolTip(sendReqestAndgetJsonResponse(mnDanaModelItm.getName())));
					//					mnDanaModelItm.addActionListener(mnItmActionListener);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	private void addJsonMenu(JSONObject jo,Component comp)
	{
		JSONArray ja=jo.names();
		for (int i=0;i<ja.length();i++)
		{
			try {
				Object obj=jo.get(ja.getString(i));
				if (comp instanceof JMenuBar)
				{
					JMenuBar mnuB=(JMenuBar) comp;
					if (obj instanceof JSONObject)
					{
						JMenu mnDanaModel = new JMenu(ja.getString(i).substring(ja.getString(i).lastIndexOf('.')+1));               
						mnDanaModel.setName(ja.getString(i));
						int j=0;
						int s=mnuB.getMenuCount(); 
						while (j<s && ((JMenu)mnuB.getMenu(j)).getText().compareTo(mnDanaModel.getText())<0)
							j++;
						mnuB.add(mnDanaModel,j);
						addJsonMenu((JSONObject)(obj),mnDanaModel);					
					}
				}
				if (comp instanceof JMenu)
				{
					JMenu mnu=(JMenu)comp;
					JPopupMenu popupMenu = mnu.getPopupMenu();
					int clmnCount=ja.length()/20+1;
					int rowCount=(ja.length()<20) ? ja.length() : ja.length()/clmnCount+1;
					popupMenu.setLayout(new GridLayout(rowCount, clmnCount));
					int j=0;
					int s=mnu.getItemCount(); 
					if (obj instanceof JSONObject)
					{
						JMenu mnDanaModel = new JMenu(ja.getString(i).substring(ja.getString(i).lastIndexOf('.')+1));               
						mnDanaModel.setName(ja.getString(i));
						while (j<s && ((JMenu)mnu.getMenuComponent(j)).getText().compareTo(mnDanaModel.getText())<0)
							j++;
						mnu.add(mnDanaModel,j);
						addJsonMenu((JSONObject)(obj),mnDanaModel);					
					}
					else
					{
						JMenuItem mnDanaModelItm = new JMenuItem(ja.getString(i));
						//TODO text for types must contains simple name
						mnDanaModelItm.setName(mnu.getName().concat(".").concat(ja.getString(i)));
						while (j<s && ((JMenuItem)mnu.getMenuComponent(j)).getText().compareTo(mnDanaModelItm.getText())<0)
							j++;
						mnu.add(mnDanaModelItm,j);
						mnDanaModelItm.setToolTipText(createHtmlToolTip(sendReqestAndgetJsonResponse(mnDanaModelItm.getName())));
						mnDanaModelItm.addActionListener(mnItmActionListener);
					}
				} 
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	private Socket danaSocket;
	private Integer danaPort=2001;
	private PrintWriter out;
	private BufferedReader in;
	private JTextPane textPane;
	private StyledDocument doc;
	private SimpleAttributeSet attribkeyWord;
	private SimpleAttributeSet attribError;
	private SimpleAttributeSet attribSucc;
	private SimpleAttributeSet attribAlert;
	private SimpleAttributeSet attribNormal;
	private String lastLine;
	private ActionListener mnItmActionListener;
	private JToolBar toolBar_1;
	private JToggleButton tglbtnDanamodels;
	private JMenuBar menuBar;
	private JMenu mnTest;
	private JSONObject jsonMenuStructure=null;
	private void defineTextAttributres()
	{
		//  Define a keyword attribute
		attribkeyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(attribkeyWord, Color.RED);
		StyleConstants.setBackground(attribkeyWord, Color.YELLOW);
		StyleConstants.setBold(attribkeyWord, true);

		//  Define a error attribute
		attribError = new SimpleAttributeSet();
		StyleConstants.setForeground(attribError, Color.RED);

		//  Define a success attribute
		attribSucc = new SimpleAttributeSet();
		StyleConstants.setForeground(attribSucc, Color.GREEN);

		//  Define a success attribute
		attribAlert = new SimpleAttributeSet();
		StyleConstants.setForeground(attribAlert, Color.ORANGE);

		//  Define a normal attribute
		attribNormal = new SimpleAttributeSet();
		StyleConstants.setForeground(attribNormal, Color.BLACK);
	}
	//print text
	private void printout(String txt,SimpleAttributeSet attrib)
	{
		try
		{
			doc.insertString(doc.getLength(), txt, attrib );
		}
		catch(Exception e) 
		{ 
			e.printStackTrace();
		}
	}
	private void createDanaModelsMenu()
	{
		String strResponce="";
		try 
		{
			danaSocket = new Socket("localhost" , danaPort);
			out = new PrintWriter(danaSocket.getOutputStream(), true);
			in = new BufferedReader( new InputStreamReader(danaSocket.getInputStream()));
			printout("Successfully connect to server\n",attribSucc);
			char[] chBuf=new char[MaxBufferLength];
			out.println("DanaModels");
			strResponce=String.valueOf(chBuf, 0, in.read(chBuf));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			printout("Failed to establish server connection\n",attribError);
			return;
		}
		try {
			jsonMenuStructure=new JSONObject(strResponce);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//addJsonToolbar(jo,toolBar_1);
	}
	private String createHtmlToolTip(JSONObject jsonArgs)
	{
		Iterator<?> keys = jsonArgs.keys();
		String htmlToolTip="<html>";
		while( keys.hasNext() ){
			String key = (String)keys.next();
			try {
				Object val=jsonArgs.get(key);
				htmlToolTip+="<b>"+key+"</b> "+val.toString()+"<br/>";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return htmlToolTip;
	}
	private JSONObject sendReqestAndgetJsonResponse(String request)
	{
		String strResponce="";
		char[] chBuf=new char[MaxBufferLength];
		int rSize=0;
		if (out!=null && danaSocket!=null && !danaSocket.isClosed() && !danaSocket.isOutputShutdown())
		{
			out.println(request);
			try {
				rSize=in.read(chBuf);
				strResponce=String.valueOf(chBuf, 0, rSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONObject jsonResponse = null;
		try {
			jsonResponse=new JSONObject(strResponce);
			// Create simple option pane
			//JOptionPaneMultiInput mop=new JOptionPaneMultiInput(jo,mnDanaModelItm.getName());
			// Create tabbed pane
		} catch (JSONException e) {
			e.printStackTrace();
			printout("\nInValid server response! response length=".concat(String.valueOf(rSize)).concat(" "),attribError);
			if (rSize==1)
				printout("Please try again.",attribNormal);
			else
				printout(strResponce,attribError);
		}
		return jsonResponse;
	}
}
