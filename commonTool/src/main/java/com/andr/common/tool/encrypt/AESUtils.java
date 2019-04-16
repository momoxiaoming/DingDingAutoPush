package com.andr.common.tool.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     author: momoxiaoming
 *     blog  : http://blog.momoxiaoming.com
 *     time  : 2019/2/27
 *     desc  : AES加密解密
 * </pre>
 */
public class AESUtils
{

    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";  //加密模式

    private static final String TRANSFORMATION = "AES";

    private static final byte[] DEFULT_PASSWORD = new byte[]{10, 30, 40, 50, 60, 70, 80, 11, 22, 33, 44, 55, 66, 77, 88, 99};  //密码,最少16位代表128位加密,32位代表256加密


    /**
     * AES加密
     *
     * @param pwd     密码必须为16位或者32位,也就是16个字母或者32个字母
     * @param content 加密的字符串
     * @return
     */
    public static String encrypt(byte[] pwd, String content) {

        if (pwd == null)
        {
            pwd = DEFULT_PASSWORD;
        }

        try
        {
            // 创建AES秘钥
            SecretKeySpec key = new SecretKeySpec(pwd, ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bt = cipher.doFinal(content.getBytes("UTF-8"));

            return Base64Encoder.encode(bt);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * AES解密
     *
     * @param pwd     密码必须为16位或者32位,也就是16个字母或者32个字母
     * @param content 加密的字符串
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] pwd, String content) {
        try
        {
            //先base64解密
            content = Base64Decoder.decode(content);
            if (pwd == null)
            {
                pwd = DEFULT_PASSWORD;
            }
            // 创建AES秘钥
            SecretKeySpec key = new SecretKeySpec(pwd, ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密

            byte[] bt = cipher.doFinal(content.getBytes("utf-8"));
            return new String(bt);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
