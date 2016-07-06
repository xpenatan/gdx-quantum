//
// Copyright (c) 2009 Mario Zechner.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the GNU Lesser Public License v2.1
// which accompanies this distribution, and is available at
// http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// 
// Contributors:
//     Mario Zechner - initial API and implementation
//

package quantum.tests;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import quantum.Renderer;
import quantum.game.Bot;
import quantum.game.Creature;
import quantum.game.Planet;
import quantum.game.Simulation;


public class LocalTest implements ApplicationListener {

	Simulation sim;
	Creature c1;
	Creature c2;
	Renderer renderer;
	
	Array<Bot> bots = new Array<Bot>();
	
	Vector2 tmp = new Vector2();
	
	BitmapFont font;
	SpriteBatch batch;
	ScreenViewport ui;
	
	@Override
	public void create() {
		sim = new Simulation(true);
		ui = new ScreenViewport();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("resources/sounds/bgsound.ogg"));
		bgMusic.setLooping(true);
		bgMusic.play();
		
		boolean flag = true;
		if(flag) { 
			
			Planet planet1 = addPlanet(-1, 4000, 0, 0, 1, 1, 1, 100);
			Planet planet3 = addPlanet(-1, 8000, 0, 0, 1, 1, 1, 30);
			Planet planet4 = addPlanet(-1, 12000, 0, 0, 1, 1, 1, 100);
			Planet planet5 = addPlanet(-1, 12000, -3500, 0, 1, 1, 1, 30);
			Planet planet6 = addPlanet(-1, 12000, -7000, 0, 1, 1, 1, 100);
			Planet planet7 = addPlanet(-1, 8000, -7000, 0, 1, 1, 1, 30);
			Planet planet8 = addPlanet(-1, 4000, -7000, 0, 1, 1, 1, 30);
			Planet planet9 = addPlanet(-1, 0, -7000, 0, 1, 1, 1, 30);
			Planet planet10 = addPlanet(0, -4000, -7000, 3, 1, 1, 1, 100);   // START R
			bots.add(new Bot(null, planet10.getOwner()));
			Planet planet11 = addPlanet(-1, -4000, -3500, 0, 1, 1, 1, 30);
			Planet planet12 = addPlanet(-1, -4000, 0, 0, 1, 1, 1, 30);
			Planet planet13 = addPlanet(-1, -4000, 3500, 0, 1, 1, 1, 30);
			Planet planet14 = addPlanet(-1, -4000, 7000, 0, 1, 1, 1, 100);
			Planet planet15 = addPlanet(-1, 0, 7000, 0, 1, 1, 1, 30);
			Planet planet16 = addPlanet(-1, 4000, 7000, 0, 1, 1, 1, 30);
			Planet planet17 = addPlanet(-1, 8000, 7000, 0, 1, 1, 1, 30);
			Planet planet18 = addPlanet(-1, 12000, 7000, 0, 1, 1, 1, 100);
			
			Planet planet19 = addPlanet(-1, 20000, -7000, 0, 1, 1, 1, 100);
			Planet planet20 = addPlanet(-1, 20000, -3500, 0, 1, 1, 1, 30);
			Planet planet21 = addPlanet(-1, 20000, 0, 0, 1, 1, 1, 30);
			Planet planet22 = addPlanet(-1, 20000, 3500, 0, 1, 1, 1, 30);
			Planet planet23 = addPlanet(1, 20000, 7000, 3, 1, 1, 1, 100); // START G
			bots.add(new Bot(null, planet23.getOwner()));
			Planet planet24 = addPlanet(-1, 24000, 7000, 0, 1, 1, 1, 30);
			Planet planet25 = addPlanet(-1, 28000, 7000, 0, 1, 1, 1, 30);
			Planet planet26 = addPlanet(-1, 32000, 4500, 0, 1, 1, 1, 30);
			Planet planet27 = addPlanet(-1, 32000, 0, 0, 1, 1, 1, 100);
			Planet planet28 = addPlanet(-1, 32000, -4500, 0, 1, 1, 1, 30);
			Planet planet29 = addPlanet(-1, 28000, -7000, 0, 1, 1, 1, 30);
			Planet planet30 = addPlanet(-1, 24000, -7000, 0, 1, 1, 1, 30);
			
			Planet planet31 = addPlanet(-1, 40000, -7000, 0, 1, 1, 1, 100);
			Planet planet32 = addPlanet(-1, 43500, -3500, 0, 1, 1, 1, 30);
			Planet planet33 = addPlanet(-1, 47000, -0, 0, 1, 1, 1, 100); // center of X
			Planet planet34 = addPlanet(-1, 50500, 3500, 0, 1, 1, 1, 30);
			Planet planet35 = addPlanet(-1, 54000, 7000, 0, 1, 1, 1, 100);
			Planet planet36 = addPlanet(-1, 50500, -3500, 0, 1, 1, 1, 30);
			Planet planet37 = addPlanet(2, 54000, -7000, 3, 1, 1, 1, 100);  // START B
			bots.add(new Bot(null, planet37.getOwner()));
			Planet planet38 = addPlanet(-1, 43500, 3500, 0, 1, 1, 1, 30);
			Planet planet39 = addPlanet(-1, 40000, 7000, 0, 1, 1, 1, 100);
			
			//G paths
			planet1.addReachablePlanet(planet12.getId());
			planet12.addReachablePlanet(planet1.getId());
			planet1.addReachablePlanet(planet3.getId());
			planet3.addReachablePlanet(planet1.getId());
			planet3.addReachablePlanet(planet4.getId());
			planet4.addReachablePlanet(planet3.getId());
			planet4.addReachablePlanet(planet5.getId());
			planet5.addReachablePlanet(planet4.getId());
			planet5.addReachablePlanet(planet6.getId());
			planet6.addReachablePlanet(planet5.getId());
			planet6.addReachablePlanet(planet7.getId());
			planet7.addReachablePlanet(planet6.getId());
			planet7.addReachablePlanet(planet8.getId());
			planet8.addReachablePlanet(planet7.getId());
			planet8.addReachablePlanet(planet9.getId());
			planet9.addReachablePlanet(planet8.getId());
			planet9.addReachablePlanet(planet10.getId());
			planet10.addReachablePlanet(planet9.getId());
			planet10.addReachablePlanet(planet11.getId());
			planet11.addReachablePlanet(planet10.getId());
			planet11.addReachablePlanet(planet12.getId());
			planet12.addReachablePlanet(planet11.getId());
			planet12.addReachablePlanet(planet13.getId());
			planet13.addReachablePlanet(planet12.getId());
			planet13.addReachablePlanet(planet14.getId());
			planet14.addReachablePlanet(planet13.getId());
			planet14.addReachablePlanet(planet15.getId());
			planet15.addReachablePlanet(planet14.getId());
			planet15.addReachablePlanet(planet16.getId());
			planet16.addReachablePlanet(planet15.getId());
			planet16.addReachablePlanet(planet17.getId());
			planet17.addReachablePlanet(planet16.getId());
			planet17.addReachablePlanet(planet18.getId());
			planet18.addReachablePlanet(planet17.getId());
			
			//D paths
			planet19.addReachablePlanet(planet20.getId());
			planet20.addReachablePlanet(planet19.getId());
			
			planet20.addReachablePlanet(planet21.getId());
			planet21.addReachablePlanet(planet20.getId());
			
			planet21.addReachablePlanet(planet22.getId());
			planet22.addReachablePlanet(planet21.getId());
			
			planet22.addReachablePlanet(planet23.getId());
			planet23.addReachablePlanet(planet22.getId());
			
			planet23.addReachablePlanet(planet24.getId());
			planet24.addReachablePlanet(planet23.getId());
			
			planet24.addReachablePlanet(planet25.getId());
			planet25.addReachablePlanet(planet24.getId());
			
			planet25.addReachablePlanet(planet26.getId());
			planet26.addReachablePlanet(planet25.getId());
	//		planet26.addReachablePlanet(planet27.getId());
	//		planet27.addReachablePlanet(planet26.getId());
			
	//		planet27.addReachablePlanet(planet28.getId());
	//		planet28.addReachablePlanet(planet27.getId());
			planet28.addReachablePlanet(planet29.getId());
			planet29.addReachablePlanet(planet28.getId());
			planet29.addReachablePlanet(planet30.getId());
			planet30.addReachablePlanet(planet29.getId());
			planet30.addReachablePlanet(planet19.getId());
			planet19.addReachablePlanet(planet30.getId());
			
			//X paths
			planet31.addReachablePlanet(planet32.getId());
			planet32.addReachablePlanet(planet31.getId());
			planet32.addReachablePlanet(planet33.getId());
			planet33.addReachablePlanet(planet32.getId());
			planet33.addReachablePlanet(planet34.getId());
			planet34.addReachablePlanet(planet33.getId());
			planet34.addReachablePlanet(planet35.getId());
			planet35.addReachablePlanet(planet34.getId());
			planet33.addReachablePlanet(planet36.getId());
			planet36.addReachablePlanet(planet33.getId());
			planet36.addReachablePlanet(planet37.getId());
			planet37.addReachablePlanet(planet36.getId());
			planet33.addReachablePlanet(planet38.getId());
			planet38.addReachablePlanet(planet33.getId());
			planet38.addReachablePlanet(planet39.getId());
			planet39.addReachablePlanet(planet38.getId());
	
			
			planet26.addReachablePlanet(planet39.getId());
			planet39.addReachablePlanet(planet26.getId());
			planet28.addReachablePlanet(planet31.getId());
			planet31.addReachablePlanet(planet28.getId());
		
			planet5.addReachablePlanet(planet20.getId());
			planet20.addReachablePlanet(planet5.getId());
			
			planet18.addReachablePlanet(planet23.getId());
			planet23.addReachablePlanet(planet18.getId());
			
			planet23.addReachablePlanet(planet27.getId());
			planet27.addReachablePlanet(planet23.getId());
			planet19.addReachablePlanet(planet27.getId());
			planet27.addReachablePlanet(planet19.getId());
		}
		else {
			
			Planet planet1 = addPlanet(0, 0, 0, 60, 1, 1, 1, 100);
			Planet planet2 = addPlanet(1, 4000, 0, 5, 1, 1, 1, 30);
			
			planet1.addReachablePlanet(planet2.getId());
			planet2.addReachablePlanet(planet1.getId());
			
			bots.add(new Bot(null, planet1.getOwner()));
			bots.add(new Bot(null, planet2.getOwner()));
			
		}
			
		
		renderer = new Renderer();
		renderer.centerCamera(sim);
	}
	
	public Planet addPlanet(int owner, float x, float y, int troops, float strength, float health, float speed, int max_creatures) {
		Planet planet = new Planet(sim, tmp.set(x, y), 0, strength, health, speed, max_creatures);
		sim.addObject(planet);
		if(owner >= 0) {
			planet.setOwner(owner);
			planet.spawnTree();
		}
		for (int j = 0; j < troops; j++)
			planet.spawnCreature();
		return planet;
	}

	@Override
	public void render() {
		try {
			sim.update();
			for(int i = 0; i < bots.size;i++)
				bots.get(i).update(sim);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderer.render(sim);
		Camera camera = ui.getCamera();
		camera.update();
		
		font.getData().setScale(1, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, "\nQuantum port by xpenatan (Early build not playable)", 80,80);
		
		
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond()
			+ "\nsimulation: " + sim.getSimulationUpdateTime() 
			+ "\nrendering: " + renderer.getRenderTime()
			+ "\ntrees: " + renderer.getTreeRenderTime() 
			+ "\ncreatures: " + renderer.getCreatureRenderTime()
			+ "\nplanets: " + renderer.getPlanetRenderTime()
			+ "\nobject: " + sim.getObjectCount()
			+ "\nculled: " + renderer.getCulledObjects()
			+ "\nculled planet creature: " + renderer.getPlanetCreatureCulled(), 80, Gdx.graphics.getHeight()-80);
		
				
		batch.end();
		
		
		List<Planet> planets = sim.getPlanets();
		Iterator<Planet> iterator = planets.iterator();
		
		batch.setProjectionMatrix(renderer.getCamera().combined);
		batch.begin();
		
		while(iterator.hasNext()) {
			Planet planet = iterator.next();
			int size = planet.getTrees().size();
			int maxResources = planet.getMaxResources();
			int resources = planet.getResources();
			int csize = planet.getCreatures().size();
			Vector2 position = planet.getPosition();
			font.getData().setScale(10, 10);
			if(maxResources == 100) {
				font.draw(batch, "Trees: " + size
						+ "\nResources: " + resources 
						+ "\nCreatures: " + csize, position.x-250, position.y-250);
			}
			else {
				font.draw(batch, "Trees: " + size
					+ "\nResources: " + resources
					+ "\nCreatures: " + csize, position.x-250, position.y-600);
			}
		}
		
		batch.end();
		
	}
	
	@Override
	public void resize(int width, int height) {
		ui.update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
