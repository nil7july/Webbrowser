package com.example.lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

public class MainActivity extends Activity {

	EditText address;
	WebView webview;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
	   	webview = (WebView) findViewById(R.id.browser);
	   	address =  (EditText) findViewById(R.id.addressbar);
    }
    
    
    public void goButtonClick(View view) {
    	
    	String url = address.getText().toString().replaceAll("\\s+","");
    	if (!url.startsWith("http://")) {
    	    url = "http://" + url;
    	}
    
    	new RetrieveCode().execute(url);
    	
    }
    
    private class RetrieveCode extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... urls) {
    		
    		String result = "";

			try {
				HttpGet httpGet = new HttpGet(urls[0]);
				HttpClient client = new DefaultHttpClient();
				
				HttpResponse response = client.execute(httpGet);
				
				int statusCode = response.getStatusLine().getStatusCode();
				
				if(statusCode == 200) {
					InputStream inputstream = response.getEntity().getContent();
					BufferedReader in = new BufferedReader(new InputStreamReader(inputstream));			
			
					String line;
					while ((line = in.readLine()) != null){
						result +=line;
					}
				}
				
	    	} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return result;
    	}
    	
    	protected void onPostExecute(String result) {
    		   // execution of result of Long time consuming operation
    		updateWebView(result);
    	}
    	
    	private void updateWebView(String result) { 
    	   	webview.getSettings().setJavaScriptEnabled(true);
    	   	webview.setWebChromeClient(new WebChromeClient());
    		webview.loadData(result, "text/html", "UTF-8");
    	}	
    }   
}