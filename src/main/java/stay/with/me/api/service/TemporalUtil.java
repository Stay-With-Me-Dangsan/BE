package stay.with.me.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class TemporalUtil {
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String generateTempPassword() {
        char firstChar = RANDOM.nextBoolean() ? (char) ('A' + RANDOM.nextInt(26)) : (char) ('a' + RANDOM.nextInt(26));
        char secondChar = RANDOM.nextBoolean() ? (char) ('A' + RANDOM.nextInt(26)) : (char) ('a' + RANDOM.nextInt(26));
        String randomNumbers = String.format("%05d", RANDOM.nextInt(100000));
        char specialChar = SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length()));

        String rawPassword = "" + firstChar + secondChar + randomNumbers + specialChar;
        return rawPassword;
    }

    public String socialPassword(String provider) {


        char firstChar = Character.toLowerCase(provider.charAt(0));
        String randomNumbers = String.format("%05d", RANDOM.nextInt(100000));
        char specialChar = SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length()));

        String rawPassword = firstChar + randomNumbers + specialChar;
        return passwordEncoder.encode(rawPassword);
    }

    public String socialNickname(String provider, String providerId) {


        String providerIdPart = providerId.length() > 4
                ? providerId.substring(providerId.length() - 4)
                : providerId;


        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            randomPart.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }


        return provider + "_" + providerIdPart + randomPart.toString();
    }

}
