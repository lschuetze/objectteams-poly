package test537533;

import static org.junit.Assert.*;

import org.junit.Test;

import aspect.Team2;
import base537533.MethodDeclarationCompletionProposal;
import junit.framework.TestSuite;

public class TestBug537533 extends TestSuite {
	@Test
	public void testDirect() {
		StringBuilder result = new StringBuilder();
		new Team2().run(result);
		assertEquals("AbstractJavaCompletionProposal", result.toString());
	}
	@Test
	public void testIntercepted() {
		StringBuilder result = new StringBuilder();
		new MethodDeclarationCompletionProposal().setReplacementString(result);
		assertEquals("RMethodDeclarationCompletionProposal", result.toString());
	}
}
