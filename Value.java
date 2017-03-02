import java.math.BigDecimal;

public class Value extends BigDecimal implements Term
{
    private int id=NUMBER;
    
    public Value(String expr)
    {
	super(expr);
    }
    
    public Value(BigDecimal dec)
    {
	super(dec.toString());
    }
    
    public int getID()
    {
	return id;
    }
    
    public int getType()
    {
	return VALUE;
    }
    
    public String toString()
    {
	return super.toString();
    }
    
    public boolean equals(Object obj)
    {
	return super.equals(obj);
    }
    
    public static void main(String[] args)
    {
	
    }

}
