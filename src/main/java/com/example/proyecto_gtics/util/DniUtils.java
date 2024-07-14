package com.example.proyecto_gtics.util;

public class DniUtils {
    public static String formatDni(Integer dni) {
        return String.format("%08d", dni);
    }

    public static Integer parseDni(String dniStr) {
        return Integer.parseInt(dniStr);
    }
}
