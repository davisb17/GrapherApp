public interface Term
{
    static final int NUMBER=0;
    
    static final int SIN=1;
    static final int ARCSIN=2;
    static final int COS=3;
    static final int ARCCOS=4;
    static final int TAN=5;
    static final int ARCTAN=6;
    static final int LOG=7;
    static final int LN=8;
    static final int SQRT=9;
    
    static final int POW=10;
    
    static final int MULTIPLY=11;
    static final int DIVIDE=12;
    
    static final int ADD=13;
    static final int SUBTRACT=14;
    
    static final int VALUE=101;
    static final int UNARY_OPERATION=102;
    static final int BINARY_OPERATION=103;
    
    public int getID();
    public int getType();
    public String toString();
    @Override
    public boolean equals(Object obj);
}
