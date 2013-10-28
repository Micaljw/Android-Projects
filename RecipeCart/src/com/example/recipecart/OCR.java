package com.example.recipecart;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OCR extends Activity implements View.OnClickListener{
	
	ImageButton ib;
	ImageView iv;
	Intent i;
	Bundle b;
	final static int cameraData = 0;
	Bitmap bmp;
	HttpGet jobStatusRequest = new HttpGet();
	
	boolean done = false;
	TextView messageTextView;
	private String stsImageUrl;
//	private String stsKey;
//	private String ocrKey;
	String ocrTextUrl = "";
	String ocrPdfUrl = "";
	
	private final static String OCR_SUBMIT_URL = 
			"http://svc.webservius.com/v1/wisetrend/wiseocr/submit?wsvKey=atEy2v48-dJVbm1fDqt4xGrlyCxfif_P";
	
	private final static String OCR_UPLOAD_URL =
			"http://ws.webservius.com/sts/v1/20mb/15m?wsvKey=FYBMuOMHCy2uDfkmfocfRkCGCiNNiCBK";
	
	private final static String OCR_JOB_XML1 ="<Job><InputURL>OCR_UPLOAD_URL</InputURL> + " +
			"<InputType>JPG</InputType></Job>";
			
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ocr);
		initialize();
	}
	
	private void initialize(){
		
		b = new Bundle();
		iv = (ImageView) findViewById(R.id.ivReturnedPic);
		ib = (ImageButton) findViewById(R.id.ibTakePic);
		ib.setOnClickListener(this);
		
	//	stsKey = b.getString("stsKey");
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i, cameraData);
		b = i.getBundleExtra("info");
		//ocrKey = b.getString("ocrKey");
		//sendPhoto();
		new ImageUploadTask().execute();
		//submitOCRJobRequest();
		//new ImageUploadTask().execute();
	}
	
	/*
	private void sendPhoto() {
		// TODO Auto-generated method stub
	//	stsImageUrl = getApplicationContext().
		try {
			URL myUrl = new URL(OCR_UPLOAD_URL);
			HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();
			
			OutputStream output = connection.getOutputStream();
			
			bmp.compress(CompressFormat.JPEG, 50, output);
			
			output.close();
			
			Scanner result = new Scanner(connection.getInputStream());
			String response = result.nextLine();
			
			while(response != null){
				Log.e("ImageUploader", "Error uploading image: " + response);
			}
			result.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException p){
			p.printStackTrace();
		} catch (IOException i){
			i.printStackTrace();
		}
		
	}*/
	
	class ImageUploadTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... unsued) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				//Log.d("debug", " url is " + OCR_UPLOAD_URL + stsKey);
				HttpPost httpPost = new HttpPost(OCR_UPLOAD_URL);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.JPEG, 50, bos);
				byte[] data = bos.toByteArray();
				ByteArrayEntity bae = new ByteArrayEntity(data);
				bae.setContentType("image/jpeg");
				httpPost.setEntity(bae);
				HttpResponse response = httpClient.execute(httpPost, localContext);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String sResponse = reader.readLine();
				return sResponse;
			} catch (Exception e) {
				Toast.makeText(null, e.getMessage(), Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	messageTextView.setText("uploading image ...");
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (sResponse != null) {
					stsImageUrl = sResponse;
			    	messageTextView.setText("submitting OCR Job ...");
					submitOCRJobRequest();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}
	
	private boolean submitOCRJobRequest(){
		BufferedReader in = null;
    	messageTextView.setText("submitting OCR Job ...");
		try{	
    	// prepare and send post request (ocr job submission)
    	final HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(OCR_SUBMIT_URL);

		StringEntity se;
		
		se = new StringEntity(OCR_JOB_XML1 + stsImageUrl);
		se.setContentType("text/xml");
		httpPost.setEntity(se);
		HttpResponse response = httpClient.execute(httpPost, localContext);
		
		// read XML response
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String sResponse = reader.readLine();
		// parse XML response, check it was submitted
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		final DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(sResponse.getBytes("UTF-8"));
		Document doc = db.parse(is);
		NodeList nl = doc.getElementsByTagName("Status");
		final String ocrJobUrl;
		if (nl.item(0).getFirstChild().getNodeValue().equals("Submitted")){
			// job submitted ok, prepare to check status
			messageTextView.setText("OCR Job submited ... checking status");
			ocrJobUrl = doc.getElementsByTagName("JobURL").item(0).getFirstChild().getNodeValue();
		    jobStatusRequest.setURI(new URI(ocrJobUrl));

		    final Handler handler = new Handler(); 
	        Timer t = new Timer();
	        TimerTask tt = new TimerTask() { 
                public void run() { handler.post(new Runnable() { 
                	public void run() { 

    					try {
							// execute "get" to check job status
    						HttpResponse statusResponse = httpClient.execute(jobStatusRequest);
    						// read XML response
							BufferedReader in = new BufferedReader
								(new InputStreamReader(statusResponse.getEntity().getContent()));
						    StringBuffer sb = new StringBuffer("");
						    String line = "";
						    while ((line = in.readLine()) != null) {
						        sb.append(line);
						    }
						    in.close();
							InputStream is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
							// parse XML response, check status, if finished, retrieve TXT and PDF urls
							// otherwise wait 5 seconds and check again
							Document doc = db.parse(is);
							NodeList nl = doc.getElementsByTagName("Status");
							if (nl.item(0).getFirstChild().getNodeValue().equals("Finished")){
								String[] uri = new String[2];
								String[] outputType = new String[2];
								NodeList files = doc.getElementsByTagName("File");
								if (files.getLength() != 2) Log.e("debug", "problem: got " + 
										files.getLength() + " file nodes");
								for (int i=0; i<files.getLength(); i++){
									 NodeList elements = files.item(i).getChildNodes();
										for (int j=0; j<elements.getLength(); j++){
											if (elements.item(j).getNodeName().equals("Uri"))
												uri[i] = elements.item(j).getFirstChild().getNodeValue();
											if (elements.item(j).getNodeName().equals("OutputType"))
												outputType[i] = elements.item(j).getFirstChild().getNodeValue();
										}
									}
									if (outputType[0].equals("PDF")) {
										ocrPdfUrl = uri[0];
										ocrTextUrl = uri[1];
									}
									if (outputType[0].equals("TXT")) {
										ocrPdfUrl = uri[1];
										ocrTextUrl = uri[0];
									}
								
						    		Intent i = new Intent(getApplicationContext(), CompleteActivity.class);
									Bundle b = new Bundle();
									b.putString("ocrTextUrl", ocrTextUrl);
									b.putString("ocrPdfUrl", ocrPdfUrl);
									i.putExtra("info", b);
									startActivity(i);
								} else {
						    		messageTextView.setText("Job is " + nl.item(0).getFirstChild().getNodeValue() +
						    			" .. waiting 5 seconds to try again");
									handler.postDelayed(this, 5000);
								}
							} catch (Exception e) {
								e.printStackTrace();
						}
                            
                	}
                });
                	}
            	}; 
	        	t.schedule(tt, 5000); 
			} else toast("Error submitting job: status is " + nl.item(0).getNodeValue());
		
			return true;	
		} catch (Exception e){
			Log.e("error", e.getMessage(), e);
			return false;
		}
		finally {
	    	if (in != null) {
	        	try {
	            	in.close();
	            	} catch (IOException e) {
	            	Log.e("error", e.getMessage());
	            }
	    	}
		}
	}
	
	private final void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
		}
	}
}
