/*
 * Agora - Project for BCN Apps for DEMOCRACY!
 * 
 * @autors 
 * 	Raul Santos & Luis Aguilar Cruz
 * 
 * @description 
 * 	Clase que maneja las llamadas a los webservices, posts, gets a urls y retorna la informacion
 * 	solicitada.
 * 
 */


package com.aiscad.agora.comunication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.aiscad.agora.map.GeoTools;
import com.aiscad.agora.objects.Debat;
import com.aiscad.agora.objects.Idea;
import com.aiscad.agora.objects.Proposta;
import com.aiscad.agora.objects.Twiit;

public class HttpHandler {

	public static String TWITTER_USER = "barcelona_cat";
	private static String TWITTER_API_URL = "http://api.twitter.com/1/statuses/user_timeline.json?screen_name="+TWITTER_USER;
	

	
	
	//Retorna los twits de el usuario llamado en TWITTER_API_URL
	public static ArrayList<Twiit> getTwits(){
		ArrayList<Twiit> twiits = new ArrayList<Twiit>();
		String resultstring = "";
		InputStream is = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(TWITTER_API_URL);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			//recogemos string del httpget
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);	
			}
			is.close();
			resultstring = sb.toString();
//			Log.i("HTTPHandler","resultstring: "+resultstring );
			
		} catch (Exception e) {
			Log.e("TestConnection.getTastArray()", "Error limpiar string " + e.toString());
		}
		
		
		try {
			JSONArray jArray = new JSONArray(resultstring);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				int id = json_data.getInt("id");
				String text = json_data.getString("text");
				
				twiits.add(new Twiit(id, text, 0000000000000));
			}

		} catch (JSONException e) {
			Log.e("TestConnection.getTastArray()", "Error parsing data " + e.toString());
		}
		
		return twiits;
	}
	
	
	//retorna Objectes proposta
	public static ArrayList<Idea> getIdees(){
		ArrayList<Idea> idees = new ArrayList<Idea>();
		idees.add(new Idea(0, 
				"",
				"Condicionaments dels carrers interior del carrer del Maresme amb el carrer pujades.", 
				10, 
				20, 
				"4 dies",
				GeoTools.getGeopoint(41.413831,2.21369)));
		idees.add(new Idea(0, 
				"",
				"Desenvolupar un procés de propostes per afavorir l’ús de les instal•lacions esportives del Carrer Josep Pla amb Veneçuela pels nens del barri .", 
				2, 
				5, 
				"4 dies",
				GeoTools.getGeopoint(41.41231,2.210333)));
		idees.add(new Idea(0, 
				"",
				"Remodelació de l’accés dels vehicles pel carrer del Treball entre Pere IV i el carrer Cristobal de Moure.", 
				1, 
				50, 
				"4 dies",
				GeoTools.getGeopoint(41.409776,2.203615)));
		idees.add(new Idea(0, 
				"",
				"Propostes d’ubicació dels contenidors de recollida selectiva al Carrer Puigcerda tram baix.", 
				3, 
				4, 
				"4 dies",
				GeoTools.getGeopoint(41.412986,2.212917)));
		
		return idees;
	}
	
	//retorna Objectes proposta
	public static ArrayList<Proposta> getPropostes(){
		ArrayList<Proposta> propostes = new ArrayList<Proposta>();
		propostes.add(new Proposta(0,
				"Consulta sobre la ordenació del accessos al Parc Güell",
				"Creus necessari augmentar la zona de aparcaments per autocars?",
				null,
				"fins 12/11/1012"
				));
		propostes.add(new Proposta(0,
				"Consulta sobre la ordenació del accessos al Parc Güell",
				"Creus que el cobrament d'entrada al Parc Güell seria bona per la gestió del Parc",
				null,
				"fins 12/11/1012"
				));
		propostes.add(new Proposta(0,
				"Consulta sobre la ordenació del accessos al Parc Güell",
				"Tancaries l'accés superior del Parc per la entrada de visitants",
				null,
				"fins 12/11/1012"
				));
		propostes.add(new Proposta(0,
				"Consulta sobre la ordenació del accessos al Parc Güell",
				"Creus que els veins també tindrian que pagar",
				null,
				"fins 12/11/1012"
				));
		
		return propostes;
	}
	
	//retorna objectes debat
	public static ArrayList<Debat> getDebats(){
		ArrayList<Debat> debats = new ArrayList<Debat>();
		debats.add(new Debat(
				"Taula de treball del Park Güell", 
				"La sessió te com objectiu recollir les diferents propostes col•lectives per aconseguir que el Parc Güell segueixi essent un parc urbà d’accés lliure, gratuït i de qualitat.", 
				Debat.PENDENT,
				GeoTools.getGeopoint(41.411337,2.145732), 
				null,
				"26/09/12",
				"Av. Vallcarca, nº 91, baixos. Barcelona ",
				"FAVB - Esther Argelich \nAssociació d'amics del Parc Güell\nPilar Rafales",
				"http://apvallcarca.wordpress.com/about/"
				));
		
		debats.add(new Debat(
				"16 portes a Collserola",  
				"Procés participatiu per integrar Collserola, urbanísticament i en ús ciutadà, a Barcelona. L'Ajuntament de Barcelona va posar en marxa un projecte per ordenar i connectar la ciutat i el Parc natural a través de 16 Portes o corredors verds i equipaments.", 
				Debat.PENDENT,
				GeoTools.getGeopoint(41.411337,2.145732), 
				null,
				"1 dia",
				"",
				"",
				""
				));
		

		return debats;
	}
	

	
	
	
//	//Retorna Objecte RSSItem de un url-feed
//	public static ArrayList<RSSItem> getRSS(String rssFeed){
//		ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
//		
//		XMLParser xmlparser = new XMLParser();
//		String xml = xmlparser.getXML(rssFeed);
//		Document doc = xmlparser.XMLfromString(xml);
//		doc.normalize();
//		NodeList nl = doc.getElementsByTagName(XMLParser.KEY_ITEM);
//		
//		Log.i("RSSPARSE","items - " + nl.getLength());
//		for (int i = 0; i < nl.getLength(); i++) {
//			
//			 Element e = (Element) nl.item(i);
//			 String content = "none";
//			 
//			 if (xmlparser.getValue(e, XMLParser.ALT_KEY_CONTENT).compareTo("")!=0){
//				 content = xmlparser.getValue(e, XMLParser.ALT_KEY_CONTENT);
//			 }else{
//				 content = xmlparser.getValue(e, XMLParser.KEY_CONTENT);
//			 }
//
//			
////			 // si tiene imagen
//			 org.jsoup.nodes.Document jdoc = Jsoup.parse(content);
//			 Elements element = jdoc.getElementsByTag("img");
//			 String imgUrl =element.attr("src");
////			 
//			 
//			 String title = Html.fromHtml(xmlparser.getValue(e, XMLParser.KEY_TITLE)).toString();
//			 String date = xmlparser.getValue(e,XMLParser.KEY_DATE);
////			 date = date.substring(0,date.length()-5);
////			 content= content.substring(0, content.indexOf("<br/>")).replace("&quot;", "\"");
//			 
//			 RSSItem item = new RSSItem( title, content, date);
//			 item.setImageUrl(imgUrl);
//			 rssItems.add(item);
//			
//		}
//		return rssItems;		
//	}
	
}
