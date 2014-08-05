package net.bieli.HomeAutomation.Services.HaHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import net.bieli.HomeAutomation.Services.HAMessageType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.inject.Inject;

/**
 * HAServiceImpl class
 * 
 * Send HTTP POST with data for Arduino open hardware.
 * We sending bit( 0 | 1 | 2 | 3) and state (1 = ON | 0 = OFF) dataset.
 * Arduino has network interface with hardcoded tcp/ip location.
 * Arduino waiting for HTTP POST dataset for parsing and controlling four relays.
 * Relays are connected with lamps in room and we can controlling those lamps from Android phone.    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class HAServiceImpl implements HAService {
    private String serviceUri = "";
    private String logTag = "";
    private StringBuffer outputStringBuffer;
    private String token = "defaultTOKEN";

    @Inject
    private DefaultHttpClient httpClient;

    @Inject
    private HttpPost request;

    @Inject
    private URI uri;

    @Inject
    private Log logger;

    public HAServiceImpl() { }

	public Boolean send(HAMessage message) {
		Boolean result = true;
		String state = "";

		if (message.getMask() == 1) {
			state = "1";			
		} else {
			state = "0";
		} 
		
		outputStringBuffer = new StringBuffer("");
		
        logger.v(logTag, "HTTP POST starting ...");

		BufferedReader bufferedReader = null;

        uri.resolve(getServiceUri());
        request.setURI(uri);
		
		request.addHeader("HA-SEND-MESAGE", "test-add-header-value");
        request.addHeader("HA-Token", token);
        logger.v(logTag, "add header 'HA-Token': '" + token + "'");

		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		
		switch (message.getMessageType()) {
			case HAMessageType.SET_OUTPUT_DIGITAL:
				break;
			default:
				logger.e(
                    logTag,
                    "Unknown MESSAGE_TYPE '" + message.getMessageType() + "' - NOT IMPLEMENTED !!!"
                );
				return false;
		}

		switch (message.getValue()) {
			case 0:
			case 1:
			case 2:
			case 3:
				postParameters.add(
					new BasicNameValuePair(
						"SET_OUTPUT_DIGITAL#" + message.getMessageType() + "#" + message.getValue(), 
						state
					)
				);
				break;
			default:
				logger.e(
                    logTag,
                    "Unknown BIT '" + message.getValue() + "' - NOT IMPLEMENTED !!!"
                );
				return false;
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

	public int recive() {
		return 0;
	}

    public void setLoggerTag(String logTag) {
        this.logTag = logTag;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceUri() {
		return serviceUri;
	}

    public void setServiceUri(String serviceUri) {
		this.serviceUri = serviceUri;
	}

	public StringBuffer getOutputStringBuffer() {
		return outputStringBuffer;
	}
}
