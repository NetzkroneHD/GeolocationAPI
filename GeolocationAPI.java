package de.netzkronehd.geolocationapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author NetzkroneHD
 * @version 1.0
 *
 */
public class GeolocationAPI {

	public static final HashMap<String, GeolocationInfo> CACHE = new HashMap<String, GeolocationInfo>(); 
	public static final HashMap<String, GeolocationInfo> LANGUAGE_CACHE = new HashMap<String, GeolocationInfo>(); 
	
	
	/**
	 * 
	 * @see #getGeolocation(String)
	 * 
	 */
	
	public static GeolocationInfo getGeolocation(InetSocketAddress adress) throws MalformedURLException, IOException {
		return getGeolocation(adress.getHostName());
	}
	
	
	/**
	 * 
	 * @param ip
	 * 	The raw Ip-Adress (127.0.0.1)
	 * @return {@link GeolocationInfo} (default language is english)
	 * @throws MalformedURLException
	 * @throws IOException
	 */
    
	
	
	@SuppressWarnings("deprecation")
	public static GeolocationInfo getGeolocation(String ip) throws MalformedURLException, IOException {
		if(CACHE.containsKey(ip)) {
			return CACHE.get(ip);
		}
		final URL url = new URL("http://ip-api.com/json/"+ip+"?fields=status,message,country,countryCode,region,regionName,city,zip,lat,lon,timezone,isp,org,as,proxy,query");
		final BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
		final StringBuilder entirePage = new StringBuilder();
		String inputLine;
		while ((inputLine = stream.readLine()) != null)
			entirePage.append(inputLine);
		stream.close();
		
		final String entire = entirePage.toString();
		final JsonParser jp = new JsonParser();
		final JsonElement je = jp.parse(entire);
		if(je.isJsonObject()) {
			final JsonObject jo = je.getAsJsonObject();
			
			final GeolocationInfo info = new GeolocationInfo("en", jo.get("country").getAsString(), jo.get("city").getAsString(), jo.get("countryCode").getAsString(), jo.get("isp").getAsString(), jo.get("regionName").getAsString(), jo.get("proxy").getAsBoolean());
			CACHE.put(ip, info);
			return info;
			
		}
		
		return null;
	}
	
	/**
	 * 
	 * @see #getGeolocation(String, String)
	 */
    
	public static GeolocationInfo getGeolocation(InetSocketAddress adress, String languageCode) throws MalformedURLException, IOException {
		return getGeolocation(adress.getHostName(), languageCode);
	}
	
	/**
	 * 
	 * @param ip
	 * 	The raw Ip-Adress (127.0.0.1)
	 * @param languageCode
	 * 	de Deutsch (German) 
	 * 	en English (default) 
	 * 	es Español (Spanish)
	 * 	fr Français (French)
	 * 	ja 日本語 (Japanese)
	 * 	pt-BR Español - Argentina (Spanish)
	 * 	ru Русский (Russian)
	 * 	zh-CN 中国 (Chinese)
	 * 
	 * @return {@link GeolocationInfo}
	 * @throws MalformedURLException
	 * @throws IOException
	 */
    
	
	@SuppressWarnings("deprecation")
	public static GeolocationInfo getGeolocation(String ip, String languageCode) throws MalformedURLException, IOException {
		if(LANGUAGE_CACHE.containsKey(ip)) {
			return LANGUAGE_CACHE.get(ip);
		}
		final URL url = new URL("http://ip-api.com/json/"+ip+"?lang="+languageCode+"&fields=180767");
		final BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
		final StringBuilder entirePage = new StringBuilder();
		String inputLine;
		while ((inputLine = stream.readLine()) != null)
			entirePage.append(inputLine);
		stream.close();
		
		final String entire = entirePage.toString();
		final JsonParser jp = new JsonParser();
		final JsonElement je = jp.parse(entire);
		if(je.isJsonObject()) {
			final JsonObject jo = je.getAsJsonObject();
			
			final GeolocationInfo info = new GeolocationInfo(languageCode, jo.get("country").getAsString(), jo.get("city").getAsString(), jo.get("countryCode").getAsString(), jo.get("isp").getAsString(), jo.get("regionName").getAsString(), jo.get("proxy").getAsBoolean());
			LANGUAGE_CACHE.put(ip, info);
			return info;
			
		}
		
		return null;
	}
	
}
