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
public class Signature implements Instruction
{
    Expression id;
    Expression value;
    public int line, column, index;
    String className;
    public LinkedList<Expression> parameters, values;
    public LinkedList<Sym> paramsResult, symResult;
    boolean array;
    EnumType type;
    
    public Signature(Expression id, Expression value, int line, int column)
    {
        this.id = id;
        this.value = value;
        this.line = line;
        this.column = column;
        array = false;
    }
    
    public Signature(Expression id, LinkedList<Expression> values, int line, int column)
    {
        this.id = id;
        this.values = values;
        this.line = line;
        this.column = column;
        this.array = true;
    }
    
    public Signature(Expression id, String className, LinkedList<Expression> parameters, boolean array, int line, int column)
    {
        this.id = id;
        this.className = className;
        this.parameters = parameters;
        this.line = line;
        this.column = column;
        this.array = array;
    }
    
    public Signature(Expression id, EnumType type, LinkedList<Expression> parameters, boolean array, int line, int column)
    {
        this.id = id;
        this.type = type;
        this.parameters = parameters;
        this.line = line;
        this.column = column;
        this.array = array;
    }
    
    public Object execute(Enviroment env)
    {
        if(id != null)id.paramsResult = executeParams(id, env);
        if(array)
        {
            Sym re = (Sym)id.execute(env);
            if(re.type == EnumType.arreglo)
            {
                Array array_ = (Array) re.value;
                if(className != null)
                {
                    if(className.equalsIgnoreCase(array_.className))
                    {
                        if(parameters != null)
                        {
                            index = 0;
                            array_ = new Array();
                            symResult = new LinkedList<>();
                            for(Expression expSize : parameters)
                            {
                                expSize.paramsResult = executeParams(expSize, env);
                                Object result_ = expSize.execute(env);
                                if(result_ instanceof Sym) symResult.add((Sym) result_);
                            }
                            initArray(array_, parameters.size() - 1, env);
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "' de tipo " + array_.className + " y asignacion de tipo " + className);
                        env.getGlobal().errors.add(error);
                    }
                }
                else if(type != null)
                {
                    className = array_.className;
                    if(type.toString().equalsIgnoreCase(array_.className))
                    {
                        if(parameters != null)
                        {
                            index = 0;
                            array_ = new Array();
                            symResult = new LinkedList<>();
                            for(Expression expSize : parameters)
                            {
                                expSize.paramsResult = executeParams(expSize, env);
                                Object result_ = expSize.execute(env);
                                if(result_ instanceof Sym) symResult.add((Sym) result_);
                            }
                            initArray(array_, parameters.size() - 1, env);
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "' de tipo " + array_.className + " y asignacion de tipo " + type.toString());
                        env.getGlobal().errors.add(error);
                    }
                }
                else if(values != null)
                {
                    className = array_.className;
                    if(type.toString().equalsIgnoreCase(array_.className))
                    {
                        symResult = new LinkedList<>();
                        for(Expression expSize : values)
                        {
                            expSize.paramsResult = executeParams(expSize, env);
                            Object result = expSize.execute(env);
                            if(result instanceof Sym) symResult.add((Sym) result);
                        }

                        if(array_.dimension == 1)
                        {
                            array_ = new Array();
                            array_.dimension = 1;
                            array_.size = symResult.size();
                            array_.type = EnumType.arreglo;
                            array_.className = type.toString();
                            for(int i = 0; i < symResult.size(); i++)
                            {
                                array_.array.add(symResult.get(i));
                            }
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "' de tipo " + array_.className + " y asignacion de tipo " + type.toString());
                        env.getGlobal().errors.add(error);
                    }
                }
                Sym update = new Sym(re.type, array_);
                if(id.type == Expression_type.ACCESO)
                {
                    if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                    if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                    Object result = id.leftExp.execute(env);
                    if(result instanceof Sym)
                    {
                        Sym sym = (Sym) result;
                        if(sym.type == Sym.EnumType.objeto)
                        {
                            Enviroment objectEnv = (Enviroment) sym.value;
                            if(id.rightExp.type == Expression_type.IDENTIFICADOR)
                            {
                                if(id.rightExp.symResult != null)
                                {
                                    id.rightExp.symResult = new LinkedList<>();
                                    for(Expression e : id.rightExp.arraySizes)
                                    {
                                        Sym symIn = (Sym) e.execute(env);
                                        id.rightExp.symResult.add(symIn);
                                    }
                                }
                                Sym sym1 = objectEnv.search(id.rightExp.value.toString(), line, column);
                                if(sym1 != null)
                                {
                                    objectEnv.updateValue(id.rightExp.value.toString(), update, line, column);
                                }
                                else
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                        }
                    }
                    return null;
                }
                else if(id.type == Expression_type.THIS)
                {
                    if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                    if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                    if(id.leftExp.type == Expression_type.IDENTIFICADOR)
                    {
                        if(id.leftExp.symResult != null)
                        {
                            id.leftExp.symResult = new LinkedList<>();
                            for(Expression e : id.leftExp.arraySizes)
                            {
                                Sym symIn = (Sym) e.execute(env);
                                id.leftExp.symResult.add(symIn);
                            }
                        }
                        Enviroment classEnv = env.getOnClass();
                        String idr = id.leftExp.value.toString();
                        Sym sym1 = classEnv.search(idr, line, column);
                        if(sym1 != null)
                        {
                            classEnv.updateValue(idr, update, line, column);
                        }
                        else
                        {
                            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    return null;
                }
                Sym sym = env.search(id.value.toString(), line, column);
                if(sym != null)
                {
                    env.updateValue(id.value.toString(), update, line, column);
                }
                else
                {
                    Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
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
            if(value != null)
            {
                value.paramsResult = executeParams(value, env);
                Sym valueResult = (Sym)value.execute(env);
                if(id.type == Expression_type.ACCESO)
                {
                    if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                    if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                    Object result = id.leftExp.execute(env);
                    if(result instanceof Sym)
                    {
                        Sym sym = (Sym) result;
                        if(sym.type == Sym.EnumType.objeto)
                        {
                            Enviroment objectEnv = (Enviroment) sym.value;
                            if(id.rightExp.type == Expression_type.IDENTIFICADOR)
                            {
                                if(id.rightExp.arraySizes != null) arraySignature(objectEnv, id.rightExp, valueResult);
                                else
                                {
                                    Sym sym1 = objectEnv.search(id.rightExp.value.toString(), line, column);
                                    if(sym1 != null)
                                    {
                                        if(valueResult.type == sym1.type)
                                        {
                                            Sym update = new Sym(valueResult.type, valueResult.value);
                                            objectEnv.updateValue(id.rightExp.value.toString(), update, line, column);
                                        }
                                        else if(valueResult.type == Sym.EnumType.nulo)
                                        {
                                            Sym update = new Sym(sym1.type, null);
                                            objectEnv.updateValue(id.rightExp.value.toString(), update, line, column);
                                        }
                                        else 
                                        {
                                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                                            env.getGlobal().errors.add(error);
                                        }
                                    }
                                    else
                                    {
                                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                                        env.getGlobal().errors.add(error);
                                    }
                                }
                            }
                        }
                    }
                    return null;
                }
                else if(id.type == Expression_type.THIS)
                {
                    if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                    if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                    if(id.leftExp.type == Expression_type.IDENTIFICADOR)
                    {
                        Enviroment classEnv = env.getOnClass();
                        String idr = id.leftExp.value.toString();
                        if(id.leftExp.arraySizes != null) arraySignature(classEnv, id.leftExp, valueResult);
                        else
                        {
                            Sym sym1 = classEnv.search(idr, line, column);
                            if(sym1 != null)
                            {
                                if(valueResult.type == sym1.type)
                                {
                                    Sym update = new Sym(valueResult.type, valueResult.value);
                                    classEnv.updateValue(idr, update, line, column);
                                }
                                else if(valueResult.type == Sym.EnumType.nulo)
                                {
                                    Sym update = new Sym(sym1.type, null);
                                    classEnv.updateValue(idr, update, line, column);
                                }
                                else 
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                            else
                            {
                                Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                                env.getGlobal().errors.add(error);
                            }
                        } 
                    }
                    return null;
                }

                if(id.arraySizes != null) arraySignature(env, id, valueResult);
                else
                {
                    Sym sym = env.search(id.value.toString(), line, column);

                    if(sym != null)
                    {
                        if(valueResult.type == sym.type) 
                        {
                            Sym update = new Sym(sym.type, valueResult.value);
                            env.updateValue(id.value.toString(), update, line, column);
                        }
                        else if(valueResult.type == Sym.EnumType.nulo)
                        {
                            Sym update = new Sym(sym.type, null);
                            env.updateValue(id.value.toString(), update, line, column);
                        }
                        else 
                        {
                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                        env.getGlobal().errors.add(error);
                    }
                } 
            }
            else
            {
                Enviroment local = new Enviroment(env.getGlobal());
                local.setOnClass(true);
                Sym symClass = env.getGlobal().search(className + "##", line, column);

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
                    Sym builder = local.search(className + parameters.size() + "??", 0, 0);//constructor
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
                    Sym update = new Sym(EnumType.objeto, local);

                    if(id.type == Expression_type.ACCESO)
                    {
                        if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                        if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                        Object result = id.leftExp.execute(env);
                        if(result instanceof Sym)
                        {
                            Sym sym = (Sym) result;
                            if(sym.type == Sym.EnumType.objeto)
                            {
                                Enviroment objectEnv = (Enviroment) sym.value;
                                if(id.rightExp.type == Expression_type.IDENTIFICADOR)
                                {
                                    Sym sym1 = objectEnv.search(id.rightExp.value.toString(), line, column);
                                    if(sym1 != null)
                                    {
                                        if(update.type == sym1.type)env.updateValue(id.rightExp.value.toString(), update, line, column);
                                        else 
                                        {
                                            Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                                            env.getGlobal().errors.add(error);
                                        }
                                    }
                                    else
                                    {
                                        Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                                        env.getGlobal().errors.add(error);
                                    }
                                }
                            }
                        }
                        return null;
                    }
                    else if(id.type == Expression_type.THIS)
                    {
                        if(id.leftExp != null)id.leftExp.paramsResult = executeParams(id.leftExp, env);
                        if(id.rightExp != null)id.rightExp.paramsResult = executeParams(id.rightExp, env);
                        if(id.leftExp.type == Expression_type.IDENTIFICADOR)
                        {
                            Enviroment classEnv = env.getOnClass();
                            String idr = id.leftExp.value.toString();
                            Sym sym1 = classEnv.search(idr, line, column);
                            if(sym1 != null)
                            {
                                if(update.type == sym1.type) env.updateValue(idr, update, line, column);
                                else 
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                                    env.getGlobal().errors.add(error);
                                }
                            }
                            else
                            {
                                Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                                env.getGlobal().errors.add(error);
                            }
                        }
                        return null;
                    }

                    if(id.arraySizes != null) arraySignature(env, id, update);
                    else
                    {
                        Sym sym = env.search(id.value.toString(), line, column);
                        if(sym != null)
                        {
                            if(update.type == sym.type) env.updateValue(id.value.toString(), update, line, column);
                            else 
                            {
                                Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                                env.getGlobal().errors.add(error);
                            }
                        }
                        else
                        {
                            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
                            env.getGlobal().errors.add(error);
                        }
                    }   
                }
            }
        }
        return null;
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
    
    void arraySignature(Enviroment env, Expression e, Sym insert)
    {
        Sym sym1 = env.search(e.value.toString(), line, column);
        if(sym1 != null)
        {
            if(sym1.type == EnumType.arreglo)
            {
                Array array_ = (Array)sym1.value;
                if(insert.type == getType(array_.className))
                {
                    symResult = new LinkedList<>();
                    for(Expression expSize : e.arraySizes)
                    {
                        expSize.paramsResult = executeParams(expSize, env);
                        Object result_ = expSize.execute(env);
                        if(result_ instanceof Sym) symResult.add((Sym) result_);
                    }
                    
                    Array update = array_;
                    for(int i = (int)array_.dimension; i > 0; i--)
                    {
                        Sym symSize = symResult.get(symResult.size() - i);
                        if(symSize.type == Sym.EnumType.entero)
                        {
                            long aux = (long)symSize.value;
                            int indexx = (int) aux;
                            if(indexx < update.size)
                            {
                                if(update.dimension == 1)
                                {
                                    update.array.set(indexx, insert);
                                    Sym symIn = new Sym(sym1.type, array_);
                                    env.updateValue(e.value.toString(), symIn, line, column);
                                }
                                else
                                {
                                    Object arrayIn = update.array.get(indexx);
                                    if(arrayIn != null) update = (Array) arrayIn;
                                }
                            }
                            else
                            {
                                Error_ error = new Error_(line, column, "Semantico", "Error en indice de acceso de arreglo, variable: '" + value.toString() + "' (el indice es mayor al tamaño del arreglo).");
                                env.getGlobal().errors.add(error);
                                return;
                            }
                        }
                        else
                        {
                            Error_ error = new Error_(line, column, "Semantico", "Error en indice de acceso de arreglo, variable: '" + value.toString() + "' (el indice no es entero).");
                            env.getGlobal().errors.add(error);
                            return;
                        }
                    }
                }
                else 
                {
                    Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asignacion, variable: '" + id + "'.");
                    env.getGlobal().errors.add(error);
                }
            }
            else
            {
                Error_ error = new Error_(line, column, "Semantico", "Error de tipos en asginacion, variable: '" + value.toString() + "', se esperaba arreglo");
                env.getGlobal().errors.add(error);
            }
        }
        else
        {
            Error_ error = new Error_(line, column, "Semantico", "La variable '" + id + "' no existe en el ambito actual.");
            env.getGlobal().errors.add(error);
        }
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
    
    public LinkedList<Sym> executeParams(LinkedList<Expression> parameters, Enviroment env)
    {
        LinkedList<Sym> params = new LinkedList<>();
        if(parameters != null)
        {
            for(Expression exp : parameters)
            {
                exp.paramsResult = executeParams(exp, env);
                Object r = exp.execute(env);
                if(r instanceof Sym)
                {
                    Sym s = (Sym) r;
                    params.add(s);
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
    
    public Sym.EnumType getType(Expression.Expression_type type)
    {
        switch(type)
        {
            case ENTERO:
                return Sym.EnumType.entero;
                
            case DOBLE:
                return Sym.EnumType.doble;
                
            case CADENA:
                return Sym.EnumType.cadena;
                
            case CARACTER:
                return Sym.EnumType.caracter;
                
            case BOOLEANO:
                return Sym.EnumType.booleano;
        }
        return Sym.EnumType.error;
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
