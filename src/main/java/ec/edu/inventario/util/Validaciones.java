package ec.edu.inventario.util;

public class Validaciones {

    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().length() == 0;
    }

    public static boolean esEntero(String texto) {
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esDecimal(String texto) {
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esEnteroNoNegativo(String texto) {
        if (!esEntero(texto)) {
            return false;
        }
        return Integer.parseInt(texto.trim()) >= 0;
    }

    public static boolean esEnteroPositivo(String texto) {
        if (!esEntero(texto)) {
            return false;
        }
        return Integer.parseInt(texto.trim()) > 0;
    }

    public static boolean esDecimalNoNegativo(String texto) {
        if (!esDecimal(texto)) {
            return false;
        }
        return Double.parseDouble(texto.trim()) >= 0;
    }
}
