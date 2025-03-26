import java.io.IOException;

public class run {

	public static void main(String[] args) {
		Booking runner = new Booking();
		
		try {
			System.out.println(runner.Search("2021-07-22 1400", "2021-07-25 1200", "1047", "1060", 1, 0, 0, 0, false));
			//System.out.println(runner.Book(2, 2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
