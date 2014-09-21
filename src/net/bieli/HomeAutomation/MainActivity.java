package net.bieli.HomeAutomation;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.bieli.HomeAutomation.Services.HAMessageType;
import net.bieli.HomeAutomation.Services.HaHttp.HAMessage;
import net.bieli.HomeAutomation.Services.HaHttp.HAServiceImpl;
import net.bieli.HomeAutomation.Services.HaWebservice.HaWebservice;
import net.bieli.HomeAutomation.Services.HaWebservice.HaWebserviceImpl;
import net.bieli.HomeAutomation.Utils.DeviceIdFactory;
import net.bieli.HomeAutomation.Services.HaWebservice.model.UserData;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.android.AndroidHttpClient;


/**
 * MainActivity Android application class
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class MainActivity extends Activity {
	private static final String LOG_TAG = "HA";
	private static final String DEFAULT_URI = "http://192.96.201.102:30229";

	Log logger;
	HAMessage haMessage;
	HAServiceImpl haServiceImpl;

	TextView resultTextView;
	EditText editTextUri;
	String serviceUrl;
	TextView tokenTextView;
	private String token;
	private HaWebservice haWebservice;
	private Set<UserData> registers;
	final Handler myHandler = new Handler();
	int i = 0;
	TextView textView5;

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
        
        
/*        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
           @Override
           public void run() {
        	   UpdateGUI();
    	   }
        }, 0, 1000);
*/
    }
/*	
	private void UpdateGUI() {
		i++;
		textView5 = (TextView) findViewById(R.id.textView5);
		textView5.setText(String.valueOf(i));
		myHandler.post(myRunnable);
//		myRunnable.run();
//		this.runOnUiThread(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			textView5 = (TextView) findViewById(R.id.textView5);
			textView5.setText(String.valueOf(i));
		}
	};*/

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
		haWebservice = new HaWebserviceImpl(
			new AndroidHttpClient(),
			124,
			token,
			new net.bieli.HomeAutomation.Utils.CSV()
		);

		String uri = serviceUrl;
		haWebservice.setUri(uri);

		updateRegisters();
    }
    
    private void updateRegisters() {
    	Log.d(LOG_TAG, "updateRegisters(): START");

    	registers = haWebservice.getAll();
//    	Log.d(LOG_TAG, "updateRegisters(): registers count: " + String.format("%d", registers.toArray().length));
    	
    	List<EditText> textEditsList = getAllTextEditsFromLayout();
//    	Log.d(LOG_TAG, "updateRegisters(): textEditsList count: " + String.format("%d", textEditsList.toArray().length));
    	
    	for (UserData register : registers) {
//	    	Log.d(LOG_TAG, "updateRegisters(): for (EditText editTextNext : textEditsList)");
    		for (EditText editTextNext : textEditsList) {
//    	    	Log.d(LOG_TAG, "updateRegisters(): if (register.getKey().trim() == editTextNext.getText().toString().trim())");
//    	    	Log.d(LOG_TAG, "updateRegisters(): register.getKey().trim(): '" + register.getKey().trim() + "'");
//    	    	Log.d(LOG_TAG, "updateRegisters(): editTextNext.getText().toString().trim(): '" + editTextNext.getText().toString().trim() + "'");
				if (register.getKey().trim().equals(editTextNext.getText().toString().trim())) {
		        	Log.d(LOG_TAG, "updateRegisters(): found register.getKey().trim(): '" + register.getKey().trim() +"'");

					Integer tb = 0;
					switch (editTextNext.getId()) {
						case R.id.editText1:
				        	Log.d(LOG_TAG, "updateRegisters(): R.id.editText1");
							tb = R.id.toggleButton1;
							break;
						case R.id.editText2:
				        	Log.d(LOG_TAG, "updateRegisters(): R.id.editText2");
							tb = R.id.toggleButton2;
							break;
						case R.id.editText3:
				        	Log.d(LOG_TAG, "updateRegisters(): R.id.editText3");
							tb = R.id.toggleButton3;
							break;
						case R.id.editText4:
				        	Log.d(LOG_TAG, "updateRegisters(): R.id.editText4");
							tb = R.id.toggleButton4;
							break;
						default:
							break;
					}
					if (tb > 0) {
			        	Log.d(LOG_TAG, "updateRegisters(): if (tb > 0) {");
						ToggleButton toggleButton = (ToggleButton) findViewById(tb);
						Boolean checked = false;
			        	Log.d(LOG_TAG, "updateRegisters(): register.getValue().trim(): '" + register.getValue().trim() + "'");
						if (register.getValue().trim().equals("1")) {						
							checked = true;
				        	Log.d(LOG_TAG, "updateRegisters(): tb checked = true");
						} else {
				        	Log.d(LOG_TAG, "updateRegisters(): tb checked = false");
						}
						toggleButton.setChecked(checked);
					}
				}
    		}
    	}

    	Log.d(LOG_TAG, "updateRegisters(): END");
    }
    
    private List<EditText> getAllTextEditsFromLayout() {
    	Log.d(LOG_TAG, "getAllTextEditsFromLayout(): START");
    	List<EditText> output = new ArrayList<EditText>();

    	EditText editText1 = (EditText) findViewById(R.id.editText1);
    	output.add(editText1);
    	EditText editText2 = (EditText) findViewById(R.id.editText2);
    	output.add(editText2);
    	EditText editText3 = (EditText) findViewById(R.id.editText3);
    	output.add(editText3);
    	EditText editText4 = (EditText) findViewById(R.id.editText4);
    	output.add(editText4);

/*
    	ViewGroup parent = (ViewGroup) findViewById(R.layout.activity_main);
    	Integer childs = parent.getChildCount();
//    	Log.d(LOG_TAG, "getAllTextEditsFromLayout() parent.getChildCount(): " + String.format("%d", childs));

    	for(int i = 0; i < childs; i++) {
    		View singleChild = parent.getChildAt(i);
    	    if (singleChild instanceof EditText) {
    	    	Log.d(LOG_TAG, "getAllTextEditsFromLayout() singleChild R.id: " + String.valueOf(singleChild.getId()));

    	        EditText mEditText = (EditText) singleChild;

    	        if(mEditText.getText().length() >  0){
    	            Log.d(LOG_TAG, "getAllTextEditsFromLayout(): EditText is NOT NULL - value: '" + mEditText.getText().toString() + "'");
    	        	output.add(mEditText);
    	        } else{
    	            Log.d(LOG_TAG, "getAllTextEditsFromLayout(): EditText is NULL");
    	        }
    	    }
    	}
*/
    	Log.d(LOG_TAG, "getAllTextEditsFromLayout(): END");
    	return output;
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

    	initHaWebservice();
    }

    public void onEditTokenClicked(View view) {
    	tokenTextView = (EditText) findViewById(R.id.edit_token);
        String token = tokenTextView.getText().toString().trim();

        setToken(token);
        Log.v(LOG_TAG, "change HA TOKEN -> '" + token + "'");

    	initHaWebservice();
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

  //TODO: ------------ refactor
          String key = "-";
          switch (mask) {
          	case HAMessageType.DIGITAL_DEVICE_0:
          		key = "Relay 1";
          		break;
          	case HAMessageType.DIGITAL_DEVICE_1:
          		key = "Relay 2";
          		break;
          	case HAMessageType.DIGITAL_DEVICE_2:
          		key = "Night Lamp 1";
          		break;
          	case HAMessageType.DIGITAL_DEVICE_3:
          		key = "Night Lamp 2";
          		break;
          	default:
                  Log.e(LOG_TAG, "doHAServiceAction UNKNOWN mask: '" + String.format("%s", mask) + "'");
          		break;
          }

          String val = "0";
          if (on) {
          	val = "1";
          }

  		haWebservice.put(key, val);
  		
		updateRegisters();

  		return true;
  //TODO: ------------ refactor

      		

/*
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
*/
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
