package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.IOException;

import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Implements a file system cache and provides cached tiles. This functions as a tile provider by
 * serving cached tiles for the supplied tile source.
 *
 * This MapTileAssetProvder is designed to fulfill the 
 * role of MapTileFilesystemProvider for tile sources stored
 * as Android assets. It omits extending MapTileFileStorageProviderBase,
 * which serves to monitor the sdcard state
 *
 * @author David Brodsky
 * @author Marc Kurtz
 * @author Nicolas Gramlich
 *
 */
public class MapTileAssetProvider extends MapTileModuleProviderBase {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final Logger logger = LoggerFactory.getLogger(MapTileAssetProvider.class);
	
	// directory within /assets where tiles are stored
	private static final String TILE_ASSETS_BASE_PATH = "tiles";
	
	// ===========================================================
	// Fields
	// ===========================================================

	private final long mMaximumCachedFileAge;

	private ITileSource mTileSource;
	
	// Maintain a reference to the system AssetManager
	// provides low-level access to files included in the apk
	// as Android Assets
	private AssetManager assets;

	// ===========================================================
	// Constructors
	// ===========================================================
	/*
	public MapTileAssetProvider(final IRegisterReceiver pRegisterReceiver) {
		this(pRegisterReceiver, TileSourceFactory.DEFAULT_TILE_SOURCE);
	}

	public MapTileAssetProvider(final IRegisterReceiver pRegisterReceiver,
			final ITileSource aTileSource) {
		this(pRegisterReceiver, aTileSource, DEFAULT_MAXIMUM_CACHED_FILE_AGE);
	}
	*/
	/**
	 * Provides a file system based cache tile provider. Other providers can register and store data
	 * in the cache.
	 *
	 * @param pRegisterReceiver
	 */
	//public MapTileAssetProvider(final IRegisterReceiver pRegisterReceiver,
	public MapTileAssetProvider(AssetManager assets, 
			final ITileSource pTileSource, final long pMaximumCachedFileAge) {
		//super(pRegisterReceiver, NUMBER_OF_TILE_FILESYSTEM_THREADS,
		//		TILE_FILESYSTEM_MAXIMUM_QUEUE_SIZE);
		super(NUMBER_OF_TILE_FILESYSTEM_THREADS, TILE_FILESYSTEM_MAXIMUM_QUEUE_SIZE);
		this.assets = assets;
		mTileSource = pTileSource;
		mMaximumCachedFileAge = pMaximumCachedFileAge;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean getUsesDataConnection() {
		return false;
	}

	@Override
	protected String getName() {
		return "Asset Filesystem Cache Provider";
	}

	@Override
	protected String getThreadGroupName() {
		return "filesystem";
	}

	@Override
	protected Runnable getTileLoader() {
		return new TileLoader();
	};

	@Override
	public int getMinimumZoomLevel() {
		return mTileSource != null ? mTileSource.getMinimumZoomLevel() : MINIMUM_ZOOMLEVEL;
	}

	@Override
	public int getMaximumZoomLevel() {
		return mTileSource != null ? mTileSource.getMaximumZoomLevel() : MAXIMUM_ZOOMLEVEL;
	}

	@Override
	public void setTileSource(final ITileSource pTileSource) {
		mTileSource = pTileSource;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class TileLoader extends MapTileModuleProviderBase.TileLoader {

		@Override
		public Drawable loadTile(final MapTileRequestState pState) throws CantContinueException {

			if (mTileSource == null) {
				return null;
			}

			final MapTile tile = pState.getMapTile();

			// if there's no sdcard then don't do anything
			/*
			if (!getSdCardAvailable()) {
				if (DEBUGMODE) {
					logger.debug("No sdcard - do nothing for tile: " + tile);
				}
				return null;
			}
			*/

			// Check the tile source to see if its file is available and if so, then render the
			// drawable and return the tile
			try {
				//Drawable temp = mTileSource.getDrawable(
				//		assets.open(TILE_ASSETS_BASE_PATH +"/" + mTileSource.getTileRelativeFilenameString(tile)));
				//Log.d("MapTileAssetProvider","success! opened " + TILE_ASSETS_BASE_PATH +"/" + mTileSource.getTileRelativeFilenameString(tile));
				//return temp;
				return mTileSource.getDrawable(
								assets.open(TILE_ASSETS_BASE_PATH +"/" + mTileSource.getTileRelativeFilenameString(tile)));
				
			} catch (IOException e1) {
				// If the tile does not exist, return null
				//Log.d("MapTileAssetProvider","cannot open " + TILE_ASSETS_BASE_PATH +"/" + mTileSource.getTileRelativeFilenameString(tile));
				e1.printStackTrace();
				return null;
			} catch (LowMemoryException e) {
				e.printStackTrace();
				throw new CantContinueException(e);
			} catch (org.osmdroid.tileprovider.tilesource.BitmapAssetTileSource.LowMemoryException e) {
				e.printStackTrace();
				throw new CantContinueException(e);
			}
			/*
			final File file = new File(TILE_PATH_BASE,
					mTileSource.getTileRelativeFilenameString(tile) + TILE_PATH_EXTENSION);
			if (file.exists()) {

				try {
					final Drawable drawable = mTileSource.getDrawable(file.getPath());

					// Check to see if file has expired
					final long now = System.currentTimeMillis();
					final long lastModified = file.lastModified();
					final boolean fileExpired = lastModified < now - mMaximumCachedFileAge;

					if (fileExpired) {
						if (DEBUGMODE) {
							logger.debug("Tile expired: " + tile);
						}
						drawable.setState(new int[] {ExpirableBitmapDrawable.EXPIRED });
					}

					return drawable;
				} catch (final LowMemoryException e) {
					// low memory so empty the queue
					logger.warn("LowMemoryException downloading MapTile: " + tile + " : " + e);
					throw new CantContinueException(e);
				}
			}

			// If we get here then there is no file in the file cache
			return null;
			*/
		}
	}
}
