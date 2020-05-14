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
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Void implements Instruction
{
    int line, column;
    String id;
    EnumType type;
    LinkedList<Instruction> instructions;
    LinkedList<Declaration> parameters;
    
    public Void(String id, Sym.EnumType type, LinkedList<Instruction> instructions, LinkedList<Declaration> parameters, int line, int column)
    {
        this.id = id;
        this.type = type;
        this.instructions = instructions;
        this.parameters = parameters;
        this.line = line;
        this.column = column;
    }

    @Override
    public Object execute(Enviroment env)
    {
        ParametersIns parametersIns = new ParametersIns(instructions, parameters);
        Sym sym = new Sym(type, parametersIns);
        if(parameters == null) parameters = new LinkedList<>();
        boolean insert = false;
        
        if(type == EnumType.constructor) insert = env.insert(id + parameters.size() + "??", sym, line, column);
        else if(type == EnumType.metodo) insert = env.insert(id + parameters.size() + "$$", sym, line, column);

        if(!insert)
        {
            Error_ error = new Error_(line, column, "Semantico", "El metodo '" + id + "' ya existe en el ambito actual.");
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
