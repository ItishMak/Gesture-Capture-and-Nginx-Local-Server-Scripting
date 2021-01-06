Group-32/Assignement - 1


Execution Instructions:
there are 2 files: 
1. One is 'Group32.zip' (Contains android Source Code) 
2. and other is 'app_debug.apk' (.apk file)

To execute/install the application you just install app_debug.apk in your android device.


To execute file in android studio:
1. Unzip Group32.zip file.
2. Now Import 'mobile_computing535' project to your android studio.
3. Sync the Gradle to install dependencies.
4. Now run the application by selecting any virtual device/connecting to real-device through USB.
5. Once the app is runned succesfully, then the following activites should be done to explore the application.

Functionalities of App:

1. Login Activity:
On Launching the app, "Practice Gestures" screen is launched. In this user is asked give their 'userlastname' and 'asu-id' (this asu-id is used as one of the POST parameters which required while uploading to server. So, Please make sure that ASU-Rite given by you iscorrect).

2. Main Acitivity:
In these activity there is drop-down menu to select the Host server (By default we added the server address given in assignment specification. Therfore, uploading files will be done to given server).
When the user clicks sign in button user will be directed to Screen containing drop down menu of servers and gestures. Now user will select one of the gestures to learn and Upon clicking the 'Learn' button. User will be prompted to other activity.

3. Video Activity:
In these an expert who is performing the selected gesture will be played and also there is a button 'Practice' which takes the user to pratice the gesture that he/she selected.

4. Camera Activity:
In these video recording will starts and ends with in 5 sec. So in this time user need to practice the gesture. Once 5 sec is completed the video will be saved in the format given in assignment specification, then the user will be directed to other activity. 


5. Upload Activity: (Uploaded to the Server given in Assignment Specification)
In these there is a videoview, in which recorded video of the user will be played. Now there are two buttons 'Upload' and 'Reject'.
On clicking Upload button, the recorded-video is uploaded to Host server to the folder named 'accept'. 
On clicking Reject button, the recorded-video is uploaded to Host server to the folder named 'reject'.


6. Additional Functionality to Upload Button (only):
Once the user uploaded the video successfully, then 'Success' is appeared as Toast in the acitivity screen. Now 'ON_LONG_PRESSING' the "UPLOAD BUTTON" the user will be taken to Main Activity screen which contains drop-down menu of gestures.


Ignore Files:
1. Module : aFileChooser
2. In app/java/com.example.mobile_computing535: Remote, Utils
3. In Gradle Scripts: build.gradle(Module: aFilechooser)








