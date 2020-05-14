/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import java.util.LinkedList;

/**
 *
 * @author Javier Bran
 */
public interface Instruction 
{
    public int getLine();
    public int getColumn();
    public Object execute(Enviroment env);
}
