package com.chat.core.commons.sharding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @classDesc: 功能描述:(KetamaHash)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class KetamaHash {
    private static final MessageDigest MD5_DIGEST;
    static {
        try {
            MD5_DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            throw new RuntimeException("MD5 not supported", var1);
        }
    }
    public int getHash(String origin) {
        byte[] bKey = computeMd5(origin);
        long rv = (long)(bKey[3] & 255) << 24 | (long)(bKey[2] & 255) << 16 | (long)(bKey[1] & 255) << 8 | (long)(bKey[0] & 255);
        return (int)(rv & 4294967295L);
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest)MD5_DIGEST.clone();
        } catch (CloneNotSupportedException var3) {
            throw new RuntimeException("clone of MD5 not supported", var3);
        }

        md5.update(k.getBytes());
        return md5.digest();
    }
}
