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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.os.AsyncTask;


public class SendPageCreateAsyncTask extends
AsyncTask<String, String, ApiResponse> {

	private final String PAGES_ENDPOINT = "https://www.onenote.com/api/v1.0/pages";
	private OutputStream mOutputStream = null;
	private final String DELIMITER = "--";
	private final String BOUNDARY = "Asdfs"+Long.toString(System.currentTimeMillis()) + "aBc";
	private HttpsURLConnection mUrlConnection = null;
	private String mAccessToken = null;
	public AsyncResponse delegate = null;
	public AssetManager assetManager = null;

	@Override
	protected ApiResponse doInBackground(String... params) {

		String scenario = params[0];
		mAccessToken = params[1];

		if (scenario == "Simple" ) {

			return createSimplePage();
		} else if (scenario == "Image" ) {

			return createPageWithImage();

		} else if (scenario == "URL") {

			return createPageWithUrlScreenShot();

		} else if ( scenario == "HTML" ) {

			return createPageWithHTMLScreenshot();

		} else if(scenario == "Attachment") {

			return createPageWithAttachment();

		}
		return null;
	}

	private ApiResponse createPageWithHTMLScreenshot() {

		try {
			this.postMultipartRequest(PAGES_ENDPOINT);
			String embeddedPartName = "embedded1";
			String date = getDate();
			String embeddedWebPage =
					"<html>" +
							"<head>" +
							"<title>Embedded HTML</title>" +
							"</head>" +
							"<body>" +
							"<h1>This is a screen grab of a web page</h1>" +
							"<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vehicula magna quis mauris accumsan, nec imperdiet nisi tempus. Suspendisse potenti. " +
							"Duis vel nulla sit amet turpis venenatis elementum. Cras laoreet quis nisi et sagittis. Donec euismod at tortor ut porta. Duis libero urna, viverra id " +
							"aliquam in, ornare sed orci. Pellentesque condimentum gravida felis, sed pulvinar erat suscipit sit amet. Nulla id felis quis sem blandit dapibus. Ut " +
							"viverra auctor nisi ac egestas. Quisque ac neque nec velit fringilla sagittis porttitor sit amet quam.</p>" +
							"</body>" +
							"</html>";

			String requestPresentationContent =  "<html>" +
					"<head>" +
					"<title>A Page Created With Snapshot of Webpage in it (Android Sample)</title>" +
					"<meta name=\"created\" content=\"" + date + "\" />" +
					"</head>" +
					"<body>" +
					"<h1>This is a page with an image of an html page on it.</h1>" +
					"<img data-render-src=\"name:" + embeddedPartName + "\" alt=\"A website screen grab\" />" +
					"</body>" +
					"</html>";

			this.addPart("presentation", "application/xhtml+xml", requestPresentationContent);
			this.addPart(embeddedPartName, "text/html", embeddedWebPage);
			this.finishMultipart();
			ApiResponse response = this.getResponse();
			return response;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	// This method demonstrates how to create a OneNote page that contains a screenshot of a URL.
	private ApiResponse createPageWithUrlScreenShot() {

		try {
			this.postMultipartRequest(PAGES_ENDPOINT);
			String date = getDate();
			String simpleHtml = 	"<html>" +
					"<head>" +
					"<title>A Page Created With a URL Snapshot on it (Android Sample)</title>" +
					"<meta name=\"created\" content=\"" + date + "\" />" +
					"</head>" +
					"<body>" +
					"<p>This is a page with an image of an html page rendered from a URL on it.</p>" +
					"<img data-render-src=\"http://www.onenote.com\" alt=\"An important web page\"/>" +
					"</body>" +
					"</html>";

			this.addPart("presentation", "application/xhtml+xml", simpleHtml);
			this.finishMultipart();
			ApiResponse response = this.getResponse();
			return response;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(ApiResponse response) {

		super.onPostExecute(response);		
		delegate.processFinish(response);

	}

	public ApiResponse createSimplePage() {
		try {
			this.postMultipartRequest(PAGES_ENDPOINT);
			String date = getDate();
			String simpleHtml = "<html>" +
					"<head>" +
					"<title>A page created from basic HTML-formatted text (Android Sample)</title>" +
					"<meta name=\"created\" content=\"" + date + "\" />" +
					"</head>" +
					"<body>" +
					"<p>This is a page that just contains some simple <i>formatted</i> <b>text</b></p>" +
					"</body>" +
					"</html>";
			this.addPart("Presentation", "application/xhtml+xml", simpleHtml);
			this.finishMultipart();
			ApiResponse response = this.getResponse();
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads fully from the specified input stream and returns a byte array containing the contents read
	 * @param stream The input stream to read from
	 * @return A byte array containing data read from the specified input stream
	 * @throws IOException An error encountered when reading from the input stream
	 */
	public static byte[] readFromStreamToByteArray(InputStream stream) throws IOException
	{
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead;
		while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1)
		{
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}

	/**
	 * Creates a page with a document file attachment
	 * @return The response received from the OneNote Service API for the create page operation
	 */
	public ApiResponse createPageWithAttachment() {
		String attachmentPartName = "pdfattachment1";
		InputStream is = null;
		try {
			this.postMultipartRequest(PAGES_ENDPOINT);
			String date = getDate();
			String requestHtml = "<html>" +
					"<head>" +
					"<title>A page with a file attachment (Android Sample)</title>" +
					"<meta name=\"created\" content=\"" + date + "\" />" +
					"</head>" +
					"<body>" +
					"<p>This page contains a pdf file attachment</p>" + 
					"<object data-attachment=\"attachment.pdf\" data=\"name:" + attachmentPartName + "\" />" +
					"</body>" +
					"</html>";
			is = assetManager.open("attachment.pdf");
			byte[] attachmentContents = readFromStreamToByteArray(is);
			//Add a part that contains the HTML content for this request and refers to another part for the file attachment
			this.addPart("Presentation", "text/html", requestHtml);
			//Add the content of the document file attachment in a separate part referenced by the part name
			this.addBinaryPart("pdfattachment1", "application/pdf", attachmentContents);
			this.finishMultipart();
			ApiResponse response = this.getResponse();
			return response;
		}
		catch (Exception createPageException) {
			createPageException.printStackTrace();
			return null;
		}
		finally {
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ApiResponse createPageWithImage() {
		InputStream is = null;
		try {
			this.postMultipartRequest(PAGES_ENDPOINT);
			String date = getDate();
			String imagePartName = "image1";
			String requestPresentationContent = "<html>" +
					"<head>" +
					"<title>A page with an image on it (Android Sample)</title>" +
					"<meta name=\"created\" content=\"" + date + "\" />" +
					"</head>" +
					"<body>" +
					"<h1>This is a page with an image on it</h1>" +
					"<img src=\"name:" + imagePartName + "\" alt=\"A beautiful logo\" width=\"426\" height=\"68\" />" +
					"</body>" +
					"</html>";

			is = assetManager.open("Logo.jpg");
			byte[] imageContents = readFromStreamToByteArray(is);
			this.addPart("Presentation", "application/xhtml+xml", requestPresentationContent);
			this.addBinaryPart(imagePartName, "image/jpeg", imageContents);	
			this.finishMultipart();

			ApiResponse response = this.getResponse();
			return response;
		}
		catch (Exception createPageException) {
			createPageException.printStackTrace();
			return null;
		}
		finally {
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void postMultipartRequest(String endpoint) throws Exception {

		mUrlConnection = (HttpsURLConnection) ( new URL(endpoint)).openConnection();
		mUrlConnection.setDoOutput(true);
		mUrlConnection.setRequestMethod("POST");
		mUrlConnection.setDoInput(true);
		mUrlConnection.setRequestProperty("Connection", "Keep-Alive");
		mUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		mUrlConnection.setRequestProperty("Authorization", "Bearer " + mAccessToken);
		mUrlConnection.connect();
		mOutputStream = mUrlConnection.getOutputStream();
	}

	private void addBinaryPart(String paramName, String contentType, byte[] contents) throws Exception {
		writePartData(paramName, contentType, contents);
	}

	private void addPart(String paramName, String contentType, String contents) throws Exception {
		writePartData(paramName, contentType, contents.getBytes());
	}

	private void writePartData(String paramName, String contentType, byte[] contents) throws Exception {
		mOutputStream.write( (DELIMITER + BOUNDARY + "\r\n").getBytes());
		mOutputStream.write( ("Content-Type: " + contentType + "\r\n").getBytes());
		mOutputStream.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());
		mOutputStream.write(("\r\n").getBytes());
		mOutputStream.write(contents);
		mOutputStream.write(("\r\n").getBytes());
	}

	private void finishMultipart() throws Exception {
		mOutputStream.write( (DELIMITER + BOUNDARY + DELIMITER + "\r\n").getBytes());
		mOutputStream.close();
	}

	public ApiResponse getResponse() throws Exception {

		int responseCode = mUrlConnection.getResponseCode();
		String responseMessage = mUrlConnection.getResponseMessage();
		String responseBody = null;
		if ( responseCode == 201) {
			InputStream is = null;
			try
			{
				is = mUrlConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = null;
				String lineSeparator = System.getProperty("line.separator");
				StringBuffer buffer = new StringBuffer();
				while((line = reader.readLine()) != null)
				{
					buffer.append(line);
					buffer.append(lineSeparator);
				}
				responseBody = buffer.toString();
			}
			finally
			{
				if(is != null)
				{
					is.close();
				}
				mUrlConnection.disconnect();
			}
		}
		JSONObject responseObject = null;
		ApiResponse response = new ApiResponse();
		try {			
			response.setResponseCode(responseCode);
			response.setResponseMessage(responseMessage);
			if ( responseCode == 201) {
				responseObject = new JSONObject(responseBody);
				String clientUrl = responseObject.getJSONObject("links").getJSONObject("oneNoteClientUrl").getString("href");		
				String webUrl = responseObject.getJSONObject("links").getJSONObject("oneNoteWebUrl").getString("href");
				response.setOneNoteClientUrl(clientUrl);
				response.setOneNoteWebUrl(webUrl);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return response;

	}

	/* Gets the Created Date in ISO-8601 with timezone format */
	private String getDate() {

		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		return df.format(d);
	}

}
