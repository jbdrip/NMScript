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
public class Switch implements Instruction
{
    private Expression condition;
    private LinkedList<Case> caseList;
    public int line, column;

    public Switch(Expression condition, LinkedList<Case> caseList, int line, int column)
    {
        this.condition = condition;
        this.caseList = caseList;
        this.line = line;
        this.column = column;
    }
    public Object execute(Enviroment env)
    {
        try
        {
            Enviroment local = new Enviroment(env);
            condition.paramsResult = executeParams(condition, env);
            Sym conditionSym = (Sym)condition.execute(local);
            String conditionResult = conditionSym.toString();
            boolean onCondition = false;
            boolean end = false;
            Case defaultCase = null;

            for(Case caseIn : caseList)
            {
                Expression expresion = caseIn.operation;
                if(expresion != null)expresion.paramsResult = executeParams(expresion, env);
                if(onCondition)
                {
                    for(Instruction ins : caseIn.instructions)
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
                            Object instruction = ins.execute(local);
                            if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break"))
                            {
                                end = true;
                                break;
                            }
                            else if((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue"))
                            {
                                Error_ error = new Error_(line, column, "Semantico", "'continue' afuera de un ciclo.");
                                env.getGlobal().errors.add(error);
                            }
                            else if (instruction != null && (instruction instanceof Sym))
                            {
                                Sym sym = (Sym) instruction;
                                if(sym.breturn) return instruction;
                            } //sentencia return
                            //else if (instruction != null) return instruction; //sentencia return
                        }
                }
                else
                {
                    if(expresion != null)
                    {
                        Sym expSym = (Sym)expresion.execute(local);
                        String expresionResult = expSym.toString();

                        if(expresionResult.equals(conditionResult))
                        {
                            onCondition = true;
                            for(Instruction ins : caseIn.instructions)
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
                                Object instruction = ins.execute(local);
                                if(instruction != null && instruction.toString().equals("break"))
                                {
                                    end = true;
                                    break;
                                }
                                else if((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue"))
                                {
                                    Error_ error = new Error_(line, column, "Semantico", "'continue' afuera de un ciclo.");
                                    env.getGlobal().errors.add(error);
                                }
                                else if(instruction != null && (instruction instanceof String) && instruction.toString().equals("return")) return "return";
                                else if (instruction != null && (instruction instanceof Sym))
                                {
                                    Sym sym = (Sym) instruction;
                                    if(sym.breturn) return instruction;
                                } //sentencia return
                                //else if (instruction != null) return instruction; //sentencia return
                            }
                        }
                    }
                    else
                    {
                        defaultCase = caseIn;
                        break;
                    }
                }
                if(end) break;
            }
            if(!onCondition && defaultCase != null)
            {
                for(Instruction ins : defaultCase.instructions)
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
                    Object instruction = ins.execute(local);
                    if((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break"))
                    {
                        end = true;
                        break;
                    }
                    else if((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue"))
                    {
                        Error_ error = new Error_(line, column, "Semantico", "'continue' afuera de un ciclo.");
                        env.getGlobal().errors.add(error);
                    }
                    else if(instruction != null && (instruction instanceof String) && instruction.toString().equals("return")) return "return";
                    else if (instruction != null && (instruction instanceof Sym))
                    {
                        Sym sym = (Sym) instruction;
                        if(sym.breturn) return instruction;
                    } //sentencia return
                    //else if (instruction != null) return instruction; //sentencia return
                }
            }
        }
        catch(Exception ex)
        {
            Error_ error = new Error_(line, column, "De ejecucion", "Excepcion no controlada en sentencia 'Switch'.");
            env.getGlobal().errors.add(error);
        }
        return null;
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
    
    public int getLine()
    {
        return line;
    }
    
    public int getColumn()
    {
        return column;
    }
}
