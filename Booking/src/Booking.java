import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.*;

public class Booking {
	//check
	
	JSONArray jsonArray = JSONUtils.getJSONArrayFromFile("/timeTable.json");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm"); //此為輸入日期格式
	
	private int Direction; //去程方向
	
	JSONArray Davailable = new JSONArray(); //去回程列次JSONArray
	JSONArray Ravailable = new JSONArray();
	
	ArrayList<String> Dseatno; //去回程 對應上面JSONArray第幾個列次 該列次分配給的位子
	ArrayList<String> Rseatno; 
	
	CSVFile builder = new CSVFile(); //建檔的caller
	
	ArrayList<ArrayList<Object>> DEDarray = new ArrayList<ArrayList<Object>>(); //早鳥票特價
	ArrayList<ArrayList<Object>> REDarray = new ArrayList<ArrayList<Object>>();
	
	ArrayList<Double> DUDdiscount = new ArrayList<Double>(); //大學生特價
	ArrayList<Double> RUDdiscount = new ArrayList<Double>();
	
	String DMonDay = "";
	String RMonDay = "";
	
	String SStation = ""; 
	String DStation = "";
	
	public String Search(String Ddate, String Rdate, // Ddate出發時間, Rdate返程時間
			String SStation, String DStation, //S始站, D終站
			int normalT, int concessionT, int studentT, //一般票, 優待票(5折), 大學生票
			int AorW, boolean BorS) throws IOException // 走道or靠窗(0沒要求1靠窗2走道), true商務 false標準 車廂
	{
		
		this.SStation = SStation; 
		this.DStation = DStation;
		
		//檢查票數有沒有超過		
		int totalT = normalT+concessionT+studentT;
		
		if ((totalT > 10) || ((Rdate != "")&&(totalT > 5))) {
			return "失敗，因訂單預定過多車票(每筆最多10張，來回車票獨立計算)";
		}
		
		//處理方向
		this.trainDirection(SStation, DStation);
		
		//處理時間

		//今天時間
		long current = System.currentTimeMillis();
		Date ttoday = new Date(current);
		Calendar today = Calendar.getInstance();
		today.setTime(ttoday);
	
		//去程
		Date Dedate  = null; //Date object
		String DoWD  = ""; //day of week
		String Dtime = ""; //time
		Calendar DeCal = Calendar.getInstance();
		
		if(Ddate != "") {
			try {
				Dedate = sdf.parse(Ddate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			DoWD = getWeekofDay(Dedate);
			
			Dtime = Ddate.substring(11);
			
			DeCal.setTime(Dedate);
		}
		
		//建檔
		try {
			builder.createFile(Dedate);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
		//回程
		Date Redate  = null; //Date object
		String DoWR  = ""; //day of week
		String Rtime = ""; //time
		Calendar ReCal = Calendar.getInstance();
		
		if(Rdate != "") {
			try {
				Redate = sdf.parse(Rdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			DoWR = getWeekofDay(Redate);
			
			Rtime = Rdate.substring(11);
			
			ReCal.setTime(Redate);
		}
		
		//建檔
		try {
			builder.createFile(Redate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		/*
		 * 提供預訂當日及未來28日以內之車票。
		 * 訂位開放時間為乘車日(含)前28日凌晨0點開始，
		 * 當日車次之預訂僅受理至列車出發時間前1小時為止
		 */
		
		//日期期限 (今日後的28天)
		Calendar Limitdate = today;
		Limitdate.add(Calendar.DAY_OF_MONTH, 28);

		//確認當日時間是否可供訂票
		if(Limitdate.before(DeCal)) {
			if(Limitdate.before(ReCal)) {
				return "去回程列車皆尚未開放訂票";
			}
			else {
				return "去程列車皆尚未開放訂票";
			}
		}
		else {
			//無問題
		}

		//將符合條件的列車編號的JSONObject 放入JSONArray Davailable 與 Ravailable
		
		//注意這裡還沒確認有沒有座位
		
		/*    確認順序為：
		 * 1. 確認方向 Direction
		 * 2. 確認星期 DayofWeek
		 * 3. 確認是否符合路線(有無抵達始站與終站) Stations
		 * 4. 確認始站出發時間 Date
		 */
		
		for(int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject train = jsonArray.getJSONObject(i);
			JSONObject timetable = train.getJSONObject("GeneralTimetable");
			
			//去程
			if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") == Direction)
				//確認方向
				&& (timetable.getJSONObject("ServiceDay").getInt(DoWD) == 1) 
				//確認星期
				&& (trainroutehas(train, SStation, DStation)) 
				//確認路線
				&& (dparturetime(Dtime, SStation, timetable.getJSONArray("StopTimes")))) 
				//確認出發時間
			{
				Davailable.put(train);
			}
			
			//回程
			if (Rdate != "") {
				//確認是否有回程
				if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") != Direction)
					//確認方向
					&& (timetable.getJSONObject("ServiceDay").getInt(DoWR) == 1) 
					//確認星期
					&& (trainroutehas(train, SStation, DStation)) 
					//確認路線
					&& (dparturetime(Rtime, DStation, timetable.getJSONArray("StopTimes")))) 
					//確認出發時間
				{
					Ravailable.put(train);
				}
			}
		}
		
		//處理票量問題 與 早鳥票問題
		
		SimpleDateFormat format =new SimpleDateFormat("MMdd");
	    String DMonDay = format.format(Dedate);
		String RMonDay = format.format(Redate);
		
		this.DMonDay = DMonDay;
		this.RMonDay = RMonDay;
	    
		int Dlength = Davailable.length();
		int Rlength = Ravailable.length();
		
		Dseatno = new ArrayList<String>();
		Rseatno = new ArrayList<String>();
		
		//把Llimitdate改為今日後五天(28-23) 
		
		Limitdate.add(Calendar.DAY_OF_MONTH, -23);
		 
		//去程 //確認是否於五日前 
		

		System.out.println(Davailable.length());
		System.out.println(Ravailable.length());
		
		if ((totalT == 1) && (AorW != 0)) {
			
			String kind = "";
			
			if(AorW == 1) {
				kind = "window";
			}
			else if(AorW == 2) {
				kind = "aisle";
			}
			
			for (int i = 0; i< Dlength; i++) {
				
				
				String trainno = TrainNoofAv(Davailable, i);
				String tmp     = searchDB.getSeatnoSpecial(DMonDay, trainno, SStation, DStation, kind);
				
				
				if (tmp.equals("")) {
					Davailable.remove(i);
					Dlength--;
				}
				else {
					Dseatno.add(tmp);
					if (Limitdate.before(DeCal)) {
						DEDarray.add(searchDB.checkEarly(DMonDay, trainno, 1));
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						DEDarray.add(ttmp);
					}
					
				}
			}
			
			for (int j = 0; j< Rlength; j++) {
				String trainno = TrainNoofAv(Ravailable, j);
				String tmp     = searchDB.getSeatnoSpecial(RMonDay, trainno, DStation, SStation, kind);
				
				if (tmp.equals("")) {
					Ravailable.remove(j);
					Rlength--;
				}
				else {
					Rseatno.add(tmp);
					if (Limitdate.before(ReCal)) {
						REDarray.add(searchDB.checkEarly(RMonDay, trainno, 1));
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						REDarray.add(ttmp);
					}
				}
			}
		}
		

		
		else {
			
			for (int i = 0; i< Dlength; i++) {
				String trainno = TrainNoofAv(Davailable, i);
				String tmp     = searchDB.getSeatno(DMonDay, trainno, SStation, DStation, totalT);
				
				if (tmp.equals("")) {
					Davailable.remove(i);
					Dlength--;
				}
				else {
					Dseatno.add(tmp);
					if (Limitdate.before(DeCal)) {
						DEDarray.add(searchDB.checkEarly(DMonDay, trainno, normalT + studentT));
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						DEDarray.add(ttmp);
					}
				}
			}
			
			for (int j = 0; j< Rlength; j++) {
				
				String trainno = TrainNoofAv(Ravailable, j);
				String tmp     = searchDB.getSeatno(RMonDay, trainno, DStation, SStation, totalT);
				
				if (tmp.equals("")) {
					System.out.println("why remoove?");
					Ravailable.remove(j);
					Rlength--;
				}
				else {
					Rseatno.add(tmp);
					if (Limitdate.before(ReCal)) {
						System.out.println(searchDB.checkEarly(RMonDay, trainno, normalT + studentT).get(0));
						
						REDarray.add(searchDB.checkEarly(RMonDay, trainno, normalT + studentT));
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						REDarray.add(ttmp);
					}
				}
			}
		}


		System.out.println(DEDarray.size());
		System.out.println(REDarray.size());
		//學生票處理
		
		
		//優待票處理
		
		//商務則沒有各種優待票
		if (BorS == false) {
			
//---------------------------------------------剩下早鳥票--------------------------------------------------------
			
		//大學生票 (只有折扣)
		/*
		 * 大學生優惠（5折/75折/88折）票恕無法與其他優惠合併使用。
		 */
			if (studentT > 0) {
				JSONObject universityDiscount = JSONUtils.getJSONObjectFromFile("/universityDiscount.json");
				JSONArray UDTrains = universityDiscount.getJSONArray("DiscountTrains");
				//studentT
				
				//外圈為去程的JSONArray
				for(int j = 0; j < Davailable.length(); j++) {
					//內圈1為所有ED的JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//若找到對應的列車

						if (TrainNoof(UDTrains, i).equals(TrainNoofAv(Davailable, j))) {
							//將該列車的於該星期的折扣放入DUDdiscount中
							DUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWD));
						}
						else if (i+1 == UDTrains.length()) {
							//若都找不到則維持原價add(1.0)
							DUDdiscount.add(1.0);
						}
						else;
					}
				}
				
				//外圈為去程的JSONArray
				for(int j = 0; j < Ravailable.length(); j++) {
					//內圈1為所有ED的JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//若找到對應的列車
						if (TrainNoof(UDTrains, i).equals(TrainNoofAv(Ravailable, j))) {
							//將該列車的於該星期的折扣放入DUDdiscount中
							RUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWR));
						}
						else if (i+1 == UDTrains.length()) {
							//若都找不到則維持原價add(1.0)
							RUDdiscount.add(1.0);
						}
						else;
					}
				}
				//搜尋班次的折價與以及是否還有位子
			}
			
			else;
			
		//優待票 (各類價格)
		
		//整車正常票(分一般與商務)
			
			//輸出搜尋結果
		//1.
		// 首先選擇車次
			// 去程：起站終站 日期(星期)
			// 選擇按鈕 車次 全票優惠 出發時間 到達時間 行車時間
			// 
			// 回程：起站終站 日期(星期)
			// 選擇按鈕 車次 全票優惠 出發時間 到達時間 行車時間
			// 
			// 車廂:標準/商務 票數：全票幾張|愛心票幾張|敬老票幾張
			//
			// 重新查詢                         確認車次
		//2.
		// 再來確認票
			// 行程 日期 車次 起站 終站 出發 到達 全票(含價格) 其他票種數量(含價格) 小計算
			// 去
			// 回
			// 車廂 票數 總票價
		// 取票人資訊
			// 識別碼(身分證或護照號碼) 必要
			// 電話 email等
		// 完全訂位
			
			System.out.println("去程列車如下：\n");
			System.out.println("車次   | 早鳥優惠 | 大學生優惠 | 出發時間 | 抵達時間 |");
			
			for (int i = 0; i< Davailable.length();i++) {
				System.out.print(TrainNoofAv(Davailable,i) + " |");
				System.out.print(DEDarray.get(i).get(0).toString() + "折  |");
				if (studentT > 0) {
					System.out.print(" " + DUDdiscount.get(i) + "折  |");
				}
				else {
					System.out.print("    折  |");
				}
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				System.out.println("回程列車如下：\n");
				System.out.println("車次   | 早鳥優惠 | 大學生優惠 | 出發時間 | 抵達時間 |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.print(TrainNoofAv(Ravailable,j) + " |");
					System.out.print(REDarray.get(j).get(0).toString() + "折  |");
					if (studentT > 0) {
						System.out.print(RUDdiscount.get(j) + "折  |");
					}
					else {
						System.out.print("    折  |");
					}

					JSONArray timetable = Ravailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
					
					System.out.print(" " + Departuretime(DStation,timetable) + " |");
					System.out.print(" " + Arrivetime   (SStation,timetable) + " |");
					
					System.out.println();
					System.out.println();
				}
			}
			
			return "訂票搜尋結果顯示完畢";
		}
		
		// END -----> 輸出商務車廂的車票價格、並處理劃為(改變excel座位檔案)
		else {
			//輸出搜尋結果
			
			System.out.println("去程列車如下：\n");
			System.out.println("0000  | 00:00 | 00:00 |");
			System.out.println("車次   | 出發時間 | 抵達時間 |");
			
			for (int i = 0; i< Davailable.length();i++) {
				System.out.print(TrainNoofAv(Davailable,i) + " |");
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONArray("StopTimes");
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				System.out.println("回程列車如下：\n");
				System.out.println("車次   | 出發時間 | 抵達時間 |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.print(TrainNoofAv(Ravailable,j) + " |");

					JSONArray timetable = Davailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONArray("StopTimes");
					
					System.out.print("| " + Departuretime(DStation,timetable) + " |");
					System.out.print("| " + Arrivetime   (SStation,timetable) + " |");
					
					System.out.println();
					System.out.println();
				}
			}
			
			return "訂票搜尋結果顯示完畢";
		}
	}
	
	public String Book(int Dint, int Rint) throws IOException {
		// size是零(沒有特價)就會直接忽略for迴圈
		ArrayList<Object> Dtmparraylist =  DEDarray.get(Dint);
		ArrayList<Object> Rtmparraylist =  REDarray.get(Rint);

		String[] Dseats = Dseatno.get(Dint).split(",");
		String[] Rseats = Rseatno.get(Rint).split(",");
		
		String   Dtrain = TrainNoofAv(Davailable,Dint);
		String   Rtrain = TrainNoofAv(Ravailable,Rint);
		
		System.out.println(Dtrain);
		
		for (int j = 0; j < Dseats.length; j++) {
			searchDB.setSeatno(DMonDay, Dtrain, SStation, DStation, Dseats[j]);
			
			System.out.print(Dseats[j] + ",");
		}
		
		System.out.println();
		
		if (Dtmparraylist.size() > 0) {
			searchDB.setED(DMonDay, Dtrain, Dtmparraylist);
		}

		System.out.println(Rtrain);
		
		for (int k = 0; k < Rseats.length; k++) {
			searchDB.setSeatno(RMonDay, Rtrain, DStation, SStation, Rseats[k]);
			
			System.out.print(Rseats[k] + ",");
		}
		
		System.out.println();
		
		if (Rtmparraylist.size() > 0) {
			searchDB.setED(RMonDay, Rtrain, Rtmparraylist);
		}
		
		return "訂票完成";
	}
	

	/**
	 * 此method用來確認列車(去程 如果有來回的話)行徑方向
	 * @param sStation
	 * @param dStation
	 */
	
	private void trainDirection(String sStation, String dStation) {
		if (Integer.valueOf(sStation) < Integer.valueOf(dStation)) {
			Direction = 0; //南向
		}
		else{
			Direction = 1; //北向
		}
	}


	/**
	 * 此method方便找查available ArrayList中的TrainNo
	 * 
	 * @param Ravailable
	 * @param which 第幾個
	 * @return 該位置的TrainNo
	 */
	
	public static String TrainNoofAv(JSONArray Ravailable, int which) {
		return Ravailable.getJSONObject(which).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo");
	}
	
	/**
	 * 此method方便找查Discount table中的TrainNo
	 * 
	 * @param DTrains discount trains
	 * @param which 第幾個
	 * @return 該位置的TrainNo
	 */
	
	public static String TrainNoof(JSONArray EDTrains, int which) {
		return EDTrains.getJSONObject(which).getString("TrainNo");
	}
	
	/**
	 * @param time 輸入時間
	 * @param SStation 起站
	 * @param StopTimes 該列次停站表
	 * @return 若該列次該站的出站時間 在 輸入時間 後 則回傳true 反之回傳false
	 */
	
	private boolean dparturetime(String time, String SStation, JSONArray StopTimes) {
		for (int i=0 ; i < StopTimes.length(); i++) {
			if (StopTimes.getJSONObject(i).getString("StationID").equals(SStation)){
				String DepartureTime = StopTimes.getJSONObject(i).getString("DepartureTime").replace(":", "");
				if (Integer.valueOf(DepartureTime) >= Integer.valueOf(time)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param DStation
	 * @param StopTimes
	 * @return 找出StopTimes裡該站的出發時間
	 */
	
	private String Departuretime(String DStation, JSONArray StopTimes) {
		for (int i=0 ; i < StopTimes.length(); i++) {
			if (StopTimes.getJSONObject(i).getString("StationID").equals(DStation)){
				return StopTimes.getJSONObject(i).getString("DepartureTime");
			}
			else;
		}
		return "error: cant find departuretime";
	}
	
	/**
	 * @param DStation
	 * @param StopTimes
	 * @return 找出StopTimes裡該站的出發時間(因查詢不到我們將其視作抵達時間)
	 */
	
	private String Arrivetime(String AStation, JSONArray StopTimes) {
		for (int i=0 ; i < StopTimes.length(); i++) {
			if (StopTimes.getJSONObject(i).getString("StationID").equals(AStation)){
				return StopTimes.getJSONObject(i).getString("DepartureTime");
			}
			else;
		}
		return "error: cant find arrive time";
	}

	/**
	 * @param train 該列次的JSONobject
	 * @param SStation 始站
	 * @param DStation 終站
	 * @return true 若路線正確 false 反之
	 */
	
	private boolean trainroutehas(JSONObject train, String SStation, String DStation) {
		boolean S = false;
		boolean D = false;
		
		for (int j = 0; j < train.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").length(); j++) {
			String station = train.getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(j).getString("StationID");
			if (station.equals(SStation)) {
				S = true;
			}
			if (station.equals(DStation)) {
				D = true;
			}
		}

		if (S && D) {
			return true;
		}
		
		else return false;
	}
	
	/**
	 * @param date
	 * @return 該日期的星期
	 */
	
	private String getWeekofDay(Date date) {
		
		String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        
		return weekDays[w];
	}
}
