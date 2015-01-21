package tw.edu.ntue.dtd.friends;

import java.util.List;

import tw.edu.ntue.dtd.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CandidateInformation extends Fragment {
	private List<ParseObject> arrayCandidates;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final RelativeLayout mainView=(RelativeLayout)inflater.inflate(R.layout.rl_groupedlist,container,false);
	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DTDCandidate");
		query.findInBackground(new FindCallback<ParseObject>() {
		    
			@Override
			public void done(List<ParseObject> listObj, ParseException exception) {
				// TODO Auto-generated method stub
				arrayCandidates=listObj;
				initGUI(mainView);
			}
		});
		
		return mainView;
	}
	
	private void initGUI(View ParentView){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		String[] sectionTitles=new String[]{getResources().getString(R.string.CandidateInfo),
											""};
		
		LinearLayout llcontainer=(LinearLayout) ParentView.findViewById(R.id.ll_SectionContainer);
		for(int i=0;i<sectionTitles.length;i++){
			
			LinearLayout llSection=(LinearLayout) inflater.inflate(R.layout.sectionlayout, llcontainer,false);
			TextView tvTitle=(TextView) llSection.findViewById(R.id.tvSectionTitle);
			tvTitle.setText(sectionTitles[i]);
			llcontainer.addView(llSection);
			
			if(i==0) initRows(arrayCandidates, llSection);
			else initSingleRows(getResources().getString(R.string.goVote), llSection);
			
		}
		ProgressBar progress=(ProgressBar) ParentView.findViewById(R.id.groupedsecLoadingProgress);
		progress.setVisibility(View.GONE);
	}
	
	private void initRows(List<ParseObject> list,LinearLayout secContainer){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		
		for(final ParseObject obj :list){
			RelativeLayout rowCandidate=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
			secContainer.addView(rowCandidate);
			TextView rowTitle=(TextView) rowCandidate.findViewById(R.id.tv_title);
			rowTitle.setText(obj.getString("Name"));
			
			rowCandidate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Builder builder=new Builder(getActivity());
//					builder.setTitle(obj.getString("Title"));
//					TextView content=new TextView(getActivity()){
//						{
//							setVerticalScrollBarEnabled(true);
//		                    setMovementMethod(ScrollingMovementMethod.getInstance());
//		                    setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
//
//		                    // Force scrollbars to be displayed.
//		                    TypedArray a = this.getContext().getTheme().obtainStyledAttributes(new int[0]);
//		                    initializeScrollbars(a);
//		                    a.recycle();
//						}
//					};
//					content.setText(obj.getString("content"));
//					content.setLinksClickable(true);
//					content.setTextSize(18);
//					content.setPadding(15,15,15,15);
//					builder.setView(content);
//					builder.setNeutralButton(getResources().getString(R.string.read), new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
//					if(obj.getString("link")!=null&&!obj.getString("link").equalsIgnoreCase("")){
//						builder.setPositiveButton(getResources().getString(R.string.getlink), new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								Uri uri = Uri.parse(obj.getString("link")); 
//							    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
//							    startActivity(intent);
//							}
//						});
//					}
//					
//					builder.show();
				}
			});
		}
	}
	private void initSingleRows(String title,LinearLayout secContainer){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		RelativeLayout goVoteRow=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
		secContainer.addView(goVoteRow);
		TextView rowTitle=(TextView) goVoteRow.findViewById(R.id.tv_title);
		rowTitle.setText(title);
	}
}
