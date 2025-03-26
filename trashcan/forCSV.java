
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.*;

public class forCSV {

	public forCSV() {};

	public static String getSeatno(String train, String start, String end) throws IOException {
		String sss = "table";
		BufferedReader rDB = new BufferedReader(new FileReader("C://NTU/" + sss + ".csv"));
		// �����wtrain
		// System.out.println("here");
		String line = rDB.readLine();
		int lineCount = 1;

		boolean found = false;
		while (found == false) {

			line = rDB.readLine();
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}

			else {
				lineCount++;
			}
		}
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] stationSeat = new String[10];

		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��assign���쬰��
		try {
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					matchS = true;
				} else {
					s++;
					e++;
				}
				st++;
			}
			while (matchE == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}
		if (s > e) {
			rDB.close();
			return "wrong direction"; // ��V���~

		}
		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[0].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		String seatno = "not found";
		for (int i = 1; i <= 985; i++) {
			int checkseat = 0;
			for (int n = 0; n <= e - s; n++) {
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				seatno = num[i]; // �nreturn���F��
				break;
			}
		}
		rDB.close();
		return seatno;
	}

	public static String getSeatnoSpecial(String train, String start, String end,String kind) throws IOException {
		String sss = "table";
		BufferedReader rDB = new BufferedReader(new FileReader("C://NTU/" + sss + ".csv"));
// �����wtrain
		String line = rDB.readLine();
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}
			else {}
		}
//��_�l���B���I��
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] stationSeat = new String[10];
		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��assign���쬰��
		try {
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					matchS = true;
				} else {
					s++;
					e++;
				}
				st++;
			}
			while (matchE == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(end)) {
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}
		if (s > e) {
			rDB.close();
			return "wrong direction"; // ��V���~

		}
		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[0].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		String seatno = "not found";
		for (int i = 1; i <= 985; i++) {
			int checkseat = 0;
			for (int n = 0; n <= e - s; n++) {
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				seatno = num[i]; // �nreturn���F��
				if((seatno.contains("C")||seatno.contains("D"))&&(kind.equals("aisle"))) {
					break;
				}
				else if((seatno.contains("A")||seatno.contains("E"))&&(kind.equals("window"))) {
					break;
				}
				else {
					seatno ="no seats beside "+kind;
				}
				
				
			}
		}
		rDB.close();
		return seatno;
	}
	
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
						bw.write(0 + ","); // �o�̧ڧ�ele�����令0�N�����O0�F
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public void seat(BufferedWriter bw) throws IOException { // 1-1A,1-1B,1-1C,...
		JSONObject obj = JSONUtils.getJSONObjectFromFile("/seat.json");
		JSONArray cars = obj.getJSONArray("cars");
		for (int i = 1; i <= cars.length(); i++) {
			JSONObject carsObj = cars.getJSONObject(i - 1);
			JSONObject seats = carsObj.getJSONObject("seats");
			try {
				for (int j = 1; j <= 20; j++) { // �ѩ�C�����[�̦h��20�A�G��20���W��
					JSONArray Seat = seats.getJSONArray(String.valueOf(j));
					for (int k = 0; k < Seat.length(); k++) {

						String eachSeat = Seat.getString(k);
						bw.write(i + "-" + j + eachSeat + ",");
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// timeTable ���c�G
	// timeTable (a JSONArray) [
	// GeneralTimetable{GeneralTrainInfo{},StopTimes[],ServiceDay[]}]�A�o�ǬO�ڭ̭n��
	public static void main(String[] args) throws IOException {
		forCSV a = new forCSV();

		JSONArray timeTable = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		JSONObject seat = JSONUtils.getJSONObjectFromFile("/seat.json");

		BufferedWriter bw = new BufferedWriter(new FileWriter("C://NTU/table.csv"));

		int Mon[] = new int[timeTable.length()];
		for (int i = 0; i < timeTable.length(); i++) {
			JSONObject element = timeTable.getJSONObject(i); // �̥~�harray���C�@�Ӥ�����JSONObject
			JSONObject GeneralTimetable = element.getJSONObject("GeneralTimetable"); // element�̪�GeneralTimetable{}
			JSONObject ServiceDay = GeneralTimetable.getJSONObject("ServiceDay"); // GeneralTimetable�̪�ServiceDay
			Mon[i] = ServiceDay.getInt("Monday"); // ��C�@��ServiceDay�̪�Monday���X�_���ܤ@��array,��K�d�ݨC�@�x���O�_���}(1 or 0)
		} // ��L�ѥi�H������

		// for (int i = 0; i < Mon.length; i++) { ��´���
		// System.out.println(Mon[i]);
		// }

		for (int i = 0; i < Mon.length; i++) {
			if (Mon[i] == 0) {
				continue; // 0�]�S�}�^���ܴN�������L
			}
			// if (i == 10)break; �´��իe�Q�xMon���}

			JSONObject element = timeTable.getJSONObject(i);
			JSONObject GeneralTimetable = element.getJSONObject("GeneralTimetable");
			// �W�����P�W�@��loop
			JSONArray StopTimes = GeneralTimetable.getJSONArray("StopTimes");
			JSONObject GeneralTrainInfo = GeneralTimetable.getJSONObject("GeneralTrainInfo");
			String TrainNo = GeneralTrainInfo.getString("TrainNo"); // �C�x��������
			int Direction = GeneralTrainInfo.getInt("Direction"); // �C�x����Direction

			// System.out.println(TrainNo);
			bw.write(TrainNo + "," + Direction + "\n");
			bw.write("Seats" + ",");
			a.seat(bw);
			bw.write("\n");

			for (int j = 0; j < StopTimes.length(); j++) {
				JSONObject StopStations = StopTimes.getJSONObject(j);
				String ID = StopStations.getString("StationID");
				JSONObject StationName = StopStations.getJSONObject("StationName");
				String name = StationName.getString("En");
				name = ID + " : " + name; // ��U�ӯ���ID��W�r���X

				// System.out.println(name);
				bw.write(name + ",");

				a.seat0("/seat.json", bw);
				bw.write("\n");

			}
			// System.out.print("\n");

		}
		bw.close();

		String aaa = getSeatno("0300", "1060 : Tainan", "1000 : Taipei");
		System.out.println(aaa);
		
		String spe = getSeatnoSpecial("0300", "1060 : Tainan", "1000 : Taipei","aisle");
		System.out.println(spe);

	}
}