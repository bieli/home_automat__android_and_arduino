package net.bieli.HomeAutomation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

/**
 * HAService class
 * 
 * Send HTTP POST with data for Arduino open hardware.
 * We sending bit( 0 | 1 | 2 | 3) and state (1 = ON | 0 = OFF) dataset.
 * Arduino has network interface with hardcoded tcp/ip location.
 * Arduino waiting for HTTP POST dataset for parsing and controlling four relays.
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone.    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class HAService {
	private String uri = "";
	
	private static Log logger;
	
	private String logTag = "";
	
	private StringBuffer outputStringBuffer;

	public HAService() { }
	
	public void setLoggerTag(String logTag) { 
		this.logTag = logTag;
	}
	
	public Boolean send(HAMessage message) {
		Boolean status = true;
		String state = "";
		
		if (message.getState() == true) {
			state = "1";			
		} else {
			state = "0";
		} 
		
		switch (message.getBit()) {
			case 0:
				status = httpServiceRequest(state, "", "", "", "", "", "", "");
				break;
			case 1:
				status = httpServiceRequest("", state, "", "", "", "", "", "");
				break;
			case 2:
				status = httpServiceRequest("", "", state, "", "", "", "", "");
				break;
			case 3:
				status = httpServiceRequest("", "", "", state, "", "", "", "");
				break;
			default:
				logger.e(logTag, "Unknown BIT '" + message.getBit() + "' - NOT IMPLEMENTED !!!");
				break;
		}

		return status;
	}

	/**
	 * @deprecated NEED HARD REFACTORING !!!
	 * 
	 * @param relay_1_state
	 * @param relay_2_state
	 * @param relay_3_state
	 * @param relay_4_state
	 * @param relay_1_name
	 * @param relay_2_name
	 * @param relay_3_name
	 * @param relay_4_name
	 * @return
	 */
	private Boolean httpServiceRequest(
			String relay_1_state,
			String relay_2_state,
			String relay_3_state,
			String relay_4_state,
			String relay_1_name,
			String relay_2_name,
			String relay_3_name,
			String relay_4_name) {
		
		Boolean result = true;
		
		outputStringBuffer = new StringBuffer("");
		
        logger.v(logTag, "Http POST STARTING ...");

		BufferedReader bufferedReader = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(getUri());
		
		request.addHeader("additional-header", "test-add-header-value");

		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		if (relay_1_state.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_1_state", relay_1_state));
		}

		if (relay_2_state.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_2_state", relay_2_state));
		}

		if (relay_3_state.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_3_state", relay_3_state));
		}

		if (relay_4_state.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_4_state", relay_4_state));
		}

		if (relay_1_name.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_1_name", relay_1_name));
		}

		if (relay_2_name.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_2_name", relay_2_name));
		}

		if (relay_3_name.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_3_name", relay_3_name));
		}

		if (relay_4_name.length() > 0) {
			postParameters.add(new BasicNameValuePair("relay_4_name", relay_4_name));
		}

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(entity);

			logger.v(logTag, "Just about to send http request to " + request.getURI());
			HttpResponse response = httpClient.execute(request);
		
			logger.v(logTag, "Received http response..");		
			logger.v(logTag, response.toString());

			bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent())
			);

			String line = "";
			String LineSeparator = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				outputStringBuffer.append(line + LineSeparator); 
			}

			bufferedReader.close();

			logger.v(logTag, "Finished");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.e(logTag, e.toString());
			result = false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.e(logTag, e.toString());
			result = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.e(logTag, e.toString());
			result = false;
		} finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
					logger.v(logTag, "bufferedReader.close()");
				} catch (IOException e) {
					e.printStackTrace();
					logger.v(logTag, e.toString());
					result = false;
				}
			}
		}
        
		logger.v(logTag, "Http POST END.");

		return result;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public StringBuffer getOutputStringBuffer() {
		return outputStringBuffer;
	}
}
