package br.com.cardeal.globals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtils {
	
	public static String getDateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getDateAndTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String s = formatter.format(date.getTime());
		return s;
	}
	
	public static Date getDateFormat(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date dateReturn = null;
		try {
			dateReturn = formatter.parse( formatter.format( date.getTime() ) );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateReturn;
	}
	
	public static String getDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String s = formatter.format(date.getTime());
		return s;
	}
	
	public static java.util.Date buildInitDate() {
		java.util.Date today = now();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		return buildInitDate(
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.YEAR)
				);
	}

	public static java.util.Date buildEndDate() {
		java.util.Date today = now();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		return buildEndDate(
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.YEAR)
				);
	}

	public static java.util.Date buildInitDate(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return buildInitDate(
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.YEAR)
				);
	}

	public static java.util.Date buildEndDate(java.util.Date date) {		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return buildEndDate(
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.YEAR)
				);
	}

	public static java.util.Date buildInitDate(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();		
	}
	
	public static java.util.Date buildEndDate(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();		
	}
	
	public static String getWeekDay(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		switch(i) {
		case Calendar.SUNDAY: return "Domingo";
		case Calendar.MONDAY: return "Segunda-Feira";
		case Calendar.TUESDAY: return "Terça-Feira";
		case Calendar.WEDNESDAY: return "Quarta-Feira";
		case Calendar.THURSDAY: return "Quinta-Feira";
		case Calendar.FRIDAY: return "Sexta-Feira";
		case Calendar.SATURDAY: return "Sábado";
		default: return "???";
		}
	}
	
	public static java.sql.Date getSqlDate ( java.util.Date date ) {  
	     if ( date == null )   {  
	         return null; 
	      }  else  {  
	         return new java.sql.Date ( date.getTime (  )  ) ; 
	      }  
	} 	
	
	public static java.util.Date getUtilDate ( java.sql.Date date ) {  
	     if ( date == null )   {  
	         return null; 
	      }  else  {  
	         return new java.util.Date ( date.getTime (  )  ) ; 
	      }  
	} 	
	
	public static String formatDate(java.sql.Date date) {
		if(date == null)
			return "";
		String DATE_FORMAT = "dd/MM/yyyy";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    //Calendar c1 = Calendar.getInstance();
	    return sdf.format(date);
	}

	public static String formatDate(java.util.Date date) {
		if(date == null)
			return "";
		String DATE_FORMAT = "dd/MM/yyyy";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(date);
	}

	public static String formatDate(java.util.Date date, String format) {
		if(date == null)
			return "";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(format);
	    return sdf.format(date);
	}

	public static String formatCompleteDate(java.util.Date date) {
		if(date == null)
			return "";
		String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(date);
	}

	public static String formatTime(java.util.Date date) {
		if(date == null)
			return "";
		String DATE_FORMAT = "HH:mm";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(date);
	}

	public static String formatDate(Calendar calendar) {
		if(calendar == null)
			return "";
		String DATE_FORMAT = "dd/MM/yyyy";
	    java.text.SimpleDateFormat sdf = 
	          new java.text.SimpleDateFormat(DATE_FORMAT);
	    return sdf.format(calendar.getTime());
	}
	
	public static String getCompleteDateForDb() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getDateForDb() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getTimeForDb() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getCurrentShortTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getCurrentDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getCurrentMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}

	public static String getCurrentYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}

	public static String getCurrentYearMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static String getMostProbablyCurrentYearMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		GregorianCalendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day <= 5){		
			//o mes de referencia será o mês passado
			int month = calendar.get(Calendar.MONTH); //retorna valores de 0 a 11
			if(month == Calendar.JANUARY){
				int year = calendar.get(Calendar.YEAR) - 1;
				month = 12;
				String s = String.format("%04d%02d", year, month);
				return s;
			}
			else {
				int year = calendar.get(Calendar.YEAR);
				String s = String.format("%04d%02d", year, month);
				return s;				
			}
		}
		else {
			String s = formatter.format(calendar.getTime());
			return s;			
		}
	}
	
	
	public static String getCompleteDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return s;
	}
	
	public static java.util.Date now() {
		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.getTime();
	}
	
	public static int timestamp() {
		Calendar calendar = Calendar.getInstance();
		int result = (int) ( (calendar.getTimeInMillis() / 1000L) - (3 * 60 * 60) );
		return result;
	}
	
	public static int getActualYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	public static java.util.Date addDays(java.util.Date d, int numberOfDays) {
		 
		java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
		cal.setTime(d);
		cal.add(java.util.GregorianCalendar.DATE, numberOfDays);
		return cal.getTime();
	}
	
	public static String addDays(String d, int numberOfDays) {
		try {
			java.util.Date date1 = strToDate(d, "yyyyMMdd");
			java.util.Date date2 = addDays(date1, numberOfDays);
			String d2 = formatDate(date2, "yyyyMMdd");
			return d2;
		} catch (Exception e) {
			return d;
		}
		
	}
	
	public static java.util.Date strToDate(String strDate) throws Exception {
		
		java.util.Date date = null;
    	try {
    		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
    		date=df.parse( strDate );
    		return date;
	    }
	    catch (Exception err) {
	    	throw new Exception("Data " + strDate + " inválida");
	    }
	}
	
	public static java.util.Date strToDate(String strDate, String format) throws Exception {
		
		java.util.Date date = null;
    	try {
    		SimpleDateFormat df=new SimpleDateFormat(format);
    		date=df.parse( strDate );
    		return date;
	    }
	    catch (Exception err) {
	    	throw new Exception("Data " + strDate + " inválida");
	    }
	}
	
	public static java.util.Date strToDateTime(String strDate) throws Exception {
		
		java.util.Date date = null;
    	try {
    		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		date=df.parse( strDate );
    		return date;
	    }
	    catch (Exception err) {
	    	throw new Exception("Data " + strDate + " inválida");
	    }
	}
	
	public static java.util.Date strToDateTimeFull(String strDate) throws Exception {
		
		java.util.Date date = null;
    	try {
    		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
    		date = df.parse( strDate );
    		return date;
	    }
	    catch (Exception err) {
	    	throw new Exception("Data " + strDate + " inválida");
	    }
	}
	
	public static boolean dateIsValid(String strDate) {
		
    	try {
    		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
    		df.parse( strDate );
    		return true;
	    }
	    catch (Exception err) {
	    	return false;
	    }
	}

	public static boolean sapDateIsValid(String strDate) {
		
    	try {
    		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
    		df.parse(strDate);
    		return true;
	    }
	    catch (Exception err) {
	    	return false;
	    }
	}

	public static java.util.Date validateTime(String strTime) throws Exception {
		
		java.util.Date date = null;
    	try {
    		SimpleDateFormat df=new SimpleDateFormat("HH:mm");
    		date=df.parse(strTime);
    		return date;
	    }
	    catch (Exception err) {
	    	throw new Exception("Hora " + strTime + " invalida");
	    }
	}
	
	public static String getFirstDayOfMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		return "01/" + s;
	}
	
	public static String getLastDayOfMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
		GregorianCalendar calendar = new GregorianCalendar();
		String s = formatter.format(calendar.getTime());
		String month = s.substring(0, 2);
		String day;
		switch(Integer.parseInt(month)) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = "31";
			break;
		
		case 2:
			day = "28";
			break;
			
		default:
			day = "30";
			break;
		}
		return day + "/" + s;
	}
	
	static final long ONE_HOUR = 60 * 60 * 1000L;
	public static long daysBetween(Date d1, Date d2){
	    return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / 
	                  (ONE_HOUR * 24));
	}   
	  	
	public static long daysBetween(String d1, String d2){
		try {
			Date dt1;
			Date dt2;
			if(d1.length() == 8) {
				dt1 = DateTimeUtils.strToDate(d1, "yyyyMMdd");
		    	dt2 = DateTimeUtils.strToDate(d2, "yyyyMMdd");				
			}
			else {
				dt1 = DateTimeUtils.strToDate(d1, "yyyyMMddHHmmss");
		    	dt2 = DateTimeUtils.strToDate(d2, "yyyyMMddHHmmss");								
			}
	    	return (int)DateTimeUtils.daysBetween(dt1, dt2);
		} catch (Exception e) {
			return 0;
		}
	}   
	
	public static int minutesBetween(java.util.Date d1, java.util.Date d2){
		try {
	    	return (int)((d2.getTime() - d1.getTime()) / 1000 / 60);
		} catch (Exception e) {
			return 0;
		}
	}   
	
    public static String dateDescription(String date) {
        if (date != null && (date.length() == 8 || date.length() == 6))
        {
            String descMonth = "";
            String descYear = date.substring(0, 4);
            String month = date.substring(4, 6);
            switch (Integer.parseInt(month))
            {
                case 0: return "";
                case 1: descMonth = "JAN"; break;
                case 2: descMonth = "FEV"; break;
                case 3: descMonth = "MAR"; break;
                case 4: descMonth = "ABR"; break;
                case 5: descMonth = "MAI"; break;
                case 6: descMonth = "JUN"; break;
                case 7: descMonth = "JUL"; break;
                case 8: descMonth = "AGO"; break;
                case 9: descMonth = "SET"; break;
                case 10: descMonth = "OUT"; break;
                case 11: descMonth = "NOV"; break;
                case 12: descMonth = "DEZ"; break;
            }
            return (descMonth + "/" + descYear);
        }
        else
            return "";
    }
    
    public static String valueToAbrevMonth(String value) {
        switch (Integer.parseInt(value))
        {
            case 0: return "";
            case 1: return "JAN"; 
            case 2: return "FEV"; 
            case 3: return "MAR"; 
            case 4: return "ABR"; 
            case 5: return "MAI"; 
            case 6: return "JUN"; 
            case 7: return "JUL"; 
            case 8: return "AGO"; 
            case 9: return "SET"; 
            case 10: return "OUT"; 
            case 11: return "NOV"; 
            case 12: return "DEZ"; 
        } 
        return "";
    }
    
    public static String abrevMonthToValue(String abrev) {
    	if(abrev.equals("JAN"))
    		return "01";
    	else if(abrev.equals("FEV"))
    		return "02";
    	else if(abrev.equals("MAR"))
    		return "03";
    	else if(abrev.equals("ABR"))
    		return "04";
    	else if(abrev.equals("MAI"))
    		return "05";
    	else if(abrev.equals("JUN"))
    		return "06";
    	else if(abrev.equals("JUL"))
    		return "07";
    	else if(abrev.equals("AGO"))
    		return "08";
    	else if(abrev.equals("SET"))
    		return "09";
    	else if(abrev.equals("OUT"))
    		return "10";
    	else if(abrev.equals("NOV"))
    		return "11";
    	else if(abrev.equals("DEZ"))
    		return "12";
    	else
    		return "00";
    }
    
    //verifica se a hora atual é par do tempo passado como parametro
    public static boolean isEvenMinute(int period) {
    	Calendar now = Calendar.getInstance();
    	int min = now.get(Calendar.MINUTE);
    	
    	return (min % period) == 0;
    }
	
    public static boolean isEvenMinute(String dateTime, int period) {
    	try {
	    	String strMinutes;
	    	if(dateTime.length() == 6)
	    		strMinutes = dateTime.substring(2, 4);
	    	else
	    		strMinutes = dateTime.substring(10, 12);
	    	int minutes = Integer.parseInt(strMinutes);	
	    	
	    	return (minutes % period) == 0;
    	}
    	catch(Exception e) {
    		return false;
    	}
    }
	
    public static java.util.Date validateDate(String day, String month, String year) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateInString = day + "-" + month + "-" + year;
	 
		try {	 
			java.util.Date date = formatter.parse(dateInString);
			return date;
		} 
		catch (ParseException e) {
			return null;
		}	
	}    
}
