package net.bieli.HomeAutomation.Services.HaWebservice;

import java.util.List;

import com.turbomanage.httpclient.BasicHttpClient;

public interface HaWebservice {
	public void setUserId(Integer userId);
	public void setToken(String token);
	public void setUri(String uri);
	public void setUsersData(List<UserData> usersDataList);
	public void setHttpClient(BasicHttpClient httpClient);
	public boolean post();
	public boolean put();
	public List<UserData> get();
}

