package base537350_b;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.framework.TestSuite;

public class TestBug537350_B extends TestSuite {
	@Test
	public void test() {
		StringBuilder result = new StringBuilder();
		new FindReadReferencesInProjectAction().get(result);
		assertEquals("RFindAction-FindReadReferencesAction-RFindReferencesAction", result.toString());
	}
}
