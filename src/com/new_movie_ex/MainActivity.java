package com.new_movie_ex;

import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.new_movie_ex.R;
import com.new_movie_ex.DetectConnection;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

@TargetApi(14)
public class MainActivity extends Activity {

	private RelativeLayout nonVideoLayout;
	private FrameLayout videoLayout;
	private VideoEnabledWebView mWebView;
	private VideoEnabledWebChromeClient mWebChromeClient;
	 private ProgressBar progress;
	 private final Handler handler = new Handler();
	 protected AdView adView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		adView = (AdView)findViewById(R.id.ad);
		adView.loadAd(new AdRequest());
		
		
		initViews();
		initWebView();
		
		if (getPhoneAndroidSDK() >= 14) {// 4.0���Ӳ������
			getWindow().setFlags(0x1000000, 0x1000000);
		}
		progress = (ProgressBar) findViewById(R.id.progressBar);
        //progress.setMax(100);
        progress.setVisibility(View.GONE);
//		mWebView.loadUrl("file:///android_asset/1234.html");
        if(check()){
    		mWebView.addJavascriptInterface(new AndroidBridge(), "android");
    		if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            } else {
            	mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                    String contentDisposition, String mimetype,
                    long contentLength) {
              Intent i = new Intent(Intent.ACTION_VIEW);
              i.setData(Uri.parse(url));
              startActivity(i);
            }
        });
        	mWebView.loadUrl("http://thenewmovie.net");
            }
		}else{
			install();
		}
		//mWebView.loadUrl("http://theumovie.net/old/test.html");
        MainActivity.this.progress.setProgress(0);

		
	}
	private boolean check() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}
	
	private class AndroidBridge {
		public void goMarket() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.adobe.flashplayer"));
					startActivity(installIntent);
				}
			});
		}
	

		public void goumovie() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.umovie"));
					startActivity(installIntent);
				}
			});
			
		}
		public void gonewmovieex() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.new_movie_ex"));
					startActivity(installIntent);
				}
			});
			
		}
		public void gonewmovie() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.new_movie_free"));
					startActivity(installIntent);
				}
			});
			
		}
		public void gotimer() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.w_m_timer"));
					startActivity(installIntent);
				}
			});
			
		}
		public void restartnewmovie() {
			Intent i = getBaseContext().getPackageManager()
		             .getLaunchIntentForPackage( getBaseContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		}
	}
	private void install() {
		mWebView.addJavascriptInterface(new AndroidBridge(), "android");
		mWebView.loadUrl("file:///android_asset/go_market.html");
	}
	
	private void initViews() {
		
		nonVideoLayout = (RelativeLayout)findViewById(R.id.nonVideoLayout);
		videoLayout = (FrameLayout)findViewById(R.id.videoLayout);
		
		mWebView = (VideoEnabledWebView) findViewById(R.id.webView);

	}

	private void initWebView() {
		
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		settings.setPluginState(WebSettings.PluginState.ON);
		settings.setAllowFileAccess(true);
		settings.setLoadWithOverviewMode(true);
		
		mWebChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout,videoLayout);
		mWebView.setWebChromeClient(mWebChromeClient);
		
	}

	public static int getPhoneAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mWebView.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mWebView.onPause();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if(mWebChromeClient.isVideoFullscreen()){
			mWebChromeClient.onHideCustomView();
		}else{
			super.onBackPressed();
		}
	}
	public void setValue(int progress) {
        this.progress.setProgress(progress);       
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     // TODO Auto-generated method stub
     MenuInflater menuInflater = getMenuInflater();
     menuInflater.inflate(R.layout.menu, menu);
     return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
     // TODO Auto-generated method stub
     if(mWebView.canGoForward())
      menu.findItem(R.id.goforward).setEnabled(true);
     else
      menu.findItem(R.id.goforward).setEnabled(false);
     
     if(mWebView.canGoBack())
      menu.findItem(R.id.goback).setEnabled(true);
     else
      menu.findItem(R.id.goback).setEnabled(false);
     
     
     return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     // TODO Auto-generated method stub
     switch(item.getItemId()){
     case (R.id.exit):
      openExitDialog();
      break;
     case (R.id.goback):
    	 mWebView.goBack();
      break;
     case (R.id.reload):
    	mWebView.reload();
     MainActivity.this.progress.setProgress(0);
     MainActivity.this.progress.setVisibility(View.GONE);
      break;
     case (R.id.goforward):
    	 mWebView.goForward();
      break;
       }
       return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            	openExitDialog();
            
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openExitDialog()
    {
     new AlertDialog.Builder(this)
      .setTitle("Exit")
      .setMessage("Exit the NEW-MOVIE?" )
      .setNegativeButton("NO",
        new DialogInterface.OnClickListener(){
       public void onClick(DialogInterface dialoginterface, int i)
       {}
      })
      .setPositiveButton("YES",
        new DialogInterface.OnClickListener()
         {
          public void onClick(DialogInterface dialoginterface, int i)
          { finish();
          }
         })
      .show();
     }
}
