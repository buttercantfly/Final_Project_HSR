
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yy121
 *
 */
/**
 * @author yy121
 *
 */
public class searchDB {

	/**
	 * @param date     日期形式如0628不用加.csv
	 * @param train    只需要車次編號(如0861)
	 * @param start    只需要站點編號(如0990) 南港
	 * @param end      同上
	 * @param number   欲購票數量
	 * @return 座位的編號,座位的編號,座位的編號,.....
	 * @throws IOException
	 */
	public static String getSeatno(String date, String train, String start, String end, int number) throws IOException {

		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv"));

		String line = "";

		while (true) {
			line = BR.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}

		String[] seatnos = null; // 座位號碼
		ArrayList<List<String>> seats = new ArrayList<List<String>>(); // 所有座位情形的二維ArrayList/array

		boolean in = false;
		int    way = 0;

		for (int st = 0; st < 15; st++) {

			String tmp = BR.readLine();

			if (tmp.contains("Seats")) {
				seatnos = tmp.split(",");
			}

			if (in) {
				seats.add(Arrays.asList(tmp.split(",")));
				way++;
				if (tmp.contains(end)) {
					break;
				}
			}

			if (tmp.contains(start)) {
				seats.add(Arrays.asList(tmp.split(",")));
				way++;
				in = true;
			}
		}

		// 直的是相同座位，橫的是相同站
		// 先以一個位子為單位(大圈for)，直的向下加之後(內圈for)，再進入下一次的迴圈
		
		String seatno = "";
		
		int Tnumber = 0;
		
		// i迴圈跑座位(橫)
		for(int i = 1; i < seatnos.length - 1 ; i++) {
			// T迴圈跑站(直)
			T: for(int j = 0; j < way; j++) {
				if (Tnumber == number) {
					break T;
				}
				if (seats.get(j).get(i).equals("1") ) {
					break T;
				}
				else if(j == way - 1) {
					if(seatno.equals("")) {
						seatno = seatnos[i];
					}
					else {
						seatno = seatno + "," + seatnos[i];
					}
					Tnumber++;
				}
			}
		}
		
		BR.close();
		
		return seatno;
	}

	/**
	 * @param date(日期形式如0628不用加.csv )
	 * @param train(列車號碼)
	 * @param start
	 * @param end
	 * @param number(票數)
	 * @param kind(只能填window        or aisle 但我沒有寫偵錯)
	 * @return 座位的編號,座位的編號,座位的編號,.....(就算只有一張票右邊也會有逗號)
	 * @throws IOException
	 */
	public static String getSeatnoSpecial(String date, String train, String start, String end, String kind)
			throws IOException {
		
		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv"));
// 找到指定train
		String line = "";

		while (true) {
			line = BR.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
//找起始站、終點站
		// 建一個array元素是各個站的string，預設空間十個車站
		String[] seatnos = null; // 座位號碼
		ArrayList<List<String>> seats = new ArrayList<List<String>>(); // 所有座位情形的二維ArrayList/array

		boolean in = false;
		int    way = 0;

		for (int st = 0; st < 15; st++) {

			String tmp = BR.readLine();

			if (tmp.contains("Seats")) {
				seatnos = tmp.split(",");
			}

			if (in) {
				seats.add(Arrays.asList(tmp.split(",")));
				way++;
				if (tmp.contains(end)) {
					break;
				}
			}

			if (tmp.contains(start)) {
				seats.add(Arrays.asList(tmp.split(",")));
				in = true;
				way++;
			}
		}

		// 直的是相同座位，橫的是相同站
		// 先以一個位子為單位(大圈for)，直的向下加之後(內圈for)，再進入下一次的迴圈
		
		String seatno = "";
		
		String currentSeat;
		
		for(int i = 1; i < seatnos.length - 1 ; i++) {
			currentSeat = seatnos[i];
			
			T: for(int j = 0; j < way; j++) {
				System.out.println(seatnos[i]);
				if (((currentSeat.contains("C") || currentSeat.contains("D")) && kind.equals("aisle")) || 
						((currentSeat.contains("A") || currentSeat.contains("E")) && kind.equals("window"))) {
					
				}else {
					break T;
				}
		
				if (seats.get(j).get(i).equals("1") ) {
					break T;
				}
				else if(j == way - 1) {
					seatno = seatnos[i];
				}
			}
		}
		
		BR.close();
		return seatno;
	}

	/**
	 * @param date
	 * @param train
	 * @param (String)discount
	 * @return 回傳ArrayList 格式為:{} (也就是沒票) 或 {折扣, 可買} 或 {折扣, 可買票, 折扣, 可買票}
	 * @throws IOException
	 */
	public static ArrayList<Object> checkEarly(String date, String train, int number) throws IOException {
		
		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv"));
		
		String line = "";

		while (true) {
			line = BR.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
		// 該列次的折價與剩餘票數
		String[] dis = BR.readLine().split(",");
		
		String[] tickets = BR.readLine().split(",");
		
		// 回傳ArrayList 一個折多少 一個能拿幾張
		ArrayList<Object> A = new ArrayList<Object>();
		

		if(dis[0].equals("Seats")) {
			A.add("");
			BR.close();
			return A;
		}
		

		int num = number;

		for (int t = 0; t < dis.length; t++) {
			int remain = Integer.parseInt(tickets[t]);

			if (remain == 0) {
				
			} else if (remain < number && remain > 0) {
				if (t == dis.length - 1) {
					A.add(dis[t]);
					A.add(remain);
					break;
				} else {
					A.add(dis[t]);
					A.add(remain);
					num = num - remain;
				}
			} else {
				A.add(dis[t]);
				A.add(num);
				break;
			}
			
			if (t == dis.length - 1) {
				break;
			}
		}

		BR.close();

		return A;
	}

	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @throws IOException
	 */
	public static void setSeatno(String date, String train, String start, String end, String seatNO)
			throws IOException {

		String ttt = "tabble";

		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
		BufferedWriter BW = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here

		// 找到指定train
		String line = "";
		
		BW.newLine();

		while (true) {
			line = BR.readLine();
			BW.write(line);
			BW.newLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) {
					break;
				}
			}
		}
		
		//座位號碼
		String[] seatsno;
		
		while (true) {
			line = BR.readLine();
			BW.newLine();
			String[] tt = line.split(",");
			
			if (tt[0].contains("Seats")) {
				seatsno = tt;
				BW.write(line);
				break;
			}
			else {
				BW.write(line);
			}
		}
		
		int index = Arrays.asList(seatsno).indexOf(seatNO);
		
		System.out.println(index);
		
		//找出座位表
		
		boolean in = false;
		
		while (true) {
			line = BR.readLine();
			BW.newLine();
			String[] tt = line.split(",");
			
			if (tt[0].contains(start)) {
				in = true;
			}
			
			if(in) {
				tt[index] = "1";
				line = String.join(",", tt);
				BW.write(line);
			}
			
			if (tt[0].contains(end)) {
				break;
			}
		}
		

		// 剩下的再重新write一次
		try {
			while (true) {
				BW.newLine();
				BW.write(BR.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			BR.close();
			BW.close();
		}

		BufferedReader BR2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter BW2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			int fake2 = 1;
			BW2.write(BR2.readLine());
			while (fake2 == 1) {
				BW2.newLine();
				BW2.write(BR2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			BR2.close();
			BW2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();
	}
	

	
	public static void setED(String date, String train, ArrayList<Object> arraylist) throws IOException {
		String ttt = "tabble";

		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv"));
		BufferedWriter BW = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv"));
		
		//找到指定train
		String line = "";

		while (true) {
			line = BR.readLine();
			BW.write(line);
			BW.newLine();
			
			if (line != null) { //防止nullpointer
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
		//扣除該列車的earlydiscount數量
		
		String[] caldiscount = new String[2];
		caldiscount[0] = BR.readLine();
		caldiscount[1] = BR.readLine();
		
		String[] dis = caldiscount[0].split(",");
		String[] tickets = caldiscount[1].split(",");
		
		BW.newLine();
		BW.write(caldiscount[0]);
		
		for(int i = 0; i < dis.length; i++) {
			if(dis[i].equals(arraylist.get(0))) {
				int tmp = Integer.valueOf(tickets[i]) - (Integer) arraylist.get(1);
				tickets[i] = String.format("%d",tmp);
				if (tmp == 0 && arraylist.size() > 2) {
					tmp = Integer.valueOf(tickets[i+1]) - Integer.valueOf((String) arraylist.get(3));
					tickets[i+1] = String.format("%d",tmp);
				}
			}
		}
		
		caldiscount[1] = String.join(",", tickets);
		BW.newLine();
		BW.write(caldiscount[1]);
		
		// 剩下的再重新write一次
		try {
			while (true) {
				BW.newLine();
				BW.write(BR.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			BR.close();
			BW.close();
		}

		BufferedReader BR2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter BW2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			BW2.write(BR2.readLine());
			while (true) {
				BW2.newLine();
				BW2.write(BR2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			BR2.close();
			BW2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();
	}
	
//
//	/**
//	 * @param date
//	 * @param train
//	 * @param start
//	 * @param end
//	 * @param seatNO
//	 * @param discount
//	 * @throws IOException
//	 * 
//	 *                     先見一個中介檔案把資料輸進去，輸完之後再生一個與原本檔案一樣的名字，把中繼檔案輸入 再把中繼檔案刪除
//	 */	
//	public static void setSeatnoEarly(String date, String train, String start, String end, String seatNO,
//			String discount) throws IOException {
//
//		String ttt = "tabble";
//
//		BufferedReader BR = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
//		BufferedWriter BW = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here
//
//		// 找到指定train
//		String line = BR.readLine();
//		BW.write(line);
//		boolean found = false;
//		while (found == false) {
//			line = BR.readLine();
//			BW.newLine();
//			BW.write(line);
//			String[] tt = line.split(",");
//			if (tt[0].contains(train)) {
//				found = true;
//			} else {
//			}
//		}
//
//		// 找到並扣掉折價的票
//		String[] caldiscount = new String[2];
//		caldiscount[0] = BR.readLine();
//		BW.newLine();
//		BW.write(caldiscount[0]);
//		caldiscount[1] = BR.readLine();
//		String[] dis = caldiscount[0].split(",");
//		String[] tickets = caldiscount[1].split(",");
//
//		for (int t = 1; t <= 10; t++) {
//			if (dis[t].contains(discount)) {
//				int tick = Integer.parseInt(tickets[t]) - 1;
//				tickets[t] = Integer.toString(tick);
//				caldiscount[1] = String.join(",", tickets);
//				BW.newLine();
//				BW.write(caldiscount[1]);
//				break;
//			}
//		}
//
//		// 橫排初始String
//		String[] rows = new String[15];
//
//		// 找出起站終站
//		int st = 0;
//		int s = 0, e = 0;
//		boolean matchS = false, matchE = false;
//
//		// 找到開始站，一路write到找到為止
//		try {
//
//			while (matchS == false) {
//				rows[st] = BR.readLine();
//				if (rows[st].contains(start)) {
//					e++;
//					matchS = true;
//				} else {
//					s++;
//					e++;
//					BW.newLine();
//					BW.write(rows[st]);
//				}
//				st++;
//			}
//			while (matchE == false) {
//				rows[st] = BR.readLine();
//				if (rows[st].contains(end)) {
//					matchE = true;
//				} else {
//					e++;
//				}
//				st++;
//			}
//		} catch (Exception ee) {
//		}
//		// rows[s]就是從起始站
//		// rows[e]就是終點站
//
//		// 先找座位的橫的編號X
//		int x = 0;
//		String[] num = rows[0].split(","); // here
//		for (int t = 1; t <= 985; t++) {
//			if (num[t].contains(seatNO)) {
//				x = t;
//				break;
//			}
//		}
//		// 中間的站都變成array，指派第x個元素為1
//		String[] seats = new String[985];
//
//		for (int tt = s; tt <= e; tt++) {
//			seats = rows[tt].split(",");
//			seats[x] = "1";
//			rows[tt] = String.join(",", seats);
//			BW.newLine();
//			BW.write(rows[tt]);
//		}
//
//		// 剩下的再重新write一次
//		try {
//			int fake = 1;
//			while (fake == 1) {
//				BW.newLine();
//				BW.write(BR.readLine());
//
//			}
//		} catch (Exception ee) {
//			ee.getMessage();
//		} finally {
//			BR.close();
//			BW.close();
//		}
//
//		BufferedReader BR2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
//		BufferedWriter BW2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
//		try {
//			int fake2 = 1;
//			BW2.write(BR2.readLine());
//			while (fake2 == 1) {
//				BW2.newLine();
//				BW2.write(BR2.readLine());
//			}
//		} catch (Exception ee) {
//			ee.getMessage();
//		} finally {
//			BR2.close();
//			BW2.close();
//		}
//		File file_dulplicate = new File("Data/" + ttt + ".csv");
//		file_dulplicate.delete();
//
//	}
}