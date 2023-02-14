package org.sys.rate.config;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

// 参考：https://blog.51cto.com/nanjke/2845623
// RSA公私钥生成和解码
@Component
@Data
public class RsaUtil {
    private String publicKey;
    private String privateKey;

    private RsaUtil(){
        Map<String,String> result = generateRsaKey(1024);
        publicKey = result.get("publicKey");
        privateKey = result.get("privateKey");
    }

    //生成RSA公私钥并保存
    public static Map<String,String> generateRsaKey(int keySize) {
        Map<String,String> result = new HashMap<>(2);
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            result.put("publicKey", new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()))); // 得到公钥字符串
            result.put("privateKey", new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()))); // 得到私钥字符串
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 通过私钥解密密文，返回明文
    public String decrypt(String str) {
        byte[] inputByte;
        String outStr = "";
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            byte[] decoded = Base64.decodeBase64(this.privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outStr;
    }
}
