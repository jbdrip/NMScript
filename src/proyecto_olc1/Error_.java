/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_olc1;

/**
 *
 * @author Javier Bran
 */
public class Error_ 
{
    public int line, column;
    public String type, description;
    
    public Error_(int line, int column, String type, String description)
    {
        this.line = line;
        this.column = column;
        this.type = type;
        this.description = description;
    }
}
