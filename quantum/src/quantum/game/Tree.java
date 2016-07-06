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

package quantum.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import quantum.Renderer;



public strictfp class Tree extends GameObject implements AliveGameObject {
	class Branch {
		Simulation sim;
		int planet = -1;
		float angle;
		float height;
		float curr_height = 0;
		boolean wither = false;
		Vector2 pos = new Vector2();
		Vector2 dir = new Vector2();
		Branch[] children = new Branch[2];
		int depth;
		int grown_creatures;
		int creature = -1;
		Tree tree;

		public void read (DataInputStream in) throws Exception {
			planet = in.readInt();
			angle = in.readFloat();
			height = in.readFloat();
			curr_height = in.readFloat();
			pos.x = in.readFloat();
			pos.y = in.readFloat();
			dir.x = in.readFloat();
			dir.y = in.readFloat();
			depth = in.readInt();
			grown_creatures = in.readInt();
			creature = in.readInt();
			wither = in.readBoolean();
			if (in.readByte() == 1) {
				children[0] = new Branch(sim, tree);
				children[1] = new Branch(sim, tree);
				children[0].read(in);
				children[1].read(in);
			}
		}

		public void write (DataOutputStream out) throws Exception {
			out.writeInt(planet);
			out.writeFloat(angle);
			out.writeFloat(height);
			out.writeFloat(curr_height);
			out.writeFloat(pos.x);
			out.writeFloat(pos.y);
			out.writeFloat(dir.x);
			out.writeFloat(dir.y);
			out.writeInt(depth);
			out.writeInt(grown_creatures);
			out.writeInt(creature);
			out.writeBoolean(wither);
			if (children[0] != null) {
				out.writeByte(1);
				children[0].write(out);
				children[1].write(out);
			} else
				out.writeByte(0);
		}

		public Branch (Simulation sim, Tree tree) {
			this.sim = sim;
			this.tree = tree;
			this.tree.health += Constants.BRANCH_HEALTH;
		}

		public Branch (Simulation sim, Tree tree, int planet, Vector2 pos, float angle, float height, int depth) {
			this.pos.set(pos);
			this.planet = planet;
			this.angle = angle;
			this.height = height;
			this.sim = sim;
			this.tree = tree;
			this.tree.health += Constants.BRANCH_HEALTH;
			dir.x = (float)Math.cos(Math.toRadians(angle));
			dir.y = (float)Math.sin(Math.toRadians(angle));
			this.depth = depth;
		}

		public void update () {
			if (sim.getPlanet(planet).getResources() == 0 && children[0] == null && !wither && creature == -1) {
				wither = true;
				grown_creatures = 0;
				return;
			}

			if (wither) {
				if (sim.getPlanet(planet).isRegrowing()) {
					if (depth > 1) {
						curr_height -= Constants.TREE_GROWTH;
						curr_height = Math.max(curr_height, 0);
					}
				} else {
					wither = false;
					grown_creatures = 0;
				}
				return;
			}

			if (children[0] != null) {
				if (children[0].wither && children[0].curr_height <= 0 && children[1].wither && children[1].curr_height <= 0) {
					children[0] = null;
					children[1] = null;
					wither = true;
					grown_creatures = 0;
					return;
				}
			}

			if (curr_height < height && grown_creatures == 0) {
				curr_height += Constants.TREE_GROWTH;
			} else {
				if (grown_creatures < Constants.TREE_MAX_CREATURES || depth == Constants.TREE_DEPTH) {
					if (creature == -1 && tree.getHealth() > 0) {
						Planet planet = sim.getPlanet(this.planet);
						if (planet.requestResourceForCreature()) {
							Creature creature = new Creature(sim, planet.getId(), planet.getOwner(), pos.x + dir.x * curr_height, pos.y
								+ dir.y * curr_height, angle + 90 * sim.rand() - 90 * sim.rand(), planet.getStrength(),
								planet.getHealth(), planet.getSpeed());
							planet.addCreature(creature);
							sim.addObject(creature);
							this.creature = creature.getId();
						}
					} else {
						if (sim.getCreature(creature) != null && sim.getCreature(creature).isBorn()) {
							creature = -1;
							grown_creatures++;
						}
					}
				} else {
					if (children[0] == null) {
						children[0] = new Branch(sim, tree, planet, pos.cpy().add(dir.cpy().scl(curr_height)), angle
							+ Constants.TREE_ANGLE * sim.rand(0.5f, 1), Constants.TREE_DELATION * height * sim.rand(0.5f, 1.5f),
							depth + 1);
						children[1] = new Branch(sim, tree, planet, pos.cpy().add(dir.cpy().scl(curr_height)), angle
							- Constants.TREE_ANGLE * sim.rand(0.5f, 1), Constants.TREE_DELATION * height * sim.rand(0.5f, 1.5f),
							depth + 1);
					} else {
						children[0].update();
						children[1].update();
					}
				}
			}
		}

		public void renderBranch (Renderer renderer) {
			renderer.shapeRenderer.rectLine(pos.x, pos.y, pos.x + dir.x * curr_height, pos.y + dir.y * curr_height, Renderer.LINE_WIDTH);
			if (children[0] != null) {
				children[0].renderBranch(renderer);
				children[1].renderBranch(renderer);
			}
		}

		public void destroyCreatures () {
			if (creature != -1) {
				if (sim.getCreature(creature) != null) {
					sim.getCreature(creature).setHealth(0);
					sim.getCreature(creature).setScale(1);
				}
			}

			if (children[0] != null) {
				children[0].destroyCreatures();
				children[1].destroyCreatures();
			}
		}

		public boolean hasCreaturesGrowing () {
			if (children[0] != null) {
				return children[0].hasCreaturesGrowing() || children[1].hasCreaturesGrowing();
			} else
				return creature != -1;
		}
	}

	Branch root;
	int planet;
	float health;
	float scale = 1;
	Color col = new Color(0.2f, 0.2f, 0.2f, 1);
// Timer timer = new Timer( );
	long start_time = 0;

	protected Tree (Simulation sim) {
		super(sim);
		root = new Branch(sim, this);
// timer.start();
		start_time = System.nanoTime();
	}

	public Tree (Simulation sim, Vector2 pos, int planet) {
		super(sim, pos);
		this.planet = planet;

		float angle = 0;
		Vector2 tmp = pos.cpy().sub(sim.getPlanet(planet).getPosition());
		angle = (float)Math.toDegrees(Math.atan2(tmp.y, tmp.x));

		root = new Branch(sim, this, planet, pos, angle, Constants.TREE_HEIGHT, 1);
// timer.start();
		start_time = System.nanoTime();
	}

	public void render (Renderer renderer) {
		renderer.shapeRenderer.setColor(0.7f * scale, 0.7f * scale, 1 * scale, 1);
		renderer.shapeRenderer.begin(ShapeType.Line);
		root.renderBranch(renderer);
		renderer.shapeRenderer.end();
	}

	int grow_direction = -1;
	float halo_size = 0.11f;

	public void renderHalo (Renderer renderer) {

		if (renderer.getCamera().zoom > 1) {
			float elapsed_seconds = (renderer.getSystemTime() - start_time) / 1000000000.0f;
			float x = -(float)Math.sin(Math.toRadians(root.angle));
			float y = (float)Math.cos(Math.toRadians(root.angle));
			float alpha = 1;
			if (renderer.getCamera().zoom > 20)
				alpha = 1;
			else
				alpha = Math.min(1 - (20 - renderer.getCamera().zoom) / 19.0f, 1);

			halo_size += grow_direction * 0.3f * elapsed_seconds;
			if (halo_size < 0.49f) grow_direction = 1;
			if (halo_size > 1) grow_direction = -1;
			float size = 0.11f + 0.66f * halo_size;

//			GL11.glColor4f(col.r, col.g, col.b, scale * alpha);
//			GL11.glTexCoord2f(0, 0);
//			GL11.glVertex2f(pos.x - 100 * x, pos.y - 100 * y);
//			GL11.glTexCoord2f(1, 0);
//			GL11.glVertex2f(pos.x + 100 * x, pos.y + 100 * y);
//			GL11.glTexCoord2f(1, 1);
//			GL11.glVertex2f(pos.x + 100 * x + y * 2000 * size, pos.y + 100 * y - x * 2000 * size);
//			GL11.glTexCoord2f(0, 1);
//			GL11.glVertex2f(pos.x - 100 * x + y * 2000 * size, pos.y - 100 * y - x * 2000 * size);
			start_time = renderer.getSystemTime();
		}
	}

	@Override
	public void update () {
		boolean dead = isDead();
		if (dead == false) root.update();

		// if( sim.getPlanet( planet ).getResources() <= 0 && root.hasCreaturesGrowing() == false )
		// health = 0;

		if (health <= 0) scale -= Constants.TREE_DEATH_DECREASE;
	}

	public void read (DataInputStream in) throws Exception {
		super.read(in);
		planet = in.readInt();
		health = in.readFloat();
		root.read(in);
	}

	public void write (DataOutputStream out) throws Exception {
		super.write(out);
		out.writeInt(planet);
		out.writeFloat(health);
		root.write(out);
	}

	public float getHealth () {
		return health;
	}

	public void adjustHealth (float health) {
		this.health += health;
		if (health <= 0) {
			destroyCreatures();
		}
	}

	private void destroyCreatures () {
		root.destroyCreatures();
	}

	public int getPlanet () {
		return planet;
	}

	public boolean isDead () {
		return health <= 0 && scale < 0.1f;
	}

	public void setColor (Color playerColor) {
		col.set(playerColor);
	}
}
