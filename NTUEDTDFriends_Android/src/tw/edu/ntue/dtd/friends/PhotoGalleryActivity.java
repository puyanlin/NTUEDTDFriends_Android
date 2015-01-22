package tw.edu.ntue.dtd.friends;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import tw.edu.ntue.dtd.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;



public class PhotoGalleryActivity extends FragmentActivity{
	
	private int NUM_PAGES = 1;
	private List<ParseObject> photoURLs;
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_gallery);
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(Color.BLACK);
        
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DTDPhotos");
        query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> listParseObj, ParseException e) {
				// TODO Auto-generated method stub
				ProgressBar initProgress=(ProgressBar) findViewById(R.id.progress_galleryview);
				initProgress.setVisibility(View.GONE);
				 
				 photoURLs=listParseObj;
				 NUM_PAGES=listParseObj.size();
				 
				 mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
			     mPager.setAdapter(mPagerAdapter);
			}
        });
        
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	
	private class ScreenSlidePageFragment extends Fragment {
		private int pageIndex;
		public ScreenSlidePageFragment(int pos) {
			// TODO Auto-generated constructor stub
			pageIndex=pos;
		}
		
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        RelativeLayout rootView = (RelativeLayout) inflater.inflate(
	                R.layout.rl_imgview, container, false);
	        ImageView imgView=(ImageView) rootView.findViewById(R.id.imgVoteInk);
	        GetImageTask task=new GetImageTask(imgView,photoURLs.get(pageIndex).getString("url"));
	        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
	        return rootView;
	    }
	    
		private class GetImageTask extends AsyncTask<Void, Void, Void>{
			private ImageView mImgView;
			private Bitmap bmp;
			private String mUrl;
			public GetImageTask(ImageView imgV,String url){
				super();
				mImgView=imgV;
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
				mImgView.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						final String[] funcs=new String[]{getResources().getString(R.string.share)};
						AlertDialog.Builder builder=new AlertDialog.Builder(PhotoGalleryActivity.this);
						builder.setItems(funcs, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(funcs[which].equalsIgnoreCase(getResources().getString(R.string.share))){
								
							        if (bmp != null) {
							            try {
							                String filepath = getExternalFilesDir("cache").getAbsolutePath() + "/snapshot.png";
							                File shareImg = new File(filepath);
							                if (shareImg.exists()) shareImg.delete();
							                FileOutputStream out = new FileOutputStream(filepath);
							                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);

							                shareImg = new File(filepath);
							                if (shareImg.exists()) {
							                    Intent shareIntent = new Intent();
							                    shareIntent.setAction(Intent.ACTION_SEND);
							                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareImg));
							                    shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.app_name));
							                    shareIntent.setType("image/jpeg");
							                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));
							                }

							            } catch (Exception e) {
							                e.printStackTrace();
							            }
							        } else {
							            Toast.makeText(PhotoGalleryActivity.this, R.string.share_fail, Toast.LENGTH_SHORT).show();
							        }
								}
							}
						});
						builder.setCancelable(true);
						builder.show().setCanceledOnTouchOutside(true);
						
						return false;
					}
				});
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
