/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import Enviroment.Sym;
import Enviroment.Sym.EnumType;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import proyecto_olc1.Error_;
import proyecto_olc1.Proyecto_OLC1;

/**
 *
 * @author Javier Bran
 */
public class GraphDot implements Instruction
{
    Expression name, dot;
    public int line, column;
    
    public GraphDot(Expression name, Expression dot, int line, int column)
    {
        this.name = name;
        this.dot = dot;
        this.line = line;
        this.column = column;
    }

    @Override
    public Object execute(Enviroment env) 
    {
        name.paramsResult = executeParams(name, env);
        dot.paramsResult = executeParams(dot, env);
        Sym nameSym = (Sym)name.execute(env);
        Sym dotSym = (Sym)dot.execute(env);
        
        if(nameSym.type == EnumType.cadena && dotSym.type == EnumType.cadena)
        {
            ProcessBuilder pbuilder;
            FileWriter createDot = null;
            PrintWriter pw = null;
            String imgName = nameSym.value.toString();
            String dotName = imgName.substring(0, nameSym.value.toString().length() - 4);
            String dotPath = System.getProperty("user.dir") + "\\graphviz\\dots\\" + dotName + ".dot";
            String imgPath = System.getProperty("user.dir") + "\\graphviz\\images\\" + imgName;
            
            try 
            {
                createDot = new FileWriter(dotPath);
                pw = new PrintWriter(createDot);
                pw.print(dotSym.toString());
                createDot.close();
                if(imgName.endsWith(".png")) pbuilder = new ProcessBuilder( "dot", "-Tpng", dotPath, "-o", imgPath);
                else if(imgName.endsWith(".pdf")) pbuilder = new ProcessBuilder( "dot", "-Tpdf", dotPath, "-o", imgPath);
                else pbuilder = new ProcessBuilder( "dot", "-Tjpg", dotPath, "-o", imgPath);
                pbuilder.redirectErrorStream(true);
                pbuilder.start();
            }
            catch(IOException ex) 
            {
                Error_ error = new Error_(line, column, "De ejecucion", "Excepcion no controlada en sentencia 'Graficar_dot'.");
                env.getGlobal().errors.add(error);
            }
        }
        return null;
    }
    
    public LinkedList<Sym> executeParams(Expression e, Enviroment env)
    {
        LinkedList<Sym> paramsResult = null;
        
        if(e != null && e.parameters != null)
        {
            paramsResult = new LinkedList<>();
            for(int i = 0; i < e.parameters.size(); i++)
            {
                Expression exp = e.parameters.get(i);
                exp.paramsResult = executeParams(exp, env);
                Object r = exp.execute(env);
                if(r instanceof Sym)
                {
                    final Sym s = (Sym) r;
                    if(paramsResult.size() < e.parameters.size()) paramsResult.add(i, s);
                    else paramsResult.set(i, s);
                }
            }
        }
        return paramsResult;
    }
    
    public int getLine()
    {
        return line;
    }
    
    public int getColumn()
    {
        return column;
    }
}
