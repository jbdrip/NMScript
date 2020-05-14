
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import Enviroment.Sym;
import Enviroment.Sym.EnumType;
import Tree.Expression.Expression_type;
import java.util.LinkedList;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Declaration implements Instruction
{
    public String id;
    private Expression value;
    LinkedList<Sym> symResult;
    LinkedList<Expression> values, arraysSize;
    EnumType type, type1;
    public int line, column;
    int arrayDimension, index;
    
    public Declaration(String id, EnumType type, Expression value, int arraySize, int line, int column)
    {
        this.id = id;
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.arrayDimension = arraySize;
    }
    
    public Declaration(String id, EnumType type, Expression value, int arraySize, LinkedList<Expression> values, int line, int column)
    {
        this.id = id;
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.arrayDimension = arraySize;
        this.values = values;
    }
    
    public Declaration(EnumType type1, String id, EnumType type, int arraySize, LinkedList<Expression> arraysSize, int line, int column)
    {
        this.id = id;
        this.type = type;
        this.type1 = type1;
        this.line = line;
        this.column = column;
        this.arrayDimension = arraySize;
        this.arraysSize = arraysSize;
    }

    @Override
    public Object execute(Enviroment env)
    {
        try
        {
            if(arrayDimension > -1)
            {
                if(values != null)
                {
                    symResult = new LinkedList<>();
                    for(Expression expSize : values)
                    {
                        expSize.paramsResult = executeParams(expSize, env);
                        Object result = expSize.execute(env);
                        if(result instanceof Sym) symResult.add((Sym) result);
                    }
                    
                    if(arrayDimension == 1)
                    {
                        Array array = new Array();
                        array.dimension = 1;
                        array.size = symResult.size();
                        array.type = EnumType.arreglo;
                        array.className = type.toString();
                        for(int i = 0; i < symResult.size(); i++)
                        {
                            array.array.add(symResult.get(i));
                        }
                        Sym sym = new Sym(EnumType.arreglo, array);
                        boolean insert = env.insert(id, sym, line, column);
                        if(!insert)
                        {
                            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                }
                else
                {
                    if(type1 != null)
                    {
                        if(type == type1)
                        {
                            if(arraysSize != null)
                            {
                                if(arrayDimension == arraysSize.size())
                                {
                                    index = 0;
                                    Array array = new Array();
                                    symResult = new LinkedList<>();
                                    for(Expression expSize : arraysSize)
                                    {
                                        expSize.paramsResult = executeParams(expSize, env);
                                        Object result = expSize.execute(env);
                                        if(result instanceof Sym) symResult.add((Sym) result);
                                    }
                                    initArray(array, arrayDimension - 1, env);
                                    Sym sym = new Sym(EnumType.arreglo, array);
                                    boolean insert = env.insert(id, sym, line, column);
                                    if(!insert)
                                    {
                                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                                        env.getGlobal().errors.add(error);
                                    }
                                }
                                else
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "El numero de dimensiones a asignar es diferente al que se quiere declarar, variable: '" + id + "'.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                        }
                        else
                        {
                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en declaracion, variable: '" + id + "'.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else if(value != null)
                    {
                        value.paramsResult = executeParams(value, env);
                        Sym valueResult = (Sym)value.execute(env);
                        if(valueResult.type == EnumType.arreglo)
                        {
                            if(((Array)valueResult.value).className.equalsIgnoreCase(type.toString()))
                            {
                                Sym sym = new Sym(valueResult.type, valueResult.value);
                                boolean insert = env.insert(id, sym, line, column);

                                if(!insert)
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                            else
                            {
                                Error_ error = new Error_(line, column, "Semantico", "Error de tipos en instancia, ambos arreglos deben ser del mismo tipo, variable: '" + id + "'.");
                                env.getGlobal().errors.add(error);
                            }
                        }
                        else
                        {
                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en instancia, variable: '" + id + "'.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else
                    {
                        Array array = new Array();
                        array.dimension = arrayDimension;
                        array.type = EnumType.arreglo;
                        array.className = type.toString();
                        Sym sym = new Sym(EnumType.arreglo, array);
                        boolean insert = env.insert(id, sym, line, column);
                        if(!insert)
                        {
                            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                }
            }
            else
            {
                if(value != null)
                {
                    value.paramsResult = executeParams(value, env);
                    Sym valueResult = (Sym)value.execute(env);

                    if(valueResult.type == type)
                    {
                        Sym sym = new Sym(valueResult.type, valueResult.value);
                        boolean insert = env.insert(id, sym, line, column);

                        if(!insert)
                        {
                            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "Error de tipos en declaracion, variable: '" + id + "'.");
                        env.getGlobal().errors.add(error);
                    }
                }
                else
                {
                    Sym sym;
                    switch(type)
                    {
                        case entero:
                            long l = 0;
                            sym = new Sym(type, l);
                            break;

                        case doble:
                            sym = new Sym(type, 0.0);
                            break;

                        case cadena:
                            sym = new Sym(type, "");
                            break;

                        case caracter:
                            sym = new Sym(type, '\0');
                            break;

                        case booleano:
                            sym = new Sym(type, false);
                            break;

                        default:
                            sym = new Sym(type, "@null");
                            break;
                    }
                    boolean insert = env.insert(id, sym, line, column);

                    if(!insert)
                    {
                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                        env.getGlobal().errors.add(error);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Error_ error = new Error_(line, column, "De ejecucion", "Excepcion no controlada en declaracion de variable.");
            env.getGlobal().errors.add(error);
        }
    return null;
    }
    
    void initArray(Array array, int dimension, Enviroment env)
    {
        Sym symSize = symResult.get(symResult.size() - (dimension + 1));
        if(symSize.type == EnumType.entero)
        {
            array.size = (long) symSize.value;
            array.dimension = dimension + 1;
            array.type = EnumType.arreglo;
            array.className = type.toString();
            if(dimension > 0)
            {
                for(int i = 0; i < array.size; i++)
                {
                    Array subArray = new Array();
                    initArray(subArray, dimension - 1, env);
                    array.array.add(subArray);
                }
            }
            else
            {
                for(int i = 0; i < array.size; i++)
                {
                    switch(type)
                    {
                        case entero:
                            long l = 0;
                            array.array.add(new Sym(type, l));
                            break;
                        case doble:
                            array.array.add(new Sym(type, 0.0));
                            break;
                        case cadena:
                            array.array.add(new Sym(type, ""));
                            break;
                        case caracter:
                            array.array.add(new Sym(type, '\0'));
                            break;
                        case booleano:
                            array.array.add(new Sym(type, false));
                            break;
                         default:
                            array.array.add(new Sym(EnumType.nulo, "@null"));
                            break;
                    }
                }
            }
        }
        else
        {
            Error_ error = new Error_(line, column, "Semantico", "Error en indice de declaracion de arreglo, variable: '" + id + "' (el indice no es entero).");
            env.getGlobal().errors.add(error);
        }
    }
    
    
    public LinkedList<Sym> executeParams(Expression e, Enviroment env)
    {
        LinkedList<Sym> paramsResult = null;
        
        if(e != null && e.parameters != null)
        {
            paramsResult = new LinkedList<>();
            for(int i = 0; i < e.parameters.size(); i++)
            {
                Expression exp = e.parameters.get(i);
                exp.paramsResult = executeParams(exp, env);
                Object r = exp.execute(env);
                if(r instanceof Sym)
                {
                    final Sym s = (Sym) r;
                    if(paramsResult.size() < e.parameters.size()) paramsResult.add(i, s);
                    else paramsResult.set(i, s);
                }
            }
        }
        return paramsResult;
    }
    
    public EnumType getType(Expression_type type)
    {
        switch(type)
        {
            case ENTERO:
                return EnumType.entero;
                
            case DOBLE:
                return EnumType.doble;
                
            case CADENA:
                return EnumType.cadena;
                
            case CARACTER:
                return EnumType.caracter;
                
            case BOOLEANO:
                return EnumType.booleano;
        }
        return EnumType.error;
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
    
    public void setExpression(Expression e)
    {
        this.value = e;
    }
}
