package MidTermProject.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {
    public static void main(String[] args) {
        String password1 = passwordEncoder("ironhack");
        String password2 = passwordEncoder("123abc");
        String password3 = passwordEncoder("abc123");
        System.out.println(password1);
        System.out.println(password2);
        System.out.println(password3);
    }

    static String passwordEncoder(String rawPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
