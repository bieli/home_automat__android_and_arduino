package net.bieli.HomeAutomation.Services.HaHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import net.bieli.HomeAutomation.Services.HAMessageType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

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
    private String token = "";

    private DefaultHttpClient httpClient;

    private HttpPost request;

    private URI uri;

    private Log logger;

    public HAServiceImpl(DefaultHttpClient httpClient, HttpPost request, Log logger) { 
    	this.httpClient = httpClient;
    	this.request = request;
    	this.logger = logger;
    }

	@SuppressWarnings("static-access")
	public Boolean send(HAMessage message) {
		Boolean result = true;
		outputStringBuffer = new StringBuffer("");
		
        logger.v(logTag, "HTTP POST starting ...");

		BufferedReader bufferedReader = null;

        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		postParameters = preparePostParametersByHAMessage(postParameters, message);

		try {
	        logger.v(logTag, "set URI: '" + getServiceUri() + "'");
			uri.resolve(getServiceUri());
	        request.setURI(uri);

	        logger.v(logTag, "add header 'HA-TOKEN': '" + token + "'");
			request.addHeader("HA-TOKEN", token);

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(entity);

			logger.v(logTag, "Sending http request to " + request.getURI());
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

			logger.v(logTag, "Finished (sending http request)");

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
        
		logger.v(logTag, "HTTP POST END.");

		return result;
	}

	@SuppressWarnings("static-access")
	private List<NameValuePair> preparePostParametersByHAMessage(List<NameValuePair> postParameters, HAMessage message)
	{		
		logger.v(logTag, "preparePostParametersByHAMessage START");

		logger.v(
	        logTag,
	        "MESSAGE_TYPE: '" + message.getMessageType() + "'"
	    );

		switch (message.getMessageType()) {
		case HAMessageType.SET_OUTPUT_DIGITAL:
			postParameters.add(
				new BasicNameValuePair(
					"type", String.format("%s", message.getMessageType())
				)
			);
	
			switch (message.getMask()) {			
				case HAMessageType.DIGITAL_DEVICE_0:
				case HAMessageType.DIGITAL_DEVICE_1:
				case HAMessageType.DIGITAL_DEVICE_2:
				case HAMessageType.DIGITAL_DEVICE_3:
					postParameters.add(
						new BasicNameValuePair(
							"mask", String.format("%s", message.getMask())
						)
					);
	
					break;
				default:
					logger.e(
	                    logTag,
	                    "Unknown MASK '" + message.getMask() + "' - NOT IMPLEMENTED IN SET_OUTPUT_DIGITAL !!!"
	                );
			}
	
			switch (message.getValue()) {
				case HAMessageType.DIGITAL_STATE_LOW:
				case HAMessageType.DIGITAL_STATE_HIGH:
					postParameters.add(
						new BasicNameValuePair(
							"value", String.format("%s", message.getValue())
						)
					);
					break;
				default:
					logger.e(
	                    logTag,
	                    "Unknown BIT '" + message.getValue() + "' - NOT IMPLEMENTED !!!"
	                );
			}
			break;
		case HAMessageType.SET_OUTPUT_ANALOG:
		case HAMessageType.GET_INPUT_DIGITAL:
		case HAMessageType.GET_INPUT_ANALOG:
			break;
		default:
			logger.e(
	            logTag,
	            "Unknown MESSAGE_TYPE '" + message.getMessageType() + "' - NOT IMPLEMENTED !!!"
	        );
		}

		logger.v(logTag, "preparePostParametersByHAMessage END");

		return postParameters;
	}
	
	@SuppressWarnings("static-access")
	public void setLoggerTag(String logTag) {
        this.logTag = logTag;
		logger.v(logTag, "HAServiceImpl -> setLoggerTag: '" + logTag + "'");
    }

	@SuppressWarnings("static-access")
    public void setToken(String token) {
        this.token = token;
		logger.v(logTag, "HAServiceImpl -> setToken: '" + token + "'");
    }

    public String getServiceUri() {
		return serviceUri;
	}

	@SuppressWarnings("static-access")
    public void setServiceUri(String serviceUri) throws URISyntaxException {
    	uri = new URI(serviceUri);
		this.serviceUri = serviceUri;

		logger.v(logTag, "HAServiceImpl -> setServiceUri: '" + serviceUri + "'");
	}

	public StringBuffer getOutputStringBuffer() {
		return outputStringBuffer;
	}
}
