public class Validator {
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
		  Integer.parseInt(str);
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

}
