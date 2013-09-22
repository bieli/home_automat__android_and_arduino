package net.bieli.HomeAutomation;

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
import net.bieli.HomeAutomation.Services.HaHttp.HAService;

/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends Activity {
	private static final String LOG_TAG = "HA";
	private static final String DEFAULT_URI = "http://192.168.1.5/ha.php";
	private String ServicveUrl = "";

	TextView result;
	HAMessage haMessage;
	HAService haService; 

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		Toast.makeText(
			getApplicationContext(), 
			"Welcome in HA app ...",
			Toast.LENGTH_LONG
		).show();
		
		setServicveUrl(DEFAULT_URI);

		this.haService = new HAService();

		this.haService.setUri(ServicveUrl);
		this.haService.setLoggerTag(LOG_TAG);	
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
		EditText EditTextUri = (EditText) findViewById(R.id.http_address);
		String uri = EditTextUri.getText().toString().trim();

        this.setServicveUrl(uri);

    	Log.v(LOG_TAG, "change URI -> '" + uri + "'");
    }

	private void setServicveUrl(String servicveUrl) {
		final CharSequence text = servicveUrl.subSequence(0, servicveUrl.length());
		
		EditText EditTextUri = (EditText) findViewById(R.id.http_address);
		EditTextUri.setText((CharSequence) text);

		this.ServicveUrl = servicveUrl;
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

		haMessage = new HAMessage();

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

		result = (TextView)findViewById(R.id.textView2);

		Boolean status = this.haService.send(haMessage);

		if (status == true) {			
			String onoff = "";
			
			if (haMessage.getMask() == 1) {
				onoff = "ON";
			} else {
				onoff = "OFF";
			}
			
			onoff = "switch '" + haMessage.getValue() + "' -> '" + onoff + "'";
			
			Log.v(LOG_TAG, onoff);
			
			StringBuffer sb = this.haService.getOutputStringBuffer(); 
			
			result.setText(sb.toString());
			
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

