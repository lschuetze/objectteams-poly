package test537350_a;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import base537350_a.PluginJUnitMainTab;
import junit.framework.TestSuite;

public class TestBug537350_A extends TestSuite {
	@Test
	public void test() {
		StringBuilder result = new StringBuilder();
		new PluginJUnitMainTab().runit(result);
		assertEquals("PluginJUnitMainTab-PDELaunchingAdapter.MainTab", result.toString());
	}
}
