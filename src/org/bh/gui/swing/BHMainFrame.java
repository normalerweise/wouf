package org.bh.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import org.bh.platform.IPlatformListener;
import org.bh.platform.PlatformEvent;
import org.bh.platform.Services;
import org.bh.platform.PlatformEvent.Type;

/**
 * Main Frame for Business Horizon Application.
 * 
 * <p>
 * This <code>JFrame</code> provides the main frame for Business Horizon
 * Application.
 * 
 * @author Tietze.Patrick
 * @author Thiele.Klaus
 * @author Schmalzhaf.Alexander
 * 
 * @version 0.1.1, 2009/12/16
 * @version 0.2, 2009/12/22
 * 
 */
public class BHMainFrame extends JFrame implements IPlatformListener {

	/**
	 * Standard Bar height.
	 */
	public static final int STANDARDBARHEIGHT = 40;

	/**
	 * Tree Bar height.
	 */
	public static final int TREEBARWIDTH = 200;

	/**
	 * main panel.
	 */
	private JPanel desktop;

	/**
	 * Menu Bar for application
	 */
	private BHMenuBar menuBar;

	/**
	 * ToolBar for desktop.
	 */
	private BHToolBar toolBar;

	/**
	 * Tree for File contents (placed on a ScrollPane)
	 */
	private JScrollPane BHTreeScroller;
	private BHTree BHTree;

	/**
	 * Status Bar.
	 */
	private BHStatusBar statusBar;

	/**
	 * TODO: Javadoc
	 */
	private BHContent content;

	/**
	 * Horizontal Split pane.
	 */
	private JSplitPane paneH;

	/**
	 * Vertical Split pane.
	 */
	private JSplitPane paneV;

	/**
	 * Open / Save dialog
	 */
	private BHFileChooser chooser;

	/**
	 * Standard constructor for <code>BHMainFrame</code>.
	 * 
	 * @param title
	 *            title to be set for the <code>BHMainFrame</code>.
	 */
	public BHMainFrame(String title) {
		super(title);
		this.setProperties();

		// Build MenuBar
		menuBar = new BHMenuBar();
		this.setJMenuBar(menuBar);
		
		// build GUI components
		desktop = new JPanel();
		desktop.setLayout(new BorderLayout());

		toolBar = new BHToolBar(getWidth(), STANDARDBARHEIGHT);
		// toolBar.setBounds(0, 0, screenSize.width, standardBarHeight);

		BHTree = new BHTree();
		BHTreeScroller = new JScrollPane(BHTree);

		// treeBar.setBounds(0, standardBarHeight, treeBarWidth,
		// screenSize.height-standardBarHeight);
		// treeBar.setBounds(0,200,200,400);
		
		statusBar = Services.getBHstatusBar();
		content = new BHContent();

		// Create the horizontal split pane and put the treeBar and the content
		// in it.
		paneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, BHTreeScroller,
				content);
		paneH.setOneTouchExpandable(true);
		paneH.setDividerLocation(TREEBARWIDTH);

		// stop moving the divider
		// pane.setEnabled(false);

		desktop.add(toolBar, BorderLayout.PAGE_START);
		desktop.add(paneH, BorderLayout.CENTER);
		desktop.add(statusBar, BorderLayout.PAGE_END);
		// desktop.add(content, BorderLayout.CENTER);

		this.setContentPane(desktop);

		chooser = new BHFileChooser();

		// work around a Java-or-whatever bug which causes the main window to
		// hide behind Eclipse TODO Remove
		//this.setAlwaysOnTop(true);
		//this.setAlwaysOnTop(false);

		Services.firePlatformEvent(new PlatformEvent(this,
				Type.PLATFORM_LOADING_COMPLETED));
	}

	/**
	 * Sets initial properties on <code>BHMainFrame</code>.
	 */
	private synchronized void setProperties() {
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setSize(1024, 768);
		// EXIT is like app suicide
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		Services.addPlatformListener(this);
		this.setIconImage(new ImageIcon("/org/bh/images/bh-logo.jpg").getImage()); //TODO Test on windows
		
	}

	public void addContentForms(Component content) {
		JScrollPane formsScrollPane = new JScrollPane(content);
		paneH.setRightComponent(formsScrollPane);

	}

	public void addContentFormsAndChart(Component forms, Component chart) {
		JSplitPane paneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, forms,
				chart);

		paneV.setOneTouchExpandable(true);

		paneH.setRightComponent(paneV);
	}

	public BHFileChooser getChooser() {
		return chooser;
	}

	public BHTree getBHTree() {
		return BHTree;
	}

	@Override
	public void platformEvent(PlatformEvent e) {
		if (e.getEventType() == Type.PLATFORM_LOADING_COMPLETED) {
			this.setVisible(true);
		}
	}
}
