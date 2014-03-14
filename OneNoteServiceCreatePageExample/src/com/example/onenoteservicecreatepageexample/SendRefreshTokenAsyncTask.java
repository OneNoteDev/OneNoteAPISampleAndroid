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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

/**
 * Sends a request to refresh the access token using the refresh token
 * Parses the JSON response received and returns as a result the new access token, the new refresh token and the expiration time
 * @author machandw
 *
 */
public class SendRefreshTokenAsyncTask extends AsyncTask<String, Void, Object[]> {
	
	//Properties for refreshing the access token (only applicable when the application uses the wl.offline_access wl.signin scopes)
	private final String TOKEN_REFRESH_REQUEST_BODY = "client_id={0}&redirect_uri={1}&grant_type=refresh_token&refresh_token={2}";
	private final String MSA_TOKEN_REFRESH_URL = "https://login.live.com/oauth20_token.srf";
	private final String TOKEN_REFRESH_CONTENT_TYPE = "application/x-www-form-urlencoded";
	private final String TOKEN_REFRESH_REDIRECT_URL = "https://login.live.com/oauth20_desktop.srf";

	protected Object[] doInBackground(String... params) {
		try {
			return attemptRefreshAccessToken(params[0]);
		}
		catch(Exception sendRefreshTokenException) {
			sendRefreshTokenException.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object[] response) {
		super.onPostExecute(response);
	}

	private Object[] attemptRefreshAccessToken(String refreshToken) throws Exception {
		/**
		 * A new connection to the endpoint that processes requests for refreshing the access token
		 */
		HttpsURLConnection refreshTokenConnection = (HttpsURLConnection) (new URL(MSA_TOKEN_REFRESH_URL)).openConnection();
		refreshTokenConnection.setDoOutput(true);
		refreshTokenConnection.setRequestMethod("POST");
		refreshTokenConnection.setDoInput(true);
		refreshTokenConnection.setRequestProperty("Content-Type", TOKEN_REFRESH_CONTENT_TYPE);
		refreshTokenConnection.connect();
		OutputStream refreshTokenRequestStream = null;
		try {	
			refreshTokenRequestStream = refreshTokenConnection.getOutputStream();
			String requestBody = MessageFormat.format(TOKEN_REFRESH_REQUEST_BODY, Constants.CLIENTID, TOKEN_REFRESH_REDIRECT_URL, refreshToken);
			refreshTokenRequestStream.write(requestBody.getBytes());
			refreshTokenRequestStream.flush();
		}
		finally {
			if(refreshTokenRequestStream != null) {
				refreshTokenRequestStream.close();
			}
		}
		if(refreshTokenConnection.getResponseCode() == 200) {
			return parseRefreshTokenResponse(refreshTokenConnection);
		}
		else {
			throw new Exception("The attempt to refresh the access token failed");
		}
	}

	private Object[] parseRefreshTokenResponse(HttpsURLConnection refreshTokenConnection) throws IOException, JSONException {
		BufferedReader refreshTokenResponseReader = null;
		try {
			refreshTokenResponseReader = new BufferedReader(new InputStreamReader(refreshTokenConnection.getInputStream()));
			String temporary = null;
			StringBuffer response = new StringBuffer();
			while((temporary = refreshTokenResponseReader.readLine()) != null) {
				response.append(temporary);
				response.append("\n");
			}
			JSONObject refreshTokenResponse = new JSONObject(response.toString());
			Object[] refreshTokenResult = new Object[3];
			refreshTokenResult[0] = (String) refreshTokenResponse.get("access_token");
			refreshTokenResult[1] = (String) refreshTokenResponse.get("refresh_token");
			refreshTokenResult[2] = (Integer) refreshTokenResponse.get("expires_in");
			return refreshTokenResult;
		}
		finally {
			if(refreshTokenResponseReader != null) {
				refreshTokenResponseReader.close();
			}
		}
	}

}
