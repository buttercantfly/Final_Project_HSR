import org.json.JSONArray;
import org.json.JSONObject;

public class SearchOrder {
	public SearchOrder() {
		
	}

	private String[] infoArray,seats,Bseats;
	private String code = null;
	private String uid = null;
	private String Date,trainNumber,start,end,STime,ETime,ticketType;
	private String BDate,BtrainNumber,Bstart,Bend,BSTime,BETime;
	private int length,price,ticketCount; 
	
	JSONArray arrayofbooking = JSONUtils.getJSONArrayFromFile("/booking.json");
	
	public String search(String uid , String ticketCode) {
		for (int i = 1; i < arrayofbooking.length(); i++) {
			JSONObject data = arrayofbooking.getJSONObject(i);
			String temporuid = data.getString("uid");
			String temporcode = data.getString("code");
			//System.out.print(temporcode);
			price = data.getInt("payment");
			if (temporuid.contentEquals(uid)) {
				if (temporcode.contentEquals(ticketCode)) {	
					code = temporcode;
					JSONArray ticketInfo = data.getJSONArray("ticketInfo");
					//System.out.print(code);
					length = ticketInfo.length();
					if (length == 1 || length == 2) {//單程
						start = ticketInfo.getJSONObject(0).getString("start");
						//System.out.print(start);
						end = ticketInfo.getJSONObject(0).getString("end");
						STime = ticketInfo.getJSONObject(0).getString("departure time");
						ETime = ticketInfo.getJSONObject(0).getString("arrival time");
						ticketType = ticketInfo.getJSONObject(0).getString("carType");
						ticketCount = ticketInfo.getJSONObject(0).getInt("ticketsCount");
						trainNumber = ticketInfo.getJSONObject(0).getString("DTrainNo");
						Date = ticketInfo.getJSONObject(0).getString("date");	
						seats = new String[ticketInfo.getJSONObject(0).getJSONArray("seats").length()];
						for (int m = 0; m <ticketInfo.getJSONObject(0).getJSONArray("seats").length(); m++) {
							seats[m] = ticketInfo.getJSONObject(0).getJSONArray("seats").getString(m);
						}					
					}
					if (length == 2) {//回程
						//start = ticketInfo.getJSONObject(1).getString("start");
						//end = ticketInfo.getJSONObject(1).getString("end");
						BSTime = ticketInfo.getJSONObject(1).getString("departure time");
						BETime = ticketInfo.getJSONObject(1).getString("arrival time");
						BtrainNumber = ticketInfo.getJSONObject(1).getString("RTrainNo");
						BDate = ticketInfo.getJSONObject(1).getString("date");	
						Bseats = new String[ticketInfo.getJSONObject(1).getJSONArray("seats").length()];
						for (int m = 0; m <ticketInfo.getJSONObject(1).getJSONArray("seats").length(); m++) {
							Bseats[m] = ticketInfo.getJSONObject(1).getJSONArray("seats").getString(m);
						}				
					}				
				}
				
				    
			}
			
				
				
			
		}
		
		
		if (uid != null){
			if (code != null) {
				if (length == 1) {
					infoArray = new String [8];
				}
				else if (length == 2) {
					infoArray = new String[16];
				}
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
						infoArray[7] += seats[t]+" ";
					}
				}
				if (length == 2) {
					infoArray[8] = "回程";
					infoArray[9] = BDate;
					infoArray[10] = BtrainNumber;
					infoArray[11] = end;
					infoArray[12] = start;
					infoArray[13] = BSTime;
					infoArray[14] = BETime;
					infoArray[15] = "";
					for (int n = 0; n < seats.length; n ++) {
						infoArray[15] += Bseats[n]+" ";
					}
				}
			}
			else
				return "TicketCode不正確";
		}
		else
			return"請輸入正確的UID";
		return "???";
		
	}
	public String[] getArray() {
		return infoArray;
	}
	public String getTicketType() {
		return ticketType;
	}


	public int getTicketCount() {
		return ticketCount;
	}
	
	public int getPrice() {
		return price;
	}
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		SearchOrder s = new SearchOrder();
//		s.search("A123456789", "000000005");
//		for (int i = 0 ; i<s.getArray().length;i++) {
//			System.out.print(s.getArray()[i]);
//			System.out.print(" ");
//		}
//		
//		
//
//	}

}
