
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

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

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));

		String line = " ";

		while (true) {
			line = rDB.readLine();

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

			String tmp = rDB.readLine();

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
		
		int Tnumber = 0;
		
		for(int i = 1; i < seatnos.length - 1 ; i++) {
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
		
		rDB.close();
		
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
		
		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));
// 找到指定train
		String line = " ";

		while (true) {
			line = rDB.readLine();

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

			String tmp = rDB.readLine();

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
				if (((currentSeat.contains("C") || currentSeat.contains("D")) && kind.equals("aisle")) || 
						((currentSeat.contains("A") || currentSeat.contains("E")) && kind.equals("window"))) {
					break;
				}
		
				if (seats.get(j).get(i).equals("1") ) {
					break T;
				}
				else if(j == way - 1) {
					seatno = seatnos[i];
				}
			}
		}
		
		rDB.close();
		if (seatno.equals("")) {
			seatno = "no seats beside " + kind;
		}

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
		
		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv"));
		
		String line = " ";

		while (true) {
			line = rDB.readLine();

			if (line != null) {
				String[] tt = line.split(",");
				if (tt[0].contains(train)) break;
			}
		}
		
		// 該列次的折價與剩餘票數
		String[] dis = rDB.readLine().split(",");
		
		String[] tickets = rDB.readLine().split(",");
		
		// 回傳ArrayList 一個折多少 一個能拿幾張
		ArrayList<Object> A = new ArrayList<Object>();
		

		if(dis[0].equals("Seats")) {
			A.add("");
			rDB.close();
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

		rDB.close();

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

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here

		// 找到指定train
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].contains(train)) {
				found = true;
			} else {
			}
		}
		// 找到起始、終點
		String[] rows = new String[15];
		// 要記錄是哪兩個站
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// 找到開始站，一路write到找到為止
		try {

			while (matchS == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(start)) {
					s = st;
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(rows[st]);
				}
				st++;
			}
			while (matchE == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
		}

		// rows[s]就是從起始站
		// rows[e]就是終點站

		// 先找座位的橫的編號X
		int x = 0;
		String[] num = rows[2].split(","); // here
		for (int t = 1; t <= 985; t++) {
			if (num[t].contains(seatNO)) {
				x = t;
				break;
			}
		}
		// 中間的站都變成array，指派第x個元素為1
		String[] seats = new String[985];

		for (int tt = s; tt <= e; tt++) {
			seats = rows[tt].split(",");
			seats[x] = "1";
			rows[tt] = String.join(",", seats);
			sDB.newLine();
			sDB.write(rows[tt]);
		}

		// 剩下的再重新write一次
		try {
			int fake = 1;
			while (fake == 1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB.close();
			sDB.close();
		}

		BufferedReader rDB2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			int fake2 = 1;
			sDB2.write(rDB2.readLine());
			while (fake2 == 1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();
	}

	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @param discount
	 * @throws IOException
	 * 
	 *                     先見一個中介檔案把資料輸進去，輸完之後再生一個與原本檔案一樣的名字，把中繼檔案輸入 再把中繼檔案刪除
	 */
	public static void setSeatnoEarly(String date, String train, String start, String end, String seatNO,
			String discount) throws IOException {

		String ttt = "tabble";

		BufferedReader rDB = new BufferedReader(new FileReader("Data/" + date + ".csv")); // here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("Data/" + ttt + ".csv")); // here

		// 找到指定train
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].contains(train)) {
				found = true;
			} else {
			}
		}

		// 找到並扣掉折價的票
		String[] caldiscount = new String[2];
		caldiscount[0] = rDB.readLine();
		sDB.newLine();
		sDB.write(caldiscount[0]);
		caldiscount[1] = rDB.readLine();
		String[] dis = caldiscount[0].split(",");
		String[] tickets = caldiscount[1].split(",");

		for (int t = 1; t <= 10; t++) {
			if (dis[t].contains(discount)) {
				int tick = Integer.parseInt(tickets[t]) - 1;
				tickets[t] = Integer.toString(tick);
				caldiscount[1] = String.join(",", tickets);
				sDB.newLine();
				sDB.write(caldiscount[1]);
				break;
			}
		}

		// 橫排初始String
		String[] rows = new String[15];

		// 找出起站終站
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;

		// 找到開始站，一路write到找到為止
		try {

			while (matchS == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(start)) {
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(rows[st]);
				}
				st++;
			}
			while (matchE == false) {
				rows[st] = rDB.readLine();
				if (rows[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
		}
		// rows[s]就是從起始站
		// rows[e]就是終點站

		// 先找座位的橫的編號X
		int x = 0;
		String[] num = rows[0].split(","); // here
		for (int t = 1; t <= 985; t++) {
			if (num[t].contains(seatNO)) {
				x = t;
				break;
			}
		}
		// 中間的站都變成array，指派第x個元素為1
		String[] seats = new String[985];

		for (int tt = s; tt <= e; tt++) {
			seats = rows[tt].split(",");
			seats[x] = "1";
			rows[tt] = String.join(",", seats);
			sDB.newLine();
			sDB.write(rows[tt]);
		}

		// 剩下的再重新write一次
		try {
			int fake = 1;
			while (fake == 1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB.close();
			sDB.close();
		}

		BufferedReader rDB2 = new BufferedReader(new FileReader("Data/" + ttt + ".csv")); // here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("Data/" + date + ".csv")); // here
		try {
			int fake2 = 1;
			sDB2.write(rDB2.readLine());
			while (fake2 == 1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		} catch (Exception ee) {
			ee.getMessage();
		} finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("Data/" + ttt + ".csv");
		file_dulplicate.delete();

	}

	// 示範
	public static void main(String[] args) throws IOException {

		String ss = getSeatno("0612", "0565", "1000 : Taipei", "1035 : Miaoli", 8);
		System.out.println(ss);// 0101A, 0101B, 0101C, 0102A, 0102B, 0102C, 0102D, 0102E,

		setSeatno("0612", "0565", "1000 : Taipei", "1035 : Miaoli", "0102B");

		setSeatnoEarly("0612", "0565", "1000 : Taipei", "1035 : Miaoli", "0102D", "0.9");
	}

}