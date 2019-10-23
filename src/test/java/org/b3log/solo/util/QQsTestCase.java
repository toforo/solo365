/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.util;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.json.JSONObject;
import org.testng.annotations.Test;

/**
 * {@link org.b3log.solo.util.QQs} test case.
 * 
 * @author zhuangyilian
 */
public final class QQsTestCase {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QQs.class);
    
    @Test
    public void qqAuth() {
        final String appKey = "";
        final String appSecret = "";
        final String code = "";
        
        // token
        final JSONObject tokenParams = new JSONObject();
        tokenParams.put("grant_type", "authorization_code");
        tokenParams.put("client_id", appKey);
        tokenParams.put("client_secret", appSecret);
        tokenParams.put("code", code);
        tokenParams.put("redirect_uri", "https://www.jingqueyimu.com/oauth/qq");
        final HttpResponse tokenRes = HttpRequest.get(buildUrl("https://graph.qq.com/oauth2.0/token", tokenParams)).trustAllCerts(true).
                connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).send();
        LOGGER.log(Level.INFO, tokenRes.bodyText());
        JSONObject tokenData = urlDataToJson(tokenRes.bodyText());
        final String token = tokenData.optString("access_token");
        
        // openId
        final JSONObject params = new JSONObject();
        params.put("access_token", token);
        final HttpResponse openIdRes = HttpRequest.get(buildUrl("https://graph.qq.com/oauth2.0/me", params)).trustAllCerts(true).
                connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).send();
        LOGGER.log(Level.INFO, openIdRes.bodyText());
        JSONObject openIdData = jsonpToJson(openIdRes.bodyText());
        String openId = openIdData.optString("openid");
        
        // userInfo
        final JSONObject userInfoParams = new JSONObject();
        userInfoParams.put("access_token", token);
        userInfoParams.put("oauth_consumer_key", appKey);
        userInfoParams.put("openid", openId);
        final HttpResponse userInfoRes = HttpRequest.get(buildUrl("https://graph.qq.com/user/get_user_info", userInfoParams)).trustAllCerts(true).
                connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).charset("UTF-8").send();
        LOGGER.log(Level.INFO, userInfoRes.charset("UTF-8").bodyText());
    }

    /**
     * Build url
     * 
     * @author zhuangyilian
     * @param url
     * @param params
     * @return
     */
    private static String buildUrl(String url, JSONObject params) {
        if(params == null){
            return url;
        }
        
        int count = 0;
        for (String key : params.keySet()) {
            if(count == 0){
                url += "?";
            }else{
                url += "&";
            }
            url += key + "=" + params.get(key);
            count++;
        }
        
        return url;
    }

    /**
     * Convert url data to json
     * 
     * @author zhuangyilian
     * @param urlData
     * @return
     */
    private static JSONObject urlDataToJson(final String urlData) {
        final JSONObject json = new JSONObject();
        if(StringUtils.isBlank(urlData)){
            return json;
        }
        
        final String[] datas = urlData.split("&");
        String[] pairs = new String [] {};
        for (String param : datas) {
            if(StringUtils.isBlank(param)){
                continue;
            }
            
            pairs = param.split("=");
            if(pairs.length == 2){
                json.put(pairs[0], pairs[1]);
            }
        }
        
        return json;
    }
    
    /**
     * Convert jsonp to json
     * jsonp: callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} )
     * 
     * @author zhuangyilian
     * @param jsonp
     * @return
     */
    private static JSONObject jsonpToJson(final String jsonp){
        final JSONObject json = new JSONObject();
        if(StringUtils.isBlank(jsonp)){
            return json;
        }
        
        final int startIndex = jsonp.indexOf("{");
        final int endIndex = jsonp.indexOf("}");
        final String jsonStr = jsonp.substring(startIndex, endIndex + 1);
        
        return new JSONObject(jsonStr);
    }
}
