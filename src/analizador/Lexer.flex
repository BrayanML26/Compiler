package analizador;

import static analizador.Tokens.*;

%%
%class Lexer
%type Tokens

// Definición de patrones regulares:
L=[a-zA-Z_]+ // Definición de un identificador que comienza con letras o guion bajo, seguido de más letras o dígitos.
D=[0-9]+ // Definición de un número (dígitos).
espacio=[ ,\t,\r]+ // Definición de espacios en blanco (incluye espacios, tabs).

%{
    public String lexeme;
%}
%%

/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
( "//"(.)* ) {/*Ignore*/}

/* Salto de linea */
( "\n" ) {return Salto_Linea;}

/* Comillas */
( "\"" ) {lexeme=yytext(); return Comillas;}

/* Tipos de datos */
( byte | int | char | long | float | double ) {lexeme=yytext(); return Tipo_dato;}

/* Tipo de dato String */
( String ) {lexeme=yytext(); return Cadena;}

/* Palabra reservada If */
( if ) {lexeme=yytext(); return If;}

/* Palabra reservada Else */
( else ) {lexeme=yytext(); return Else;}

/* Palabra reservada Do */
( do ) {lexeme=yytext(); return Do;}

/* Palabra reservada While */
( while ) {lexeme=yytext(); return While;}

/* Palabra reservada For */
( for ) {lexeme=yytext(); return For;}

/* Operador Igual */
( "=" ) {lexeme=yytext(); return Igual;}

/* Operador Suma */
( "+" ) {lexeme=yytext(); return Suma;}

/* Operador Resta */
( "-" ) {lexeme=yytext(); return Resta;}

/* Operador Multiplicacion */
( "*" ) {lexeme=yytext(); return Multiplicacion;}

/* Operador Division */
( "/" ) {lexeme=yytext(); return Division;}

/* Operador Mod */
( "%" ) {lexeme=yytext(); return Mod;}

/* Operadores logicos */
( "&&" | "||" | "!" | "&" | "|" ) {lexeme=yytext(); return Operador_logico;}

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" | "<<" | ">>" ) {lexeme = yytext(); return Operador_relacional;}

/* Operadores Atribucion */
( "+=" | "-="  | "*=" | "/=" | "%=" ) {lexeme = yytext(); return Operador_atribucion;}

/* Operadores Incremento y decremento */
( "++" | "--" ) {lexeme = yytext(); return Operador_incremento;}

/*Operadores Booleanos*/
(true | false)      {lexeme = yytext(); return Operador_booleano;}

/* Parentesis de apertura */
( "(" ) {lexeme=yytext(); return Parentesis_abierto;}

/* Parentesis de cierre */
( ")" ) {lexeme=yytext(); return Parentesis_cerrado;}

/* Llave de apertura */
( "{" ) {lexeme=yytext(); return Llave_abierta;}

/* Llave de cierre */
( "}" ) {lexeme=yytext(); return Llave_cerrada;}

/* Corchete de apertura */
( "[" ) {lexeme = yytext(); return Corchete_abierto;}

/* Corchete de cierre */
( "]" ) {lexeme = yytext(); return Corchete_cerrado;}

/* Marcador de inicio de algoritmo */
( "main" ) {lexeme=yytext(); return Main;}

/* Punto y coma */
( ";" ) {lexeme=yytext(); return Punto_coma;}

/* Imprimir */
( "print" ) {lexeme=yytext(); return Print;}

/* Palabras en cadena de caracteres */
( "\"" [^\r\n\"]* "\"" ) {lexeme=yytext(); return String_lit;}

/* Identificador */
{L}({L}|{D})* {lexeme=yytext(); return Identificador;} // Retorna identificadores que comienzan con una letra o guion bajo.

/* Numero */
("(-"{D}+")")|{D}+ {lexeme=yytext(); return Numero;} // Retorna números.

/* Numero Decimal */
("-"?{D}+"."{D}+)|("-"?{D}+) { lexeme=yytext(); return NumeroDecimal; }

/* Error de analisis */
 . {return ERROR;} // Si ningún patrón coincide, se retorna un token de error.