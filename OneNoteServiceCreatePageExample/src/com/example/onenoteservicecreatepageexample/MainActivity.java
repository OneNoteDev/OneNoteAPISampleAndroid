//*********************************************************
// Copyright (c) Microsoft Corporation
// All rights reserved. 
//
// Licensed under the Apache License, Version 2.0 (the ""License""); 
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
// http://www.apache.org/licenses/LICENSE-2.0 
//
// THIS CODE IS PROVIDED ON AN  *AS IS* BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS 
// OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED 
// WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR 
// PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT. 
//
// See the Apache Version 2.0 License for specific language 
// governing permissions and limitations under the License.
//*********************************************************

package com.example.onenoteservicecreatepageexample;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveStatus;

public class MainActivity extends Activity implements LiveAuthListener, AsyncResponse {

	private LiveAuthClient mAuthClient;
	private LiveConnectClient mLiveConnectClient;
	private TextView resultTextView;
	private String mAccessToken = null;
	private final String CLIENT_ID_MESSAGE = "Insert Your Client Id Here";
	private Date accessTokenExpiration = null;
	private String refreshToken = null;
	private EditText sectionNameTextBox = null;
	private String sectionName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sectionName = "";
		setContentView(R.layout.activity_main);
		resultTextView = (TextView) findViewById(R.id.txtView_Auth);
		mAuthClient = new LiveAuthClient(this, Constants.CLIENTID);
		if(Constants.CLIENTID.equals(CLIENT_ID_MESSAGE))
		{
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			/**
			 * Please enter your client ID for field Constants#CLIENTID
			 */
			dialogBuilder.setMessage("Visit http://go.microsoft.com/fwlink/?LinkId=392537 for instructions on getting a Client Id. Please specify your client ID at field Constants.CLIENTID and rebuild the application.");
			dialogBuilder.setTitle("Please add a client Id to your code.");
			dialogBuilder.setNeutralButton("OK", null);
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
		}
		setCreatePageButtonsVisibility(false);
		Button signoutButton = (Button) findViewById(R.id.btn_signout);
		signoutButton.setEnabled(false);
		sectionNameTextBox = (EditText) findViewById(R.id.sectionName);
		sectionNameTextBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable sectionNameField) {
				sectionName = sectionNameTextBox.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				//No handling done by this sample here
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//No handling done by this sample here
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void setCreatePageButtonsVisibility(boolean visibility) {
		Button simplePageButton = (Button) findViewById(R.id.btn_sendHTML);
		simplePageButton.setEnabled(visibility);
		Button imagePageButton = (Button) findViewById(R.id.btn_sendImg);
		imagePageButton.setEnabled(visibility);
		Button urlPageButton = (Button) findViewById(R.id.btn_sendURL);
		urlPageButton.setEnabled(visibility);
		Button embeddedHTMLPageButton = (Button) findViewById(R.id.btn_sendEmbeddedHTML);
		embeddedHTMLPageButton.setEnabled(visibility);
		Button attachmentPageButton = (Button) findViewById(R.id.btn_createPageWithAttachmentAndPdfRendering);
		attachmentPageButton.setEnabled(visibility);
	}

	public void onAuthComplete(LiveStatus status, LiveConnectSession session, Object userState) {		
		if(status == LiveStatus.CONNECTED) {
			Button authenticateButton = (Button) findViewById(R.id.btn_auth);
			authenticateButton.setEnabled(false);
			resultTextView.setText(R.string.auth_yes);
			mAccessToken = session.getAccessToken();
			accessTokenExpiration = session.getExpiresIn();
			refreshToken = session.getRefreshToken();
			mLiveConnectClient = new LiveConnectClient(session);
			setCreatePageButtonsVisibility(true);
			Button signoutButton = (Button) findViewById(R.id.btn_signout);
			signoutButton.setEnabled(true);
		}
		else {
			resultTextView.setText(R.string.auth_no);
			mLiveConnectClient = null;
		}
	}

	public void onAuthError(LiveAuthException exception, Object userState) {
		exception.printStackTrace();
		resultTextView.setText(getResources().getString(R.string.auth_err) + exception.getMessage());
		mLiveConnectClient = null;
	}

	public void btn_signout_onClick(View view) {
		setCreatePageButtonsVisibility(false);
		mAuthClient.logout(this);
		mLiveConnectClient = null;
		/**
		 * Since the user requested to sign out, we first logout on the LiveAuthClient instance
		 * Then, we restart the activity because we want to finish with and release the current activity and start a new activity based on the same intent (MainActivity)
		 * This results into the user seeing the start page with Authenticate enabled and all create page buttons disabled, which means that we signed out and must Authenticate again
		 */
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	/** Called when the user clicks the Authenticate Button */
	public void callOAuth(View vew) {

		super.onStart();
		Iterable<String> scopes = Arrays.asList("office.onenote_create", "wl.signin", "wl.offline_access");
		mAuthClient.login(this, scopes, this);
	}


	/** Called when the user clicks the HTML Button */
	public void btn_sendHTML_onClick(View view)
	{
		boolean success = StartRequest();
		if(success)
		{
			SendPageCreateAsyncTask task = new SendPageCreateAsyncTask();
			task.delegate = this;
			task.execute("Simple", mAccessToken, sectionName);
		}
		else
		{
			//Disabling the create page buttons
			setCreatePageButtonsVisibility(false);
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("An error was encountered when trying to refresh the access token.");
		}
	}

	/**
	 * Called when the user clicks the attachment request button
	 */
	public void btn_sendAttachmentWithPdfRendering_onClick(View view) {
		boolean success = StartRequest();
		if(success)
		{
			SendPageCreateAsyncTask task = new SendPageCreateAsyncTask();
			task.delegate = this;
			AssetManager assetManager = getResources().getAssets();
			task.assetManager = assetManager;
			task.execute("AttachmentWithPdfRendering", mAccessToken, sectionName);
		}
		else
		{
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("An error was encountered when trying to process this request.");
		}
	}

	/** Called when the user clicks the Img Button */
	/** Creates a page with an image on it 
	 * @throws MalformedURLException */
	public void btn_sendImg_onClick(View view) {
		boolean success = StartRequest();
		if(success)
		{
			SendPageCreateAsyncTask task = new SendPageCreateAsyncTask();
			task.delegate = this;
			AssetManager assetManager = getResources().getAssets();
			task.assetManager = assetManager;
			task.execute("Image", mAccessToken, sectionName);
		}
		else
		{
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("A failure was encountered when trying to process this request.");
		}
	}

	private boolean requestRefreshToken(String refreshToken) throws InterruptedException, ExecutionException
	{
		SendRefreshTokenAsyncTask task = new SendRefreshTokenAsyncTask();
		Object[] refreshTokenResult = task.execute(refreshToken).get();
		if(refreshTokenResult != null) {
			mAccessToken = (String) refreshTokenResult[0];
			refreshToken = (String) refreshTokenResult[1];
			/**
			 * The value at refreshTokenResult[2] denotes the number of seconds since the time of the request in which the new access token will expire
			 * We add this value to keep the accessTokenExpiration property updated.
			 * This property is used to check if the access token has expired every time we attempt to use the access token for a new request.
			 * If yes, then we issue a new request for refreshing the token.  
			 */
			accessTokenExpiration.setTime(accessTokenExpiration.getTime() + 1000 * (Integer)refreshTokenResult[2]);
			return true;
		}
		return false;
	}

	private boolean StartRequest() {
		//Check whether the access token has expired and if yes then attempt to refresh
		Date now = new Date();
		if(accessTokenExpiration.compareTo(now) <= 0) {
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("Status: Waiting for token refresh");
			try {
				if(requestRefreshToken(refreshToken))
				{
					return true;
				}
				else {
					tvStatus.setText("Status: Token refresh failed. Attempting to restart the application.");
					/**
					 * Since the token refresh failed, we restart the activity because we want to finish with and release the current activity which could not get the token refreshed
					 * We start a new activity based on the same intent (MainActivity)
					 * Then, clicking Authenticate will lead to a new login request to the LiveAuthClient which would attempt to get a new token
					 */
					Intent intent = getIntent();
					finish();
					startActivity(intent);
					return false;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				tvStatus.setText("Status: Token refresh failed. Attempting to restart the application.");
				/**
				 * Since the token refresh failed even on retry, we restart the activity because we want to finish with and release the current activity which could not get the token refreshed
				 * We start a new activity based on the same intent (MainActivity)
				 * Then, clicking Authenticate will lead to a new login request to the LiveAuthClient which would attempt to get a new token
				 */
				Intent intent = getIntent();
				finish();
				startActivity(intent);
				return false;
			}
		}
		else {
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("Status: Sending request...");
			return true;
		}
	}

	/** Called when the user clicks the EmbeddedHTML Button */
	public void btn_sendEmbeddedHTML_onClick(View view) {
		boolean success = StartRequest();
		if(success)
		{
			SendPageCreateAsyncTask task = new SendPageCreateAsyncTask();
			task.delegate = this;
			task.execute("HTML", mAccessToken, sectionName);
		}
		else
		{
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("A failure was encountered when trying to process this request.");
		}
	}

	/** Called when the user clicks the URL Button */
	public void btn_sendURL_onClick(View view) {
		boolean success = StartRequest();
		if(success)
		{
			SendPageCreateAsyncTask task = new SendPageCreateAsyncTask();
			task.delegate = this;
			task.execute("URL", mAccessToken, sectionName);
		}
		else
		{
			TextView tvStatus = (TextView)findViewById(R.id.tvStatus);
			tvStatus.setText("A failure was encountered when trying to process this request.");
		}
	}

	@Override
	public void processFinish(ApiResponse response) {
		showResponse(response);

	}

	public void showResponse(ApiResponse response)
	{
		TextView tvStatus = (TextView)findViewById(R.id.tvStatus);

		if (response.getReseponseCode() == 201) {

			tvStatus.setText("Status: page created successfully.");

			Intent intent = new Intent(this, ResultsActivity.class);

			String message = response.getResponseMessage();
			String clientUrl = response.getOneNoteClientUrl();
			String webUrl = response.getOneNoteWebUrl();

			intent.putExtra(Constants.RESPONSE, message);
			intent.putExtra(Constants.CLIENT_URL, clientUrl);
			intent.putExtra(Constants.WEB_URL, webUrl);

			startActivity(intent);
		} else {
			tvStatus.setText("Status: page creation failed with error code " + response.getReseponseCode());
		}



	}
}


