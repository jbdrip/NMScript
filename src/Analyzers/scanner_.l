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
    int start = 0;
%}


%class scanner_
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
    yychar = 0; 
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
espacio = (\t|\f|" "|\r|\n)

%%

//<YYINITIAL> "'" { yybegin(ESPECIAL); }
//<ESPECIAL> "\\"

<YYINITIAL> "\"" {cadena = ""; iniCadena = yychar; lengthCadena = 0; lengthCadena++; yybegin(CADENA);}

<CADENA> "\\" {yybegin(ESPECIAL); lengthCadena++;}
<CADENA> [^\"] {cadena += yytext(); lengthCadena++;}
<CADENA> "\"" {yybegin(YYINITIAL); lengthCadena++; paint.pintaNara(iniCadena,lengthCadena); return new Symbol(sym.cadena, yychar, yyline + 1,  "\"" + cadena + "\"");}

<ESPECIAL> "\\" {cadena += "\\"; yybegin(CADENA);}
<ESPECIAL> "n" {cadena += "\n"; yybegin(CADENA);}
<ESPECIAL> "r" {cadena += "\r"; yybegin(CADENA);}
<ESPECIAL> "t" {cadena += "\t"; yybegin(CADENA);}
<ESPECIAL> "\"" {cadena += "\""; yybegin(CADENA);}

<YYINITIAL> "int" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.int_, yychar, yyline + 1, yytext());}
<YYINITIAL> "double" {paint.pintaAzul(yychar,yylength());return new Symbol(sym.double_, yychar, yyline + 1, yytext());}
<YYINITIAL> "char" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.char_, yychar, yyline + 1, yytext());}
<YYINITIAL> "String" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.string_, yychar, yyline + 1, yytext());}
<YYINITIAL> "boolean" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.boolean_, yychar, yyline + 1, yytext());}
<YYINITIAL> "null" {return new Symbol(sym.null_, yychar, yyline, yytext());}
<YYINITIAL> "true" {return new Symbol(sym.true_, yychar, yyline + 1, yytext());}
<YYINITIAL> "false" {return new Symbol(sym.false_, yychar , yyline + 1, yytext());}

<YYINITIAL> "if" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.if_, yychar, yyline + 1, yytext());}
<YYINITIAL> "else" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.else_, yychar, yyline + 1, yytext());}
<YYINITIAL> "for" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.for_, yychar, yyline + 1, yytext());}
<YYINITIAL> "do" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.do_, yychar, yyline + 1, yytext());}
<YYINITIAL> "while" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.while_, yychar, yyline + 1, yytext());}
<YYINITIAL> "switch" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.switch_, yychar , yyline + 1, yytext());}
<YYINITIAL> "case" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.case_, yychar, yyline + 1, yytext());}
<YYINITIAL> "default" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.default_, yychar, yyline + 1, yytext());}
<YYINITIAL> "break" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.break_, yychar, yyline + 1, yytext());}
<YYINITIAL> "continue" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.continue_, yychar, yyline + 1, yytext());}
<YYINITIAL> "print" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.print_, yychar, yyline + 1, yytext());}
<YYINITIAL> "println" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.println_, yychar, yyline + 1, yytext());}
<YYINITIAL> "import" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.import_, yychar, yyline + 1, yytext());}
<YYINITIAL> "Graficar_dot" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.graficar_dot, yychar, yyline + 1, yytext());}
<YYINITIAL> "Graficar_entornos" {paint.pintaAzul(yychar,yylength()); return new Symbol(sym.graficar_env, yychar, yyline + 1, yytext());}

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
/*<YYINITIAL> "[" {return new Symbol(sym.corcheteAbre, yychar, yyline, yytext());}
<YYINITIAL> "]" {return new Symbol(sym.corcheteCierre, yychar, yyline, yytext());}*/
                                                        //proba con yychar
//<YYINITIAL> {cadena} {return new Symbol(sym.cadena, yychar, yyline, yytext());}                                                        
<YYINITIAL> {caracter} {paint.pintaNara(yychar,yylength()); return new Symbol(sym.caracter, yychar, yyline + 1, yytext());}
<YYINITIAL> {entero} {paint.pintaMora(yychar,yylength()); return new Symbol(sym.entero, yychar, yyline + 1, yytext());}
<YYINITIAL> {doble} {paint.pintaMora(yychar,yylength()); return new Symbol(sym.doble, yychar, yyline + 1, yytext());}
<YYINITIAL> {id} {return new Symbol(sym.id, yychar + 1, yyline + 1, yytext());}

<YYINITIAL> {espacio} {/*Ignorar*/}
<YYINITIAL> {Comentario} {paint.pintaGris(yychar,yylength());}
<YYINITIAL> {ComentarioMulti} {paint.pintaGris(yychar,yylength());}


.
{
    /*Error_ newError = new Error_(yyline + 1, yychar - 1, "Lexico", "Elemento lexico desconocido: '" + yytext() + "'");
    errorList.add(newError);*/
}