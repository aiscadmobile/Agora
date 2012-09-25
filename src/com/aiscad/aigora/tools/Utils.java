package com.aiscad.aigora.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
   
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    
    //Metodo usado para obtener la fecha actual
    //@return Retorna un <b>STRING</b> con la fecha actual formato "dd-MM-yyyy"
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }

    //Metodo usado para obtener la hora actual del sistema
    //@return Retorna un <b>STRING</b> con la hora actual formato "hh:mm:ss"
    public static String getHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
        return formateador.format(ahora);
    }

    //Sumarle dias a una fecha determinada
    //@param fch La fecha para sumarle los dias
    //@param dias Numero de dias a agregar
    //@return La fecha agregando los dias
    public static java.sql.Date sumarFechasDias(Date date, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.DATE, dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    //Restarle dias a una fecha determinada
    //@param fch La fecha
    //@param dias Dias a restar
    //@return La fecha restando los dias
    public static synchronized java.sql.Date restarFechasDias(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, -dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    //Diferencias entre dos fechas
    //@param fechaInicial La fecha de inicio
    //@param fechaFinal  La fecha de fin
    //@return Retorna el numero de dias entre dos fechas
    public static synchronized int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }
    
    //Devuele un java.util.Date desde un String en formato dd-MM-yyyy
    //@param La fecha a convertir a formato date
    //@return Retorna la fecha en formato Date
    public static synchronized java.util.Date deStringToDate(String fecha,String format) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat(format);
        Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);
            return fechaEnviar;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String timeDiff(Date fechaInicial, Date fechaFinal){

    	  long milliseconds1 = fechaInicial.getTime();
    	  long milliseconds2 = fechaFinal.getTime();
    	  long diff = milliseconds2 - milliseconds1;
    	  long diffSeconds = diff / 1000;
    	  long diffMinutes = diff / (60 * 1000);
    	  long diffHours = diff / (60 * 60 * 1000);
    	  long diffDays = diff / (24 * 60 * 60 * 1000);
    	
        	  
    	  String result="";
    	  String days="";
    	  String hours="";
    	  String minutes="";
    	  String seconds="";
    	  
    	  if (diffDays!=0){
    		  days=" "+diffDays+" dias";
    	  }   
    	  else if (diffHours!=0){
    		  hours = " "+diffHours+" h";
    	  }
    	  else if (diffMinutes!=0){
    		  minutes = " "+diffMinutes+" min";
    	  }
    	  else if (diffSeconds!=0){
    		  seconds = " "+diffSeconds+" s";
    		  
    	  }else{
    		 return "Ahora";
    	  }
    	  result = "Hace"+days+hours+minutes+seconds;
    	return result;
    }
    
    public static String formatedDiffDate(Date dateDiff){
    	
		Date myDate= new Date();
		Date itemDate = dateDiff;

		String dateInfo=Utils.timeDiff(dateDiff, myDate);
		if (Utils.diferenciasDeFechas(myDate, dateDiff)==-1){
			dateInfo = "Ayer  " + itemDate.getHours()+":"+itemDate.getMinutes();
		}else{
			if (myDate.getYear() == dateDiff.getYear()){
				dateInfo = Utils.timeDiff(dateDiff, myDate);
			}else{
				CharSequence dateItem = android.text.format.DateFormat.format(" dd/MM/yyy", dateDiff);
				dateInfo = itemDate.getHours()+":"+itemDate.getMinutes() + dateItem.toString();
			}
		}				
		return dateInfo;
    }
}