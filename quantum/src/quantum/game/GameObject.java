package quantum.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.badlogic.gdx.math.Vector2;

public strictfp class GameObject {
	Vector2 pos = new Vector2();
	Simulation sim;
	int id = 0;

	protected GameObject (Simulation sim) {
		this.sim = sim;
		this.id = sim.getNextId();
	}

	public GameObject (Simulation sim, Vector2 pos) {
		this.sim = sim;
		this.pos.set(pos);
		this.id = sim.getNextId();
	}

	public void setSimulation (Simulation sim) {
		this.sim = sim;
	}

	public Simulation getSimulation () {
		return sim;
	}

	public int getId () {
		return id;
	}

	public Vector2 getPosition () {
		return pos;
	}

	public void update () {
	};

	public void render () {
	};

	public void read (DataInputStream in) throws Exception {
		id = in.readInt();
		pos.x = in.readFloat();
		pos.y = in.readFloat();
	}

	public void write (DataOutputStream out) throws Exception {
		out.writeInt(id);
		out.writeFloat(pos.x);
		out.writeFloat(pos.y);
	}
}
