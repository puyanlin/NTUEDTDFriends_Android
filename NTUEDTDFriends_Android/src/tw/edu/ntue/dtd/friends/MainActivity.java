package tw.edu.ntue.dtd.friends;

import tw.edu.ntue.dtd.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.parse.ConfigCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends Activity implements OnClickListener {
	private final String TAG="MainActivity";
	private String[] subTitles;
	private ParseConfig mConfig;
	
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
							   getResources().getString(R.string.jobs),
							   getResources().getString(R.string.signup),
							   getResources().getString(R.string.photos),
							   getResources().getString(R.string.articles),
							   getResources().getString(R.string.donate),
							   };
		
		
		final LinearLayout llSubjectContainer=(LinearLayout) findViewById(R.id.ll_SubjectContainer);
		final LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		
		for(String title : subTitles){
			RelativeLayout cell=(RelativeLayout)inflater.inflate(R.layout.subjectcell,llSubjectContainer,false);
			TextView tvTitle=(TextView) cell.findViewById(R.id.tv_candidateName);
			tvTitle.setText(title);
			cell.setTag(title);
			cell.setOnClickListener(this);
			llSubjectContainer.addView(cell);
		}
		
		ParseConfig.getInBackground(new ConfigCallback() {
			
			@Override
			public void done(ParseConfig config, ParseException exp) {
				// TODO Auto-generated method stub
				if(config!=null){
					mConfig=config;
					
					if(config.getBoolean("isVoting")){
						RelativeLayout cell=(RelativeLayout)inflater.inflate(R.layout.subjectcell,llSubjectContainer,false);
						TextView tvTitle=(TextView) cell.findViewById(R.id.tv_candidateName);
						tvTitle.setText(config.getString("votingTitle"));
						cell.setOnClickListener(MainActivity.this);
						cell.setTag(config.getString("votingTitle"));
						llSubjectContainer.addView(cell, 0);
					
						cell.setBackgroundColor(Color.YELLOW);
					}
					
				}
			}
		});
		
		
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
			builder.setTitle(R.string.app_name);
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
	    if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.jobs))){
	    	Fragment newFragment = new JobListFragment();
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
			Uri uri = Uri.parse("http://alumnus.ntue.edu.tw/donation2.php"); 
		    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		    startActivity(intent);
		}
		if(((String)(v.getTag())).equalsIgnoreCase(getResources().getString(R.string.articles))){
			Fragment newFragment = new RuleFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
		}
		if(((String)(v.getTag())).equalsIgnoreCase(mConfig.getString("votingTitle"))){
			Fragment newFragment = new CandidateInformationFragment();
		    transaction.replace(R.id.rl_main, newFragment);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    transaction.addToBackStack(null);

		    // Commit the transaction
		    transaction.commit();
		}
	}
}
