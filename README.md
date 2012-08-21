#Osmdroid-Plus
This is a mirror of `http://osmdroid.googlecode.com/svn/trunk/ osmdroid-read-only` with enhancements. All required libraries are included.

##Enhancements


###Support for TMS tile sources ([Issue 195](http://code.google.com/p/osmdroid/issues/detail?id=195&q=TMS))

TMS map sources have a reversed Y-axis. Example use:

	XYTileSource tileSource = new XYTileSource("sampleTileSource", null, 5, 18, 
											256, ".png", "http://path/to/xysource/");
		
	// This is new!
	tileSource.isSourceTMS = true;
		
    mapView.setTileSource(tileSource);

###MapView Boundaries ([Issue 209](http://code.google.com/p/osmdroid/issues/detail?id=209))
	
    GeoPoint northEast = new GeoPoint(40802822, -119172673);
	GeoPoint southWest = new GeoPoint(40.759210, -11923454);
    BoundingBoxE6 bounds = new BoundingBoxE6(northEast, southWest);
    mapView.setScrollableAreaLimit(bounds);

###Support for tilesources bundled within the .apk
The official osmdroid trunk only supports reading tiles from the sdcard partition. Support for Android assets allows reading tile sources distributed within the .apk archive.

You can now specify a tilesource in /assets/tiles/ using **MapTileAssetProvider**. It's analagous to osmdroid's MapTileFilesystemProvider.

	// In this ex, offline tiles are located in /assets/tiles/burn
	final BitmapAssetTileSource tileSource = new BitmapAssetTileSource("burn", null, 5, 18, 256, "");
	AssetManager assets = Activity.getAssets();

	MapTileModuleProviderBase[] myProviders = new MapTileModuleProviderBase[1];

    myProviders[0] =  new MapTileAssetProvider(assets, tileSource, 0);

    MapTileProviderArray MyTileProvider = new MapTileProviderArray(tileSource, null, myProviders);

    TilesOverlay MyTilesOverlay = new TilesOverlay(MyTileProvider, FragmentTabsPager.app);

   	mapView.getOverlays().add(MyTilesOverlay);

###Specify BoundingBox by NorthEast, SouthWest GeoPoints
The official osmdroid trunk only supports specifying bounding boxes given four pairs of lat/lon coordinates. 

	GeoPoint northEast = new GeoPoint(40802822, -119172673);
	GeoPoint southWest = new GeoPoint(40.759210, -11923454);
	
	BoundingBoxE6 mapBounds = new BoundingBoxE6(northEast, southWest);

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

