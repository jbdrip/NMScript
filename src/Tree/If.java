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
import proyecto_olc1.Error_;

/**
 *
 * @author Javier Bran
 */
public class If implements Instruction
{
    private Expression condition;
    private LinkedList<If> elseIfList;
    private LinkedList<Instruction> instructionsList;
    private LinkedList<Instruction> insListElse;
    public int line, column;

    public If(Expression condition, LinkedList<Instruction> instructionsList, int line, int column)
    {
        this.condition = condition;
        this.instructionsList = instructionsList;
        this.line = line;
        this.column = column;
    }

    public If(Expression condition, LinkedList<Instruction> instructionsList, LinkedList<Instruction> insListElse, int line, int column)
    {
        this.condition = condition;
        this.instructionsList = instructionsList;
        this.insListElse = insListElse;
        this.line = line;
        this.column = column;
    }

    public If(Expression condition, LinkedList<Instruction> instructionsList, LinkedList<Instruction> insListElse, LinkedList<If> elseIfList, int line, int column)
    {
        this.condition = condition;
        this.instructionsList = instructionsList;
        this.insListElse = insListElse;
        this.elseIfList = elseIfList;
        this.line = line;
        this.column = column;
    }

    public If(Expression condition, LinkedList<Instruction> instructionsList, LinkedList<If> elseIfList, boolean elseif, int line, int column)
    {
        this.condition = condition;
        this.instructionsList = instructionsList;
        this.elseIfList = elseIfList;
        this.line = line;
        this.column = column;
    }

    
    public Object execute(Enviroment env)
    {
        condition.paramsResult = executeParams(condition, env);
        Sym conditionValue = (Sym) condition.execute(env);
        if (conditionValue.type == EnumType.booleano && (boolean)conditionValue.value)
        {
            Enviroment localEnv = new Enviroment(env);
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
                    if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break")) return "break";
                    else if((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue")) return "continue";
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
        else if(conditionValue.type != EnumType.booleano)
        {
            Error_ error = new Error_(line, column, "De ejecucion", "Excepcion no controlada en sentencia 'If'.");
            env.getGlobal().errors.add(error);
        }
        else if(elseIfList != null)
        {
            boolean condition = false;
            If elseIf = null;

            for (int i = elseIfList.size() - 1; i >= 0; i--)
            {
                elseIf = elseIfList.get(i);
                Sym conValue = (Sym)elseIf.condition.execute(env);
                if (conValue.type == EnumType.booleano && (boolean)conValue.value)
                {
                    condition = true;
                    break;
                }
                else if(conValue.type != EnumType.booleano)
                {
                    Error_ error = new Error_(elseIf.line, elseIf.column, "De ejecucion", "Excepcion no controlada en sentencia 'If'.");
                    env.getGlobal().errors.add(error);
                }
            }

            if (condition)
            {
                Enviroment localEnv = new Enviroment(env);
                if(elseIf.instructionsList != null)
                {
                    for(Instruction ins : elseIf.instructionsList)
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
                        if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break")) return "break";
                        else if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue")) return "continue";
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
            else if (insListElse != null)
            {
                Enviroment localEnv = new Enviroment(env);
                if(insListElse != null)
                {
                    for(Instruction ins : insListElse)
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
                        if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break")) return "break";
                        else if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue")) return "continue";
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
        }
        else
        {
            if (insListElse != null)
            {
                Enviroment localEnv = new Enviroment(env);
                if(insListElse != null)
                {
                    for(Instruction ins : insListElse)
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
                        if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("break")) return "break";
                        else if ((instruction != null && (instruction instanceof String)) && instruction.toString().equals("continue")) return "continue";
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
