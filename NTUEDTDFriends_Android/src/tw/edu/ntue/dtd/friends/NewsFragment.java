package tw.edu.ntue.dtd.friends;

import tw.edu.ntue.dtd.R;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class NewsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final RelativeLayout mainView=(RelativeLayout)inflater.inflate(R.layout.listview_news,container,false);
		
		final NewsAdapter adapter = new NewsAdapter(getActivity(), "DTDNotification");
		adapter.setTextKey("title");
	
		ListView lv=(ListView) mainView.findViewById(R.id.lv_news);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub

				final ParseObject parseObject=adapter.getItem(position);
				Builder builder=new Builder(getActivity());
				builder.setTitle(parseObject.getString("title"));
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
				content.setText(parseObject.getString("content"));
				content.setLinksClickable(true);
				content.setTextSize(18);
				content.setPadding(15,15,15,15);
				builder.setView(content);
				builder.setNeutralButton(getResources().getString(R.string.read), new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				if(parseObject.getString("link")!=null&&!parseObject.getString("link").equalsIgnoreCase("")){
					builder.setPositiveButton(getResources().getString(R.string.getlink), new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Uri uri = Uri.parse(parseObject.getString("link")); 
						    Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
						    startActivity(intent);
						}
					});
				}
				
				builder.show();
			}
		});
		
		return mainView;
	}
	
	private class NewsAdapter extends ParseQueryAdapter<ParseObject>{

		public NewsAdapter(Context context, String className) {
			super(context, className);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getItemView(ParseObject arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view=super.getItemView(arg0, arg1, arg2);
			TextView tv=(TextView) view.findViewById(android.R.id.text1);
			tv.setTextSize(30);
			
			return view;
		}
		

		
	}
}
