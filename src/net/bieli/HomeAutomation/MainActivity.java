package net.bieli.HomeAutomation;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ToggleButton;

import com.google.inject.Inject;

import net.bieli.HomeAutomation.Services.HAMessageType;
import net.bieli.HomeAutomation.Services.HaHttp.HAMessage;
import net.bieli.HomeAutomation.Services.HaHttp.HAServiceImpl;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends RoboActivity {
	private static final String LOG_TAG = "HA";
	private static final String DEFAULT_URI = "http://192.168.1.5/ha.php";
	private String serviceUrl = "";

    @InjectView(R.id.http_address)
    EditText editTextUri;

    @InjectView(R.id.textView2)
    TextView resultTextView;

    @InjectView(R.id.editToken)
    TextView tokenTextView;

    @Inject
    HAServiceImpl haServiceImpl;

    @Inject
    HAMessage haMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		setServiceUrl(DEFAULT_URI);

		haServiceImpl.setServiceUri(serviceUrl);
		haServiceImpl.setLoggerTag(LOG_TAG);

        Toast.makeText(
                getApplicationContext(),
                "Welcome in HA app ...",
                Toast.LENGTH_LONG
        ).show();
    }

	@Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "onDestory");

		super.onDestroy();
	}

    public void onToggle1Clicked(View view) {
        doHAServiceAction(view, (byte) 0);
    }

    public void onToggle2Clicked(View view) {
        doHAServiceAction(view, (byte) 1);
    }

    public void onToggle3Clicked(View view) {
        doHAServiceAction(view, (byte) 2);
    }
    
    public void onToggle4Clicked(View view) {
        doHAServiceAction(view, (byte) 3);
    }

    public void onAddressUrlClicked(View view) {
		String uri = editTextUri.getText().toString().trim();

        this.setServiceUrl(uri);

    	Log.v(LOG_TAG, "change URI -> '" + uri + "'");
    }

    public void onEditTokenClicked(View view) {
        String token = tokenTextView.getText().toString().trim();

        haServiceImpl.setToken(token);

        Log.v(LOG_TAG, "change HA TOKEN -> '" + token + "'");
    }


    private void setServiceUrl(String serviceUrl) {
		final CharSequence text = serviceUrl.subSequence(0, serviceUrl.length());
		
		editTextUri.setText((CharSequence) text);

		this.serviceUrl = serviceUrl;
	}

	private Boolean doHAServiceAction(View view, byte bit) {
        boolean on = ((ToggleButton) view).isChecked();
        byte mask = 0;
        
        if (on) {
        	mask = 1;
        } else {
        	mask = 0;
        }

        view.setClickable(false);
        view.setEnabled(false);

		haMessage.setMessageType(HAMessageType.SET_OUTPUT_DIGITAL);
		haMessage.setValue((byte) bit);
		haMessage.setMask(mask);
		haMessage.setRealValue(false);

        view.setEnabled(true);
        view.setClickable(true);

        return sendHAMessageForOutputDigital(haMessage);
	}

	private Boolean sendHAMessageForOutputDigital(HAMessage haMessage) {
		if (haMessage.getMessageType() != HAMessageType.SET_OUTPUT_DIGITAL) {
			return false;
		}

		Boolean status = haServiceImpl.send(haMessage);

		if (status == true) {			
			String onoff = "";
			
			if (haMessage.getMask() == 1) {
				onoff = "ON";
			} else {
				onoff = "OFF";
			}
			
			onoff = "switch '" + haMessage.getValue() + "' -> '" + onoff + "'";
			
			Log.v(LOG_TAG, onoff);
			
			StringBuffer sb = haServiceImpl.getOutputStringBuffer();
			
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
		}
		
		return status;
	}
}

