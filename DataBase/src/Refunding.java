import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Refunding {
private String start,trainNumber,code,Date;
private String []seats;
private String time;
private int hour,min;
private int number,remainPeople;



	JSONArray arrayofbooking = JSONUtils.getJSONArrayFromFile("/booking.json");
	JSONArray obj = JSONUtils.getJSONArrayFromFile("/timeTable.json");

	public String Refund (String UID, String ticketCode , int people) throws IOException {
		for (int i = 0; i < arrayofbooking.length(); i++) {
			JSONObject date = arrayofbooking.getJSONObject(i);
			String temporcode = date.getString("code");
			if (temporcode.contentEquals(ticketCode)) {
				number = i;
				code = temporcode;
				JSONArray ticketInfo = date.getJSONArray("ticketInfo");
				start = ticketInfo.getJSONObject(0).getString("start");
				trainNumber = ticketInfo.getJSONObject(0).getString("TrainNo");
				Date = ticketInfo.getJSONObject(0).getString("date");
				seats = new String[ticketInfo.getJSONObject(0).getJSONArray("seats").length()];
				for (int m = 0; m <ticketInfo.getJSONObject(0).getJSONArray("seats").length(); m++) {
					seats[m] = ticketInfo.getJSONObject(0).getJSONArray("seats").getString(m);
					}
				//get seats��array��K����R����l
				}
		}
		//booking variables : code , start , train number , Date , seats array
		
		//timetable.json
		for(int j = 0; j < obj.length(); j++) {	
		    JSONObject train = obj.getJSONObject(j);
		    JSONObject timetable = train.getJSONObject("GeneralTimetable");
		    String trainNo = timetable.getJSONObject("GeneralTrainInfo").getString("TrainNo");
		    if (trainNo.contentEquals(trainNumber)) {
		    	JSONArray stopTimes = timetable.getJSONArray("StopTimes");
		    	for(int n = 0; n <stopTimes.length(); n++) {
		    		String stationID = stopTimes.getJSONObject(n).getString("StationID");
		    		if (stationID.contentEquals(start)) {
		    			time = stopTimes.getJSONObject(n).getString("DepartureTime");
		    		}
		    	}
		    }
	    }
		//�o�Ӥj�j�骺�ت��O�A���btimeTable�̭����ڭn��trainNo��A�A�h��_��������stationID
		//������A��departuretime�s��time�A���Ftime����A�᭱�N�i�H�B�z�b�p�ɰh�������D
		//variables : time
		
		Date currentDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM:dd:hh:mm"); 
		String [] today = df.format(currentDate).split(":");//[0] = month ,[1] = day ,[2] = hour ,[3] = minute
		
		String []DateArray = Date.split("-");//DateArray[1],[2]�i�H���Ӥ�����
		
		String[]DepartureTime = time.split(":");//hour:min
		if (Integer.parseInt(DepartureTime[1]) > 30) {
			hour = Integer.parseInt(DepartureTime[0]);
			min = Integer.parseInt(DepartureTime[1]) - 30;
		}
		else {
			hour = Integer.parseInt(DepartureTime[0]) - 1 ;
			min = Integer.parseInt(DepartureTime[1]) + 30;
		}
		
		if (code.contentEquals(ticketCode)) {
			if(Integer.parseInt(DateArray[1]) >= Integer.parseInt(today[0]) && Integer.parseInt(DateArray[2]) >= Integer.parseInt(today[1])) {
				if((Integer.parseInt(DateArray[1]) >= Integer.parseInt(today[0]) && Integer.parseInt(DateArray[2]) >= Integer.parseInt(today[1]))
						|| (hour >= Integer.parseInt(today[2]) && min >= Integer.parseInt(today[3]))) {
					if (seats.length == people) {
						DeleteTicket("Data/booking.json","booking.json",number);		
						return "�h�����\�A�w�����z���q�����";
					}
					else {
						remainPeople = seats.length - people;
						ConditionRefund("Data/booking.json","booking.json",number,remainPeople);
						return "�ק令�\�A�w�N�z�H���ܧ�" + remainPeople + "��";
					}
				}
				else
					return "�h��/�ק異�ѡA�ݩ���w�����}���e�b�p��";
			}
			else
				return "�h��/�ק異�ѡA�ݩ���w�����}���e�b�p��";
				
			}
		else
			return "�h��/�ק異�ѡA���q��N�����s�b";
					
		
	}
	private void DeleteTicket(String writer , String filelocation , int number) throws IOException {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(writer));			
			
			JSONArray dataJSON = JSONUtils.getJSONArrayFromFile(filelocation);      
			dataJSON.remove(number);														
			String ws = dataJSON.toString();		
			bw.write(ws);
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ConditionRefund(String writer , String filelocation , int number , int remainPeople) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(writer));
		
		JSONArray dataJSON = JSONUtils.getJSONArrayFromFile(filelocation);
		String ticketsCount = "ticketsCount";
		String s = "seats";
		
		JSONArray ticketInfo = dataJSON.getJSONObject(number).getJSONArray("ticketInfo");
		for ( int i = 0 ; i < ticketInfo.length() ; i++ ) {
			ticketInfo.getJSONObject(i).remove(ticketsCount);
			ticketInfo.getJSONObject(i).put(ticketsCount,remainPeople);
			ticketInfo.getJSONObject(i).getJSONArray(s).clear();
			for (int j = 0; j < remainPeople; j++) {
				ticketInfo.getJSONObject(i).getJSONArray(s).put(j,seats[j]);    //�q�᭱�}�l�R
			}
		}
		String ws = dataJSON.toString();
		bw.write(ws);
		bw.flush();
		bw.close();
	}
	
	public static void main(String []arg) throws IOException {
		Refunding r = new Refunding();
		System.out.println(r.Refund("A123456789", "123456789", 1));
	}

}
