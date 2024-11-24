/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package analizador;

import static analizador.Tokens.Identificador;
import static analizador.Tokens.Llave_abierta;
import static analizador.Tokens.Llave_cerrada;
import static analizador.Tokens.Salto_Linea;
import static analizador.Tokens.Tipo_dato;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author braya
 */
public class Frm1 extends javax.swing.JFrame {

    /**
     * Creates new form Frm1
     */
    
    private String traducido = "";
    private String intermedio = "";
    private String semanticErrors = "";
    private String currentScope = ""; //para almacenar el ambito actual "solo funciona con main"
    HashMap<String, String> declaredVariable = new HashMap<>(); //Para almacenar las variables declaradas
    
    public Frm1() {
        initComponents();
        
        // Establece la ventana en el centro de la pantalla cuando se abre
        this.setLocationRelativeTo(null);
        
        // Hace que el área de texto de resultados ("txtResult") no sea editable
        txtLexicalResult.setEditable(false);
        txtSyntacticResult.setEditable(false);
        txtSemanticResult.setEditable(false);
        txtTranslatorResult.setEditable(false);
    }
    
    public void RunCprogram() {
        String cCode = traducido;

        // Crear un archivo temporal para almacenar el código C
        File tempFile;
        try {
            tempFile = File.createTempFile("temp", ".c");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(cCode);
            }
        } catch (IOException e) {
            System.err.println("Error al crear el archivo temporal.");
            e.printStackTrace();
            return;
        }

        // Compilar el archivo C
        String outputPath = tempFile.getAbsolutePath().replace(".c", ".exe");
        String gccPath = "C:/MinGW/bin/gcc.exe"; // Asegúrate de que esta ruta sea correcta
        String compileCommand = gccPath + " " + tempFile.getAbsolutePath() + " -o " + outputPath;
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCommand);
            compileProcess.waitFor();

            // Leer la salida de error de la compilación
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            // Verificar si la compilación falló
            if (compileProcess.exitValue() != 0) {
                System.err.println("Error de compilación:\n" + errors);
                return;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error durante la compilación.");
            e.printStackTrace();
            return;
        }

        // Ejecutar el programa compilado
        try {
            String executeCommand = "cmd /c start cmd.exe /K \"" + outputPath + "\"";
            Runtime.getRuntime().exec(executeCommand);

            // Opcional: Limpieza de archivos temporales después de ejecutar
            tempFile.deleteOnExit();
            new File(outputPath).deleteOnExit();
        } catch (IOException e) {
            System.err.println("Error al ejecutar el programa.");
            e.printStackTrace();
        }
    }

    public void CodigoIntermedio() throws IOException{
        String expr = (String) txtInput.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String cierreFuncion = "";
        
        intermedio = "";
        
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                txtIcodeResult.setText(intermedio);
                return;
            }
            
            switch (token) {
                case Salto_Linea:
                    intermedio += "\n";
                    break;
                case Comillas:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Cadena:
                    intermedio += "String ";
                    break;
                case Tipo_dato:
                    switch(lexer.lexeme){
                        case "int":
                            intermedio += "variableInt ";
                            break;
                        case "char":      
                            intermedio += "variableChar ";
                            break;
                        case "float":      
                            intermedio += "variableFloat ";
                            break;
                        case "double":      
                            intermedio += "variableDouble ";
                            break;    
                    }
                    break;
                case If:
                    cierreFuncion = "If";
                    intermedio += "inicioIf ";
                    break;
                case Else:
                    intermedio += "inicioElse ";
                    cierreFuncion = "Else";
                    break;
                case Do:
                    intermedio += "inicioDo ";
                    cierreFuncion = "Do";
                    break;
                case While:
                    intermedio += "inicioWhile ";;
                    cierreFuncion = "While";
                    break;
                case For:
                    cierreFuncion = "For";
                    intermedio += "inicioFor ";;
                    break;
                case Igual:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Suma:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Resta:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Multiplicacion:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Division:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Operador_logico:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Operador_incremento:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Operador_relacional:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Operador_atribucion:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Operador_booleano:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Parentesis_abierto:
                    intermedio += lexer.lexeme;
                    break;
                case Parentesis_cerrado:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Llave_abierta:
                    //intermedio += lexer.lexeme + " ";
                    break;
                case Llave_cerrada:
                    intermedio += "cierre" + cierreFuncion;
                    cierreFuncion = "Main";
                    break;
                case Corchete_abierto:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Corchete_cerrado:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Main:
                    intermedio = "";
                    intermedio += "inicioMain ";
                    cierreFuncion = "Main";
                    break;
                case Punto_coma:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Identificador:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Numero:
                    intermedio += lexer.lexeme + " ";
                    break;
                case NumeroDecimal:
                    intermedio += lexer.lexeme + " ";
                    break;
                case Print:
                    intermedio += "Printf ";
                    break;
                case String_lit:
                    intermedio += lexer.lexeme + " ";
                    break;
                default:
                    break;
            }
        }
    }
    
    public void Traductor() throws IOException{
        String expr = (String) txtInput.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        
        int tamano;
        traducido = "#include <stdio.h>\n\n";
        
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                txtTranslatorResult.setText(traducido);
                return;
            }
            switch (token) {
                case Salto_Linea:
                    traducido += "\n";
                    break;
                case Comillas:
                    traducido += lexer.lexeme + " ";
                    break;
                case Cadena:
                    traducido += "String ";
                    break;
                case Tipo_dato:
                    switch(lexer.lexeme){
                        case "int":
                            traducido += "int ";
                            break;
                        case "char":      
                            traducido += "char ";
                            break;
                        case "float":      
                            traducido += "float ";
                            break;
                        case "double":      
                            traducido += "double ";
                            break;    
                    }
                    break;
                case If:
                    traducido += "if ";
                    break;
                case Else:
                    traducido += "else ";
                    break;
                case Do:
                    traducido += "do ";
                    break;
                case While:
                    traducido += "while ";;
                    break;
                case For:
                    traducido += "for ";;
                    break;
                case Igual:
                    traducido += lexer.lexeme + " ";
                    break;
                case Suma:
                    traducido += lexer.lexeme + " ";
                    break;
                case Resta:
                    traducido += lexer.lexeme + " ";
                    break;
                case Multiplicacion:
                    traducido += lexer.lexeme + " ";
                    break;
                case Division:
                    traducido += lexer.lexeme + " ";
                    break;
                case Mod:
                    traducido += lexer.lexeme + " ";
                    break;
                case Operador_logico:
                    traducido += lexer.lexeme + " ";
                    break;
                case Operador_incremento:
                    traducido += lexer.lexeme + " ";
                    break;
                case Operador_relacional:
                    traducido += lexer.lexeme + " ";
                    break;
                case Operador_atribucion:
                    traducido += lexer.lexeme + " ";
                    break;
                case Operador_booleano:
                    traducido += lexer.lexeme + " ";
                    break;
                case Parentesis_abierto:
                    traducido += lexer.lexeme;
                    break;
                case Parentesis_cerrado:
                    traducido += lexer.lexeme + " ";
                    break;
                case Llave_abierta:
                    traducido += lexer.lexeme + " ";
                    break;
                case Llave_cerrada:
                    traducido += lexer.lexeme + " ";
                    break;
                case Corchete_abierto:
                    traducido += lexer.lexeme + " ";
                    break;
                case Corchete_cerrado:
                    traducido += lexer.lexeme + " ";
                    break;
                case Main:
                    traducido += "main ";
                    break;
                case Punto_coma:
                    traducido += lexer.lexeme + " ";
                    break;
                case Identificador:
                    tamano = traducido.length();
                    if (tamano >= 5 && 
                        Character.toLowerCase(traducido.charAt(tamano - 7)) == 'p' &&
                        Character.toLowerCase(traducido.charAt(tamano - 6)) == 'r' &&
                        Character.toLowerCase(traducido.charAt(tamano - 5)) == 'i' &&
                        Character.toLowerCase(traducido.charAt(tamano - 4)) == 'n' &&
                        Character.toLowerCase(traducido.charAt(tamano - 3)) == 't' &&
                        Character.toLowerCase(traducido.charAt(tamano - 2)) == 'f') {
                      // Penultimate character is part of "printf" (case-insensitive)
                      traducido += "\"%" + "d " + "\", ";
                      traducido += lexer.lexeme + " ";
                    } else {
                      traducido += lexer.lexeme + " ";
                    }
                    break;
                case Print:
                    traducido += "printf";
                    break;
                case Numero:
                    traducido += lexer.lexeme + " ";
                    break;
                case NumeroDecimal:
                    traducido += lexer.lexeme + " ";
                    break;
                case String_lit:
                    tamano = traducido.length();
                    
                    if (tamano >= 5 && 
                        Character.toLowerCase(traducido.charAt(tamano - 7)) == 'p' &&
                        Character.toLowerCase(traducido.charAt(tamano - 6)) == 'r' &&
                        Character.toLowerCase(traducido.charAt(tamano - 5)) == 'i' &&
                        Character.toLowerCase(traducido.charAt(tamano - 4)) == 'n' &&
                        Character.toLowerCase(traducido.charAt(tamano - 3)) == 't' &&
                        Character.toLowerCase(traducido.charAt(tamano - 2)) == 'f') {
                      // Penultimate character is part of "printf" (case-insensitive)
                      traducido += lexer.lexeme + " ";
                      Tokens siguienteToken = lexer.yylex();
                      if (siguienteToken != null && siguienteToken == Tokens.Identificador){
                        traducido += ", " + lexer.lexeme;
                      }else{
                          traducido += lexer.lexeme + " ";
                      }
                      
                    } else {
                      traducido += lexer.lexeme + " ";
                    }
                    break;
                default:
                    break;
            }
        } 
    }
    
    private void lexicalAnalyze() throws IOException{
        int cont = 1;
        
        String expr = (String) txtInput.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String resultado = "   Name " + "\t\tToken\n\n";
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                txtLexicalResult.setText(resultado);
                return;
            }
            switch (token) {
                case Salto_Linea:
                    cont++;
                    resultado += "-Salto de Linea-\t " + cont + "\n";
                    break;
                case Comillas:
                    resultado += "  -Comillas-\t\t" + lexer.lexeme + "\n";
                    break;
                case Cadena:
                    resultado += "  -Tipo de dato-\t" + lexer.lexeme + "\n";
                    break;
                case Tipo_dato:
                    resultado += "  -Tipo de dato-\t" + lexer.lexeme + "\n";
                    break;
                case If:
                    resultado += "  -Reservada if-\t" + lexer.lexeme + "\n";
                    break;
                case Else:
                    resultado += "  -Reservada else-\t" + lexer.lexeme + "\n";
                    break;
                case Do:
                    resultado += "  -Reservada do-\t" + lexer.lexeme + "\n";
                    break;
                case While:
                    resultado += "  -Reservada while-\t" + lexer.lexeme + "\n";
                    break;
                case For:
                    resultado += "  -Reservada while-\t" + lexer.lexeme + "\n";
                    break;
                case Igual:
                    resultado += "  -Operador igual-\t" + lexer.lexeme + "\n";
                    break;
                case Suma:
                    resultado += "  -Operador suma-\t" + lexer.lexeme + "\n";
                    break;
                case Resta:
                    resultado += "  -Operador resta-\t" + lexer.lexeme + "\n";
                    break;
                case Multiplicacion:
                    resultado += "  -Operador multiplicacion-\t" + lexer.lexeme + "\n";
                    break;
                case Division:
                    resultado += "  -Operador division-\t" + lexer.lexeme + "\n";
                    break;
                case Mod:
                    resultado += "  Operador mod\t\t" + lexer.lexeme + "\n";
                    break;
                case Operador_logico:
                    resultado += "  -Operador logico-\t" + lexer.lexeme + "\n";
                    break;
                case Operador_incremento:
                    resultado += "  -Operador incremento-\t" + lexer.lexeme + "\n";
                    break;
                case Operador_relacional:
                    resultado += "  -Operador relacional-\t" + lexer.lexeme + "\n";
                    break;
                case Operador_atribucion:
                    resultado += "  -Operador atribucion-\t" + lexer.lexeme + "\n";
                    break;
                case Operador_booleano:
                    resultado += "  -Operador booleano-\t" + lexer.lexeme + "\n";
                    break;
                case Parentesis_abierto:
                    resultado += "  -Parentesis de apertura-\t" + lexer.lexeme + "\n";
                    break;
                case Parentesis_cerrado:
                    resultado += "  -Parentesis de cierre-\t" + lexer.lexeme + "\n";
                    break;
                case Llave_abierta:
                    resultado += "  -Llave de apertura-\t" + lexer.lexeme + "\n";
                    break;
                case Llave_cerrada:
                    resultado += "  -Llave de cierre-\t" + lexer.lexeme + "\n";
                    break;
                case Corchete_abierto:
                    resultado += "  -Corchete de apertura-\t" + lexer.lexeme + "\n";
                    break;
                case Corchete_cerrado:
                    resultado += "  -Corchete de cierre-\t" + lexer.lexeme + "\n";
                    break;
                case Main:
                    resultado += "  -Reservada main-\t" + lexer.lexeme + "\n";
                    break;
                case Punto_coma:
                    resultado += "  -Punto y coma-\t" + lexer.lexeme + "\n";
                    break;
                case Identificador:
                    resultado += "  -Identificador-\t" + lexer.lexeme + "\n";
                    break;
                case Print:
                    resultado += "  Reservada Printf\t" + lexer.lexeme + "\n";
                    break;
                case Numero:
                    resultado += "  -Numero-\t\t" + lexer.lexeme + "\n";
                    break;
                case NumeroDecimal:
                    resultado += "  -NumeroDecimal-\t" + lexer.lexeme + "\n";
                    break;
                case String_lit:
                    resultado += "  String literal\t\t" + lexer.lexeme + "\n";
                    break;
                case ERROR:
                    resultado += "  -Simbolo no definido-\n";
                    break;
                default:
                    resultado += "   " + lexer.lexeme + " \n";
                    break;
            }
        }
    }
    
    public void syntacticAnalyze() throws IOException {
        String ST = txtInput.getText();
        Sintax s = new Sintax(new analizador.LexerCup(new StringReader(ST)));
        
        try {
            s.parse();
            txtSyntacticResult.setText("Analisis realizado correctamente");
            txtSyntacticResult.setForeground(new Color(25, 111, 61));
        } catch (Exception ex) {
            Symbol sym = s.getS();
            txtSyntacticResult.setText("Error de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"");
            txtSyntacticResult.setForeground(Color.red);
        }
    }
    
    public void semanticAnalyze() throws IOException{
        currentScope = "";
        semanticErrors = "";
        declaredVariable.clear();
        
        String expr = (String) txtInput.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        
        boolean errorsFound = false;
        int linea = 1; 
        String currentScope = "otro"; 

        // Análisis léxico y semántico
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                break;
            }

            switch (token) {
                case Salto_Linea:
                    linea++;
                    break;
                case Tipo_dato:
                    String typeData = lexer.lexeme;
                    Tokens nextToken = lexer.yylex();
                    if (nextToken == Tokens.Identificador) {
                        String identifier = lexer.lexeme;
                        
                        if (currentScope.equals("main")) {
                            if (declaredVariable.containsKey(identifier)) {
                            semanticErrors += " Error: La variable '" + identifier + "' ya ha sido declarada en el mismo ámbito (línea " + linea + ")\n";
                            errorsFound = true;
                            txtSemanticResult.setForeground(Color.red);
                        } else {
                            declaredVariable.put(identifier, typeData);
                            Tokens tokenAsignacion = lexer.yylex();
                            if (tokenAsignacion == Tokens.Igual) {
                                Tokens typeValue = lexer.yylex();
                                if ((typeData.equals("int") && typeValue == Tokens.NumeroDecimal) ||
                                    (typeData.equals("float") && typeValue != Tokens.Numero && typeValue != Tokens.NumeroDecimal) || 
                                        (typeData.equals("double") && typeValue != Tokens.Numero && typeValue != Tokens.NumeroDecimal)){
                                    semanticErrors += " Error: Tipo de dato incorrecto para la variable '" + identifier + "' (línea " + linea + ")\n";
                                    errorsFound = true;
                                    txtSemanticResult.setForeground(Color.red);
                                }
                            }
                        }
                        } else {
                            semanticErrors += " Error: Variable '" + identifier + "' declarada fuera de 'main' (línea " + linea + ")\n";
                            errorsFound = true;
                            txtSemanticResult.setForeground(Color.red);
                        }
                    }
                    break;
                case Identificador:
                    String identifier = lexer.lexeme;
                    // Verificar si la variable no ha sido declarada
                    if (!declaredVariable.containsKey(identifier)) {
                        semanticErrors += " Error: Variable no declarada '" + identifier + "' (línea " + linea + ")\n";
                        errorsFound = true;
                        txtSemanticResult.setForeground(Color.red);
                    } else {
                        Tokens tokenAsignacion = lexer.yylex();
                        // Verificar si hay un valor asignado a la variable y que este sea el corecto
                        if (tokenAsignacion == Tokens.Igual) {
                            String tipoDatoVariable = declaredVariable.get(identifier);
                            Tokens typeValue = lexer.yylex();
                            if ((tipoDatoVariable.equals("int") && typeValue == Tokens.NumeroDecimal) ||
                                (tipoDatoVariable.equals("float") && typeValue != Tokens.Numero && typeValue != Tokens.NumeroDecimal) ||
                                (tipoDatoVariable.equals("double") && typeValue != Tokens.Numero && typeValue != Tokens.NumeroDecimal)) {
                                semanticErrors += " Error: Tipo de dato incorrecto para la variable '" + identifier + "' (línea " + linea + ")\n";
                                errorsFound = true;
                                txtSemanticResult.setForeground(Color.red);
                            }
                        }
                    }
                    break;
                case Llave_abierta:
                    currentScope = "main";
                    break;
                case Llave_cerrada:
                    currentScope = "otro";
                    break;
                default:
                    break;
            }
        }

        // Verificación final de errores y mensajes
         if (!errorsFound) {
            semanticErrors += "Semántica correcta. \n";
            txtSemanticResult.setForeground(new Color(25, 111, 61));
        }

         // Mostrar los errores semánticos
        txtSemanticResult.setText(semanticErrors);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLexicalResult = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        btnLexicalAnalyze = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSyntacticResult = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        btnSyntacticAnalyze = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtSemanticResult = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        btnSemanticAnalyze = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInput = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        btnAnalyze = new javax.swing.JButton();
        btnClean = new javax.swing.JButton();
        btnOpenFile = new javax.swing.JButton();
        btnExec = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtTranslatorResult = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        btnTranslate = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtIcodeResult = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setFont(new java.awt.Font("Tempus Sans ITC", 3, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("BRAYAN MARTINEZ, 1-20-1136");

        btnClose.setBackground(new java.awt.Color(255, 0, 0));
        btnClose.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("EXIT");
        btnClose.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        txtLexicalResult.setBackground(new java.awt.Color(204, 204, 204));
        txtLexicalResult.setColumns(20);
        txtLexicalResult.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtLexicalResult.setRows(5);
        jScrollPane1.setViewportView(txtLexicalResult);

        jLabel1.setFont(new java.awt.Font("Nirmala UI", 3, 24)); // NOI18N
        jLabel1.setText("Lexical Analyzer");

        btnLexicalAnalyze.setBackground(new java.awt.Color(51, 51, 51));
        btnLexicalAnalyze.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnLexicalAnalyze.setForeground(new java.awt.Color(255, 255, 255));
        btnLexicalAnalyze.setText("ANALYZE");
        btnLexicalAnalyze.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLexicalAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLexicalAnalyzeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLexicalAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnLexicalAnalyze))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtSyntacticResult.setBackground(new java.awt.Color(204, 204, 204));
        txtSyntacticResult.setColumns(20);
        txtSyntacticResult.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtSyntacticResult.setRows(5);
        jScrollPane3.setViewportView(txtSyntacticResult);

        jLabel8.setFont(new java.awt.Font("Nirmala UI", 3, 24)); // NOI18N
        jLabel8.setText("Syntactic Analyzer");

        btnSyntacticAnalyze.setBackground(new java.awt.Color(51, 51, 51));
        btnSyntacticAnalyze.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSyntacticAnalyze.setForeground(new java.awt.Color(255, 255, 255));
        btnSyntacticAnalyze.setText("ANALYZE");
        btnSyntacticAnalyze.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSyntacticAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSyntacticAnalyzeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 275, Short.MAX_VALUE)
                        .addComponent(btnSyntacticAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(btnSyntacticAnalyze))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtSemanticResult.setBackground(new java.awt.Color(204, 204, 204));
        txtSemanticResult.setColumns(20);
        txtSemanticResult.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtSemanticResult.setRows(5);
        jScrollPane5.setViewportView(txtSemanticResult);

        jLabel6.setFont(new java.awt.Font("Nirmala UI", 3, 24)); // NOI18N
        jLabel6.setText("Semantic Analyzer");

        btnSemanticAnalyze.setBackground(new java.awt.Color(51, 51, 51));
        btnSemanticAnalyze.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnSemanticAnalyze.setForeground(new java.awt.Color(255, 255, 255));
        btnSemanticAnalyze.setText("ANALYZE");
        btnSemanticAnalyze.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSemanticAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSemanticAnalyzeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                        .addComponent(btnSemanticAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnSemanticAnalyze))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );

        txtInput.setColumns(20);
        txtInput.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtInput.setRows(5);
        jScrollPane2.setViewportView(txtInput);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel5.setText("Input:");

        btnAnalyze.setBackground(new java.awt.Color(0, 51, 204));
        btnAnalyze.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnAnalyze.setForeground(new java.awt.Color(255, 255, 255));
        btnAnalyze.setText("ANALYZE");
        btnAnalyze.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalyzeActionPerformed(evt);
            }
        });

        btnClean.setBackground(new java.awt.Color(204, 204, 0));
        btnClean.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnClean.setForeground(new java.awt.Color(255, 255, 255));
        btnClean.setText("CLEAN");
        btnClean.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClean.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanActionPerformed(evt);
            }
        });

        btnOpenFile.setBackground(new java.awt.Color(153, 153, 153));
        btnOpenFile.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnOpenFile.setForeground(new java.awt.Color(255, 255, 255));
        btnOpenFile.setText("OPEN FILE");
        btnOpenFile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                        .addComponent(btnOpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClean, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAnalyze, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnAnalyze)
                    .addComponent(btnClean)
                    .addComponent(btnOpenFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnExec.setBackground(new java.awt.Color(51, 51, 51));
        btnExec.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnExec.setForeground(new java.awt.Color(255, 255, 255));
        btnExec.setText("EXECUTE");
        btnExec.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(btnExec, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClose)
                        .addComponent(btnExec))))
        );

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));

        txtTranslatorResult.setBackground(new java.awt.Color(204, 204, 204));
        txtTranslatorResult.setColumns(20);
        txtTranslatorResult.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtTranslatorResult.setRows(5);
        jScrollPane4.setViewportView(txtTranslatorResult);

        jLabel2.setFont(new java.awt.Font("Nirmala UI", 3, 24)); // NOI18N
        jLabel2.setText("Code Translator");

        btnTranslate.setBackground(new java.awt.Color(51, 51, 51));
        btnTranslate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnTranslate.setForeground(new java.awt.Color(255, 255, 255));
        btnTranslate.setText("TRANSLATE");
        btnTranslate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnTranslate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTranslateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTranslate, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnTranslate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtIcodeResult.setBackground(new java.awt.Color(204, 204, 204));
        txtIcodeResult.setColumns(20);
        txtIcodeResult.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtIcodeResult.setRows(5);
        jScrollPane6.setViewportView(txtIcodeResult);

        jLabel3.setFont(new java.awt.Font("Nirmala UI", 3, 24)); // NOI18N
        jLabel3.setText("Intermediate Code");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLexicalAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLexicalAnalyzeActionPerformed
         try {
            lexicalAnalyze();
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLexicalAnalyzeActionPerformed

    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanActionPerformed
        // Limpiar el campo de texto de entrada ("txtInput") y el área de resultados ("txtResult")
        txtInput.setText("");
        txtLexicalResult.setText("");
        txtSyntacticResult.setText("");
        txtSemanticResult.setText("");
        txtTranslatorResult.setText("");
        txtIcodeResult.setText("");
    }//GEN-LAST:event_btnCleanActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSyntacticAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyntacticAnalyzeActionPerformed
        try {
            syntacticAnalyze();
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSyntacticAnalyzeActionPerformed

    private void btnOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenFileActionPerformed
        // TODO add your handling code here:
         JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File archivo = new File(chooser.getSelectedFile().getAbsolutePath());
        
        try {
            String ST = new String(Files.readAllBytes(archivo.toPath()));
            txtInput.setText(ST);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnOpenFileActionPerformed

    private void btnAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyzeActionPerformed
        // TODO add your handling code here:
        String palabra = "Error";
        
         try {
            lexicalAnalyze();
            syntacticAnalyze();
            semanticAnalyze();
            
            //Traductor();
            String sintactico = txtSyntacticResult.getText().toString().trim();
            String semantico = txtSemanticResult.getText().toString().trim();
            if (sintactico.toLowerCase().contains(palabra.toLowerCase()) || semantico.toLowerCase().contains(palabra.toLowerCase())){
                txtTranslatorResult.setText("Hay errores en el codigo, no es posible traducir.");
                txtIcodeResult.setText("Hay errores en el codigo.\n No es posible generar el codigo intermedio.");
            }else{
                Traductor();
                CodigoIntermedio();
                btnExec.setEnabled(true);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAnalyzeActionPerformed

    private void btnSemanticAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSemanticAnalyzeActionPerformed
        try {
            semanticAnalyze();
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSemanticAnalyzeActionPerformed

    private void btnTranslateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTranslateActionPerformed
        try {
            Traductor();
        } catch (IOException ex) {
            Logger.getLogger(Frm1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTranslateActionPerformed

    private void btnExecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecActionPerformed
        // TODO add your handling code here:
        if (!"".equals(traducido)){
            RunCprogram();
        } else{
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "No hay un codigo traducido que ejecutar");
        }
    }//GEN-LAST:event_btnExecActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalyze;
    private javax.swing.JButton btnClean;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExec;
    private javax.swing.JButton btnLexicalAnalyze;
    private javax.swing.JButton btnOpenFile;
    private javax.swing.JButton btnSemanticAnalyze;
    private javax.swing.JButton btnSyntacticAnalyze;
    private javax.swing.JButton btnTranslate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea txtIcodeResult;
    private javax.swing.JTextArea txtInput;
    private javax.swing.JTextArea txtLexicalResult;
    private javax.swing.JTextArea txtSemanticResult;
    private javax.swing.JTextArea txtSyntacticResult;
    private javax.swing.JTextArea txtTranslatorResult;
    // End of variables declaration//GEN-END:variables
}
