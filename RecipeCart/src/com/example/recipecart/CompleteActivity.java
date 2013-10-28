package com.example.recipecart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CompleteActivity extends Activity implements OnClickListener {
	private String ocrTextUrl = null;
	private String ocrPdfUrl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.complete);
        
        ((TextView) findViewById(R.id.textView_completedClickText)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_viewText)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_viewPDF)).setOnClickListener(this);

        Intent i = getIntent();
		Bundle b = new Bundle();
		b = i.getBundleExtra("info");
		ocrTextUrl = b.getString("ocrTextUrl");
		ocrPdfUrl = b.getString("ocrPdfUrl");
	}

	@Override
	public void onClick(View view) {
		Intent viewIntent = new Intent(); 
		switch (view.getId()){
		case R.id.textView_completedClickText:
			viewIntent.setAction("android.intent.action.VIEW");
			viewIntent.setData(Uri.parse("http://www.ocr-it.com/ocr-cloud-2-0-api"));
			startActivity(viewIntent);  
			break;
		case R.id.button_viewText:
			viewIntent.setAction("android.intent.action.VIEW");
			viewIntent.setData(Uri.parse(ocrTextUrl));
			startActivity(viewIntent);  
			break;
		case R.id.button_viewPDF:
			viewIntent.setAction("android.intent.action.VIEW");
			viewIntent.setData(Uri.parse(ocrPdfUrl));
			startActivity(viewIntent);  
			break;
		}
		
	}
}
