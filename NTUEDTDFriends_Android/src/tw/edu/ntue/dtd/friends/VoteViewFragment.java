package tw.edu.ntue.dtd.friends;

import java.util.ArrayList;
import java.util.List;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import tw.edu.ntue.dtd.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class VoteViewFragment extends Fragment {
	public static final int SELECT_NONE=25535;
	private int selectIndex=SELECT_NONE;
	private ArrayList<View> arrayCandidateView=new ArrayList<View>();
	private List<ParseObject> listCandidate;
	private ParseObject voteTicket;
	
	public VoteViewFragment(List<ParseObject> candidates) {
		// TODO Auto-generated constructor stub
		listCandidate=candidates;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final ScrollView mainView=(ScrollView)inflater.inflate(R.layout.voteview,container,false);
		LinearLayout llcandidatesContainer=(LinearLayout)mainView.findViewById(R.id.ll_candidateContainer);
		
		for(ParseObject candidate : listCandidate){
			RelativeLayout rowCandidate=(RelativeLayout) inflater.inflate(R.layout.candidateview, llcandidatesContainer, false);
			TextView tvNumber=(TextView) rowCandidate.findViewById(R.id.tv_candidateNumber);
			TextView tvName=(TextView) rowCandidate.findViewById(R.id.tv_candidateName);
			
			tvNumber.setText(CandidateInformationFragment.candidateNumers[candidate.getNumber("Number").intValue()]);
			tvName.setText(candidate.getString("Name"));
			rowCandidate.setTag(candidate.getNumber("Number"));
			final int index=candidate.getNumber("Number").intValue();
			rowCandidate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectIndex=index;
					updateChoice();
				}
			});
			llcandidatesContainer.addView(rowCandidate);
			arrayCandidateView.add(rowCandidate);
		}
		
		mainView.findViewById(R.id.btnClearChioce).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectIndex=SELECT_NONE;
				updateChoice();
			}
		});
		
		final Button btnCheckSerial=(Button)mainView.findViewById(R.id.btn_sendSerial);
		btnCheckSerial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText serialField=(EditText) mainView.findViewById(R.id.edtTxtSerial);
				final ProgressBar progress=(ProgressBar) mainView.findViewById(R.id.progress_checkSerial);
				progress.setVisibility(View.VISIBLE);
				ParseQuery<ParseObject> query = ParseQuery.getQuery("DTDMajorVoting");
				query.getInBackground(serialField.getText().toString(), new GetCallback<ParseObject>() {
					
					@Override
					public void done(ParseObject ticket, ParseException e) {
						// TODO Auto-generated method stub
						progress.setVisibility(View.INVISIBLE);
						if(e==null){
							voteTicket=ticket;
							
							if(voteTicket.getNumber("selectNum")!=null&&voteTicket.getNumber("selectNum").intValue()>0){
								serialField.setError("此序號已投票");
							}else{
								serialField.setEnabled(false);
								btnCheckSerial.setVisibility(View.GONE);
								mainView.findViewById(R.id.chckMark).setVisibility(View.VISIBLE);
								mainView.findViewById(R.id.btnVote).setVisibility(View.VISIBLE);
							}
							
						}else{
							serialField.setError("序號錯誤");
						}
					}
				});
			}
		});
		Button btnVote=(Button) mainView.findViewById(R.id.btnVote);
		btnVote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(voteTicket!=null){
					voteTicket.put("selectNum", selectIndex+1);
					voteTicket.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e==null){
								Toast.makeText(getActivity(), "已完成投票，謝謝",Toast.LENGTH_LONG).show();
								getActivity().onBackPressed();
							}
						}
					});
				}
			}
		});
		
		return mainView;
	}
	
	private void updateChoice(){
		int i=0;
		for(View candidateview : arrayCandidateView){
			candidateview.findViewById(R.id.imgVoteInk).setVisibility(i==selectIndex?View.VISIBLE:View.INVISIBLE);
			i++;
		}
	}
}
