package quantum.dragome.config;

import com.badlogic.gdx.backends.dragome.DragomeConfiguration;
import com.dragome.commons.DragomeConfiguratorImplementor;

@DragomeConfiguratorImplementor(priority= 11)
public class JsConfiguration extends DragomeConfiguration{
	@Override
	public boolean filterClassPath(String aClassPathEntry) {
		boolean include = super.filterClassPath(aClassPathEntry);
		include|= aClassPathEntry.contains("quantum.jar") || aClassPathEntry.contains("quantum\\bin");
		return include;
	}
}
