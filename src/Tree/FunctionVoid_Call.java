/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import Enviroment.Enviroment;
import Enviroment.Sym;
import java.util.LinkedList;
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class FunctionVoid_Call implements Instruction
{
    public LinkedList<Expression> parametersExpression;
    public LinkedList<Sym> paramsResult;
    public int line, column;
    public String id;
    
    public FunctionVoid_Call(String id, LinkedList<Expression> parametersExpression, int line, int column)
    {
        this.id = id;
        this.line = line;
        this.column = column;
        this.parametersExpression = parametersExpression;
    }
    
    @Override
    public Object execute(Enviroment env) 
    {
        boolean function = false;
        Enviroment local = new Enviroment(env.getOnClass());
        if(parametersExpression == null) parametersExpression = new LinkedList<>();
        Sym functionVoid_ = local.search(id + parametersExpression.size() + "$$", line, column);
        
        if(functionVoid_ == null)
        {
            functionVoid_ = local.search(id + parametersExpression.size() + "%%", line, column);
            function = true;
        }
        
        if(functionVoid_ != null)
        { 
            ParametersIns parametersIns = (ParametersIns) functionVoid_.value;
            LinkedList<Declaration> parameters = parametersIns.parameters;
            LinkedList<Instruction> instructions = parametersIns.instructions;
            int index = 0;
            
            if(parameters != null)
            {
                for(Declaration d : parameters)
                {
                    Sym sym = (Sym) paramsResult.get(index++);
                    d.execute(local);
                    local.updateValue(d.id, sym, line, column);
                }
            }
            
            if(instructions != null)
            {
                for(Instruction ins : instructions)
                {
                    if(ins instanceof Signature)
                    {
                        Signature signature = (Signature) ins;
                        signature.paramsResult = executeParams(signature.parameters, local);
                        ins = signature;
                    }
                    else if(ins instanceof Instance)
                    {
                        Instance instance = (Instance) ins;
                        instance.paramsResult = executeParams(instance.parameters, local);
                        ins = instance;
                    }
                    else if(ins instanceof FunctionVoid_Call)
                    {
                        FunctionVoid_Call call = (FunctionVoid_Call) ins;
                        call.paramsResult = executeParams(call.parametersExpression, local);
                        ins = call;
                    }
                    else if(ins instanceof Expression)
                    {
                        Expression expression = (Expression) ins;
                        expression.paramsResult = executeParams(expression, local);
                        ins = expression;
                    }
                    
                    Object result = ins.execute(local);
                    
                    if(result != null && function && (result instanceof Sym) && !(ins instanceof Expression)) //sentencia return
                    {
                        Sym sym = (Sym) result;
                        if(sym.breturn) return sym;
                    }
                    else if(result != null && (result instanceof Sym) && !(ins instanceof Expression))
                    {
                        Sym sym = (Sym) result;
                        if(sym.breturn)
                        {
                            Error_ error = new Error_(line, column, "Semantico", "'return EXPRESION' afuera de una funcion.");
                            env.getGlobal().errors.add(error);
                        }
                    }
                    else if(result != null && !function && (result instanceof String) && result.toString().equals("return") && !(ins instanceof Expression)) return "return";
                    else if(result != null && (result instanceof String) && result.toString().equals("return") && !(ins instanceof Expression))
                    {
                        Error_ error = new Error_(line, column, "Semantico", "'return' afuera de un metodo.");
                        env.getGlobal().errors.add(error);
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
                
                if(function)
                {
                    Error_ error = new Error_(line, column, "De ejecucion", "Error en sentencia return de funcion (return fue nulo).");
                    env.getGlobal().errors.add(error);
                    return new Sym(Sym.EnumType.error, "@error");
                }
            }
        }
        else
        {
            Error_ error = new Error_(line, column, "Semantico", "El metodo '" + id + "' no existe en el ambito actual o los parametros no son correctos.");
            env.getGlobal().errors.add(error);
        }
        return null;
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
                    if(params.size() < parameters.size()) params.add(i, s);
                    else params.set(i, s);
                }
            }
        }
        else return null;
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
