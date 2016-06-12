import org.junit.Test;
import static org.junit.Assert.*;

public class TheTest {
	@Test
	public void testIt() {
		new TestTeam().activate();
		new TheBase().bar();
		assertEquals("Result:", "barfoos\n", TheBase.result);
	}
}