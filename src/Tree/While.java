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
public class While implements Instruction
{
    private Expression condition;
    private LinkedList<Instruction> instructionsList;
    public int line, column;

    public While(Expression condition, LinkedList<Instruction> list, int line, int column)
    {
        this.condition = condition;
        this.instructionsList = list;
        this.line = line;
        this.column = column;
    }

    public Object execute(Enviroment env)
    {
        boolean end = false;
        boolean next = false;

        try
        {
            condition.paramsResult = executeParams(condition, env);
            while ((boolean)((Sym)condition.execute(env)).value)
            {
                Enviroment localEnv = new Enviroment(env);
                if (env.getOnClass_()) localEnv.setOnClass(true);

                if(instructionsList != null)
                {
                    for(Instruction ins : instructionsList)
                    {
                        if(ins instanceof Signature)
                        {
                            Signature signature = (Signature) ins;
                            signature.paramsResult = executeParams(signature.parameters, localEnv);
                            ins = signature;
                        }
                        else if(ins instanceof Instance)
                        {
                            Instance instance = (Instance) ins;
                            instance.paramsResult = executeParams(instance.parameters, localEnv);
                            ins = instance;
                        }
                        else if(ins instanceof FunctionVoid_Call)
                        {
                            FunctionVoid_Call call = (FunctionVoid_Call) ins;
                            call.paramsResult = executeParams(call.parametersExpression, localEnv);
                            ins = call;
                        }
                        else if(ins instanceof Expression)
                        {
                            Expression expression = (Expression) ins;
                            expression.paramsResult = executeParams(expression, localEnv);
                            ins = expression;
                        }
                        Object instruction = ins.execute(localEnv);
                        if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break"))
                        {
                            end = true;
                            break;
                        }
                        else if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue"))
                        {
                            next = true;
                            break;
                        }
                        else if(instruction != null && (instruction instanceof String) && instruction.toString().equals("return")) return "return";
                        else if (instruction != null && (instruction instanceof Sym))
                        {
                            Sym sym = (Sym) instruction;
                            if(sym.breturn) return instruction;
                        } //sentencia return
                        //else if (instruction != null) return instruction; //sentencia return
                    }

                    if (end) break;
                    if (next) continue;
                }
            }
        }
        catch(Exception ex)
        {
            Error_ error = new Error_(line, column, "De ejecucion", "Excepcion no controlada en sentencia 'While'.");
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
