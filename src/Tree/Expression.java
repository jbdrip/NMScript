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
public class Expression implements Instruction
{
    public static enum Expression_type
    {
        SUMA,
        RESTA,
        MULTIPLICACION,
        DIVISION,
        POTENCIA,
        MODULO,
        NEGATIVO,
        ENTERO,
        DOBLE,
        CADENA,
        CARACTER,
        BOOLEANO,
        IDENTIFICADOR,
        NULO,
        FUNCION,
        ACCESO,
        THIS,
        MAYOR_QUE,
        MENOR_QUE,
        MAYOR_IGUAL,
        MENOR_IGUAL,
        IGUAL_IGUAL,
        DIFERENTE_IGUAL,
        AUMENTO,
        DECREMENTO,
        AND,
        OR,
        XOR,
        NOT
    }
    
    public LinkedList<Expression> parameters, arraySizes;
    public LinkedList<Sym> paramsResult, symResult;
    public Expression_type type;
    public Expression leftExp;
    public Expression rightExp;
    public Object value;
    public Sym val;
    public int line, column;
    
    public Expression(Expression_type type, Expression leftExp, Expression rightExp, Object value, int line, int column)
    {
        this.type = type;
        this.leftExp = leftExp;
        this.rightExp = rightExp;
        this.value = value;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
    
    public Expression(Expression leftExp, Expression rightExp, Expression_type tipo, int line, int column) 
    {
        this.type = tipo;
        this.leftExp = leftExp;
        this.rightExp = rightExp;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
    
    public Expression(Expression exp, int line, int column)
    {
        this.type = exp.type;
        this.leftExp = exp.leftExp;
        this.rightExp = exp.rightExp;
        this.value = exp.value;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
     //* Constructor para operaciones unarias (un operador), estas operaciones son:
     //* NEGATIVO NOT
    public Expression(Expression leftExp, Expression_type type, int line, int column)
    {
        this.type = type;
        this.leftExp = leftExp;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
    
    public Expression(String id, Expression_type type, int line, int column)
    {
        this.value = id;
        this.type = type;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
    
    public Expression(String id, Expression_type type, LinkedList<Expression> arraySizes, int line, int column)
    {
        this.value = id;
        this.type = type;
        this.arraySizes = arraySizes;
        val = new Sym();
        this.line = line;
        this.column = column;
    }
    
    public Expression(String id, LinkedList<Expression> parameters, Expression_type type, int line, int column)
    {
        this.value = id;
        this.type = type;
        val = new Sym();
        this.line = line;
        this.column = column;
        this.parameters = parameters;
    }
    
    public Expression(double doble, int line, int column)
    {
        this.value = doble;
        this.type = Expression_type.DOBLE;
        this.val = new Sym(EnumType.doble, doble);
        this.line = line;
        this.column = column;
    }
    
    public Expression(long entero, int line, int column)
    {
        this.value = entero;
        this.type = Expression_type.ENTERO;
        val = new Sym(EnumType.entero, entero);
        this.line = line;
        this.column = column;
    }
    
    public Expression(char caracter, int line, int column)
    {
        this.value = caracter;
        this.type = Expression_type.CARACTER;
        val = new Sym(EnumType.caracter, caracter);
        this.line = line;
        this.column = column;
    }
    
    public Expression(String cadena, int line, int column)
    {
        this.value = cadena;
        this.type = Expression_type.CADENA;
        val = new Sym(EnumType.cadena, cadena);
        this.line = line;
        this.column = column;
    }
    
    public Expression(boolean booleano, int line, int column)
    {
        this.value = booleano;
        this.type = Expression_type.BOOLEANO;
        val = new Sym(EnumType.booleano, booleano);
        this.line = line;
        this.column = column;
    }
    
    public Expression(Expression_type nulo, int line, int column)
    {
        this.value = "null";
        this.type = nulo;
        val = new Sym(EnumType.nulo, "null");
        this.line = line;
        this.column = column;
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
    
    @Override
    public Object execute(Enviroment env) 
    {
        if(leftExp != null)leftExp.paramsResult = executeParams(leftExp, env);
        if(rightExp != null)rightExp.paramsResult = executeParams(rightExp, env);
        
        if(type == Expression_type.DIVISION) return Division(env);
        else if(type == Expression_type.MULTIPLICACION) return Multiplicacion(env);
        else if(type == Expression_type.RESTA) return Resta(env);
        else if(type == Expression_type.SUMA) return Suma(env);
        else if(type == Expression_type.POTENCIA) return Potencia(env);
        else if(type == Expression_type.MODULO) return Modulo(env);
        else if(type == Expression_type.MAYOR_QUE) return MayorQue(env);
        else if(type == Expression_type.MENOR_QUE) return MenorQue(env);
        else if(type == Expression_type.MAYOR_IGUAL) return MayorIgual(env);
        else if(type == Expression_type.MENOR_IGUAL) return MenorIgual(env);
        else if(type == Expression_type.IGUAL_IGUAL) return IgualIgual(env);
        else if(type == Expression_type.DIFERENTE_IGUAL) return DiferenteIgual(env);
        else if(type == Expression_type.AUMENTO) return Aumento(env);
        else if(type == Expression_type.DECREMENTO) return Decremento(env);
        else if(type == Expression_type.AND) return And(env);
        else if(type == Expression_type.OR) return Or(env);
        else if(type == Expression_type.XOR) return Xor(env);
        else if(type == Expression_type.NOT) return Not(env);
        else if(type == Expression_type.NEGATIVO) return Negativo(env);
        else if(type == Expression_type.DOBLE) return val;
        else if(type == Expression_type.ENTERO) return val;
        else if(type == Expression_type.CARACTER) return val;
        else if(type == Expression_type.CADENA) return val;
        else if(type == Expression_type.BOOLEANO) return val;
        else if(type == Expression_type.NULO) return val;
        else if(type == Expression_type.IDENTIFICADOR) return Identificador(env);
        else if(type == Expression_type.FUNCION) return functionVoid_Call(env);
        else if(type == Expression_type.ACCESO) return objectAccess(env);
        else if(type == Expression_type.THIS) return thisAccess(env);
        else return null;
    }
    
    public Object thisAccess(Enviroment env)
    {
        return leftExp.execute(env.getOnClass());
    }
    
    public Object objectAccess(Enviroment env)
    {
        Access access = new Access(leftExp, rightExp, line, column);
        return access.execute(env);
    }
    
    public Object functionVoid_Call(Enviroment env)
    {
        FunctionVoid_Call call = new FunctionVoid_Call((String)value, parameters, line, column);
        call.paramsResult = paramsResult;
        return call.execute(env);
    }
    
    public Object Aumento(Enviroment env)
    {
        Sym sym = env.search(value.toString(), 0, 0);
        if(sym != null)
        {
            if(sym.type == Sym.EnumType.entero)
            {
                long aumento = (long)sym.value;
                Sym update = new Sym(sym.type, aumento + 1);
                env.updateValue(value.toString(), update, 0, 0);
            }
            else if(sym.type == Sym.EnumType.doble)
            {
                double aumento = (double)sym.value;
                Sym update = new Sym(sym.type, aumento + 1.0);
                env.updateValue(value.toString(), update, 0, 0);
            }
            else if(sym.type == Sym.EnumType.caracter)
            {
                char aumento = (char)sym.value;
                Sym update = new Sym(sym.type, (char)(aumento + 1));
                env.updateValue(value.toString(), update, 0, 0);
            }
            else
            {
                Error_ error = new Error_(line, column, "Semantico", "Error de tipo en operacion incremento.");
                env.getGlobal().errors.add(error);
                return new Sym(EnumType.error, "@error");
            }
            val = new Sym(sym.type, sym.value);
            return new Sym(sym.type, sym.value);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "La variable '" + value.toString() + "' no existe en el ambito actual.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Decremento(Enviroment env)
    {
        Sym sym = env.search(value.toString(), 0, 0);
        if(sym != null)
        {
            if(sym.type == Sym.EnumType.entero)
            {
                long decremento = (long)sym.value;
                Sym update = new Sym(sym.type, decremento - 1);
                env.updateValue(value.toString(), update, 0, 0);
            }
            else if(sym.type == Sym.EnumType.doble)
            {
                double decremento = (double)sym.value;
                Sym update = new Sym(sym.type, decremento - 1.0);
                env.updateValue(value.toString(), update, 0, 0);
            }
            else if(sym.type == Sym.EnumType.caracter)
            {
                char aumento = (char)sym.value;
                Sym update = new Sym(sym.type, (char)(aumento - 1));
                env.updateValue(value.toString(), update, 0, 0);
            }
            else
            {
                Error_ error = new Error_(line, column, "Semantico", "Error de tipo en operacion decremento.");
                env.getGlobal().errors.add(error);
                return new Sym(EnumType.error, "@error");
            }
            val = new Sym(sym.type, sym.value);
            return new Sym(sym.type, sym.value);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "La variable '" + value.toString() + "'no existe en el ambito actual.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
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
    
    public Sym getArrayValue(Array array, Enviroment env)
    {
        Sym arrayValue = new Sym();
        Sym symSize = symResult.get(symResult.size() - ((int)array.dimension));
        if(symSize.type == Sym.EnumType.entero)
        {
            long aux = (long)symSize.value;
            int index = (int)aux;
            if(index < array.size)
            {
                if(array.dimension == 1)
                {
                    Sym valueIn =(Sym) array.array.get(index);
                    if(valueIn != null)
                    {
                        arrayValue.type = valueIn.type;
                        arrayValue.value = valueIn.value;
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "El elemento del arreglo al que se quiere acceder es nulo: '" + value.toString() + "' (NullPointerException).");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                }
                else
                {
                    Object valueIn = array.array.get(index);
                    if(valueIn != null)
                    {
                        Array next = (Array)valueIn;
                        return getArrayValue(next, env);
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "El elemento del arreglo al que se quiere acceder es nulo: '" + value.toString() + "' (NullPointerException).");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                }
            }
            else
            {
                Error_ error = new Error_(line, column, "Semantico", "Error en indice de acceso de arreglo, variable: '" + value.toString() + "' (el indice es mayor al tamaño del arreglo).");
                env.getGlobal().errors.add(error);
                return new Sym(EnumType.error, "@error");
            }
        }
        else
        {
            Error_ error = new Error_(line, column, "Semantico", "Error en indice de acceso de arreglo, variable: '" + value.toString() + "' (el indice no es entero).");
            env.getGlobal().errors.add(error);
            return new Sym(EnumType.error, "@error");
        }
        return arrayValue;
    }
    
    public Object Identificador(Enviroment env)
    {
        Sym sym = env.search(value.toString(), 0, 0);
        if(sym != null)
        {
            if(arraySizes != null)
            {
                if(sym.type == EnumType.arreglo)
                {
                    Array array = (Array) sym.value;
                    symResult = new LinkedList<>();
                    for(Expression expSize : arraySizes)
                    {
                        expSize.paramsResult = executeParams(expSize, env);
                        Object result_ = expSize.execute(env);
                        if(result_ instanceof Sym) symResult.add((Sym) result_);
                    } 
                    if(symResult.size() == array.dimension)
                    {
                        Sym result = getArrayValue(array, env);
                        val = new Sym(result.type, result.value);
                        return new Sym(result.type, result.value);
                    }
                    else
                    {
                        Error_ error = new Error_(line, column, "Semantico", "El numero de dimensiones es mayor al que posee el arreglo, variable: '" + value.toString() + "'.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                }
                else
                {
                    Error_ error = new Error_(line, column, "Semantico", "Error de tipos en acceso, variable: '" + value.toString() + "', se esperaba arreglo");
                    env.getGlobal().errors.add(error);
                    return new Sym(EnumType.error, "@error");
                }
            }
            else
            {
                val = new Sym(sym.type, sym.value);
                return new Sym(sym.type, sym.value);
            }
        }
        
        Error_ error = new Error_(line, column, "Semantico", "La variable '" + value.toString() + "' no existe en el ambito actual.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Negativo(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        
        if(leftResult.type == EnumType.entero)
        {
            val = new Sym(EnumType.entero, (long)leftResult.value * -1);
            return new Sym(EnumType.entero, (long)leftResult.value * -1);
        }
        else if(leftResult.type == EnumType.doble)
        {
            val = new Sym(EnumType.doble, (double)leftResult.value * -1.0);
            return new Sym(EnumType.doble, (double)leftResult.value * -1.0);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "El operando negativo solo puede aplicarse a datos de tipo entero o doble.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Division(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        
        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                {
                    if((long)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    long result = (long)leftResult.value / (long)rightResult.value;
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
                }  
                
                case doble:
                {
                    if((double)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    double result = Double.valueOf((long)leftResult.value) / (double)rightResult.value;
                    val = new Sym(EnumType.doble, result);
                    return new Sym(EnumType.doble, result);
                } 

                case caracter:
                    if((long)((char)rightResult.value) == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    long result = (long)leftResult.value / (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    if((long)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    double result1 = (double)leftResult.value / Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    if((double)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    double result2 = (double)leftResult.value / (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    if((long)((char)rightResult.value) == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    double result3 = (double)leftResult.value / Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    if((long)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    long result1 = (long)((char)leftResult.value) / (long)rightResult.value;
                    val = new Sym(EnumType.entero, result1);
                    return new Sym(EnumType.entero, result1);

                case doble:
                    if((double)rightResult.value == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    double result2 = Double.valueOf((long)((char)leftResult.value)) / (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    if((long)((char)rightResult.value) == 0)
                    {
                        Error_ error = new Error_(line, column, "De ejecucion", "No se puede realizar division entre cero.");
                        env.getGlobal().errors.add(error);
                        return new Sym(EnumType.error, "@error");
                    }
                    long result3 = (long)((char)leftResult.value) / (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result3);
                    return new Sym(EnumType.entero, result3);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion division.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Multiplicacion(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        
        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                {
                    long result = (long)leftResult.value * (long)rightResult.value;
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
                }  
                
                case doble:
                {
                    double result = Double.valueOf((long)leftResult.value) * (double)rightResult.value;
                    val = new Sym(EnumType.doble, result);
                    return new Sym(EnumType.doble, result);
                } 

                case caracter:
                    long result = (long)leftResult.value * (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (double)leftResult.value * Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = (double)leftResult.value * (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (double)leftResult.value * Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    long result1 = (long)((char)leftResult.value) * (long)rightResult.value;
                    val = new Sym(EnumType.entero, result1);
                    return new Sym(EnumType.entero, result1);

                case doble:
                    double result2 = Double.valueOf((long)((char)leftResult.value)) * (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    long result3 = (long)((char)leftResult.value) * (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result3);
                    return new Sym(EnumType.entero, result3);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion multiplicacion.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Resta(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        
        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                {
                    long result = (long)leftResult.value - (long)rightResult.value;
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
                }  
                
                case doble:
                {
                    double result = Double.valueOf((long)leftResult.value) - (double)rightResult.value;
                    val = new Sym(EnumType.doble, result);
                    return new Sym(EnumType.doble, result);
                } 

                case caracter:
                    long result = (long)leftResult.value - (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result);
                    return new Sym(EnumType.entero, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (double)leftResult.value - Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = (double)leftResult.value - (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (double)leftResult.value - Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    long result1 = (long)((char)leftResult.value) - (long)rightResult.value;
                    val = new Sym(EnumType.entero, result1);
                    return new Sym(EnumType.entero, result1);

                case doble:
                    double result2 = Double.valueOf((long)((char)leftResult.value)) - (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    long result3 = (long)((char)leftResult.value) - (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result3);
                    return new Sym(EnumType.entero, result3);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion resta.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Suma(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);

        if(leftResult.type == EnumType.entero) 
        {
            switch(rightResult.type)
            {
                case entero:
                    long result1 = (long)leftResult.value + (long)rightResult.value;
                    val = new Sym(EnumType.entero, result1);
                    return new Sym(EnumType.entero, result1);

                case doble:
                    double result2 = Double.valueOf((long)leftResult.value) + (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    long result3 = (long)leftResult.value + (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result3);
                    return new Sym(EnumType.entero, result3);

                case cadena:
                    String result4 = leftResult.value.toString() + (String)rightResult.value;
                    val = new Sym(EnumType.cadena, result4);
                    return new Sym(EnumType.cadena, result4);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (double)leftResult.value + Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = (double)leftResult.value + (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (double)leftResult.value + Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);

                case cadena:
                    String result4 = leftResult.value.toString() + (String)rightResult.value;
                    val = new Sym(EnumType.cadena, result4);
                    return new Sym(EnumType.cadena, result4);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    long result1 = (long)((char)leftResult.value) + (long)rightResult.value;
                    val = new Sym(EnumType.entero, result1);
                    return new Sym(EnumType.entero, result1);

                case doble:
                    val.type = EnumType.doble;
                    double result2 = Double.valueOf((long)((char)leftResult.value)) + (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    long result3 = (long)((char)leftResult.value) + (long)((char)rightResult.value);
                    val = new Sym(EnumType.entero, result3);
                    return new Sym(EnumType.entero, result3);

                case cadena:
                    String result4 = leftResult.value.toString() + (String)rightResult.value;
                    val = new Sym(EnumType.cadena, result4);
                    return new Sym(EnumType.cadena, result4);
            }
        }
        else if(leftResult.type == EnumType.cadena)
        {
            switch(rightResult.type)
            {
                case entero:
                    String result1 = (String)leftResult.value + rightResult.value.toString();
                    val = new Sym(EnumType.cadena, result1);
                    return new Sym(EnumType.cadena, result1);

                case doble:
                    String result2 = (String)leftResult.value + rightResult.value.toString();
                    val = new Sym(EnumType.cadena, result2);
                    return new Sym(EnumType.cadena, result2);

                case caracter:
                    String result3 = (String)leftResult.value + rightResult.value.toString();
                    val = new Sym(EnumType.cadena, result3);
                    return new Sym(EnumType.cadena, result3);

                case cadena:
                    String result4 = (String)leftResult.value + rightResult.value.toString();
                    val = new Sym(EnumType.cadena, result4);
                    return new Sym(EnumType.cadena, result4);

                case booleano:
                    String result5 =  (String)leftResult.value + rightResult.value.toString();
                    val = new Sym(EnumType.cadena, result5);
                    return new Sym(EnumType.cadena, result5);
            }
        }
        else if(leftResult.type == EnumType.booleano)
        {
            switch(rightResult.type)
            {
                case cadena:
                    String result5 =  leftResult.value.toString() + (String)rightResult.value;
                    val = new Sym(EnumType.cadena, result5);
                    return new Sym(EnumType.cadena, result5);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion suma.");
        env.getGlobal().errors.add(error);
        return new Sym (EnumType.error, "@error");
    }
    
    public Object Potencia(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = Math.pow(Double.valueOf((long)leftResult.value), Double.valueOf((long)rightResult.value));
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);
// 15 + 4
                case doble:
                    double result2 = Math.pow(Double.valueOf((long)leftResult.value), (double)rightResult.value);
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = Math.pow(Double.valueOf((long)leftResult.value), Double.valueOf((long)((char)rightResult.value)));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = Math.pow((double)leftResult.value, Double.valueOf((long)rightResult.value));
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = Math.pow((double)leftResult.value, (double)rightResult.value);
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = Math.pow((double)leftResult.value, Double.valueOf((long)((char)rightResult.value)));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = Math.pow(Double.valueOf((long)((char)leftResult.value)), Double.valueOf((long)rightResult.value));
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = Math.pow(Double.valueOf((long)((char)leftResult.value)), (double)rightResult.value);
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = Math.pow(Double.valueOf((long)((char)leftResult.value)), Double.valueOf((long)((char)rightResult.value)));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion potencia.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Modulo(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (long)leftResult.value % (long)rightResult.value;
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 =  Double.valueOf((long)leftResult.value) % (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (long)leftResult.value % (long)((char)rightResult.value);
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (double)leftResult.value % Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = (double)leftResult.value % (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (double)leftResult.value % Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    double result1 = (long)((char)leftResult.value) % (long)rightResult.value;
                    val = new Sym(EnumType.doble, result1);
                    return new Sym(EnumType.doble, result1);

                case doble:
                    double result2 = Double.valueOf((long)((char)leftResult.value)) % (double)rightResult.value;
                    val = new Sym(EnumType.doble, result2);
                    return new Sym(EnumType.doble, result2);

                case caracter:
                    double result3 = (long)((char)leftResult.value) % (long)((char)rightResult.value);
                    val = new Sym(EnumType.doble, result3);
                    return new Sym(EnumType.doble, result3);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion modulo.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object MayorQue(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value > (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) > (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value > (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value > Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value > (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value > Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) > (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) > (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) > (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion mayor que.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object MenorQue(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value < (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) < (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value < (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value < Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value < (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value < Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) < (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) < (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) < (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion menor que.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object MayorIgual(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value >= (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) >= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value >= (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value >= Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value >= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value >= Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) >= (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) >= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) >= (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion mayor igual que.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object MenorIgual(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value <= (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) <= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value <= (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value <= Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value <= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value <= Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) <= (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) <= (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) <= (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion menor igual que.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object IgualIgual(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value == (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) == (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value == (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value == Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value == (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value == Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) == (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) == (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) == (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.cadena)
        {
            if(rightResult.type == EnumType.cadena)
            {
                result = ((String)leftResult.value).equals((String)rightResult.value);
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.booleano)
        {
            if(rightResult.type == EnumType.booleano)
            {
                result = (boolean)leftResult.value == (boolean) rightResult.value;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.objeto)
        {
            if(rightResult.type == EnumType.objeto)
            {
                result = leftResult.value == rightResult.value;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
            else if(rightResult.type == EnumType.nulo)
            {
                if(leftResult.value == null) result = true;
                else result = false;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.nulo)
        {
            if(rightResult.type == EnumType.nulo)
            {
                result = true;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
            else if(rightResult.type == EnumType.objeto)
            {
                if(rightResult.value == null) result = true;
                else result = false;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion igual igual.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object DiferenteIgual(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;

        if(leftResult.type == EnumType.entero)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)leftResult.value != (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = Double.valueOf((long)leftResult.value) != (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)leftResult.value != (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.doble)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (double)leftResult.value != Double.valueOf((long)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    result = (double)leftResult.value != (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (double)leftResult.value != Double.valueOf((long)((char)rightResult.value));
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.caracter)
        {
            switch(rightResult.type)
            {
                case entero:
                    result = (long)((char)leftResult.value) != (long)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case doble:
                    val.type = EnumType.booleano;
                    result = Double.valueOf((long)((char)leftResult.value)) != (double)rightResult.value;
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);

                case caracter:
                    result = (long)((char)leftResult.value) != (long)((char)rightResult.value);
                    val = new Sym(EnumType.booleano, result);
                    return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.cadena)
        {
            if(rightResult.type == EnumType.cadena)
            {
                result = !((String)leftResult.value).equals((String)rightResult.value);
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
            else if(rightResult.type == EnumType.nulo)
            {
                result = !((String)leftResult.value).equalsIgnoreCase("");
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.booleano)
        {
            if(rightResult.type == EnumType.booleano)
            {
                result = (boolean)leftResult.value != (boolean) rightResult.value;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.objeto)
        {
            if(rightResult.type == EnumType.objeto)
            {
                result = leftResult.value != rightResult.value;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
            else if(rightResult.type == EnumType.nulo)
            {
                if(leftResult.value != null) result = true;
                else result = false;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        else if(leftResult.type == EnumType.nulo)
        {
            if(rightResult.type == EnumType.nulo)
            {
                result = false;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
            else if(rightResult.type == EnumType.objeto)
            {
                if(rightResult.value != null) result = true;
                else result = false;
                val = new Sym(EnumType.booleano, result);
                return new Sym(EnumType.booleano, result);
            }
        }
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion diferente igual.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object And(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;
        
        if(leftResult.type == EnumType.booleano && leftResult.type == EnumType.booleano)
        {
            result = (boolean)leftResult.value && (boolean)rightResult.value;
            val = new Sym(EnumType.booleano, result);
            return new Sym(EnumType.booleano, result);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion and.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Or(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;
        
        if(leftResult.type == EnumType.booleano && leftResult.type == EnumType.booleano)
        {
            result = (boolean)leftResult.value || (boolean)rightResult.value;
            val = new Sym(EnumType.booleano, result);
            return new Sym(EnumType.booleano, result);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion or.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Xor(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        Sym rightResult = (Sym)rightExp.execute(env);
        boolean result;
        
        if(leftResult.type == EnumType.booleano && leftResult.type == EnumType.booleano)
        {
            result = (boolean)leftResult.value ^ (boolean)rightResult.value;
            val = new Sym(EnumType.booleano, result);
            return new Sym(EnumType.booleano, result);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "Alguno o ambos operandos son de tipo incorrecto para la operacion xor.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    public Object Not(Enviroment env)
    {
        Sym leftResult = (Sym)leftExp.execute(env);
        boolean result;
        
        if(leftResult.type == EnumType.booleano)
        {
            result = !(boolean)leftResult.value;
            val = new Sym(EnumType.booleano, result);
            return new Sym(EnumType.booleano, result);
        }
        
        Error_ error = new Error_(line, column, "Semantico", "El operando es de tipo incorrecto para la operacion not.");
        env.getGlobal().errors.add(error);
        return new Sym(EnumType.error, "@error");
    }
    
    
    public int getLine()
    {
        return line;
    }
    
    public int getColumn()
    {
        return column;
    }
    
    public Expression_type getType(Sym.EnumType type)
    {
        switch(type)
        {
            case entero:
                return Expression_type.ENTERO;
                
            case doble:
                return Expression_type.DOBLE;
                
            case cadena:
                return Expression_type.CADENA;
                
            case caracter:
                return Expression_type.CARACTER;
                
            case booleano:
                return Expression_type.BOOLEANO;
        }
        return Expression_type.NULO;
    }
}
