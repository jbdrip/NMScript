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
public class ParametersIns 
{
    public LinkedList<Instruction> instructions;
    public LinkedList<Declaration> parameters;

    public ParametersIns(LinkedList<Instruction> instructions, LinkedList<Declaration> parameters)
    {
        this.instructions = instructions;
        this.parameters = parameters;
    }
}
