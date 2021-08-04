
package com.github.pangju666.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    private static final Logger logger = LoggerFactory.getLogger(Md5Util.class);

    public static String encode(String pwd) {
        String hash = null;
        try {
            hash = DatatypeConverter.printHexBinary(
                    MessageDigest.getInstance("MD5").digest(pwd.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        return hash;
    }
}
