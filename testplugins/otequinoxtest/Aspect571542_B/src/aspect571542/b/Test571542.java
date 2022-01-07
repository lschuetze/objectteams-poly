package aspect571542.b;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import aspect571542.a.C1;
import aspect571542.a.Team_A;
import junit.framework.TestSuite;

public class Test571542 extends TestSuite {
	
	@Test
	public void test() {
		C3 c3 = new C3();
		within (new Team_A()) {
			StringBuilder buf = new StringBuilder();
			new C1().start(buf);
			c3.start(buf);
			assertEquals("Overall result", "C1C0RoleAC3C2C0", buf.toString());
		}
	}
}
