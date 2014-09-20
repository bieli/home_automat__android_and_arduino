package net.bieli.HomeAutomation.Services.HaWebservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLEngineResult.Status;

import net.bieli.HomeAutomation.Services.HaWebservice.model.UserData;
import net.bieli.HomeAutomation.Utils.CSV;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

public class HaWebserviceImpl implements HaWebservice {
	public static final String DEFAULT_PATH_PATTERN = "/v1/feed/%d.csv";
	public static final String HA_TOKEN_SEPARATOR = "#";
	public static final String KEY_VALUE_SEPARATOR = ",";
	public static final String HA_TOKEN_NAME = "HA-TOKEN";
	private static final Integer DEFAULT_CONNECTION_TIMEOUT = 2000;
	private static final Integer DEFAULT_READ_TIMEOUT = 1000;

	private Integer userId;
	private String token;
	private String uri;
	private String path;
	private Set<UserData> userDataSet;
	private BasicHttpClient httpClient;
	private ParameterMap params;
	private CSV csv;

	
	public HaWebserviceImpl(
		BasicHttpClient httpClient,
		Integer userId,
		String token,
		CSV csv
	) {
		this.httpClient = httpClient;

		this.userId = userId;
		this.path = String.format(DEFAULT_PATH_PATTERN, userId);

		httpClient.addHeader(HA_TOKEN_NAME, prepareToken(userId, token));
        httpClient.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        httpClient.setReadTimeout(DEFAULT_READ_TIMEOUT);

        params = httpClient.newParams();

        	//.add("param1", "paramValue");
        
        this.csv = csv;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri + path;
	}

	@Override
	public void setUsersDataSet(Set<UserData> userDataSet) {
		this.userDataSet = userDataSet;
	}

	@Override
	public boolean post() {
        HttpResponse httpResponse = httpClient.post(uri, params);
        String body = httpResponse.getBodyAsString();

		return false;
	}

	@Override
	public boolean put(String key, String value) {
		String datStr = key + KEY_VALUE_SEPARATOR + value;
        HttpResponse httpResponse = httpClient.put(
			uri,
			"text/html",
			datStr.getBytes()
		);
//        String body = httpResponse.getBodyAsString();

        if (200 == httpResponse.getStatus()) {
        	return true;
        } else {
        	System.err.println("httpResponse.getStatus(): " + httpResponse.getStatus());
        	return false;
        }
	}

	@Override
	public Set<UserData> getAll() {
		params.clear();
        HttpResponse httpResponse = httpClient.get(uri, params);
        String body = httpResponse.getBodyAsString();

    	System.out.println("getAll() body START... \n" + body);
    	System.out.println("getAll() body END. \n");

    	Set<UserData> outputData = new HashSet<UserData>();
    	String[] csvLine = body.split("\n");
    	for (String line : csvLine) {
    		ArrayList<String> values = (ArrayList<String>) csv.parse(line);


    		UserData dataObj;
    	    try {
        		String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        	    Date datef = new Date();
        	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        	    String stringDate = sdf.format(datef);
    	        Date dateFormated = sdf.parse(values.get(0));

        		dataObj = new UserData(
        				dateFormated,
        				values.get(1),
        				values.get(2)
        			);
    	    } catch(ParseException e) {
				dataObj = new UserData(
					values.get(1),
					values.get(2)
				);
    	    }
			outputData.add(dataObj);
    	}

		return outputData;
	}

	private String prepareToken(Integer userId, String token) {
		return new StringBuilder(token).reverse().toString()
			+ HA_TOKEN_SEPARATOR
			+ new StringBuilder(Integer.toString(userId)).reverse().toString();
	}

	@Override
	public boolean delete(String key) {
		params.clear();
		httpClient.addHeader("data", key);
        HttpResponse httpResponse = httpClient.delete(uri, params);
        
        if (200 == httpResponse.getStatus()) {
        	return true;
        } else {
        	System.err.println("httpResponse.getStatus(): " + httpResponse.getStatus());
        	return false;
        }
	}

}
