package com.redcarpetup.saveaccesstoken.save_token;


import com.redcarpetup.saveaccesstoken.constant.AccessTokenConstant;
import com.redcarpetup.saveaccesstoken.dynamodb.AccessToken;
import com.redcarpetup.saveaccesstoken.dynamodb.GitHubError;
import com.redcarpetup.saveaccesstoken.dynamodb.SaveToDynamoDB;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class GetAndSaveAccessTokenFromGithub {

    public String getAndSaveAccessToken(String code) throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AccessTokenConstant.GITHUB_URI + "?code=" + code
                        + "&client_secret=" + AccessTokenConstant.CLIENT_SECRET + "&client_id=" + AccessTokenConstant.CLIENT_ID
                        + "&redirect_uri=" + AccessTokenConstant.REDIRECT_URI))
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        String status = null;
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            status = parseKeyValueString(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        return status;
    }

    private static String parseKeyValueString(String input) throws URISyntaxException {
        Map<String, String> keyValueMap = new HashMap<>();
        String[] keyValuePairs = input.split("&");
        for (String pair : keyValuePairs) {
            System.out.println("PAIR "+pair);
            String[] keyValue = pair.split("=");
            if(keyValue.length==2)
            keyValueMap.put(keyValue[0], keyValue[1]);
        }
       if(keyValueMap.containsKey("access_token")){
           mapToAccessToken(keyValueMap);
       }else{
           mapToGitHubError(keyValueMap);
       }
        return "Authorization Successful";
    }
    private static AccessToken mapToAccessToken(Map<String, String> keyValueMap) throws URISyntaxException {
        AccessToken token = new AccessToken();
        token.setAccessToken(keyValueMap.getOrDefault("access_token", ""));
        token.setExpiresIn(Integer.parseInt(keyValueMap.getOrDefault("expires_in", "")));
        token.setRefreshToken(keyValueMap.getOrDefault("refresh_token", ""));
        token.setRefreshTokenExpiresIn(Integer.parseInt(keyValueMap.getOrDefault("refresh_token_expires_in", "")));
        token.setScope(keyValueMap.getOrDefault("scope", ""));
        token.setTokenType(keyValueMap.getOrDefault("token_type", ""));
        SaveToDynamoDB saveToDynamoDB = new SaveToDynamoDB();
        saveToDynamoDB.saveAccessTokenToDynamoDB(token);
        System.out.println("ACCESS TOKEN SAVED");
        return token;
    }
    private static GitHubError mapToGitHubError(Map<String, String> keyValueMap) {
        GitHubError gitHubError = new GitHubError();
        gitHubError.setError(keyValueMap.get("error"));
        gitHubError.setErrorDescription(keyValueMap.get("error_description").replace('+', ' '));
        gitHubError.setErrorUri(keyValueMap.get("error_uri"));
        SaveToDynamoDB saveToDynamoDB = new SaveToDynamoDB();
        saveToDynamoDB.saveGitHubErrorToDynamoDB(gitHubError);
        return gitHubError;
    }
}
