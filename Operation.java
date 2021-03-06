public class Operation implements Term
{
    static Operation SIN_OP=new Operation("sin");
    static Operation ARCSIN_OP=new Operation("arcsin");
    static Operation COS_OP=new Operation("cos");
    static Operation ARCCOS_OP=new Operation("arccos");
    static Operation TAN_OP=new Operation("tan");
    static Operation ARCTAN_OP=new Operation("arctan");
    static Operation LOG_OP=new Operation("log");
    static Operation LN_OP=new Operation("ln");
    static Operation SQRT_OP=new Operation("sqrt");
    
    static Operation POW_OP=new Operation("^");
    static Operation MULTIPLY_OP=new Operation("*");
    static Operation DIVIDE_OP=new Operation("/");
    static Operation ADD_OP=new Operation("+");
    static Operation SUBTRACT_OP=new Operation("-");
    
    private int id;
    
    public Operation(String expr)
    {
	expr=expr.toLowerCase();
	if (expr.equals("sin")) id=SIN;
	else if (expr.equals("arcsin")) id=ARCSIN;
	else if (expr.equals("cos")) id=COS;
	else if (expr.equals("arccos")) id=ARCCOS;
	else if (expr.equals("tan")) id=TAN;
	else if (expr.equals("arctan")) id=ARCTAN;
	else if (expr.equals("log")) id=LOG;
	else if (expr.equals("ln")) id=LN;
	else if (expr.equals("sqrt")) id=SQRT;
	else if (expr.equals("^")) id=POW;
	else if (expr.equals("*")) id=MULTIPLY;
	else if (expr.equals("/")) id=DIVIDE;
	else if (expr.equals("+")) id=ADD;
	else if (expr.equals("-")) id=SUBTRACT;
	else
	    throw new UnsupportedOperationException("Invalid Operation");
    }
    
    public int getID()
    {
	return id;
    }
    
    public int getType()
    {
	if (id>=SIN && id<=SQRT) return UNARY_OPERATION;
	else return BINARY_OPERATION;
    }
    
    public String toString()
    {
	if (id==SIN) return "sin";
	else if (id==ARCSIN)return "arcsin";
	else if (id==COS)return "cos";
	else if (id==ARCCOS)return "arccos";
	else if (id==TAN)return "tan";
	else if (id==ARCTAN)return "arctan";
	else if (id==LOG)return "log";
	else if (id==LOG)return "ln";
	else if (id==SQRT)return "sqrt";
	else if (id==POW)return "^";
	else if (id==MULTIPLY)return "*";
	else if (id==DIVIDE)return "/";
	else if (id==ADD)return "+";
	else if (id==SUBTRACT)return "-";
	else return "WEIRD";
    }
    
    public boolean equals(Object obj) {
	
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	    
	Operation o = (Operation) obj;
	return (this.id == o.getID());
    } 
}
