package org.util;
import org.util.constants.Collisions;
import org.games.Game;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourcesManager{
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	/*
	private static BitmapTextureAtlas ItemAtlas;
	public static TiledTextureRegion BlockTexture;

	public void loadBrickGraphics(Game parent){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.ItemAtlas = new BitmapTextureAtlas(parent.getTextureManager(),512,256,TextureOptions.DEFAULT);
		this.BlockTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.ItemAtlas,parent,"blocks.png",0,0,16,5);
		this.ItemAtlas.load();
	}
	*/
}

