#Osmdroid-Plus
This is a mirror of `http://osmdroid.googlecode.com/svn/trunk/ osmdroid-read-only` with enhancements.

##Enhancements

###Required libraries included
see ./libs. Add these to your project build path.

###Support for TMS tile sources ([Issue 195](http://code.google.com/p/osmdroid/issues/detail?id=195&q=TMS))

TMS map sources have a reversed Y-axis. Example use:

		XYTileSource tileSource = new XYTileSource("sampleTileSource", null, 5, 18, 
											256, ".png", "http://path/to/xysource/");
		
		// This is new!
		tileSource.isSourceTMS = true;
		
    	mapView.setTileSource(tileSource);

###MapView Boundaries

##Configuration

###1. Link project to android.jar
The osmdroid team have this project linked to android.jar through an environemnt variable. To fix android libraries not resolving do the following:
####In Eclipse:
	R-click project -> Properties -> Java Build Path -> Libraries (Tab) -> Add Variable -> Configure Variables -> New
In the dialog add a variable with Name, Path as below:

**Name**: ANDROID_SDK_PLATFORM

**Path**: path to android.jar. i.e: `/path/to/android-sdk-macosx/platforms/android-16/android.jar`
###2. Add external libraries to build path
####In Eclipse:
	R-click project -> Properties -> Java Build Path -> Libraries (Tab)

	Add JARs -> ./libs/apache-mime4j-0.4.jar
	Add JARs -> ./libs/httpmime-4.0-beta1.jar
	Add JARs -> ./libs/slf4j-android-1.5.8.jar
	Add Library -> JUnit -> JUnit 4

