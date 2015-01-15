package tw.edu.ntue.dtd.friends;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JobListFragment extends Fragment {	
	private ArrayList<ParseObject> listCSJobs=new ArrayList<ParseObject>();
	private ArrayList<ParseObject> listDesignJobs=new ArrayList<ParseObject>();
	private ArrayList<ParseObject> listManageJobs=new ArrayList<ParseObject>();
	private ArrayList<ParseObject> listPTJobs=new ArrayList<ParseObject>();
	private ArrayList<ParseObject> listOtherJobs=new ArrayList<ParseObject>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final RelativeLayout mainView=(RelativeLayout)inflater.inflate(R.layout.rl_groupedlist,container,false);
	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DTDJobs");
		query.findInBackground(new FindCallback<ParseObject>() {
		    
			@Override
			public void done(List<ParseObject> listObj, ParseException exception) {
				// TODO Auto-generated method stub
				for(ParseObject obj:listObj){
					if(obj.getString("Category").equalsIgnoreCase("CS")){
						listCSJobs.add(obj);
					}
					if(obj.getString("Category").equalsIgnoreCase("DESIGN")){
						listDesignJobs.add(obj);
					}
					if(obj.getString("Category").equalsIgnoreCase("MANAGE")){
						listManageJobs.add(obj);
					}
					if(obj.getString("Category").equalsIgnoreCase("PT")){
						listPTJobs.add(obj);
					}
					if(obj.getString("Category").equalsIgnoreCase("OTHER")){
						listOtherJobs.add(obj);
					}
				}
				initGUI(mainView);
			}
		});
		
		return mainView;
	}
	
	private void initGUI(View ParentView){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		String[] sectionTitles=new String[]{getResources().getString(R.string.job_CS),
											getResources().getString(R.string.job_Design),
											getResources().getString(R.string.job_Manage),
											getResources().getString(R.string.job_PT),
											getResources().getString(R.string.job_Other)};
		
		LinearLayout llcontainer=(LinearLayout) ParentView.findViewById(R.id.ll_SectionContainer);
		for(String title :sectionTitles){
			LinearLayout llSection=(LinearLayout) inflater.inflate(R.layout.sectionlayout, llcontainer,false);
			TextView tvTitle=(TextView) llSection.findViewById(R.id.tvSectionTitle);
			tvTitle.setText(title);
			llcontainer.addView(llSection);
			LinearLayout secContainer=(LinearLayout) llSection.findViewById(R.id.ll_rowContainer);
			if(title.equalsIgnoreCase(getResources().getString(R.string.job_CS))){
				initRows(listCSJobs, secContainer);
			}
			if(title.equalsIgnoreCase(getResources().getString(R.string.job_Design))){
				initRows(listDesignJobs, secContainer);
			}
			if(title.equalsIgnoreCase(getResources().getString(R.string.job_Manage))){
				initRows(listManageJobs, secContainer);
			}
			if(title.equalsIgnoreCase(getResources().getString(R.string.job_PT))){
				initRows(listPTJobs, secContainer);
			}
			if(title.equalsIgnoreCase(getResources().getString(R.string.job_Other))){
				initRows(listOtherJobs, secContainer);
			}
			
		}
		ProgressBar progress=(ProgressBar) ParentView.findViewById(R.id.groupedsecLoadingProgress);
		progress.setVisibility(View.GONE);
	}
	
	private void initRows(ArrayList<ParseObject> list,LinearLayout secContainer){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		if(list.size()==0){
			TextView tvNoOp=new TextView(getActivity());
			tvNoOp.setText("¼ÈµLÂ¾¯Ê¸ê°T");
			secContainer.addView(tvNoOp);
			tvNoOp.setPadding(5, 0,0,0);
			return;
		}
		for(final ParseObject obj :list){
			RelativeLayout jobrow=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
			secContainer.addView(jobrow);
			TextView rowTitle=(TextView) jobrow.findViewById(R.id.tv_title);
			rowTitle.setText(obj.getString("Title"));
			
			jobrow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Builder builder=new Builder(getActivity());
					builder.setTitle(obj.getString("Title"));
					TextView content=new TextView(getActivity()){
						{
							setVerticalScrollBarEnabled(true);
		                    setMovementMethod(ScrollingMovementMethod.getInstance());
		                    setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

		                    // Force scrollbars to be displayed.
		                    TypedArray a = this.getContext().getTheme().obtainStyledAttributes(new int[0]);
		                    initializeScrollbars(a);
		                    a.recycle();
						}
					};
					content.setText(obj.getString("content"));
					content.setLinksClickable(true);
					content.setTextSize(18);
					content.setPadding(15,15,15,15);
					builder.setView(content);
					builder.setNeutralButton(getResources().getString(R.string.read), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					if(obj.getString("link")!=null&&!obj.getString("link").equalsIgnoreCase("")){
						builder.setPositiveButton(getResources().getString(R.string.getlink), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Uri uri = Uri.parse(obj.getString("link")); 
							    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
							    startActivity(intent);
							}
						});
					}
					
					builder.show();
				}
			});
		}
	}
	
}
