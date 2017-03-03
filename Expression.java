import java.util.LinkedList;
import java.util.Scanner;
import java.math.BigDecimal;

public class Expression
{
    public static double evaluate(String expr)
    {
	LinkedList<Object> out=new LinkedList<Object>();
	
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
		    Double v=new Double(checkExpr); 
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
    
    public static double simplify(LinkedList<Object> terms)
    {
	if (terms.size()==1)
	    return (Double)terms.get(0);
	
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
	    double val1=(double)terms.remove(maxI);
	    terms.add(maxI,operate(op,val1,0));
	    return simplify(terms);
	}
	
	int powIndex=terms.indexOf(Operation.POW_OP);
	if (powIndex>=0)
	{
	    double val1=(double)terms.remove(powIndex-1);
	    Operation op=(Operation)terms.remove(powIndex-1);
	    double val2=(double)terms.remove(powIndex-1);
	    terms.add(powIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	
	int multDivIndex=Math.max(terms.lastIndexOf(Operation.MULTIPLY_OP),terms.lastIndexOf(Operation.DIVIDE_OP));
	if (multDivIndex>=0)
	{
	    double val1=(double)terms.remove(multDivIndex-1);
	    Operation op=(Operation)terms.remove(multDivIndex-1);
	    double val2=(double)terms.remove(multDivIndex-1);
	    terms.add(multDivIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	
	int addSubIndex=Math.max(terms.lastIndexOf(Operation.ADD_OP),terms.lastIndexOf(Operation.SUBTRACT_OP));
	if (addSubIndex>=0)
	{
	    double val1=(double)terms.remove(addSubIndex-1);
	    Operation op=(Operation)terms.remove(addSubIndex-1);
	    double val2=(double)terms.remove(addSubIndex-1);
	    terms.add(addSubIndex-1,operate(op,val1,val2));
	    return simplify(terms);
	}
	System.out.println("Something went wrong");
	throw new UnsupportedOperationException("Unable to simplify expression");
    }
    
    //Does single binary or unary operation
    public static double operate(Operation op, double val1, double val2)
    {
	
	//Supported by BigDecimal
	if(op.equals(Operation.ADD_OP))
	    return val1+val2;
	    
	else if(op.equals(Operation.SUBTRACT_OP))
	    return val1-val2;
	    
	else if(op.equals(Operation.MULTIPLY_OP))
	    return val1*val2;
	    
	else if(op.equals(Operation.DIVIDE_OP))
	    return val1/val2;
	
	else if(op.equals(Operation.SIN_OP))
	    return Math.sin(val1);
	    
	else if(op.equals(Operation.ARCSIN_OP))
	    return Math.asin(val1);
	    
	else if(op.equals(Operation.COS_OP))
	    return Math.cos(val1);
	    
	else if(op.equals(Operation.ARCCOS_OP))
	    return Math.acos(val1);
	    
	else if(op.equals(Operation.TAN_OP))
	    return Math.tan(val1);
	    
	else if(op.equals(Operation.ARCTAN_OP))
	    return Math.atan(val1);
	    
	else if(op.equals(Operation.LOG_OP))
	    return Math.log10(val1);
	    
	else if(op.equals(Operation.LN_OP))
	    return Math.log(val1);
	    
	else if(op.equals(Operation.SQRT_OP))
	    return Math.sqrt(val1);
	    
	else if(op.equals(Operation.POW_OP))
	    return Math.pow(val1,val2);
	
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
		String newVal=evaluate(expr.substring(openI+1,i))+"";
		String after=replaceParenthesis(expr.substring(i+1));
		
		return before+newVal+after;
	    }
	}
	throw new UnsupportedOperationException("Invalid Parentheses");
    }
 
    public static void main(String[] args)
    {
	
	for (int i=1;i<10000;i++)
	{
	    String expr="18*(sin(x^2))+arcsin1*x+sqrt(lnx)";
	    expr=expr.replace("x",i+"");
	    operate(Operation.SIN_OP,evaluate(expr),0);
	    if (i%100==0)
		System.out.println(i);
	}
	
	Scanner sc=new Scanner(System.in);
	
	while (sc.hasNextLine())
	{
	    System.out.println(evaluate(sc.nextLine()));
	}
	
    }
    
}
