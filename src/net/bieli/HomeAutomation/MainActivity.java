package net.bieli.HomeAutomation;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.turbomanage.httpclient.BasicHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ToggleButton;

import net.bieli.HomeAutomation.R;
import net.bieli.HomeAutomation.Services.HAMessageType;
import net.bieli.HomeAutomation.Services.HaHttp.HAMessage;
import net.bieli.HomeAutomation.Services.HaHttp.HAServiceImpl;
import net.bieli.HomeAutomation.Services.HaWebservice.HaWebservice;
import net.bieli.HomeAutomation.Services.HaWebservice.HaWebserviceImpl;
import net.bieli.HomeAutomation.Services.HaWebservice.model.UserData;
import net.bieli.HomeAutomation.Utils.DeviceIdFactory;


/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends Activity {
	private static final String LOG_TAG = "HA";
	private static final String DEFAULT_URI = "http://192.168.1.5/ha.php";

	Log logger;
	HAMessage haMessage;
	HAServiceImpl haServiceImpl;

	TextView resultTextView;
	EditText editTextUri;
	String serviceUrl;
	TextView tokenTextView;
	private String token;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "MainActivity: onCreate");

		// special code for clickable TextView element
		TextView linkToGithub = (TextView) findViewById(R.id.link_to_github);
		if (linkToGithub != null) {
			linkToGithub.setClickable(true);
			linkToGithub.setMovementMethod(LinkMovementMethod.getInstance());
		}
//TODO: we need link, but in Android 2.1 it is terible story !
//		Linkify.addLinks((TextView) findViewById(R.id.link_to_github), Linkify.ALL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
			initializeComponents();
			initHaWebservice();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void initializeComponents() throws URISyntaxException {
    	haMessage = new HAMessage();

    	haServiceImpl = new HAServiceImpl(new DefaultHttpClient(), new HttpPost(), logger);
        haServiceImpl.setLoggerTag(LOG_TAG);

    	token = DeviceIdFactory.getShortToken(getBaseContext());
    	Log.v(LOG_TAG, "DeviceUuidFactory.getShortToken: '" + token + "'");

        setToken(token);
    	setServiceUrl(DEFAULT_URI);

        Toast.makeText(
                getApplicationContext(),
                "Welcome in HA app ...",
                Toast.LENGTH_LONG
        ).show();
    }
    
    private void initHaWebservice() {
		HaWebservice haWebservice = new HaWebserviceImpl(
			new BasicHttpClient(),
			123,
			"Test123",
			new net.bieli.HomeAutomation.Utils.CSV()
		);
	
		String uri = "http://127.0.0.1:1234";
//		String uri = "http://192.96.201.102:30229"
		haWebservice.setUri(uri);

//		UserData userData = new UserData("Relay 2", "1");

//		Set<UserData> userDataSet = new HashSet<UserData>();
//		userDataSet.add(userData);
//
//		haWebservice.setUsersDataSet(userDataSet);

//		Set<UserData> data = haWebservice.getAll();

		haWebservice.put("Relay 2", "1");
    }

    @Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "MainActivity: onDestory");

		super.onDestroy();
	}

    public void onToggle1Clicked(View view) {
        doHAServiceAction(view, HAMessageType.DIGITAL_DEVICE_0);
    }

    public void onToggle2Clicked(View view) {
        doHAServiceAction(view, HAMessageType.DIGITAL_DEVICE_1);
    }

    public void onToggle3Clicked(View view) {
        doHAServiceAction(view, HAMessageType.DIGITAL_DEVICE_2);
    }
    
    public void onToggle4Clicked(View view) {
        doHAServiceAction(view, HAMessageType.DIGITAL_DEVICE_3);
    }

    public void onAddressUrlClicked(View view) throws URISyntaxException {
        editTextUri = (EditText) findViewById(R.id.http_address);
		String uri = editTextUri.getText().toString().trim();

        this.setServiceUrl(uri);

    	Log.v(LOG_TAG, "change URI -> '" + uri + "'");
    }

    public void onEditTokenClicked(View view) {
    	tokenTextView = (EditText) findViewById(R.id.edit_token);
        String token = tokenTextView.getText().toString().trim();

        setToken(token);

        Log.v(LOG_TAG, "change HA TOKEN -> '" + token + "'");
    }

    private void setServiceUrl(String serviceUrl) throws URISyntaxException {
    	CharSequence serviceUrlText = serviceUrl;

		if (serviceUrlText != null) {
	    	editTextUri = (EditText) findViewById(R.id.http_address);    	
			editTextUri.setText(serviceUrlText);

			Log.v(LOG_TAG, "setServiceUrl http_address: '" + serviceUrlText + "'");

			this.serviceUrl = serviceUrl;

	        haServiceImpl.setServiceUri(serviceUrl);
		}
	}

    private void setToken(String token) {
    	CharSequence tokenText = token;

		if (tokenText != null) {
	    	tokenTextView = (EditText) findViewById(R.id.edit_token);    	
	    	tokenTextView.setText(tokenText);

			Log.v(LOG_TAG, "setToken edit_token: '" + tokenText + "'");

			this.token = token;

	        haServiceImpl.setToken(token);
		}
	}

    private Boolean doHAServiceAction(View view, byte mask) {
        boolean on = ((ToggleButton) view).isChecked();
        byte value = 0;

        Log.v(LOG_TAG, "doHAServiceAction on: '" + String.format("%s", on) + "'");

        if (on) {
        	value = HAMessageType.DIGITAL_STATE_HIGH;
        } else {
        	value = HAMessageType.DIGITAL_STATE_LOW;
        }

        Log.v(LOG_TAG, "doHAServiceAction mask: '" + String.format("%s", mask) + "'");

        view.setClickable(false);
        view.setEnabled(false);

        Log.v(LOG_TAG, "doHAServiceAction set message");

		haMessage.setMessageType(HAMessageType.SET_OUTPUT_DIGITAL);
		haMessage.setValue(value);
		haMessage.setMask(mask);
		haMessage.setRealValue(false);

        view.setEnabled(true);
        view.setClickable(true);

        return sendHAMessageForOutputDigital(haMessage);
	}

	private Boolean sendHAMessageForOutputDigital(HAMessage haMessage) {
        Log.v(LOG_TAG, "sendHAMessageForOutputDigital START");

        // TODO: add other than SET_OUTPUT_DIGITAL types in app.
		if (haMessage.getMessageType() != HAMessageType.SET_OUTPUT_DIGITAL) {
	        Log.v(LOG_TAG, "sendHAMessageForOutputDigital START");

			return false;
		}

		Boolean status = haServiceImpl.send(haMessage);

		Log.v(LOG_TAG, "sendHAMessageForOutputDigital send status: '" + String.format("%s", status) + "'");

		if (status == true) {			
			String onoff = "";
			
			switch (haMessage.getValue()) {
				case HAMessageType.DIGITAL_STATE_HIGH:
					onoff = "ON";
					break;
				case HAMessageType.DIGITAL_STATE_LOW:
					onoff = "OFF";
					break;
			}
			
			onoff = "sendHAMessageForOutputDigital switch '" + haMessage.getValue() + "' -> '" + onoff + "'";
			
			Log.v(LOG_TAG, onoff);
			
			StringBuffer sb = haServiceImpl.getOutputStringBuffer();
			
			Log.v(LOG_TAG, "sendHAMessageForOutputDigital getOutputStringBuffer: \n" + sb);
			resultTextView = (TextView)findViewById(R.id.textView2);
			resultTextView.setText(sb.toString());
			
			Toast.makeText(
				getApplicationContext(), 
				onoff,
				Toast.LENGTH_LONG
			).show();		
		} else {
			Toast.makeText(
				getApplicationContext(), 
				"Error -> when calling HAMessage()", 
				Toast.LENGTH_LONG
			).show();
			
			Log.e(LOG_TAG, "sendHAMessageForOutputDigital: Error -> when calling HAMessage()");
		}
		
        Log.v(LOG_TAG, "sendHAMessageForOutputDigital END");

		return status;
	}
}
