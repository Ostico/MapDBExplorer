package mapdb.explorer.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public abstract class GeneratedFrame extends JFrame {


	private static final long serialVersionUID = -5160200742024335456L;
	/**
	 *  class generated with Window Builder, don't put logic in it
	 */
	
	private JPanel contentPane;
	protected JTable table;
	private JSplitPane splitPane;
	private JScrollPane scrollPane_1;
	protected JList<String> list;

	Map<String, TableModel> tables;
	private JMenuBar menuBar;
	private JMenu mnFile;
	protected JMenuItem mntmOpen;

	public GeneratedFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		try {
			setIconImage(ImageIO.read( ClassLoader.getSystemResource( "rocket-small.png" ) ));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openDatabase();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(GeneratedFrame.this,
					    e1.getMessage(),
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnFile.add(mntmOpen);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		
		contentPane.add(splitPane, "cell 0 0,grow");
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);
		
		list = new JList<String>();
		
		
		list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                   changeTable(list.getSelectedValue());
                }
            }
        });
		
		scrollPane_1.setViewportView(list);
	}

	protected abstract void openDatabase() throws Exception;

	protected void changeTable(String selectedValue) {
		table.setModel(tables.get(selectedValue));
		
	}

	
	
}
