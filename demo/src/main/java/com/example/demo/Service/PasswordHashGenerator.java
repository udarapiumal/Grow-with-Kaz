package com.example.demo.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * One-off helper — not a Spring bean, not wired into the app. Run its main
 * method once (right-click > Run in your IDE, passing the password as the
 * program argument) to get a hash to paste into the admin_users insert in
 * V2__products_and_admin.sql. Never store the plaintext password anywhere.
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Pass the plaintext password as the single argument.");
            return;
        }
        System.out.println(new BCryptPasswordEncoder().encode(args[0]));
    }
}