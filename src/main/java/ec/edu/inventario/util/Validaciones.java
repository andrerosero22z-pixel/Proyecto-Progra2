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
        if (estaVacio(texto)) {
            return false;
        }
        try {
            double valor = Double.parseDouble(texto.trim());
            return Double.isFinite(valor);
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

    public static boolean esDecimalPositivo(String texto) {
        if (!esDecimal(texto)) {
            return false;
        }
        return Double.parseDouble(texto.trim()) > 0;
    }

    public static boolean esTelefonoValido(String texto) {
        if (estaVacio(texto)) {
            return false;
        }

        String telefono = texto;
        if ((telefono.length() != 9 && telefono.length() != 10) || telefono.charAt(0) != '0') {
            return false;
        }

        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean esCorreoValido(String texto) {
        if (estaVacio(texto)) {
            return false;
        }

        String correo = texto;
        for (int i = 0; i < correo.length(); i++) {
            if (Character.isWhitespace(correo.charAt(i))) {
                return false;
            }
        }

        int posicionArroba = correo.indexOf('@');
        if (posicionArroba <= 0 || posicionArroba != correo.lastIndexOf('@')) {
            return false;
        }

        String dominio = correo.substring(posicionArroba + 1);
        int posicionPunto = dominio.indexOf('.');
        return posicionPunto > 0 && !dominio.endsWith(".");
    }

    public static boolean esRucValido(String texto) {
        if (estaVacio(texto)) {
            return false;
        }

        String ruc = texto;
        if (ruc.length() != 13) {
            return false;
        }

        for (int i = 0; i < ruc.length(); i++) {
            if (!Character.isDigit(ruc.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean esTextoValido(String texto) {
        if (estaVacio(texto)) {
            return false;
        }

        String valor = texto.trim();
        for (int i = 0; i < valor.length(); i++) {
            if (Character.isLetter(valor.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
