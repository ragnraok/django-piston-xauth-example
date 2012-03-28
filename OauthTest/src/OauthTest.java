import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;


public class OauthTest {
	public static final String CONSUMER_KEY = "2Tk7LcRnykRdTV4PCe";
	public static final String CONSUMER_SECRET = "utf5ZCp6L8ZYY3ukkq2bRxjjwNsmm4nG";
	public static final String SIGNATURE_METHOD = "HMAC-SHA1";
	
	public static final String SITE = "http://127.0.0.1:8000";
	
	
	public static void main(String[] args) {
		//getRequestToken();
		AccessTokenGetter tokenGetter = new AccessTokenGetter(SITE + "/oauth/access_token/", 
				"tom", "tom", CONSUMER_KEY, CONSUMER_SECRET);
		
		String[] tokenArray = tokenGetter.getAccessToken();
		//System.out.println(tokenArray[0]); // access token
		//System.out.println(tokenArray[1]); // access token secret
		//System.out.println(tokenArray[2]); // access token verify
		
		String token = tokenArray[0];
		String tokenSecret = tokenArray[1];
		
		ResourceDataGetter resourceGetter = new ResourceDataGetter(SITE  +"/name/", token, tokenSecret, CONSUMER_KEY, 
				CONSUMER_SECRET, "GET");
		System.out.println(resourceGetter.getResource());
		
	}
	
	
	public static String set_nonce() {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < 18; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		
		return sb.toString();
	}
	
	
	
	
	
	
	
}
