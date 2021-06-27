import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class windowRefunding {

	private JFrame refundFrame;
	private JTextField textField;
	private JTextField textField1;
	private JTextField textField2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					windowRefunding window = new windowRefunding();
					window.refundFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public windowRefunding() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		refundFrame = new JFrame();
		refundFrame.setBounds(100, 100, 540, 332);
		refundFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		refundFrame.getContentPane().setLayout(null);
		refundFrame.setVisible(true);
		
		JLabel lblNewLabel = new JLabel("UID         :");
		lblNewLabel.setFont(new Font("標楷體", Font.BOLD, 20));
		lblNewLabel.setBounds(83, 76, 155, 39);
		refundFrame.getContentPane().add(lblNewLabel);
		
		JLabel lblTicketCode = new JLabel("Ticket Code :");
		lblTicketCode.setFont(new Font("標楷體", Font.BOLD, 20));
		lblTicketCode.setBounds(83, 127, 155, 39);
		refundFrame.getContentPane().add(lblTicketCode);
		
		JLabel lblNewLabel_2 = new JLabel("\u522A\u6E1B\u4EBA\u6578    :");
		lblNewLabel_2.setFont(new Font("標楷體", Font.BOLD, 20));
		lblNewLabel_2.setBounds(83, 178, 155, 39);
		refundFrame.getContentPane().add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(238, 84, 194, 24);
		refundFrame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField1 = new JTextField();
		textField1.setColumns(10);
		textField1.setBounds(238, 135, 194, 24);
		refundFrame.getContentPane().add(textField1);
		
		textField2 = new JTextField();
		textField2.setColumns(10);
		textField2.setBounds(238, 186, 194, 24);
		refundFrame.getContentPane().add(textField2);
		
		JButton btnNewButton = new JButton("\u9000\u7968");
		btnNewButton.setBackground(new Color(230,230,230));
		btnNewButton.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFrame jframe = new JFrame();
				jframe.setBounds(100, 100, 496, 189);
				jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jframe.getContentPane().setLayout(null);
				jframe.setVisible(true);
				
				JLabel lblNewLabel = new JLabel("New label");
				lblNewLabel.setFont(new Font("新細明體", Font.BOLD, 20));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setBounds(12, 34, 458, 36);
				jframe.getContentPane().add(lblNewLabel);
				
				JButton btnNewButton = new JButton("\u78BA\u8A8D");
				btnNewButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						jframe.setVisible(false);
					}
				});
				btnNewButton.setBounds(191, 82, 95, 27);
				jframe.getContentPane().add(btnNewButton);
				Refunding refund = new Refunding();
				try {
					lblNewLabel.setText(refund.Refund(textField.getText(),textField1.getText(), Integer.parseInt(textField2.getText())));
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(413, 247, 101, 36);
		refundFrame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("HSR \u9000\u7968\u7CFB\u7D71");
		lblNewLabel_1.setForeground(new Color(0, 0, 128));
		lblNewLabel_1.setFont(new Font("標楷體", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel_1.setBounds(12, 12, 333, 52);
		refundFrame.getContentPane().add(lblNewLabel_1);
		
		JButton btnNewButton_1 = new JButton("\u53D6\u6D88");
		btnNewButton_1.setBackground(new Color(230,230,230));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refundFrame.setVisible(false);
			}
		});
		btnNewButton_1.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton_1.setBounds(12, 247, 101, 36);
		refundFrame.getContentPane().add(btnNewButton_1);
	}

}
