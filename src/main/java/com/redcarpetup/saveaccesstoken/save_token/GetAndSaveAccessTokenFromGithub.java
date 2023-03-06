package com.redcarpetup.saveaccesstoken.save_token;


import com.redcarpetup.saveaccesstoken.constant.AccessTokenConstant;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetAndSaveAccessTokenFromGithub {

    public String getAndSaveAccessToken(String code) throws IOException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AccessTokenConstant.GITHUB_URI+"?code="+code
                        +"&client_secret="+ AccessTokenConstant.CLIENT_SECRET+"?client_id="+ AccessTokenConstant.CLIENT_ID
                        +"redirect_uri="+ AccessTokenConstant.REDIRECT_URI))
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response  = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

}
