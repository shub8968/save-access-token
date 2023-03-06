package com.redcarpetup.saveaccesstoken.code_event;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.redcarpetup.saveaccesstoken.save_token.GetAndSaveAccessTokenFromGithub;
import com.redcarpetup.saveaccesstoken.util.GetCodeUtil;

import java.io.IOException;

public class GetCodeEvent implements RequestHandler<APIGatewayProxyRequestEvent, String> {
        @Override
        public String handleRequest(APIGatewayProxyRequestEvent input, Context context) {
            GetAndSaveAccessTokenFromGithub getAndSaveAccessTokenFromGithub = new GetAndSaveAccessTokenFromGithub();
            try {
                getAndSaveAccessTokenFromGithub.getAndSaveAccessToken(GetCodeUtil.getCode(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "";
        }
    }
