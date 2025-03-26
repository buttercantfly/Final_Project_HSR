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
private String start,trainNumber,Date;
private String code = null;
private String uid = null;
private String []seats;
private String []DateArray = null;
private String []DepartureTime = null;
private String time;
private int hour,min;
private int number,remainPeople;



	JSONArray arrayofbooking = JSONUtils.getJSONArrayFromFile("/booking.json");
	JSONArray obj = JSONUtils.getJSONArrayFromFile("/timeTable.json");

	public String Refund (String UID, String ticketCode , int people) throws IOException {
		for (int i = 1; i < arrayofbooking.length(); i++) {
			JSONObject date = arrayofbooking.getJSONObject(i);
			String temporcode = date.getString("code");		
			
			if (UID.contentEquals(date.getString("uid"))) {
				if (temporcode.contentEquals(ticketCode)) {
					uid = date.getString("uid");
					number = i;
					code = temporcode;
					JSONArray ticketInfo = date.getJSONArray("ticketInfo");
					start = ticketInfo.getJSONObject(0).getString("start");
					trainNumber = ticketInfo.getJSONObject(0).getString("DTrainNo");
					System.out.println(trainNumber);
					Date = ticketInfo.getJSONObject(0).getString("date");
					seats = new String[ticketInfo.getJSONObject(0).getJSONArray("seats").length()];
					for (int m = 0; m <ticketInfo.getJSONObject(0).getJSONArray("seats").length(); m++) {
						seats[m] = ticketInfo.getJSONObject(0).getJSONArray("seats").getString(m);
						System.out.println(seats[m]);
						}
					//get seats的array方便之後刪掉位子
					}
			}
			
		}
		//booking variables : code , start , train number , Date , seats array
		
		//timetable.json
		if (uid != null) {
			if (code != null) {
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
			}
		}
		
		
		//這個大迴圈的目的是，先在timeTable裡面找到我要的trainNo後，再去找起站對應的stationID
		//都找到後，把departuretime存給time，有了time之後，後面就可以處理半小時退票的問題
		//variables : time
		
		Date currentDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM:dd:hh:mm"); 
		String [] today = df.format(currentDate).split(":");//[0] = month ,[1] = day ,[2] = hour ,[3] = minute
		if (uid != null) {
			if (code != null) {
				DateArray = Date.split("-");//DateArray[1],[2]可以拿來比較月份
				DepartureTime = time.split(":");//hour:min
				if (Integer.parseInt(DepartureTime[1]) > 30) {
					hour = Integer.parseInt(DepartureTime[0]);
					min = Integer.parseInt(DepartureTime[1]) - 30;
				}
				else {
					hour = Integer.parseInt(DepartureTime[0]) - 1 ;
					min = Integer.parseInt(DepartureTime[1]) + 30;
				}
			}
		}

		//(hour >= Integer.parseInt(today[2]) && min >= Integer.parseInt(today[3]));
		if (uid != null) {
			if (code.contentEquals(ticketCode)) {
				if(Integer.parseInt(DateArray[1]) > Integer.parseInt(today[0])) {					
					if((Integer.parseInt(DateArray[1]) >= Integer.parseInt(today[0]))) {					
						if (seats.length == people) {
							System.out.println(2);
							DeleteTicket("Data/booking.json","booking.json",number);		
							return "退票成功，已取消您的訂位紀錄";
						}
						else {
							System.out.println(2);
							remainPeople = seats.length - people;
							ConditionRefund("Data/booking.json","booking.json",number,remainPeople);
							return "修改成功，已將您人數變更為" + remainPeople + "位";
						}
					}
					else
						return "退票/修改失敗，需於指定車次開車前半小時";
				}
				else if(Integer.parseInt(DateArray[1]) == Integer.parseInt(today[0]) && Integer.parseInt(DateArray[2]) > Integer.parseInt(today[1])) {
					if (seats.length == people) {
						System.out.println(2);
						DeleteTicket("Data/booking.json","booking.json",number);		
						return "退票成功，已取消您的訂位紀錄";
					}
					else {
						System.out.println(2);
						remainPeople = seats.length - people;
						ConditionRefund("Data/booking.json","booking.json",number,remainPeople);
						return "修改成功，已將您人數變更為" + remainPeople + "位";
					}
				}
				else if(Integer.parseInt(DateArray[1]) == Integer.parseInt(today[0]) && Integer.parseInt(DateArray[2]) == Integer.parseInt(today[1])) {
					if ((hour >= Integer.parseInt(today[2]) && min >= Integer.parseInt(today[3]))) {
						if (seats.length == people) {
							System.out.println(2);
							DeleteTicket("Data/booking.json","booking.json",number);		
							return "退票成功，已取消您的訂位紀錄";
						}
						else {
							System.out.println(2);
							remainPeople = seats.length - people;
							ConditionRefund("Data/booking.json","booking.json",number,remainPeople);
							return "修改成功，已將您人數變更為" + remainPeople + "位";
						}
					}
					else
						return "退票/修改失敗，需於指定車次開車前半小時";
				}
				else
					return "退票/修改失敗，需於指定車次開車前半小時";
					
			}
			else
				return "退票/修改失敗，此訂位代號不存在";
		}
		else
			return "請輸入正確的UID";					
		
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
				ticketInfo.getJSONObject(i).getJSONArray(s).put(j,seats[j]);    //從後面開始刪
			}
		}
		String ws = dataJSON.toString();
		bw.write(ws);
		bw.flush();
		bw.close();
	}
	
	public static void main(String []arg) throws IOException {
		Refunding r = new Refunding();
		System.out.println(r.Refund("A12345679", "000000004", 1));
	}

}
