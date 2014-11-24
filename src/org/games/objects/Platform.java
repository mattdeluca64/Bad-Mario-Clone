package org.games.objects;
import org.util.constants.Collisions;
import org.games.Game;


import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;

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

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.primitive.Rectangle;

public class Platform{
	private static BitmapTextureAtlas PlayerAtlas;
	private static TiledTextureRegion PlatformTexture;
	public static AnimatedSprite box;
	public static AnimatedSprite box2;
	public static AnimatedSprite box3;
		//Player = new AnimatedSprite(x,y,this.PlayerTexture,parent.getVertexBufferObjectManager()){
	//public static Rectangle box;
	//
	public Platform(int x,int y,int w,final Game parent){
		//------------------------------------------------------------------
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.PlayerAtlas = new BitmapTextureAtlas(parent.getTextureManager(),512,256,TextureOptions.DEFAULT);
		this.PlatformTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.PlayerAtlas,parent,"blocks.png",0,0,16,5);
		this.PlayerAtlas.load();
		//------------------------------------------------------------------
		//this.box = new Rectangle( x,y,w,32,parent.getVertexBufferObjectManager());
		//box.setVisible(false);
		this.box = new AnimatedSprite(x,y,this.PlatformTexture,parent.getVertexBufferObjectManager());
		this.box.setCurrentTileIndex(12);
		this.box2 = new AnimatedSprite(x+32,y,this.PlatformTexture,parent.getVertexBufferObjectManager());
		this.box2.setCurrentTileIndex(13);
		this.box3 = new AnimatedSprite(x+64,y,this.PlatformTexture,parent.getVertexBufferObjectManager());
		this.box3.setCurrentTileIndex(15);
		Body floor = PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData("platform");
		floor = PhysicsFactory.createBoxBody(parent.World, box2, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData("platform");
		floor = PhysicsFactory.createBoxBody(parent.World, box3, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		floor.setUserData("platform");
		parent.scene.getChildByIndex(1).attachChild(box);
	}
}
