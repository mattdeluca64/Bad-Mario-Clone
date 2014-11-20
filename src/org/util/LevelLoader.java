package org.util;
import org.games.Mario;
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
	public LevelLoader(final Mario parent,Engine engine){
		try{
			TMXLoader tmxLoader = new TMXLoader( 
					parent.getAssets(),
					engine.getTextureManager(),
					TextureOptions.NEAREST,
					parent.getVertexBufferObjectManager(),
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap map,
								final TMXLayer layer,
								final TMXTile tile,
								final TMXProperties<TMXTileProperty> props) { 
									if(props.containsTMXProperty("type", "background")) { }
									else if(props.containsTMXProperty("type", "ground")) { }
									else if(props.containsTMXProperty("type", "window")) { }
									else if(props.containsTMXProperty("type", "pipe")) { 
										parent.addBlock(tile);
									}
									else if(props.containsTMXProperty("type", "shrub")) { }
									else if(props.containsTMXProperty("type", "coin")) {
										parent.addCoin(tile);
									}
									else if(props.containsTMXProperty("type", "mushroom")) { }
									else if(props.containsTMXProperty("type", "star")) { }
									else if(props.containsTMXProperty("type", "flower")) { }
									else if(props.containsTMXProperty("type", "platform")) { }
									else if(props.containsTMXProperty("type", "cloud")) { }
									else if(props.containsTMXProperty("type", "death")) { }
									else if(props.containsTMXProperty("type", "block")) { 
										parent.addBlock(tile);
									}
									else{ }
								}
			});
			parent.map = tmxLoader.loadFromAsset("tmx/blocks.tmx");
			//this.map = tmxLoader.loadFromAsset("tmx/rocks.tmx");
			//this.map = tmxLoader.loadFromAsset("tmx/cave.tmx");
			//this.map = tmxLoader.loadFromAsset("tmx/untitled.tmx");
		}catch (final TMXLoadException e){
			Debug.e(e);
		}
		for(TMXLayer layer : parent.map.getTMXLayers()){
			parent.scene.getChildByIndex(0).attachChild(layer);
		}
		final TMXLayer ll = parent.map.getTMXLayers().get(0);
		parent.addBounds(ll);

	}
}
