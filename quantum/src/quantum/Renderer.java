package quantum;

import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quantum.game.Constants;
import quantum.game.Creature;
import quantum.game.GameInterface;
import quantum.game.Planet;
import quantum.game.Simulation;
import quantum.game.Tree;
import quantum.math.WindowedMean;
import quantum.utils.OrthoCamController;
import quantum.utils.Timer;

public class Renderer {
	OrthographicCamera cam;
	SpriteBatch batch;
	boolean glow = false;
	public static float LINE_WIDTH = 1;
	boolean render_all_paths = false;
	boolean render_is_start_planet = false;
	boolean instancing = true;
	boolean colors_set = false;
	int color_idx = 0;
	Color[] colors = {new Color(1, 0, 0, 1), new Color(0, 1, 0, 1), new Color(0.2f, 0.2f, 1, 1), new Color(1, 1, 0, 1),
		new Color(0, 1, 1, 1), new Color(1, 0, 1, 1), new Color(0.5f, 0.5f, 0.5f, 1)};
	HashMap<Integer, Color> player_color = new HashMap<Integer, Color>();
	long current_time = System.nanoTime();
	Timer timer = new Timer();
	Timer planet_timer = new Timer();
	Timer tree_timer = new Timer();
	Timer creature_timer = new Timer();
	Timer glow_timer = new Timer();
	Timer gui_timer = new Timer();
	Planet selected_planet = null;
	
	int planetCreatureCulled = 0;
	int culled = 0;
	
	ScreenViewport viewport;
	
	public ShapeRenderer shapeRenderer;
	
	OrthoCamController camController;
	
	WindowedMean elapsed_seconds = new WindowedMean(10);
	WindowedMean planet_render_time = new WindowedMean(10);
	WindowedMean tree_render_time = new WindowedMean(10);
	WindowedMean creature_render_time = new WindowedMean(10);
	WindowedMean glow_render_time = new WindowedMean(10);
	WindowedMean gui_render_time = new WindowedMean(10);
	
	public Renderer () {
		cam = new OrthographicCamera();
		viewport = new ScreenViewport(cam);
		shapeRenderer = new ShapeRenderer();
		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);
	}
	
	public void render (Simulation sim) {
		render(sim, null);
	}

	public void render (Simulation sim, GameInterface gui) {
		
		timer.start();
		tree_timer.start();
		tree_timer.pause();
		planet_timer.start();
		planet_timer.pause();
		creature_timer.start();
		creature_timer.pause();
		
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(false);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		camController.update();
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		if(glow) {
			
		} else {
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			renderPass(sim, gui);
		}
		
		elapsed_seconds.addValue(timer.getElapsedSeconds());
		tree_render_time.addValue(tree_timer.getElapsedSeconds());
		planet_render_time.addValue(planet_timer.getElapsedSeconds());
		creature_render_time.addValue(creature_timer.getElapsedSeconds());
		glow_render_time.addValue(glow_timer.getElapsedSeconds());
		gui_render_time.addValue(gui_timer.getElapsedSeconds());
		timer.stop();
		tree_timer.stop();
		planet_timer.stop();
		creature_timer.stop();
		glow_timer.stop();
		gui_timer.stop();
	}
	
	public void renderPass (Simulation sim, GameInterface gui) {
		culled = 0;
		planetCreatureCulled = 0;
		if (!colors_set) {
			for (Planet planet : sim.getPlanets()) {
				allocatePlayerColor(sim, planet.getOwner());
			}
		}
		
		for (Planet planet : sim.getPlanets()) {
			renderPlanetPathsLight(sim, planet);
		}
		Renderer.LINE_WIDTH = 1.5f;
		Gdx.gl.glLineWidth(1.5f);

		tree_timer.start();
		
//		Gdx.gl.glPolygonMode(GL20.GL_FRONT_AND_BACK, GL20.GL_FILL);
//		textures.get("smoke1").bind(0);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//		GL11.glBegin(GL11.GL_QUADS);
//		for (Planet planet : sim.getPlanets()) {
//			for (Tree tree : planet.getTrees()) {
//				tree.setColor(getPlayerColor(planet.getOwner()));
//				tree.renderHalo(canvas, this);
//			}
//		}
//
//		GL11.glEnd();
		Gdx.gl.glEnable(GL20.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		textures.get("smoke1").unbind();
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//
		for (Planet planet : sim.getPlanets()) {
			for (Tree tree : planet.getTrees()) {
				if (visible(tree.getPosition(), 0)) {
					tree.render(this);
				}
				else
					culled++;
			}
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
		tree_timer.pause();

		planet_timer.start();
		Renderer.LINE_WIDTH = 1;
		Gdx.gl.glLineWidth(1);
		shapeRenderer.begin(ShapeType.Line);
		for (Planet planet : sim.getPlanets()) {
			if (visible(planet.getPosition(), 0)) {
				Color col = getPlayerColor(planet.getOwner());
				if (col != null)
					shapeRenderer.setColor(col.r, col.g, col.b, 1);
				else
					shapeRenderer.setColor(0.7f, 0.7f, 1, 1);
	
				planet.renderCreature(this);
			} else
				planetCreatureCulled++;
		}
		shapeRenderer.end();
		Renderer.LINE_WIDTH = 1.5f;
		Gdx.gl.glLineWidth(1.5f);
		planet_timer.pause();
//
		for (Planet planet : sim.getPlanets()) {
			planet_timer.start();
			if (visible(planet.getPosition(), 0)) {
				planet.render(this);
			}
			else
				culled++;
			if (planet == selected_planet) renderPlanetPaths(sim, selected_planet);

			if (render_all_paths) renderPlanetPaths(sim, planet);

			if (render_is_start_planet) {
				if (planet.isStartPlanet()) planet.renderMesh(this, 0.2f, 0, 1, 0);
			}

			planet_timer.pause();
			instancing = false;
			creature_timer.start();
			if (instancing) {
////				if (cam.zoom > 12) {
////					GL11.glPointSize(2);
////					GL11.glBegin(GL11.GL_POINTS);
////					for (Creature creature : planet.getCreatures()) {
////						creature.setColor(getPlayerColor(creature.getOwner()));
////						creature.render(canvas, true);
////					}
////					GL11.glEnd();
////					GL11.glPointSize(1);
////				} else {
////					instance_shader.bind();
////
////					GL11.glBegin(GL11.GL_TRIANGLES);
////					for (Creature creature : planet.getCreatures()) {
////						if (cam.visible(creature.getPosition(), Constants.BOID_SIZE)) {
////							creature.setColor(getPlayerColor(creature.getOwner()));
////							creature.render(canvas, false);
////						} else
////							culled++;
////					}
////					canvas.getGL().getGL2().glEnd();
////
////					instance_shader.unbind();
////				}
			} else {
				if (cam.zoom > 6) {
//					GL11.glPointSize(2);
					shapeRenderer.begin(ShapeType.Filled);
					for (Creature creature : planet.getCreatures()) {
						if (visible(creature.getPosition(), Constants.BOID_SIZE)) {
							creature.setColor(getPlayerColor(creature.getOwner()));
							creature.render(this, true);
						} else
							culled++;
					}
					shapeRenderer.end();
				} else {
					for (Creature creature : planet.getCreatures()) {
						if (visible(creature.getPosition(), Constants.BOID_SIZE)) {
							shapeRenderer.begin(ShapeType.Line);
							creature.setColor(getPlayerColor(creature.getOwner()));
							creature.render(this, false);
							shapeRenderer.end();
						} else
							culled++;
					}
				}
			}
//			GL11.glDisable(GL11.GL_BLEND);
			creature_timer.pause();
		}
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		Renderer.LINE_WIDTH = 1;
		Gdx.gl.glLineWidth(1);

		colors_set = true;
	}
	Vector3 vec3 = new Vector3();	
	
	public boolean visible (Vector2 pos, float radius) {
		
		vec3.x = pos.x;
		vec3.y = pos.y;
		vec3.z = 0;
		cam.project(vec3);
//		float x = getWorldToScreenX(pos.x);
//		float r = getWorldToScreenX(pos.x + radius) - x;
//		float y = getWorldToScreenY(pos.y);

		float x = vec3.x;
		float y = vec3.y;
		float r = 0;
		if(x > 0 + r && x - r < Gdx.graphics.getWidth() && y + r > 0 && y - r < Gdx.graphics.getHeight())
			return true;
		else
			return false;
	}
	
	Vector2 tmp = new Vector2();
	
	public void renderPlanetPaths (Simulation sim, Planet planet) {
		if (planet == null) return;

		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0.3f, 0.3f, 0.7f, 1);
		for (int id : planet.getReachablePlanets()) {
			Planet p = sim.getPlanet(id);
			tmp.set(p.getPosition()).sub(planet.getPosition()).nor();
			shapeRenderer.rectLine(planet.getPosition().x + tmp.x * planet.getRadius(), planet.getPosition().y + tmp.y * planet.getRadius(), p.getPosition().x + tmp.x * -p.getRadius(), p.getPosition().y + tmp.y * -p.getRadius(), Renderer.LINE_WIDTH);
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		planet.renderMesh(this, 0.9f, 0.7f, 0.7f, 1);
	}
	
	public void renderPlanetPathsLight (Simulation sim, Planet planet) {
		if (planet == null) return;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Renderer.LINE_WIDTH = 1;
		Gdx.gl.glLineWidth(1);
			
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 1);
		for (int id : planet.getReachablePlanets()) {
			Planet p = sim.getPlanet(id);
			tmp.set(p.getPosition()).sub(planet.getPosition()).nor();
			shapeRenderer.rectLine(planet.getPosition().x + tmp.x * planet.getRadius(), planet.getPosition().y + tmp.y * planet.getRadius(), p.getPosition().x + tmp.x * -p.getRadius(), p.getPosition().y + tmp.y * -p.getRadius(), Renderer.LINE_WIDTH);
		}
		shapeRenderer.end();
		Renderer.LINE_WIDTH = 1.5f;
		Gdx.gl.glLineWidth(1.5f);
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void centerCamera (Simulation sim) {
		BoundingBox b = new BoundingBox();
		for (Planet planet : sim.getPlanets()) {
			float x = planet.getPosition().x;
			float y = planet.getPosition().y;
			float r = planet.getRadius();
			b.ext(new Vector3(x + r, y, 0));
			b.ext(new Vector3(x, y + r, 0));
			b.ext(new Vector3(x - r, y, 0));
			b.ext(new Vector3(x, y - r, 0));
		}
		b.getDimensions(vec3);
		float width = vec3.x;
		float height = vec3.y;

		float scale = Math.max(width / Gdx.graphics.getWidth(), height / Gdx.graphics.getHeight());
		b.getCenter(vec3);
		cam.position.set(vec3);
		cam.zoom = scale;
	}

	public void setSimulation (Simulation sim) {
		player_color.clear();
		colors_set = false;
		color_idx = 0;
	}
	
	public Color allocatePlayerColor (Simulation sim, int id) {
		if (id == -1) {
			player_color.put(-1, new Color(1, 1, 1, 1));
			return new Color(1, 1, 1, 1);
		}

		if (player_color.containsKey(id)) return player_color.get(id);

		Color col = null;
		if (color_idx >= colors.length)
			col = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
		else
			col = colors[color_idx++];
		player_color.put(id, col);
		return col;
	}
	
	public Color getPlayerColor (int id) {
		return player_color.get(id);
	}
	
	public void dispose () {
//		cam.dispose();
//		if (screen_fbo != null) screen_fbo.dispose();
//
//		if (offscreen_fbo != null) offscreen_fbo.dispose();
//
//		if (fbo != null) fbo.dispose();
//
//		if (fbo_tmp != null) fbo_tmp.dispose();
//
//		if (vert_shader != null) vert_shader.dispose();
//
//		if (hort_shader != null) hort_shader.dispose();
//
//		if (instance_shader != null) instance_shader.dispose();
//
//		for (Texture texture : textures.values())
//			texture.dispose();

		Gdx.app.log("[Renderer]", "disposed");
	}
	
	public void useGlow (boolean value) {
		glow = value;
//		if (fbo == null) glow = false;
	}
	
	public OrthographicCamera getCamera() {
		return cam;
	}
	
	public long getSystemTime () {
		return current_time;
	}
	
	public double getRenderTime () {
		return elapsed_seconds.getMean() * 1000;
	}
	
	public double getCreatureRenderTime () {
		return creature_render_time.getMean() * 1000;
	}
	
	public double getTreeRenderTime () {
		return tree_render_time.getMean() * 1000;
	}
	
	public double getPlanetRenderTime () {
		return planet_render_time.getMean() * 1000;
	}

	public int getCulledObjects () {
		return culled;
	}

	public int getPlanetCreatureCulled () {
		return planetCreatureCulled;
	}
}
