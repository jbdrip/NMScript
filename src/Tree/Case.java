/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.util.LinkedList;

/**
 *
 * @author Javier Bran
 */
public class Case 
{
    public Expression operation;
    public LinkedList<Instruction> instructions;
    public int line, column;

    public Case(Expression operation, LinkedList<Instruction> instructions, int line, int column)
    {
        this.operation = operation;
        this.instructions = instructions;
        this.line = line;
        this.column = column;
    }
}
