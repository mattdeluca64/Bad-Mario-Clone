package org.games;
import org.util.math.DFS;
import org.util.LevelLoader;
import org.games.entity.Player;
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
/*
 * *****************************************************************************************************8
 * Create Mario Class
 * *****************************************************************************************************8
 * Create Game Activity
 * 	-onCreateScene();
 * 		-scene
 * 		-world
 * 		-tmx tiles && body placement
 * 		-addPlayer
 * 	-onLoadResources();
 * 		-textures/sounds
 * 	-updateHandlers();
 * 		-getPlayerFacingDirection
 * 		-apply current physics to all players/objects/etc
 * 	-collisionHandlers();
 * 		-setPlayerState[AIR,GROUND,WATER,DIE,JUMPING,WALKING,RUNNING,STANDING]
 * 		-reactionPhysics()? -- like if its a trampoline tile, but with the wall def..? dumb?
 * ******************************************************************************************************/
public class Mario extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;
	//settings - values
	private static float GRAVITY = 25.0f;
	//PlayerFlags
	private static boolean DIRECTION=true;//true right, false left
	private static boolean SKIDDING=false;
	private static boolean RUNNING=false;
	private static boolean JUMPING=false;
	private static boolean BUTTON4=false;
	private static ArrayList<TMXLayer> LAYERS;
	private static TMXTile[][] TILES;
	//-----------------------------------------------------------------------------------------------------------
	//Wall Collides with Player
	//...PLAYER_DEF defined in xml/mario.xml, but not sure exactly what its definition is!
	public static final short CATEGORYBIT_WALL = 1;
	public static final short CATEGORYBIT_PLAYER = 2;
	public static final short MASKBITS_WALL = CATEGORYBIT_PLAYER; 
	
	//(...,float Mass,float Elasticity,float friction...)
	public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(
			1, 0.0f, 0.45f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
	//-----------------------------------------------------------------------------------------------------------
	private String message;
	public Scene scene;
	public BoundCamera camera;
	//public Camera camera;
	private HUD hud;
	public PhysicsWorld World;

	private static BitmapTextureAtlas ItemAtlas;
	private static TiledTextureRegion CoinTexture;

	private static Player mario;
	private static Font font;
	private static Text score;
	private static int SCORE;
	private static Sound CoinSound;

	public TMXTiledMap map;
	//***********************************************************************************************************
	//Create Resources
	//***********************************************************************************************************
	@Override
	public void onCreateResources() {
		//-------------------------------------------------------------------------------------------------------
		this.font = FontFactory.create(
				this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL), 12);
		this.font.load();
		this.score = new Text(0, 0, this.font, "Score:0123456789",this.getVertexBufferObjectManager());
		//-------------------------------------------------------------------------------------------------------
		this.mario = new Player(this,this.mEngine);
		//-------------------------------------------------------------------------------------------------------
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.ItemAtlas = new BitmapTextureAtlas(this.getTextureManager(),256,256,TextureOptions.DEFAULT);
		this.CoinTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.ItemAtlas,this,"coins.png",0,0,5,1);
		this.ItemAtlas.load();
		//-------------------------------------------------------------------------------------------------------
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.CoinSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(),this,"coin.ogg");
		}catch(Exception e){
		}
	}
	//***********************************************************************************************************
	//SCENE
	//***********************************************************************************************************
	@Override
	public Scene onCreateScene() {
		this.initScene();
		new LevelLoader(this,this.mEngine);
		//this.loadLevel();
		this.computeFloors("ground");
		final TMXLayer ll = this.map.getTMXLayers().get(0);
		this.camera.setBounds(0, 0, ll.getWidth(),ll.getHeight());
		this.camera.setBoundsEnabled(true);
		this.mario.createPlayer(32*15,32*10,this);
		this.camera.setChaseEntity(this.mario.Player);
		this.scene.getChildByIndex(1).attachChild(this.mario.Player);
		this.World.registerPhysicsConnector(new PhysicsConnector(this.mario.Player, this.mario.PlayerBody, true, true));
		this.setupHUD();
		return this.scene;
	}
	//***********************************************************************************************************
	//need these for something im importing...
	@Override
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) { }
	@Override
	public void onAccelerationChanged(final AccelerationData data) {
	}
	
	//***********************************************************************************************************
	//Pause/Resume
	//***********************************************************************************************************
	@Override
	public void onPauseGame() {
		super.onPauseGame();
		//this.disableAccelerationSensor();
	}
	@Override
	public void onResumeGame() {
		super.onResumeGame();
		//this.enableAccelerationSensor(this);
	}

	//**********************************************************************************************************************
	//SCENE TOUCH
	//**********************************************************************************************************************
	@Override
	public boolean onSceneTouchEvent(final Scene scene, final TouchEvent touch) {
		return false;
	}
	//********************************************************************************************************************
	//OPTIONS
	//********************************************************************************************************************
	@Override
	public EngineOptions onCreateEngineOptions() {
		//Toast.makeText(this, "Collect Canada Coins!.", Toast.LENGTH_LONG).show();
		this.camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), 
				this.camera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}
	//*************************************************************************************************************************
	//My Methods
	//*************************************************************************************************************************
	public void popup(String message){
		this.message = message;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(
						Mario.this, 
						Mario.this.message,
						Toast.LENGTH_SHORT).show();
						//Toast.LENGTH_LONG).show();
			}
		});
	}
	//***********************************************************************************************************************
	private ContactListener createContactListener(){
		ContactListener contactListener = new ContactListener(){
			@Override
			public void beginContact(Contact contact){
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				if (x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals("ground")){
					mario.hitGround();
				}
				if (x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals("wall")){
					mario.hitGround();
				}
			}
			public void endContact(Contact contact){
			}
			public void preSolve(Contact contact,Manifold impulse){
				//
			}
			public void postSolve(Contact contact,ContactImpulse impulse){
			}
		};
		return contactListener;
	}
	//************************************************************************************************************************
	public void addBounds(TMXLayer layer){
		this.addWall(0,0,layer.getWidth(),0);
		this.addWall(0,layer.getHeight(),layer.getWidth(),0);
		this.addWall(0,0,0,layer.getHeight());
		this.addWall(layer.getWidth(),0,0,layer.getHeight());
	}
	//************************************************************************************************************************
	public void addCoin(TMXTile tile){
		final AnimatedSprite coin = new AnimatedSprite(tile.getTileX(), tile.getTileY(), 
				this.CoinTexture, this.getVertexBufferObjectManager()){
			@Override
			protected void onManagedUpdate(final float seconds){
				if(mario.Player.collidesWith(this)){
					setVisible(false);
					//play sound
					CoinSound.play();
					SCORE++;
					score.setText("Score: "+SCORE);
					setIgnoreUpdate(true);
				}
				super.onManagedUpdate(seconds);
			}
		};
		coin.setVisible(true);
		coin.setCullingEnabled(true);
		coin.animate(100);
		this.scene.getChildByIndex(1).attachChild(coin);
	}
	//************************************************************************************************************************
	//this finds all tiles with type==ground
	//and loops through and finds horizontal groups, and makes it one big physics rectangle
	public void computeFloors(String type){
		this.LAYERS = this.map.getTMXLayers();
		this.TILES =  this.LAYERS.get(2).getTMXTiles();
		boolean searching = false;
		int startX;
		int startY;
		int offset = 0;
		for(int j=0;j<this.map.getTileRows();j++){
			for(int i=0;i<this.map.getTileColumns();i++){
				if (this.TILES[j][i].getTMXTileProperties(this.map).containsTMXProperty("type", type)){
					searching = true;
					startX = i;
					startY = j;
					offset = 0;
					while(searching){
						if((i+offset) == (this.map.getTileColumns() - 1))
								searching = false;
						if (this.TILES[j][i+offset].getTMXTileProperties(this.map).containsTMXProperty("type", type)){
							offset++;
						}
						else{
							searching = false;
						}
					}
					if(offset==0)
						offset++;
					if(type=="ground")
						this.addGround(i*32,j*32,offset*32,32);
					else if(type=="box")
						this.addBlock(i*32,j*32,offset*32,32);
					if(!((i+offset) == (this.map.getTileColumns() - 1)))
						i = i+offset;
				}
			}
		}
	}
	//************************************************************************************************************************
	//Make a physics rectangle/box with userdata "ground"
	public void addGround(int x,int y,int w,int h){
		final Rectangle box = new Rectangle( x,y,w,h,this.getVertexBufferObjectManager());
		//box.setColor(1.0f,0.0f,1.0f,1.0f);
		//box.setVisible(true);
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(Mario.this.World, box, BodyType.StaticBody, WALL_FIXTURE_DEF);
		floor.setUserData("ground");
		this.scene.getChildByIndex(1).attachChild(box);
	}
	//************************************************************************************************************************
	//Make a physics rectangle/box with userdata "wall"
	public void addWall(int x,int y,int w,int h){
		final Rectangle box = new Rectangle( x,y,w,h,this.getVertexBufferObjectManager());
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(Mario.this.World, box, BodyType.StaticBody, WALL_FIXTURE_DEF);
		floor.setUserData("wall");
		this.scene.getChildByIndex(1).attachChild(box);
	}
	//********************************************************************************************************************
	public void addBlock(int x,int y,int w,int h){
		final Rectangle box = new Rectangle( x,y,w,h,this.getVertexBufferObjectManager());
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(Mario.this.World, box, BodyType.StaticBody, WALL_FIXTURE_DEF);
		floor.setUserData("wall");
		this.scene.getChildByIndex(1).attachChild(box);
	}
	//********************************************************************************************************************
	//all of these get userdata set as "wall"
	public void addBlock(TMXTile tile){
		final Rectangle box = new Rectangle(
				tile.getTileX(), 
				tile.getTileY(), 
				tile.getTileWidth(),
				tile.getTileHeight(), 
				this.getVertexBufferObjectManager());
		//box.setColor(1.0f,1.0f,0.0f,1.0f);
		//box.setVisible(true);
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(Mario.this.World, box, BodyType.StaticBody, WALL_FIXTURE_DEF);

		floor.setUserData("wall");
		this.scene.getChildByIndex(1).attachChild(box);
	}
	//************************************************************************************************************************
	//moved all of this down here, hasn't changed much
	public void initScene(){
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.hud = new HUD();
		//2 layers,bg and fg
		this.scene.attachChild(new Entity());
		this.scene.attachChild(new Entity());
		
		this.scene.setBackground(new Background(0, 0, 0));
		this.scene.setOnSceneTouchListener(this);
		this.World = new PhysicsWorld(new Vector2(0, GRAVITY), false);
		this.World.setContactListener(createContactListener());
		this.scene.registerUpdateHandler(this.World);
		DebugRenderer debug = new DebugRenderer(this.World, this.getVertexBufferObjectManager());
		this.scene.attachChild(debug);
	}
	//************************************************************************************************************************
	//5 buttons
	//button1 sends mario.Jump()
	//buttons 2/3 left/right sends mario.Left() Right() respectively
	//button 4 constantly adds upward force to mario...doesn't call/go thru the Player class
	//button 5 just prints "hi" in a notification box
	//************************************************************************************************************************
	public void setupHUD(){
		final Rectangle Button1 = new Rectangle(CAMERA_WIDTH - 60,CAMERA_HEIGHT - 60,60,60,this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent touch, float x, float y){
				if(touch.isActionDown()){
					mario.Jump(true);
				}
				if (touch.isActionMove()){
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()){
						mario.Jump(false);
						return false;
					}
				}
				if(touch.isActionUp()){
					mario.Jump(false);
				}
				return true;
			}
		};
	//*********************************************************************************
		final Rectangle Button2 = new Rectangle(0,CAMERA_HEIGHT - 60,60,60,this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent touch, float x, float y){
				if(touch.isActionUp()){
					mario.Left(false);
				}
				if (touch.isActionMove()){
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()){
						mario.Left(false);
						return false;
					}
				}
				if(touch.isActionDown()){
					mario.Left(true);
				}
				return true;
			}
		};
	//*********************************************************************************
		final Rectangle Button3 = new Rectangle( 60,CAMERA_HEIGHT - 60,60,60,this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent touch, float x, float y){
				if(touch.isActionUp()){
					mario.Right(false);
				}
				if (touch.isActionMove()){
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()){
						mario.Right(false);
						return false;
					}
				}
				if(touch.isActionDown() ){
					mario.Right(true);
				}
				return true;
			}
		};
	//*********************************************************************************
		final Rectangle Button4 = new Rectangle(CAMERA_WIDTH-60,0,60,60,this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent touch, float x, float y){
				if(touch.isActionUp()){
					BUTTON4=false;
				}
				if (touch.isActionMove()){
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()){
						BUTTON4=false;
						return false;
					}
				}
				if(touch.isActionDown()){
					BUTTON4=true;
				}
				return true;
			}
			@Override
			protected void onManagedUpdate(float seconds){
				if(BUTTON4){
					mario.PlayerBody.applyForce(new Vector2(
								0,-Mario.this.World.getGravity().y*5),mario.PlayerBody.getWorldCenter());
				}
				super.onManagedUpdate(seconds);
			}
		};
	//*********************************************************************************
		final Rectangle Button5 = new Rectangle(CAMERA_WIDTH-120,0,60,60,this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent touch, float x, float y){
				if(touch.isActionUp()){
					//score.setText("UP");
				}
				if (touch.isActionMove()){
					if(x < 0 || x > getWidth() || y < 0 || y > getHeight()){
						//
						return false;
					}
				}
				if(touch.isActionDown()){
					Mario.this.popup("hi!");
				}
				return true;
			}
		};
	//*********************************************************************************
	//set color, register touch area, bind to hud, add hud to scene
		Button1.setColor(1.0f, 0.5f, 0.5f, 0.85f);
		Button2.setColor(0.5f, 1.0f, 0.5f, 0.85f);
		Button3.setColor(0.6f, 0.2f, 0.5f, 0.85f);
		Button4.setColor(0.8f, 0.2f, 0.8f, 0.85f);
		Button4.setColor(0.8f, 0.2f, 0.8f, 0.85f);
		Button5.setColor(1.0f, 0.0f, 0.0f, 1.00f);
		this.hud.registerTouchArea(Button1);
		this.hud.registerTouchArea(Button2);
		this.hud.registerTouchArea(Button3);
		this.hud.registerTouchArea(Button4);
		this.hud.registerTouchArea(Button5);
		this.hud.setTouchAreaBindingOnActionDownEnabled(true);
		this.hud.setTouchAreaBindingOnActionMoveEnabled(true);
		this.hud.attachChild(Button1);
		this.hud.attachChild(Button2);
		this.hud.attachChild(Button3);
		this.hud.attachChild(Button4);
		this.hud.attachChild(Button5);
		//add the score to the hud
		this.score.setText("Score: "+SCORE);
		this.score.setColor(Color.RED);
		this.hud.attachChild(this.score);
		this.camera.setHUD(hud);
	}
}
/*
 *
 * Various cut/paste from andengine examples / forum posts / my previous code used as examples/test
 */

/*
	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (this.stopManagedUpdate)
			return;
		physicsWorld.onUpdate(pSecondsElapsed);
		Entity e = new Entity();

		int tilemapheight = this.mTMXTiledMap.getTileHeight() * this.mTMXTiledMap.getTileRows() 
		int mapW = this.mTMXTiledMap.getTileWidth() * this.mTMXTiledMap.getTileColumns();

		float y = (tilemapheight - (GameConstants.CAMERA_HEIGHT / 2));

		if (player.getX() > (GameConstants.CAMERA_WIDTH / 2) && player.getX() < (mapW - GameConstants.CAMERA_WIDTH / 2))
			e.setPosition(player.getX(), y);
		else if (player.getX() < (GameConstants.CAMERA_WIDTH / 2))
			e.setPosition((GameConstants.CAMERA_WIDTH / 2), y);
		else if (player.getX() > (mapW - GameConstants.CAMERA_WIDTH / 2))
			e.setPosition((mapW - GameConstants.CAMERA_WIDTH / 2), y);

		camera.setChaseEntity(e);

		final MoveModifier modifier = new MoveModifier(30, e.getX(), player.getX(), e.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				camera.setChaseEntity(null);
			}
		};
		e.registerEntityModifier(modifier);
	}
}
*/
		//***********************************************************************************************************
		//Adoption
		//***********************************************************************************************************
		/*
		this.scene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float delta) {
				float V=mario.PlayerBody.getLinearVelocity().x;
				if (V >= 11.0f)
					Mario.this.popup(""+V+" velocity?");
			}
		});
		//********************************************************************************************
		scene.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
			}
		}));
		*/
		//********************************************************************************************
	//***********************************************************************************************************
		//if (tile.getTMXTileProperties(this.map).containsTMXProperty("type", "rock")){
	//ACCELOMETER
	//***********************************************************************************************************
		//private static float GRAVITYFACTOR = 15f;
		//this.World = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH * this.GRAVITYFACTOR), false);
	//***********************************************************************************************************
		//DFS dfs = new DFS(this.RILES,this.map.getTileRows(),this.map.getTileColumns());
		//int rocks = dfs.getConnected();
		//this.popup("there are this many rock islands: "+rocks);
	//***********************************************************************************************************
	//getTMXLayers() /////\\\\\\
	//getTMXTiles()  |||||||||||
	//               \\\\\//////
	//***********************************************************************************************************
					//TextureOptions.DEFAULT,
					//TextureOptions.NEAREST,
					//TextureOptions.BILINEAR,
					//TextureOptions.NEAREST_PREMULTIPLYALPHA,
					//TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					//TextureOptions.REPEATING_NEAREST,
					//TextureOptions.REPEATING_BILINEAR,
					//TextureOptions.REPEATING_NEAREST_PREMULTIPLYALPHA,
					//TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA,
		//this.text.setColor(1.0f,1.0f,1.0f,1.0f); 
		////this.text.setPosition(x,y); 
		////this.text.setHorizontalAlign(???); 
		////this.text.setScale(????);
