import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 获取资源的类
 * @author ragnarok
 *
 */
public class ResourceDataGetter {
	
	private String resourceSite = null;
	private String accesToken = null;
	private String tokenSecret = null;
	private String consumerKey = null;
	private String method = null; // post or get
	private String consumerSecret = null;
	private String signatureMethod = "HMAC-SHA1";
	
	private xAuthResourceData data;
	
	/**
	 * 获取资源所用的类
	 * @param resourceSite 资源地址
	 * @param accessToken 
	 * @param tokenSecret
	 * @param consumerKey
	 * @param method HTTP方法，只能为POST或者GET，大小写不限
	 */
	public ResourceDataGetter(String resourceSite, String accessToken, String tokenSecret, String consumerKey, 
			 String consumerSecret, String method) {
		this.resourceSite = resourceSite;
		this.accesToken = accessToken;
		this.tokenSecret = tokenSecret;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.method = method.toUpperCase();
		
		
	}
	
	private void setData() {
		this.data = new xAuthResourceData(this.resourceSite, this.consumerKey, this.accesToken,
				String.valueOf(new Date().getTime()).substring(0, 10), this.tokenSecret, 
				this.consumerSecret, this.method.toUpperCase());
	}
	
	/**
	 * 获取到最终资源
	 * @return 资源的字符串表示
	 */
	public String getResource() {
		this.setData();
		
		try {
			URL url = new URL(resourceSite);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			connection.setRequestMethod(this.method);
			
			String header = this.data.getAuthorizationData();
			
			connection.setRequestProperty("Authorization", header);
			
			connection.connect();
			
			if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				
				StringBuffer buffer = new StringBuffer("");
				
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					//System.out.println(line);
					buffer.append(line);
				}
				
				return buffer.toString();
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
