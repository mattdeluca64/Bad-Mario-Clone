package org.games.entity;
import org.util.constants.Collisions;
import org.games.Game;
import org.util.PhysicsEditorLoader;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.util.color.Color;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.BitmapFont;
import org.andengine.util.HorizontalAlign;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import android.hardware.SensorManager;
import android.widget.Toast;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import android.graphics.Typeface;
public class Mario{
	//-------------------------------
	//States
	private static int feetcontacts = 0;
	private static boolean ONGROUND;
	private static boolean JUMPING;
	private static boolean RUNNING;
	private static boolean SKIDDING;
	private static boolean DIRECTION;
	private static boolean LEFT;
	private static boolean RIGHT;
	private static boolean TOGGLE = false;
	//-------------------------------
	//Constants
	private static final float MOVEMENT = 11.8f;
	private static final float RUNSPEED = 10.0f;
	private static final float WALKSPEED = 4.0f;
	private static final float MAXSPEED = 12.0f;
													//450,4,1 good height
	//for apply linear impulse
	private static float JUMPFORCE = 5f;
	private static final int CAP1 = 3;
	private static final float JUMPFACTOR = 0.90f;
	//for apply force
	//private static float JUMPFORCE = 200f;
	//private static final int CAP1 = 20;
	//private static final float   JUMPFACTOR = 2.08f;
	private static int JUMPTIMER;
	private static float currentJUMPFORCE ; 
	//-------------------------------
	//Fields
	private static BitmapTextureAtlas PlayerAtlas;
	private static TiledTextureRegion PlayerTexture;
	//-------------------------------
	public static AnimatedSprite Player;
	public static Body PlayerBody;
	public static Sound JumpSound;
	//-------------------------------
	private static PhysicsEditorLoader loader;
	//---------------------------------------------------------------------------------------------------------------
	//Player billy = new Player(Game parent,Engine engine)
	//billy.createPlayer(x,y,currentActivity);
	//---------------------------------------------------------------------------------------------------------------
	public void createPlayer(int x,int y,Game parent){
		DIRECTION=true;
		JUMPING=false;
		ONGROUND=false;
		Player = new AnimatedSprite(x,y,this.PlayerTexture,parent.getVertexBufferObjectManager()){
			@Override
			protected void onManagedUpdate(float seconds){
				tick();
				super.onManagedUpdate(seconds);
			}
		};
		try{
			this.loader.setAssetBasePath("xml/");
			this.PlayerBody = this.loader.load(parent,parent.World,"mario.xml",this.Player,true,true);
		}catch(Exception e){
			//
		}
		final PolygonShape shape = new PolygonShape();
		final Vector2[] verts = new Vector2[]{
			new Vector2(-0.25f,0.51f),
			new Vector2(-0.25f,0.4f),
			new Vector2(0.25f,0.4f),
			new Vector2(0.25f,0.51f)};
		shape.set(verts);
		final PolygonShape shape1 = new PolygonShape();
		final Vector2[] verts1 = new Vector2[]{
			new Vector2(-0.25f,-0.3f),
			new Vector2(-0.25f,-0.4f),
			new Vector2(0.25f,-0.4f),
			new Vector2(0.25f,-0.3f)};
		shape1.set(verts1);
		//final FixtureDef fd = PhysicsFactory.createFixtureDef(0f,0f,0f);
		Collisions.PLAYER_FIXTURE_DEF.shape = shape;
		final Fixture feet = this.PlayerBody.createFixture(Collisions.PLAYER_FIXTURE_DEF);
		shape.dispose();
		Collisions.PLAYER_FIXTURE_DEF.shape = shape1;
		final Fixture head = this.PlayerBody.createFixture(Collisions.PLAYER_FIXTURE_DEF);
		shape1.dispose();
		//Collisions.FOOT_FIXTURE_DEF.shape = shape;
		//final Fixture feet = this.PlayerBody.createFixture(Collisions.FOOT_FIXTURE_DEF);
		//Collisions.HEAD_FIXTURE_DEF.shape = shape1;
		//final Fixture head = this.PlayerBody.createFixture(Collisions.HEAD_FIXTURE_DEF);
		//feet.setBullet(true);
		this.PlayerBody.getFixtureList().get(1).setUserData("feet");
		this.PlayerBody.getFixtureList().get(0).setUserData("body");
		this.PlayerBody.getFixtureList().get(2).setUserData("head");
		//feet.setUserData("feet");
		this.PlayerBody.setUserData("player");
		this.PlayerBody.setFixedRotation(true);
		this.Player.setCurrentTileIndex(0);
		parent.World.registerPhysicsConnector(
				new PhysicsConnector(this.Player, this.PlayerBody, true, true));
		JUMPFORCE *= -PlayerBody.getMass();
	}
	//---------------------------------------------------------------------------------------------------------------
	//Ticker
	private static void tick(){
		//tick
		if(JUMPING && JUMPTIMER != CAP1){
			PlayerBody.applyLinearImpulse(new Vector2(0,currentJUMPFORCE),PlayerBody.getWorldCenter());
			//PlayerBody.applyForce(new Vector2(0,currentJUMPFORCE),PlayerBody.getWorldCenter());
			JUMPTIMER++;
			currentJUMPFORCE /= JUMPFACTOR;
		}
		if(RIGHT){
			Vector2 v = PlayerBody.getLinearVelocity();
			if(v.x > MAXSPEED ){
				PlayerBody.setLinearVelocity(v.mul(MAXSPEED / v.x));
			}

			PlayerBody.applyForce(new Vector2(MOVEMENT,0f),PlayerBody.getWorldCenter());

			if(v.x<0.0f && !LEFT && ONGROUND){
				SKIDDING=true;
				Player.stopAnimation(4);
			}else if (v.x>=0.0f && SKIDDING && ONGROUND){
				SKIDDING=false;
				walk();
			}

			if(RUNNING && ONGROUND){
				if(v.x<RUNSPEED && v.x>=0.0f && ONGROUND){
					RUNNING=false;
					walk();
				}
			}
			if(!RUNNING && ONGROUND){
				if(v.x>RUNSPEED && ONGROUND){
					RUNNING=true;
					run();
				}
			}
		}
		if(LEFT){
			Vector2 v = PlayerBody.getLinearVelocity();
			if(v.x < -MAXSPEED){
				PlayerBody.setLinearVelocity(v.mul(-(MAXSPEED / v.x)));
			}

			PlayerBody.applyForce(new Vector2(-MOVEMENT,0f),PlayerBody.getWorldCenter());

			if(v.x>0.0f && !RIGHT && ONGROUND){
				SKIDDING=true;
				Player.stopAnimation(10);
			}else if (v.x<=0.0f && SKIDDING && ONGROUND){
				SKIDDING=false;
				walk();
			}

			if(RUNNING && ONGROUND){
				if(v.x>-RUNSPEED && v.x<0.0f && ONGROUND){
					RUNNING=false;
					walk();
				}
			}

			if(!RUNNING && ONGROUND){
				if(v.x<-RUNSPEED && ONGROUND){
					RUNNING=true;
					run();
				}
			}
		}
	}
	//---------------------------------------------------------------------------------------------------------------
	//Actions & States
	public void leftGround(){
		if (feetcontacts > 0)
			feetcontacts--;
		if (feetcontacts <= 0){
			feetcontacts = 0;
			ONGROUND=false;
		}

	}
	public void hitGround(){
		feetcontacts++;
		if(ONGROUND){
			return;
		}else{
			ONGROUND=true;
		}
		if(ONGROUND && !RUNNING && (LEFT || RIGHT)){
			walk();
		}
		else if(ONGROUND && RUNNING && (LEFT || RIGHT)){
			run();
		}
		else if(ONGROUND && !(LEFT || RIGHT)){
			stand();
		}
	}
	public static void stand(){
		if(DIRECTION){
			Player.setCurrentTileIndex(0);
		}
		else if(!DIRECTION){
			Player.setCurrentTileIndex(6);
		}
	}
	public static void run(){
		//Player.setCurrentTileIndex(6);
		if(DIRECTION){
			Player.animate(new long[]{200,200},2,3,true);
		}
		else if(!DIRECTION){
			Player.animate(new long[]{200,200},8,9,true);
		}
	}
	public static void walk(){
		if(DIRECTION){
			Player.animate(new long[]{200,200},0,1,true);
		}
		else if(!DIRECTION){
			Player.animate(new long[]{200,200},6,7,true);
		}
	}
	public void Jump(boolean state){
		if(!state)
			JUMPING=false;
		if(!JUMPING && state && ONGROUND && feetcontacts>=1){
			feetcontacts=0;
			ONGROUND=false;
			JUMPTIMER = 0;
			currentJUMPFORCE = JUMPFORCE;
			Player.stopAnimation();
			JumpSound.play();
			JUMPING=true;
			if(DIRECTION)
				Player.setCurrentTileIndex(5);
			else
				Player.setCurrentTileIndex(11);
		}
	}




	public void Left(boolean state){
		if(!state){
			LEFT = false;
			if(ONGROUND)
				Player.stopAnimation(6);
		}
		if(state){
			DIRECTION=false;
			LEFT = true;
		}
		if(state && !JUMPING && ONGROUND){
			Player.setCurrentTileIndex(7);
			Player.animate(new long[]{200,200},6,7,true);
		}
	}
	public void Right(boolean state){
		if(!state){
			RIGHT = false;
			if(ONGROUND)
				Player.stopAnimation(0);
		}
		if(state){
			DIRECTION=true;
			RIGHT = true;
		}
		if(state && !JUMPING && ONGROUND){
			Player.setCurrentTileIndex(1);
			Player.animate(new long[]{200,200},0,1,true);
		}
	}
	//---------------------------------------------------------------------------------------------------------------
	//CONSTRUCTOR
	public Mario(Game parent,Engine engine){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.PlayerAtlas = new BitmapTextureAtlas(parent.getTextureManager(),512,512,TextureOptions.DEFAULT);
		this.PlayerTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.PlayerAtlas,parent,"mario.png",0,0,6,2);
		this.PlayerAtlas.load();
		//-------------------------------
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.JumpSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(),parent,"jump.ogg");
		}catch(Exception e){
			//
		}
		//-------------------------------
		this.loader = new PhysicsEditorLoader();
	}
}

	/*-------------------------------
	private static short STATE;
	private static final short STANDING=1;
	private static final short JUMPING=2;
	private static final short WALKING=3;
	private static final short RUNNING=4;
	//-------------------------------
	private static short ACTION;
	private static final short LEFT=1;
	private static final short RIGHT=2;
	private static final short JUMPING=3;
	private static final short RUNNING=4;
	//-------------------------------
	*/
/*
 * PLAYER CLASS:
 * 	-CONSTRUCTOR(int x,int y,tilesheet,world?)
 * 	-STATES
 * 		-STANDING
 * 		-JUMPING
 * 		-FALLING
 * 		-ALIVE
 * 		-WALKING
 * 		-RUNNING
 * 		-SKIDDING
 * 	-CONSTANTS
 * 		-JUMPFORCE
 * 		-WALKFORCE
 * 		-RUNFORCE
 * 		-MAXAIRSPEED
 * 		-MAXGROUNDSPEED
 *  -UPDATES
 * 		if(COLLISION)
 *			AFFECT STATE
 * 		if(STATE)
 *			ANIMATE
 */
		/*
		final short CATEGORYBIT_WALL = 1;
		final short CATEGORYBIT_PLAYER = 2;
		final short MASKBITS_WALL = CATEGORYBIT_PLAYER; 
		final short MASKBITS_PLAYER = CATEGORYBIT_PLAYER + CATEGORYBIT_WALL; 
		final FixtureDef PLAYER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1, 0.0f, 0.45f, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);
		this.PlayerBody = PhysicsFactory.createBoxBody(parent.World,this.Player,BodyType.DynamicBody,PLAYER_FIXTURE_DEF);
		*/
