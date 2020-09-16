package com.mpic.evolution.chair.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mpic.evolution.chair.common.constant.SecretKeyConstants;

/**
 * @author Administrator
 */
public class JWTUtil {

	/** token 过期时间: 10天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     *
     * 	方法名：verify
     *	 描述： 校验token是否正确
     * @author SJ
     *	 创建时间：2019-9-10 10:35
     * @param token
     * @param username
     * @param secret
     * @return boolean
     *
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     *
     * 	方法名：getUsername
     * 	描述：token中获取用户名
     * @author SJ
     * 	创建时间：2019-9-10 10:35
     * @param token
     * @return java.lang.String
     *
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     *
     * 	方法名：getUserId
     *	 描述：token中获取UserId
     * @author lianghonglei
     * 	创建时间：2019-9-12 16:55
     * @param token
     * @return java.lang.String
     *
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     *
     *	 方法名：sign
     * 	描述：加密的token
     * @author SJ
     * 	创建时间：2019-9-10 10:34
     * @param username
     * @param secret
     * @return java.lang.String
     *
     */
    public static String sign(String userId,String username, String secret) {
    	// header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        
        Date iatDate = new Date();
        //expire time
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(calendarField, calendarInterval);
		Date expiresDate = nowTime.getTime();

		Algorithm algorithm = Algorithm.HMAC256(MD5Utils.encrypt(secret));
		// 附带username信息
		return JWT.create().withHeader(map) // header
		        .withClaim("userId", userId)
		        .withClaim("username", username)
		        .withIssuedAt(iatDate) // sign time
		        .withExpiresAt(expiresDate)
		        .sign(algorithm);
    }
    
    public static void main(String[] args) {
		String token = JWTUtil.sign("1352", "12313", SecretKeyConstants.jwtSecretKey);
		System.out.println(token);
		String userId = JWTUtil.getUserId(token);
		System.out.println(userId);
		String username = JWTUtil.getUsername(token);
		System.out.println(username);
	}
}
