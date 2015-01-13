package tw.edu.ntue.dtd.friends;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


public class SignupFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final RelativeLayout mainView=(RelativeLayout)inflater.inflate(R.layout.webview,container,false);
		
		WebView wbView=(WebView) mainView.findViewById(R.id.webview);
		wbView.getSettings().setJavaScriptEnabled(true);
		
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
		
		wbView.loadUrl("http://goo.gl/forms/1qlmEb2zr1");
		
		return mainView;
	}

	

}
