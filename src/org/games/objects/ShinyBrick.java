package org.games.objects;
import org.util.constants.Collisions;
import org.games.Game;
import org.util.ResourcesManager;


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

public class ShinyBrick extends Block{
	public ShinyBrick(int x,int y,final Game parent){
		super(x,y,parent);
		this.box.animate(new long[]{200,200,200,200},32,35,true);
		body = PhysicsFactory.createBoxBody(parent.World, box, BodyType.StaticBody, Collisions.WALL_FIXTURE_DEF);
		//----------------------------------------------------------------------------------
		//bottom
		PolygonShape shape1 = new PolygonShape();
		Vector2[] verts1 = new Vector2[]{
			new Vector2(-0.50f,0.5f),
			new Vector2(-0.50f,0.4f),
			new Vector2(0.50f,0.4f),
			new Vector2(0.50f,0.5f)};
		shape1.set(verts1);
		//Collisions.WALL_FIXTURE_DEF.shape = shape1;
		//Fixture bottom = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		Collisions.BOTTOM_FIXTURE_DEF.shape = shape1;
		Fixture bottom = body.createFixture(Collisions.BOTTOM_FIXTURE_DEF);
		shape1.dispose();
		//----------------------------------------------------------------------------------
		//top
		final PolygonShape shape = new PolygonShape();
		final Vector2[] verts = new Vector2[]{
			new Vector2(-0.50f,-0.4f),
			new Vector2(-0.50f,-0.5f),
			new Vector2(0.50f,-0.5f),
			new Vector2(0.50f,-0.4f)};
		shape.set(verts);
		Collisions.WALL_FIXTURE_DEF.shape = shape;
		Fixture top = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		//Collisions.WALL_FIXTURE_DEF.shape = shape;
		//Collisions.GROUND_FIXTURE_DEF.shape = shape;
		//final Fixture top = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		//final Fixture top = body.createFixture(Collisions.GROUND_FIXTURE_DEF);
		shape.dispose();
		//----------------------------------------------------------------------------------
		//left
		final PolygonShape shape3 = new PolygonShape();
		final Vector2[] verts3 = new Vector2[]{
			new Vector2(-0.49f,0.49f),
			new Vector2(-0.49f,-0.49f),
			new Vector2(-0.48f,-0.49f),
			new Vector2(-0.48f,0.49f)};
		shape3.set(verts3);
		Collisions.WALL_FIXTURE_DEF.shape = shape3;
		Fixture left = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		//Collisions.WALL_FIXTURE_DEF.shape = shape3;
		//final Fixture left = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		shape3.dispose();
		//----------------------------------------------------------------------------------
		//right
		final PolygonShape shape4 = new PolygonShape();
		final Vector2[] verts4 = new Vector2[]{
			new Vector2(0.48f,0.49f),
			new Vector2(0.48f,-0.49f),
			new Vector2(0.49f,-0.49f),
			new Vector2(0.49f,0.49f)};
		shape4.set(verts4);
		Collisions.WALL_FIXTURE_DEF.shape = shape4;
		Fixture right = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		//Collisions.WALL_FIXTURE_DEF.shape = shape4;
		//final Fixture right = body.createFixture(Collisions.WALL_FIXTURE_DEF);
		shape4.dispose();
		//----------------------------------------------------------------------------------
		body.getFixtureList().get(0).setUserData("boxbody");
		body.getFixtureList().get(1).setUserData("bottom");
		body.getFixtureList().get(2).setUserData("ground");
		body.getFixtureList().get(3).setUserData("wall");
		body.getFixtureList().get(4).setUserData("wall");
		body.setUserData(this.box);
		//body.setUserData(this.box);
		parent.scene.getChildByIndex(1).attachChild(box);
	}
}
