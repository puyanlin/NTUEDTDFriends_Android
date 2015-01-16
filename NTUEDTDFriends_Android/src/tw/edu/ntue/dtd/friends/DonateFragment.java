package tw.edu.ntue.dtd.friends;

import tw.edu.ntue.dtd.R;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class DonateFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final ScrollView mainView=(ScrollView)inflater.inflate(R.layout.scrlv_donateinfo,null);
		
		TextView tvGetCreditCardAuth=(TextView) mainView.findViewById(R.id.tv_getCreditCardDonateInfo);
		tvGetCreditCardAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("http://site.puyan.idv.tw:8080/share.cgi?ssid=0Wtc3D8&fid=0Wtc3D8&ep=LS0tLQ==&open=normal"); 
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
			    startActivity(intent);
			}
		});
		
		TextView tvGoNTUE=(TextView) mainView.findViewById(R.id.tv_link);
		tvGoNTUE.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("http://alumnus.ntue.edu.tw/donation2.php"); 
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
			    startActivity(intent);
			}
		});
		
		return mainView;
	}
}
