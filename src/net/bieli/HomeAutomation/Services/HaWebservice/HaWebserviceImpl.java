package net.bieli.HomeAutomation.Services.HaWebservice;

import java.util.List;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

public class HaWebserviceImpl implements HaWebservice {
	public static final String DEFAULT_PATH_PATTERN = "/v1/feed/%d.csv";
	public static final String HA_TOKEN_SEPARATOR = "#";
	public static final String HA_TOKEN_NAME = "HA-TOKEN";
	private static final Integer DEFAULT_CONNECTION_TIMEOUT = 2000;
	private static final Integer DEFAULT_READ_TIMEOUT = 1000;

	private Integer userId;
	private String token;
	private String uri;
	private List<UserData> usersDataList;
	private BasicHttpClient httpClient;
	private ParameterMap params;

	@Override
	public void setUserId(Integer userId) {
		this.userId = userId;
		uri = String.format(DEFAULT_PATH_PATTERN, userId);
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public void setUsersData(List<UserData> usersDataList) {
		this.usersDataList = usersDataList;
	}

	@Override
	public void setHttpClient(BasicHttpClient httpClient) {
		this.httpClient = httpClient;

		httpClient.addHeader(HA_TOKEN_NAME, prepareToken(userId, token));
        httpClient.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        httpClient.setReadTimeout(DEFAULT_READ_TIMEOUT);

//        params = httpClient.newParams().add("param1", "paramValue");
	}

	@Override
	public boolean post() {
        HttpResponse httpResponse = httpClient.post(uri, params);
        String body = httpResponse.getBodyAsString();

		return false;
	}

	@Override
	public boolean put() {
		byte[] data = {0x45, 0x22};
        HttpResponse httpResponse = httpClient.put(uri, "text/html", data);
        String body = httpResponse.getBodyAsString();

		return false;
	}

	@Override
	public List<UserData> get() {
        HttpResponse httpResponse = httpClient.get(uri, params);
        String body = httpResponse.getBodyAsString();

		return null;
	}

	private String prepareToken(Integer userId, String token) {
		return new StringBuilder(token).reverse().toString()
			+ HA_TOKEN_SEPARATOR
			+ new StringBuilder(Integer.toString(userId)).reverse().toString();
	}
}
