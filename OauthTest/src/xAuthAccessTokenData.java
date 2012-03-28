import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

/**
 * 获得Access Token所需数据的类
 * @author ragnarok
 *
 */
public class xAuthAccessTokenData {

	private String accesTokenSite = null;
	private String consumerSecret = null;
	
	/**
	 * 签名方法，默认为HMAC-SHA1
	 */
	private String signatureMethod = "HMAC-SHA1";
	
	private String[][] data = new String[][]{
			{"oauth_consumer_key", ""},
			{"oauth_nonce", ""},
			{"oauth_signature_method", ""},
			{"oauth_signature", ""},
			{"oauth_timestamp", ""},
			{"x_auth_password", ""},
			{"x_auth_username", ""}
	};
	
	/**
	 * 获得Access Token所需数据的类
	 * @param consumerKey
	 * @param timeStamp
	 * @param userName
	 * @param userPassword
	 * @param accessTokenSite 获取access token 的地址
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws InvalidKeyException 
	 */
	public xAuthAccessTokenData(String consumerKey, String timeStamp, String userName,
			String userPassword, String accessTokenSite, String consumerSecret) 
					throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		data[0][1] = consumerKey;
		data[1][1] = this.set_nonce();
		data[2][1] = this.signatureMethod;
		data[4][1] = timeStamp;
		data[5][1] = userPassword;
		data[6][1] = userName;
		
		this.accesTokenSite = accessTokenSite;
		this.consumerSecret = consumerSecret;
		
		data[3][1] = generateSignature();
	}
	
	/**
	 * 获得最后存放在Authorization header里面的数据，以用于获取access token
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
            "POST&"+URLEncoder.encode(accesTokenSite, "UTF-8")+"&";
        for(int i = 0; i < data.length; i++) {
            // ignore the empty oauth_signature field
            if(i != 3) {
            signature_base_string +=
                URLEncoder.encode(data[i][0], "UTF-8") + "%3D" +
                URLEncoder.encode(data[i][1], "UTF-8") + "%26";
            }
        }
        // cut the last appended %26 
        signature_base_string = signature_base_string.substring(0,
            signature_base_string.length()-3);
        
        //System.out.println(signature_base_string);
       
		
		/**
         * Sign the request
         */
        Mac m = Mac.getInstance("HmacSHA1");
        // if get the access token, the consuemrSecret& is the key
        m.init(new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1"));
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
