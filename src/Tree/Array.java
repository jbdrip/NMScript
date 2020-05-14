/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Sym.EnumType;
import java.util.LinkedList;

/**
 *
 * @author Javier Bran
 */
public class Array 
{
    public LinkedList<Object> array;
    public EnumType type;
    public long size, dimension;
    public String className;
    
    public Array()
    {
        this.array = new LinkedList<>();
        this.size = 0;
        this.dimension = 0;
        this.type = EnumType.nulo;
        className = "";
    }
}
