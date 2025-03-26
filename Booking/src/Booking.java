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
	
	JSONArray Davailable = new JSONArray(); //�h�^�{�C��JSONArray
	JSONArray Ravailable = new JSONArray();
	
	ArrayList<String> Dseatno; //�h�^�{ �����W��JSONArray�ĴX�ӦC�� �ӦC�����t������l
	ArrayList<String> Rseatno; 
	
	CSVFile builder = new CSVFile(); //���ɪ�caller
	
	ArrayList<ArrayList<Object>> DEDarray = new ArrayList<ArrayList<Object>>(); //�������S��
	ArrayList<ArrayList<Object>> REDarray = new ArrayList<ArrayList<Object>>();
	
	ArrayList<Double> DUDdiscount = new ArrayList<Double>(); //�j�ǥͯS��
	ArrayList<Double> RUDdiscount = new ArrayList<Double>();
	
	String DMonDay = "";
	String RMonDay = "";
	
	String SStation = ""; 
	String DStation = "";
	
	public String Search(String Ddate, String Rdate, // Ddate�X�o�ɶ�, Rdate��{�ɶ�
			String SStation, String DStation, //S�l��, D�ׯ�
			int normalT, int concessionT, int studentT, //�@�벼, �u�ݲ�(5��), �j�ǥͲ�
			int AorW, boolean BorS) throws IOException // ���Dor�a��(0�S�n�D1�a��2���D), true�Ӱ� false�з� ���[
	{
		
		this.SStation = SStation; 
		this.DStation = DStation;
		
		//�ˬd���Ʀ��S���W�L		
		int totalT = normalT+concessionT+studentT;
		
		if ((totalT > 10) || ((Rdate != "")&&(totalT > 5))) {
			return "���ѡA�]�q��w�w�L�h����(�C���̦h10�i�A�Ӧ^�����W�߭p��)";
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
				return "�h�^�{�C���ҩ|���}��q��";
			}
			else {
				return "�h�{�C���ҩ|���}��q��";
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
		
		Dseatno = new ArrayList<String>();
		Rseatno = new ArrayList<String>();
		
		//��Llimitdate�אּ����᤭��(28-23) 
		
		Limitdate.add(Calendar.DAY_OF_MONTH, -23);
		 
		//�h�{ //�T�{�O�_�󤭤�e 
		

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
		//�ǥͲ��B�z
		
		
		//�u�ݲ��B�z
		
		//�Ӱȫh�S���U���u�ݲ�
		if (BorS == false) {
			
//---------------------------------------------�ѤU������--------------------------------------------------------
			
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
							DUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWD));
						}
						else if (i+1 == UDTrains.length()) {
							//�Y���䤣��h�������add(1.0)
							DUDdiscount.add(1.0);
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
							//�N�ӦC������ӬP�����馩��JDUDdiscount��
							RUDdiscount.add(UDTrains.getJSONObject(i).getJSONObject("ServiceDayDiscount").getDouble(DoWR));
						}
						else if (i+1 == UDTrains.length()) {
							//�Y���䤣��h�������add(1.0)
							RUDdiscount.add(1.0);
						}
						else;
					}
				}
				//�j�M�Z��������P�H�άO�_�٦���l
			}
			
			else;
			
		//�u�ݲ� (�U������)
		
		//�㨮���`��(���@��P�Ӱ�)
			
			//��X�j�M���G
		//1.
		// ������ܨ���
			// �h�{�G�_���ׯ� ���(�P��)
			// ��ܫ��s ���� �����u�f �X�o�ɶ� ��F�ɶ� �樮�ɶ�
			// 
			// �^�{�G�_���ׯ� ���(�P��)
			// ��ܫ��s ���� �����u�f �X�o�ɶ� ��F�ɶ� �樮�ɶ�
			// 
			// ���[:�з�/�Ӱ� ���ơG�����X�i|�R�߲��X�i|�q�Ѳ��X�i
			//
			// ���s�d��                         �T�{����
		//2.
		// �A�ӽT�{��
			// ��{ ��� ���� �_�� �ׯ� �X�o ��F ����(�t����) ��L���ؼƶq(�t����) �p�p��
			// �h
			// �^
			// ���[ ���� �`����
		// �����H��T
			// �ѧO�X(�����ҩ��@�Ӹ��X) ���n
			// �q�� email��
		// �����q��
			
			System.out.println("�h�{�C���p�U�G\n");
			System.out.println("����   | �����u�f | �j�ǥ��u�f | �X�o�ɶ� | ��F�ɶ� |");
			
			for (int i = 0; i< Davailable.length();i++) {
				System.out.print(TrainNoofAv(Davailable,i) + " |");
				System.out.print(DEDarray.get(i).get(0).toString() + "��  |");
				if (studentT > 0) {
					System.out.print(" " + DUDdiscount.get(i) + "��  |");
				}
				else {
					System.out.print("    ��  |");
				}
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				System.out.println("�^�{�C���p�U�G\n");
				System.out.println("����   | �����u�f | �j�ǥ��u�f | �X�o�ɶ� | ��F�ɶ� |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.print(TrainNoofAv(Ravailable,j) + " |");
					System.out.print(REDarray.get(j).get(0).toString() + "��  |");
					if (studentT > 0) {
						System.out.print(RUDdiscount.get(j) + "��  |");
					}
					else {
						System.out.print("    ��  |");
					}

					JSONArray timetable = Ravailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONArray("StopTimes");
					
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
			//��X�j�M���G
			
			System.out.println("�h�{�C���p�U�G\n");
			System.out.println("0000  | 00:00 | 00:00 |");
			System.out.println("����   | �X�o�ɶ� | ��F�ɶ� |");
			
			for (int i = 0; i< Davailable.length();i++) {
				System.out.print(TrainNoofAv(Davailable,i) + " |");
				
				JSONArray timetable = Davailable.getJSONObject(i).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONArray("StopTimes");
				
				System.out.print(" " + Departuretime(SStation,timetable) + " |");
				System.out.print(" " + Arrivetime   (DStation,timetable) + " |");
				
				System.out.println();
				System.out.println();
			}
			
			if (Rdate != "") {
				System.out.println("�^�{�C���p�U�G\n");
				System.out.println("����   | �X�o�ɶ� | ��F�ɶ� |");
				
				for (int j = 0; j < Ravailable.length(); j++) {
					System.out.print(TrainNoofAv(Ravailable,j) + " |");

					JSONArray timetable = Davailable.getJSONObject(j).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONArray("StopTimes");
					
					System.out.print("| " + Departuretime(DStation,timetable) + " |");
					System.out.print("| " + Arrivetime   (SStation,timetable) + " |");
					
					System.out.println();
					System.out.println();
				}
			}
			
			return "�q���j�M���G��ܧ���";
		}
	}
	
	public String Book(int Dint, int Rint) throws IOException {
		// size�O�s(�S���S��)�N�|��������for�j��
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
		
		return "�q������";
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
}
