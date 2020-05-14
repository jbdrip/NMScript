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
import java.util.Map;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class Instance implements Instruction
{
    public LinkedList<Expression> parameters, arraysSize;
    public LinkedList<Sym> paramsResult, symResult;
    int line, column, arrayDimension, index;
    String id, className;
    String classname1;
    Expression expression;
    
    public Instance(String id, String className, String classname1, LinkedList<Expression> parameters, int line, int column)
    {
        this.parameters = parameters;
        this.className = className;
        this.classname1 = classname1;
        this.id = id;
        this.line = line;
        this.column = column;
        this.arrayDimension = -1;
    }
    
    public Instance(String id, String className, String classname1, int arrayDimension, LinkedList<Expression> arraysSize, Expression expression, int line, int column)
    {
        this.id = id;
        this.className = className;
        this.classname1 = classname1;
        this.arrayDimension = arrayDimension;
        this.expression = expression;
        this.line = line;
        this.column = column;
        this.arraysSize = arraysSize;
    }
    
    public Instance(String id, String className, Expression expression, int line, int column)
    {
        this.id = id;
        this.className = className;
        this.expression = expression;
        this.line = line;
        this.column = column;
        this.arrayDimension = -1;
    }
    
    public LinkedList<Sym> executeParams(LinkedList<Expression> parameters, Enviroment env)
    {
        LinkedList<Sym> params = new LinkedList<>();
        if(parameters != null)
        {
            for(int i = 0; i < parameters.size(); i++)
            {
                Expression exp = parameters.get(i);
                exp.paramsResult = executeParams(exp, env);
                Object r = exp.execute(env);
                if(r instanceof Sym)
                {
                    Sym s = (Sym) r;
                    params.add(i, s);
                }
            }
        }
        return params;
    }
    
    public LinkedList<Sym> executeParams(Expression e, Enviroment env)
    {
        LinkedList<Sym> paramsResultt = null;
        
        if(e != null && e.parameters != null)
        {
            paramsResultt = new LinkedList<>();
            for(int i = 0; i < e.parameters.size(); i++)
            {
                Expression exp = e.parameters.get(i);
                exp.paramsResult = executeParams(exp, env);
                Object r = exp.execute(env);
                if(r instanceof Sym)
                {
                    final Sym s = (Sym) r;
                    if(paramsResultt.size() < e.parameters.size()) paramsResultt.add(i, s);
                    else paramsResultt.set(i, s);
                }
            }
        }
        return paramsResultt;
    }
    
    @Override
    public Object execute(Enviroment env) 
    {
        Enviroment local = new Enviroment(env.getGlobal());
        local.setOnClass(true);
        Sym symClass = env.getGlobal().search(className + "##", line, column);
        
        if(arrayDimension > -1)
        {
            if(className != null)
            {
                if(classname1 != null)
                {
                    if(className.equalsIgnoreCase(classname1))
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
                                Sym sym = new Sym(Sym.EnumType.arreglo, array);
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
                else if(expression != null)
                {
                    expression.paramsResult = executeParams(expression, env);
                    Sym valueResult = (Sym)expression.execute(env);
                    if(valueResult.type == EnumType.arreglo)
                    {
                        if(((Array)valueResult.value).className.equalsIgnoreCase(className))
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
                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en instancia, ambos arreglos deben ser de la misma clase, variable: '" + id + "'.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "Error de tipos en instancia, variable: '" + id + "', se esperaba arreglo");
                        env.getGlobal().errors.add(error);
                    }
                }
                else
                {
                    Array array = new Array();
                    array.dimension = arrayDimension;
                    array.type = EnumType.arreglo;
                    array.className = className;
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
        else if(expression != null)
        {
            expression.paramsResult = executeParams(expression, env);
            Object result = expression.execute(env);
            if(result instanceof Sym)
            {
                Sym sym = (Sym) result;
                if(sym.type == Sym.EnumType.objeto)
                {
                    Enviroment objectEnv = (Enviroment) sym.value;
                    Enviroment copy = objectEnv; 
                    Sym object = new Sym(Sym.EnumType.objeto, copy);
                    boolean insert = env.insert(id, object, line, column);
                    
                    if(!insert)
                    {
                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                        env.getGlobal().errors.add(error);
                    }
                }
            }
            return null;
        }
        else
        {
            if(!classname1.equals(""))
            {
                if(symClass != null)
                {
                    LinkedList<Instruction> instructions = (LinkedList<Instruction>)symClass.value;

                    for(Instruction ins : instructions)
                    {
                        if(ins instanceof Void || ins instanceof Function) ins.execute(local);
                    }
                    for(Instruction ins : instructions)
                    {
                        if(!(ins instanceof Void) && !(ins instanceof Function)) ins.execute(local);
                    }
                    //ejecutar aqui el constructor
                    if(parameters == null) parameters = new LinkedList<>();
                    Sym builder = local.search(className + parameters.size() + "??", 0, 0);
                    if(builder != null)
                    {
                        Enviroment builderEnv = new Enviroment(local);
                        ParametersIns parametersIns = (ParametersIns) builder.value;
                        LinkedList<Declaration> paramsList = parametersIns.parameters;
                        LinkedList<Instruction> insList = parametersIns.instructions;
                        int index = 0;
                        
                        if(paramsList != null)
                        {
                            for(Declaration d : paramsList)
                            {
                                Sym sym = (Sym) paramsResult.get(index++);
                                d.execute(builderEnv);
                                builderEnv.updateValue(d.id, sym, line, column);
                            }
                        }
                        
                        if(insList != null)
                        {
                            for(Instruction ins : insList)
                            {
                                if(ins instanceof Signature)
                                {
                                    Signature signature = (Signature) ins;
                                    signature.paramsResult = executeParams(signature.parameters, builderEnv);
                                    ins = signature;
                                }
                                else if(ins instanceof Instance)
                                {
                                    Instance instance = (Instance) ins;
                                    instance.paramsResult = executeParams(instance.parameters, builderEnv);
                                    ins = instance;
                                }
                                else if(ins instanceof FunctionVoid_Call)
                                {
                                    FunctionVoid_Call call = (FunctionVoid_Call) ins;
                                    call.paramsResult = executeParams(call.parametersExpression, builderEnv);
                                    ins = call;
                                }
                                else if(ins instanceof Expression)
                                {
                                    Expression express = (Expression) ins;
                                    express.paramsResult = executeParams(express, builderEnv);
                                    ins = express;
                                }

                                Object result = ins.execute(builderEnv);

                                if(result != null && (result instanceof String) && result.toString().equals("return") && !(ins instanceof Expression)) return "return";
                                else if(result != null && (result instanceof Sym) && !(ins instanceof Expression))
                                {
                                    Sym sym = (Sym) result;
                                    if(sym.breturn)
                                    {
                                        Error_ error = new Error_(line, column, "Semantico", "'return EXPRESION' afuera de una funcion.");
                                        env.getGlobal().errors.add(error);
                                    }
                                }
                                else if ((result != null && (result instanceof String)) && result.toString().equals("break"))
                                {
                                    Error_ error = new Error_(ins.getLine(), ins.getColumn(), "Semantico", "'break' afuera de un ciclo o switch.");
                                    env.getGlobal().errors.add(error);
                                }
                                else if ((result != null && (result instanceof String)) && result.toString().equals("continue"))
                                {
                                    Error_ error = new Error_(ins.getLine(), ins.getColumn(), "Semantico", "'cotinue' afuera de un ciclo.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                        }
                    }
                    Sym object = new Sym(Sym.EnumType.objeto, local);
                    boolean insert = env.insert(id, object, line, column);

                    if(!insert)
                    {
                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                        env.getGlobal().errors.add(error);
                    }
                }
                return null;
            }

            Sym object = new Sym(Sym.EnumType.objeto, null);
            boolean insert = env.insert(id, object, line, column);

            if(!insert)
            {
                Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' ya existe en el ambito actual.");
                env.getGlobal().errors.add(error);
            }
        }
        return null;
    }
    
    void initArray(Array array, int dimension, Enviroment env)
    {
        Sym symSize = symResult.get(symResult.size() - (dimension + 1));
        if(symSize.type == Sym.EnumType.entero)
        {
            array.size = (long) symSize.value;
            array.dimension = dimension + 1;
            array.type = EnumType.arreglo;
            array.className = className;
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
                EnumType t = getType(className);
                for(int i = 0; i < array.size; i++)
                {
                    switch(t)
                    {
                        case entero:
                            long l = 0;
                            array.array.add(new Sym(t, l));
                            break;
                        case doble:
                            array.array.add(new Sym(t, 0.0));
                            break;
                        case cadena:
                            array.array.add(new Sym(t, ""));
                            break;
                        case caracter:
                            array.array.add(new Sym(t, '\0'));
                            break;
                        case booleano:
                            array.array.add(new Sym(t, false));
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
    
    public EnumType getType(String className)
    {
        className = className.toLowerCase();
        switch(className)
        {
            case "entero":
                return EnumType.entero;
            case "doble":
                return EnumType.doble;    
            case "caracter":
                return EnumType.caracter;
            case "booleano":
                return EnumType.booleano;
            case "cadena":
                return EnumType.cadena;
            default: 
                return EnumType.objeto;
        }
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
