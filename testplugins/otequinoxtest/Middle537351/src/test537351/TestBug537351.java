package test537351;

import static org.junit.Assert.*;

import org.junit.Test;

import base537351.BaseClass1;
import junit.framework.TestSuite;

public class TestBug537351 extends TestSuite {
	@Test
	public void zigzag() {
		StringBuilder result = new StringBuilder();
		new BaseClass1().doBase1(result);
		assertEquals("Collected result", "Team1.R|Middle1|Team2.R|base1", result.toString());
	}
}
