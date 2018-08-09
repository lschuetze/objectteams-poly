package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	test537351.TestBug537351.class,
	test537533.TestBug537533.class,
	test537350_a.TestBug537350_A.class,
	base537350_b.TestBug537350_B.class
})
public class AllOTEquinoxTests extends TestSuite { }
