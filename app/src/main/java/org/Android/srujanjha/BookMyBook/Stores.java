package org.android.srujanjha.bookmybook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.json.JSONArray;
import org.json.JSONObject;


public class Stores extends ActionBarActivity {
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
            bd.putIntArray("pr",pr);
            bd1.putString("det", details);
            bd1.putString("sum", summary);
            if(isInternet()) {new GetPricesTask().execute(0);
            }else Toast.makeText(getApplicationContext(),"Sorry, but you are not connected to Internet.",Toast.LENGTH_LONG).show();
        }
        else{
            try{
                imageSet=savedInstanceState.getBoolean("imageset");
                titleSet=savedInstanceState.getBoolean("titleset");
                txtTitle=(TextView)findViewById(R.id.txtTitle);
                imageLoader = AppController.getInstance().getImageLoader();
                if (imageLoader == null)
                    imageLoader = AppController.getInstance().getImageLoader();
                img = (NetworkImageView) findViewById(R.id.imgBook);
                txtTitle.setText(savedInstanceState.getString("title"));
                img.setImageUrl(image, imageLoader);
                details=savedInstanceState.getString("det");
            summary=savedInstanceState.getString("sum");
            try{pr[1]=Integer.parseInt(savedInstanceState.getString("p1"));}catch (Exception ignored){}
            try{pr[2]=Integer.parseInt(savedInstanceState.getString("p2"));}catch (Exception ignored){}
            try{pr[3]=Integer.parseInt(savedInstanceState.getString("p3"));}catch (Exception ignored){}
            try{pr[4]=Integer.parseInt(savedInstanceState.getString("p4"));}catch (Exception ignored){}
            try{pr[5]=Integer.parseInt(savedInstanceState.getString("p5"));}catch (Exception ignored){}
            try{pr[6]=Integer.parseInt(savedInstanceState.getString("p6"));}catch (Exception ignored){}
            try{pr[7]=Integer.parseInt(savedInstanceState.getString("p7"));}catch (Exception ignored){}
            pr[0]=pr[1];
            for(int i=1;i<7;i++)
            {
                if(pr[i]!=0 && pr[0]>pr[i])pr[0]=pr[i];
            }
            bd.putIntArray("pr", pr);
            bd1.putString("det", details);
            bd1.putString("sum",summary);
            PlaceholderFragment.setInstance(bd);
            DetailsFragment.setInstance(bd1);}catch(Exception ignored){}
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stores, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_stores:sendEmail();
                return true;
            case R.id.action_refresh: new GetPricesTask().execute(0);
                return true;
            case R.id.refresh_details: new GetPricesTask().execute(8);
                return true;
            case R.id.refresh_summary: new GetPricesTask().execute(8);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putBoolean("imageset",imageSet);
        icicle.putBoolean("titleset",titleSet);
        if(imageSet)icicle.putString("img",image);
        if(titleSet)icicle.putString("title",title);
        icicle.putString("p1",pr[1]+"");
        icicle.putString("p2",pr[2]+"");
        icicle.putString("p3",pr[3]+"");
        icicle.putString("p4",pr[4]+"");
        icicle.putString("p5",pr[5]+"");
        icicle.putString("p6",pr[6]+"");
        icicle.putString("p7",pr[7]+"");
        icicle.putString("det",details);
        icicle.putString("sum",summary);
    }
    private boolean isInternet() {ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        return i.isAvailable();
    }
    public static Bundle bd=new Bundle();
    public static Bundle bd1=new Bundle();
    String summary="", details=""; String image = "", title = "";
    TextView txtTitle;
    ImageLoader imageLoader;NetworkImageView img;
    Boolean imageSet = false, titleSet = false;
    int pr[]=new int[8];
    public String s1 = "", s2 = "", s3 = "", s4 = "", s5 = "", s6 = "", s7 = "",
            h1 = "http://www.flipkart.com/search?q=" + MainActivity.isbn,
            h2 = "http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Dstripbooks&field-keywords=" + MainActivity.isbn ,
            h3 = "http://www.infibeam.com/search.jsp?storeName=Books&query=" + MainActivity.isbn ,
            h4 = "http://books.rediff.com/book/" + MainActivity.isbn ,
            h5 = "http://www.crossword.in/home/search?q=" + MainActivity.isbn ,
            h6 = "http://www.uread.com/search-books/" + MainActivity.isbn ,
            h7 = "http://www.landmarkonthenet.com/books/" + MainActivity.isbn ;
    public void btnStore(View v)
    {
        String url="http://www.flipkart.com";
        String tg=(String)v.getTag();
        switch(tg)
        {
            case "flipkart":url=h1;
                break;
            case "amazon":url=h2;
                break;
            case "infibeam":url=h3;
                break;
            case "rediff":url=h4;
                break;
            case "crossword":url=h5;
                break;
            case "uread":url=h6;
                break;
            case "landmark":url=h7;
                break;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
    public void refresh(View v)
    {
        String tg=(String)v.getTag();
        switch(tg)
        {
            case "flipkart":new GetPricesTask().execute(1);
                break;
            case "amazon":new GetPricesTask().execute(2);
                break;
            case "infibeam":new GetPricesTask().execute(3);
                break;
            case "rediff":new GetPricesTask().execute(4);
                break;
            case "crossword":new GetPricesTask().execute(5);
                break;
            case "uread":new GetPricesTask().execute(6);
                break;
            case "landmark":new GetPricesTask().execute(7);
                break;
        }
    }
    private class GetPricesTask extends AsyncTask<Integer, Integer, Integer> {

        private final ProgressDialog dialog = new ProgressDialog(Stores.this);
        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }
        @Override
        protected Integer doInBackground(Integer... params) {
            int done = 0;
            if(params[0]==0){
            Flipkart();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            Amazon();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            if(MainActivity.sPrice.equals("")){
            Infibeam();}
            else {s3=MainActivity.sPrice;h3=MainActivity.link;pr[3]=(int)Double.parseDouble(s3);}
            if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            Rediff();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            Crossword();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            uRead();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            Landmark();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));
            duRead();if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 8) * 100));}
            else{
                switch(params[0])
                {
                    case 1:Flipkart();
                        break;
                    case 2:Amazon();
                        break;
                    case 3:Infibeam();
                        break;
                    case 4:Rediff();
                        break;
                    case 5:Crossword();
                        break;
                    case 6:uRead();
                        break;
                    case 7:Landmark();
                        break;
                    case 8:duRead();
                        break;
                }
            }
            return done;
        }
        @Override
        protected void onPreExecute(){
            txtTitle=(TextView)findViewById(R.id.txtTitle);
            imageLoader = AppController.getInstance().getImageLoader();
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            img = (NetworkImageView) findViewById(R.id.imgBook);
            dialog.setMessage("Getting prices from the server. Please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bd.putIntArray("pr", pr);
                    bd1.putString("det", details);
                    bd1.putString("sum",summary);
                    PlaceholderFragment.setInstance(bd);
                    DetailsFragment.setInstance(bd1);
                    dialog.dismiss();cancel(true);
                }
            });
            dialog.show();
        }
        @Override
        protected void onPostExecute(Integer result) {
            pr[0]=pr[1];
            for(int i=1;i<7;i++)
            {
                if(pr[i]!=0 && pr[0]>pr[i])pr[0]=pr[i];
            }
            if (dialog.isShowing()) dialog.dismiss();
            bd.putIntArray("pr", pr);
            bd1.putString("det", details);
            bd1.putString("sum",summary);
            PlaceholderFragment.setInstance(bd);
            DetailsFragment.setInstance(bd1);
        }
        private void Flipkart() {
            HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/f696659f-503e-40c4-97ae-5d38f328fb18/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h1);
            ob.fetchJSON();
            while (ob.parsingComplete);
            try {
                JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h1=ob1.getString("pageUrl");
                try{pr[1] = ob2.getInt("price");
                    s1=""+pr[1];
                }catch(Exception e){System.out.println("Error1" + e.toString());}
                if(!titleSet)
                {
                    try{title = ob2.getString("title");titleSet=true;
                       runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                // new DownloadImageTask(img).execute(ob2.getJSONObject(0).getString("image"));
            } catch (Exception e) {
                System.out.println("Error1" + e.toString()+"\n"+ob.result);
            }
        }
        private void Amazon() {

                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/860b95af-002d-44b1-b9f2-41b550bd31c5/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h2);
                ob.fetchJSON();
                while (ob.parsingComplete);
            try{    JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h2=ob2.getString("title");
                try{pr[2] = ob2.getInt("price");s2=""+pr[2];
                }catch(Exception ignored){}
                if(!titleSet)
                {
                    try{title = ob2.getString("title/_title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{image = ob2.getString("image");imageSet=true;
                      runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });
                    }catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error2" + e.toString()+"\n"+ob.result);
            }
        }
        private void Infibeam() {

                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/a5a94c0d-f397-4352-af5e-725dc48c2e75/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h3);
                ob.fetchJSON();
                while (ob.parsingComplete);
            try{    JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h3=ob1.getString("pageUrl");
                try{s3 = ob2.getJSONArray("price").getString(0).substring(4);
                  pr[3]=Integer.parseInt(s3);
                }catch(Exception e){System.out.println("Error3" + e.toString()+"\n"+ob.result);}
                if(!titleSet)
                {
                    try{title = ob2.getString("title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{ image = ob2.getString("image");imageSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });}catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error3" + e.toString()+"\n"+ob.result);
            }
        }
        private void Rediff() {


                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/7be69692-cb88-478d-bdd1-6469a7344302/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h4);
                ob.fetchJSON();
                while (ob.parsingComplete);
             try{   JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h4=ob2.getString("title");
                try{pr[4] = ob2.getInt("price");
                    s4=""+pr[4];
                }catch(Exception e){}
                if(!titleSet)
                {
                    try{ title = ob2.getString("title/_title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{ image = ob2.getString("image");imageSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });}catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error4" + e.toString()+"\n"+ob.result);
            }
        }
        private void Crossword() {


                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/64f906f0-2cf4-4f2f-b97c-745a7fbe0567/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h5);
                ob.fetchJSON();
                while (ob.parsingComplete);
             try{   JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h5=ob2.getString("title");
                try{pr[5] = ob2.getInt("price");s5=""+pr[5];
                   
                }catch(Exception e){}
                if(!titleSet)
                {
                    try{ title = ob2.getString("title/_title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{ image = ob2.getString("image");imageSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });}catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error5" + e.toString()+"\n"+ob.result);
            }
        }
        private void uRead() {
                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/698c07bf-7190-4583-bbe6-1914dab15ac6/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h6);
                ob.fetchJSON();
                while (ob.parsingComplete);
            try{    JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h6=ob1.getString("pageUrl");
                try{ s6 = ob2.getString("price").substring(1);pr[6]=Integer.parseInt(s6);
                     }catch(Exception e){}
                if(!titleSet)
                {
                    try{ title = ob2.getString("title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{ image = ob2.getString("image");imageSet=true;
                        image=image.replace("images200","mainimages");
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });}catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error6" + e.toString()+"\n"+ob.result);
            }
        }
        private void Landmark() {

                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/d9eded32-4f19-4018-acfd-4823ec8b7642/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h7);
                ob.fetchJSON();
                while (ob.parsingComplete);
            try{    JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                h7=ob1.getString("pageUrl");
                try{ s7 = ob2.getString("price").substring(3);pr[7]=Integer.parseInt(s7);

                }catch(Exception e){}
                if(!titleSet)
                {
                    try{ title = ob2.getString("title");titleSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {txtTitle.setText("Title: "+title);}
                        });}catch(Exception e){titleSet=false;}
                }
                if(!imageSet)
                {
                    try{ image = ob2.getString("image");imageSet=true;
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {img.setImageUrl(image, imageLoader);}
                        });}catch(Exception e){imageSet=false;}
                }
            } catch (Exception e) {
                System.out.println("Error7" + e.toString()+"\n"+ob.result);
            }
        }
        private void duRead() {

                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/585cb4d0-b06f-480c-93de-abe091fc6e53/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h6);
                ob.fetchJSON();
                while (ob.parsingComplete);
             try{   JSONObject ob1 = new JSONObject(ob.result);
                JSONObject ob2 = ob1.getJSONArray("results").getJSONObject(0);
                try {
                    JSONArray ob3 = ob2.getJSONArray("summary");
                    for (int i = 0; i < ob3.length(); i++)
                        summary += ob3.getString(i) + "\n";
                }catch(Exception e){System.out.println(e.toString()+"\n"+ob.result);}
                try{ JSONArray ob3 = ob2.getJSONArray("details");
                    for (int i = 0; i < ob3.length(); i+=2)
                    {   details+= ob3.getString(i)+" "+ob3.getString(i+1)+"\n" ;}
                }catch(Exception e){System.out.println(e.toString()+"\n"+ob.result);}
            } catch (Exception e) {
                System.out.println("Error8" + e.toString()+"\n"+ob.result);
            }
        }
    }
    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"srujanjh@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BookMyBook");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I liked the app. Write your text here...");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Stores.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public static class PlaceholderFragment extends Fragment {

        public static void setInstance(Bundle take){
            try{TextView p[]=new TextView[7];
            p[0] = (TextView) view.findViewById(R.id.txtFlipkart);
            p[1] = (TextView) view.findViewById(R.id.txtAmazon);
            p[2] = (TextView) view.findViewById(R.id.txtInfibeam);
            p[3] = (TextView) view.findViewById(R.id.txtRediff);
            p[4] = (TextView) view.findViewById(R.id.txtCrossword);
            p[5] = (TextView) view.findViewById(R.id.txtuRead);
            p[6] = (TextView) view.findViewById(R.id.txtLandmark);
            int pr[]=take.getIntArray("pr");
            for(int i=0;i<7;i++)
            {
               if(pr[i+1]!=0) p[i].setText(""+pr[i+1]);
                if(pr[0]!=0 && pr[i+1]==pr[0])p[i].setTextColor(Color.GREEN);
            }
                ImageButton iB[]=new ImageButton[7];
                iB[0]=(ImageButton)view.findViewById(R.id.iB1);
                iB[1]=(ImageButton)view.findViewById(R.id.iB2);
                iB[2]=(ImageButton)view.findViewById(R.id.iB3);
                iB[3]=(ImageButton)view.findViewById(R.id.iB4);
                iB[4]=(ImageButton)view.findViewById(R.id.iB5);
                iB[5]=(ImageButton)view.findViewById(R.id.iB6);
                iB[6]=(ImageButton)view.findViewById(R.id.iB7);
                if(pr[1]==0)iB[0].setVisibility(View.VISIBLE);
                else iB[0].setVisibility(View.INVISIBLE);
                for(int i=1;i<7;i++)
                {if(pr[i+1]==0)iB[i].setVisibility(View.VISIBLE);
                    else iB[i].setVisibility(View.INVISIBLE);
                }}catch(Exception ignored){}
        }
        public static PlaceholderFragment newInstance(CharSequence title, int indicatorColor,int dividerColor) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(KEY_TITLE, title);
            bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
            bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        static View view;
        public PlaceholderFragment() {

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stores, container, false);
            view=rootView;
            setInstance(bd);
            return rootView;
        }
        }
    public static class DetailsFragment extends Fragment {
        public static void setInstance(Bundle take){
            try{
                TextView det=(TextView)view.findViewById(R.id.text_details);
                det.setText(take.getString("det"));
            }catch(Exception e){System.out.println(e.toString());}
        }
        public static DetailsFragment newInstance(CharSequence title, int indicatorColor,int dividerColor) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(KEY_TITLE, title);
            bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
            bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        static View view;
        public DetailsFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            view=rootView;
            setInstance(bd1);
            return rootView;
        }
    }
    public static class SummaryFragment extends Fragment {
        public static void setInstance(Bundle take){
            TextView sum = (TextView) view.findViewById(R.id.txtSummary);
            sum.setText(take.getString("sum"));
        }
        public static SummaryFragment newInstance(CharSequence title, int indicatorColor,int dividerColor) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(KEY_TITLE, title);
            bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
            bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);
            SummaryFragment fragment = new SummaryFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
        static View view;
        public SummaryFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
            view=rootView;
            setInstance(bd1);
            return rootView;
        }
    }

}
