import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

public class windowDEMO {

	private JFrame frame;
	private JButton btnNewButton1;
	private JButton btnNewButton2;
	private JButton btnNewButton3;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					windowDEMO window = new windowDEMO();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public windowDEMO() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//frame.setAlwaysOnTop(true);
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 257, 332);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnNewButton1 = new JButton("\u8A02\u7968");
		btnNewButton1.setBackground(new Color(230,230,230));
		btnNewButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WindowBooking a = new WindowBooking();
			}
		});
		btnNewButton1.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton1.setBounds(59, 63, 131, 51);
		frame.getContentPane().add(btnNewButton1);
		
		btnNewButton2 = new JButton("\u67E5\u8A62");
		btnNewButton2.setBackground(new Color(230,230,230));
		btnNewButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Searching s = new Searching();
			}
		});
		btnNewButton2.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton2.setBounds(59, 126, 131, 51);
		frame.getContentPane().add(btnNewButton2);
		
		btnNewButton3 = new JButton("\u9000\u7968");
		btnNewButton3.setBackground(new Color(230,230,230));
		btnNewButton3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				windowRefunding b = new windowRefunding();
			}
		});
		btnNewButton3.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton3.setBounds(59, 189, 131, 51);
		frame.getContentPane().add(btnNewButton3);
		
		lblNewLabel = new JLabel("HSR \u7CFB\u7D71");
		lblNewLabel.setForeground(new Color(25, 25, 112));
		lblNewLabel.setFont(new Font("標楷體", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel.setBounds(12, 12, 180, 39);
		frame.getContentPane().add(lblNewLabel);
		
	}
}
