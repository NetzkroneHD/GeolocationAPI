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
 *
 */
public class GeolocationAPI {
	
	private static final HashMap<String, GeolocationInfo> CACHE = new HashMap<String, GeolocationInfo>(); 
	
	
	/**
	 * 
	 * @see #getGeolocation(String)
	 * 
	 */
	
	public GeolocationInfo getGeolocation(InetSocketAddress adress) throws MalformedURLException, IOException {
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
    
	
	public GeolocationInfo getGeolocation(String ip) throws MalformedURLException, IOException {
		return getGeolocation(ip, "en");
	}
	
	/**
	 * 
	 * @see #getGeolocation(String, String)
	 */
    
	public GeolocationInfo getGeolocation(InetSocketAddress adress, String languageCode) throws MalformedURLException, IOException {
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
	public GeolocationInfo getGeolocation(String ip, String languageCode) throws MalformedURLException, IOException {
		if(CACHE.containsKey(ip)) {
			return CACHE.get(ip);
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
			
			CACHE.put(ip, info);
			return info;
			
		}
		
		return null;
	}
	
	public class GeolocationInfo {
		private String language, country, city, countryCode, provider, region;
		private boolean vpn;
		
		public GeolocationInfo(String language, String country, String city, String countryCode, String provider, String region, boolean vpn) {
			this.language = language;
			this.country = country;
			this.city = city;
			this.countryCode = countryCode;
			this.provider = provider;
			this.region = region;
			this.vpn = vpn;
		}

		
		public String getLanguage() {
			return language;
		}
		
		public String getCountry() {
			return country;
		}

		public String getCity() {
			return city;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public String getProvider() {
			return provider;
		}

		public String getRegion() {
			return region;
		}

		public boolean isVpn() {
			return vpn;
		}

		@Override
		public String toString() {
			return "GeolocationInfo [country=" + country + ", city=" + city + ", countryCode=" + countryCode + ", provider="
					+ provider + ", region=" + region + ", vpn=" + vpn + "]";
		}
	}
	
}
