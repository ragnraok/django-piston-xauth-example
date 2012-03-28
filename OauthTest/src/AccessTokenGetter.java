import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 获取access token的类
 * @author ragnarok
 *
 */
public class AccessTokenGetter {
	
	private String accessTokenSite = null;
	private String userName = null;
	private String userPassword = null;
	
	private String consumerKey = null;
	private String consumerSecret = null;
	
	private xAuthAccessTokenData data = null;
	
	/**
	 * the signature method, HMAC-SHA1
	 */
	private String SIGNATURE_METHOD = "HMAC-SHA1"; 
	
	/**
	 * 获取access token的类
	 * @param accessTokenSite 获取access token的地址
	 * @param userName
	 * @param userPassword
	 * @param consumerKey
	 * @param consumerSecret
	 */
	public AccessTokenGetter(String accessTokenSite, String userName, String userPassword, 
			String consumerKey, String consumerSecret) {
		this.accessTokenSite = accessTokenSite;
		this.userName = userName;
		this.userPassword = userPassword;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}
	
	private void setData() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		data = new xAuthAccessTokenData(consumerKey, 
				String.valueOf(new Date().getTime()).substring(0, 10),
				"tom", "tom", accessTokenSite, consumerSecret);
	}
	
	/**
	 * 获得access token 和 access token secret
	 * @return 一个String数组，
	 * 第一个元素为access token, 第二个元素为 access token secret, 第三个元素为 access token verify
	 */
	public String[] getAccessToken() {
		
		try {
			
			setData();
			
			URL url = new URL(accessTokenSite);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			String header = data.getAuthorizationData();
			
			connection.setRequestProperty("Authorization", header);
			
			connection.connect();
			
			if (connection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
			
				String rawTokenData = reader.readLine();
				
				//System.out.println(rawTokenData);
				
				int tokenStartIndex = rawTokenData.indexOf("&oauth_token=") + "&oauth_token=".length();
				//System.out.println(tokenStartIndex);
				int tokenEndIndex = rawTokenData.indexOf("&oauth_callback_confirmed");
				
				String token = rawTokenData.substring(tokenStartIndex, tokenEndIndex);
				
				int secretStartIndex = rawTokenData.indexOf("oauth_token_secret=") + "oauth_token_secret=".length();
				//System.out.println(secretStartIndex);
				int secretEndIndex = rawTokenData.indexOf("&oauth_verifier");
				
				String secret = rawTokenData.substring(secretStartIndex, secretEndIndex);
				
				int verifyStartIndex = rawTokenData.indexOf("&oauth_verifier=") + "&oauth_verifier=".length();
				int verifyEndIndex = rawTokenData.indexOf("&oauth_token=");
				
				String verify = rawTokenData.substring(verifyStartIndex, verifyEndIndex);
				
				return new String[]{token, secret, verify};
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
