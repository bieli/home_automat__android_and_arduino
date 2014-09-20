package net.bieli.HomeAutomation.Services.HaWebservice;

import java.util.Set;

import net.bieli.HomeAutomation.Services.HaWebservice.model.UserData;
import net.bieli.HomeAutomation.Services.HaWebservice.model.UserDataCollection;

import com.turbomanage.httpclient.BasicHttpClient;

public interface HaWebservice {
	public void setUri(String uri);
	public void setUsersDataSet(Set<UserData> userDataSet);
	public boolean post();
	public boolean put(String key, String value);
	public Set<UserData> getAll();
	public boolean delete(String key);
}
