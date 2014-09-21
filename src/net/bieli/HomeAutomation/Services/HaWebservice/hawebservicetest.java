package net.bieli.HomeAutomation.Services.HaWebservice;

import java.util.HashSet;
import java.util.Set;

import net.bieli.HomeAutomation.Services.HaWebservice.model.UserData;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public final class hawebservicetest {
	HaWebservice haWebservice;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String token = "test123";
		Integer userId = 123;

		HaWebservice haWebservice = new HaWebserviceImpl(
			new AndroidHttpClient(),
			userId,
			token,
			new net.bieli.HomeAutomation.Utils.CSV()
		);
	
		String uri = "http://127.0.0.1:1234";
		haWebservice.setUri(uri);

		UserData userData = new UserData("Relay 2", "1");

		Set<UserData> userDataSet = new HashSet<UserData>();
		userDataSet.add(userData);

		haWebservice.setUsersDataSet(userDataSet);

		Set<UserData> data = haWebservice.getAll();

		//haWebservice.put("Relay 2", "1");
		//haWebservice.delete("Relay 4");
	}

}
