package cn.com.koriesh.memo.common;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* @Author: wangh 
* @Description:
* @Date: 2021/1/3
* @Param: 
* @Return: 
*/
@Slf4j
public class MD5Util {
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes(Charset.forName("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5Util出错，无此MD5算法"+e.getMessage());
        }
        StringBuilder md5code = new StringBuilder();
        if (secretBytes != null) {
            md5code.append(new BigInteger(1, secretBytes).toString(16));
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code.insert(0, "0");
            }
        }
        return md5code.toString();
    }

//    public static void main(String[] args) {
//        log.info(stringToMD5("nsmmApiKey"));
//    }
}