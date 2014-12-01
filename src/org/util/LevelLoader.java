package org.util;

import org.util.constants.LoaderConstants;
import org.util.constants.Collisions;
import org.games.Game;
import org.games.objects.*;
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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.engine.Engine;
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
public class LevelLoader{
	private static VertexBufferObjectManager vbo;
	private static BitmapTextureAtlas BlockAtlas;
	/*
	 *
	 * 16x5 SMB3 BLOCKS
	 * 	--------------------------
	 * 	Bricks
	 * 	--------------------------
	 * 	Grey   4 wide,1tall,0,0
	 * 	ICE    4 wide,1tall,0,32
	 * 	Shiny  4 wide,1tall,0,64
	 *  Brick1 4 wide,1tall,0,96
	 * 	Brick2 4 wide,1tall,0,128
	 * 	---------------------------
	 * 	Items
	 * 	---------------------------
	 * 	Coins   4 wide, 1 tall ,256,0
	 * 	SwitchR 4 wide, 1 tall, 256,32
	 * 	SwitchB 4 wide, 1 tall, 256,64
	 * 	next row 4 ice? blocks
	 * 	mush    1 wide, 1tall,256,128
	 * 	flower  1 wide, 1tall,288,128
	 * 	star    1 wide, 1tall,320,128
	 * 	oneup   1 wide, 1tall,352,128
	 *
	 * 	---------------------------
	 * 	Platforms
	 * 	---------------------------
	 *	finish later
	 *
	 * 	---------------------------
	 * 	Blocks
	 * 	---------------------------
	 * 	Note    4 wide, 1tall,128,0
	 * 	item    4 wide, 1tall,128,32
	 * 	blank   1 wide, 1tall,128,64
	 * 	falling 1 wide, 1tall,160,64
	 * 	stone1  1 wide, 1tall,192,64
	 * 	stone2  1 wide, 1tall,224,64
	 * 	redWood 1 wide, 1tall,128,96
	 * 	wood    1 wide, 1tall,160,96
	 * 	ring    1 wide, 1tall,192,96
	 * 	poles   1 wide, 1tall,224,96
	 * 	fan     1 wide, 1tall,128,128
	 * 	lineR   1 wide, 1tall,160,128
	 * 	lineL   1 wide, 1tall,192,128
	 * 	poles2  1 wide, 1tall,224,128
	 *
	 */
	private static TiledTextureRegion NoteTexture;
	private static TiledTextureRegion BlockTexture;
	private static TiledTextureRegion BlankTexture;
	private static TiledTextureRegion StoneTexture;
	private static TiledTextureRegion CoinTexture;
	private static Sound CoinSound;

	private static TMXTiledMap map;
	private static int ROWS;
	private static int COLS;
	private final static int TOPLAYER = 2; //right now, object - background - foreground

	private static TMXTile[][] TILES;
	private static ArrayList<TMXLayer> LAYERS;

	private static int width;
	private static int height;
	//(...,float Mass,float Elasticity,float friction...)
	//----------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------
	public LevelLoader(final Game parent,final Engine engine,String file){
		this.vbo = parent.getVertexBufferObjectManager();
		this.loadResources(parent,engine);
		if(file=="test2"){
			this.width = parent.CAMERA_WIDTH;
			this.height = parent.CAMERA_HEIGHT;
			this.addBounds(parent);
			parent.camera.setBounds(0, 0, this.width,this.height);
			parent.camera.setBoundsEnabled(true);
			this.COLS = this.width / 32;
			this.ROWS = this.height / 32;
			new Brick(this.width / 2,this.height / 2,parent);
			new Brick(this.width / 2+32,this.height / 2,parent);
			new Brick(this.width / 2-32,this.height / 2,parent);
			new Coin(this.width/2,this.height / 2-32,parent,engine);
			/*
			//-------------
			new Brick2(this.width/4,this.height / 4,parent);
			new IceBrick(this.width/4+32,this.height / 4,parent);
			new ShinyBrick(this.width/4-32,this.height / 4,parent);
			new GreyBrick(this.width/4+64,this.height / 4,parent);
			//-------------
			new Stone(this.width/2+32,this.height / 2+32,parent);
			new Note(this.width/2+64,this.height / 2+32,parent);
			new ItemBlock(this.width/2+32,this.height / 2+64,parent);
			new WoodBlock(this.width/2+64,this.height / 2+64,parent);
			new BlankBlock(this.width/2-32,this.height / 2+64,parent);
			*/
		}
		else if(file=="test"){
			this.width = parent.CAMERA_WIDTH;
			this.height = parent.CAMERA_HEIGHT;
			this.addBounds(parent);
			parent.camera.setBounds(0, 0, this.width,this.height);
			parent.camera.setBoundsEnabled(true);
			this.COLS = this.width / 32;
			this.ROWS = this.height / 32;
			//addBlock(this.width / 2,0,parent,"note");
			//addBlock(this.width / 2,0,32,32,parent);
			new Note(this.width / 2, 96,parent);
			new Coin(this.width / 2, 64,parent,engine);
			new ItemBlock(this.width / 2 + 32,96,parent);
			new ShinyBrick(this.width / 2 - 32,96,parent);
			//new Platform(this.width / 2, 96, 32,parent);
			/*
			new Platform(this.width / 2, 128, 32,parent);
			new Platform(this.width / 2+32, 128, 32,parent);
			new Platform(this.width / 2+64, 128, 32,parent);
			new Platform(this.width / 2-32, 128, 32,parent);
			new Platform(this.width / 2-64, 128, 32,parent);
			*/


		}else{
			try{
				TMXLoader tmxLoader = new TMXLoader( 
						parent.getAssets(), engine.getTextureManager(), 
						TextureOptions.NEAREST, this.vbo,
						new ITMXTilePropertiesListener() {
							@Override
							public void onTMXTileWithPropertiesCreated(
									final TMXTiledMap map,
									final TMXLayer layer,
									final TMXTile tile,
									final TMXProperties<TMXTileProperty> props) { 
										int x = tile.getTileX();
										int y = tile.getTileY();
										if(props.containsTMXProperty("item", "coin")) {
											new Coin(x,y,parent,engine);
										}
										else if(props.containsTMXProperty("type", "coin")) {
											new Coin(x,y,parent,engine);
										}
										else if(props.containsTMXProperty("block", "itemblock")) {
											new ItemBlock(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "wood")) {
											new WoodBlock(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "brick2")) {
											new Brick(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "brick1")) {
											new Brick(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "ice")) {
											new IceBrick(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "shiny")) {
											new ShinyBrick(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "grey")) {
											new GreyBrick(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "blank")) {
											new BlankBlock(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "note")) {
											new Note(x,y,parent);
										}
										else if(props.containsTMXProperty("block", "stone")) { 
											//this is gonna be the floor
											//new Stone(x,y,parent);
										}
										else{ }
									}
				});
				this.map = tmxLoader.loadFromAsset("tmx/"+file);
				//this.map = tmxLoader.loadFromAsset("tmx/blocks.tmx");
			}catch (final TMXLoadException e){
				Debug.e(e);
			}
			//---------------------------------------
			this.LAYERS = this.map.getTMXLayers();
			this.TILES =  this.LAYERS.get(TOPLAYER).getTMXTiles();
			this.ROWS = this.map.getTileRows();
			this.COLS = this.map.getTileColumns();
			//---------------------------------------
			for(TMXLayer layer : this.LAYERS){
				parent.scene.getChildByIndex(0).attachChild(layer);
			}
			//---------------------------------------
			final TMXLayer ll = this.map.getTMXLayers().get(0);
			this.width = ll.getWidth();
			this.height = ll.getHeight();
			addBounds(ll,parent);
			//parent.camera.setBounds(0, 0,this.width,this.height);
			//parent.camera.setBoundsEnabled(true);
			//---------------------------------------
			this.computeFloors("block","stone",parent);
			//this.computeFloors("type","ground",parent);
		}
	}

	public void addBounds(final Game parent){
		this.addWall(0,0,this.width,0,"ground",parent);
		this.addWall(0,this.height-64,this.width,0,"ground",parent);
		this.addWall(0,0,0,this.height,"ground",parent);
		this.addWall(this.width,0,0,this.height,"ground",parent);
	}
	public void addBounds(TMXLayer layer,final Game parent){
		this.addWall(0,0,layer.getWidth(),0,"ground",parent);
		this.addWall(0,layer.getHeight(),layer.getWidth(),0,"ground",parent);
		this.addWall(0,0,0,layer.getHeight(),"ground",parent);
		this.addWall(layer.getWidth(),0,0,layer.getHeight(),"ground",parent);
	}

	public void addCoin(final Game parent,TMXTile tile){
		final AnimatedSprite coin = new AnimatedSprite(
				tile.getTileX(), tile.getTileY(), 
				this.CoinTexture, this.vbo){
			@Override
			protected void onManagedUpdate(final float seconds){
				if(parent.mario.Player.collidesWith(this)){
					setVisible(false);
					//play sound
					CoinSound.play();
					parent.SCORE++;
					parent.score.setText("Score: "+parent.SCORE);
					setIgnoreUpdate(true);
				}
				super.onManagedUpdate(seconds);
			}
		};
		coin.setVisible(true);
		coin.setCullingEnabled(true);
		coin.animate(100);
		parent.scene.getChildByIndex(1).attachChild(coin);
	}
	//finds tiles with properties(type,value)
	//attempts to group horizontal blocks into big floor 
	public void computeFloors(String type,String value,final Game parent){
		boolean searching = false;
		int startX;
		int startY;
		int offset = 0;
		for(int j=0;j<ROWS;j++){
			for(int i=0;i<COLS;i++){
				if (this.TILES[j][i].getTMXTileProperties(this.map).containsTMXProperty(type,value)){
					searching = true;
					startX = i;
					startY = j;
					offset = 0;
					while(searching){
						if((i+offset) == (this.map.getTileColumns() - 1))
								searching = false;
						if (this.TILES[j][i+offset].getTMXTileProperties(this.map).containsTMXProperty(type,value)){
							offset++;
						}
						else{
							searching = false;
						}
					}
					if(offset==0)
						offset++;
					this.addGround(i*32,j*32,offset*32,32,parent);
					//this.addGround(i*32,j*32,offset*32,2,parent);
					if(!((i+offset) == (this.map.getTileColumns() - 1)))
						i = i+offset;
				}
			}
		}
	}
	//Make a physics rectangle/box with userdata "ground"
	public void addGround(int x,int y,int w,int h,final Game parent){
		final Rectangle box = new Rectangle( x,y,w,h,this.vbo);
		//box.setColor(0.4f,0.0f,1.0f,0.4f);
		//box.setVisible(true);
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
			//PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.GROUND_FIXTURE_DEF);
		floor.setUserData("ground");
		parent.scene.getChildByIndex(1).attachChild(box);
	}
	//Make a physics rectangle/box with userdata "wall"
	public void addWall(int x,int y,int w,int h,String type,final Game parent){
		final Rectangle box = new Rectangle( x,y,w,h,this.vbo);
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData(type);
		parent.scene.getChildByIndex(1).attachChild(box);
	}
	public void addBlock(int x,int y,final Game parent,String type){
		AnimatedSprite box;
		if (type.equals("block")){
			box = new AnimatedSprite(x,y,this.BlockTexture, this.vbo);
			box.animate(100);
		}else if (type.equals("blank")){
			box = new AnimatedSprite(x,y,this.BlankTexture, this.vbo);
		}else if (type.equals("stone")){
			box = new AnimatedSprite(x,y,this.NoteTexture, this.vbo);
		}else if (type.equals("note")){
			box = new AnimatedSprite(x,y,this.StoneTexture, this.vbo);
			box.animate(100);
		}
		else{
			box = new AnimatedSprite(x,y,this.CoinTexture, this.vbo);
			box.animate(100);
		}
		final Body floor = 
			PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData("wall");
		parent.scene.getChildByIndex(1).attachChild(box);
	}
	public void addBlock(int x,int y,int w,int h,final Game parent){
		final Rectangle box = new Rectangle( x,y,w,h,this.vbo);
		box.setVisible(false);
		final Body floor = 
			PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData("wall");
		parent.scene.getChildByIndex(1).attachChild(box);
	}
	//all of these get userdata set as "wall"
	public void addBlock(TMXTile tile,String type,final Game parent){
		final Rectangle box = new Rectangle(
				tile.getTileX(), 
				tile.getTileY(), 
				tile.getTileWidth(),
				tile.getTileHeight(), 
				this.vbo);
		//box.setColor(1.0f,1.0f,0.0f,1.0f);
		//box.setVisible(true);
		box.setVisible(false);
		final Body floor;
		if(type=="block"){
			floor = PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.BOX_FIXTURE_DEF);
		} else{
			floor = PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		} 

		floor.setUserData("wall");
		parent.scene.getChildByIndex(1).attachChild(box);
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public void loadResources(final Game parent,final Engine engine){
		//--------------------------------------------------------------//
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.BlockAtlas = new BitmapTextureAtlas(parent.getTextureManager(),512,256,TextureOptions.DEFAULT);
		//--------------------------------------------------------------//
		this.CoinTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"coins.png",0,0,5,1);
		/*
		this.CoinTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"blocks.png",256,0,4,1);
		this.NoteTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"blocks.png",128,0,4,1);
		this.BlockTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"blocks.png",128,32,4,1);
		//--------------------------------------------------------------//
		this.StoneTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"blocks.png",192,64,1,1);
		this.BlankTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.BlockAtlas,parent,"blocks.png",128,64,1,1);
				*/
		this.BlockAtlas.load();
		//--------------------------------------------------------------//
		SoundFactory.setAssetBasePath("mfx/");
		try{
			this.CoinSound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(),parent,"coin.ogg");
		}catch(Exception e){
			//
		}
	}
}
