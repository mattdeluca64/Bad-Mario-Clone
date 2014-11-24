package org.util.constants;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.extension.physics.box2d.PhysicsFactory;
public class Collisions{
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_PLAYER = 2;
	public static final short CATEGORYBIT_GROUND = 4;
	public static final short CATEGORYBIT_BOTTOM = 8;
	public static final short CATEGORYBIT_FOOT = 16;
	public static final short CATEGORYBIT_HEAD = 32;

	//wall collides with player
	public static final short MASKBITS_WALL = 
	CATEGORYBIT_WALL+ CATEGORYBIT_PLAYER+ CATEGORYBIT_GROUND+ 
		CATEGORYBIT_BOTTOM;
	//ground gets player && feet
	public static final short MASKBITS_GROUND =
	CATEGORYBIT_WALL+ CATEGORYBIT_PLAYER+ CATEGORYBIT_GROUND+ 
		CATEGORYBIT_BOTTOM+ CATEGORYBIT_FOOT;
	//bottom gets player && head
	public static final short MASKBITS_BOTTOM =
	CATEGORYBIT_WALL+ CATEGORYBIT_PLAYER+ CATEGORYBIT_GROUND+ 
		CATEGORYBIT_BOTTOM+CATEGORYBIT_HEAD;
	//player gets all the above
	public static final short MASKBITS_PLAYER =
	CATEGORYBIT_WALL+ CATEGORYBIT_PLAYER+ CATEGORYBIT_GROUND+ 
		CATEGORYBIT_BOTTOM+ CATEGORYBIT_FOOT + CATEGORYBIT_HEAD;
	//head&feet only get their respectives
	public static final short MASKBITS_HEAD = 
	CATEGORYBIT_PLAYER+ CATEGORYBIT_BOTTOM+ CATEGORYBIT_FOOT + CATEGORYBIT_HEAD;
	public static final short MASKBITS_FOOT = 
	CATEGORYBIT_PLAYER+ CATEGORYBIT_GROUND+ + CATEGORYBIT_FOOT + CATEGORYBIT_HEAD;

		//-------------------------
	public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1, 0.0f, 0.45f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	public static final FixtureDef BOX_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1, 0.0f, 0.45f, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
	public static final FixtureDef GROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1.0f, 0.0f, 0.45f, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
		//-------------------------
	public static final FixtureDef HEAD_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			0, 0.0f, 0.00f, true, CATEGORYBIT_HEAD, MASKBITS_HEAD, (short)0);
	public static final FixtureDef FOOT_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			0, 0.0f, 0.00f, true, CATEGORYBIT_FOOT, MASKBITS_FOOT, (short)0);
		//-------------------------
	public static final FixtureDef PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1.0f, 0.0f, 0.45f, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);
		//-------------------------
	public static final FixtureDef BOTTOM_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			0.0f, 0.4f, 0.00f, false, CATEGORYBIT_BOTTOM, MASKBITS_BOTTOM, (short)0);
	//left/right use wall
	//use ground for top..for now
		//-------------------------
	public static final FixtureDef BOXSENSOR_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			0.0f, 0.0f,0.45f, true, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
		//-------------------------
}
