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
import org.b3log.latke.Latkes;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.json.JSONObject;

/**
 * QQ utilities.
 * 
 * @author zhuangyilian
 */
public final class QQs {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QQs.class);

    /**
     * QQ access token
     * 
     * @author zhuangyilian
     * @param code
     * @return
     */
    public static JSONObject qqAccessToken(final String code){
        try {
            final JSONObject params = new JSONObject();
            params.put("grant_type", "authorization_code");
            params.put("client_id", Latkes.getLocalProperty("qq.appId"));
            params.put("client_secret", Latkes.getLocalProperty("qq.appKey"));
            params.put("code", code);
            params.put("redirect_uri", Latkes.getServePath() + "/oauth/qq");
            
            final HttpResponse res = HttpRequest.get(buildUrl("https://graph.qq.com/oauth2.0/token", params)).trustAllCerts(true).
                    connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).send();
            
            return urlDataToJson(res.bodyText());
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Gets QQ access token fail", e);
    
            return null;
        }
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
     * Get QQ openId
     * 
     * @author zhuangyilian
     * @param accessToken
     * @return
     */
    public static JSONObject getQQOpenId(final String accessToken){
        try {
            final JSONObject params = new JSONObject();
            params.put("access_token", accessToken);
            
            final HttpResponse res = HttpRequest.get(buildUrl("https://graph.qq.com/oauth2.0/me", params)).trustAllCerts(true).
                    connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).send();
            
            return jsonpToJson(res.bodyText());
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Gets QQ openId fail", e);
    
            return null;
        }
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
    
    /**
     * Get QQ user info
     * 
     * @author zhuangyilian
     * @param accessToken
     * @param openId
     * @return
     */
    public static JSONObject getQQUserInfo(final String accessToken, final String openId){
        try {
            final JSONObject params = new JSONObject();
            params.put("access_token", accessToken);
            params.put("oauth_consumer_key", Latkes.getLocalProperty("qq.appId"));
            params.put("openid", openId);
            
            final HttpResponse res = HttpRequest.get(buildUrl("https://graph.qq.com/user/get_user_info", params)).trustAllCerts(true).
                    connectionTimeout(3000).timeout(7000).header("User-Agent", Solos.USER_AGENT).send();
            
            return new JSONObject(res.charset("UTF-8").bodyText());
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Gets QQ user info fail", e);
    
            return null;
        }
    }

    /**
     * Private constructor.
     */
    private QQs() {
    }
}
