package org.util;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
//import resourcesManager;
import org.util.SceneManager.SceneType;
public abstract class BaseScene extends Scene{
	protected Engine engine;
	protected Activity activity;
	//protected ResourcesManager rm;
	protected VertexBufferObjectManager vbo;
	protected BoundCamera camera;

	public BaseScene(){
		//this.rm = ResourcesManager.getInstance();
		//this.engine = rm.engine;
		//this.activity = rm.activity;
		//this.vbo = rm.vbo;
		//this.camera = rm.camera;
		createScene();
	}
	//abstract
	public abstract void createScene();
	public abstract void onBackKeyPressed();
	public abstract SceneType getSceneType();
	public abstract void disposeScene();
}

