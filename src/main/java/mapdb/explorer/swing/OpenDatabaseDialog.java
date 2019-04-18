package mapdb.explorer.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class OpenDatabaseDialog extends JDialog {


	private static final long serialVersionUID = -6774533600260047103L;
	private final JPanel contentPanel = new JPanel();
	private JTextField jarFileName;
	private JTextField dbFileName;
	
	public boolean ok = false;


	/**
	 * Create the dialog.
	 */
	public OpenDatabaseDialog() {
		setModal(true);
		setTitle("Open Database");
		try {
			setIconImage(ImageIO.read( ClassLoader.getSystemResource( "rocket-small.png" ) ));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		setBounds(100, 100, 450, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][]", "[][][][]"));
		{
			JLabel lblNewLabel = new JLabel("Select JAR file containing map classes:");
			contentPanel.add(lblNewLabel, "cell 0 0");
		}
		{
			jarFileName = new JTextField();
			//jarFileName.setText("D:\\nicola\\varie\\MsxBot\\MsxBot\\target\\MsxBot-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
			contentPanel.add(jarFileName, "cell 0 1,growx");
			jarFileName.setColumns(10);
		}
		{
			JButton btnNewButton = new JButton("browse..");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					   JFileChooser fileopen = new JFileChooser();
					   fileopen.setCurrentDirectory(new File("."));
					    FileNameExtensionFilter filter = new FileNameExtensionFilter("jar files", "jar");
					    fileopen.addChoosableFileFilter(filter);

					    int ret = fileopen.showDialog(null, "Open Jar file");

					    if (ret == JFileChooser.APPROVE_OPTION) {
					      File file = fileopen.getSelectedFile();
					      jarFileName.setText(file.toString());
					    }
				}
			});
			contentPanel.add(btnNewButton, "cell 1 1");
		}
		{
			JLabel lblSelectDatabaseFile = new JLabel("Select Database file:");
			contentPanel.add(lblSelectDatabaseFile, "cell 0 2");
		}
		{
			dbFileName = new JTextField();
			//dbFileName.setText("d:\\nicola\\testdb");
			contentPanel.add(dbFileName, "cell 0 3,growx");
			dbFileName.setColumns(10);
		}
		{
			JButton btnNewButton_1 = new JButton("browse..");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					  JFileChooser fileopen = new JFileChooser();
					  fileopen.setCurrentDirectory(new File("."));
					    int ret = fileopen.showDialog(null, "Open database file");

					    if (ret == JFileChooser.APPROVE_OPTION) {
					      File file = fileopen.getSelectedFile();
					      dbFileName.setText(file.toString());
					    }
				}
			});
			contentPanel.add(btnNewButton_1, "cell 1 3");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ok = true;
						OpenDatabaseDialog.this.setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ok = false;
						OpenDatabaseDialog.this.setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	public JTextField getJarFileName() {
		return jarFileName;
	}
	public JTextField getDbFileName() {
		return dbFileName;
	}
}
