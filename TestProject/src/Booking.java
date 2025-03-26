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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmm"); //������J����榡
	
	private int Direction; //�h�{��V
	
	private JSONArray Davailable = new JSONArray(); //�h�^�{�C��JSONArray
	private JSONArray Ravailable = new JSONArray();
	
	private ArrayList<String> Dseatnos = new ArrayList<String>(); //�h�^�{ �����W��JSONArray�ĴX�ӦC�� �ӦC�����t������l
	private ArrayList<String> Rseatnos = new ArrayList<String>();
	
	CSVFile builder = new CSVFile(); //���ɪ�caller
	
	private ArrayList<ArrayList<Object>> DEDarray = new ArrayList<ArrayList<Object>>(); //�������S�� �ӦC�� �榡: �馩 ��������
	private ArrayList<ArrayList<Object>> REDarray = new ArrayList<ArrayList<Object>>();
	
	private ArrayList<String> DUDdiscount = new ArrayList<String>(); //�j�ǥͯS�� �ӦC���N�O���ӻ���
	private ArrayList<String> RUDdiscount = new ArrayList<String>();
	
	SimpleDateFormat OPformat = new SimpleDateFormat("yyyy-MM-dd"); //outputformat
	
	//�H�U����search��trainnosearch���G���x�s�a��(�j�M���n�������άO���w������)
	
	private int Dint = -1; //����search���襤��(�C�����ĴX��)
	private int Rint = -1;
	
	private String Ddate = ""; //date�Φ�
	private String Rdate = ""; 
	
	private String DMonDay = ""; //MONDAY�Φ�(��KŪ�ɮ�)
	private String RMonDay = "";
	
	private String SStation = ""; //�_�ׯ�
	private String DStation = "";
	
	private String ticketType = "standard"; //����(�馩)
	
	boolean BorS = false;
	
	int totalT = 0;
	int normalT = 0;
	int concessionT = 0;
	int studentT = 0;
	
	String[][] Davareturn;
	String[][] Ravareturn;
	
	public String Search(String Ddate, String Rdate, // Ddate�X�o�ɶ�, Rdate��{�ɶ�
			String SStation, String DStation, //S�l��, D�ׯ�
			int normalT, int concessionT, int studentT, //�@�벼, �u�ݲ�(5��), �j�ǥͲ�
			int AorW, boolean BorS) // ���Dor�a��(0�S�n�D1�a��2���D), true�Ӱ� false�з� ���[
			throws IOException, BookingExceptions
	{
		
		this.SStation = SStation; 
		this.DStation = DStation;
		
		//�ˬd���Ʀ��S���W�L		
		
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
			throw new BookingExceptions("�`�N�G�q��w�w�L�h����(�C���̦h10�i�A�Ӧ^�����W�߭p��)");
		}
		
		//�B�z��V
		this.trainDirection(SStation, DStation);
		
		//�B�z�ɶ�

		//���Ѯɶ�
		long current = System.currentTimeMillis();
		Date ttoday = new Date(current);
		Calendar today = Calendar.getInstance();
		today.setTime(ttoday);
	
		//�h�{
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
		
		//����
		try {
			builder.createFile(Dedate);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
		//�^�{
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
		
		//����
		try {
			builder.createFile(Redate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		/*
		 * ���ѹw�q���Υ���28��H���������C
		 * �q��}��ɶ���������(�t)�e28����0�I�}�l�A
		 * ��騮�����w�q�Ȩ��z�ܦC���X�o�ɶ��e1�p�ɬ���
		 */
		
		//������� (����᪺28��)
		Calendar Limitdate = today;
		Limitdate.add(Calendar.DAY_OF_MONTH, 28);

		//�T�{���ɶ��O�_�i�ѭq��
		if(Limitdate.before(DeCal)) {
			if(Limitdate.before(ReCal)) {
				throw new BookingExceptions("�`�N�G�h�^�{�C������ҩ|���}��q��");
			}
			else {
				throw new BookingExceptions("�`�N�G�h�{�C������ҩ|���}��q��");
			}
		}
		else {
			//�L���D
		}

		//�N�ŦX���󪺦C���s����JSONObject ��JJSONArray Davailable �P Ravailable
		
		//�`�N�o���٨S�T�{���S���y��
		
		/*    �T�{���Ǭ��G
		 * 1. �T�{��V Direction
		 * 2. �T�{�P�� DayofWeek
		 * 3. �T�{�O�_�ŦX���u(���L��F�l���P�ׯ�) Stations
		 * 4. �T�{�l���X�o�ɶ� Date
		 */
		
		for(int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject train = jsonArray.getJSONObject(i);
			JSONObject timetable = train.getJSONObject("GeneralTimetable");
			
			//�h�{
			if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") == Direction)
				//�T�{��V
				&& (timetable.getJSONObject("ServiceDay").getInt(DoWD) == 1) 
				//�T�{�P��
				&& (trainroutehas(train, SStation, DStation)) 
				//�T�{���u
				&& (dparturetime(Dtime, SStation, timetable.getJSONArray("StopTimes")))) 
				//�T�{�X�o�ɶ�
			{
				Davailable.put(train);
			}
			
			//�^�{
			if (Rdate != "") {
				//�T�{�O�_���^�{
				if ((timetable.getJSONObject("GeneralTrainInfo").getInt("Direction") != Direction)
					//�T�{��V
					&& (timetable.getJSONObject("ServiceDay").getInt(DoWR) == 1) 
					//�T�{�P��
					&& (trainroutehas(train, SStation, DStation)) 
					//�T�{���u
					&& (dparturetime(Rtime, DStation, timetable.getJSONArray("StopTimes")))) 
					//�T�{�X�o�ɶ�
				{
					Ravailable.put(train);
				}
			}
		}
		
		//�B�z���q���D �P ���������D
		
		SimpleDateFormat format =new SimpleDateFormat("MMdd");
	    String DMonDay = format.format(Dedate);
		String RMonDay = format.format(Redate);
		
		this.DMonDay = DMonDay;
		this.RMonDay = RMonDay;
	    
		int Dlength = Davailable.length();
		int Rlength = Ravailable.length();
		
		Dseatnos = new ArrayList<String>();
		Rseatnos = new ArrayList<String>();
		
		//��Llimitdate�אּ����᤭��(28-23) 
		
		Limitdate.add(Calendar.DAY_OF_MONTH, -23);
		
		//�h�{ //�T�{�O�_�󤭤�e 
		
		if (AorW != 0 && totalT != 1) {
			throw new BookingExceptions("�`�N�G�����H�ƶW�L�@�H�ɵL�k�ϥΰ��n�y��\��C");
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

		//�ǥͲ��B�z
		
		
		//�u�ݲ��B�z
		
		//�Ӱȫh�S���U���u�ݲ�
		
		if (BorS == false) {
			
		//�j�ǥͲ� (�u���馩)
		/*
		 * �j�ǥ��u�f�]5��/75��/88��^�����L�k�P��L�u�f�X�֨ϥΡC
		 */
			if (studentT > 0) {
				
				JSONObject universityDiscount = JSONUtils.getJSONObjectFromFile("/universityDiscount.json");
				JSONArray UDTrains = universityDiscount.getJSONArray("DiscountTrains");
				//studentT				
				
				//�~�鬰�h�{��JSONArray
				for(int j = 0; j < Davailable.length(); j++) {
					//����1���Ҧ�ED��JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//�Y���������C��

						if (TrainNoof(UDTrains, i).equals(TrainNoofAv(Davailable, j))) {
							//�N�ӦC������ӬP�����馩��JDUDdiscount��
							DUDdiscount.add(String.valueOf(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWD)));
							break;
						}
						else if (i+1 == UDTrains.length()) {
							//�Y���䤣��h�R�����C��(���ŦX����)
							Davailable.remove(j);
							DEDarray.remove(j);
							j--;
						}
						else;
					}
				}
				
				//�~�鬰�h�{��JSONArray
				for(int j = 0; j < Ravailable.length(); j++) {
					//����1���Ҧ�ED��JSONArray
					for(int i = 0; i < UDTrains.length(); i++) {
						//�Y���������C��
						if (TrainNoof(UDTrains, i).equals(TrainNoofAv(Ravailable, j))) {
							//�N�ӦC������ӬP�����馩��JRUDdiscount��
							RUDdiscount.add(String.valueOf(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWR)));
							break;
						}
						else if (i+1 == UDTrains.length()) {
							//�Y���䤣��h�������add(1.0)
							Ravailable.remove(j);
							REDarray.remove(j);
							j--;
						}
						else;
					}
				}
				//�j�M�Z��������P�H�άO�_�٦���l
			}
			
			else;
			
			System.out.println("�h�{�C���p�U�G\n");
			System.out.println("����   | �����u�f | �j�ǥ��u�f | �X�o�ɶ� | ��F�ɶ� |");
			
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
				System.out.print(DEDarray.get(i).get(0).toString() + "��  |");
				if (studentT > 0) {
					System.out.print(" " + DUDdiscount.get(i) + "��  |");
				}
				else {
					System.out.print("    ��  |");
				}
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				
				
				System.out.println("�^�{�C���p�U�G\n");
				System.out.println("����   | �����u�f | �j�ǥ��u�f | �X�o�ɶ� | ��F�ɶ� |");
				
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
					System.out.print(REDarray.get(j).get(0).toString() + "��  |");
					if (studentT > 0) {
						System.out.print(RUDdiscount.get(j) + "��  |");
					}
					else {
						System.out.print("    ��  |");
					}
					
					System.out.print(" " + Departuretime(DStation,timetable) + " |");
					System.out.print(" " + Arrivetime   (SStation,timetable) + " |");
					
					System.out.println();
					System.out.println();
				}
			}
						
			return "�q���j�M���G��ܧ���";
		}
		
		// END -----> ��X�ӰȨ��[����������B�óB�z����(����excel�y���ɮ�)
		else {
			
			this.BorS = true;
			ticketType = "business";
			//��X�j�M���G
			
			System.out.println("�h�{�C���p�U�G\n");
			System.out.println("0000  | 00:00 | 00:00 |");
			System.out.println("����   | �X�o�ɶ� | ��F�ɶ� |");
			
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
				System.out.println("�^�{�C���p�U�G\n");
				System.out.println("����   | �X�o�ɶ� | ��F�ɶ� |");
				
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
			
			return "�q���j�M���G��ܧ���";
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
	 * @param Dint �s�J�h�{��� 
	 * @param Rint �s�J�^�{��� 
	 * @return 
	 */
	
	public String[][] SearchSelect(int Dint, int Rint) {
		this.Dint = Dint;
		this.Rint = Rint;
		
		JSONArray Dtimetable = Davailable.getJSONObject(Dint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		JSONArray Rtimetable = Ravailable.getJSONObject(Rint).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
		
		
		
		ArrayList<String> gowayresult = new ArrayList<String>();
		ArrayList<String> backwayresult = new ArrayList<String>();
		
		//��{ ��� ���� �_�{�� ��F�� �X�o�ɶ� ��F�ɶ�
		//�h
		//�^
		
		gowayresult.add("�h�{");
		gowayresult.add(Ddate.split(" ")[0]);
		gowayresult.add(TrainNoofAv(Dtimetable, Dint));
		gowayresult.add(SStation);
		gowayresult.add(DStation);
		gowayresult.add(Departuretime(SStation, Dtimetable));
		gowayresult.add(Arrivetime(DStation, Dtimetable));
		
		if(Rdate.equals("") == false) {
			backwayresult.add("�^�{");
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
	 * @return �G���C�� �����(�h�^) �C�檺�榡�p�k:��{ ��� ���� �_�{�� ��F�� �X�o�ɶ� ��F�ɶ� 
	 * @throws BookingExceptions
	 */
	public String[][] TrainnoSearchSelect(String DTrainno,String RTrainno,
									String Ddate, String Rdate, // Ddate�X�o�ɶ�, Rdate��{�ɶ�
									String SStation, String DStation, //S�l��, D�ׯ�
									int normalT, int concessionT, int studentT, //�@�벼, �u�ݲ�(5��), �j�ǥͲ�
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
		
		//��{ ��� ���� �_�{�� ��F�� �X�o�ɶ� ��F�ɶ�
		//�h
		//�^
		
		gowayresult.add("�h�{");
		gowayresult.add(Ddate.split(" ")[0]);
		gowayresult.add(DTrainno);
		gowayresult.add(SStation);
		gowayresult.add(DStation);
		gowayresult.add(Departuretime(SStation, Dtimetable));
		gowayresult.add(Arrivetime(DStation, Dtimetable));
		
		if(Rdate.equals("") == false) {
			backwayresult.add("�^�{");
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
		// size�O�s(�S���S��)�N�|��������for�j��
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
		
		//�H�U�}�l�p��h�{�P�^�{�`��
		String Dprice = "";
		
		Integer NormalP = 0;
		Integer StudentP = 0;
		Integer ConcessionP = 0;
			
		ArrayList<Object> DED = DEDarray.get(Dint);
		
		//�����P������
		if(DED.size() == 0) {
			NormalP = NormalP + this.foundprice(SStation, DStation, ticketType) * normalT;
		}else if(DED.size() == 2) {
			NormalP = NormalP + this.foundprice(SStation, DStation, (String.valueOf(DED.get(0))) ) * Integer.parseInt((String.valueOf(DED.get(1))) );
			NormalP = NormalP + this.foundprice(SStation, DStation, ticketType) * (normalT - (Integer) DED.get(1));
		}else if(DED.size() == 4) {
			NormalP = this.foundprice(SStation, DStation, (String) DED.get(0)) * (Integer) DED.get(1);
			NormalP = this.foundprice(SStation, DStation, (String) DED.get(2)) * (Integer) DED.get(3);
		}else {
			throw new BookingExceptions("����q���欰�ɵo�ͥh�{�C�����Ϳ��~");
		}
		
		//�ǥͲ�
		if(studentT > 0) {
			StudentP = this.foundprice(SStation, DStation, DUDdiscount.get(Dint)) * studentT;
		}
		else;
		
		//�u�ݲ�
		if(concessionT > 0) {
			ConcessionP = this.foundprice(SStation, DStation, "0.5") * concessionT;
		}
		else;
		
		Dprice = String.valueOf(NormalP + StudentP + ConcessionP); //�h�{����
		
		String Rprice = "";
		
		if(Rint != -1) {
			
			NormalP = 0;
			StudentP = 0;
			ConcessionP = 0;
			
			ArrayList<Object> RED = DEDarray.get(Rint);
			
			//�����P������
			if(DED.size() == 0) {
				NormalP = NormalP + this.foundprice(DStation, SStation, ticketType) * normalT;
			}else if(DED.size() == 2) {
				NormalP = NormalP + this.foundprice(DStation, SStation, (String) RED.get(0)) * (Integer) RED.get(1);
				NormalP = NormalP + this.foundprice(DStation, SStation, ticketType) * (normalT - (Integer) RED.get(1));
			}else if(DED.size() == 4) {
				NormalP = this.foundprice(DStation, SStation, (String) RED.get(0)) * (Integer) RED.get(1);
				NormalP = this.foundprice(DStation, SStation, (String) RED.get(2)) * (Integer) RED.get(3);
			}else {
				throw new BookingExceptions("����q���欰�ɵo�ͦ^�{�C�����Ϳ��~");
			}
			
			//�ǥͲ�
			if(studentT > 0) {
				StudentP = this.foundprice(DStation, SStation, DUDdiscount.get(Rint)) * studentT;
			}
			else;
			
			//�u�ݲ�
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
		
		return "�q������";
	}
	
	
	/**
	 * @param departuretime �X�o�ɶ�
	 * @param arrivetime    ��F�ɶ�
	 * @return ��|�ɶ�(�榡: "hhmm")
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
			backway.put("start", DStation); //�˹L�ө�
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
	 * ��method�ΨӽT�{�C��(�h�{ �p�G���Ӧ^����)��|��V
	 * @param sStation
	 * @param dStation
	 */
	
	private void trainDirection(String sStation, String dStation) {
		if (Integer.valueOf(sStation) < Integer.valueOf(dStation)) {
			Direction = 0; //�n�V
		}
		else{
			Direction = 1; //�_�V
		}
	}


	/**
	 * ��method��K��davailable ArrayList����TrainNo
	 * 
	 * @param Ravailable
	 * @param which �ĴX��
	 * @return �Ӧ�m��TrainNo
	 */
	
	public static String TrainNoofAv(JSONArray Ravailable, int which) {
		return Ravailable.getJSONObject(which).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo");
	}
	
	/**
	 * ��method��K��dDiscount table����TrainNo
	 * 
	 * @param DTrains discount trains
	 * @param which �ĴX��
	 * @return �Ӧ�m��TrainNo
	 */
	
	public static String TrainNoof(JSONArray EDTrains, int which) {
		return EDTrains.getJSONObject(which).getString("TrainNo");
	}
	
	/**
	 * @param time ��J�ɶ�
	 * @param SStation �_��
	 * @param StopTimes �ӦC��������
	 * @return �Y�ӦC���ӯ����X���ɶ� �b ��J�ɶ� �� �h�^��true �Ϥ��^��false
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
	 * @return ��XStopTimes�̸ӯ����X�o�ɶ�
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
	 * @return ��XStopTimes�̸ӯ����X�o�ɶ�(�]�d�ߤ���ڭ̱N����@��F�ɶ�)
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
	 * @param train �ӦC����JSONobject
	 * @param SStation �l��
	 * @param DStation �ׯ�
	 * @return true �Y���u���T false �Ϥ�
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
	 * @return �Ӥ�����P��
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
	 * @param SStation �h�{�_��
	 * @param DStation �h�{�ׯ�
	 * @param TicketType ����
	 * @return �^�ǲ��� (int type)
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
