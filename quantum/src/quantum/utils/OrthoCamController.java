package quantum.utils;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamController extends InputAdapter {
	final OrthographicCamera camera;
	final Vector3 curr = new Vector3();
	final Vector3 last = new Vector3(-1, -1, -1);
	final Vector3 delta = new Vector3();
	private int mouse_wheel = 0;
	float zoom_force = 0;
	float zoom_speed = 0;
	protected float scale_vel = 0;
	protected float scale_dir = 1;
	long start_time = System.nanoTime();
	
	protected boolean interpolating = false;

	public OrthoCamController (OrthographicCamera camera) {
		this.camera = camera;
	}
	
	public void update() {
		
		float elapsed_seconds = (System.nanoTime() - start_time) / 1000000000.0f;
		
		if (!interpolating) {
				zoom_force = 0;
				int w = getMouseWheel();
				if (w != 0) {
					scale_dir = w < 0 ? -1 : 1;
					scale_vel = 0.1f;
					zoom_force = (scale_vel * scale_dir);
				} 
				zoom_speed += zoom_force * Math.log(camera.zoom + 2);
				zoom_speed = Math.min(zoom_speed, 0.1f * camera.zoom);
				camera.zoom += zoom_speed * elapsed_seconds * 40;
				zoom_speed *= 0.93f;
				if (Math.abs(zoom_speed) < 0.001) zoom_speed = 0;
				if(camera.zoom <= 0.5f) {
					camera.zoom = 0.5f;
					zoom_speed = 0;
				}
		}
		start_time = System.nanoTime();
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		camera.unproject(curr.set(x, y, 0));
		if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
			camera.unproject(delta.set(last.x, last.y, 0));
			delta.sub(curr);
			camera.position.add(delta.x, delta.y, 0);
		}
		last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		last.set(-1, -1, -1);
		return false;
	}
	
	public int getMouseWheel () {
		int w = mouse_wheel;
		mouse_wheel = 0;
		return w;
	}
	
	@Override
	public boolean scrolled(int amount) {
		mouse_wheel = amount;
		return false;
	}
}
