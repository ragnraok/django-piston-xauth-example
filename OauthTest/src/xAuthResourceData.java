import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

/**
 * 存放获取资源所需数据的类，即为放在Authorization header中的数据
 * @author ragnarok
 *
 */
public class xAuthResourceData {
	
	private String resourceSite = null;
	
	private String consumerKey = null;
	private String token = null;
	private String signature = null;
	/**
	 * 签名方法，默认为 HMAC-SHA1
	 */
	private String signatureMethod = "HMAC-SHA1";
	private String timestamp = null;
	private String tokenSecret = null;
	private String consumerSecret = null;
	private String method = null;
	
	private String[][] data = new String[][] {
			//{"oauth_consumer_key", ""},
			//{"oauth_token", ""},
			//{"oauth_signature", ""},
			//{"oauth_signature_method", ""},
			//{"oauth_timestamp", ""},
			//{"oauth_nonce", ""}
			{"oauth_consumer_key", ""},
			{"oauth_nonce", ""},
			{"oauth_signature_method", ""},
			{"oauth_timestamp", ""},
			{"oauth_token", ""},
			{"oauth_signature", ""}
			
			/**
			 * oauth_consumerkey
			 * oauth_nonce
			 * oauth_signature_method
			 * oauth_timestamp
			 * oauth_token
			 */
	};
	
	/**
	 * 存放获取资源所需数据的类，即为放在Authorization header中的数据
	 * @param resourceSite
	 * @param consumerKey
	 * @param accessToken access token
	 * @param signature
	 * @param timestamp
	 * @param tokenSecret access token secret
	 * @param method 只能为post后者get(不限大小写),否则无法获取资源
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public xAuthResourceData(String resourceSite, String consumerKey, String accessToken,
			String timestamp, String tokenSecret, String consumerSecret, String method) {
		this.resourceSite = resourceSite;
		this.consumerKey = consumerKey;
		this.token = accessToken;
		//this.signatureMethod = signatureMethod;
		this.timestamp = timestamp;
		this.tokenSecret = tokenSecret;
		this.consumerSecret = consumerSecret;
		
		this.method = method.toUpperCase();
		
		//this.signature  = generateSignature();
		
		updateData();
	}
	
	private void updateData() {
		/*
		data[0][1] = consumerKey;
		data[1][1] = token;
		data[2][1] = signature;
		data[3][1] = signatureMethod;
		data[4][1] = timestamp;
		data[5][1] = set_nonce();*/
		/**
		 * oauth_consumerkey
		 * oauth_nonce
		 * oauth_signature_method
		 * oauth_timestamp
		 * oauth_token
		 * oauth_signature
		 */
		
		data[0][1] = consumerKey;
		data[1][1] = set_nonce();
		data[2][1] = signatureMethod;
		data[3][1] = timestamp;
		data[4][1] = token;
		try {
			data[5][1] = this.generateSignature();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获得最后存放在Authorization header里面的数据，以用于获取resource
	 * @return
	 */
	public String getAuthorizationData() {
		String header = "OAuth ";
		for (String[] item : data) {
			header += item[0]+"="+item[1]+", ";
		}
		// cut off last appended comma
		header = header.substring(0, header.length()-2);
		
		return header;
	}
	
	private String generateSignature() 
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		/**
         * Generation of the signature base string
         */
        String signature_base_string = 
            this.method + "&"+URLEncoder.encode(resourceSite, "UTF-8")+"&";
        for(int i = 0; i < data.length; i++) {
            // ignore the empty oauth_signature field
            if(i != 5) {
            signature_base_string +=
                URLEncoder.encode(data[i][0], "UTF-8") + "%3D" +
                URLEncoder.encode(data[i][1], "UTF-8") + "%26";
            }
        }
        // cut the last appended %26 
        signature_base_string = signature_base_string.substring(0,
            signature_base_string.length()-3);
        
        System.out.println(signature_base_string);
       
		
		/**
         * Sign the request
         */
        Mac m = Mac.getInstance("HmacSHA1");
        // if get the resource, the token secret is the key
        m.init(new SecretKeySpec((consumerSecret + "&" + tokenSecret).getBytes(), "HmacSHA1"));
        //m.update(signature_base_string.getBytes());
        byte[] res = m.doFinal(signature_base_string.getBytes());
        String sig = String.valueOf(new BASE64Encoder().encode(res));
        
        return sig;
	}
	
	private String set_nonce() {
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
