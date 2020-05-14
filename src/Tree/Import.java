/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Analyzers.parser;
import Analyzers.scanner;
import Enviroment.Enviroment;
import Enviroment.Sym;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import proyecto_olc1.ErrorTable;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Import implements Instruction
{
    private Expression path;
    public int line, column;
    
    public Import(Expression path, int line, int column)
    {
        this. path = path;
        this.line = line;
        this.column = column;
    }

    @Override
    public Object execute(Enviroment env)
    {
        String localPath = System.getProperty("user.dir") + "\\input\\" + ((Sym)path.execute(env)).toString();
        String aux, input = "";
        boolean error = false;
        
        File file = new File(localPath);
        if(file.exists())
        {
            try 
            {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                while ((aux = br.readLine()) != null) input += aux + "\n";
                
                scanner scanner = new scanner(new BufferedReader(new StringReader(input)));
                parser parser = new parser(scanner);
                parser.parse();
                
                LinkedList<Instruction> root = parser.AST;
                
                if(!scanner.errorList.isEmpty())
                {
                    Iterator<Error_> iterator = scanner.errorList.iterator();
                    while(iterator.hasNext()) env.getGlobal().errors.add(iterator.next());
                    error = true;
                }

                if(!parser.errorList.isEmpty())
                {
                    Iterator<Error_> iterator = parser.errorList.iterator();
                    while(iterator.hasNext()) env.getGlobal().errors.add(iterator.next());
                    error = true;
                }

                if(error)
                {
                    JOptionPane.showMessageDialog(null, "Se han encontrado uno o varios errores en el analisis de los archivos que se desean importar, verificar en \"REPORTES\" para tabla en formato html");
                }
                else
                {
                    Enviroment envImport = new Enviroment(null);
                    envImport.setGlobal(true);
                    executeImports(root, env, envImport);
                    executeAST(root, env, envImport);
                }
            }
            catch(Exception ex) 
            {
                System.out.println(ex.toString());
                Error_ error_ = new Error_(line, column, "De ejecucion", "Excepcion no controlada en sentencia 'Import'. " + ex.toString());
                env.getGlobal().errors.add(error_);
            }
        }
        return null;
    }
    
    void executeImports(LinkedList<Instruction> root, Enviroment env, Enviroment envImport)
    {
        if(root == null)
        {
            System.out.println("No es posible ejecutar las instrucciones porque\r\n"
                    + "el árbol no fue cargado de forma adecuada por la existencia\r\n"
                    + "de errores léxicos o sintácticos.");
            return;
        }
        
        for(Instruction ins : root)
        {
            if(ins != null && (ins instanceof Import))
            {
                ins.execute(env);
            }
        }
        
        if(!envImport.errors.isEmpty())
        {
            Iterator<Error_> iterator = envImport.errors.iterator();
            while(iterator.hasNext()) env.getGlobal().errors.add(iterator.next());
            JOptionPane.showMessageDialog(null, "Se han encontrado uno o varios errores en el analisis de los archivos que se desean importar, verificar en \"REPORTES\" para tabla en formato html");
        }
    }
    
    void executeAST(LinkedList<Instruction> root, Enviroment env, Enviroment envImport)
    {
        if(root == null)
        {
            System.out.println("No es posible ejecutar las instrucciones porque\r\n"
                    + "el árbol no fue cargado de forma adecuada por la existencia\r\n"
                    + "de errores léxicos o sintácticos.");
            return;
        }
        
        int classCounter = 0;
        for(Instruction ins : root)
        {
            if(ins != null && (ins instanceof Class_))
            {
                classCounter++;
                if(classCounter > 1)
                {
                    Error_ error = new Error_(ins.getLine(), ins.getColumn(), "Sintactico", "Elemento sintactico desconocido: 'class'.");
                    env.getGlobal().errors.add(error);
                    break;
                }
                ins.execute(envImport);
            }
        }
        
        if(!envImport.errors.isEmpty())
        {
            Iterator<Error_> iterator = envImport.errors.iterator();
            while(iterator.hasNext()) env.getGlobal().errors.add(iterator.next());
            JOptionPane.showMessageDialog(null, "Se han encontrado uno o varios errores en el analisis de los archivos que se desean importar, verificar en \"REPORTES\" para tabla en formato html");
            return;
        }
        
        Iterator<String> sout = envImport.printList.iterator();
        while(sout.hasNext()) env.printList.add(sout.next());
        //env.getGlobal().setPrevious(envImport);
        env.getPreviousNull().setPrevious(envImport);
    }
    
    @Override
    public int getLine()
    {
        return line;
    }
    
    @Override
    public int getColumn()
    {
        return column;
    }
}
