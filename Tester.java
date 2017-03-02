public class Tester
{
    public static void main(String[] args)
    {
	String ops="+-*/^";
	String other="1234567890=qwertyuiopasdfghjkl;'[]\\zxcvbnm,./7894561230.!@#$%^&()_QWERTYUIOP{}|ASDFGHJKL:ZXCVBNM<>?";
	for (int l=1;l<100;l++)
	{
	    String expr="";
	    boolean good=false;
	    if (Math.random()>.7) good=true;
	    for (int i=0;i<l;i++)
	    {
		if (good)
		    expr+=ops.charAt((int)(Math.random()*5));
		else
		    expr+=other.charAt((int)(Math.random()*99));
	    }
	    
	    try
	    {
		Function f=new Function(expr);
		System.out.println(good?"good":"bad");
	    }
	    catch (UnsupportedOperationException e)
	    {
		System.out.println(good?"bad":"good");
	    }
	}
	
	
	
	
    }
}
