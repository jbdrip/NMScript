/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enviroment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import proyecto_olc1.Error_;
/**
 *
 * @author Javier Bran
 */
public class Enviroment
{
    public HashMap<String, Sym> table;          //Tabla donde se almacenan las variables
    public ArrayList<Error_> errors;
    public ArrayList<String> printList;
    private boolean global, onClass, onFunctionVoid;
    private Enviroment previous;
    
    public Enviroment(Enviroment previous)
    {
        this.previous = previous;
        this.table = new HashMap<>();
        this.errors = new ArrayList<>();
        this.printList = new ArrayList<>();
        this.global = false;
        this.onClass = false;
        this.onFunctionVoid = false;
    }
    
    public Enviroment getPreviousNull()
    {
        for(Enviroment env = this; env != null; env = env.previous)
        {
            if (env.previous == null) return env;
        }
        return null;
    }
    
    public Enviroment getGlobal()
    {
        for(Enviroment env = this; env != null; env = env.previous)
        {
            if (env.global) return env;
        }
        return null;
    }
    
    public Enviroment getOnClass()
    {
        for(Enviroment env = this; env != null; env = env.previous)
        {
            if (env.onClass) return env;
        }
        return null;
    }
    
    public boolean insert(String name, Sym sym, int line, int column)
    {
        name = name.toLowerCase();
        if(table.containsKey(name)) return false;
        table.put(name, sym);
        return true;
    }
    
    public Sym search(String name, int line, int column)
    {
        name = name.toLowerCase();
        for(Enviroment e = this; e != null; e = e.previous)
        {
            if(e.table.containsKey(name))
            {
                Sym sym = e.table.get(name);
                return sym;
            }
        }
        return null;
    }
    
    public void updateValue(String name, Sym sym, int line , int column)
    {
        name = name.toLowerCase();
        for(Enviroment e = this; e != null; e = e.previous)
        {
            if(e.table.containsKey(name))
            {
                e.table.replace(name, sym);
                return;
            } //e.table.put(name, sym);
        }
    }
    
    public void setGlobal(boolean global)
    {
        this.global = global;
    }
    
    public void setOnClass(boolean onClass)
    {
        this.onClass = onClass;
    }
    
    public void setOnFunctionVoid(boolean onFunctionVoid)
    {
        this.onFunctionVoid = onFunctionVoid;
    }
    
    public boolean getOnClass_()
    {
        return onClass;
    }
    
    /*public boolean getOnFunctionVoid()
    {
        return onFunctionVoid;
    }*/
    
    public void setPrevious(Enviroment previous)
    {
        this.previous = previous;
    }
    
    public Enviroment getPrevious()
    {
        return this.previous;
    }
    
    public void graf()
    {
        String r = "G_envs";
        String ext = "pdf";
        try {
            FileWriter fw = new FileWriter("G_envs.dot");
            PrintWriter pw = new PrintWriter(fw);
            int i = 0;
            pw.println("digraph {\n");
            for (Enviroment e = this; e != null; e = e.previous)
            {
                pw.println(e.draw(i));
                if (e.previous != null) pw.println("node" + (i + 1) + " -> " + "node" + i + ";\n");
                i++;
            }
            pw.println(" }");
            fw.close();
        }
        catch (Exception rep) {
            //proyecto_diciembre.Proyecto_Diciembre.lista_errores.add(new CError("Error al escribir el dot", "Sentencia Graficar_dot ", linea, columna));
        }

        ProcessBuilder pbuilder = new ProcessBuilder("dot", "-T" + ext, "-o", r + "." + ext, r + ".dot");
        pbuilder.redirectErrorStream(true);
        try 
        {
            pbuilder.start();
        } 
        catch (IOException ex) {
            //proyecto_diciembre.Proyecto_Diciembre.lista_errores.add(new CError("Error al graficar", "Sentencia Graficar_dot ", linea, columna));
        }
    }
    
    public String draw(int i)
    {
        String text = "\n";
        String t = "";
        for (String k : this.table.keySet())
        {
            t += "      <tr><td>" + table.get(k).type.toString() + "</td><td>" + k + "</td><td>" + table.get(k).toString() + "</td>" + "</tr>\n";
        }
        text += "node" + i + " ["
                + "    shape=plaintext\n"
                + "    label=<\n"
                + "\n"
                + "      <table cellspacing='0'>\n"
                + "      <tr><td>TIPO</td><td>ID</td><td>VALOR</td></tr>\n"
                + t
                + "    </table>\n"
                + ">];";

        return text;
    }
}
