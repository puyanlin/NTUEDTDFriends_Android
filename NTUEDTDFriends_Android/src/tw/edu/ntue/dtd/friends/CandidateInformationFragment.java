package tw.edu.ntue.dtd.friends;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import tw.edu.ntue.dtd.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CandidateInformationFragment extends Fragment {
	private List<ParseObject> arrayCandidates;
	public static String[] candidateNumers=new String[]{"①","②","③","④","⑤","⑥","⑦","⑧","⑨"};
	
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
		String[] sectionTitles=new String[]{getResources().getString(R.string.CandidateInfo),""};
		
		LinearLayout llcontainer=(LinearLayout) ParentView.findViewById(R.id.ll_SectionContainer);
		for(int i=0;i<sectionTitles.length;i++){
			
			LinearLayout llSection=(LinearLayout) inflater.inflate(R.layout.sectionlayout, llcontainer,false);
			TextView tvTitle=(TextView) llSection.findViewById(R.id.tvSectionTitle);
			tvTitle.setText(sectionTitles[i]);
			llcontainer.addView(llSection);
			
			if(i==0) initRows(arrayCandidates, llSection);
			else{
				initSingleRows(getResources().getString(R.string.goVote), llSection);
			}
			
		}
		ProgressBar progress=(ProgressBar) ParentView.findViewById(R.id.groupedsecLoadingProgress);
		progress.setVisibility(View.GONE);
	}
	
	private void initRows(List<ParseObject> list,LinearLayout secContainer){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		
		for(final ParseObject obj :list){
			RelativeLayout rowCandidate=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
			secContainer.addView(rowCandidate);
			TextView rowTitle=(TextView) rowCandidate.findViewById(R.id.tv_candidateName);
			rowTitle.setText(candidateNumers[obj.getNumber("Number").intValue()]+" "+obj.getString("Name"));
			
			rowCandidate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
				    
				    Fragment newFragment = new CandidateDetailFragment(obj);
					transaction.replace(R.id.rl_grouplist_container, newFragment);
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					transaction.addToBackStack(null);
					transaction.commit();

				}
			});
		}
	}
	private void initSingleRows(String title,LinearLayout secContainer){
		LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
		RelativeLayout goVoteRow=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
		secContainer.addView(goVoteRow);
		TextView rowTitle=(TextView) goVoteRow.findViewById(R.id.tv_candidateName);
		rowTitle.setText(title);
		goVoteRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
			    
			    Fragment newFragment = new VoteViewFragment(arrayCandidates);
				transaction.replace(R.id.rl_grouplist_container, newFragment);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				transaction.addToBackStack(null);
				transaction.commit();

			}
		});
	}
	
	private class CandidateDetailFragment extends Fragment{
		private ParseObject cadidateObj;
		
		public CandidateDetailFragment(ParseObject cadidate) {
			// TODO Auto-generated constructor stub
			super();
			cadidateObj=cadidate;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			final ScrollView mainView=(ScrollView)inflater.inflate(R.layout.candidate_detail,container,false);
			
			TextView tvNum=(TextView) mainView.findViewById(R.id.tv_num);
			tvNum.setText(candidateNumers[cadidateObj.getNumber("Number").intValue()]);
			TextView tvName=(TextView) mainView.findViewById(R.id.tv_name);
			tvName.setText(cadidateObj.getString("Name"));
			
			TextView tvClass=(TextView) mainView.findViewById(R.id.tv_class);
			tvClass.setText(cadidateObj.getString("Class"));
			TextView tvGrade=(TextView) mainView.findViewById(R.id.tv_grade);
			tvGrade.setText(cadidateObj.getNumber("Grade").intValue()+"級");
			
			LinearLayout ll_container=(LinearLayout) mainView.findViewById(R.id.ll_candidatedetail_container);
		
			LinearLayout llSectionExp=(LinearLayout) inflater.inflate(R.layout.sectionlayout, ll_container,false);
			TextView tvTitle=(TextView) llSectionExp.findViewById(R.id.tvSectionTitle);
			tvTitle.setText(getResources().getString(R.string.expeirnce));
			ll_container.addView(llSectionExp);
			this.initRows(cadidateObj.getList("Experience"), llSectionExp);
			
			LinearLayout llSectionPolitics=(LinearLayout) inflater.inflate(R.layout.sectionlayout, ll_container,false);
			tvTitle=(TextView) llSectionPolitics.findViewById(R.id.tvSectionTitle);
			tvTitle.setText(getResources().getString(R.string.politics));
			ll_container.addView(llSectionPolitics);
			this.initRows(cadidateObj.getList("Politics"), llSectionPolitics);
			
			GetImageTask task=new GetImageTask((ImageView)mainView.findViewById(R.id.img_candidate), (ProgressBar)mainView.findViewById(R.id.progress_loadImg), cadidateObj.getString("ImageUrl"));
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
			return mainView;
		}
		
		private void initRows(List<Object> list,LinearLayout secContainer){
			LayoutInflater inflater=(LayoutInflater)(getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
			
			for(Object exp :list){
				RelativeLayout rowCandidate=(RelativeLayout) inflater.inflate(R.layout.subjectcell, secContainer, false);
				secContainer.addView(rowCandidate);
				TextView rowTitle=(TextView) rowCandidate.findViewById(R.id.tv_candidateName);
				rowTitle.setText((String)exp);
				rowCandidate.findViewById(R.id.imgVoteInk).setVisibility(View.GONE);
			}
		}
		
		private class GetImageTask extends AsyncTask<Void, Void, Void>{
			private ImageView mImgView;
			private Bitmap bmp;
			private String mUrl;
			private ProgressBar mProgressBar;
			public GetImageTask(ImageView imgV,ProgressBar progressBar,String url){
				super();
				mImgView=imgV;
				mProgressBar=progressBar;
				mUrl=url;
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//bmp=getImageBitmap("http://goo.gl/IkIGzs");
				loadPic(mUrl);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				mImgView.setImageBitmap(bmp);
				mProgressBar.setVisibility(View.GONE);
			}

			private void loadPic(String mUrl){

	            try {
	                URL url = new URL(mUrl);

	                InputStream input = url.openStream();
	                bmp=BitmapFactory.decodeStream(input);
	                input.close();
	                
	            }catch (Exception e){
	                e.printStackTrace();
	            }

	        }
			
		}
		
		
	}
}
