/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import Enviroment.Sym;
import java.util.LinkedList;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Class_ implements Instruction
{
    LinkedList<Instruction> instructions;
    int line, column;
    String id;
    
    public Class_(String id, LinkedList<Instruction> instructions, int line, int column)
    {
        this.id = id;
        this.instructions = instructions;
        this.line = line;
        this.column = column;
    }
    
    @Override
    public Object execute(Enviroment env)
    {
        if(instructions != null)
        {
            Sym sym = new Sym(Sym.EnumType.clase, instructions);
            boolean insert = env.insert(id + "##", sym, line, column);

            if(!insert)
            {
                Error_ error = new Error_(line, column, "Semantico", "La clase '" + id + "' ya existe en el ambito actual.");
                env.getGlobal().errors.add(error);
            }
        }
        else
        {
            Error_ error = new Error_(line, column, "De ejecucion", "La clase '" + id + "' no tiene contenido (clase vacia).");
            env.getGlobal().errors.add(error);
        }
        return null;
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
