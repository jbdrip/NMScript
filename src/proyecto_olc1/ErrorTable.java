/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_olc1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Jose
 */
public class ErrorTable
{
    FileWriter filewriter;
    PrintWriter printw;
    
    public ErrorTable()
    {
        filewriter = null;
        printw = null;
    }
    
    public void buildRepTable(ArrayList<Error_> errorList)
    {
        try
        {
            File directory = new File("reportes");
            Iterator<Error_> iterator = errorList.iterator();
            directory.mkdir();
            File created = new File(System.getProperty("user.dir") + "\\reportes\\TablaErrores.html");
            
            if(created.exists())
            {
                filewriter = new FileWriter(created);
                printw = new PrintWriter(filewriter);
                
                try 
                {
                    FileReader filereader = new FileReader(created);
                    BufferedReader reader = new BufferedReader(filereader);

                    if(reader.readLine() == null)
                    {
                        printw.println("<html>");
                        printw.println("<head align= center>ERRORES</head>");
                        printw.println("<body>");
                        printw.println("<table border= " + '"' + 2 + '"' + "width= " + '"' + 850 + '"' + " align= center>");
                        printw.println("<tr>");
                        printw.println("<td>NO.</td>");
                        printw.println("<td>TIPO</td>");
                        printw.println("<td>DESCRIPCION</td>");
                        printw.println("<td>FILA</td>");
                        printw.println("<td>COLUMNA</td>");
                        printw.println("</tr>");
                    }
                }
                catch (Exception iOException) {}
                
                int counter = 1;
                while(iterator.hasNext())
                {
                    Error_ error = iterator.next();
                    
                    printw.println("<tr>");
                    printw.println("<td>" + counter +"</td>");
                    counter++;
                    
                    switch (error.type) 
                    {
                        case "Lexico":
                            printw.println("<td>Lexico</td>");
                            break;
                        case "Sintactico":
                            printw.println("<td>Sintactico</td>");
                            break;
                        case "Semantico":
                            printw.println("<td>Semantico</td>");
                            break;
                        default:
                            printw.println("<td>De ejecucion</td>");
                            break;
                    }
                    
                    printw.println("<td>" + error.description + "</td>");
                    printw.println("<td>" + error.line + "</td>");
                    printw.println("<td>" + error.column + "</td>");
                    printw.println("</tr>");
                }
                printw.close();
            }
            
            else
            {
                filewriter = new FileWriter(System.getProperty("user.dir") + "\\reportes\\TablaErrores.html");
                printw = new PrintWriter(filewriter);

                printw.println("<html>");
                printw.println("<head align= center>ERRORES</head>");
                printw.println("<body>");
                printw.println("<table border= " + '"' + 2 + '"' + "width= " + '"' + 850 + '"' + " align= center>");
                printw.println("<tr>");
                printw.println("<td>NO.</td>");
                printw.println("<td>TIPO</td>");
                printw.println("<td>DESCRIPCION</td>");
                printw.println("<td>FILA</td>");
                printw.println("<td>COLUMNA</td>");
                printw.println("</tr>");
                
                int counter = 1;
                while(iterator.hasNext())
                {
                    Error_ error = iterator.next();
                    
                    printw.println("<tr>");
                    printw.println("<td>" + counter +"</td>");
                    counter++;
                    
                    switch (error.type) 
                    {
                        case "Lexico":
                            printw.println("<td>Lexico</td>");
                            break;
                        case "Sintactico":
                            printw.println("<td>Sintactico</td>");
                            break;
                        case "Semantico":
                            printw.println("<td>Semantico</td>");
                            break;
                        default:
                            break;
                    }
                    
                    printw.println("<td>" + error.description + "</td>");
                    printw.println("<td>" + error.line + "</td>");
                    printw.println("<td>" + error.column + "</td>");
                    printw.println("</tr>");
                }
                printw.close();
            }
        }
        catch(Exception ex){}
    }
    
}
