package ec.edu.inventario.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ArchivoUtil {

    private static final String CARPETA_DATOS = "datos";

    public static File obtenerArchivo(String nombreArchivo) throws IOException {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, nombreArchivo);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
        return archivo;
    }

    public static ArrayList<String> leerLineas(String nombreArchivo) throws FileNotFoundException, IOException {
        ArrayList<String> lineas = new ArrayList<>();
        File archivo = obtenerArchivo(nombreArchivo);

        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea = lector.readLine();
            while (linea != null) {
                if (linea.trim().length() > 0) {
                    lineas.add(linea);
                }
                linea = lector.readLine();
            }
        } finally {
            if (lector != null) {
                lector.close();
            }
        }
        return lineas;
    }

    public static void escribirLineas(String nombreArchivo, ArrayList<String> lineas) throws IOException {
        File archivo = obtenerArchivo(nombreArchivo);

        BufferedWriter escritor = null;
        try {
            escritor = new BufferedWriter(new FileWriter(archivo));
            for (int i = 0; i < lineas.size(); i++) {
                escritor.write(lineas.get(i));
                escritor.newLine();
            }
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }

    public static String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().replace(";", " ").replace("\n", " ").replace("\r", " ");
    }

    public static String[] separar(String linea) {
        return linea.split(";", -1);
    }
}
