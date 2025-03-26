import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class windowInputUID {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					windowInputUID window = new windowInputUID();
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
	public windowInputUID() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 536, 323);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 50, 522, 50);
		frame.getContentPane().add(panel);
		panel.setBackground(Color.white);
		panel.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("\u8EAB\u5206\u8B49\u5B57\u865F");
		lblNewLabel_3.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(12, 12, 80, 26);
		panel.add(lblNewLabel_3);
		
		textField = new JTextField();
		textField.setBounds(104, 13, 193, 24);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("*");
		lblNewLabel_1_1.setForeground(Color.RED);
		lblNewLabel_1_1.setFont(new Font("新細明體", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(3, 17, 54, 18);
		panel.add(lblNewLabel_1_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 150, 522, 50);
		frame.getContentPane().add(panel_1);
		panel_1.setBackground(Color.white);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_3_1 = new JLabel("\u884C\u52D5\u96FB\u8A71");
		lblNewLabel_3_1.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel_3_1.setBounds(12, 12, 80, 26);
		panel_1.add(lblNewLabel_3_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(104, 13, 193, 24);
		panel_1.add(textField_1);
		
		JLabel lblNewLabel = new JLabel("\u53D6\u7968\u8B58\u5225\u78BC\uFF0A\uFF08\u53D6\u7968\u6642\u8ACB\u4F9D\u7167\u60A8\u8F38\u5165\u4E4B\u8CC7\u8A0A\u63D0\u4F9B\u9069\u7576\u4E4B\u8B49\u660E\u6587\u4EF6\u3002\uFF09");
		lblNewLabel.setFont(new Font("新細明體", Font.BOLD, 16));
		lblNewLabel.setBounds(15, 0, 482, 49);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("*");
		lblNewLabel_1.setFont(new Font("新細明體", Font.PLAIN, 20));
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(3, 16, 54, 18);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u96FB\u8A71");
		lblNewLabel_2.setFont(new Font("新細明體", Font.BOLD, 16));
		lblNewLabel_2.setBounds(15, 99, 482, 49);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("\u5B8C\u6210\u8A02\u4F4D");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String uid = textField.getText();
				//booking 存入      var:code
				SearchOrder s = new SearchOrder();
//				WindowSearchResult w = new WindowSearchResult();
				//w.var(s.search(uid, "code"), uid, "code", s.getTicketType(), s.getTicketCount(), s.getPrice());
				System.out.println(uid);
				
			}
		});
		btnNewButton.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton.setBounds(372, 225, 138, 49);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u53D6\u6D88");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
			}
		});
		btnNewButton_1.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton_1.setBounds(12, 225, 138, 49);
		frame.getContentPane().add(btnNewButton_1);
	}
}
