import org.json.JSONArray;
import org.json.JSONObject;

public class SearchOrder {
	public SearchOrder() {
		
	}

	private String[] infoArray,seats,Bseats;
	private String code = null;
	private String Date,trainNumber,start,end,STime,ETime;
	private String BDate,BtrainNumber,Bstart,Bend,BSTime,BETime;
	private int length; 
	
	JSONArray arrayofbooking = JSONUtils.getJSONArrayFromFile("/booking.json");
	
	public String[] search(String uid , String ticketCode) {
		for (int i = 0; i < arrayofbooking.length(); i++) {
			JSONObject data = arrayofbooking.getJSONObject(i);
			String temporcode = data.getString("code");
			
			if (temporcode.contentEquals(ticketCode)) {	
				code = temporcode;
				JSONArray ticketInfo = data.getJSONArray("ticketInfo");
				length = ticketInfo.length();
				if (length == 1 || length == 2) {//單程
					//start = ticketInfo.getJSONObject(0).getString("");
					//end = ticketInfo.getJSONObject(0).getString("");
					//STime = ticketInfo.getJSONObject(0).getString("");
					//ETime = ticketInfo.getJSONObject(0).getString("");
					trainNumber = ticketInfo.getJSONObject(0).getString("TrainNo");
					Date = ticketInfo.getJSONObject(0).getString("date");	
					seats = new String[ticketInfo.getJSONObject(0).getJSONArray("seats").length()];
					for (int m = 0; m <ticketInfo.getJSONObject(0).getJSONArray("seats").length(); m++) {
						seats[m] = ticketInfo.getJSONObject(0).getJSONArray("seats").getString(m);
					}					
				}
				if (length == 2) {//回程
					//start = ticketInfo.getJSONObject(1).getString("");
					//end = ticketInfo.getJSONObject(1).getString("");
					//STime = ticketInfo.getJSONObject(1).getString("");
					//ETime = ticketInfo.getJSONObject(1).getString("");
					BtrainNumber = ticketInfo.getJSONObject(1).getString("TrainNo");
					BDate = ticketInfo.getJSONObject(1).getString("date");	
					Bseats = new String[ticketInfo.getJSONObject(1).getJSONArray("seats").length()];
					for (int m = 0; m <ticketInfo.getJSONObject(1).getJSONArray("seats").length(); m++) {
						Bseats[m] = ticketInfo.getJSONObject(1).getJSONArray("seats").getString(m);
					}				
				}				
			}	
			
		}
		if (length == 1) {
			infoArray = new String [8];
		}
		else if (length == 2) {
			infoArray = new String[16];
		}
		if (code != null) {
			if (length == 1 || length == 2) {
				infoArray[0] = "去程";
				infoArray[1] = Date;
				infoArray[2] = trainNumber;
				infoArray[3] = start;
				infoArray[4] = end;
				infoArray[5] = STime;
				infoArray[6] = ETime;
				infoArray[7] = "";
				for (int t = 0; t < seats.length; t ++) {
					infoArray[7] += seats[t];
				}
			}
			if (length == 2) {
				infoArray[8] = "回程";
				infoArray[9] = BDate;
				infoArray[10] = BtrainNumber;
				infoArray[11] = Bstart;
				infoArray[12] = Bend;
				infoArray[13] = BSTime;
				infoArray[14] = BETime;
				infoArray[15] = "";
				for (int n = 0; n < seats.length; n ++) {
					infoArray[15] += Bseats[n];
				}
			}
		}
		
		
		return infoArray;
				
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SearchOrder s = new SearchOrder();
		for (int i = 0 ; i < s.search("A123456789", "123456789").length ; i ++) {
			System.out.print(s.search("A123456789", "123456789")[i]);
		}
		

	}

}