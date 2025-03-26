
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

/**
 * @author yy121
 *
 */
/**
 * @author yy121
 *
 */
public class searchDB {

	/**
	 * @param date ����Φ��p0628���Υ[.csv 
	 * @param train
	 * @param start
	 * @param end
	 * @param number����
	 * @return �y�쪺�s��,�y�쪺�s��,�y�쪺�s��,.....(�N��u���@�i���k��]�|���r��)
	 * @throws IOException
	 */	
	public static String getSeatno(String date,String train, String start, String end,int number) throws IOException {
		BufferedReader rDB = new BufferedReader(new FileReader("./" + date + ".csv"));
		// �����wtrain
		// System.out.println("here");
		String line=" ";// = rDB.readLine();
//		int lineCount++;
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}
			else {
				}
		}
		// �ؤ@��array�����O�U�ӯ���string�A�w�]�Ŷ��Q�Ө���
		String[] stationSeat = new String[10];

		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��assign���쬰��
		int sts=0,ste=0;
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					sts=st;
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
					ste=st;
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			//System.out.println(ee.getMessage());
		}
		if ((sts >= ste)) {
			rDB.close();
			return "wrong direction"; // ��V���~
		}

		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[2].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}

		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		int col=0;
		String seatno = "";
		String currentSeat="no seat available";
		for(int t=1;t<=number;t++) {
		for (int i = col; i <= 985; i++) {
			int checkseat = 0;

			for (int n = 0; n <= e - s; n++) {
				
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				currentSeat=num[i];
				seatno = seatno+currentSeat+", "; // �nreturn���F��
				col=i+1;
				break;
			}

		}
		}
		rDB.close();
		return seatno;
	}


	/**
	 * @param date(����Φ��p0628���Υ[.csv )
	 * @param train(�C�����X)
	 * @param start
	 * @param end
	 * @param number(����)
	 * @param kind(�u���window or aisle ���ڨS���g����)
	 * @return �y�쪺�s��,�y�쪺�s��,�y�쪺�s��,.....(�N��u���@�i���k��]�|���r��)
	 * @throws IOException
	 */
	public static String getSeatnoSpecial(String date,String train, String start, String end,int number,String kind) throws IOException {
		BufferedReader rDB = new BufferedReader(new FileReader("./" + date + ".csv"));
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
		// ���}�l���A�@��assign��stationSeat[]���쬰��
		int sts=0,ste=0;
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					e++;
					sts=st;
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
					ste=st;
					matchE = true;
				} else {
					e++;
				}
				st++;
			}
		} catch (Exception ee) {
			//System.out.println(ee.getMessage());
		}
		if ((sts >= ste)) {
			rDB.close();
			return "wrong direction"; // ��V���~
		}
		// ��Ĥ@�Ƴ]�w���y��
		String[] num = stationSeat[2].split(",");
		// �⦳����m��i�@�ӤG�����y��array
		String[][] seats = new String[e - s + 1][986];
		for (int n = 0; n <= e - s; n++) {
			seats[n] = stationSeat[n + s].split(",");
		}
		// �����O�ۦP�y��A��O�ۦP��
		// ���H�@�Ӧ�l�����(�j��for)�A�����V�U�[����(����for)�A�A�i�J�U�@�����j��
		int col=0;
		String seatno = "";
		String currentSeat="no seats available";
		for(int t=1;t<=number;t+=0) {		
		for (int i = col; i <= 985; i++) {
			int checkseat = 0;
			for (int n = 0; n <= e - s; n++) {
				if (seats[n][i].equals("0")) {
				} else {
					checkseat++;
				}
			}
			if (checkseat == 0) {
				currentSeat=num[i];
				if((currentSeat.contains("C")||currentSeat.contains("D"))&&(kind.equals("aisle"))) {
					seatno =seatno+ currentSeat+", "; // �nreturn���F��
					col=i+1;
					t++;
					break;
				}
				else if((currentSeat.contains("A")||currentSeat.contains("E"))&&(kind.equals("window"))) {
					seatno =seatno+currentSeat+", "; // �nreturn���F��
					col=i+1;
					t++;
					break;
				}
				else {
					
				}		
			}
		}
		}
		rDB.close();
		if(seatno.equals("")) {
			seatno ="no seats beside "+kind;
		}
		
		return seatno;
	}


	/**
	 * @param date
	 * @param train
	 * @param (String)discount
	 * @return"there is still early-tickets"�άO"there is no early-tickets"
	 * @throws IOException
	 */
	public static String checkEarly(String date,String train,String discount) throws IOException {
		BufferedReader rDB = new BufferedReader(new FileReader("./" + date + ".csv"));
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
				
				
				//���æ����������
				int tick=0;
				String[] caldiscount = new String[2];
				caldiscount[0] = rDB.readLine();
				caldiscount[1] = rDB.readLine();
				String[] dis = caldiscount[0].split(",");
				String[] tickets = caldiscount[1].split(",");		
				for(int t=1;t<=10;t++) {
					if (dis[t].contains(discount)) {
						tick = Integer.parseInt(tickets[t]);

						break;
					}
				}
				rDB.close();
		
				if (tick>0) {
					return "there is still early-tickets";
				}
				else {
					return "there is no early-tickets";
				}

	}
	
	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @throws IOException
	 */
	public static void setSeatno(String date,String train, String start, String end,String seatNO) throws IOException {
	
		String ttt="tabble";
		
		BufferedReader rDB = new BufferedReader(new FileReader("./" + date + ".csv"));							//here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("./" + ttt + ".csv"));							//here
		
		// �����wtrain
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}
			else {
			}
		}		
		//���_�l�B���I
		String[] stationSeat = new String[15];
		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		// ���}�l���A�@��write���쬰��
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();
				if (stationSeat[st].contains(start)) {
					s=st;
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(stationSeat[st]);
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
		}

		//stationSeat[s]�N�O�q�_�l��
		//stationSeat[e]�N�O���I��
		
		//����y�쪺��s��X
		int x = 0;
		String[] num = stationSeat[2].split(",");																//here
		for(int t=1;t<=985;t++) {
			if (num[t].contains(seatNO)) {
				x=t;
				break;
			}
		}
		//�����������ܦ�array�A������x�Ӥ�����1
		String[] seats=new String[985] ;
		
		for(int tt=s;tt<=e;tt++) {
			seats=stationSeat[tt].split(",");
			seats[x]="1";
			stationSeat[tt]=String.join(",", seats);
			sDB.newLine();
			sDB.write(stationSeat[tt]);
		}
		
		//�ѤU���A���swrite�@��
		try {
			int fake=1;
			while(fake==1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		}
		catch(Exception ee) {
			ee.getMessage();
		}
		finally {
			rDB.close();
			sDB.close();
		}
		
		BufferedReader rDB2 = new BufferedReader(new FileReader("./" + ttt + ".csv"));							//here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("./" + date + ".csv"));							//here
		try {
			int fake2=1;
			sDB2.write(rDB2.readLine());
			while(fake2==1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		}
		catch(Exception ee) {
			ee.getMessage();
		}
		finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("C://NTU/" + ttt +".csv");
		file_dulplicate.delete();
			
	}

	
	/**
	 * @param date
	 * @param train
	 * @param start
	 * @param end
	 * @param seatNO
	 * @param discount
	 * @throws IOException
	 * 
	 * �����@�Ӥ����ɮק��ƿ�i�h�A�ѧ�����A�ͤ@�ӻP�쥻�ɮפ@�˪��W�r�A�⤤�~�ɮ׿�J
	 * �A�⤤�~�ɮקR��
	 */
	public static void setSeatnoEarly(String date,String train, String start, String end,String seatNO,String discount) throws IOException {
		
		String ttt="tabble";
		
		BufferedReader rDB = new BufferedReader(new FileReader("./" + date + ".csv"));							//here
		BufferedWriter sDB = new BufferedWriter(new FileWriter("./" + ttt + ".csv"));							//here
		
		// �����wtrain
		String line = rDB.readLine();
		sDB.write(line);
		boolean found = false;
		while (found == false) {
			line = rDB.readLine();
			sDB.newLine();
			sDB.write(line);
			String[] tt = line.split(",");
			if (tt[0].equals(train)) {
				found = true;
			}
			else {
			}
		}		


		//���æ����������
		String[] caldiscount = new String[2];
		caldiscount[0] = rDB.readLine();
		sDB.newLine();
		sDB.write(caldiscount[0]);
		caldiscount[1] = rDB.readLine();
		String[] dis = caldiscount[0].split(",");
		String[] tickets = caldiscount[1].split(",");		
		for(int t=1;t<=10;t++) {
			if (dis[t].contains(discount)) {
				int tick = Integer.parseInt(tickets[t])-1;
				tickets[t]=Integer.toString(tick);
				caldiscount[1]=String.join(",", tickets);
				sDB.newLine();
				sDB.write(caldiscount[1]);
				break;
			}
		}
		
		//���_�l�B���I
		String[] stationSeat = new String[15];
		// �n�O���O����ӯ�
		int st = 0;
		int s = 0, e = 0;
		boolean matchS = false, matchE = false;
		
		// ���}�l���A�@��write���쬰��
		try {
			
			while (matchS == false) {
				stationSeat[st] = rDB.readLine();	
				if (stationSeat[st].contains(start)) {
					e++;
					matchS = true;
				} else {
					s++;
					e++;
					sDB.newLine();
					sDB.write(stationSeat[st]);
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
		}
		//stationSeat[s]�N�O�q�_�l��
		//stationSeat[e]�N�O���I��
		

		
		
		//����y�쪺��s��X
				int x = 0;
				String[] num = stationSeat[0].split(",");															//here
				for(int t=1;t<=985;t++) {
					if (num[t].contains(seatNO)) {
						x=t;
						break;
					}
				}
				//�����������ܦ�array�A������x�Ӥ�����1
				String[] seats=new String[985] ;
				
				for(int tt=s;tt<=e;tt++) {
					seats=stationSeat[tt].split(",");
					seats[x]="1";
					stationSeat[tt]=String.join(",", seats);
					sDB.newLine();
					sDB.write(stationSeat[tt]);
				}
				
		//�ѤU���A���swrite�@��
		try {
			int fake=1;
			while(fake==1) {
				sDB.newLine();
				sDB.write(rDB.readLine());

			}
		}
		catch(Exception ee) {
			ee.getMessage();
		}
		finally {
			rDB.close();
			sDB.close();
		}
		
		BufferedReader rDB2 = new BufferedReader(new FileReader("./" + ttt + ".csv"));							//here
		BufferedWriter sDB2 = new BufferedWriter(new FileWriter("./" + date + ".csv"));							//here
		try {
			int fake2=1;
			sDB2.write(rDB2.readLine());
			while(fake2==1) {
				sDB2.newLine();
				sDB2.write(rDB2.readLine());
			}
		}
		catch(Exception ee) {
			ee.getMessage();
		}
		finally {
			rDB2.close();
			sDB2.close();
		}
		File file_dulplicate = new File("./" + ttt +".csv");
		file_dulplicate.delete();
			
	}
	
	
	//�ܽd
	public static void main(String[] args) throws IOException {


		String ss=getSeatno("0612","0565","1000 : Taipei", "1035 : Miaoli",8);
		System.out.println(ss);//0101A, 0101B, 0101C, 0102A, 0102B, 0102C, 0102D, 0102E, 
		
		setSeatno("0612","0565","1000 : Taipei", "1035 : Miaoli","0102B");
		
		System.out.println(checkEarly("0612","0565","0.9"));//there is still early-tickets
		setSeatnoEarly("0612","0565", "1000 : Taipei", "1035 : Miaoli","0102D","0.9");
	}

}	