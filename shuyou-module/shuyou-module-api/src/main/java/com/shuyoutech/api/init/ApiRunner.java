package com.shuyoutech.api.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author YangChao
 * @date 2025-08-10 15:13
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRunner implements CommandLineRunner {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.get(APPLICATION_JSON_VALUE);

    public static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder() //
            .connectTimeout(3, TimeUnit.MINUTES) // 3分
            .readTimeout(5, TimeUnit.MINUTES) // 5分
            .writeTimeout(5, TimeUnit.MINUTES) // 5分
            .build();

    @Override
    public void run(String... args) {

    }


}
