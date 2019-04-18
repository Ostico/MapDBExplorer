package mapdb.explorer;

import java.awt.EventQueue;

import javax.swing.UIManager;

import mapdb.explorer.swing.MainFrame;

public class MapDbExplorer {

	public static void main(String[] args) throws Exception {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainFrame frame = new MainFrame();
					frame.setTitle("MapDb Explorer");
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
