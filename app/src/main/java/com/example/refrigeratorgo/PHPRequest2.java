package com.example.refrigeratorgo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PHPRequest2 {
    public String PHPtest(String food_name, String memo, String food_date, int food_index) { //String img_url
        try {
            String post_data = "foodName=" + food_name + "&memo=" + memo + "&foodDate=" + food_date + "&foodIndex=" + food_index; //전송 데이터 https://twinw.tistory.com/29
            String url = "http://zoooz0616.dothome.co.kr/UpdateFood.php?food";
            URL php_url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)php_url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
            os.write(post_data.getBytes("UTF-8"));
            os.flush();
            os.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result; // success : 1, fail : -1
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }
    private String readStream(InputStream is) throws IOException { //** 수정 필요없이 그대로 복붙하면 됨
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = null;

        while((line = br.readLine()) != null)
            sb.append(line);

        br.close();
        return sb.toString();
    }
}

