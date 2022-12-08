package MidTermProject.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {
    public static void main(String[] args) {
        String password1 = passwordEncoder("passA01");
        String password2 = passwordEncoder("passA02");
        String password3 = passwordEncoder("passA03");
        String password4 = passwordEncoder("admin");
        String password5 = passwordEncoder("passT01");
        String password6 = passwordEncoder("passT02");
        System.out.println(password1);
        System.out.println(password2);
        System.out.println(password3);
        System.out.println(password4);
        System.out.println(password5);
        System.out.println(password6);
    }

    static String passwordEncoder(String rawPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.encode(rawPassword);
    }
}
