package com.tybingham.graphics.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class Utils {
    private Utils(){}

    public static String readResource(String path) {
        try (InputStream in = Utils.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new RuntimeException("Resource not found: " + path);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }
}