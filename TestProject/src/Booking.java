import java.io.BufferedWriter;
import java.io.FileWriter;
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
	
	private JSONArray Davailable = new JSONArray(); //去回程列次JSONArray
	private JSONArray Ravailable = new JSONArray();
	
	private ArrayList<String> Dseatnos = new ArrayList<String>(); //去回程 對應上面JSONArray第幾個列次 該列次分配給的位子
	private ArrayList<String> Rseatnos = new ArrayList<String>();
	
	CSVFile builder = new CSVFile(); //建檔的caller
	
	private ArrayList<ArrayList<Object>> DEDarray = new ArrayList<ArrayList<Object>>(); //早鳥票特價 該列次 格式: 折扣 給的票數
	private ArrayList<ArrayList<Object>> REDarray = new ArrayList<ArrayList<Object>>();
	
	private ArrayList<String> DUDdiscount = new ArrayList<String>(); //大學生特價 該列次就是那個價格
	private ArrayList<String> RUDdiscount = new ArrayList<String>();
	
	SimpleDateFormat OPformat = new SimpleDateFormat("yyyy-MM-dd"); //outputformat
	
	//以下為該search或trainnosearch結果的儲存地方(搜尋後選好的車次或是指定的車次)
	
	private int Dint = -1; //此為search完選中的(列表中的第幾個)
	private int Rint = -1;
	
	private String Ddate = ""; //date形式
	private String Rdate = ""; 
	
	private String DMonDay = ""; //MONDAY形式(方便讀檔案)
	private String RMonDay = "";
	
	private String SStation = ""; //起終站
	private String DStation = "";
	
	private String ticketType = "standard"; //票種(折扣)
	
	boolean BorS = false;
	
	int totalT = 0;
	int normalT = 0;
	int concessionT = 0;
	int studentT = 0;
	
	String[][] Davareturn;
	String[][] Ravareturn;
	
	public String Search(String Ddate, String Rdate, // Ddate出發時間, Rdate返程時間
			String SStation, String DStation, //S始站, D終站
			int normalT, int concessionT, int studentT, //一般票, 優待票(5折), 大學生票
			int AorW, boolean BorS) // 走道or靠窗(0沒要求1靠窗2走道), true商務 false標準 車廂
			throws IOException, BookingExceptions
	{
		
		this.SStation = SStation; 
		this.DStation = DStation;
		
		//檢查票數有沒有超過		
		
		this.normalT = normalT;
		this.concessionT = concessionT;
		this.studentT = studentT;
		this.totalT = normalT+concessionT+studentT;
		
		DEDarray.clear();
		REDarray.clear();
		DUDdiscount.clear();
		RUDdiscount.clear();
		Dseatnos.clear();
		Rseatnos.clear();
		
		if ((totalT > 10) || ((Rdate != "")&&(totalT > 5))) {
			throw new BookingExceptions("注意：訂單預定過多車票(每筆最多10張，來回車票獨立計算)");
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
		this.Ddate   = Ddate;
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
		this.Rdate   = Rdate;
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
				throw new BookingExceptions("注意：去回程列車日期皆尚未開放訂票");
			}
			else {
				throw new BookingExceptions("注意：去程列車日期皆尚未開放訂票");
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
		
		Dseatnos = new ArrayList<String>();
		Rseatnos = new ArrayList<String>();
		
		//把Llimitdate改為今日後五天(28-23) 
		
		Limitdate.add(Calendar.DAY_OF_MONTH, -23);
		
		//去程 //確認是否於五日前 
		
		if (AorW != 0 && totalT != 1) {
			throw new BookingExceptions("注意：乘車人數超過一人時無法使用偏好座位功能。");
		}
		
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
				
				String tmp = null;
				
				if (BorS) {
					tmp = searchDB.getBSeatnoSpecial(DMonDay, trainno, SStation, DStation, kind);
				}else {
					tmp = searchDB.getSeatnoSpecial(DMonDay, trainno, SStation, DStation, kind);
				}
				
				if (tmp.equals("")) {
					Davailable.remove(i);
					Dlength--;
				}
				else {
					Dseatnos.add(tmp);
					if (BorS == false) {
						if (Limitdate.before(DeCal)) {
							DEDarray.add(searchDB.checkEarly(DMonDay, trainno, 1));
						}
						else {
							ArrayList<Object> ttmp = new ArrayList<Object> ();
							DEDarray.add(ttmp);
						}
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						DEDarray.add(ttmp);
					}
				}
			}
			
			for (int j = 0; j< Rlength; j++) {
				String trainno = TrainNoofAv(Ravailable, j);
				String tmp = null;
				if(BorS) {
					tmp = searchDB.getBSeatnoSpecial(RMonDay, trainno, DStation, SStation, kind);
				}else {
					tmp = searchDB.getSeatnoSpecial(RMonDay, trainno, DStation, SStation, kind);
				}
				
				if (tmp.equals("")) {
					Ravailable.remove(j);
					Rlength--;
				}
				else {
					Rseatnos.add(tmp);
					if (BorS == false) {
						System.out.println("test");
						if (Limitdate.before(ReCal)) {
							REDarray.add(searchDB.checkEarly(RMonDay, trainno, 1));
						}
						else {
							ArrayList<Object> ttmp = new ArrayList<Object> ();
							REDarray.add(ttmp);
						}
					}
					else {
						ArrayList<Object> ttmp = new ArrayList<Object> ();
						REDarray.add(ttmp);
					}
				}
			}
		}
		
		else {
			ArrayList<Object> ttmp = new ArrayList<Object> ();
			
			for (int i = 0; i< Dlength; i++) {
				String trainno = TrainNoofAv(Davailable, i);
				String tmp = null;
				if(BorS) {
					tmp = searchDB.getBSeatno(DMonDay, trainno, SStation, DStation, totalT);
				}else {
					tmp = searchDB.getSeatno(DMonDay, trainno, SStation, DStation, totalT);
				}
				
				if (tmp.equals("")) {
					Davailable.remove(i);
					Dlength--;
				}
				else {
					Dseatnos.add(tmp);
					if(BorS == false) {
						if (Limitdate.before(DeCal)) {
							DEDarray.add(searchDB.checkEarly(DMonDay, trainno, normalT));
						}
						else {
							System.out.println("BorS empty");
							DEDarray.add(ttmp);
						}
					}
					else {
						System.out.println("empty");
						DEDarray.add(ttmp);
					}
				}
			}
			
			for (int j = 0; j< Rlength; j++) {
				
				String trainno = TrainNoofAv(Ravailable, j);
				String tmp = null;
				if(BorS) {
					tmp = searchDB.getBSeatno(RMonDay, trainno, DStation, SStation, totalT);
				}else {
					tmp = searchDB.getSeatno(RMonDay, trainno, DStation, SStation, totalT);
				}
				
				if (tmp.equals("")) {
					Ravailable.remove(j);
					Rlength--;
				}
				else {
					Rseatnos.add(tmp);
					if(BorS == false) {
						if (Limitdate.before(ReCal)) {
							REDarray.add(searchDB.checkEarly(RMonDay, trainno, normalT));
						}
						else {
							REDarray.add(ttmp);
						}
					}
					else {
						REDarray.add(ttmp);
					}
				}
			}
		}

		//學生票處理
		
		
		//優待票處理
		
		//商務則沒有各種優待票
		
		if (BorS == false) {
			
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
							DUDdiscount.add(String.valueOf(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWD)));
							break;
						}
						else if (i+1 == UDTrains.length()) {
							//若都找不到則刪除此列次(不符合條件)
							Davailable.remove(j);
							DEDarray.remove(j);
							j--;
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
							//將該列車的於該星期的折扣放入RUDdiscount中
							RUDdiscount.add(String.valueOf(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWR)));
							break;
						}
						else if (i+1 == UDTrains.length()) {
							//若都找不到則維持原價add(1.0)
							Ravailable.remove(j);
							REDarray.remove(j);
							j--;
						}
						else;
					}
				}
				//搜尋班次的折價與以及是否還有位子
			}
			
			else;
			
			System.out.println("去程列車如下：\n");
			System.out.println("車次   | 早鳥優惠 | 大學生優惠 | 出發時間 | 抵達時間 |");
			
			Davareturn = new String[Davailable.length()][5];
			Ravareturn = new String[Ravailable.length()][5];
			
			for (int i = 0; i< Davailable.length();i++) {
				ArrayList<String> tmp = new ArrayList<String>();
				
				tmp.add(TrainNoofAv(Davailable,i));
				tmp.add(DEDarray.get(i).get(0).toString());
				tmp.add(DUDdiscount.get(i));
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
				
				tmp.add(Departuretime(SStation,timetable));
				tmp.add(Arrivetime   (DStation,timetable));
				
				Davareturn[i] = tmp.toArray(new String[5]);
				
				System.out.print(TrainNoofAv(Davailable,i) + " |");
				System.out.print(DEDarray.get(i).get(0).toString() + "折  |");
				if (studentT > 0) {
					System.out.print(" " + DUDdiscount.get(i) + "折  |");
				}
				else {
					System.out.print("    折  |");
				}
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				
				
				System.out.println("回程列車如下：\n");
				System.out.println("車次   | 早鳥優惠 | 大學生優惠 | 出發時間 | 抵達時間 |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					ArrayList<String> tmp = new ArrayList<String>();
					
					tmp.add(TrainNoofAv(Ravailable,j));
					tmp.add(REDarray.get(j).get(0).toString());
					tmp.add(RUDdiscount.get(j));
					
					JSONArray timetable = Ravailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
					
					tmp.add(Departuretime(DStation,timetable));
					tmp.add(Arrivetime   (SStation,timetable));
					
					Ravareturn[j] = tmp.toArray(new String[5]);
					
					System.out.print(TrainNoofAv(Ravailable,j) + " |");
					System.out.print(REDarray.get(j).get(0).toString() + "折  |");
					if (studentT > 0) {
						System.out.print(RUDdiscount.get(j) + "折  |");
					}
					else {
						System.out.print("    折  |");
					}
					
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
			
			this.BorS = true;
			ticketType = "business";
			//輸出搜尋結果
			
			System.out.println("去程列車如下：\n");
			System.out.println("0000  | 00:00 | 00:00 |");
			System.out.println("車次   | 出發時間 | 抵達時間 |");
			
			Davareturn = new String[Davailable.length()][5];
			Ravareturn = new String[Ravailable.length()][5];
			
			for (int i = 0; i< Davailable.length();i++) {
				ArrayList<String> tmp = new ArrayList<String>();
				
				tmp.add(TrainNoofAv(Davailable,i));
				tmp.add("");
				tmp.add("");
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
				
				tmp.add(Departuretime(SStation,timetable));
				tmp.add(Arrivetime   (DStation,timetable));
				
				Davareturn[i] = tmp.toArray(new String[5]);
				
				System.out.print(TrainNoofAv(Davailable,i) + " |");
								
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				System.out.println("回程列車如下：\n");
				System.out.println("車次   | 出發時間 | 抵達時間 |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					
					ArrayList<String> tmp = new ArrayList<String>();
					
					tmp.add(TrainNoofAv(Ravailable,j));
					tmp.add("");
					tmp.add("");
					
					JSONArray timetable = Ravailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
					
					tmp.add(Departuretime(DStation,timetable));
					tmp.add(Arrivetime   (SStation,timetable));
					
					Ravareturn[j] = tmp.toArray(new String[5]);
					
					System.out.print(TrainNoofAv(Ravailable,j) + " |");
					
					System.out.print("| " + Departuretime(DStation,timetable) + " |");
					System.out.print("| " + Arrivetime   (SStation,timetable) + " |");
					
					System.out.println();
					System.out.println();
				}
			}
			
			return "訂票搜尋結果顯示完畢";
		}
	}
	
	/**
	 * @return Davareturn
	 */
	public String[][] getDavareturn(){
		return Davareturn;
	}
	
	/**
	 * @return Ravareturn
	 */
	public String[][] getRavareturn(){
		return Ravareturn;
	}
	
	/**
	 * @param Dint 存入去程選擇 
	 * @param Rint 存入回程選擇 
	 * @return 
	 */
	
	public String[][] SearchSelect(int Dint, int Rint) {
		this.Dint = Dint;
		this.Rint = Rint;
		
		JSONArray Dtimetable = Davailable.getJSONObject(Dint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		JSONArray Rtimetable = Ravailable.getJSONObject(Rint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		
		
		
		ArrayList<String> gowayresult = new ArrayList<String>();
		ArrayList<String> backwayresult = new ArrayList<String>();
		
		//行程 日期 車次 起程站 到達站 出發時間 到達時間
		//去
		//回
		
		gowayresult.add("去程");
		gowayresult.add(Ddate.split(" ")[0]);
		gowayresult.add(TrainNoofAv(Dtimetable, Dint));
		gowayresult.add(SStation);
		gowayresult.add(DStation);
		gowayresult.add(Departuretime(SStation, Dtimetable));
		gowayresult.add(Arrivetime(DStation, Dtimetable));
		
		if(Rdate.equals("") == false) {
			backwayresult.add("回程");
			backwayresult.add(Rdate.split(" ")[0]);
			backwayresult.add(TrainNoofAv(Rtimetable, Rint));
			backwayresult.add(DStation);
			backwayresult.add(SStation);
			backwayresult.add(Departuretime(SStation, Rtimetable));
			backwayresult.add(Arrivetime(DStation, Rtimetable));
		}
		
		String[][] result = {gowayresult.toArray(new String[gowayresult.size()]) , backwayresult.toArray(new String[backwayresult.size()])};
		
		return result;
	}
	
	/**
	 * @param DTrainno
	 * @param RTrainno
	 * @param Ddate
	 * @param Rdate
	 * @param SStation
	 * @param DStation
	 * @param normalT
	 * @param concessionT
	 * @param studentT
	 * @param AorW
	 * @param BorS
	 * @return 二維列表 有兩行(去回) 每行的格式如右:行程 日期 車次 起程站 到達站 出發時間 到達時間 
	 * @throws BookingExceptions
	 */
	public String[][] TrainnoSearchSelect(String DTrainno,String RTrainno,
									String Ddate, String Rdate, // Ddate出發時間, Rdate返程時間
									String SStation, String DStation, //S始站, D終站
									int normalT, int concessionT, int studentT, //一般票, 優待票(5折), 大學生票
									int AorW, boolean BorS) throws BookingExceptions {
		try {
			this.Search(Ddate, Rdate, SStation, DStation, normalT, concessionT, studentT, AorW, BorS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		for(int d = 0;d < Davailable.length();d++) {
			if (DTrainno.equals(TrainNoofAv(Davailable, d))){
				this.Dint = d;
			}
		}
		
		if(RTrainno.equals("") == false) {
			for(int r = 0;r < Ravailable.length();r++) {
				if (RTrainno.equals(TrainNoofAv(Ravailable, r))){
					this.Rint = r;
				}
			}
		}
		
		JSONArray Dtimetable = Davailable.getJSONObject(Dint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		JSONArray Rtimetable = Ravailable.getJSONObject(Rint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		
		
		
		ArrayList<String> gowayresult = new ArrayList<String>();
		ArrayList<String> backwayresult = new ArrayList<String>();
		
		//行程 日期 車次 起程站 到達站 出發時間 到達時間
		//去
		//回
		
		gowayresult.add("去程");
		gowayresult.add(Ddate.split(" ")[0]);
		gowayresult.add(DTrainno);
		gowayresult.add(SStation);
		gowayresult.add(DStation);
		gowayresult.add(Departuretime(SStation, Dtimetable));
		gowayresult.add(Arrivetime(DStation, Dtimetable));
		
		if(Rdate.equals("") == false) {
			backwayresult.add("回程");
			backwayresult.add(Rdate.split(" ")[0]);
			backwayresult.add(RTrainno);
			backwayresult.add(DStation);
			backwayresult.add(SStation);
			backwayresult.add(Departuretime(SStation, Rtimetable));
			backwayresult.add(Arrivetime(DStation, Rtimetable));
		}
		
		String[][] result = {gowayresult.toArray(new String[gowayresult.size()]) , backwayresult.toArray(new String[backwayresult.size()])};
		
		return result;
	}
	
	
	public String Book(String uid) throws IOException, BookingExceptions {
		// size是零(沒有特價)就會直接忽略for迴圈
		ArrayList<Object> Dtmparraylist =  DEDarray.get(Dint);
		ArrayList<Object> Rtmparraylist =  REDarray.get(Rint);
		
		JSONArray Dtimetable = Davailable.getJSONObject(Dint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		JSONArray Rtimetable = Ravailable.getJSONObject(Rint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		
		String[] Dseats = Dseatnos.get(Dint).split(",");
		String[] Rseats = Rseatnos.get(Rint).split(",");
		
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
		
		for (int k = 0; k< Rseats.length; k++) {
			searchDB.setSeatno(RMonDay, Rtrain, DStation, SStation, Rseats[k]);
			
			System.out.print(Rseats[k] + ",");
		}
		
		System.out.println();
		
		if (Rtmparraylist.size() > 0) {
			searchDB.setED(RMonDay, Rtrain, Rtmparraylist);
		}
		
		//以下開始計算去程與回程總價
		String Dprice = "";
		
		Integer NormalP = 0;
		Integer StudentP = 0;
		Integer ConcessionP = 0;
			
		ArrayList<Object> DED = DEDarray.get(Dint);
		
		//全票與早鳥票
		if(DED.size() == 0) {
			NormalP = NormalP + this.foundprice(SStation, DStation, ticketType) * normalT;
		}else if(DED.size() == 2) {
			NormalP = NormalP + this.foundprice(SStation, DStation, (String.valueOf(DED.get(0))) ) * Integer.parseInt((String.valueOf(DED.get(1))) );
			NormalP = NormalP + this.foundprice(SStation, DStation, ticketType) * (normalT - (Integer) DED.get(1));
		}else if(DED.size() == 4) {
			NormalP = this.foundprice(SStation, DStation, (String) DED.get(0)) * (Integer) DED.get(1);
			NormalP = this.foundprice(SStation, DStation, (String) DED.get(2)) * (Integer) DED.get(3);
		}else {
			throw new BookingExceptions("執行訂票行為時發生去程列車產生錯誤");
		}
		
		//學生票
		if(studentT > 0) {
			StudentP = this.foundprice(SStation, DStation, DUDdiscount.get(Dint)) * studentT;
		}
		else;
		
		//優待票
		if(concessionT > 0) {
			ConcessionP = this.foundprice(SStation, DStation, "0.5") * concessionT;
		}
		else;
		
		Dprice = String.valueOf(NormalP + StudentP + ConcessionP); //去程票價
		
		String Rprice = "";
		
		if(Rint != -1) {
			
			NormalP = 0;
			StudentP = 0;
			ConcessionP = 0;
			
			ArrayList<Object> RED = DEDarray.get(Rint);
			
			//全票與早鳥票
			if(DED.size() == 0) {
				NormalP = NormalP + this.foundprice(DStation, SStation, ticketType) * normalT;
			}else if(DED.size() == 2) {
				NormalP = NormalP + this.foundprice(DStation, SStation, (String) RED.get(0)) * (Integer) RED.get(1);
				NormalP = NormalP + this.foundprice(DStation, SStation, ticketType) * (normalT - (Integer) RED.get(1));
			}else if(DED.size() == 4) {
				NormalP = this.foundprice(DStation, SStation, (String) RED.get(0)) * (Integer) RED.get(1);
				NormalP = this.foundprice(DStation, SStation, (String) RED.get(2)) * (Integer) RED.get(3);
			}else {
				throw new BookingExceptions("執行訂票行為時發生回程列車產生錯誤");
			}
			
			//學生票
			if(studentT > 0) {
				StudentP = this.foundprice(DStation, SStation, DUDdiscount.get(Rint)) * studentT;
			}
			else;
			
			//優待票
			if(concessionT > 0) {
				ConcessionP = this.foundprice(DStation, SStation, "0.5") * concessionT;
			}
			else;
			
			Rprice = String.valueOf(NormalP + StudentP + ConcessionP);
		}
		
		
		String Ddrivetime = getdrivetime(Departuretime(SStation, Dtimetable), Departuretime(DStation, Dtimetable));
		String Rdrivetime = getdrivetime(Departuretime(DStation, Rtimetable), Departuretime(SStation, Rtimetable));
		
		this.orderstore(uid, Ddate, Rdate, 
						ticketType, totalT, SStation, DStation, Dseats, Rseats,
						Dtrain, Rtrain, 
						Departuretime(SStation, Dtimetable), Arrivetime(DStation, Dtimetable),
						Departuretime(DStation, Rtimetable), Arrivetime(SStation, Rtimetable),
						Dprice, Rprice, Ddrivetime, Rdrivetime
						);
		
		return "訂票完成";
	}
	
	
	/**
	 * @param departuretime 出發時間
	 * @param arrivetime    抵達時間
	 * @return 行徑時間(格式: "hhmm")
	 */
	private String getdrivetime(String departuretime, String arrivetime) {
		departuretime = departuretime.replace(":", "");
		arrivetime    =    arrivetime.replace(":", "");
		
		int DH = Integer.parseInt(departuretime.substring(0,2));
		int DM = Integer.parseInt(departuretime.substring(2,4));
		
		int AH = Integer.parseInt(arrivetime.substring(0,2));
		int AM = Integer.parseInt(arrivetime.substring(2,4));
		
		if(DH == 0) {
			DH = 24;
		}
		if(AH == 0) {
			AH = 24;
		}
		
		if (AM - DM >= 0) {
			int H = AH - DH;
			int M = AM - DM;
			return String.format("%02d", H) +":"+ String.format("%02d", M);
		}else {
			int H = AH - DH - 1;
			int M = AM - DM + 60;
			return String.format("%02d", H) +":"+ String.format("%02d", M);
		}
	}

	private void orderstore(String uid, String DDate, String RDate,
							 String ticketType, int number, String SStation, String DStation, String[] Dseats, String[] Rseats,
							 String DTrainNo, String RTrainNo,
							 String Ddeparturetime, String Darraivetime, 
							 String Rdeparturetime, String Rarraivetime, 
							 String Dprice, String Rprice, String Ddrivetime, String Rdrivetime) {
		
//		for (int i = 0; i < goSeats.length; i++) {
//			JgoSeats.add(i, goSeats[i]);
//		}
		
		JSONArray booking = JSONUtils.getJSONArrayFromFile("/booking.json");
		
		JSONObject codegenerator = booking.getJSONObject(0);
		int seed = codegenerator.getInt("seed");
		JSONObject Seed = booking.getJSONObject(0);
		seed++;
		Seed.remove("seed");
		Seed.put("seed", seed);
		booking.put(0,Seed);
		
		JSONObject order = new JSONObject();
		JSONArray ticketInfo = new JSONArray();
		JSONObject goway = new JSONObject();
		JSONObject backway;
		
		int payment = Integer.parseInt(Dprice) + Integer.parseInt(Rprice);
		
		order.put("code", String.format("%09d", seed));
		order.put("uid", uid);
		order.put("payment", payment);
		//to.put("CarType", carType);
		
		ArrayList<String> DtTypes = new ArrayList<String>();
		ArrayList<String> RtTypes = new ArrayList<String>();
		
		for(int d =0; d< normalT;d++) {
			DtTypes.add("normal");
		}
		for(int d =0; d< concessionT;d++) {
			DtTypes.add("concession");
		}
		for(int d =0; d< studentT;d++) {
			DtTypes.add("student");
		}
		
		String[] DticketTypes = DtTypes.toArray(new String[DtTypes.size()]);
		
		DDate = DDate.split(" ")[0];
		RDate = RDate.split(" ")[0];
		
		goway.put("DTrainNo", DTrainNo);
		goway.put("date", DDate);
		goway.put("carType", ticketType);
		goway.put("ticketsCount", number);
		goway.put("ticketsTypes", DticketTypes);
		goway.put("start", SStation);
		goway.put("end", DStation);
		goway.put("seats", Dseats);
		goway.put("departure time", Ddeparturetime);
		goway.put("arrival time", Darraivetime);
		goway.put("price", Dprice);
		goway.put("driving time", Ddrivetime);
		
		ticketInfo.put(goway);
		
		if (RDate.equals("") ) {
			
		}else {
			for(int d =0; d< normalT;d++) {
				RtTypes.add("normal");
			}
			for(int d =0; d< concessionT;d++) {
				RtTypes.add("concession");
			}
			for(int d =0; d< studentT;d++) {
				RtTypes.add("student");
			}
			
			String[] RticketTypes = RtTypes.toArray(new String[RtTypes.size()]);
			
			backway = new JSONObject();
			
			backway.put("RTrainNo", RTrainNo);
			backway.put("date", RDate);
			backway.put("carType", ticketType);
			backway.put("ticketsCount", number);
			backway.put("ticketsTypes", RticketTypes);
			backway.put("start", DStation); //倒過來放
			backway.put("end", SStation);   //
			backway.put("seats", Rseats);
			backway.put("departure time", Rdeparturetime);
			backway.put("arrival time", Rarraivetime);
			backway.put("price", Rprice);
			backway.put("driving time", Rdrivetime);
			
			ticketInfo.put(backway);
		}
		
		order.put("ticketInfo", ticketInfo);
		
		booking.put(order);
		
		try {
			BufferedWriter BW = new BufferedWriter(new FileWriter("Data/booking.json"));
			BW.write(booking.toString());
			BW.flush();
			BW.close();
		} catch (IOException e) {
			System.out.println("IOException occurs");
		}
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
	
	/**
	 * @param SStation 去程起站
	 * @param DStation 去程終站
	 * @param TicketType 票種
	 * @return 回傳票價 (int type)
	 */
	private int foundprice(String SStation, String DStation, String TicketType) {
		JSONArray priceTable = JSONUtils.getJSONArrayFromFile("/price.json");
		for (int i = 0; i < priceTable.length(); i++) {
			if(priceTable.getJSONObject(i).getString("OriginStationID").equals(SStation)){
				JSONArray array = priceTable.getJSONObject(i).getJSONArray("DesrinationStations");
				for (int j = 0; j < array.length(); j++) {
					if(array.getJSONObject(j).getString("ID").equals(DStation)) {
						JSONArray fares = array.getJSONObject(j).getJSONArray("Fares");
						for(int k = 0; k< fares.length();k++){
							if(fares.getJSONObject(k).getString("TicketType").equals(TicketType)) {
								return fares.getJSONObject(k).getInt("Price");
							}
						}
					}
				}
			}
		}
		return 0;
	}
}
