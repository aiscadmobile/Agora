package com.aiscad.agora.sqlite;

public interface DataBaseConstants {
	
	//Base de datos
	public static String DB_NAME = "agora.db";
	public static int DB_VERSION = 1;
	
	//Tabla de cola de salida de ficheros
	public static String TABLE_LOCATION = "location_table";

	public static String ROW_LOCATION_ID = "id";
	public static String ROW_LOCATION_NAME = "name";
	public static String ROW_LOCATION_LATITUDE = "latitude";
	public static String ROW_LOCATION_LONGITUDE= "longitude";
	public static String ROW_LOCATION_RANGE= "range";
	public static String ROW_LOCATION_ADDRESS = "address";



	//Tabla de cola de entrada de ficheros

}
