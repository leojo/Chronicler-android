Chronicler android app:

	https://github.com/leojo/Chronicler-android/

Chronicler webapp on Heroku: 

		https://chronicler-webapp.herokuapp.com/

Chronicler webapp on github:

		https://github.com/leojo/Chronicler


The Rest controllers and the relevant database functions in the backend can be found here:

	/src/main/java/project/controller/DatabaseRestController.java
	/src/main/java/project/persistence/dbLookup/AccountStorage.java
	/src/main/java/project/persistence/dbLookup/OfflineResultSet.java
	/src/main/java/project/persistence/dbLookup/Lookup.java


About this project: 

This app is a tool to play D&D with interactive character sheets, searchable database and other features that are supposed to replace the pen and paper aspect of D&D. We built this app as part of our project for a Software Development course and as such, it is still in the early stages of development and not at all finished. 

More information in our slideshow: http://slides.com/andreab/deck-1#/

// ----------------------
// BUILDING INSTRUCTIONS
// ----------------------

NB: ----- BEFORE YOU BUILD !!!

A debug .apk file for this app can be found on github under releases:


This can be installed on android devices directly through github so there's no need to build from source if you simply want to test out the app in its' current form.

BUILDING

1. Clone this git project into desired directory, f.ex. by doing: 

	$ git clone https://github.com/leojo/Chronicler-android.git

2. Run build.gradle to install dependencies. 

3. Run the MainActivity to try out the app!

// ----------
// KNOWN BUGS
// ----------

* User can sometimes (though rarely) lose his login within inner activities. When this happens, requests get stuck on wait screens. If this happens, try logging out and login again.
* Character sheet security was severly reduced when we decided to let the character information be available to all members of the same campaign. Doing this made the sheet available to changes by members of the campaign although they should not be able to. This is a high priority security problem that will be fixed.
* Despite preparing requests, database can not handle symbols such as ' so please avoid those. This should also be fixed.
* Error handling is still needed in some places, esp. with faulty requests to the server.
* Limiting user input is needed in some places.
* Magic items have not been implemented. This is not a bug, it's a lack of a feature. :)
* When using the search, a fatal error occurs when the user clicks on a search result, goes back to 
  the search activity and tries to search for a new string. This is because the adapter for the list
  is updated out of the ui thread which is risky and we need to fix it by making sure its always
  updated in sync with the list content.


