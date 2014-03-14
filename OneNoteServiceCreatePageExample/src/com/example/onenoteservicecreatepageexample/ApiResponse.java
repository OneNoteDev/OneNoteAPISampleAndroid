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

public class ApiResponse {
	
	private String mOneNoteClientUrl;
	private String mOneNoteWebUrl;
	private String mResponseMessage;
	private int mResponseCode;
	
	public String getOneNoteClientUrl() {
		return mOneNoteClientUrl;
	}
	
	public String getOneNoteWebUrl() {
		return mOneNoteWebUrl;
	}
	
	public String getResponseMessage() {
		return mResponseMessage;
	}
	
	public int getReseponseCode() {
		return mResponseCode;
	}
	
	public void setOneNoteClientUrl(String url) {
		mOneNoteClientUrl = url;
	}
	
	public void setOneNoteWebUrl(String url) {
		mOneNoteWebUrl = url;
	}
	
	public void setResponseCode(int code) {
		mResponseCode= code;
	}
	
	public void setResponseMessage(String message) {
		mResponseMessage = message;
	}
	
	
	
	
	
	
}
