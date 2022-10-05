# OTP Viewer
<img src="https://img.shields.io/badge/code_style-standard-brightgreen.svg"> <img src="https://img.shields.io/badge/architecture-MVVM-blue"> 
<img src="https://img.shields.io/badge/minSdk-21-orange"> <img src="https://img.shields.io/badge/targetSdk-31-lightgrey"> 
<img src="https://img.shields.io/badge/version-1-yellow">

An Android application that detects OTP in incoming texts and shows the OTP with a direct button to copy in the notification drawer. The application behaves like a default messaging application with similar UI/UX but with easy visibility and access to OTP in the message.

### Screenshots of Application
![All screenshots of app][1]
----------

### Requirements
For development, you will only need:
 - Android Studio
 - Android Device or Emulator installed along with Android Studio
 - Minimum supported Android SDK

### Getting started
 - Install Android Studio
 - Extract the project from the zip file.
 - Open the project in Android Studio.
 - Build the project and run the sample. You may need to update Gradle and library versions.
 - Follow the guidance provided by Android Studio.
 - If you are still not able to build the project try installing the APK of the application directly into an android device.

### Built With 🛠
 - MVVM with Clean Architecture
 - Kotlin: First class and official programming language for Android development.
 - Coroutines: For asynchronous coding
 - Android Architecture Components: A collection of libraries that help you design robust, testable, and maintainable apps.
        - LiveData: Data objects that notify views when the underlying database changes.
        - ViewModel: Stores UI-related data that isn't destroyed on configuration changes.
        - DataBinding: Generates a binding class for each XML layout file present in that module and allows ViewModel to communicate directly with views.
        - SaveArgs: generates simple object and builder classes for type-safe navigation and access to any associated arguments.
        - Broadcast Receivers: allows you to register for system or application events. For listening to received SMS and also to handle clicks on notifications using pending intents.
        - Notifications: Using native android Notification Manager to send proper notifications.
 - RecyclerView optimizations like DiffUtils
 - Proper thread usage using Coroutines

### Permissions Required
```
RECEIVE_SMS
READ_SMS
```

### Home Screen
![Onboarding flow][2]

When the user opens the application for the first time, the user is asked to grant permission to read/receive SMS. Once the permission is granted, the application shows the homescreen with all the previous SMS of the user.
If the permission is not granted, the user is asked again, or else the user navigates to the settings screen of the application where the user can manually grant the permission.

### SMS for any particular sender
<img src="https://he-s3.s3.amazonaws.com/media/uploads/357ab77.png" alt="Copy code from SMS which has OTP available" width="350"/>

After clicking on any sender from the listing of all senders, the user can view all the messages of that particular sender, and also a copy button will be shown for all the messages, which have OTP in them. 
Clicking on the copy button will directly copy the OTP on the clipboard.

### Notification in Notification drawer
<img src="https://he-s3.s3.amazonaws.com/media/uploads/422240c.png" alt="Pop-up notification" width="350"/>


Every time the user receives some notification, the broadcast receiver registers it and then sends our own app notification which is shown as a default notification with action to copy the OTP code (if available). If the user clicks on copy "OTP" it will be directly copied to the user's clipboard. This makes the process super simple.

### Pop-up transparent dialog
![Transparent pop-up dialog][5]

If the user clicks on a notification, a transparent pop-up dialog box opens up on the previously opened activity only. The dialog shows the entire message, with the open to copy OTP (if available) or open the app to see all messages.


### Unit test
I have also implemented some unit tests for basic function testing about extracting OTP from the message. 
``` @Test
    fun getOTP_noOtpPresent_returnNULL() {
        val message = "This message does not contain any OTP"
        assertEquals(findOTPCode(message), null)
    }

    @Test
    fun getOTP_OtpPresentInBetween_returnOTP() {
        val message = "This message contain any OTP 123456 here"
        assertEquals(findOTPCode(message), "123456")
    }

    @Test
    fun getOTP_OtpPresentAtStart_returnOTP() {
        val message = "123456 this message contain any OTP here"
        assertEquals(findOTPCode(message), "123456")
    }

    @Test
    fun getOTP_OtpPresentAtEnd_returnOTP() {
        val message = "This message contain any OTP here 123456"
        assertEquals(findOTPCode(message), "123456")
    }
```


  [1]: https://he-s3.s3.amazonaws.com/media/uploads/ca3ad55.png
  [2]: https://he-s3.s3.amazonaws.com/media/uploads/222724d.png
  [3]: https://he-s3.s3.amazonaws.com/media/uploads/357ab77.png
  [4]: https://he-s3.s3.amazonaws.com/media/uploads/422240c.png
  [5]: https://he-s3.s3.amazonaws.com/media/uploads/e7e5610.png
