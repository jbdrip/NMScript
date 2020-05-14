/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;

/**
 *
 * @author Javier Bran
 */
public class Break implements Instruction
{
    public int line, column;
    
    public Break(int line, int column)
    {
        this.line = line;
        this.column = column;
    }
    public Object execute(Enviroment env)
    {
        return "break";
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
