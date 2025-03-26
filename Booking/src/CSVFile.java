
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 根據要訂的日期,當收到訂票的指令時創建出一個檔為那天所有車次的資料,並把這天加入紀錄(recentDateFile),以作為之後刪除的參考
 * 及把訂票當天之前所有的檔全部刪掉(在constructor(類似)時執行？)
 * p.s要用前請先把路徑改成自己的,windows跟mac存路徑的方式好像不太一樣,windows可以問品元大ㄐㄐ
 * 
 * @author  fuhming
 * @apiNote Tong
 * 
 */

public class CSVFile {

	public CSVFile() {

	};

	private static String writeMonth(Calendar cal) {
		return String.format("%02d", cal.get(Calendar.MONTH) + 1);
	}

	private static String wrtieDate(Calendar cal) {
		return String.format("%02d", cal.get(Calendar.DATE));
	}

	/**
	 * 根據給定的date（要訂的日期）建檔
	 * 
	 * @param date
	 * @throws Exception
	 * 
	 */
	public void createFile(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); // cal.add(Calendar.DATE, -1); 測試用
		int Weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;

		// 判斷這天之前是否已創過
		if (!(new File("Data/" + writeMonth(cal) + wrtieDate(cal) + ".csv").exists())) {
			writeFile(Weekday, writeMonth(cal), wrtieDate(cal)); // 寫入該天的車次檔
			writeRecentFile(writeMonth(cal) + wrtieDate(cal));
		}

		else
			System.out.println("this date has already been created");
	}

	// --------------------------------寫入查詢列車的當日檔案--------------------------------------

	/**
	 * 將所有列車資訊寫入輸入日期的檔案中(所以需要先經過建檔)
	 * 
	 * @param whichWeekdays 哪個星期
	 * @param month         月
	 * @param date          日
	 * @throws Exception
	 * 
	 */

	public void writeFile(int whichWeekdays, String month, String date) throws Exception {

		// 建立的檔名為日期(ex:0613)
		BufferedWriter bw = new BufferedWriter(new FileWriter("Data/" + month + date + ".csv"));

		// 找出當天所有有開的車次
		int[] days = FindTrain(whichWeekdays);

		// 早鳥票車次的array
		JSONObject obj = JSONUtils.getJSONObjectFromFile("/earlyDiscount.json");
		JSONArray DiscountTrains = obj.getJSONArray("DiscountTrains");

		// 所有列次的array
		JSONArray timeTable = JSONUtils.getJSONArrayFromFile("/timeTable.json");

		// -----------------------依序開始處理該天所有列次--------------------------------

		for (int i = 0; i < days.length; i++) {

			// 0（沒開）的話就直接跳過
			if (days[i] == 0) continue;

			// if (i == 5)
			// break;

			JSONObject element = timeTable.getJSONObject(i);
			JSONObject GeneralTimetable = element.getJSONObject("GeneralTimetable");
			JSONObject GeneralTrainInfo = GeneralTimetable.getJSONObject("GeneralTrainInfo");

			// 這台車的車號
			String TrainNo = GeneralTrainInfo.getString("TrainNo");

			// 這台車的Direction
			int Direction = GeneralTrainInfo.getInt("Direction");

			// 這台車的停/開車時間
			JSONArray StopTimes = GeneralTimetable.getJSONArray("StopTimes");

			// -----------------------處理earlyDiscount--------------------------------
			JSONObject EDTrain = null;
			String earlyDiscountTrainNo = "";

			for (int k = 0; k < DiscountTrains.length(); k++) {

				// 註：我把它改成檢查完TrainNo才存入記憶體，邏輯比較順且較快
				if (DiscountTrains.getJSONObject(k).getString("TrainNo").equals(TrainNo)) {
					
					EDTrain = DiscountTrains.getJSONObject(k);
					earlyDiscountTrainNo = EDTrain.getString("TrainNo");
					break;
				} 
				else ;
			
			}

			// System.out.println(earlyDiscountTrainNo.equals(TrainNo)); 這行應該不用先註解掉

			// -----------------------底下開始將資料寫入檔案--------------------------------

			// 本列車資訊
			bw.write(TrainNo + "," + Direction + "\n");

			if (earlyDiscountTrainNo.equals(TrainNo)) { // 這個if是不是不用?
				JSONObject ServiceDayDiscount = EDTrain.getJSONObject("ServiceDayDiscount");
				String Weekdays = FindWeekday(whichWeekdays);

				// 把discount與tickets訊息寫入檔案

				try {
					JSONArray day = ServiceDayDiscount.getJSONArray(Weekdays);
					for (int j = 0; j < day.length(); j++) {
						JSONObject earlyDiscount = day.getJSONObject(j);
						double discount = earlyDiscount.getDouble("discount");
						bw.write(String.valueOf(discount) + ",");

					}
					bw.write("\n");
					for (int j = 0; j < day.length(); j++) {
						JSONObject earlyDiscount = day.getJSONObject(j);
						int tickets = earlyDiscount.getInt("tickets");
						bw.write(String.valueOf(tickets) + ",");
					}
					bw.write("\n");

				} catch (Exception e) {
					// 要抓什麼exception?
				}

			}

			// 本列車座位資訊

			bw.write("Seats" + ",");
			seat(bw);
			bw.write("\n");

			// 各站點的座位資訊
			for (int j = 0; j < StopTimes.length(); j++) {

				JSONObject StopStations = StopTimes.getJSONObject(j);
				String ID = StopStations.getString("StationID");

				// System.out.println(name);
				bw.write(ID + ",");

				seat0("/seat.json", bw);
				bw.write("\n");

			}

		}

		// 完成所有檔案的寫入
		bw.close();

	}

	// -----------------------recentDateFile的寫入方法--------------------------------
	public void writeRecentFile(String createdDate) throws IOException {

		if ((new File("Data/recentDateFile.csv").exists())) {
			BufferedReader br = new BufferedReader(new FileReader("Data/recentDateFile.csv"));
			String Line = br.readLine(); // 讀舊的出來
			br.close();
			String[] OLD = Line.split(",");
			ArrayList<String> DateList = new ArrayList<String>(Arrays.asList(OLD));
			DateList.add(createdDate); // 加上這次建的
			String[] NEW = DateList.toArray(new String[0]);
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("Data/recentDateFile.csv"));
			for (int i = 0; i < NEW.length; i++) {
				bw1.write(NEW[i] + ","); // 重寫新的recentDateFile
			}
			bw1.close();
		} else {
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("Data/recentDateFile.csv"));
			bw1.write(createdDate);
			bw1.close();
		}
	}

	// ----------------------------刪除已經過期的File的方法--------------------------------
	
	public void removeFile() throws IOException {
		
		Calendar cal = Calendar.getInstance(); // 取得當天日期
		String CAL = String.format("%02d", cal.get(Calendar.MONTH) + 1) + String.format("%02d", cal.get(Calendar.DATE)); // 當天月分+日期
		BufferedReader br = new BufferedReader(new FileReader("Data/recentDateFile.csv"));
		String Line = br.readLine(); // 取得之前所有建過的日期
		br.close();
		String[] OLD = Line.split(",");
		ArrayList<String> DateList = new ArrayList<String>(Arrays.asList(OLD));
		
		int j = 0; // 從0開始檢查,沒刪掉的話就往前檢查
		for (int i = 0; i < DateList.size(); i++) {
			if (Integer.valueOf(DateList.get(j)) < Integer.valueOf(CAL)) { // 建過的日期 < 當天則刪掉
				new File("Data/" + DateList.get(j) + ".csv").delete();
				DateList.remove(j);
			} else
				j++;
		}
		String[] NEW = DateList.toArray(new String[0]);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("Data/recentDateFile.csv"));
		for (int i = 0; i < NEW.length; i++) {
			bw2.write(NEW[i]); // 覆寫新的recentDateFile
		}
		bw2.close();
	}

	// ----------------------------查找該星期所有車次的方法--------------------------------
	public int[] FindTrain(int whichWeekdays) {
		JSONArray timeTable = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		int whichDay[] = new int[timeTable.length()];

		for (int i = 0; i < timeTable.length(); i++) {
			JSONObject element = timeTable.getJSONObject(i);
			JSONObject GeneralTimetable = element.getJSONObject("GeneralTimetable");
			JSONObject ServiceDay = GeneralTimetable.getJSONObject("ServiceDay");
			switch (whichWeekdays) {
			case 0:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Sunday");
				}
				break;
			case 1:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Monday");
				}
				break;
			case 2:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Tuesday");
				}
				break;
			case 3:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Wednesday");
				}
				break;
			case 4:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Thursday");
				}
				break;
			case 5:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Friday");
				}
				break;
			case 6:
				for (int m = 0; m < timeTable.length(); m++) {
					whichDay[i] = ServiceDay.getInt("Saturday");
				}
				break;
			}
		}

		return whichDay;
	}
	
	// 把WOD 的int 轉成String
	private String FindWeekday(int whichWeekdays) {
		switch (whichWeekdays) {
		case 0:
			return "Sunday";
		case 1:
			return "Monday";
		case 2:
			return "Tuesday";
		case 3:
			return "Wednesday";
		case 4:
			return "Thursday";
		case 5:
			return "Friday";
		case 6:
			return "Saturday";
		}
		return "3小餒";
	}

	// ---------------------------從seat.json抓表格過來將座位都設成0--------------------------------
	public void seat0(String str, BufferedWriter bw) throws IOException {
		JSONObject obj = JSONUtils.getJSONObjectFromFile(str);
		JSONArray train = obj.getJSONArray("cars");
		for (Object cars : train) {
			JSONObject seats = ((JSONObject) cars).getJSONObject("seats");
			for (Integer line = 1; line <= 20; line++) {
				try {
					JSONArray seatNO = seats.getJSONArray(line.toString());
					for (Object ele : seatNO) {
						// System.out.println(line+" "+ele);
						bw.write(0 + ","); // 這裡我把ele直接改成0就全都是0了
					}
				} catch (Exception e) {
				}
			}
		}
	}

	// ----------------------------從seat.json抓表格過來確定位置--------------------------------
	public void seat(BufferedWriter bw) throws IOException { // 1-1A,1-1B,1-1C,...
		JSONObject obj = JSONUtils.getJSONObjectFromFile("/seat.json");
		JSONArray cars = obj.getJSONArray("cars");
		for (int i = 1; i <= cars.length(); i++) {
			JSONObject carsObj = cars.getJSONObject(i - 1);
			JSONObject seats = carsObj.getJSONObject("seats");
			try {
				for (int j = 1; j <= 20; j++) { // 由於每輛車廂最多到20，故取20為上限
					JSONArray Seat = seats.getJSONArray(String.valueOf(j));
					for (int k = 0; k < Seat.length(); k++) {
						String eachSeat = Seat.getString(k);
						bw.write(String.format("%02d", i) + String.format("%02d", j) + eachSeat + ",");

					}
				}
			} catch (Exception e) {
			}
		}
	}
}