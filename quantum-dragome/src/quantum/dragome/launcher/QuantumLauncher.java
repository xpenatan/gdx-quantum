package quantum.dragome.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.dragome.DragomeApplication;
import com.badlogic.gdx.backends.dragome.DragomeApplicationConfiguration;
import com.badlogic.gdx.backends.dragome.DragomeWindow;
import com.badlogic.gdx.backends.dragome.preloader.AssetDownloader.AssetLoaderListener;
import com.badlogic.gdx.backends.dragome.preloader.AssetFilter.AssetType;
import com.dragome.web.annotations.PageAlias;

import quantum.tests.LocalTest;

@PageAlias(alias= "Quantum")
public class QuantumLauncher extends DragomeApplication
{
	@Override
	public ApplicationListener createApplicationListener()
	{
		getPreloader().loadAsset("resources/sounds/bgsound.ogg", AssetType.Audio, null, new AssetLoaderListener<Object>());
		return new LocalTest();
	}

	@Override
	public DragomeApplicationConfiguration getConfig()
	{
		return null;
	}

	@Override
	protected void onResize()
	{
		int clientWidth= DragomeWindow.getInnerWidth();
		int clientHeight= DragomeWindow.getInnerHeight();
		getCanvas().setWidth(clientWidth);
		getCanvas().setHeight(clientHeight);
		getCanvas().setCoordinateSpaceWidth(clientWidth);
		getCanvas().setCoordinateSpaceHeight(clientHeight);
		super.onResize();
	}
}
