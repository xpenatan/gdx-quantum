package quantum;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import quantum.tests.LocalTest;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.stencil = 8;
		config.width = 1024;
		config.height = 1024;
		config.title = "GDX Quantum";
//		new LwjglApplication(new Quantum(), config);
		new LwjglApplication(new LocalTest(), config);
		
	}
}
