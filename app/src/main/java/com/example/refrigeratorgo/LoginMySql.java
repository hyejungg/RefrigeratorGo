package com.example.refrigeratorgo;

import android.os.Handler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginMySql extends Thread { //차후에 사용 가능, 지우지 말고 놔두기
    Handler mHandler;
    String userId=null, url=null;
    String login_url = "http://zoooz0616.dothome.co.kr/SelectBasicInfo.php?userId=";

    public LoginMySql(String id){
        mHandler = new Handler();
        userId=id;
        url=login_url+userId;
    }

    @Override
    public void run() {
        super.run();
            StringBuilder sb = new StringBuilder();
            try {
                URL php_url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)php_url.openConnection();
                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            sb.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            show(sb.toString());
        }

    void show(final String result){
        mHandler.post(new Runnable(){
            @Override
            public void run() {
               new LoginActivity().getRfgName(result);
            }
        });
    }
}
