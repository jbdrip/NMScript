package Analyzers;

import java_cup.runtime.Symbol;
import java.util.ArrayList;
import proyecto_olc1.*;

%%

%{
    public ArrayList<Error_> errorList = new ArrayList<Error_>();
    public PaintLetters paint = new PaintLetters();
    String cadena = "";
    int iniCadena = 0, lengthCadena = 0;
%}


%class scanner
%public
%line
%char
%cup
%unicode
%ignorecase 
%8bit
%full


%init{ 
    yyline = 0; 
    yychar = 1; 
%init}

%state ESPECIAL
%state CADENA

entero = [0-9]+
doble = {entero}"."{entero}
letra = [a-zA-ZÑñ]
caracter = ("'").("'")
id = {letra}({letra}|[0-9]|"_")*
Comentario = "//"([^\r\n]*[^\r\n])*
ComentarioMulti = ("/*"[^/]~"*/")|("/*"[^*/]*"*/")
espacio = (\t|\f|" "|\r)

%%

//<YYINITIAL> "'" { yybegin(ESPECIAL); }
//<ESPECIAL> "\\"

<YYINITIAL> "\"" {cadena = ""; iniCadena = yychar; lengthCadena = 0; lengthCadena++; yybegin(CADENA);}

<CADENA> "\\" {yybegin(ESPECIAL); lengthCadena++;}
<CADENA> [^\"] {cadena += yytext(); lengthCadena++;}
<CADENA> "\"" {yybegin(YYINITIAL); lengthCadena++; return new Symbol(sym.cadena, iniCadena, yyline + 1,  "\"" + cadena + "\"");}

<ESPECIAL> "\\" {cadena += "\\"; yybegin(CADENA);}
<ESPECIAL> "n" {cadena += "\n"; yybegin(CADENA);}
<ESPECIAL> "r" {cadena += "\r"; yybegin(CADENA);}
<ESPECIAL> "t" {cadena += "\t"; yybegin(CADENA);}
<ESPECIAL> "\"" {cadena += "\""; yybegin(CADENA);}

<YYINITIAL> "int" {return new Symbol(sym.int_, yychar, yyline + 1, yytext());}
<YYINITIAL> "double" {return new Symbol(sym.double_, yychar, yyline + 1, yytext());}
<YYINITIAL> "char" {return new Symbol(sym.char_, yychar, yyline + 1, yytext());}
<YYINITIAL> "String" {return new Symbol(sym.string_, yychar, yyline + 1, yytext());}
<YYINITIAL> "boolean" {return new Symbol(sym.boolean_, yychar, yyline + 1, yytext());}
<YYINITIAL> "null" {return new Symbol(sym.null_, yychar, yyline, yytext());}
<YYINITIAL> "true" {return new Symbol(sym.true_, yychar, yyline + 1, yytext());}
<YYINITIAL> "false" {return new Symbol(sym.false_, yychar , yyline + 1, yytext());}

<YYINITIAL> "if" {return new Symbol(sym.if_, yychar, yyline + 1, yytext());}
<YYINITIAL> "else" {return new Symbol(sym.else_, yychar, yyline + 1, yytext());}
<YYINITIAL> "for" {return new Symbol(sym.for_, yychar, yyline + 1, yytext());}
<YYINITIAL> "do" {return new Symbol(sym.do_, yychar, yyline + 1, yytext());}
<YYINITIAL> "while" {return new Symbol(sym.while_, yychar, yyline + 1, yytext());}
<YYINITIAL> "switch" {return new Symbol(sym.switch_, yychar , yyline + 1, yytext());}
<YYINITIAL> "case" {return new Symbol(sym.case_, yychar, yyline + 1, yytext());}
<YYINITIAL> "default" {return new Symbol(sym.default_, yychar, yyline + 1, yytext());}
<YYINITIAL> "break" {return new Symbol(sym.break_, yychar, yyline + 1, yytext());}
<YYINITIAL> "continue" {return new Symbol(sym.continue_, yychar, yyline + 1, yytext());}
<YYINITIAL> "return" {return new Symbol(sym.return_, yychar, yyline + 1, yytext());}
<YYINITIAL> "class" {return new Symbol(sym.class_, yychar, yyline + 1, yytext());}
<YYINITIAL> "new" {return new Symbol(sym.new_, yychar, yyline + 1, yytext());}
<YYINITIAL> "this" {return new Symbol(sym.this_, yychar, yyline + 1, yytext());}
<YYINITIAL> "void" {return new Symbol(sym.void_, yychar, yyline + 1, yytext());}
<YYINITIAL> "print" {return new Symbol(sym.print_, yychar, yyline + 1, yytext());}
<YYINITIAL> "println" {return new Symbol(sym.println_, yychar, yyline + 1, yytext());}
<YYINITIAL> "import" {return new Symbol(sym.import_, yychar, yyline + 1, yytext());}
<YYINITIAL> "Graficar_dot" {return new Symbol(sym.graficar_dot, yychar, yyline + 1, yytext());}
<YYINITIAL> "Graficar_entornos" {return new Symbol(sym.graficar_env, yychar, yyline + 1, yytext());}

<YYINITIAL> "." {return new Symbol(sym.punto, yychar, yyline + 1, yytext());}
<YYINITIAL> "," {return new Symbol(sym.coma, yychar, yyline + 1, yytext());}
<YYINITIAL> ";" {return new Symbol(sym.puntoComa, yychar, yyline + 1, yytext());}
<YYINITIAL> ":" {return new Symbol(sym.dosPuntos, yychar, yyline + 1, yytext());}
<YYINITIAL> "+" {return new Symbol(sym.mas, yychar, yyline + 1, yytext());}
<YYINITIAL> "-" {return new Symbol(sym.menos, yychar, yyline + 1, yytext());}
<YYINITIAL> "*" {return new Symbol(sym.por, yychar, yyline + 1, yytext());}
<YYINITIAL> "/" {return new Symbol(sym.dividido, yychar, yyline + 1, yytext());}
<YYINITIAL> "**" {return new Symbol(sym.potencia, yychar, yyline + 1, yytext());}
<YYINITIAL> "%" {return new Symbol(sym.modulo, yychar, yyline + 1, yytext());}
<YYINITIAL> ">" {return new Symbol(sym.mayorQue, yychar, yyline + 1, yytext());}
<YYINITIAL> "<" {return new Symbol(sym.menorQue, yychar, yyline + 1, yytext());}
<YYINITIAL> ">=" {return new Symbol(sym.mayorIgual, yychar, yyline + 1, yytext());}
<YYINITIAL> "<=" {return new Symbol(sym.menorIgual, yychar, yyline + 1, yytext());}
<YYINITIAL> "==" {return new Symbol(sym.igualIgual, yychar, yyline + 1, yytext());}
<YYINITIAL> "!=" {return new Symbol(sym.diferenteIgual, yychar, yyline + 1, yytext());}
<YYINITIAL> "&&" {return new Symbol(sym.and, yychar, yyline + 1, yytext());}
<YYINITIAL> "||" {return new Symbol(sym.or, yychar, yyline + 1, yytext());}
<YYINITIAL> "^" {return new Symbol(sym.xor, yychar, yyline + 1, yytext());}
<YYINITIAL> "!" {return new Symbol(sym.not, yychar, yyline + 1, yytext());}
<YYINITIAL> "=" {return new Symbol(sym.igual, yychar, yyline + 1, yytext());}
<YYINITIAL> "++" {return new Symbol(sym.masMas, yychar, yyline + 1, yytext());}
<YYINITIAL> "--" {return new Symbol(sym.menosMenos, yychar, yyline + 1, yytext());}
<YYINITIAL> "(" {return new Symbol(sym.parIzq, yychar, yyline + 1, yytext());}
<YYINITIAL> ")" {return new Symbol(sym.parDer, yychar, yyline + 1, yytext());}
<YYINITIAL> "{" {return new Symbol(sym.llaveAbre, yychar, yyline + 1, yytext());}
<YYINITIAL> "}" {return new Symbol(sym.llaveCierre, yychar, yyline + 1, yytext());}
<YYINITIAL> "[" {return new Symbol(sym.corcheteAbre, yychar, yyline + 1, yytext());}
<YYINITIAL> "]" {return new Symbol(sym.corcheteCierre, yychar, yyline + 1, yytext());}
                                                        //proba con yychar
//<YYINITIAL> {cadena} {return new Symbol(sym.cadena, yychar, yyline, yytext());}                                                        
<YYINITIAL> {caracter} {return new Symbol(sym.caracter, yychar, yyline + 1, yytext());}
<YYINITIAL> {entero} {return new Symbol(sym.entero, yychar, yyline + 1, yytext());}
<YYINITIAL> {doble} {return new Symbol(sym.doble, yychar, yyline + 1, yytext());}
<YYINITIAL> {id} {return new Symbol(sym.id, yychar + 1, yyline + 1, yytext());}

"\n" {yychar = 1;}
<YYINITIAL> {espacio} {/*Ignorar*/}
<YYINITIAL> {Comentario} {yychar = 1;}
<YYINITIAL> {ComentarioMulti} {yychar = 1;}


.
{
    Error_ newError = new Error_(yyline + 1, yychar - 1, "Lexico", "Elemento lexico desconocido: '" + yytext() + "'");
    errorList.add(newError);
}