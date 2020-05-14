SET JFLEX_HOME= C:\libs\JFLEX\jflex-1.7.0
cd C:\Users\Javier Bran\Documents\NetBeansProjects\Proyecto_OLC1\src\Analyzers
java -jar %JFLEX_HOME%\lib\jflex-full-1.7.0.jar scanner_.l
pause

SET JFLEX_HOME= C:\libs\JFLEX
cd C:\Users\Javier Bran\Documents\NetBeansProjects\Proyecto_OLC1\src\Analyzers
java -jar %JFLEX_HOME%\java-cup-11b.jar -parser parser_ parser_.cup
pause