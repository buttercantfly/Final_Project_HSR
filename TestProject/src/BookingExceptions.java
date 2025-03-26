
public class BookingExceptions extends Exception{
	String message = null;
	BookingExceptions(String message){
		this.message = message;
	}
	
	public String getMessage(){
		System.out.print(message);
        return message;
	}
}
