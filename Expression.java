import java.util.LinkedList;
import java.util.Scanner;
import java.math.BigDecimal;

public class Expression
{
    public static Value evaluate(String expr)
    {
	LinkedList<Term> out=new LinkedList<Term>();
	
	expr=replaceParenthesis(expr);
	
	int startI=0;
	
	while (startI<expr.length())
	{
	    //if last thing was symbol, consider positive or neg sign
	    if (expr.charAt(startI)=='+' || expr.charAt(startI)=='-')
	    {
		if (out.size()> 0 && out.getLast().getClass() != Operation.SIN_OP.getClass())
		{
		    out.add(new Operation(expr.substring(startI,startI+1)));
		    startI++;
		    continue;
		}
	    }
	    
	    
	    boolean success=false;
	    for (int endI=expr.length();endI>startI;endI--)
	    {
		String checkExpr=expr.substring(startI,endI);
		
		try
		{
		    Operation op=new Operation(checkExpr);
		    out.add(op);
		    success=true;
		    startI=endI;
		    break;
		}
		catch (UnsupportedOperationException e) { }
		
		try
		{
		    Value v=new Value(checkExpr); 
		    out.add(v);
		    success=true;
		    startI=endI;
		    break;
		}
		catch (NumberFormatException e) { }
	    }
	    if (!success)
		throw new UnsupportedOperationException("Invalid Syntax\nIndex: "+startI);
	}
	
	
	return simplify(out);
    }
    
    public static Value simplify(LinkedList<Term> terms)
    {
	if (terms.size()==1)
	    return (Value)terms.get(0);
	
	//ARG EXPRESSIONS
	int maxI=terms.indexOf(Operation.SIN_OP);
	maxI=Math.max(terms.indexOf(Operation.ARCSIN_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.COS_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.ARCCOS_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.TAN_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.ARCTAN_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.LOG_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.LN_OP),maxI);
	maxI=Math.max(terms.indexOf(Operation.SQRT_OP),maxI);
	
	if(maxI>=0)
	{
	    Operation op=(Operation)terms.remove(maxI);
	    Value val1=(Value)terms.remove(maxI);
	    terms.add(maxI,operate(op,val1,null));
	    return simplify(terms);
	}
	
	int powIndex=terms.indexOf(Operation.POW_OP);
	if (powIndex>=0)
	{
	    Value val1=(Value)terms.remove(powIndex-1);
	    Operation op=(Operation)terms.remove(powIndex-1);
	    Value val2=(Value)terms.remove(powIndex-1);
	    terms.add(powIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	
	int multDivIndex=Math.max(terms.lastIndexOf(Operation.MULTIPLY_OP),terms.lastIndexOf(Operation.DIVIDE_OP));
	if (multDivIndex>=0)
	{
	    Value val1=(Value)terms.remove(multDivIndex-1);
	    Operation op=(Operation)terms.remove(multDivIndex-1);
	    Value val2=(Value)terms.remove(multDivIndex-1);
	    terms.add(multDivIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	
	int addSubIndex=Math.max(terms.lastIndexOf(Operation.ADD_OP),terms.lastIndexOf(Operation.SUBTRACT_OP));
	if (addSubIndex>=0)
	{
	    Value val1=(Value)terms.remove(addSubIndex-1);
	    Operation op=(Operation)terms.remove(addSubIndex-1);
	    Value val2=(Value)terms.remove(addSubIndex-1);
	    terms.add(addSubIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	System.out.println("Something went wrong");
	throw new UnsupportedOperationException("Unable to simplify expression");
    }
    
    //Does single binary or unary operation
    public static Value operate(Operation op, Value val1, Value val2)
    {
	
	//Supported by BigDecimal
	if(op.equals(Operation.ADD_OP))
	    return new Value(val1.add(val2));
	    
	else if(op.equals(Operation.SUBTRACT_OP))
	    return new Value(val1.subtract(val2));
	    
	else if(op.equals(Operation.MULTIPLY_OP))
	    return new Value(val1.multiply(val2));
	    
	else if(op.equals(Operation.DIVIDE_OP))
	{
	    int scale=(val2.precision()-val1.precision())+10;
	    return new Value(val1.divide(val2,scale,BigDecimal.ROUND_HALF_DOWN));
	}
	
	//Unsupported by BigDecimal
	
	double val1D=val1.doubleValue();
	
	if(op.equals(Operation.SIN_OP))
	    return new Value(Math.sin(val1D)+"");
	    
	else if(op.equals(Operation.ARCSIN_OP))
	    return new Value(Math.asin(val1D)+"");
	    
	else if(op.equals(Operation.COS_OP))
	    return new Value(Math.cos(val1D)+"");
	    
	else if(op.equals(Operation.ARCCOS_OP))
	    return new Value(Math.acos(val1D)+"");
	    
	else if(op.equals(Operation.TAN_OP))
	    return new Value(Math.tan(val1D)+"");
	    
	else if(op.equals(Operation.ARCTAN_OP))
	    return new Value(Math.atan(val1D)+"");
	    
	else if(op.equals(Operation.LOG_OP))
	    return new Value(Math.log10(val1D)+"");
	    
	else if(op.equals(Operation.LN_OP))
	    return new Value(Math.log(val1D)+"");
	    
	else if(op.equals(Operation.SQRT_OP))
	    return new Value(Math.sqrt(val1D)+"");
	    
	double val2D=val2.doubleValue();
	    
	if(op.equals(Operation.POW_OP))
	    return new Value(Math.pow(val1D,val2D)+"");
	
	else throw new UnsupportedOperationException("Couldn't find operation");
    }
    
    public static String replaceParenthesis(String expr)
    {
	int openI=expr.indexOf("(");
	if (openI==-1) return expr;
	
	int opened=1;
	int closed=0;
	
	for (int i=openI+1;i<expr.length();i++)
	{
	    if (expr.charAt(i)=='(')
		opened++;
	    else if (expr.charAt(i)==')')
		closed++;
		
	    if (opened==closed)
	    {
		String before=expr.substring(0,openI);
		String newVal=evaluate(expr.substring(openI+1,i)).toString();
		String after=replaceParenthesis(expr.substring(i+1));
		
		return before+newVal+after;
	    }
	}
	throw new UnsupportedOperationException("Invalid Parentheses");
    }
 
    public static void main(String[] args)
    {
	/*
	for (int i=1;i<10000;i++)
	{
	    String expr="18*(sin(x^2))+arcsin1*x+sqrt(lnx)";
	    expr=expr.replace("x",i+"");
	    operate(Operation.SIN_OP,evaluate(expr),null);
	    if (i%100==0)
		System.out.println(i);
	}
	*/
	Scanner sc=new Scanner(System.in);
	
	while (sc.hasNextLine())
	{
	    System.out.println(evaluate(sc.nextLine()));
	}
	
    }
    
}
