# Quake Report

This Android application uses the U.S. Geological Survey (USGS) Earthquake Hazards Program API to provide users with real-time information about earthquakes around the world.

This app demonstrates the following views and techniques:

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service
* [RxJava3](https://github.com/ReactiveX/RxJava) for multithreading
* [Google Maps API](https://developers.google.com/maps/documentation/android-sdk) to display earthquake locations on map

It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Room](https://developer.android.com/training/data-storage/room)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter passing between fragments


## Features
* Displays a list of recent earthquakes, including magnitude, location, and time
* Allows users to filter earthquakes by magnitude, location, and time
* Shows earthquake details on a map
* Provides links to additional information about earthquakes from the USGS
* Dark mode

## Screenshots

![Screenshot 1](screenshots/1.jpg)
![screenshot 2](screenshots/2.jpg)
![screenshot 3](screenshots/3.jpg)
![screenshot 4](screenshots/4.jpg)
![screenshot 5](screenshots/5.jpg)
![screenshot 6](screenshots/6.jpg)
![screenshot 7](screenshots/7.jpg)
![screenshot 8](screenshots/8.jpg)
![screenshot 9](screenshots/9.jpg)
![Screenshot 10](screenshots/10.jpg)
![Screenshot 11](screenshots/11.jpg)
![screenshot 12](screenshots/12.jpg)
![screenshot 13](screenshots/13.jpg)
![screenshot 14](screenshots/14.jpg)
![screenshot 15](screenshots/15.jpg)
![screenshot 16](screenshots/16.jpg)
![screenshot 17](screenshots/17.jpg)
![screenshot 18](screenshots/18.jpg)
![screenshot 19](screenshots/19.jpg)
![Screenshot 20](screenshots/20.jpg)

## Requirements
* Android SDK v23 or higher
* Android Build Tools v30.0.3 or higher
* Google Play Services

## Setup
* Clone the repository.
* Open the project in Android Studio.
* Build and run the app.

## Contributing
Contributions are welcome! Please feel free to fork the repository and submit pull requests.

## Licensing
This project is licensed under the MIT License. See the LICENSE file for more information.
