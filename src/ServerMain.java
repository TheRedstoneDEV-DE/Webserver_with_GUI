

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ServerMain {
	public static JLabel label = new JLabel("Webserever is stopped.");

	public static void main(String[] args) {
		JFrame frame = new JFrame("Webserver start window");
		JButton buttonon = new JButton("Start Server!");
		JButton buttonoff = new JButton("Stop Server!");
		JButton buttonclose = new JButton("Close");
		JButton Credits = new JButton("View Credits");
		JButton Coff = new JButton("Hide Credits");
		JLabel	Creditsl = new JLabel("--comming soon--");
		JTextField Link = new JTextField("--comming soon--");
		label.setVisible(true);
		label.setSize(300, 30);
		Coff.setSize(200, 60);
		Coff.setVisible(false);
		Link.setVisible(false);
		Link.setSize(240,402);
		Creditsl.setSize(194,293);
		Creditsl.setVisible(false);
		buttonoff.setVisible(true);
		buttonclose.setVisible(true);
		buttonoff.setSize(200, 60);
		buttonclose.setSize(200, 60);
		buttonon.setVisible(true);
		buttonon.setSize(200, 30);
		Credits.setVisible(true);
		Credits.setSize(200, 30);
		frame.setLayout(new FlowLayout());
		frame.add(buttonon);
		frame.add(buttonoff);
		frame.add(label);
		frame.add(Credits);
		frame.add(Coff);
		frame.add(buttonclose);
		frame.add(Creditsl);
		frame.add(Link);
		frame.setSize(241, 134);
		// Wir lassen unseren Frame anzeigen
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		buttonon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Webserver.start();
				label.setText("Web server is starting");
			}

		});
		buttonoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Webserver.stop();
			}
		});
		buttonclose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		Credits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setSize(240,410);
				Credits.setVisible(false);
				Coff.setVisible(true);
				Creditsl.setVisible(true);
				Link.setVisible(true);
			}
		});
		Coff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Coff.setVisible(false);
				Credits.setVisible(true);
				frame.setSize(241, 134);
				Creditsl.setVisible(false);
				Link.setVisible(false);
			}
		});
	}
}
