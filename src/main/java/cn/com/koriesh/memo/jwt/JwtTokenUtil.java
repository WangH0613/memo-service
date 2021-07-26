package cn.com.koriesh.memo.jwt;

import cn.com.koriesh.memo.jwt.TokenSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static String issuer;

    // 定义TOKEN相关的常量
    // 外部 用的token 以OPEN_开头
    public static final String OPEN_SUBJECT = "OPEN_";
    // 业务 用的token 以BIZ_开头
    public static final String BIZ_SUBJECT = "BIZ_";
    // 嵌入 用的token 以IFRAME_开头
    public static final String IFRAME_SUBJECT = "IFRAME_";

    /**
     * @Description: 初始化token的各项配置
     * secretKey jwt的秘钥
     * accessTokenExpireTime jwt的有效时长
     * refreshTokenExpireTime jwt的刷新周期
     * issuer jwt的签发人信息
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static void setTokenSettings(TokenSettings tokenSettings) {
        secretKey = tokenSettings.getSecretKey();
        accessTokenExpireTime = tokenSettings.getAccessTokenExpireTime();
        refreshTokenExpireTime = tokenSettings.getRefreshTokenExpireTime();
        issuer = tokenSettings.getIssuer();
        log.info("jwt config is end");
    }

    /**
     * @Description: 签发/生成token
     * issuer 签发人
     * subject 代表这个JWT的主体，即他的所有人，一般是用户ID
     * claims 储存在jwt里的信息(键值对)，一般是放些用户的权限/角色信息
     * ttlMillis 有效时间(毫秒)
     * secret 密钥
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String generateToken(
            String issuer,
            String subject,
            Map<String, Object> claims,
            long ttlMillis,
            String secret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // String printBase64Binary(byte[]) 将字节数组做base64编码
        // byte[] parseBase64Binary(String) 将Base64编码后的String还原成字节数组
        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);
        // 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder();
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (null != claims) {
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        //签发时间
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            //过期时间
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * @Description: 生成 access_token
     * 正常请求资源时携带的凭证
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String getAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, accessTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * @Description: 生成 refresh_token
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String getRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, refreshTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * @Description: 解析令牌 获取数据声明
     * 拿到用户及用户的角色、权限等信息
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            //用密钥(必字节数组)解析jwt，获取body（有效载荷）
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            //解析不了，这个token就是无效的
            log.error(e.getMessage());
        }
        return claims;
    }

    /**
     * @Description: 获取token中的用户id
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String getUserId(String token) {
        String userId = null;
        try {
            Claims claims = getClaimsFromToken(token);
//            userId = claims.getSubject();
            userId = claims == null ? "" : claims.getSubject();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return userId;
    }

    /**
     * @Description: 获取token中的用户名
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String getUserName(String token) {
        String username = null;
        try {
            //解析token获取claims
            Claims claims = getClaimsFromToken(token);
            //claims中的key当作自定义的常量
//            username = (String) claims.get("");
            username = claims == null ? "" : (String) claims.get("");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return username;
    }

    /**
     * 验证token 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            //首先解析，如果能解析成功，证明我服务器签发的
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            Date expiration = claims.getExpiration();
            //过期时间和当前时间比较，如果过期时间在当前时间之前，返回true，表示已过期；否则返回false，没过期
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("token已过期 {}", e);
            //解析失败，抛出异常，返回true，表示已过期
            return true;
        }
    }

    /**
     * 校验令牌
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null != claimsFromToken && !isTokenExpired(token));
    }

    /**
     * @Description: 刷新token
     * 如果是过期刷新，claims/载荷 不变；
     * 如果主动刷新，claims/载荷 改变【一般是权限/角色改变的时候去主动刷新】
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static String refreshToken(String refreshToken, Map<String, Object> claims) {
        String refreshedToken;
        try {
            Claims parserclaims = getClaimsFromToken(refreshToken);
            if (parserclaims == null) {
                return "";
            }
            // 如果传入的claims为空，说明是过期刷新，原先的用户信息不变，claims引用上个token里的内容
            if (null == claims) {
                claims = parserclaims;
            }
            // 不为空，根据传入的claims【用户信息】，生成新的Token
            refreshedToken = generateToken(parserclaims.getIssuer(), parserclaims.getSubject(), claims, accessTokenExpireTime.toMillis(), secretKey);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("刷新token发生异常 {}", e);
        }
        return refreshedToken;
    }

    /**
     * @Description: 获取token的剩余过期时间
     * @CreateDate: 2020/11/2 2020/11/2
     * @UpdateDate: 2020/11/2 2020/11/2
     */
    public static long getRemainingTime(String token) {
        long result = 0;
        try {
            long nowMillis = System.currentTimeMillis();
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return 0;
            }
            result = claims.getExpiration().getTime() - nowMillis;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

}
