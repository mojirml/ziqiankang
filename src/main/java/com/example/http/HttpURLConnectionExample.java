package com.example.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        HttpURLConnectionExample.sendRequest("万用电炉","c://");
    }

    public static boolean sendRequest(String name,String path){
        try {
            HttpURLConnectionExample http = new HttpURLConnectionExample();
            String encoderUrl = http.sendGet(name);
            http.sendPost(name,encoderUrl,path);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 第一步：获取对应网页地址
     * @param name
     * @throws Exception
     */
    private String sendGet(String name) throws Exception {
        try {
            String url = "https://hs.e-to-china.com.cn/classify/clean.php?content=";
            String encode = URLEncoder.encode(name);
            url += encode;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //默认值我GET
            con.setRequestMethod("GET");
            //添加请求头
            con.setRequestProperty("User-Agent", USER_AGENT);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int ip = new Random().nextInt(254) + 1;
                if(i == 0){
                    ip = new Random().nextInt(190) + 10;
                }
                builder.append(ip);
                if(i != 3){
                    builder.append(".");
                }
            }
            String ipStr = builder.toString();
            System.out.println("ip is:"+ipStr);
            con.setRequestProperty("X-Forwarded-For",ipStr);
            int responseCode = con.getResponseCode();
            System.out.println("\n Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String result = response.toString();
            System.out.println(result);
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("get or post error");
        }
    }

    // HTTP POST请求
    private void sendPost(String name,String encoderUrl,String path) throws Exception {
        String url = "https://hs.e-to-china.com.cn/gljy-"+encoderUrl+".html?init=1";
        URL obj = new URL(url);
        System.out.println(url.substring(0,url.length()-7));
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        //添加请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("sec-ch-ua", "\" Not;A Brand\";v=\"91\", \"Google Chrome\";v=\"99\", \"Chromium\";v=\"99\"'");
        con.setRequestProperty("sec-ch-ua-mobile", "?0");
        con.setRequestProperty("sec-ch-ua-platform", "macOS");
        con.setRequestProperty("Upgrade-Insecure-Requests", "2");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int ip = new Random().nextInt(254) + 1;
            if(i == 0){
                ip = new Random().nextInt(190) + 10;
            }
            builder.append(ip);
            if(i != 3){
                builder.append(".");
            }
        }
        String ipStr = builder.toString();
        con.setRequestProperty("X-Forwarded-For",ipStr);
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        con.setRequestProperty("Sec-Fetch-Site", "same-origin");
        con.setRequestProperty("Sec-Fetch-Mode", "navigate");
        con.setRequestProperty("Sec-Fetch-Dest", "document");
        con.setRequestProperty("Referer", url.substring(0,url.length()-7));
        con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        String lastTimeTime = 100 * System.currentTimeMillis() / 100000+"; ";
        String currentTime = System.currentTimeMillis() / 1000+"; ";
        String Hm_lvt1 = "Hm_lvt_48ffa8976c86b5a43e567ab36d723ba0=" + lastTimeTime;
        String Hm_lvt2 = "Hm_lvt_752ea6c6b3380d2c2a2bcb8c150ef39e=" + lastTimeTime;
        String Hm_lpvt1 = "Hm_lpvt_48ffa8976c86b5a43e567ab36d723ba0=" + currentTime;
        String Hm_lpvt2 = "Hm_lpvt_752ea6c6b3380d2c2a2bcb8c150ef39e=" + currentTime;
        System.out.println(Hm_lpvt2.substring(0,Hm_lpvt2.length()-2));
        con.setRequestProperty("Cookie", "PHPSESSID=52frsa913cevnk5gtk3o12313himi1; "
                + Hm_lvt1
                + Hm_lvt2
                + Hm_lpvt1
                + Hm_lpvt2.substring(0,Hm_lpvt2.length()-2));

        //发送Post请求
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\n Sending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        String result = response.toString();
        in.close();
        if(responseCode == 200){
            File file = new File(path+name+".html");
            System.out.println(file.getAbsolutePath());
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            try (BufferedWriter bufferedWriter = new BufferedWriter(write)) {
                bufferedWriter.write(result);
                bufferedWriter.flush();
            }
        }
        //打印结果
    }

}
