import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowSearchResult {

	private JFrame SRframe;
	private String [] data;
	private int row;
	private String uid , code,ticketsType;
	private JTable table;
	private int ticketsCount,price;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					WindowSearchResult window = new WindowSearchResult();
//					window.frame.setVisible(true);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public WindowSearchResult(){
		//initialize();
	}
	public void var(String [] array ,String uid ,String code,String type,int count,int price) {
		this.uid = uid;
		this.code = code;
		System.out.println(this.code);
		System.out.println(array.length);
		data = new String[array.length];
		for (int i = 0; i < data.length ; i ++) {
			data[i] = array[i];
			System.out.println(data[i]);
		}
		ticketsCount = count;
		ticketsType = type;
		this.price = price;
		System.out.println(1);
		initialize();
	}
//	public void getother(String type,String count,String price) {
//		ticketsCount = count;
//		ticketsType = type;
//		this.price = price;
//	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] columns = new String[] {
	            "行程","日期","車次","起程站","到達站","出發時間","到達時間","座位"//定位明細
	        };
		
		String [][]obj = new String[data.length/8][8]; 
		for (int i = 0 ; i < data.length/8 ; i ++) {
			for (int n = 0 ; n < 8 ; n ++) {
				obj[i][n] = data[(i*8)+n];
			}
		}
		
		
		
		SRframe = new JFrame();
		SRframe.setBounds(100, 100, 1018, 375);
		SRframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SRframe.setVisible(true);
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
		lblNewLabel_3.setText(code);
		
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
		lblNewLabel_3_1.setText(uid);
		
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
		
		table = new JTable();
		scrollPane.setViewportView(table);
		DefaultTableModel dm1 = new DefaultTableModel();
        dm1.setColumnIdentifiers(columns);  
        dm1.setDataVector(obj, columns);
        table.setModel(dm1);
	}
}
