package org.games;
import org.games.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class Menu extends Activity{
	private static final String TAG = "Menu";
	private static final int REQUEST_CODE = 6384; //onActivityResult request code
	//-------------------------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//-------------------------------------------------------
		setContentView(R.layout.menu_layout);
		//-------------------------------------------------------
		Button settingsButton = (Button)findViewById(R.id.settingsbutton);
		Button startButton = (Button)findViewById(R.id.startbutton);
		//-------------------------------------------------------
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Menu.this, Game.class));				
				Menu.this.finish();
			}
		});
		//-------------------------------------------------------
	}
}
