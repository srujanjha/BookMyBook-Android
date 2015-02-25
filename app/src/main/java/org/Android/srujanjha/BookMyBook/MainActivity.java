package org.android.srujanjha.bookmybook;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public static String isbn="", srchTxt="",sPrice="",link="";
    private ListView listView;
    private String ar[];
    private CustomListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ar=loadArray(getApplicationContext());
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,ar);
        AutoCompleteTextView txtS = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        int txtsize=22*size.x/240;
        if(txtsize>25)txtsize=25;
        txtS.setTextSize(TypedValue.COMPLEX_UNIT_SP,txtsize );
        txtS.setAdapter(adapter);
        txtS.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            btnSearch(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }
    private boolean isInternet()    {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }
    private boolean checkisbn(long n,int l)    {
        long m = n;
        m = m / 10;
        int sum = 0;
        if (l == 10)
        {
            for (int i = 1; i < 10; i++)
            {
                sum = sum + (int)((10-i) * (m % 10));
                m /= 10;
            }
            sum = sum % 11;
        }
        else if (l == 13)
        {
            for (int i = 1; i < 13; i++)
            {
                if(i%2==0)
                    sum = sum + (int)(1 * (m % 10));
                else sum = sum + (int)(3 * (m % 10));
                m /= 10;
            }
            sum =10- sum % 10;
        }
        if (sum == (n % 10))
            return true;
        else return false;
    }
    public boolean saveArray(String[] array, Context mContext) {
        try{
            SharedPreferences prefs = mContext.getSharedPreferences("BookMyBook", 0);
        SharedPreferences.Editor editor = prefs.edit();
        boolean flag=true;
        for(int i=0;i<array.length;i++) {
            if(array[i].equals(srchTxt))flag=false;
            editor.putString("History" + "_" + i, array[i]);
        }
            int sz=array.length;
        if(flag) {
            editor.putString("History" + "_" + array.length, srchTxt);sz++;
        }
        editor.putInt("History" +"_size", sz);
        return editor.commit();}
        catch(Exception e){return false;}
    }
    public String[] loadArray(Context mContext) {
       try {
           SharedPreferences prefs = mContext.getSharedPreferences("BookMyBook", 0);
           int size = prefs.getInt("History" + "_size", 0);
           String array[] = new String[size];
           for (int i = 0; i < size; i++) {
               array[i] = prefs.getString("History" + "_" + i, null);
           }
           return array;
       }catch(Exception e){remove(mContext);}return new String[0];
    }
    private void remove(Context mContext)
    {
        mContext.getSharedPreferences("BookMyBook", 0).edit().clear().commit();System.out.println("Removed");
    }
    public void btnSearch(View v) {
        if(isInternet()) {
            EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
            srchTxt = txtSearch.getText().toString();
            saveArray(ar,getApplicationContext());
            try {
                if (checkisbn(Long.parseLong(srchTxt), srchTxt.length())) {
                    Intent i = new Intent(MainActivity.this, Stores.class);
                    isbn = srchTxt;
                    startActivity(i);
                } else {
                    new GetPricesTask().execute();
                }
            } catch (Exception e) {
                new GetPricesTask().execute();
            }
        }else Toast.makeText(getApplicationContext(),"Sorry, but you are not connected to Internet.",Toast.LENGTH_LONG).show();
    }
    private class GetPricesTask extends AsyncTask<Void, Integer, Void> {
        List<Search> list=new ArrayList<Search>();
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        @Override
        protected Void doInBackground(Void... params) {
            sinfibeam();
            return null;
        }
        private int suread()
        {
            int done = 0;

            String h1="http://www.uread.com/search-books/"+srchTxt;
            try {
                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/897657b3-62f2-41d1-8a61-a5def1169d80/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h1);
                ob.fetchJSON();
                while (ob.parsingComplete);
                JSONObject ob1 = new JSONObject(ob.result);
                JSONArray ob2 = ob1.getJSONArray("results");
                int count=ob2.length();
                for(int i=0;i<count;i++)
                {
                    Search sr=new Search();
                    try {
                        JSONObject ob3 = ob2.getJSONObject(i);
                        sr.setTitle(ob3.getString("title/_text"));
                        sr.setAuthor(ob3.getString("author/_text"));
                        sr.setPrice(ob3.getString("mrp").substring(1));
                        sr.setLink(ob3.getString("title"));
                        sr.setPublisher(ob3.getString("publisher/_text"));
                        sr.setImage(ob3.getString("image"));

                    }catch(Exception e){}
                    list.add(sr);
                    publishProgress((i+1)*100/count);
                }
            }catch(Exception e){System.out.println(e.toString());}
            if (isCancelled()) return done;done++;publishProgress((int) ((done / (float) 7) * 100));
            return done;
        }
        private void sinfibeam()
        {
            String h2="http://www.infibeam.com/Books/search?q="+srchTxt;
            try {
                HandlePriceJSON ob = new HandlePriceJSON("https://api.import.io/store/data/152b6c81-0417-42fe-a150-55ff8ff6e567/_query?_user=9af12242-5eff-4b79-990f-4d42fd017332&_apikey=t8y608aARzHmiuYn5ex4UmDCasxkmkGVT1OgOBvGo5WVzglDPNjqy1K0yEXpvUM1mX%2Bbr2K7lXoEHJp5h%2FpVIQ%3D%3D", h2);
                ob.fetchJSON();
                while (ob.parsingComplete);
                JSONObject ob1 = new JSONObject(ob.result);
                JSONArray ob2 = ob1.getJSONArray("results");
                int count=ob2.length();
                for(int i=0;i<count-1;i++)
                {
                    Search sr=new Search();
                    try {
                        JSONObject ob3 = ob2.getJSONObject(i);
                        sr.setTitle(ob3.getString("title/_text"));
                        sr.setAuthor(ob3.getString("author/_text"));
                        sr.setPrice("" + ob3.getDouble("mrp"));
                        sr.setSprice(""+ob3.getJSONArray("iprice").getDouble(0));
                        sr.setLink(ob3.getString("title"));
                        sr.setPublisher("");
                        sr.setImage(ob3.getString("image"));
                        String ttl=(ob3.getString("title"));
                        sr.setLink(ttl);
                        try {
                            int k =ttl.indexOf(".html");
                            ttl = ttl.substring(k-13,k);
                            long kd=Long.parseLong(ttl);
                            if(checkisbn(kd,ttl.length()))
                            sr.setIsbn(ttl);

                        }catch(Exception e){}
                    }catch(Exception e){System.out.println(e.toString());}
                    if(!sr.getIsbn().equals(""))
                    list.add(sr);
                }
            }catch(Exception e){System.out.println(e.toString());}
        }
        @Override
        protected void onPreExecute(){
            listView=(ListView)findViewById(R.id.list);
            dialog.setMessage("Finding books related to your search. Please wait.");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) dialog.dismiss();
            if(list.size()!=0){
            adapter = new CustomListAdapter(MainActivity.this, list);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    isbn=list.get(position).getIsbn();
                    sPrice=list.get(position).getSprice();
                    if(sPrice==null)sPrice="";
                    link=list.get(position).getLink();
                    Intent i=new Intent(MainActivity.this,Stores.class);
                    startActivity(i);
                }
            });}
            else Toast.makeText(getApplicationContext(),"No results available based on your search",Toast.LENGTH_LONG).show();
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
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_feedback:sendEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
