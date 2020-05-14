/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Javier Bran
 */
public class GraphEnv implements Instruction
{
    public int line, column;
    
    public GraphEnv(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    @Override
    public Object execute(Enviroment env)
    {
        env.graf();
        return null;
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
