package syntax_out;

public team class T_testDeclaredLifting1
{
	public class R playedBy B_testDeclaredLifting1
	{
		public void bar(){}
	}
	
	public void foo(final B_testDeclaredLifting1 as R o)
	{
		extracted(o);
	}

	protected void extracted(final R o)
	{
		/*[*/o.bar();/*]*/
	}
}