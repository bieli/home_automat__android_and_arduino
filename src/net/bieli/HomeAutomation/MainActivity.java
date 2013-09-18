package net.bieli.HomeAutomation;

//import java.util.logging.ConsoleHandler;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;


//import java.net.URI;

// for internet connectivity state info
//import android.net.ConnectivityManager;
//import android.content.IntentFilter;

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
//	private TextView networkStatus;

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
		

/*
//TODO: add notification about internet connection 
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkChangeReceiver();
		registerReceiver(receiver, filter);
		
		networkStatus = (TextView) findViewById(R.id.networkStatus);
*/

/*
        String address = "http://192.168.1.10/ap.php";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(address);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("relay_1", "1"));
        pairs.add(new BasicNameValuePair("relay_2", "0"));
        pairs.add(new BasicNameValuePair("relay_3", "1"));
        pairs.add(new BasicNameValuePair("relay_4", "1"));
        post.setEntity(new UrlEncodedFormEntity(pairs));

        HttpResponse response = client.execute(post);
*/
//        URI uri = (TextView)findViewById(R.id.http_address);
//        http_address = (TextView)findViewById(R.id.http_address);

//        sendMessage();
//        boolean on = R.id.toggleButton2.isChecked();

        //ToggleButton toggle = findViewById(R.id.toggleButton2);
//        View view = findViewById(R.id.toggleButton2);
        
        //view.getContext()
        
        //onToggle2Clicked
//        boolean on = true; //toggle.isChecked();
/*
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });
        boolean on = true;
*/
/*        
        if (on) {
            // Enable vibrate
        } else {
            // Disable vibrate
        }
*/
      
        
//        R.id.textView1.setText("TEST");
    }

	@Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "onDestory");
		super.onDestroy();
		unregisterReceiver(receiver);
	}

    public void onToggle1Clicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        this.haMessage.setBit((byte) 0);
        this.haMessage.setState(on);
        
        sendHAMessage();
    }

    public void onToggle2Clicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        this.haMessage.setBit((byte) 1);
        this.haMessage.setState(on);
        
        sendHAMessage();
    }

    public void onToggle3Clicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        this.haMessage.setBit((byte) 2);
        this.haMessage.setState(on);
        
        sendHAMessage();
    }
    
    public void onToggle4Clicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        this.haMessage.setBit((byte) 3);
        this.haMessage.setState(on);
        
        sendHAMessage();
    }
    
    public void onAddressUrlClicked(View view) {
		EditText EditTextUri = (EditText) findViewById(R.id.http_address);
		String uri = EditTextUri.getText().toString().trim();

        this.setServicveUrl(uri);

    	Log.v(LOG_TAG, "change URI -> '" + uri + "'");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private void setServicveUrl(String servicveUrl) {
		final CharSequence text = servicveUrl.subSequence(0, servicveUrl.length());
		
		EditText EditTextUri = (EditText) findViewById(R.id.http_address);
		EditTextUri.setText((CharSequence) text);

		ServicveUrl = servicveUrl;
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
			//TODO: add Toaste error message, etc...
			Toast.makeText(
					getApplicationContext(), 
					"Error -> when calling HAMessage()", 
					Toast.LENGTH_LONG
					).show();
		}
	}
}

