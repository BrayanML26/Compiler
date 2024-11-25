# **Pequeño Compilador en Java**

## **Descripción**

Esta aplicación en Java permite realizar un análisis léxico, sintáctico y semántico de un código fuente en un lenguaje específico. El proyecto también incluye la capacidad de traducir el código a otro lenguaje en este caso a C y generar código intermedio. Esta herramienta es útil para la validación y traducción de código en un entorno de desarrollo.

## **Características**

- **Análisis Léxico:** Identifica y clasifica los tokens del código fuente (por ejemplo, palabras clave, operadores, identificadores, etc.).
- **Análisis Sintáctico:** Verifica la estructura gramatical del código y detecta errores de sintaxis.
- **Análisis Semántico:** Verifica el uso adecuado de las variables y su tipo de datos, y detecta errores semánticos como el uso de variables no declaradas.
- **Carga de Archivos:** Permite cargar archivos de código fuente desde el sistema de archivos local.
- **Generación de Código Intermedio:** Después de un análisis exitoso, se genera código intermedio que puede ser ejecutado.
- **Ejecutar Código:** Si no se encuentran errores, se habilita la opción de ejecutar el código traducido.

## **Requisitos**

- **JDK 21 o superior** para compilar y ejecutar el código.
- **IDE recomendada:** NetBeans para trabajar con el proyecto.
- **Librerías adicionales:** 
  - JFlex y CUP para el análisis léxico y sintáctico.
  - Java Swing para la interfaz gráfica de usuario.

## **Instalación**

1. **Clonar el repositorio o descargar los archivos del proyecto:**
   - Si aún no tienes el repositorio, clónalo usando Git:
     ```bash
     git clone https://github.com/BrayanML/Compiler.git
     ```

2. **Abrir el proyecto en tu IDE:**
   - Importa el proyecto en tu IDE favorito (por ejemplo, NetBeans).
   - Asegúrate de tener configurado el JDK en tu entorno de desarrollo.

3. **Compilar el proyecto:**
   - Una vez importado, compila el proyecto para asegurarte de que todas las dependencias estén resueltas.

## **Uso**

### **Interfaz Gráfica de Usuario (GUI)**

La aplicación cuenta con una interfaz gráfica basada en Java Swing. Las siguientes acciones están disponibles:

1. **Abrir archivo:** Permite cargar un archivo de código fuente desde el sistema de archivos local.
2. **Limpiar campos:** Borra el contenido de los campos de entrada y resultados.
3. **Análisis Léxico:** Realiza el análisis léxico del código cargado, mostrando los tokens encontrados.
4. **Análisis Sintáctico:** Realiza el análisis sintáctico para verificar si el código cumple con la gramática definida.
5. **Análisis Semántico:** Realiza un análisis semántico para verificar la consistencia del código, como la correcta declaración de variables.
6. **Ejecutar Código:** Si el código ha sido traducido correctamente, permite ejecutar el código intermedio generado.

### **Pasos para realizar un análisis:**

1. **Abrir archivo:** Carga un archivo de código fuente (por ejemplo, `.txt`).
2. **Realizar Análisis:**
   - **Análisis Léxico:** Analiza los tokens del código fuente.
   - **Análisis Sintáctico:** Verifica que la estructura gramatical del código sea válida.
   - **Análisis Semántico:** Verifica la validez de las declaraciones de variables y su uso.
3. **Generación de Código Intermedio:** Si no se detectan errores, se genera el código intermedio.
4. **Ejecutar el Código:** Si se ha generado un código intermedio válido, puedes ejecutarlo.

### **Botones de la interfaz:**
- **Abrir archivo:** Abre un archivo de código fuente.
- **Limpiar:** Borra los campos de texto de entrada y resultados.
- **Análisis Léxico:** Muestra los resultados del análisis léxico.
- **Análisis Sintáctico:** Muestra los resultados del análisis sintáctico.
- **Análisis Semántico:** Muestra los resultados del análisis semántico.
- **Traducir:** Traduce el código a un formato intermedio.
- **Ejecutar:** Ejecuta el código intermedio generado.

## **Código Intermedio**

La aplicación genera un código intermedio. Esta es una representación más abstracta que puede ser interpretada o compilada para ejecutarse en una máquina.

## **Manejo de Errores**

- Si se detecta un error durante cualquiera de los análisis (léxico, sintáctico o semántico), el sistema mostrará un mensaje en la interfaz gráfica indicando el tipo de error y la línea donde ocurrió.
- Si el código es válido, el sistema generará el código intermedio y estará listo para ser ejecutado.

## **Ejemplo de Uso**

1. Abre un archivo de código fuente:
   - Usa el botón **Abrir archivo** para seleccionar un archivo de código desde tu computadora.
   
2. Realiza el análisis:
   - Haz clic en **Análisis Léxico**, **Análisis Sintáctico**, y **Análisis Semántico** para verificar el código.

3. Traduce el código:
   - Si no se detectan errores, se genera el código intermedio.

4. Ejecuta el código:
   - Haz clic en **Ejecutar** para ejecutar el código generado.

## **Errores Comunes**

- **Error de sintaxis:** Puede ocurrir si el código no sigue las reglas del lenguaje. Asegúrate de que el código esté bien formado y cumpla con la gramática definida.
- **Error semántico:** Ocurre cuando una variable se usa sin ser declarada o se asigna un valor de un tipo incorrecto.

## **Contribuciones**

Si deseas contribuir al proyecto, por favor sigue estos pasos:

1. Haz un fork del proyecto.
2. Crea una nueva rama (`git checkout -b feature-nueva-caracteristica`).
3. Realiza los cambios y haz un commit (`git commit -am 'Añadir nueva característica'`).
4. Haz push a la rama (`git push -u origin feature-nueva-caracteristica`).
5. Crea un pull request en el repositorio principal.

## **Código de Prueba**

   ```bash
    int main() {
      int x = 10;
      float y = 2.5;
  
      int count;
      int letter = 1;
  
      for (count = 0; count < x; count++) {
          letter++;
          y += count;
      }
  
      x = 5; 
      print(x);
      print(y);
      print(letter);
  }
   ```



