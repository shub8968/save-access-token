package com.redcarpetup.saveaccesstoken.util;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import java.util.Map;
public class GetCodeUtil {
    public static String getCode(APIGatewayProxyRequestEvent input) {
        Map<String, String> inputParams = input.getQueryStringParameters();
        for (Map.Entry<String, String> entry : inputParams.entrySet()) {
            if (entry.getKey().equals("code")) {
                String code = entry.getValue();
                if (code != null && !code.isEmpty()) {
                    return code;
                }
            }
        }
        return "Success";
    }
}
