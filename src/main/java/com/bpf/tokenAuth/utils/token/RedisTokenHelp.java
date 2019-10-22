package com.bpf.tokenAuth.utils.token;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bpf.tokenAuth.utils.RedisMap;

@Component
public class RedisTokenHelp implements TokenHelper {

    @Override
    public TokenModel create(Integer id) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenModel mode = new TokenModel(id, token);
        RedisMap.set(String.valueOf(id), token);
        return mode;
    }

    @Override
    public boolean check(TokenModel model) {
        boolean result = false;
        if(model != null) {
            String userId = model.getUserId().toString();
            String token = model.getToken();
            String authenticatedToken = RedisMap.get(userId);
            if(authenticatedToken != null && authenticatedToken.equals(token)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public TokenModel get(String authStr) {
        TokenModel model = null;
        if(StringUtils.isNotEmpty(authStr)) {
            String[] modelArr = authStr.split("_");
            if(modelArr.length == 2) {
                int userId = Integer.parseInt(modelArr[0]);
                String token = modelArr[1];
                model = new TokenModel(userId, token);
            }
        }
        return model;
    }

    @Override
    public boolean delete(Integer id) {
    	return RedisMap.delete(id == null ? null : String.valueOf(id));
    }

}
