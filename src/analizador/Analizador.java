/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package analizador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author braya
 */
public class Analizador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Define la ruta del archivo "Lexer.flex" que se utilizará para generar el analizador léxico.
        String route1 = "C:/JAVA PROJECTS/Compilador/src/analizador/Lexer.flex";        
        String route2 = "C:/JAVA PROJECTS/Compilador/src/analizador/LexerCup.flex";
        String[] routeS = {"-parser", "Sintax", "C:/JAVA PROJECTS/Compilador/src/analizador/Sintax.cup"};
        
        generate(route1, route2, routeS);
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Crea una nueva instancia del JFrame (Frm1) que contiene la interfaz de usuario.
                Frm1 frame = new Frm1();
                frame.setTitle("THE BEST COMPILER"); // Establece el título de la ventana.
                frame.setVisible(true);
            }
        });
    }
    
    public static void generate(String route1, String route2, String[] routeS) throws IOException, Exception {
        File file; 
        
        file = new File(route1);
        JFlex.Main.generate(file);
        
        file = new File(route2);
        JFlex.Main.generate(file);
        
        java_cup.Main.main(routeS);
        
        Path routeSym = Paths.get("C:/JAVA PROJECTS/Compilador/src/analizador/sym.java");
        if (Files.exists(routeSym)){
              Files.delete(routeSym);
        }
        Files.move(
                Paths.get("C:/JAVA PROJECTS/Compilador/sym.java"),
                Paths.get("C:/JAVA PROJECTS/Compilador/src/analizador/sym.java")
        );
        
        Path routeSin = Paths.get("C:/JAVA PROJECTS/Compilador/src/analizador/Sintax.java");
        if (Files.exists(routeSin)){
              Files.delete(routeSin);
        }
        Files.move(
                Paths.get("C:/JAVA PROJECTS/Compilador/Sintax.java"),
                Paths.get("C:/JAVA PROJECTS/Compilador/src/analizador/Sintax.java")
        );
    }
}
