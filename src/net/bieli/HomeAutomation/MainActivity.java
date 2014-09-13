package net.bieli.HomeAutomation;

import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
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


/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends Activity {
	private static final String LOG_TAG = "HA";
	private static final String DEFAULT_URI = "http://192.168.1.5/ha.php";
	private static final String DEFAULT_TOKEN = "_token_";

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
			initializeComponents();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void initializeComponents() throws URISyntaxException {
    	haMessage = new HAMessage();

    	haServiceImpl = new HAServiceImpl(new DefaultHttpClient(), new HttpPost(), logger);
        haServiceImpl.setLoggerTag(LOG_TAG);
        
        setToken(DEFAULT_TOKEN);
    	setServiceUrl(DEFAULT_URI);

        Toast.makeText(
                getApplicationContext(),
                "Welcome in HA app ...",
                Toast.LENGTH_LONG
        ).show();
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
			
			switch (haMessage.getMask()) {
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
