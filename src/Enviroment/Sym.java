/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enviroment;

/**
 *
 * @author Javier Bran
 */
public class Sym 
{
    public EnumType type;
    public Object value;
    public boolean breturn;
    
    public Sym(EnumType type, Object value)
    {
        this.type = type;
        this.value = value;
        breturn = false;
    }

    public Sym() {}
    
    public enum EnumType
    {
        entero,
        doble,
        cadena,
        caracter,
        booleano,
        metodo,
        constructor,
        funcion,
        clase,
        objeto,
        nulo,
        arreglo,
        elementoEnArreglo,
        componente,
        error
    }
    
    @Override
    public String toString()
    {
        if(value != null) return value.toString();
        return "null";
    }
}
