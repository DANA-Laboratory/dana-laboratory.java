import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelDataFrame extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private		JTabbedPane tabbedPane;
	private		JPanel		pDescriptsaion;
	private		JScrollPane		pParameters;
	private		JScrollPane		pVariables;
	private		JPanel		pEquations;
	private     JSONObject	jsonArgs;

	public ModelDataFrame(Frame owner,JSONObject args,String title)
	{
		super( owner, title, true);
		jsonArgs=args;
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setSize(new Dimension(700, 350));
		setLocationRelativeTo(owner);

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		// Create the tab pages
		createDescriptsaion();
		createParameters();
		createVariables();
		createEquations();

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "Descriptsaion", pDescriptsaion );
		tabbedPane.addTab( "Parameters", pParameters );
		tabbedPane.addTab( "Variables", pVariables );
		tabbedPane.addTab( "Equations", pEquations );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		setResizable(false);
	}

	public void createDescriptsaion()
	{
		//		pDescriptsaion = new JPanel();
		//		pDescriptsaion.setLayout( null );
		//
		//		JLabel label1 = new JLabel( "Username:" );
		//		label1.setBounds( 10, 15, 150, 20 );
		//		pDescriptsaion.add( label1 );
		//
		//		JTextField field = new JTextField();
		//		field.setBounds( 10, 35, 150, 20 );
		//		pDescriptsaion.add( field );
		//
		//		JLabel label2 = new JLabel( "Password:" );
		//		label2.setBounds( 10, 60, 150, 20 );
		//		pDescriptsaion.add( label2 );
		//
		//		JPasswordField fieldPass = new JPasswordField();
		//		fieldPass.setBounds( 10, 80, 150, 20 );
		//		pDescriptsaion.add( fieldPass );
	}

	public void createParameters()
	{
		JPanel pLPane = new JPanel();
		JPanel pRPane = new JPanel();
		//JSplitPane pSPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pLPane,pRPane);
		JPanel pSPane = new JPanel();
		pSPane.setLayout(new BoxLayout(pSPane, BoxLayout.LINE_AXIS));
		pLPane.setAlignmentY(JPanel.TOP_ALIGNMENT);
		pRPane.setAlignmentY(JPanel.TOP_ALIGNMENT);
		pSPane.add(pLPane);
		pSPane.add(pRPane);

		pParameters = new JScrollPane(pSPane);
		Iterator<?> keys = jsonArgs.keys();
		pLPane.setLayout( new GridLayout(jsonArgs.length(),1) );
		pRPane.setLayout( new GridLayout(jsonArgs.length(),1) );
		Dimension size=new Dimension(50,20);
		pLPane.setMaximumSize( new Dimension(100,jsonArgs.length()*20));
		pLPane.setPreferredSize( new Dimension(50,jsonArgs.length()*20));
		pRPane.setMaximumSize( new Dimension(100,jsonArgs.length()*20));
		while( keys.hasNext() ){
			String key = (String)keys.next();
			try {
				JLabel lbl=new JLabel(" ".concat(key));
				lbl.setPreferredSize(size);
				lbl.setBorder(BorderFactory.createLineBorder(Color.black));
				lbl.setOpaque(true);
				lbl.setBackground(Color.WHITE);
				lbl.setForeground(Color.BLACK);
				Object val=jsonArgs.get(key);
				lbl.setToolTipText(key);
				pLPane.add(lbl);
				JTextField tf=new JTextField();
				tf.setPreferredSize(size);
				tf.setBorder(BorderFactory.createLineBorder(Color.black));
				tf.setText(val.toString());
				pRPane.add(tf);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createVariables()
	{
		JPanel pLPane = new JPanel();
		JPanel pRPane = new JPanel();
		//JSplitPane pSPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pLPane,pRPane);
		JPanel pSPane = new JPanel();
		pSPane.setLayout(new BoxLayout(pSPane, BoxLayout.LINE_AXIS));
		pLPane.setAlignmentY(JPanel.TOP_ALIGNMENT);
		pRPane.setAlignmentY(JPanel.TOP_ALIGNMENT);
		pSPane.add(pLPane);
		pSPane.add(pRPane);

		pVariables = new JScrollPane(pSPane);
		JSONArray names,types;
		try {
			names=jsonArgs.getJSONArray("names");
			types=jsonArgs.getJSONArray("types");
			pLPane.setLayout( new GridLayout(names.length(),1) );
			pRPane.setLayout( new GridLayout(names.length(),1) );
			Dimension size=new Dimension(80,20);
			pLPane.setMaximumSize( new Dimension(150,names.length()*20));
			pLPane.setPreferredSize( new Dimension(80,names.length()*20));
			pRPane.setMaximumSize(new Dimension(150,names.length()*20));
			for (int i=0;i<names.length();i++)
			{
				JLabel lbl=new JLabel("   ".concat(names.getString(i)));
				lbl.setPreferredSize(size);
				lbl.setBorder(BorderFactory.createLineBorder(Color.black));
				lbl.setOpaque(true);
				lbl.setBackground(Color.WHITE);
				lbl.setForeground(Color.BLACK);
				lbl.setToolTipText(names.getString(i).concat(" DataType:").concat(types.getString(i)));
				pLPane.add(lbl);
				JTextField tf=new JTextField();
				tf.setPreferredSize(size);
				tf.setBorder(BorderFactory.createLineBorder(Color.black));
				pRPane.add(tf);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createEquations()
	{
		//		pEquations = new JPanel();
		//		pEquations.setLayout( new GridLayout( 3, 2 ) );
		//		pEquations.add( new JLabel( "Field 1:" ) );
		//		pEquations.add( new TextArea() );
		//		pEquations.add( new JLabel( "Field 2:" ) );
		//		pEquations.add( new TextArea() );
		//		pEquations.add( new JLabel( "Field 3:" ) );
		//		pEquations.add( new TextArea() );
	}

}
