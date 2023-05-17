package executor;

import ast.*;
import lexer.TOKEN;

import java.util.function.Predicate;

import static lexer.TOKEN.CONS;

public class Executor {

    private String lastId;

    public Executor() {
    }

    public void execute(Expression program) throws Exception {
        Value v = this.defer(program, null);
        showValue(v);
    }

    private Value defer(Expression program, Env env) {
        return new Value(ValueType.Deferred, program, env);

    }

    private void showValue(Value v) throws Exception {
        if (v == null) {
            System.out.println("NULL");
            return;
        }
        switch (v.type()) {
            case Number -> System.out.println(v.number());
            case Boolean -> System.out.println(v.boolValue());
            case Character -> System.out.println(v.getCh());
            case Nil -> System.out.println("nil");
            case Empty -> System.out.println("()");
            case List -> {
                System.out.println("(");
                showValue(v.getHd());
                System.out.println("::");
                this.showValue(v.getTl());
                System.out.println(")");
            }
            case Function -> System.out.println("function");
            case Deferred -> {
                force(v);
                showValue(v);
            }

        }
    }

    private Value force(Value v) throws Exception {
        if (v.type().equals(ValueType.Deferred)) {
            Value v2 = eval(v.getExp(), v.getEnv());
            v.setValue(v2);
        }
        return v;
    }

    private Value applyEnv(Env env, String id) throws Exception {
        lastId = id;
        if (env == null)
            throw new Exception("Las id: " + lastId + "not found");
        else if (id.equals(env.id())) {
            force(env.v());
            return env.v();
        } else
            return applyEnv(env.next(), id);
    }

    private Value eval(Expression exp, Env env) throws Exception {

        Predicate<Object> isClass = r -> exp.getClass().equals(r);

        if (isClass.test(Ident.class))
            return applyEnv(env, ((Ident) exp).id());

        else if (isClass.test(NumCon.class))
            return new Value(((NumCon) exp).number());

        else if (isClass.test(BoolCon.class))
            return new Value(((BoolCon) exp).boolValue());

        else if (isClass.test(CharCon.class))
            return new Value(((CharCon) exp).ch());

        else if (isClass.test(NilCon.class))
            return new Value(ValueType.Nil);

        else if (isClass.test(EmptyCon.class))
            return new Value(ValueType.Empty);
        else if (isClass.test(LambdaExp.class)) {
            return new Value(ValueType.Function, exp, env);
        } else if (isClass.test(Application.class)) {
            Value func = eval(((Application) exp).fun(), env);

            if (func.type().equals(ValueType.Function))
                return apply(func, defer(((Application) exp).aparm(), env));
            else
                throw new Exception("apply ~fn ");

        } else if (isClass.test(UnaryExpression.class)) {

            var unExp = (UnaryExpression) exp;
            return evalUnary(unExp.operator(), eval(unExp.unaryExpression(), env));

        } else if (isClass.test(BinaryExpression.class)) {

            var binaryExp = (BinaryExpression) exp;
            if (binaryExp.operator().equals(CONS))  //cons should not eval params
                return evalBinary(
                        binaryExp.operator(),
                        defer(binaryExp.left(), env),
                        defer(binaryExp.right(), env)
                );
            else
                return evalBinary(
                        binaryExp.operator(),
                        eval(binaryExp.left(), env),
                        eval(binaryExp.right(), env)
                );

        } else if (isClass.test(IfExp.class)) {

            var ifExpr = (IfExp) exp;
            var value = eval(ifExpr.expression1(), env);

            if (!value.type().equals(ValueType.Boolean)) // check if value of predicate
                throw new Exception("if ~bool  ");

            if (value.boolValue())
                return eval(ifExpr.expression2(), env);

            else
                return eval(ifExpr.expression3(), env);

        } else if (isClass.test(Block.class)) {

            var blockExpr = (Block) exp;
            return eval(
                    blockExpr.expression(),
                    evalDecs(blockExpr.declarations(), env)
            );

        } else
            throw new Exception("Invalid Expression type: " + exp.getClass());


    }

    private Env evalDecs(Expression decs, Env env) {
        if (decs == null)
            return env;

        if (decs instanceof DecList decal && decal.recursive()) {
            Env localEnv = bind("--dumy__", null, env);
            localEnv.setNext(constructEnv(decal, localEnv, env));
            return localEnv;
        } else
            return constructEnv(decs, env, env);

    }

    private Env constructEnv(Expression decs, Env local, Env global) {
        if (decs == null)
            return global;
        else {
            var decal = (DecList) decs;
            var decaln = (Decln) decal.hd();

            return this.bind(
                    decaln.name(),
                    this.defer(decaln.val(), local),
                    this.constructEnv(decal.tl(), local, global));
        }
    }

    private Value evalBinary(TOKEN operator, Value left, Value right) throws Exception {
        switch (operator) {
            case EQ -> new Value(abs(left) == abs(right));
            case NE -> new Value(abs(left) != abs(right));
            case LT -> new Value(abs(left) < abs(right));
            case GT -> new Value(abs(left) > abs(right));
            case LE -> new Value(abs(left) <= abs(right));
            case GE -> new Value(abs(left) >= abs(right));
            case PLUS, MINUS, MUL, DIV -> {
                if (!(left.type().equals(ValueType.Number) && right.type.equals(ValueType.Number)))
                    throw new Exception("arith opr ");

                var l = left.number();
                var r = right.number();
                switch (operator) {
                    case PLUS -> {
                        return new Value(l + r);
                    }
                    case MINUS -> {
                        return new Value(l - r);
                    }
                    case MUL -> {
                        return new Value(l * r);
                    }
                    case DIV -> {
                        return new Value(l / r);
                    }
                }
            }
            case OR, AND -> {
                if (left.type().equals(ValueType.Boolean) && right.type.equals(ValueType.Boolean))
                    throw new Exception("bool opr ");
                var l = left.boolValue();
                var r = right.boolValue();
                switch (operator) {
                    case AND -> {
                        return new Value(l && r);
                    }
                    case OR -> {
                        return new Value(l || r);
                    }
                }
            }
            case CONS -> {
                return new Value(left, right);
            }

        }
        throw new Exception("Invalid binary operator: " + operator);
    }

    private double abs(Value operand) throws Exception {
        Predicate<ValueType> isValueType = r -> operand.type().equals(r);

        if (isValueType.test(ValueType.Number))
            return operand.number();
        else if (isValueType.test(ValueType.Boolean))
            return operand.boolValue() ? 1 : 0;
        else if (isValueType.test(ValueType.Character))
            return operand.getCh();
        else
            throw new Exception("rel ops   : " + operand);
    }


    private Value evalUnary(TOKEN operator, Value value) throws Exception {
        Predicate<ValueType> isValueType = r -> value.type().equals(r);

        switch (operator) {
            case MINUS -> {
                if (!isValueType.test(ValueType.Number))
                    throw new Exception(" not -int ");
                return new Value(-value.number());
            }
            case NOT -> {
                if (!isValueType.test(ValueType.Boolean))
                    throw new Exception(" not -bool ");
                return new Value(!value.boolValue());
            }
            case HD -> {
                if (!isValueType.test(ValueType.List))
                    throw new Exception(" not -list ");
                force(value.getHd());
                return value.getHd();
            }
            case TL -> {
                if (!isValueType.test(ValueType.List))
                    throw new Exception(" not -list ");
                force(value.getTl());
                return value.getTl();
            }
            case NULL -> {
                if (isValueType.test(ValueType.List))
                    return new Value(false);
                else if (isValueType.test(ValueType.Nil))
                    return new Value(true);
                else
                    throw new Exception(" not -list ");
            }
            default -> throw new Exception(" invalid unary operator : " + operator);
        }
    }

    private Value apply(Value func, Value value) throws Exception {
        if (((LambdaExp) func.getExp()).fParam().getClass().equals(EmptyCon.class)) {
            force(value);
            if (value.type().equals(ValueType.Empty))
                return eval(((LambdaExp) func.getExp()).body(), func.getEnv());
            else
                throw new Exception("L().e exp");
        } else {
            return eval(
                    ((LambdaExp) func.getExp()).body(),
                    bind(
                            ((Ident) (((LambdaExp) func.getExp()).fParam())).id(),
                            value,
                            func.getEnv()
                    ));
        }
    }

    private Env bind(String id, Value defer, Env env) {
        return new Env(id, defer, env);
    }

}
