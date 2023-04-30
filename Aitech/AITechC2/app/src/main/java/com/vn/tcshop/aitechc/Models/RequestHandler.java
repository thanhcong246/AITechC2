package com.vn.tcshop.aitechc.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {
    /**
    Đây là một lớp Java có tên là RequestHandler để xử lý các yêu cầu liên quan đến HTTP như gửi yêu cầu
    POST và GET đến một URL cụ thể.
    Lớp này có các phương thức sau:
        sendPostRequest: Gửi yêu cầu POST đến một URL cụ thể với các thông tin được đưa vào dưới dạng HashMap.
                Phương thức này trả về một chuỗi kết quả là phản hồi từ máy chủ.
        sendGetRequest: Gửi yêu cầu GET đến một URL cụ thể. Phương thức này trả về một chuỗi kết quả là phản hồi
                từ máy chủ.
        getPostDataString: Phương thức này được sử dụng trong phương thức sendPostRequest để chuyển đổi các
                thông tin đưa vào dưới dạng HashMap thành chuỗi dữ liệu POST được đọc được bởi máy chủ.

    Lớp RequestHandler được sử dụng trong hàm getUserInfo để gửi yêu cầu POST đến một URL cụ thể với thông tin
    người dùng được đưa vào dưới dạng HashMap. Sau đó, phản hồi từ máy chủ được đọc bằng BufferedReader và được
    xử lý để lấy thông tin người dùng và chuyển tiếp sang màn hình ProfileShowActivity.
     */
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        URL url;

        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
