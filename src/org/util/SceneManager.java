package org.util;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
public class SceneManager{
	private BaseScene game;
	private BaseScene menu;
	private BaseScene loading;
	private BaseScene splash;
	private static final SceneManager INSTANCE = new SceneManager();
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;
	//private Engine engine = ResourcesManager.getInstance().engine;
	public enum SceneType{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING,
	}
	/*
	public void setScene(SceneType st){
		switch(st){
			case SCENE_MENU:
				setScene(menu);
				break;
			case SCENE_GAME:
				setScene(game);
				break;
			default:
				break;
		}
	}
	public void createMenuScene(){
		ResourcesManager.getInstance().loadMenuResources();
		menu = new Menu();
		loading = new Loading();
		SceneManager.getInstance().setScene(menu);
		disposeSplashScene();
	}
	public BaseScene createLoadingScene(){
		ResourcesManager.getInstance().loadSplashScreen();
		splash = new Splash();
		currentScene = splash;
		return splash;
	}
	//disposeSplashscrene()
	//loadmenuscene(final engine engine)
	//loadgamescene(final engine engine)
	*/

	public static SceneManager getInstance(){
		return INSTANCE;
	}
	public SceneType getCurrentSceneType(){
		return currentSceneType;
	}
	public BaseScene getCurrentScene(){
		return currentScene;
	}
}



