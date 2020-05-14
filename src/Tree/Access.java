/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import Enviroment.Sym;
import Enviroment.Sym.EnumType;
import java.util.LinkedList;
import java.util.Map;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Access implements Instruction
{
    int line, column;
    private Expression objectId;
    private Expression valueId;
    public boolean left;

    public Access(Expression objectId, Expression valueId, int line, int column)
    {
        this.objectId = objectId;
        this.valueId = valueId;
        this.line = line;
        this.column = column;
        left = false;
    }
    
    @Override
    public Object execute(Enviroment env)
    {
        Object result = objectId.execute(env);
        if(result instanceof Sym)
        {
            Sym sym = (Sym) result;
            if(sym.type == Sym.EnumType.objeto)
            {
                if(sym.value != null)
                {
                    Enviroment objectEnv = (Enviroment) sym.value;
                    Object r = valueId.execute(objectEnv);
                    return r;
                }
                else
                {
                    Error_ error = new Error_(line, column, "De ejecucion", "La variable '" + objectId.value.toString() + "' es nula (NullPointerException).");
                    env.getGlobal().errors.add(error);
                    return new Sym(Sym.EnumType.error, "@error");
                }
            }
            else if(sym.type == Sym.EnumType.arreglo)
            {
                if(sym.value != null)
                {
                    Array objectEnv = (Array) sym.value;
                    if(valueId.value.toString().equalsIgnoreCase("size")) return new Sym(EnumType.entero, objectEnv.size);
                }
                else
                {
                    Error_ error = new Error_(line, column, "De ejecucion", "La variable '" + objectId.value.toString() + "' es nula (NullPointerException).");
                    env.getGlobal().errors.add(error);
                    return new Sym(Sym.EnumType.error, "@error");
                }
            }
        }
        return null;
    }
    
    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
