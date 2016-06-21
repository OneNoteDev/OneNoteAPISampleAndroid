## OneNote API Android Sample README

Created by Microsoft Corporation, 2014. Provided As-is without warranty. Trademarks mentioned here are the property of their owners.

### API functionality demonstrated in this sample

The following aspects of the API are covered in this sample. You can 
find additional documentation at the links below.

* [Log-in the user using the Live SDK](http://msdn.microsoft.com/EN-US/library/office/dn575435.aspx)
* [POST simple HTML to a new OneNote QuickNotes page](http://msdn.microsoft.com/EN-US/library/office/dn575428.aspx)
* [POST page in a specific section](http://msdn.microsoft.com/EN-US/library/office/dn672416.aspx)
* [POST multi-part message with image data included in the request](http://msdn.microsoft.com/EN-US/library/office/dn575432.aspx)
* [POST page with a URL rendered as an image](http://msdn.microsoft.com/EN-US/library/office/dn575431.aspx)
* [POST page with a file attachment](http://msdn.microsoft.com/en-us/library/office/dn575436.aspx)
* [POST page with a PDF file rendered and attached](http://msdn.microsoft.com/EN-US/library/office/dn655137.aspx)
* [Extract the returned oneNoteClientURL and oneNoteWebURL links](http://msdn.microsoft.com/EN-US/library/office/dn575433.aspx)

### Prerequisites

**Tools and Libraries** you will need to download, install, and configure for your development environment. 

Be sure to verify the prerequisites for these too.

* [Google Android Developer Tools bundle](http://developer.android.com/sdk/index.html)  
* [Microsoft Live Connect SDK for Android](https://github.com/liveservices/LiveSDK-for-Android)
  
**Accounts**

* As the developer, you'll need to [have a Microsoft account and get a client ID string](http://msdn.microsoft.com/EN-US/library/office/dn575426.aspx) 
so your app can authenticate with the Microsoft Live connect SDK.


### Using the sample

After you've setup your development tools, and installed the prerequisites listed above,....

1. Download the repo as a ZIP file to your local computer, and extract the files. Or, clone the repository into a local copy of Git.
2. Import the Android code into a workspace/project in Android development tools.
3. Get a client ID string and copy it into the file:
.../src/com/example/onenoteservicecreatepageexample/Constants.java
4. Import and Reference the Live Connect SDK source code.
5. Build and run the Android app.
6. Authenticate in the running app, using your Microsoft account.
7. Allow the app to create new pages in OneNote.

### Version Info

This is the initial public release for this code sample.
  
### Learning More

* Visit the [dev.onenote.com](http://dev.onenote.com) Dev Center
* Contact us on [StackOverflow (tagged OneNote)](http://go.microsoft.com/fwlink/?LinkID=390182)
* Follow us on [Twitter @onenotedev](http://www.twitter.com/onenotedev)
* Read our [OneNote Developer blog](http://go.microsoft.com/fwlink/?LinkID=390183)
* Explore the API using the [apigee.com interactive console](http://go.microsoft.com/fwlink/?LinkID=392871).
Also, see the [short overview/tutorial](http://go.microsoft.com/fwlink/?LinkID=390179). 
* [API Reference](http://msdn.microsoft.com/en-us/library/office/dn575437.aspx) documentation
* [Debugging / Troubleshooting](http://msdn.microsoft.com/EN-US/library/office/dn575430.aspx)
* [Getting Started](http://go.microsoft.com/fwlink/?LinkID=331026) with the OneNote service API

  This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.
