import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.Canvas;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import javax.swing.JProgressBar;

public class WindowBooking {

	public JFrame framebooking;
	private JTable table;
	private JPanel panel2;
	private JPanel panel2_1;
	private JPanel panel3;
	private JPanel panel3_1;
	private JPanel panel5;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private JComboBox comboBox_2_1;
	private JComboBox comboBox_3;
	private JComboBox comboBox_4;
	private JComboBox comboBox_5;
	private JComboBox comboBox_6;
	private JComboBox comboBox_7;
	private JCheckBox CheckBox;
	private JRadioButton radio1;
	private JRadioButton radio2;
	private JRadioButton radio3;
	private JRadioButton radio4;
	private JRadioButton radio5;
	private JRadioButton radio6;
	private JRadioButton radio7;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private String type;
	private String[] ticketArray = new String[5];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowBooking window = new WindowBooking();
					window.framebooking.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WindowBooking() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JSONArray arrayofstation = JSONUtils.getJSONArrayFromFile("/station.json");
		
		
		framebooking = new JFrame();
		framebooking.getContentPane().setForeground(Color.RED);
		framebooking.setBounds(100, 100, 1009, 605);
		framebooking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framebooking.getContentPane().setLayout(null);
		framebooking.setVisible(true);
		
		
		JLabel lblNewLabel = new JLabel("HSR \u4E00\u822C\u8A02\u7968\u7CFB\u7D71");
		lblNewLabel.setForeground(new Color(25, 25, 112));
		lblNewLabel.setFont(new Font("標楷體", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel.setBounds(44, 10, 288, 36);
		framebooking.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(271, 74, 580, 36);
		framebooking.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("\u8D77\u7A0B\u7AD9");
		lblNewLabel_2.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(23, 0, 60, 36);
		panel.add(lblNewLabel_2);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setBounds(93, 7, 80, 25);
		panel.add(comboBox);
		for (int n = 0; n < arrayofstation.length(); n++) {
			JSONObject stationName = arrayofstation.getJSONObject(n).getJSONObject("StationName");
			String CHname = stationName.getString("Zh_tw");
			String ENname = stationName.getString("En");
			comboBox.addItem(ENname);
			
		}
		
		comboBox.setSelectedItem("請選擇...");
		
		JLabel lblNewLabel_2_1 = new JLabel("\u5230\u9054\u7AD9");
		lblNewLabel_2_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_1.setBounds(183, 0, 60, 36);
		panel.add(lblNewLabel_2_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setEditable(true);
		comboBox_1.setBounds(253, 7, 80, 25);
		panel.add(comboBox_1);
		for (int n = 0; n < arrayofstation.length(); n++) {
			JSONObject stationName = arrayofstation.getJSONObject(n).getJSONObject("StationName");
			String CHname = stationName.getString("Zh_tw");
			String ENname = stationName.getString("En");
			comboBox_1.addItem(ENname);
		}
		
		comboBox_1.setSelectedItem("請選擇...");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(271, 120, 580, 36);
		framebooking.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		radio1 = new JRadioButton("\u6A19\u6E96\u8ECA\u5EC2");//上面有private JRadioButton radio1,2,3...;
		radio1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio1.isSelected()) {
					radio2.setSelected(false);
				}
			}
		});
		radio1.setHorizontalAlignment(SwingConstants.LEFT);
		radio1.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio1.setBounds(23, 6, 105, 23);
		radio1.setSelected(true);
		panel_1.add(radio1);
		
		radio2 = new JRadioButton("\u5546\u52D9\u8ECA\u5EC2");
		radio2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio2.isSelected()) {
					radio1.setSelected(false);
				}
			}
		});
		radio2.setHorizontalAlignment(SwingConstants.LEFT);
		radio2.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio2.setBounds(130, 6, 105, 23);
		panel_1.add(radio2);
		
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBounds(271, 166, 580, 36);
		framebooking.getContentPane().add(panel_1_1);
		panel_1_1.setLayout(null);
		
		radio3 = new JRadioButton("\u7121");
		radio3.setDisabledIcon(null);
		radio3.setSelected(true);
		radio3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio3.isSelected()) {
					radio4.setSelected(false);
					radio5.setSelected(false);
				}
			}
		});
		radio3.setHorizontalAlignment(SwingConstants.LEFT);
		radio3.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio3.setBounds(23, 6, 54, 23);
		panel_1_1.add(radio3);
		
		radio4 = new JRadioButton("\u9760\u7A97\u512A\u5148");
		radio4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio4.isSelected()) {
					radio3.setSelected(false);
					radio5.setSelected(false);
				}
			}
		});
		radio4.setHorizontalAlignment(SwingConstants.LEFT);
		radio4.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio4.setBounds(79, 6, 105, 23);
		panel_1_1.add(radio4);
		
		radio5 = new JRadioButton("\u8D70\u9053\u512A\u5148");
		radio5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio5.isSelected()) {
					radio4.setSelected(false);
					radio3.setSelected(false);
				}
			}
		});
		radio5.setHorizontalAlignment(SwingConstants.LEFT);
		radio5.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio5.setBounds(186, 6, 105, 23);
		panel_1_1.add(radio5);
		
		JPanel panel_1_2 = new JPanel();
		panel_1_2.setBounds(271, 212, 580, 36);
		framebooking.getContentPane().add(panel_1_2);
		panel_1_2.setLayout(null);
		
		radio6 = new JRadioButton("\u4F9D\u6642\u9593\u641C\u5C0B\u5408\u9069\u8ECA\u6B21");
		radio6.setSelected(true);
		radio6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio6.isSelected()) {
					radio7.setSelected(false);
					panel2.setVisible(true);
					panel3.setVisible(false);
					if (CheckBox.isSelected()) {
						panel2_1.setVisible(true);
						panel3_1.setVisible(false);
					}
				}
			}
		});
		radio6.setHorizontalAlignment(SwingConstants.LEFT);
		radio6.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio6.setBounds(23, 6, 163, 23);
		panel_1_2.add(radio6);
		
		radio7 = new JRadioButton("\u76F4\u63A5\u8F38\u5165\u8ECA\u6B21\u865F\u78BC");
		radio7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radio7.isSelected()) {
					radio6.setSelected(false);
					panel3.setVisible(true);
					panel2.setVisible(false);
					if (CheckBox.isSelected()) {
						panel3_1.setVisible(true);
						panel2_1.setVisible(false);
					}
				}
			}
		});
		radio7.setHorizontalAlignment(SwingConstants.LEFT);
		radio7.setFont(new Font("新細明體", Font.PLAIN, 15));
		radio7.setBounds(188, 6, 163, 23);
		panel_1_2.add(radio7);
		
		JPanel panel_1_3 = new JPanel();
		panel_1_3.setBounds(271, 258, 580, 36);
		framebooking.getContentPane().add(panel_1_3);
		panel_1_3.setLayout(null);
		
		JLabel lblNewLabel_2_2 = new JLabel("\u53BB\u7A0B");
		lblNewLabel_2_2.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_2.setBounds(23, 0, 40, 36);
		panel_1_3.add(lblNewLabel_2_2);
		
		textField = new JTextField();
		textField.setBounds(73, 5, 96, 26);
		panel_1_3.add(textField);
		textField.setColumns(10);
		
		panel2 = new JPanel();
		panel2.setBounds(179, 0, 175, 36);
		panel_1_3.add(panel2);
		panel2.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("\u7D04");
		lblNewLabel_3.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(10, 0, 18, 36);
		panel2.add(lblNewLabel_3);
		
		DecimalFormat df = new DecimalFormat("00");
		comboBox_2 = new JComboBox();
		comboBox_2.setEditable(true);
		comboBox_2.setBounds(38, 7, 80, 25);
		panel2.add(comboBox_2);
		comboBox_2.setSelectedItem("請選擇...");
		for (int t = 0; t < 24 ; t++) {
			for (int d = 0; d<2;d++) {
				comboBox_2.addItem(df.format(t)+":"+df.format(d*30));
			}
		}
		
		JLabel lblNewLabel_3_1 = new JLabel("\u51FA\u767C");
		lblNewLabel_3_1.setBounds(122, 0, 43, 36);
		panel2.add(lblNewLabel_3_1);
		lblNewLabel_3_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		
		CheckBox = new JCheckBox("\u8A02\u8CFC\u56DE\u7A0B");
		CheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CheckBox.isSelected()) {
					panel5.setVisible(true);
					if (radio6.isSelected()) {
						panel2_1.setVisible(true);
						panel3_1.setVisible(false);
					}
					else if(radio7.isSelected()) {
						panel3_1.setVisible(true);
						panel2_1.setVisible(false);
					}
				}
				else {
					panel5.setVisible(false);
				}
			}
		});
		CheckBox.setFont(new Font("新細明體", Font.PLAIN, 18));
		CheckBox.setBounds(360, 8, 107, 23);
		panel_1_3.add(CheckBox);
		
		panel3 = new JPanel();
		panel3.setBounds(179, 0, 175, 36);
		panel_1_3.add(panel3);
		panel3.setLayout(null);
		panel3.setVisible(false);
		
		JLabel lblNewLabel_3_2 = new JLabel("\u8ECA\u6B21\u865F\u78BC");
		lblNewLabel_3_2.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_3_2.setBounds(10, 0, 74, 36);
		panel3.add(lblNewLabel_3_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(92, 5, 61, 26);
		panel3.add(textField_1);
		
		JPanel panel_1_4 = new JPanel();
		panel_1_4.setBounds(271, 340, 580, 79);
		framebooking.getContentPane().add(panel_1_4);
		panel_1_4.setLayout(null);
		
		JLabel lblNewLabel_2_3 = new JLabel("\u5168\u7968");
		lblNewLabel_2_3.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_3.setBounds(23, 0, 37, 36);
		panel_1_4.add(lblNewLabel_2_3);
		
		comboBox_3 = new JComboBox();
		comboBox_3.setEditable(true);
		comboBox_3.setBounds(70, 7, 37, 25);
		panel_1_4.add(comboBox_3);
		
		for (int i = 0; i < 11; i++) {
			comboBox_3.addItem(i);
		}
		
		
		JLabel lblNewLabel_2_3_1 = new JLabel("\u5B69\u7AE5\u7968(6-11\u6B72)");
		lblNewLabel_2_3_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_3_1.setBounds(117, 0, 117, 36);
		panel_1_4.add(lblNewLabel_2_3_1);
		
		comboBox_4 = new JComboBox();
		comboBox_4.setEditable(true);
		comboBox_4.setBounds(244, 7, 37, 25);
		panel_1_4.add(comboBox_4);
		
		for (int i = 0; i < 11; i++) {
			comboBox_4.addItem(i);
		}
		
		JLabel lblNewLabel_2_3_2 = new JLabel("\u611B\u5FC3\u7968");
		lblNewLabel_2_3_2.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_3_2.setBounds(291, 0, 55, 36);
		panel_1_4.add(lblNewLabel_2_3_2);
		
		comboBox_5 = new JComboBox();
		comboBox_5.setEditable(true);
		comboBox_5.setBounds(356, 7, 37, 25);
		panel_1_4.add(comboBox_5);
		
		for (int i = 0; i < 11; i++) {
			comboBox_5.addItem(i);
		}
		
		JLabel lblNewLabel_2_3_3 = new JLabel("\u656C\u8001\u7968(65\u6B72\u4EE5\u4E0A)");
		lblNewLabel_2_3_3.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_3_3.setBounds(21, 42, 139, 36);
		panel_1_4.add(lblNewLabel_2_3_3);
		
		comboBox_6 = new JComboBox();
		comboBox_6.setEditable(true);
		comboBox_6.setBounds(170, 49, 37, 25);
		panel_1_4.add(comboBox_6);
		
		for (int i = 0; i < 11; i++) {
			comboBox_6.addItem(i);
		}
		
		JLabel lblNewLabel_2_3_4 = new JLabel("\u5927\u5B78\u751F\u512A\u60E0\u7968");
		lblNewLabel_2_3_4.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_3_4.setBounds(217, 42, 110, 36);
		panel_1_4.add(lblNewLabel_2_3_4);
		
		comboBox_7 = new JComboBox();
		comboBox_7.setEditable(true);
		comboBox_7.setBounds(337, 49, 37, 25);
		panel_1_4.add(comboBox_7);
		
		for (int i = 0; i < 11; i++) {
			comboBox_7.addItem(i);
		}
		
		JLabel lblNewLabel_1 = new JLabel("\u8D77\u8A16\u7AD9");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(44, 74, 223, 36);
		framebooking.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("\u8ECA\u5EC2\u7A2E\u985E");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(44, 120, 223, 36);
		framebooking.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("\u5EA7\u4F4D\u559C\u597D");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1_2.setBounds(44, 166, 223, 36);
		framebooking.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("\u8A02\u4F4D\u65B9\u5F0F");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1_3.setBounds(44, 212, 223, 36);
		framebooking.getContentPane().add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("\u6642\u9593");
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1_4.setBounds(44, 258, 223, 72);
		framebooking.getContentPane().add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("\u7968\u6578");
		lblNewLabel_1_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_5.setFont(new Font("標楷體", Font.PLAIN, 20));
		lblNewLabel_1_5.setBounds(44, 340, 223, 79);
		framebooking.getContentPane().add(lblNewLabel_1_5);
		
		panel5 = new JPanel();
		panel5.setLayout(null);
		panel5.setBounds(271, 294, 580, 36);
		framebooking.getContentPane().add(panel5);
		
		JLabel lblNewLabel_2_2_1 = new JLabel("\u53BB\u7A0B");
		lblNewLabel_2_2_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_2_2_1.setBounds(23, 0, 40, 36);
		panel5.add(lblNewLabel_2_2_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(73, 5, 96, 26);
		panel5.add(textField_2);
		
		panel2_1 = new JPanel();
		panel2_1.setLayout(null);
		panel2_1.setBounds(179, 0, 175, 36);
		panel5.add(panel2_1);
		
		JLabel lblNewLabel_3_3 = new JLabel("\u7D04");
		lblNewLabel_3_3.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_3_3.setBounds(10, 0, 18, 36);
		panel2_1.add(lblNewLabel_3_3);
		
		comboBox_2_1 = new JComboBox();
		comboBox_2_1.setEditable(true);
		comboBox_2_1.setBounds(38, 7, 80, 25);
		panel2_1.add(comboBox_2_1);
		comboBox_2_1.setSelectedItem("請選擇...");
		for (int t = 0; t < 24 ; t++) {
			for (int d = 0; d<2;d++) {
				comboBox_2_1.addItem(df.format(t)+":"+df.format(d*30));
			}
		}
		
		JLabel lblNewLabel_3_1_1 = new JLabel("\u51FA\u767C");
		lblNewLabel_3_1_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		lblNewLabel_3_1_1.setBounds(122, 0, 43, 36);
		panel2_1.add(lblNewLabel_3_1_1);
		
		panel3_1 = new JPanel();
		panel3_1.setBounds(179, 0, 175, 36);
		panel5.add(panel3_1);
		panel3_1.setLayout(null);
		
		JLabel lblNewLabel_3_2_1 = new JLabel("\u8ECA\u6B21\u865F\u78BC");
		lblNewLabel_3_2_1.setBounds(10, 0, 74, 36);
		panel3_1.add(lblNewLabel_3_2_1);
		lblNewLabel_3_2_1.setFont(new Font("新細明體", Font.PLAIN, 18));
		
		textField_3 = new JTextField();
		textField_3.setBounds(92, 5, 61, 26);
		panel3_1.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewButton = new JButton("\u958B\u59CB\u67E5\u8A62");
		btnNewButton.setBackground(new Color(230,230,230));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				ticketArray[0] = comboBox_3.getSelectedItem().toString();
				ticketArray[1] = comboBox_4.getSelectedItem().toString();
				ticketArray[2] = comboBox_5.getSelectedItem().toString();
				ticketArray[3] = comboBox_6.getSelectedItem().toString();
				ticketArray[4] = comboBox_7.getSelectedItem().toString();
				
				if (radio1.isSelected()) {
					type = radio1.getText();
				}
				else {
					type = radio2.getText();
				}
				if (radio6.isSelected()) {
					if (CheckBox.isSelected()) {
						
						StationtrainBack b = new StationtrainBack();				
						b.BackTime(textField.getText(), comboBox_2.getSelectedItem().toString() ,comboBox.getSelectedItem().toString() , 
								comboBox_1.getSelectedItem().toString(),type,ticketArray,
								textField_2.getText(), comboBox_2_1.getSelectedItem().toString());
					}//依時間 又要回程
					else {
						stationtrainNo s = new  stationtrainNo();
						s.inputTime
								(textField.getText(), comboBox_2.getSelectedItem().toString() ,comboBox.getSelectedItem().toString() , 
								comboBox_1.getSelectedItem().toString(),type,ticketArray);
					}//依時間 單程
				}
				else {
					if (CheckBox.isSelected()) {
						StationtrainBack b = new StationtrainBack();
						b.BackTrain(textField.getText(), comboBox_2.getSelectedItem().toString() ,comboBox.getSelectedItem().toString() , 
								comboBox_1.getSelectedItem().toString(),type,ticketArray,
								textField_2.getText(), textField_3.getText());
					}//依車次 又要回程
					else {
						stationtrainNo s = new  stationtrainNo();
						s.inputTrainNo(textField.getText(), textField_1.getText() ,comboBox.getSelectedItem().toString() , 
								comboBox_1.getSelectedItem().toString(),type,ticketArray);
					}//依車次 單程
					
				}
				
			}
		});
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton.setBounds(714, 452, 137, 44);
		framebooking.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u53D6\u6D88");
		btnNewButton_1.setBackground(new Color(230,230,230));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				framebooking.setVisible(false);
			}
		});
		btnNewButton_1.setForeground(Color.DARK_GRAY);
		btnNewButton_1.setFont(new Font("新細明體", Font.PLAIN, 20));
		btnNewButton_1.setBounds(44, 452, 137, 44);
		framebooking.getContentPane().add(btnNewButton_1);
		panel5.setVisible(false);
		
		
	}
}
