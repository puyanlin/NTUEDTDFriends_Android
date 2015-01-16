package tw.edu.ntue.dtd.friends;

import tw.edu.ntue.dtd.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class RuleFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final RelativeLayout mainView=(RelativeLayout)inflater.inflate(R.layout.webview,null);
		
		WebView wbView=(WebView) mainView.findViewById(R.id.webview);
		
		wbView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mainView.findViewById(R.id.loadingProgress).setVisibility(View.GONE);
			}
            
        });
		
		wbView.loadUrl("file:///android_asset/rule.html"); 
		
		return mainView;
	}
}
