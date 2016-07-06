package quantum;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import quantum.forms.StartMenu;
import quantum.gui.Gui;
import quantum.net.Client;
import quantum.net.Server;

public class Quantum implements ApplicationListener {
	public interface DisplayListener {
		public void display ();
	}
	public static Object LOCK = new Object();
	
	Gui gui;
	StartMenu menu;
	Server server;
	Client client;
	String user_name;
	ArrayList<DisplayListener> listeners = new ArrayList<DisplayListener>();
	
	@Override
	public void create() {
		gui = new Gui();
		menu = new StartMenu(this, gui);
	}

	@Override
	public void render() {
		synchronized (LOCK) {
			Gdx.gl.glViewport(0,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			synchronized (listeners) {
				for (DisplayListener listener : listeners)
					listener.display();
			}

//			gui.render();
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		
		closeServerAndClient();
		
	}
	
	public void addDisplayListener (DisplayListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeDisplayListener (DisplayListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public void setServer (Server server) {
		this.server = server;
	}

	public Server getServer () {
		return server;
	}

	public void setClient (Client client) {
		this.client = client;
	}

	public Client getClient () {
		return client;
	}
	
	public void createServer (int port_number, String name, String ip) throws Exception {
		if (server != null) throw new RuntimeException("oh no's! Server already running!");

		server = new Server(port_number, name, ip);
	}

	public void createClient (String user_name, String ip, int port) throws Exception {
		client = new Client(user_name, ip, port);
	}

	public void closeServerAndClient () {
		if (client != null) client.dispose();

		if (server != null) server.shutdown("");

		server = null;
		client = null;
	}
}
