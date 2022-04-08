package com.example.samplehttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String urlStr = editText.getText().toString();
                //final : 현재 영역 안에서는 전역변수 처럼 사용가능

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      request(urlStr);
                    }
                }).start();
            }
        });
    }

    public void request(String urlStr){
        StringBuilder output = new StringBuilder();
        try{
            URL url = new URL(urlStr);
            //url객체 생성

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //HttpURLConnection객체 생성

            if(conn != null){
                conn.setConnectTimeout(10000); //연결 대기 시간 설정
                conn.setRequestMethod("GET"); //메소드 방식 설정
                conn.setDoInput(true); //객체의 입력 가능 여부 결정

                int resCode = conn.getResponseCode();
                //요청 주소의 페이지코드가 있는경우 : 코드 반환
                // 없는 경우 : HTTP_NOT_FOUND 코드 반환
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //입력데이터를 받기 위한 Reader객체 생성
                String line = null;

                while(true){
                    line = reader.readLine();
                    //스트림에서 한 줄씩 읽어들이는 메서드(BufferdBeader클래스 안에 정의 되어 있음)
                    //-> HttpURLConnection객체 스트림을 이 클래스의 객체로 생성 후 처리

                    if(line == null){
                        break;
                    }

                    output.append(line + "\n");
                }

                reader.close(); //닫기
                conn.disconnect(); //연결을 끊음
            }
        }catch (Exception e){
            println("예외 발생 -> " + e.toString());
        }

        println("응답-> " + output.toString());
    }

    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });
    }
}