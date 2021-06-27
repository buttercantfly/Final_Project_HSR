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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.ImageIcon;

public class windowDEMO {

	private JFrame frame;
	private JFrame SRframe;
	private JButton btnNewButton1;
	private JButton btnNewButton2;
	private JButton btnNewButton3;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private String [] Searchdata;
	private int Searchrow;
	private String Searchuid , Searchcode,ticketsType;
	private JTable Searchtable;
	private int ticketsCount,price;

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
		frame.setBounds(100, 100, 674, 336);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//frame.setUndecorated(true);
		//frame.getContentPane().setBackground(Color.lightGray);
		
		btnNewButton1 = new JButton("\u8A02\u7968");
		btnNewButton1.setBackground(new Color(230,230,230));
		btnNewButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////booking
				WindowBooking a = new WindowBooking();
			}
		});
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(12, 63, 337, 142);
		frame.getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setIcon(new ImageIcon(windowDEMO.class.getResource("/image/\u4E0B\u8F09.png")));
		btnNewButton1.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton1.setBounds(460, 63, 131, 51);
		frame.getContentPane().add(btnNewButton1);
		
		btnNewButton2 = new JButton("\u67E5\u8A62");
		btnNewButton2.setBackground(new Color(230,230,230));
		btnNewButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////searching
				WindowSearching s = new WindowSearching();
				JFrame Sframe = new JFrame();
				Sframe.setBounds(100, 100, 450, 259);
				Sframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Sframe.getContentPane().setLayout(null);
				//Sframe.setVisible(true);
				
				JLabel lblNewLabel = new JLabel("HSR \u67E5\u8A62\u7CFB\u7D71");
				lblNewLabel.setForeground(new Color(25, 25, 112));
				lblNewLabel.setFont(new Font("標楷體", Font.BOLD | Font.ITALIC, 30));
				lblNewLabel.setBounds(12, 12, 257, 48);
				Sframe.getContentPane().add(lblNewLabel);
				
				JLabel lblNewLabel_1 = new JLabel("UID         :");
				lblNewLabel_1.setFont(new Font("標楷體", Font.BOLD, 20));
				lblNewLabel_1.setBounds(49, 72, 152, 31);
				Sframe.getContentPane().add(lblNewLabel_1);
				
				JLabel lblNewLabel_1_1 = new JLabel("Ticket Code :");
				lblNewLabel_1_1.setFont(new Font("標楷體", Font.BOLD, 20));
				lblNewLabel_1_1.setBounds(49, 115, 152, 31);
				Sframe.getContentPane().add(lblNewLabel_1_1);
				
				JTextField txtA = new JTextField();
				txtA.setText("A123456789");
				txtA.setBounds(199, 76, 197, 24);
				Sframe.getContentPane().add(txtA);
				txtA.setColumns(10);
				
				JTextField textField_1 = new JTextField();
				textField_1.setText("000000005");
				textField_1.setColumns(10);
				textField_1.setBounds(199, 115, 197, 24);
				Sframe.getContentPane().add(textField_1);
				
				JButton btnNewButton = new JButton("\u958B\u59CB\u67E5\u8A62");
				btnNewButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						SearchOrder s = new SearchOrder();
						s.search(txtA.getText(), textField_1.getText());
						
						Searchuid = txtA.getText();
						Searchcode = textField_1.getText();
						
						Searchdata = new String[s.getArray().length];
						for (int i = 0; i < Searchdata.length ; i ++) {
							Searchdata[i] = s.getArray()[i];
							System.out.println(Searchdata[i]);
						}
						ticketsCount = s.getTicketCount();
						ticketsType = s.getTicketType();
						price = s.getPrice();
						
						String[] columns = new String[] {
					            "行程","日期","車次","起程站","到達站","出發時間","到達時間","座位"//定位明細
					        };
						
						String [][]obj = new String[Searchdata.length/8][8]; 
						for (int i = 0 ; i < Searchdata.length/8 ; i ++) {
							for (int n = 0 ; n < 8 ; n ++) {
								obj[i][n] = Searchdata[(i*8)+n];
							}
						}		
						SRframe = new JFrame();
						SRframe.setBounds(100, 100, 1018, 375);
						SRframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						//SRframe.setVisible(true);
						SRframe.getContentPane().setLayout(null);
				        
				        
						
						JPanel panel = new JPanel();
						panel.setBounds(13, 198, 977, 56);
						SRframe.getContentPane().add(panel);
						panel.setLayout(null);
						
						JLabel lblNewLabel_1 = new JLabel("New label");
						lblNewLabel_1.setForeground(Color.RED);
						lblNewLabel_1.setFont(new Font("新細明體", Font.PLAIN, 30));
						lblNewLabel_1.setBounds(732, 12, 234, 44);
						panel.add(lblNewLabel_1);
						lblNewLabel_1.setText("總票價 TWD "+price);
						
						JLabel lblNewLabel_4 = new JLabel("\u8ECA\u5EC2 :     \u7968\u6578 : null");
						lblNewLabel_4.setBounds(0, 0, 156, 23);
						panel.add(lblNewLabel_4);
						String temp = "";
						if (ticketsType.contentEquals("standard")) {
							temp = "標準車廂";
						}
						else {
							temp = "商務車廂";
						}
						lblNewLabel_4.setText("車廂 : "+temp+"   "+"票數 : "+ticketsCount);
						
						JButton btnNewButton = new JButton("OK");
						btnNewButton.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								SRframe.setVisible(false);
							}
						});
						btnNewButton.setBounds(857, 291, 135, 46);
						SRframe.getContentPane().add(btnNewButton);
						
						JPanel panel_1 = new JPanel();
						panel_1.setBounds(12, 36, 488, 56);
						SRframe.getContentPane().add(panel_1);
						panel_1.setLayout(null);
						panel_1.setBackground(new Color(230,230,230));
						
						JLabel lblNewLabel_3 = new JLabel("New label");
						lblNewLabel_3.setForeground(Color.RED);
						lblNewLabel_3.setFont(new Font("新細明體", Font.BOLD, 20));
						lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
						lblNewLabel_3.setBounds(186, 12, 274, 32);
						panel_1.add(lblNewLabel_3);
						lblNewLabel_3.setText(Searchcode);
						
						JPanel panel_3 = new JPanel();
						panel_3.setBounds(0, 0, 160, 56);
						panel_1.add(panel_3);
						panel_3.setLayout(null);
						panel_3.setBackground(new Color(220,220,220));
						
						JLabel lblNewLabel_2 = new JLabel("\u8A02\u4F4D\u4EE3\u865F");
						lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
						lblNewLabel_2.setFont(new Font("新細明體", Font.BOLD, 20));
						lblNewLabel_2.setBounds(12, 12, 136, 32);
						panel_3.add(lblNewLabel_2);
						
						JPanel panel_2 = new JPanel();
						panel_2.setBounds(498, 36, 495, 56);
						SRframe.getContentPane().add(panel_2);
						panel_2.setLayout(null);
						panel_2.setBackground(new Color(230,230,230));
						
						JLabel lblNewLabel_3_1 = new JLabel("New label");
						lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.CENTER);
						lblNewLabel_3_1.setForeground(Color.BLACK);
						lblNewLabel_3_1.setFont(new Font("新細明體", Font.BOLD, 20));
						lblNewLabel_3_1.setBounds(202, 12, 274, 32);
						panel_2.add(lblNewLabel_3_1);
						lblNewLabel_3_1.setText(Searchuid);
						
						JPanel panel_3_1 = new JPanel();
						panel_3_1.setBounds(0, 0, 178, 56);
						panel_2.add(panel_3_1);
						panel_3_1.setLayout(null);
						panel_3_1.setBackground(new Color(220,220,220));
						
						JLabel lblNewLabel_2_1 = new JLabel("\u53D6\u7968\u8B58\u5225\u78BC");
						lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
						lblNewLabel_2_1.setFont(new Font("新細明體", Font.BOLD, 20));
						lblNewLabel_2_1.setBounds(12, 12, 154, 32);
						panel_3_1.add(lblNewLabel_2_1);
						
						JScrollPane scrollPane = new JScrollPane();
						scrollPane.setBounds(12, 138, 980, 126);
						SRframe.getContentPane().add(scrollPane);
						
						JTable table = new JTable();
						scrollPane.setViewportView(table);
						DefaultTableModel dm1 = new DefaultTableModel();
				        dm1.setColumnIdentifiers(columns);  
				        dm1.setDataVector(obj, columns);
				        table.setModel(dm1);
					}
				});
				btnNewButton.setBackground(new Color(230,230,230));
				btnNewButton.setFont(new Font("新細明體", Font.PLAIN, 20));
				btnNewButton.setBounds(299, 179, 125, 31);
				Sframe.getContentPane().add(btnNewButton);
				
				JButton btnNewButton_1 = new JButton("\u53D6\u6D88");
				btnNewButton_1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Sframe.setVisible(false);
					}
				});
				btnNewButton_1.setBackground(new Color(230,230,230));
				btnNewButton_1.setFont(new Font("新細明體", Font.PLAIN, 20));
				btnNewButton_1.setBounds(12, 179, 113, 31);
				Sframe.getContentPane().add(btnNewButton_1);
			}
		});
		btnNewButton2.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton2.setBounds(460, 126, 131, 51);
		frame.getContentPane().add(btnNewButton2);
		
		btnNewButton3 = new JButton("\u9000\u7968");
		btnNewButton3.setBackground(new Color(230,230,230));
		btnNewButton3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////refunding
				JFrame refundFrame = new JFrame();
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
				
				JTextField rtextField = new JTextField();
				rtextField.setBounds(238, 84, 194, 24);
				refundFrame.getContentPane().add(rtextField);
				rtextField.setColumns(10);
				
				JTextField rtextField1 = new JTextField();
				rtextField1.setColumns(10);
				rtextField1.setBounds(238, 135, 194, 24);
				refundFrame.getContentPane().add(rtextField1);
				
				JTextField rtextField2 = new JTextField();
				rtextField2.setColumns(10);
				rtextField2.setBounds(238, 186, 194, 24);
				refundFrame.getContentPane().add(rtextField2);
				
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
							lblNewLabel.setText(refund.Refund(rtextField.getText(),rtextField1.getText(), Integer.parseInt(rtextField2.getText())));
							
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
		});
		btnNewButton3.setFont(new Font("標楷體", Font.BOLD, 30));
		btnNewButton3.setBounds(460, 189, 131, 51);
		frame.getContentPane().add(btnNewButton3);
		
		lblNewLabel = new JLabel("HSR \u7CFB\u7D71");
		lblNewLabel.setForeground(new Color(25, 25, 112));
		lblNewLabel.setFont(new Font("標楷體", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel.setBounds(413, 12, 180, 39);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		panel.setBounds(12, 63, 337, 142);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
	}
}
