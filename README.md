# **Small Compiler in Java**

## **Description**

This Java application allows performing lexical, syntactic, and semantic analysis of source code in a specific language. The project also includes the ability to translate the code to another language, in this case, C, and generate intermediate code. This tool is useful for code validation and translation in a development environment.

## **Features**

- **Lexical Analysis:** Identifies and classifies tokens in the source code (e.g., keywords, operators, identifiers, etc.).
- **Syntactic Analysis:** Verifies the grammatical structure of the code and detects syntax errors.
- **Semantic Analysis:** Verifies the proper use of variables and their data types, and detects semantic errors such as the use of undeclared variables.
- **File Loading:** Allows loading source code files from the local file system.
- **Intermediate Code Generation:** After successful analysis, intermediate code is generated that can be executed.
- **Execute Code:** If no errors are found, the option to execute the translated code is enabled.

## **Requirements**

- **JDK 21 or higher** to compile and run the code.
- **Recommended IDE:** NetBeans to work with the project.
- **Additional libraries:**
  - JFlex and CUP for lexical and syntactic analysis.
  - Java Swing for the graphical user interface.

## **Installation**

1. **Clone the repository or download the project files:**
   - If you don't have the repository, clone it using Git:
     ```bash
     git clone https://github.com/BrayanML/Compiler.git
     ```

2. **Open the project in your IDE:**
   - Import the project into your favorite IDE (e.g., NetBeans).
   - Make sure the JDK is configured in your development environment.

3. **Compile the project:**
   - Once imported, compile the project to ensure all dependencies are resolved.

## **Usage**

### **Graphical User Interface (GUI)**

The application has a Java Swing-based graphical interface. The following actions are available:

1. **Open File:** Allows loading a source code file from the local file system.
2. **Clear Fields:** Clears the contents of the input and result fields.
3. **Lexical Analysis:** Performs lexical analysis of the loaded code, showing the tokens found.
4. **Syntactic Analysis:** Performs syntactic analysis to verify that the code adheres to the defined grammar.
5. **Semantic Analysis:** Performs semantic analysis to verify the consistency of the code, such as correct variable declarations.
6. **Execute Code:** If the code has been successfully translated, it allows executing the generated intermediate code.

### **Steps to Perform an Analysis:**

1. **Open File:** Load a source code file (e.g., `.txt`).
2. **Perform Analysis:**
   - **Lexical Analysis:** Analyzes the tokens in the source code.
   - **Syntactic Analysis:** Verifies that the grammatical structure of the code is valid.
   - **Semantic Analysis:** Verifies the validity of variable declarations and their use.
3. **Generate Intermediate Code:** If no errors are detected, the intermediate code is generated.
4. **Execute Code:** If valid intermediate code is generated, you can execute it.

### **Interface Buttons:**
- **Open File:** Opens a source code file.
- **Clear:** Clears the input and result text fields.
- **Lexical Analysis:** Shows the results of lexical analysis.
- **Syntactic Analysis:** Shows the results of syntactic analysis.
- **Semantic Analysis:** Shows the results of semantic analysis.
- **Translate:** Translates the code into an intermediate format.
- **Execute:** Executes the generated intermediate code.

## **Intermediate Code**

The application generates intermediate code. This is a more abstract representation that can be interpreted or compiled to run on a machine.

## **Error Handling**

- If an error is detected during any of the analyses (lexical, syntactic, or semantic), the system will display a message in the graphical interface indicating the error type and the line where it occurred.
- If the code is valid, the system will generate the intermediate code and it will be ready for execution.

## **Example Usage**

1. Open a source code file:
   - Use the **Open File** button to select a source code file from your computer.
   
2. Perform the analysis:
   - Click **Lexical Analysis**, **Syntactic Analysis**, and **Semantic Analysis** to validate the code.

3. Translate the code:
   - If no errors are detected, the intermediate code will be generated.

4. Execute the code:
   - Click **Execute** to run the generated code.

## **Common Errors**

- **Syntax Error:** This may occur if the code does not follow the language's rules. Ensure the code is well-formed and adheres to the defined grammar.
- **Semantic Error:** Occurs when a variable is used without being declared or a value of an incorrect type is assigned.

## **Contributions**

If you would like to contribute to the project, please follow these steps:

1. Fork the project.
2. Create a new branch (`git checkout -b feature-new-feature`).
3. Make your changes and commit them (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push -u origin feature-new-feature`).
5. Create a pull request on the main repository.

## **Sample Code**

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
