package org.android.srujanjha.bookmybook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Srujan Jha on 28-12-2014.
 */
public class HandlePriceJSON {
    private String urlString = null,aurl=null;
    public String result="";
    public volatile boolean parsingComplete = true;
    public HandlePriceJSON(String url,String aurl){
        this.urlString = url;
        this.aurl=aurl;
    }
    public void fetchJSON(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    if(aurl!=null)
                    {   OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write("{ \"input\": { \"webpage/url\": \""+aurl+"\"}}");
                        wr.flush();
                    }
                    BufferedReader reader ;
                    try {
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            }catch(Exception e){
                                reader=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                            }
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    result=sb.toString();
                    parsingComplete = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
