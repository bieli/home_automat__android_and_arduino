package net.bieli.HomeAutomation;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;

import net.bieli.HomeAutomation.R;
import net.bieli.HomeAutomation.NetworkChangeReceiver;
import net.bieli.HomeAutomation.HAService;
import net.bieli.HomeAutomation.HAMessage;

/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends Activity {

	private static final String LOG_TAG = "HA";

	private static final String DEFAULT_URI = "http://192.168.1.5/ha.php";
	
	TextView result;

	HAMessage haMessage;
	HAService haService; 

	private String ServicveUrl = "";

	private NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		Toast.makeText(getApplicationContext(), 
		"Welcome in HA app ...", 
		Toast.LENGTH_LONG).show();
		
		setServicveUrl(DEFAULT_URI);
		

		this.haMessage = new HAMessage();
		this.haService = new HAService();

		this.haService.setUri(ServicveUrl);
		this.haService.setLoggerTag(LOG_TAG);	
    }

	@Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "onDestory");

		super.onDestroy();

		try {
			Log.v(LOG_TAG, "unregisterReceiver");

			unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(LOG_TAG, e.toString());
		}
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//	
	private void setServicveUrl(String servicveUrl) {
		final CharSequence text = servicveUrl.subSequence(0, servicveUrl.length());
		
		EditText EditTextUri = (EditText) findViewById(R.id.http_address);
		EditTextUri.setText((CharSequence) text);

		ServicveUrl = servicveUrl;
	}


	private void doHAServiceAction(View view, byte bit) {
		// Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        view.setClickable(false);
        view.setEnabled(false);
        
        this.haMessage.setBit((byte) bit);
        this.haMessage.setState(on);

        view.setClickable(false);
        view.setEnabled(true);
        
        sendHAMessage();
	}

	private void sendHAMessage() {
		result = (TextView)findViewById(R.id.textView2);
		
		Boolean status = this.haService.send(haMessage);
		
		if (status == true) {			
			String onoff = "";
			
			if (haMessage.getState() == true) {
				onoff = "ON";
			} else {
				onoff = "OFF";
			}
			
			onoff = "switch '" + haMessage.getBit() + "' -> '" + onoff + "'";
			
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
	}
}

