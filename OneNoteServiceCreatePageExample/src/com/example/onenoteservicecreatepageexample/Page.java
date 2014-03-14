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

import java.util.ArrayList;
import java.util.Date;

public class Page {
	private String Title;
	private String Body;
	private Date Created;
	private String CaptureString;
	
	ArrayList<Image> Images;
	
	public Page(String title, String body, Date created, ArrayList<Image> images) 
	{
		Title = title;
		Body = body;
		Created = created;
		Images = images;
	}
	
	public Page(String title, String body, Date created, String capturestring) 
	{
		Title = title;
		Body = body;
		Created = created;
		CaptureString = capturestring;	
	}
	
}
