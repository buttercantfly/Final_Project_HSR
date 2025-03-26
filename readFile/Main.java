package json.test;

import org.json.*;

public class Main {

	public static void main(String[] args) {
		// youtube example
		JSONObject test = JSONUtils.getJSONObjectFromFile("/obj.json");
		System.out.println(test);
		String[] NAME = JSONObject.getNames(test);
		for (String String : NAME) {
			System.out.println(String + ": " + test.get(String));
		}
		
		System.out.print("\n");
		
		JSONArray array = test.getJSONArray("Array");
		
		for (int i = 0; i < array.length(); i++) {
			System.out.println(array.get(i));
		}
		System.out.print("\n");
		
		int number = test.getInt("Number");
		System.out.println(number);
		
		System.out.print("\n");
		
		String String = test.getString("String");
		System.out.println(String);
		
		System.out.print("\n");
		
		boolean bool = test.getBoolean("Boolean");
		System.out.println(bool);
		
		// below is to get data from all file 
		
		  //earlyDiscount
		  JSONObject obj = JSONUtils.getJSONObjectFromFile("/earlyDiscount.json");
		  String[] names = JSONObject.getNames(obj);
		  for(String string : names) {
		   System.out.println(string + ": " + obj.get(string));
		  }
		  
		  System.out.print("\n");
		  
		  JSONArray jsonArray = obj.getJSONArray("DiscountTrains");
		  for(int i = 0; i <jsonArray.length(); i++) {
		     System.out.println(jsonArray.get(i));
		  }
		  
		  System.out.print("\n");
		  //booking
		  JSONArray arr = JSONUtils.getJSONArrayFromFile("/booking.json");
		  for(int i = 0; i <arr.length(); i++) {
		     System.out.println(arr.get(i));
		  }
		  
		  System.out.print("\n");
		  //price
		  JSONArray arr2 = JSONUtils.getJSONArrayFromFile("/price.json");
		  for(int i = 0; i <arr2.length(); i++) {
		     System.out.println(arr2.get(i));
		  }
		  
		  System.out.print("\n");
		  
		  //seat
		  JSONObject obj2 = JSONUtils.getJSONObjectFromFile("/seat.json");
		  String[] names2 = JSONObject.getNames(obj2);
		  for(String string : names2) {
		   System.out.println(string + ": " + obj2.get(string));
		  }
		  System.out.print("\n");
		    
		       
		  //station
		  JSONArray arr3 = JSONUtils.getJSONArrayFromFile("/station.json");
		  for(int i = 0; i <arr3.length(); i++) {
		     System.out.println(arr3.get(i));
		  }
		  
		  System.out.print("\n");
		  
		  //timeTable
		  JSONArray arr4 = JSONUtils.getJSONArrayFromFile("/timeTable.json");
		  for(int i = 0; i <arr4.length(); i++) {
		     System.out.println(arr4.get(i));
		  }
		  
		  System.out.print("\n");
		  //universityDiscount
		  JSONObject obj3 = JSONUtils.getJSONObjectFromFile("/universityDiscount.json");
		  String[] names3 = JSONObject.getNames(obj3);
		  for(String string : names3) {
		   System.out.println(string + ": " + obj3.get(string));
		  }
		  
		  System.out.print("\n");
		  
		  JSONArray jsonArray3 = obj3.getJSONArray("DiscountTrains");
		  for(int i = 0; i <jsonArray3.length(); i++) {
		   
		   System.out.println(jsonArray3.get(i));
		  }
		  
		  System.out.print("\n");
		  
		  
		 

		
	}

}
