package com.example.printifybackend.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author SamMDev
 */
public class Sha256PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return StringUtils.equals(this.encode(rawPassword), encodedPassword);
    }
}
