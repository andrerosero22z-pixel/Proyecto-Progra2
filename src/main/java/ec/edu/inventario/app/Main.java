package ec.edu.inventario.app;

import javax.swing.SwingUtilities;

import ec.edu.inventario.ui.VentanaPrincipal;

public class Main {

    // corregir el mensaje de validacion de letras 
    // validar telefono negativo, los id genere solo
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
