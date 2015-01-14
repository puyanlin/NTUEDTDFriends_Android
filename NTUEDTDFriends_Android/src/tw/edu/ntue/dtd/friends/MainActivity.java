package tw.edu.ntue.dtd.friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends Activity implements OnClickListener {
	private final String TAG="MainActivity";
	private String[] subTitles;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, getResources().getString(R.string.ApplicationID), getResources().getString(R.string.ClientKey));
		
		// Specify a Activity to handle all pushes by default.
	    PushService.setDefaultPushCallback(this, MainActivity.class);

	    // Save the current installation.
	    ParseInstallation.getCurrentInstallation().saveInBackground();

	    ParseAnalytics.trackAppOpened(getIntent());
		
		initGUI();
	}
	
	private void initGUI(){
		subTitles=new String[]{getResources().getString(R.string.news),
							   getResources().getString(R.string.signup),
							   getResources().getString(R.string.photos),
							   getResources().getString(R.string.donate),
							   getResources().getString(R.string.articles)};
		
		
		LinearLayout llSubjectContainer=(LinearLayout) findViewById(R.id.ll_SubjectContainer);
		LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		
		for(String title : subTitles){
			RelativeLayout cell=(RelativeLayout)inflater.inflate(R.layout.subjectcell,llSubjectContainer,false);
			TextView tvTitle=(TextView) cell.findViewById(R.id.tv_title);
			tvTitle.setText(title);
			cell.setTag(title);
			cell.setOnClickListener(this);
			llSubjectContainer.addView(cell);
		}
		
		LinearLayout ll_fbbtn=(LinearLayout) findViewById(R.id.ll_fbbtn);
		ll_fbbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String facebookPackageName = "com.facebook.katana";
				//String facebookClassName = "com.facebook.katana.LoginActivity";
				   
				try {
				    getPackageManager()
				      .getApplicationInfo(facebookPackageName, 0);
				    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/207809079397118"));
				    
				    startActivity(intent); 
				} catch( PackageManager.NameNotFoundException e ){
					Toast.makeText(MainActivity.this, "please install facebook",Toast.LENGTH_LONG).show();
					
					Uri uri = Uri.parse("market://details?id=" + facebookPackageName); 
				    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
				    startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("數位系友會");
			builder.setMessage(getResources().getString(R.string.about_content));
			builder.setCancelable(true);
			builder.show().setCanceledOnTouchOutside(true);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e(TAG, (String)(v.getTag()));
		
	    FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.news))){
	    	Fragment newFragment = new NewsFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
			
		}
	    
		if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.signup))){
			Fragment newFragment = new SignupFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
			
		}
		if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.photos))){
			Intent intent=new Intent(MainActivity.this, PhotoGalleryActivity.class);
			startActivity(intent);
			
		}
		
		if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.donate))){
			Fragment newFragment = new DonateFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
			
		}
		if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.articles))){
			Fragment newFragment = new RuleFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
			
		}
	}
}