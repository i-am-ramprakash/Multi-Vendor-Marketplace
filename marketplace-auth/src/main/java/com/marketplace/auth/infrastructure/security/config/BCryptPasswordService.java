package com.marketplace.auth.infrastructure.security.config;

import com.marketplace.auth.domain.service.PasswordService;
import com.marketplace.auth.domain.valueobject.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BCryptPasswordService implements PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public PasswordHash hash(String rawPassword) {
        return PasswordHash.of(encoder.encode(rawPassword));
    }

    @Override
    public boolean verify(String rawPassword, PasswordHash hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword.getValue());
    }

    @Override
    public boolean isWeak(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            return true;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : rawPassword.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("@$!%*?&".indexOf(c) >= 0) hasSpecial = true;
        }
        
        return !(hasUpper && hasLower && hasDigit && hasSpecial);
    }

    @Override
    public int getMinLength() {
        return 8;
    }
}