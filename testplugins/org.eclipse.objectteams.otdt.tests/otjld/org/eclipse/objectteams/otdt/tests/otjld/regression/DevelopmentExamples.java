/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2005-2016 Berlin Institute of Technology, Germany.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.regression;

import java.util.Map;

import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

/**
 * @author Christine Hundt
 * @author Stephan Herrmann
 */
@SuppressWarnings("unchecked")
public class DevelopmentExamples extends AbstractOTJLDTest {
	
	public DevelopmentExamples(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testX12_resultLifting2"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return DevelopmentExamples.class;
	}

    // corner cases for callin with inheritance
    // X.1.1-otjld-binding-inheritance-1
    public void testX11_bindingInheritance1() {
       
       runConformTest(
            new String[] {
		"test1/X11bi1Main.java",
			    "\n" +
			    "package test1;\n" +
			    "public class X11bi1Main {\n" +
			    "        public static void main (String[] args) {\n" +
			    "		new SubSubTeam().activate();\n" +
			    "		//new CallinSuper().activate();\n" +
			    "		//new CallinSub(\"t1\").activate();\n" +
			    "		//new CallinSub(\"t2\").activate();\n" +
			    "		//new ExtremeSub().activate(); // runntime-error: what is wrong with this??\n" +
			    "		System.out.println((new B1(3)).getInt());\n" +
			    "		B1 b = new B1(0);\n" +
			    "		b.setPublicInt(1);\n" +
			    "		System.out.println(\"--------------------------------------\");\n" +
			    "		b.nop();\n" +
			    "		System.out.println(\"--------------------------------------\");\n" +
			    "		b.nop(1);\n" +
			    "		System.out.println(\"--------------------------------------\");\n" +
			    "		B2 b2 = new B2(\"Heiligabend\");\n" +
			    "		//SubOfB2 b2 = new SubOfB2(\"Heiligabend\");\n" +
			    "		b2.print(\"Geschenk\");\n" +
			    "		//new CallinSuper().activate();\n" +
			    "		//b.setInt(1);\n" +
			    "		System.out.println(\"-------------------- Test for Base-Subclassing ---------------\");\n" +
			    "		Person p = new Person(\"Ishi\");\n" +
			    "		p.goToWork();\n" +
			    "		System.out.println(\"---- just one sub base (Man) is effected by a callin ------\");\n" +
			    "		System.out.println(\"---- defined in a sub role (BWLStudent):-----------------------\");\n" +
			    "		Woman w = new Woman(\"Yukiko\");\n" +
			    "		Man m = new Man(\"Tanaka\");\n" +
			    "		w.goToWork();\n" +
			    "		m.goToWork();\n" +
			    "		System.out.println(\"----- callins are effective even for inherited base methods in sub bases: ------\");\n" +
			    "		p.haveFun();\n" +
			    "		w.haveFun();\n" +
			    "		System.out.println(\"----- callins are inherited to neither redefinded nor rebound base methods: --\");\n" +
			    "		p.performWorkstep();\n" +
			    "                w.performWorkstep();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"test1/B1.java",
			    "\n" +
			    "package test1;\n" +
			    "public class B1 {\n" +
			    "        private int _value;\n" +
			    "        public B1(int i) {\n" +
			    "                _value = i;\n" +
			    "        }\n" +
			    "        public int getInt() {\n" +
			    "                System.out.println(\"B1.getInt() called!\");\n" +
			    "                return _value;\n" +
			    "        }\n" +
			    "        private void setInt(int i) {\n" +
			    "                System.out.println(\"B1.setInt(\"+i+\") called!\");\n" +
			    "                _value = i;\n" +
			    "        }\n" +
			    "        public void setPublicInt(int i) {\n" +
			    "                setInt(i);\n" +
			    "        }\n" +
			    "        public void nop() {\n" +
			    "                System.out.println(\"B1.nop() called!\");\n" +
			    "        }\n" +
			    "        public void nop(int i) {\n" +
			    "                System.out.println(\"B1.nop(int) called!\");\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"test1/B2.java",
			    "\n" +
			    "package test1;\n" +
			    "import java.util.Date;\n" +
			    "public class B2 {\n" +
			    "        private Date _date;\n" +
			    "		 @SuppressWarnings(\"unused\")\n" +
			    "        private String _name;\n" +
			    "        public B2(String name) {\n" +
			    "                _date = new Date();\n" +
			    "                _name = name;\n" +
			    "        }\n" +
			    "        public Date getDate(boolean update) {\n" +
			    "                if (update)\n" +
			    "                        return new Date();\n" +
			    "                return _date; // date of creation\n" +
			    "        }\n" +
			    "        public void print(String s) {\n" +
			    "               System.out.print(\"B2.print: \" + s + \"\\n\");\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"test1/Person.java",
			    "\n" +
			    "package test1;\n" +
			    "public class Person {\n" +
			    "        private String _name;\n" +
			    "        public Person(String name) {\n" +
			    "                _name = name;\n" +
			    "        }\n" +
			    "        public void goToWork() {\n" +
			    "                System.out.println(\"Person.goToWork()\");\n" +
			    "        }\n" +
			    "        public void performWorkstep() {\n" +
			    "                System.out.println(\"Person.performWorkstep()\");\n" +
			    "        }\n" +
			    "        public void haveFun() {\n" +
			    "                System.out.println(\"Person.haveFun()\");\n" +
			    "        }\n" +
			    "        public String getName() {\n" +
			    "                return _name;\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"test1/Woman.java",
			    "\n" +
			    "package test1;\n" +
			    "public class Woman extends Person {\n" +
			    "        public Woman(String name) {\n" +
			    "                super(name);\n" +
			    "        }\n" +
			    "		 @Override\n" +
			    "        public void goToWork() {\n" +
			    "                System.out.println(\"She (\"+getName()+\") gose to work.\");\n" +
			    "        }\n" +
			    "        /*\n" +
			    "        public void haveFun() {\n" +
			    "                super.haveFun();\n" +
			    "        }*/\n" +
			    "	/*\n" +
			    "        public void performWorkstep() {\n" +
			    "                System.out.println(\"Woman.performWorkstep\");\n" +
			    "        }*/\n" +
			    "}\n" +
			    "	\n",
		"test1/Man.java",
			    "\n" +
			    "package test1;\n" +
			    "public class Man extends Person {\n" +
			    "        public Man(String name) {\n" +
			    "                        super(name);\n" +
			    "        }\n" +
			    "		 @Override\n" +
			    "        public void goToWork() {\n" +
			    "                System.out.println(\"He (\"+getName()+\") gose to work.\");\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"test1/CallinSuper.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class CallinSuper {\n" +
			    "	public void superTeamMethod() {\n" +
			    "	}\n" +
			    "	public class Role playedBy B1 {\n" +
			    "		callin void hook () {\n" +
			    "			System.out.println(/*getName()+\": \"+*/\"CallinSuper.Role.hook()\");\n" +
			    "			base.hook();\n" +
			    "		}\n" +
			    "                //hook <- replace getInt;\n" +
			    "                //void hook() <- replace int getInt() with {}\n" +
			    "		callin void check(int i) {\n" +
			    "			System.out.println(\"CallinSuper.Role.check(\"+i+\")\");\n" +
			    "			if (i<0)\n" +
			    "				throw new Error(\"only positive numbers allowed!!\");\n" +
			    "			base.check(i);\n" +
			    "		}\n" +
			    "		@SuppressWarnings(\"decapsulation\")\n" +
			    "		check <- replace setInt;\n" +
			    "	}\n" +
			    "	public class Role3 {//playedBy B2 \n" +
			    "		callin void format(String s) {\n" +
			    "			System.out.println(\"CallinSuper.Role3.format: \" + s.toUpperCase());\n" +
			    "			base.format(\"von CallinSuper.Role3\");\n" +
			    "		}\n" +
			    "		//format <- replace print;\n" +
			    "        }\n" +
			    "	public class Student playedBy Person {\n" +
			    "        	callin void doHomework() {\n" +
			    "			System.out.println(\"Stundent.doHomework()\");\n" +
			    "			base.doHomework();\n" +
			    "		}\n" +
			    "		void doHomework() <- replace void performWorkstep();\n" +
			    "		callin void enterClassroom() {\n" +
			    "			System.out.println(\"Stundent.enterClassroom()\");\n" +
			    "			base.enterClassroom();\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public class BWLStudent extends Student playedBy Man {\n" +
			    "		void enterClassroom() <- replace void goToWork();\n" +
			    "	}\n" +
			    "	// ->  Binding-Error: base-call impossible!\n" +
			    "   public class JugglingStudent extends Student playedBy Woman {\n" +
			    "		void enterClassroom() <- replace void haveFun();\n" +
			    "	} //\n" +
			    "}\n" +
			    "	\n",
		"test1/CallinSub.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class CallinSub extends CallinSuper {\n" +
			    "/*\n" +
			    "                public CallinSub(String name) {\n" +
			    "                        super(name);\n" +
			    "                }\n" +
			    "*/\n" +
			    "	public class Role2 extends Role {\n" +
			    "       @Override\n" +
			    "		callin void hook () {\n" +
			    "			System.out.println(/*getName()+\": \"+*/ \"CallinSub.Role2.hook()\");\n" +
			    "			base.hook();\n" +
			    "			//tsuper.hook();        // compiler: cannot resolve symbol <- okay\n" +
			    "			super.hook();\n" +
			    "		}\n" +
			    "		// this works well:\n" +
			    "		//void hook() <- replace void nop();\n" +
			    "//		hook <- getInt;\n" +
			    "		@Override\n" +
			    "		void hook() <- replace int getInt();\n" +
			    "		//void hook() <- void nop() with {}\n" +
			    "		@Override\n" +
			    "		callin void check(int i) {\n" +
			    "			System.out.println(\"CallinSub.Role2.check(\"+i+\")\");\n" +
			    "			base.check(i);\n" +
			    "			super.check(i);\n" +
			    "		}\n" +
			    "		//check <- setInt;\n" +
			    "		/*/ compiler-error:\n" +
			    "			void check(int i) <- void nop() with {\n" +
			    "				i <- 0\n" +
			    "			}//*/\n" +
			    "	}\n" +
			    "	@Override\n" +
			    "	public class Role3 playedBy B2 {\n" +
			    "		@Override @SuppressWarnings(\"basecall\")\n" + // two basecalls (one is indirect)
			    "       callin void format(String s) {\n" +
			    "			System.out.println(\"CallinSub.Role3.format: \" + insertChars(\" \", s));\n" +
			    "			base.format(\"von CallinSub.Role3\");\n" +
			    "			tsuper.format(s);\n" +
			    "		}\n" +
			    "		//format <- replace print;\n" +
			    "		String insertChars(String insert, String s) {\n" +
			    "			char[] chrs = s.toCharArray();\n" +
			    "			String result =\"\";\n" +
			    "			for (int i=0; i<chrs.length; i++) {\n" +
			    "				String tmp = chrs[i]+insert;\n" +
			    "				result = result.concat(tmp);\n" +
			    "			}\n" +
			    "			return result;\n" +
			    "		}\n" +
			    "	}\n" +
			    "/*\n" +
			    "	public class Role4 extends Role3 {\n" +
			    "		callin void format(String s) {\n" +
			    "			System.out.println(\"CallinSub.Role4.format: \" + s);\n" +
			    "			base.format(\"von CallinSub.Role4\");\n" +
			    "			super.format(s);\n" +
			    "		}\n" +
			    "		//format <- replace print;\n" +
			    "	}*/\n" +
			    "}\n" +
			    "	\n",
		"test1/SubSubTeam.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class SubSubTeam extends CallinSub {\n" +
			    "		 @Override\n" +
			    "        public class Role3 {\n" +
			    "				@Override @SuppressWarnings(\"basecall\")\n" + // two basecalls (one is indirect)
			    "               callin void format(String s) {\n" +
			    "               	     System.out.println(\"SubSubTeam.Role3.format: \" + s);\n" +
			    "                        base.format(\"von SubSubTeam.Role3\");\n" +
			    "                        tsuper.format(s);\n" +
			    "               }\n" +
			    "        void format(String s) <- replace void print(String s);\n" +
			    "        }\n" +
			    "        public class RoleX extends Role3 {\n" +
			    "				@Override\n" +
			    "               callin void format(String s) {\n" +
			    "                   System.out.println(\"SubSubTeam.RoleX.format: \" + super.insertChars(\"X\", s));\n" +
			    "					base.format(\"von SubSubTeam.RoleX\");\n" +
			    "					super.format(s);\n" +
			    "				}\n" +
			    "				//format <- replace print;\n" +
			    "        }\n" +
			    "        public class RoleNOP extends Role2 {\n" +
			    "                callin void nop() {\n" +
			    "                        System.out.println(\"SubSubTeam.RoleNOP.nop() called!\");\n" +
			    "                        base.nop();\n" +
			    "                }\n" +
			    "                //nop <- after nop; // okay\n" +
			    "                //hook <- nop //-> compiler error!\n" +
			    "                //void hook() <- void nop(); //-> compiler error!\n" +
			    "/*\n" +
			    "                callin void hook () {\n" +
			    "                        System.out.println(\"SubSubTeam.RoleNOP.hook\");\n" +
			    "                        base.hook();\n" +
			    "                        //tsuper.hook();        // compiler: cannot resolve symbol <- okay\n" +
			    "                        super.hook();\n" +
			    "                }\n" +
			    "*/\n" +
			    "                void hook() <- replace void nop();\n" +
			    "                //void hook() <- replace void nop(int i) with {}\n" +
			    "                void nop() <- replace void nop(int i); \n" +
			    "        }\n" +
			    "		 @Override\n" +
			    "        public class Role2 {\n" +
			    "				 @Override\n" +
			    "                callin void hook () {\n" +
			    "                        System.out.println(\"SubSub.Role2.hook()\");\n" +
			    "                        base.hook();\n" +
			    "                        //tsuper.hook();\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n"
            },
            "SubSub.Role2.hook()\n" + 
    		"B1.getInt() called!\n" + 
    		"3\n" + 
    		"CallinSub.Role2.check(1)\n" + 
    		"B1.setInt(1) called!\n" + 
    		"CallinSuper.Role.check(1)\n" + 
    		"B1.setInt(1) called!\n" + 
    		"--------------------------------------\n" + 
    		"SubSub.Role2.hook()\n" + 
    		"B1.nop() called!\n" + 
    		"--------------------------------------\n" + 
    		"SubSubTeam.RoleNOP.nop() called!\n" + 
    		"B1.nop(int) called!\n" + 
    		"--------------------------------------\n" + 
    		"SubSubTeam.RoleX.format: GXeXsXcXhXeXnXkX\n" + 
    		"B2.print: von SubSubTeam.RoleX\n" + 
    		"SubSubTeam.Role3.format: Geschenk\n" + 
    		"B2.print: von SubSubTeam.Role3\n" + 
    		"CallinSub.Role3.format: G e s c h e n k \n" + 
    		"B2.print: von CallinSub.Role3\n" + 
    		"CallinSuper.Role3.format: GESCHENK\n" + 
    		"B2.print: von CallinSuper.Role3\n" + 
    		"-------------------- Test for Base-Subclassing ---------------\n" + 
    		"Person.goToWork()\n" + 
    		"---- just one sub base (Man) is effected by a callin ------\n" + 
    		"---- defined in a sub role (BWLStudent):-----------------------\n" + 
    		"She (Yukiko) gose to work.\n" + 
    		"Stundent.enterClassroom()\n" + 
    		"He (Tanaka) gose to work.\n" + 
    		"----- callins are effective even for inherited base methods in sub bases: ------\n" + 
    		"Person.haveFun()\n" + 
    		"Stundent.enterClassroom()\n" + 
    		"Person.haveFun()\n" + 
    		"----- callins are inherited to neither redefinded nor rebound base methods: --\n" + 
    		"Stundent.doHomework()\n" + 
    		"Person.performWorkstep()\n" + 
    		"Stundent.doHomework()\n" + 
    		"Person.performWorkstep()");
    }

    // inherit a role method binding declared in the the super-super role of the super-super team
    // X.1.1-otjld-binding-inheritance-2
    public void testX11_bindingInheritance2() {
       
       runConformTest(
            new String[] {
		"test1/MainX112.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MainX112 {\n" +
			    "        public static void main (String[] args) {\n" +
			    "		MyBaseX112 b = new MyBaseX112();\n" +
			    "		MySubTeamX112 mst = new MySubTeamX112();\n" +
			    "		mst.activate();\n" +
			    "		b.bm1();\n" +
			    "		b.bm2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"test1/MyBaseX112.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MyBaseX112 {\n" +
			    "        public void bm1() {\n" +
			    "	        System.out.println(\"MyBase.bm1()\");\n" +
			    "	}\n" +
			    "	public void bm2() {\n" +
			    "	        System.out.println(\"MyBase.bm2()\");\n" +
			    "	}        \n" +
			    "}\n" +
			    "	\n",
		"test1/MySuperTeamX112.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MySuperTeamX112 {\n" +
			    "        public class MyRole1X112 playedBy MyBaseX112 {\n" +
			    "		callin void rm() {\n" +
			    "			System.out.println(\"MySuperTeam.MyRole1.rm()\");\n" +
			    "			base.rm();\n" +
			    "		}\n" +
			    "		rm <- replace bm1;                \n" +
			    "	}	\n" +
			    "}\n" +
			    "	\n",
		"test1/MyTeamX112.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MyTeamX112 extends MySuperTeamX112 {\n" +
			    "}\n" +
			    "	\n",
		"test1/MySubTeamX112.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MySubTeamX112 extends MyTeamX112 {\n" +
			    "        public class MyRole2X112 extends MyRole1X112 {\n" +
			    "	}\n" +
			    "        \n" +
			    "        public class MyRole3X112 extends MyRole2X112 {\n" +
			    "		rm <- replace bm2;\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n"
            },
            "MySuperTeam.MyRole1.rm()\n" +
			"MyBase.bm1()\n" +
			"MySuperTeam.MyRole1.rm()\n" +
			"MyBase.bm2()");
    }

    // redefine a role method in a sub role in a sub team
    // X.1.1-otjld-binding-inheritance-3
    public void testX11_bindingInheritance3() {
       
       runConformTest(
            new String[] {
		"test1/MainX113.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MainX113 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX113 b = new MyBaseX113();\n" +
			    "        MySubTeamX113 st = new MySubTeamX113();\n" +
			    "        st.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}	\n",
		"test1/MyBaseX113.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MyBaseX113 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test1/MyTeamX113.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MyTeamX113 {\n" +
			    "    public class MyRoleX113 playedBy MyBaseX113 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test1/MySubTeamX113.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MySubTeamX113 extends MyTeamX113 {\n" +
			    "  //public class MyRoleX113 {}\n" +
			    "  public class MySubRoleX113 extends MyRoleX113 {\n" +
			    "    public void rm() {\n" +
			    "      System.out.println(\"MySubTeam.MySubRole.rm()\");\n" +
			    "    }\n" +
			    "  }\n" +
			    "\n" +
			    "}\n" +
			    "	\n"
            },
            "MyBase.bm()\n" +
       		"MySubTeam.MySubRole.rm()");
    }

    // redefine a role method in a sub role in a sub team (start compile at Main)
    // X.1.1-otjld-binding-inheritance-3a
    public void testX11_bindingInheritance3a() {
       
       runConformTest(
            new String[] {
		"test1/MainX113a.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MainX113a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX113a b = new MyBaseX113a();\n" +
			    "        MySubTeamX113a st = new MySubTeamX113a();\n" +
			    "        st.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}	\n",
		"test1/MySubTeamX113a.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MySubTeamX113a extends MyTeamX113a {\n" +
			    "  //public class MyRoleX113a {}\n" +
			    "  public class MySubRoleX113a extends MyRoleX113a {\n" +
			    "    public void rm() {\n" +
			    "      System.out.println(\"MySubTeam.MySubRole.rm()\");\n" +
			    "    }\n" +
			    "  }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test1/MyTeamX113a.java",
			    "\n" +
			    "package test1;\n" +
			    "public team class MyTeamX113a {\n" +
			    "    public class MyRoleX113a playedBy MyBaseX113a {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test1/MyBaseX113a.java",
			    "\n" +
			    "package test1;\n" +
			    "public class MyBaseX113a {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyBase.bm()\n" +
       		"MySubTeam.MySubRole.rm()");
    }


    // redefine a role method in a sub role in a sub team (default package)
    // X.1.1-otjld-binding-inheritance-3b
    public void testX11_bindingInheritance3b() {
       
       runConformTest(
            new String[] {
		"MainX113b.java",
			    "\n" +
			    "public class MainX113b {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX113b b = new MyBaseX113b();\n" +
			    "        MySubTeamX113b st = new MySubTeamX113b();\n" +
			    "        st.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}	\n",
		"MyBaseX113b.java",
			    "\n" +
			    "public class MyBaseX113b {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"MyTeamX113b.java",
			    "\n" +
			    "public team class MyTeamX113b {\n" +
			    "    public class MyRoleX113b playedBy MyBaseX113b {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"MySubTeamX113b.java",
			    "\n" +
			    "public team class MySubTeamX113b extends MyTeamX113b {\n" +
			    "  //public class MyRoleX113b {}\n" +
			    "  public class MySubRoleX113b extends MyRoleX113b {\n" +
			    "    public void rm() {\n" +
			    "      System.out.println(\"MySubTeam.MySubRole.rm()\");\n" +
			    "    }\n" +
			    "  }\n" +
			    "\n" +
			    "}\n" +
			    "	\n"
            },
            "MyBase.bm()\n" +
            "MySubTeam.MySubRole.rm()");
    }

    // super call in base method - super-super method is adapted too
    // X.1.1-otjld-binding-inheritance-4
    public void testX11_bindingInheritance4() {
       
       runConformTest(
            new String[] {
		"MainX114.java",
			    "\n" +
			    "public class MainX114 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX114 b = new MyBaseX114();\n" +
			    "        MyTeamX114 t = new MyTeamX114();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "        System.out.println(\"----------------\");\n" +
			    "        MySubSubBaseX114 ssb = new MySubSubBaseX114();\n" +
			    "        ssb.bm();\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX114.java",
			    "\n" +
			    "public class MyBaseX114 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX114.java",
			    "\n" +
			    "public class MySubBaseX114 extends MyBaseX114 {}\n" +
			    "    \n",
		"MySubSubBaseX114.java",
			    "\n" +
			    "public class MySubSubBaseX114 extends MySubBaseX114 {\n" +
			    "    public void bm() {\n" +
			    "        super.bm();\n" +
			    "        System.out.println(\"MySubSubBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX114.java",
			    "\n" +
			    "public team class MyTeamX114 {\n" +
			    "    public class MyRoleX114 playedBy MyBaseX114 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class MySubRoleX114 extends MyRoleX114 playedBy MySubSubBaseX114 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MySubRole.rm()\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n"
            },
            "MyBase.bm()\n" +
			"MyTeam.MyRole.rm()\n" +
			"----------------\n" +
			"MyBase.bm()\n" +
			"MySubSubBase.bm()\n" +
			"MyTeam.MySubRole.rm()");
    }

    // super call in base method - only super-super method is adapted
    // X.1.1-otjld-binding-inheritance-4a
    public void testX11_bindingInheritance4a() {
       
       runConformTest(
            new String[] {
		"MainX114a.java",
			    "\n" +
			    "public class MainX114a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX114a b = new MyBaseX114a();\n" +
			    "        MyTeamX114a t = new MyTeamX114a();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "        System.out.println(\"----------------\");\n" +
			    "        MySubSubBaseX114a ssb = new MySubSubBaseX114a();\n" +
			    "        ssb.bm();\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX114a.java",
			    "\n" +
			    "public class MyBaseX114a {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX114a.java",
			    "\n" +
			    "public class MySubBaseX114a extends MyBaseX114a {}\n" +
			    "    \n",
		"MySubSubBaseX114a.java",
			    "\n" +
			    "public class MySubSubBaseX114a extends MySubBaseX114a {\n" +
			    "    public void bm() {\n" +
			    "        super.bm();\n" +
			    "        System.out.println(\"MySubSubBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX114a.java",
			    "\n" +
			    "public team class MyTeamX114a {\n" +
			    "    public class MyRoleX114a playedBy MyBaseX114a {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyBase.bm()\n" +
			"MyTeam.MyRole.rm()\n" +
			"----------------\n" +
			"MyBase.bm()\n" +
			"MySubSubBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // super call in base method - only super-super method is adapted, complete explicit super-chain , replace binding
    // X.1.1-otjld-binding-inheritance-4b
    public void testX11_bindingInheritance4b() {
       
       runConformTest(
            new String[] {
		"MainX114b.java",
			    "\n" +
			    "public class MainX114b {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX114b b = new MyBaseX114b();\n" +
			    "        MyTeamX114b t = new MyTeamX114b();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "        System.out.println(\"----------------\");\n" +
			    "        MySubSubBaseX114b ssb = new MySubSubBaseX114b();\n" +
			    "        ssb.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyBaseX114b.java",
			    "\n" +
			    "public class MyBaseX114b {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX114b.java",
			    "\n" +
			    "public class MySubBaseX114b extends MyBaseX114b {\n" +
			    "    public void bm() {\n" +
			    "        super.bm();\n" +
			    "        System.out.println(\"MySubBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubSubBaseX114b.java",
			    "\n" +
			    "public class MySubSubBaseX114b extends MySubBaseX114b {\n" +
			    "    public void bm() {\n" +
			    "        super.bm();\n" +
			    "        System.out.println(\"MySubSubBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX114b.java",
			    "\n" +
			    "public team class MyTeamX114b {\n" +
			    "    public class MyRoleX114b playedBy MyBaseX114b {\n" +
			    "        callin void rm() {\n" +
			    "            base.rm();\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyBase.bm()\n" +
			"MyTeam.MyRole.rm()\n" +
			"----------------\n" +
			"MyBase.bm()\n" +
			"MySubBase.bm()\n" +
			"MySubSubBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // wicked super call interferes with callin binding
    // X.1.1-otjld-binding-inheritance-4c
    public void testX11_bindingInheritance4c() {
       
       runConformTest(
            new String[] {
		"TeamX114c.java",
			    "\n" +
			    "public team class TeamX114c {\n" +
			    "    protected class R playedBy MyBaseX114c {\n" +
			    "        callin void rm() { base.rm(); }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        // not even instantiating the team!\n" +
			    "        new MySubBaseX114c().bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyBaseX114c.java",
			    "\n" +
			    "public class MyBaseX114c {\n" +
			    "    void bm() { System.out.print(\"MyBase.bm\"); }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX114c.java",
			    "\n" +
			    "public class MySubBaseX114c extends MyBaseX114c {\n" +
			    "    void bm2() {\n" +
			    "        super.bm();\n" +
			    "    }\n" +
			    "    void bm() {\n" +
			    "        System.out.println(\"MySubBase.bm\");\n" +
			    "        bm2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MySubBase.bm\n" +
            "MyBase.bm");
    }

    // super call in base method - like 4a but with relevant signature
    public void testX11_bindingInheritance4d() {
       
       runConformTest(
            new String[] {
		"MainX114d.java",
			    "\n" +
			    "public class MainX114d {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX114d b = new MyBaseX114d();\n" +
			    "        MyTeamX114d t = new MyTeamX114d();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(b.bm(\"OK\"));\n" +
			    "        System.out.println(\"----------------\");\n" +
			    "        MySubSubBaseX114d ssb = new MySubSubBaseX114d();\n" +
			    "        System.out.println(ssb.bm(\"Hoki\"));\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX114d.java",
			    "\n" +
			    "public class MyBaseX114d {\n" +
			    "    public String bm(String in) {\n" +
			    "        System.out.println(\"MyBase.bm(\"+in+\")\");\n" +
			    "        return \"retBase\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX114d.java",
			    "\n" +
			    "public class MySubBaseX114d extends MyBaseX114d {}\n" +
			    "    \n",
		"MySubSubBaseX114d.java",
			    "\n" +
			    "public class MySubSubBaseX114d extends MySubBaseX114d {\n" +
			    "    public String bm(String in) {\n" +
			    "        String res = super.bm(in);\n" +
			    "        System.out.println(\"MySubSubBase.bm(\"+in+\")\");\n" +
			    "        return res;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX114d.java",
			    "\n" +
			    "public team class MyTeamX114d {\n" +
			    "    public class MyRoleX114d playedBy MyBaseX114d {\n" +
			    "        public void rm(String in) {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm(\"+in+\")\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyBase.bm(OK)\n" +
			"MyTeam.MyRole.rm(OK)\n" +
			"retBase\n" +
			"----------------\n" +
			"MyBase.bm(Hoki)\n" +
			"MySubSubBase.bm(Hoki)\n" +
			"MyTeam.MyRole.rm(Hoki)\n" +
			"retBase");
    }

	// wicked super call interferes with callin binding
	// Bug 468712: [otdre] stack overflow at callin-bound base method involving wicked super call
	public void testX11_bindingInheritance4e() {

		runConformTest(
				new String[] {
		"TeamX114e.java",
				"\n" +
				"public team class TeamX114e {\n" +
				"    protected class R playedBy MyBaseX114e {\n" +
				"        callin int rm() {\n" +
				"            System.out.println(\"> R.rm\");\n" +
				"            int res = base.rm();\n" +
				"            System.out.println(\"< R.rm\");\n" +
				"            return res+1;\n" +
				"        }\n" +
				"        rm <- replace bmA, bmB;\n" +
				"    }\n" +
				"    public static void main(String[] args) {\n" +
				"        new TeamX114e().activate();\n" +
				"        MySub2BaseX114e b = new MySub2BaseX114e();\n" +
				"        int res = b.bmA();\n" +
				"        res = b.bmB(res);\n" +
				"        System.out.println(res);\n" +
				"    }\n" +
				"}\n" +
				"    \n",
		"MyBaseX114e.java",
				"\n" +
				"public class MyBaseX114e {\n" +
				"    protected int bmA() { System.out.println(\"MyBase.bmA\"); return 1; }\n" +
				"    protected int bmB(int in) { System.out.println(\"MyBase.bmB\"); return in+2; }\n" + // 2 methods to challenge reuse of existing ReplaceWickedSuperCallsAdapter (OTDRE)
				"}\n" +
				"    \n",
		"MySubBaseX114e.java",
				"\n" +
				"public class MySubBaseX114e extends MyBaseX114e {\n" +
				"    protected int bmA2() {\n" +
				"        System.out.println(\"MySubBase.bmA2\");\n" +
				"        return super.bmA() + 4;\n" +
				"    }\n" +
				"    protected int bmA() {\n" +
				"        System.out.println(\"MySubBase.bmA\");\n" +
				"        return bmA2() + 8;\n" +
				"    }\n" +
				"    protected int bmB2(int in) {\n" +
				"        System.out.println(\"MySubBase.bmB2\");\n" +
				"        return super.bmB(in+16);\n" + // invoke is last before return
				"    }\n" +
				"    protected int bmB(int in) {\n" +
				"        System.out.println(\"MySubBase.bmB\");\n" +
				"        return bmB2(in+32) + 64;\n" +
				"    }\n" +
				"}\n" +
				"    \n",
		"MySub2BaseX114e.java",
				"\n" +
				"public class MySub2BaseX114e extends MySubBaseX114e {\n" +
				"    protected int bmA() {\n" +
				"        System.out.println(\"MySub2Base.bmA\");\n" +
				"        return super.bmA() + 128;\n" +
				"    }\n" +
				"    protected int bmB(int in) {\n" +
				"        System.out.println(\"MySub2Base.bmB\");\n" +
				"        return super.bmB(in+256) + 512;\n" +
				"    }\n" +
				"}\n" +
				"    \n"
			},
			"> R.rm\n" +
			"MySub2Base.bmA\n" +
			"MySubBase.bmA\n" +
			"MySubBase.bmA2\n" +
			"MyBase.bmA\n" +
			"< R.rm\n" +
			"> R.rm\n" +
			"MySub2Base.bmB\n" +
			"MySubBase.bmB\n" +
			"MySubBase.bmB2\n" +
			"MyBase.bmB\n" +
			"< R.rm\n" +
			"1025");
	}

    // after callin inherited from super role, before callin to the same base method added
    // X.1.1-otjld-binding-inheritance-5
    public void testX11_bindingInheritance5() {
       
       runConformTest(
            new String[] {
		"MainX115.java",
			    "\n" +
			    "public class MainX115 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX115 b = new MyBaseX115();\n" +
			    "        MySubTeamX115 t = new MySubTeamX115();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX115.java",
			    "\n" +
			    "public class MyBaseX115 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX115.java",
			    "\n" +
			    "public team class MyTeamX115 {\n" +
			    "    public class MyRoleX115 playedBy MyBaseX115 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubTeamX115.java",
			    "\n" +
			    "public team class MySubTeamX115 extends MyTeamX115 {\n" +
			    "    public class MyRoleX115 {\n" +
			    "        rm <- before bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // after callin inherited from super base, before callin added to the same (inherited) base method in sub base
    // X.1.1-otjld-binding-inheritance-6
    public void testX11_bindingInheritance6() {
       
       runConformTest(
            new String[] {
		"MainX116.java",
			    "\n" +
			    "public class MainX116 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MySubBaseX116 sb = new MySubBaseX116();\n" +
			    "        MyTeamX116 t = new MyTeamX116();\n" +
			    "        t.activate();\n" +
			    "        sb.bm();\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX116.java",
			    "\n" +
			    "public class MyBaseX116 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MySubBaseX116.java",
			    "\n" +
			    "public class MySubBaseX116 extends MyBaseX116 {}\n" +
			    "    \n",
		"MyTeamX116.java",
			    "\n" +
			    "public team class MyTeamX116 {\n" +
			    "    public class MyRoleX116 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class MyRoleAX116 extends MyRoleX116 playedBy MyBaseX116 {\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "    public class MyRoleBX116 extends MyRoleX116 playedBy MySubBaseX116 {\n" +
			    "        rm <- before bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // callin inherited from a super base, another callin to a different team is added to the same base method in the sub base
    // X.1.1-otjld-binding-inheritance-7
    public void testX11_bindingInheritance7() {
       
       runConformTest(
            new String[] {
		"MainX117.java",
			    "\n" +
			    "public class MainX117 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "            MyTeamXX117 tX = new MyTeamXX117();\n" +
			    "            tX.activate();\n" +
			    "            MyTeamYX117 tY = new MyTeamYX117();\n" +
			    "            tY.activate();\n" +
			    "            MyBaseAX117 myClassA = new MyBaseAX117();\n" +
			    "            myClassA.method1();\n" +
			    "            MyBaseBX117 myClassB = new MyBaseBX117();\n" +
			    "            myClassB.method1();\n" +
			    "\n" +
			    "            MyBaseAX117 myClassC = new MyBaseCX117();\n" +
			    "            myClassC.method1();        \n" +
			    "\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseAX117.java",
			    "\n" +
			    "public class MyBaseAX117 {\n" +
			    "    public void method1()\n" +
			    "    {\n" +
			    "        System.out.println(\"MyBaseA.method1()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyBaseBX117.java",
			    "\n" +
			    "public class MyBaseBX117 extends MyBaseAX117 {\n" +
			    "    public void method1()\n" +
			    "    {\n" +
			    "        System.out.println(\"MyBaseB.method1()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyBaseCX117.java",
			    "\n" +
			    "public class MyBaseCX117 extends MyBaseBX117 {\n" +
			    "    public void method1()\n" +
			    "    {\n" +
			    "        System.out.println(\"MyBaseC.method1()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamXX117.java",
			    "\n" +
			    "public team class MyTeamXX117 {\n" +
			    "    public class MyRole playedBy MyBaseAX117 \n" +
			    "    {\n" +
			    "        public void m1()\n" +
			    "        {\n" +
			    "            System.out.println(\"MyTeamX.MyRole.m1()\");\n" +
			    "        }\n" +
			    "\n" +
			    "        m1 <- after method1;\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n",
		"MyTeamYX117.java",
			    "\n" +
			    "public team class MyTeamYX117 {\n" +
			    "    public class MyRole playedBy MyBaseBX117 \n" +
			    "    {\n" +
			    "        public void m1()\n" +
			    "        {\n" +
			    "            System.out.println(\"MyTeamY.MyRole.m1()\");\n" +
			    "        }\n" +
			    "\n" +
			    "        m1 <- after method1;\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n"
            },
            "MyBaseA.method1()\n" +
			"MyTeamX.MyRole.m1()\n" +
			"MyBaseB.method1()\n" +
			"MyTeamX.MyRole.m1()\n" +
			"MyTeamY.MyRole.m1()\n" +
			"MyBaseC.method1()\n" +
			"MyTeamX.MyRole.m1()\n" +
			"MyTeamY.MyRole.m1()");
    }

    // overridden callin method with non-void return type calls 'tsuper'
    // X.1.1-otjld-binding-inheritance-8
    public void testX11_bindingInheritance8() {
       
       runConformTest(
            new String[] {
		"MainX118.java",
			    "\n" +
			    "public class MainX118 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "            MySubTeamX118 t = new MySubTeamX118();\n" +
			    "            t.activate();\n" +
			    "            MyBaseX118 b = new MyBaseX118();\n" +
			    "            System.out.print(b.bm());\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX118.java",
			    "\n" +
			    "public class MyBaseX118 {\n" +
			    "    public String bm()\n" +
			    "    {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX118.java",
			    "\n" +
			    "public team class MyTeamX118 {\n" +
			    "    public class MyRole playedBy MyBaseX118 \n" +
			    "    {\n" +
			    "        callin String rm()\n" +
			    "        {\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "\n" +
			    "        rm <- replace bm;\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n",
		"MySubTeamX118.java",
			    "\n" +
			    "public team class MySubTeamX118 extends MyTeamX118 {\n" +
			    "    public class MyRole \n" +
			    "    {\n" +
			    "        callin String rm()\n" +
			    "        {\n" +
			    "            return \"O\" + tsuper.rm();\n" +
			    "        }\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // overridden callin method with non-void basic return type calls 'tsuper'
    // X.1.1-otjld-binding-inheritance-8a
    public void testX11_bindingInheritance8a() {
       
       runConformTest(
            new String[] {
		"MainX118a.java",
			    "\n" +
			    "public class MainX118a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "            MySubTeamX118a t = new MySubTeamX118a();\n" +
			    "            t.activate();\n" +
			    "            MyBaseX118a b = new MyBaseX118a();\n" +
			    "            System.out.print(b.bm());\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"MyBaseX118a.java",
			    "\n" +
			    "public class MyBaseX118a {\n" +
			    "    public int bm()\n" +
			    "    {\n" +
			    "        return 2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyTeamX118a.java",
			    "\n" +
			    "public team class MyTeamX118a {\n" +
			    "    public class MyRole playedBy MyBaseX118a \n" +
			    "    {\n" +
			    "        callin int rm()\n" +
			    "        {\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "\n" +
			    "        rm <- replace bm;\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n",
		"MySubTeamX118a.java",
			    "\n" +
			    "public team class MySubTeamX118a extends MyTeamX118a {\n" +
			    "    public class MyRole \n" +
			    "    {\n" +
			    "        callin int rm()\n" +
			    "        {\n" +
			    "            return 40 + tsuper.rm();\n" +
			    "        }\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n"
            },
            "42");
    }

    // a base class which is a role at the same time inherits a binding form its implicit super (role) class
    // X.1.1-otjld-binding-inheritance-9
    public void testX11_bindingInheritance9() {
       
       runConformTest(
            new String[] {
		"TX11bi9Main.java",
			    "\n" +
			    "        public class TX11bi9Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TX11bi9 b= new TX11bi9();\n" +
			    "                TeamX11bi9 pt = new TeamX11bi9();\n" +
			    "                TeamX11bi9_3 ot = new TeamX11bi9_3(pt);\n" +
			    "                pt.activate();\n" +
			    "                ot.activate();\n" +
			    "                b.foo();\n" +
			    "        \n" +
			    "                //cleaning up a little.\n" +
			    "                pt.deactivate();\n" +
			    "                ot.deactivate();\n" +
			    "        \n" +
			    "                //Now the same thing again - with a Subteam.\n" +
			    "                TX11bi9 b2= new TX11bi9();\n" +
			    "                TeamX11bi9_2 pt2 = new TeamX11bi9_2(); //TeamX11bi9_2 \"is a\" TeamX11bi9\n" +
			    "                TeamX11bi9_3 ot2 = new TeamX11bi9_3(pt2);\n" +
			    "                pt2.activate();\n" +
			    "                ot2.activate();\n" +
			    "                b2.foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX11bi9.java",
			    "\n" +
			    "        public class TX11bi9 {\n" +
			    "            public void foo(){\n" +
			    "                System.out.println(\"bar\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi9.java",
			    "\n" +
			    "        public team class TeamX11bi9 {  \n" +
			    "            public class RX11bi9 playedBy TX11bi9 { \n" +
			    "                public void bar(){\n" +
			    "                    System.out.println(\"Aspect woven\");\n" +
			    "                    //nothing here, except a JoinPoint\n" +
			    "                }   \n" +
			    "        \n" +
			    "                bar <- before foo;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi9_2.java",
			    "\n" +
			    "        public team class TeamX11bi9_2 extends TeamX11bi9 {  \n" +
			    "            public class RX11bi9_2 extends RX11bi9 { \n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi9_3.java",
			    "\n" +
			    "        public team class TeamX11bi9_3 {  \n" +
			    "            final TeamX11bi9 otherTeam;\n" +
			    "            \n" +
			    "            public TeamX11bi9_3(TeamX11bi9 other){\n" +
			    "                otherTeam = other;\n" +
			    "            }\n" +
			    "\n" +
			    "            protected class RX11bi9_3 playedBy RX11bi9<@otherTeam> {\n" +
			    "                public void foobar() {\n" +
			    "                    System.out.println(\"Aspect on Aspect woven\");\n" +
			    "                }\n" +
			    "                foobar <- before bar;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "Aspect on Aspect woven\n" +
			"Aspect woven\n" +
			"bar\n" +
			"Aspect on Aspect woven\n" +
			"Aspect woven\n" +
			"bar");
    }

    // a base class which is a role at the same time inherits a binding form its implicit super-super (role) class
    // X.1.1-otjld-binding-inheritance-10
    public void testX11_bindingInheritance10() {
       
       runConformTest(
            new String[] {
		"TX11bi10Main.java",
			    "\n" +
			    "        public class TX11bi10Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TX11bi10 b= new TX11bi10();\n" +
			    "                TeamX11bi10 pt = new TeamX11bi10();\n" +
			    "                TeamX11bi10_3 ot = new TeamX11bi10_3(pt);\n" +
			    "                pt.activate();\n" +
			    "                ot.activate();\n" +
			    "                b.foo();\n" +
			    "        \n" +
			    "                //cleaning up a little.\n" +
			    "                pt.deactivate();\n" +
			    "                ot.deactivate();\n" +
			    "        \n" +
			    "                //Now the same thing again - with a Subteam.\n" +
			    "                TX11bi10 b2= new TX11bi10();\n" +
			    "                TeamX11bi10_2 pt2 = new TeamX11bi10_2(); //TeamX11bi10_2 \"is a\" TeamX11bi10\n" +
			    "                TeamX11bi10_3 ot2 = new TeamX11bi10_3(pt2);\n" +
			    "                pt2.activate();\n" +
			    "                ot2.activate();\n" +
			    "                b2.foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX11bi10.java",
			    "\n" +
			    "        public class TX11bi10 {\n" +
			    "            public void foo(){\n" +
			    "                System.out.println(\"bar\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi10.java",
			    "\n" +
			    "        public team class TeamX11bi10 {  \n" +
			    "            public class RX11bi10 playedBy TX11bi10 { \n" +
			    "                public void bar(){\n" +
			    "                    System.out.println(\"Aspect woven\");\n" +
			    "                    //nothing here, except a JoinPoint\n" +
			    "                }   \n" +
			    "        \n" +
			    "                bar <- before foo;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi10_1.java",
			    "\n" +
			    "        public team class TeamX11bi10_1 extends TeamX11bi10 {  \n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi10_2.java",
			    "\n" +
			    "        public team class TeamX11bi10_2 extends TeamX11bi10_1 {  \n" +
			    "            public class RX11bi10_2 extends RX11bi10 { \n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX11bi10_3.java",
			    "\n" +
			    "        public team class TeamX11bi10_3 {  \n" +
			    "            final TeamX11bi10 otherTeam;\n" +
			    "            \n" +
			    "            public TeamX11bi10_3(TeamX11bi10 other){\n" +
			    "                otherTeam = other;\n" +
			    "            }\n" +
			    "\n" +
			    "            protected class RX11bi10_3 playedBy RX11bi10<@otherTeam> {\n" +
			    "                public void foobar() {\n" +
			    "                    System.out.println(\"Aspect on Aspect woven\");\n" +
			    "                }\n" +
			    "                foobar <- before bar;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "Aspect on Aspect woven\n" +
			"Aspect woven\n" +
			"bar\n" +
			"Aspect on Aspect woven\n" +
			"Aspect woven\n" +
			"bar");
    }

    // A role inherits a replace binding, sub-base is bound to an unrelated role (reported by Marco Mosconi)
    // X.1.1-otjld-binding-inheritance-11
    public void testX11_bindingInheritance11() {
       
       runConformTest(
            new String[] {
		"TeamX11bi11.java",
			    "\n" +
			    "public team class TeamX11bi11 {\n" +
			    "  protected class R1 playedBy TX11bi11_1 {\n" +
			    "    callin void rm() {\n" +
			    "      System.out.print(\"R1<-\");\n" +
			    "      base.rm();\n" +
			    "    }\n" +
			    "    rm <- replace test;\n" +
			    "  }\n" +
			    "  protected class R2 extends R1 playedBy TX11bi11_2 {\n" +
			    "    callin void rm() {\n" +
			    "      System.out.print(\"R2<-\");\n" +
			    "      base.rm();\n" +
			    "    }\n" +
			    "  }\n" +
			    "  protected class R3 /*no extends*/ playedBy TX11bi11_3 {}\n" +
			    "  public static void main(String[] args) {\n" +
			    "    new TeamX11bi11().activate();\n" +
			    "    new TX11bi11_1().test();\n" +
			    "    new TX11bi11_2().test();\n" +
			    "    new TX11bi11_3().test();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TX11bi11_1.java",
			    "\n" +
			    "public class TX11bi11_1 {\n" +
			    "  void test() { \n" +
			    "    System.out.print(\"B1.\");\n" +
			    "  } \n" +
			    "}\n" +
			    "  \n",
		"TX11bi11_2.java",
			    "\n" +
			    "public class TX11bi11_2 extends TX11bi11_1 {\n" +
			    "  void test() { \n" +
			    "    System.out.print(\"B2.\");\n" +
			    "  } \n" +
			    "}\n" +
			    "  \n",
		"TX11bi11_3.java",
			    "\n" +
			    "public class TX11bi11_3  extends TX11bi11_2 {\n" +
			    "  void test() { \n" +
			    "    System.out.print(\"B3.\");\n" +
			    "  } \n" +
			    "}\n" +
			    "  \n"
            },
            "R1<-B1.R2<-B2.R2<-B3.");
    }

    // a super-call bypasses a callin binding
    // X.1.1-otjld-binding-inheritance-12
    public void testX11_bindingInheritance12() {
       
       runConformTest(
            new String[] {
		"TeamX11bi12.java",
			    "\n" +
			    "public team class TeamX11bi12 {\n" +
			    "  protected class R playedBy TX11bi12_2 {\n" +
			    "    void rm() {\n" +
			    "      System.out.print(\"WRONG\");\n" +
			    "    }\n" +
			    "    rm <- after m;\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    new TeamX11bi12().activate();\n" +
			    "    new TX11bi12_2().m2();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TX11bi12_1.java",
			    "\n" +
			    "public class TX11bi12_1 {\n" +
			    "  public void m() {\n" +
			    "    System.out.print(\"K\");\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TX11bi12_2.java",
			    "\n" +
			    "public class TX11bi12_2 extends TX11bi12_1 {\n" +
			    "  public void m2() {\n" +
			    "    System.out.print(\"O\");\n" +
			    "    super.m();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "OK");
    }

    // a super-call bypasses a callin binding - other team binds super version
    // X.1.1-otjld-binding-inheritance-13
    public void testX11_bindingInheritance13() {
       
       runConformTest(
            new String[] {
		"TeamX11bi13_2.java",
			    "\n" +
			    "public team class TeamX11bi13_2 {\n" +
			    "  protected class R playedBy TX11bi13_2 {\n" +
			    "    void rm() {\n" +
			    "      System.out.print(\"WRONG\");\n" +
			    "    }\n" +
			    "    rm <- after m;\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    Class<?> tc = TeamX11bi13_1.class;\n" +
			    "    new TeamX11bi13_2().activate();\n" +
			    "    new TX11bi13_2().m2();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TX11bi13_1.java",
			    "\n" +
			    "public class TX11bi13_1 {\n" +
			    "  public void m() {\n" +
			    "    System.out.print(\"K\");\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TX11bi13_2.java",
			    "\n" +
			    "public class TX11bi13_2 extends TX11bi13_1 {\n" +
			    "  public void m2() {\n" +
			    "    System.out.print(\"O\");\n" +
			    "    super.m();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"TeamX11bi13_1.java",
			    "\n" +
			    "public team class TeamX11bi13_1 {\n" +
			    "  protected class R1 playedBy TX11bi13_1 {\n" +
			    "    void snafu() {}\n" +
			    "    snafu <- after m;\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "OK");
    }

    // a callin to a private base method does not affect the unrelated version in a sub-base (reported by Oliver Frank)
    // X.1.1-otjld-binding-inheritance-14
    public void testX11_bindingInheritance14() {
       
       runConformTest(
            new String[] {
		"TeamX11bi14.java",
			    "\n" +
			    "public team class TeamX11bi14 {\n" +
			    "    protected class R playedBy X11bi14_1 {\n" +
			    "        void no() { System.out.print(\"NO\"); }\n" +
			    "        no <- before test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new TeamX11bi14().activate();\n" +
			    "        new X11bi14_2().callTest();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"X11bi14_1.java",
			    "\n" +
			    "public class X11bi14_1 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private void test() {}\n" +
			    "}\n" +
			    "    \n",
		"X11bi14_2.java",
			    "\n" +
			    "public class X11bi14_2 extends X11bi14_1 {\n" +
			    "    private void test() { System.out.print(\"OK\"); }\n" +
			    "    public void callTest() {\n" +
			    "        test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // modeled after a situation in PullUpAdaptor, that could cause StackOverflowError
    // (bug not reproduced, though, due to different loading sequence).
    public void testX11_bindingInheritance15() {
        
        runConformTest(
             new String[] {
 		"TeamX11bi15.java",
 				"import base b.X11bi15_1;\n" +
 			    "import base b.X11bi15_2;\n" +
 			    "public team class TeamX11bi15 {\n" +
 			    "    protected class R1 playedBy X11bi15_1 {\n" +
 			    "        void no() { System.out.print(\"NO\"); }\n" +
 			    "        no <- before test;\n" +
 			    "    }\n" +
 			    "    protected class R2 playedBy X11bi15_2 {\n" +
 			    "        callin void ask() {\n" +
 			    "			System.out.print(\"?\");\n" +
 			    "			base.ask();\n" +
 			    "		 }\n" +
 			    "        ask <- replace callTest;\n" +
 			    "    }\n" +
 			    "    public static void main(String[] args) {\n" +
 			    "        new TeamX11bi15().activate();\n" +
 			    "        new b.X11bi15_2().callTest();\n" +
 			    "    }\n" +
 			    "}\n" +
 			    "    \n",
 		"b/X11bi15_1.java",
 			    "package b;\n" +
 			    "public class X11bi15_1 {\n" +
 			    "    void test() { System.out.print('!'); }\n" +
 			    "}\n" +
 			    "    \n",
 		"b/X11bi15_2.java",
 			    "package b;\n" +
 			    "public class X11bi15_2 extends X11bi15_1 {\n" +
 			    "    public void callTest() {\n" +
 			    "        test();\n" +
 			    "    }\n" +
 			    "}\n" +
 			    "    \n"
             },
             "?NO!");
    }

    public void testBug480257() {
    	runConformTest(
    		new String[] {
    			"MT.java",
    			"public team class MT {\n" + 
    			"	public class R playedBy C1A {\n" + 
    			"		void bm() <- replace void bm();\n" + 
    			"		callin void bm() {\n" + 
    			"			System.out.print(\"R\");\n" + 
    			"			base.bm();\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"	public static void main(String[] args) {\n" + 
    			"		new MT().activate();\n" + 
    			"		new C1B().bm();\n" + 
    			"	}\n" + 
    			"}\n",
    			"C0.java",
    			"public class C0 {\n" + 
    			"	public void bm() {\n" + 
    			"		System.out.print(\"C0\");\n" + 
    			"	}\n" + 
    			"}\n",
    			"C1A.java",
    			"public class C1A extends C0 {\n" + 
    			"}\n",
    			"C1B.java",
    			"public class C1B extends C0 {\n" + 
    			"	@Override\n" + 
    			"	public void bm() {\n" + 
    			"		System.out.print(\"C1B-\");\n" + 
    			"		super.bm();\n" + // this super call was confused by the (unrelated) callin binding
    			"		System.out.print('!');\n" + 
    			"	}\n" + 
    			"}\n"
    		},
    		"C1B-C0!");
    }

    // base call requires a result lifting
    // X.1.2-otjld-result-lifting-1
    public void testX12_resultLifting1() {
       
       runConformTest(
            new String[] {
		"test2/X12rl1Main.java",
			    "\n" +
			    "package test2;\n" +
			    "public class X12rl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX121 b = new MyBaseX121();\n" +
			    "        MyTeamX121 t = new MyTeamX121();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "\n" +
			    "	\n",
		"test2/MyBaseX121.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX121 {\n" +
			    "    public MyBaseX121 bm() {\n" +
			    "        System.out.println(\"MyBaseX121.bm()\");\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "}\n" +
			    "\n" +
			    "	\n",
		"test2/MyTeamX121.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX121 {\n" +
			    "    public class MyRoleX121 playedBy MyBaseX121 {\n" +
			    "        callin MyRoleX121 rm() {\n" +
			    "            System.out.println(\"MyTeamX121.MyRoleX121.rm()\");\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeamX121.MyRoleX121.rm()\n" +
       		"MyBaseX121.bm()");
    }

    // base call requires a result lifting - 'replace' modifier missing
    // X.1.2-otjld-result-lifting-1a
    public void testX12_resultLifting1a() {
        runNegativeTest(
            new String[] {
		"test2/MyTeamX121a.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX121a {\n" +
			    "    public class MyRoleX121a playedBy MyBaseX121a {\n" +
			    "        callin MyRoleX121a rm() {\n" +
			    "            System.out.println(\"MyTeamX121a.MyRoleX121a.rm()\");\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "        rm <- bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBaseX121a.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX121a {\n" +
			    "    public MyBaseX121a bm() {\n" +
			    "        System.out.println(\"MyBaseX121a.bm()\");\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "}\n" +
			    "\n" +
			    "	\n"
            },
            "----------\n" + 
    		"1. ERROR in test2\\MyTeamX121a.java (at line 9)\n" + 
    		"	rm <- bm;\n" + 
    		"	^^\n" + 
    		"Callin modifier (before, after or replace) missing for callin-binding rm (OTJLD A.3.3).\n" + 
    		"----------\n");
    }

    // base call requires a result lifting - 'replace' modifier missing (different compiler invocation)
    // X.1.2-otjld-result-lifting-1b
    public void testX12_resultLifting1b() {
        runNegativeTest(
            new String[] {
		"test2/X12rl1bMain.java",
			    "\n" +
			    "package test2;\n" +
			    "public class X12rl1bMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX121b b = new MyBaseX121b();\n" +
			    "        MyTeamX121b t = new MyTeamX121b();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "\n" +
			    "        \n",
		"test2/MyTeamX121b.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX121b {\n" +
			    "    public class MyRoleX121b playedBy MyBaseX121b {\n" +
			    "        callin MyRoleX121b rm() {\n" +
			    "            System.out.println(\"MyTeamX121b.MyRoleX121b.rm()\");\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "        rm <- bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "        \n",
		"test2/MyBaseX121b.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX121b {\n" +
			    "    public MyBaseX121b bm() {\n" +
			    "        System.out.println(\"MyBaseX121b.bm()\");\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "}\n" +
			    "\n" +
			    "        \n"
            },
            "----------\n" + 
    		"1. ERROR in test2\\MyTeamX121b.java (at line 9)\n" + 
    		"	rm <- bm;\n" + 
    		"	^^\n" + 
    		"Callin modifier (before, after or replace) missing for callin-binding rm (OTJLD A.3.3).\n" + 
    		"----------\n");
    }

    // base call requires a result lifting
    // X.1.2-otjld-result-lifting-1c
    public void testX12_resultLifting1c() {
       
       runConformTest(
            new String[] {
		"test2/X12rl1cMain.java",
			    "\n" +
			    "package test2;\n" +
			    "public class X12rl1cMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX121 b = new MyBaseX121();\n" +
			    "        MyTeamX121 t = new MyTeamX121();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "\n" +
			    "	\n",
		"test2/MyBaseX121.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX121 {\n" +
			    "    public MyBaseX121 bm() {\n" +
			    "        System.out.println(\"MyBaseX121.bm()\");\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "}\n" +
			    "\n" +
			    "	\n",
		"test2/MyTeamX121.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX121 {\n" +
			    "    public class MyRoleX121 playedBy MyBaseX121 {\n" +
			    "        callin MyRoleX121 rm() {\n" +
			    "            System.out.println(\"MyTeamX121.MyRoleX121.rm()\");\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeamX121.MyRoleX121.rm()\n" +
       		"MyBaseX121.bm()");
    }

    // base call requires a result lifting - team hierarchy -- wrong base call
    // X.1.2-otjld-result-lifting-2a
    public void testX12_resultLifting2a() {
        runNegativeTest(
            new String[] {
		"test2/MySuperTeamX122a.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySuperTeamX122a {\n" +
			    "	public class MyRole1X122a /*playedBy MyBase1X122a*/ {\n" +
			    "        callin MyRole2X122a rm() {\n" +
			    "            System.out.println(\"MySuperTeamX122a.MyRole1X122a.rm()\");\n" +
			    "            return base.m1();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class MyRole2X122a {}\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBase2X122a.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBase2X122a {\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBase1X122a.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBase1X122a {\n" +
			    "	public MyBase2X122a bm() {\n" +
			    "        System.out.println(\"MyBase1X122a.bm()\");\n" +
			    "        return new MyBase2X122a();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "----------\n" + 
    		"1. ERROR in test2\\MySuperTeamX122a.java (at line 7)\n" + 
    		"	return base.m1();\n" + 
    		"	       ^^^^^^^^^\n" + 
    		"Illegal method in base call, can only call base version of the enclosing method rm() (OTJLD 4.3(a)).\n" + 
    		"----------\n");
    }

    // base call requires a result lifting - team hierarchy
    // X.1.2-otjld-result-lifting-2
    public void testX12_resultLifting2() {
       
       runConformTest(
            new String[] {
		"test2/MainX122.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MainX122 {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		MyBase1X122 b = new MyBase1X122();\n" +
			    "		MyTeamX122 t = new MyTeamX122();\n" +
			    "		t.activate();\n" +
			    "		b.bm();\n" +
			    "	}\n" +
			    "}\n" +
			    "\n" +
			    "	\n",
		"test2/MyBase2X122.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBase2X122 {\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBase1X122.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBase1X122 {\n" +
			    "	public MyBase2X122 bm() {\n" +
			    "        System.out.println(\"MyBase1X122.bm()\");\n" +
			    "        return new MyBase2X122();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MySuperTeamX122.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySuperTeamX122 {\n" +
			    "	public class MyRole1X122 /*playedBy MyBase1X122*/ {\n" +
			    "        callin MyRole2X122 rm() {\n" +
			    "            System.out.println(\"MySuperTeamX122.MyRole1X122.rm()\");\n" +
			    "            return base.rm();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class MyRole2X122 {}\n" +
			    "}\n" +
			    "	\n",
		"test2/MyTeamX122.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX122 extends MySuperTeamX122 {\n" +
			    "	@Override\n" +
			    "	public class MyRole1X122 playedBy MyBase1X122 {\n" +
			    "		rm <- replace bm;\n" +
			    "	}\n" +
			    "	@Override\n" +
			    "	public class MyRole2X122 playedBy MyBase2X122 {}\n" +
			    "}\n" +
			    "	\n"
            },
            "MySuperTeamX122.MyRole1X122.rm()\n" +
            "MyBase1X122.bm()");
    }

    // team hierarchy - creating empty subsub team with empty superteam
    // X.1.2-otjld-result-lifting-3
    public void testX12_resultLifting3() {
       
       runConformTest(
            new String[] {
		"test2/MainX123.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MainX123 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX123 b = new MyBaseX123();\n" +
			    "        MySubTeamX123 t = new MySubTeamX123();\n" +
			    "		t.go(b);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBaseX123.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX123 {\n" +
			    "	public void bm() {\n" +
			    "        System.out.println(\"MyBaseX123.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MySuperTeamX123.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySuperTeamX123 {}\n" +
			    "	\n",
		"test2/MyTeamX123.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX123 extends MySuperTeamX123 {\n" +
			    "        @org.objectteams.ImplicitTeamActivation\n" +
			    "	public void go(MyBaseX123 b) {\n" +
			    "		b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class MyRoleX123 playedBy MyBaseX123 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeamX123.MyRoleX123.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MySubTeamX123.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySubTeamX123 extends MyTeamX123 {}\n" +
			    "	\n"
            },
            "MyBaseX123.bm()\n" +
            "MyTeamX123.MyRoleX123.rm()");
    }

    // team hierarchy - creating empty subsub team with empty superteam (different compiler invocation)
    // X.1.2-otjld-result-lifting-3a
    public void testX12_resultLifting3a() {
       
       runConformTest(
            new String[] {
		"test2/MainX123a.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MainX123a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        	MyBaseX123a b = new MyBaseX123a();\n" +
			    "        	MySubTeamX123a t = new MySubTeamX123a();\n" +
			    "		t.go(b);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"test2/MySuperTeamX123a.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySuperTeamX123a {}\n" +
			    "	\n",
		"test2/MyTeamX123a.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX123a extends MySuperTeamX123a {\n" +
			    "        @org.objectteams.ImplicitTeamActivation\n" +
			    "	public void go(MyBaseX123a b) {\n" +
			    "		b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class MyRoleX123a playedBy MyBaseX123a {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeamX123a.MyRoleX123a.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBaseX123a.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX123a {\n" +
			    "	public void bm() {\n" +
			    "        System.out.println(\"MyBaseX123a.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MySubTeamX123a.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySubTeamX123a extends MyTeamX123a {}\n" +
			    "	\n"
            },
            "MyBaseX123a.bm()\n" +
            "MyTeamX123a.MyRoleX123a.rm()");
    }

    // desc???
    // X.1.2-otjld-result-lifting-4
    public void testX12_resultLifting4() {
       
       runConformTest(
            new String[] {
		"test2/Main4.java",
			    "\n" +
			    "package test2;\n" +
			    "public class Main4 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX124 b = new MyBaseX124();\n" +
			    "        //MyTeamX124 t = new MyTeamX124();\n" +
			    "        MySubTeamX124 t = new MySubTeamX124();\n" +
			    "\n" +
			    "        t.go(b);\n" +
			    "\n" +
			    "    //System.out.println(\"---- Team is NOT active ---------\");\n" +
			    "	//b.bm();\n" +
			    "        //t.activate();\n" +
			    "        //b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test2/MySuperTeamX124.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySuperTeamX124 {\n" +
			    "    /*\n" +
			    "    public void go(MyBaseX124 b) {\n" +
			    "        b.bm();\n" +
			    "    }*/\n" +
			    "}\n" +
			    "	\n",
		"test2/MyTeamX124.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyTeamX124 extends MySuperTeamX124 {\n" +
			    "    @org.objectteams.ImplicitTeamActivation\n" +
			    "    public void go(MyBaseX124 b) {\n" +
			    "/*\n" +
			    "	if (b.equals(\"test\")) {\n" +
			    "		return;\n" +
			    "	}\n" +
			    "	\n" +
			    "	if (b==null) {\n" +
			    "		System.out.println(\"b was null\");\n" +
			    "		b.bm();\n" +
			    "		return;\n" +
			    "        }\n" +
			    "        b.bm();\n" +
			    "        return;\n" +
			    "*/\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class MyRoleX124 playedBy MyBaseX124 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeamX124.MyRoleX124.rm()\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MyBaseX124.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX124 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBaseX124.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test2/MySubTeamX124.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MySubTeamX124 extends MyTeamX124 {}\n" +
			    "	\n"
            },
            "MyBaseX124.bm()\n" +
            "MyTeamX124.MyRoleX124.rm()");
    }

    // base call result lifting for role in nested team
    // X.1.2-otjld-result-lifting-5
    public void testX12_resultLifting5() {
       
       runConformTest(
            new String[] {
		"test2/MainX125.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MainX125 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX125 b = new MyBaseX125();\n" +
			    "        MyOuterTeamX125 t = new MyOuterTeamX125();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"test2/MyBaseX125.java",
			    "\n" +
			    "package test2;\n" +
			    "public class MyBaseX125 {\n" +
			    "    public MyBaseX125 bm() {\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "    @Override public String toString() {\n" +
			    "        return\"NOTOK!\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"test2/MyOuterTeamX125.java",
			    "\n" +
			    "package test2;\n" +
			    "public team class MyOuterTeamX125 {\n" +
			    "    MyOuterTeamX125() {\n" +
			    "        activate();\n" +
			    "        new MyInnerTeamX125();\n" +
			    "    }\n" +
			    "    \n" +
			    "    public team class MyInnerTeamX125 {\n" +
			    "        public MyInnerTeamX125() {\n" +
			    "            activate();\n" +
			    "        }\n" +
			    "        public class MyRoleX125 playedBy MyBaseX125 {\n" +
			    "            @Override public String toString() {\n" +
			    "                return \"OK!\";\n" +
			    "            }\n" +
			    "            @SuppressWarnings(\"ambiguouslowering\") callin MyRoleX125 rm() {\n" +
			    "                MyRoleX125 r = base.rm();\n" +
			    "                System.out.print(r);\n" +
			    "                return r;\n" +
			    "            }\n" +
			    "            rm <- replace bm;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // callin to callin to callin to base method with base calls
    // X.1.3-otjld-callin-to-callin-1
    public void testX13_callinToCallin1() {
       
       runConformTest(
            new String[] {
		"test3/MainX131.java",
			    "\n" +
			    "package test3;\n" +
			    "public class MainX131 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamCX131 mtc = new MyTeamCX131();\n" +
			    "        mtc.activate();\n" +
			    "        MyBaseX131 mb = new MyBaseX131();\n" +
			    "        mb.doBase(\"test\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyBaseX131.java",
			    "\n" +
			    "package test3;\n" +
			    "\n" +
			    "public class MyBaseX131 {\n" +
			    "    public void doBase(String s) {\n" +
			    "        System.out.println(\"MyBase.doBase(s)\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamAX131.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamAX131 {\n" +
			    "    public class LowerRoleX131 playedBy MyBaseX131 {\n" +
			    "        callin void doA(String s) {\n" +
			    "            System.out.println(\"MyTeamA.LowerRole.doA(s)\");\n" +
			    "            base.doA(s);\n" +
			    "        }\n" +
			    "        doA <- replace doBase;\n" +
			    "\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamBX131.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamBX131 {\n" +
			    "\n" +
			    "    final MyTeamAX131 thatTeam = new MyTeamAX131();\n" +
			    "    public MyTeamBX131() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"decapsulation\") // wrt base call to doA\n" +
			    "    public class MyStackedRoleX131 playedBy LowerRoleX131<@thatTeam> {\n" +
			    "	callin void doB() {\n" +
			    "	    base.doB();\n" +
			    "	    System.out.println(\"MyTeamB.MyStackedRole.doB()\");\n" +
			    "	}\n" +
			    "	doB <- replace doA;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamCX131.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamCX131 {\n" +
			    "\n" +
			    "    final MyTeamBX131 thatTeam = new MyTeamBX131();\n" +
			    "    public MyTeamCX131() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"decapsulation\") // wrt base call to doB\n" +
			    "    protected class MyStackedRoleX131 playedBy MyStackedRoleX131<@thatTeam> {\n" +
			    "	callin void doC() {\n" +
			    "	    base.doC();\n" +
			    "	    System.out.println(\"MyTeamC.MyStackedRole.doC()\");\n" +
			    "	}\n" +
			    "	doC <- replace doB;\n" +
			    "    }\n" +
			    "}\n" +
			    "    	\n"
            },
            "MyTeamA.LowerRole.doA(s)\n" +
			"MyBase.doBase(s)\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()");
    }

    // one callin to a callin method and to a non-callin method
    // X.1.3-otjld-callin-to-callin-2
    public void testX13_callinToCallin2() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"test3/MainX132.java",
			    "\n" +
			    "package test3;\n" +
			    "public class MainX132 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamCX132 mtc = new MyTeamCX132();\n" +
			    "        mtc.activate();\n" +
			    "        MyBaseX132 mb = new MyBaseX132();\n" +
			    "        mb.doBase(\"test\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamCX132.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamCX132 {\n" +
			    "\n" +
			    "    final MyTeamBX132 thatTeam = new MyTeamBX132();\n" +
			    "    public MyTeamCX132() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    \n" +
			    "    protected class MyStackedRoleX132 playedBy MyStackedRoleX132<@thatTeam> {\n" +
			    "	callin void doC() {\n" +
			    "	    base.doC();\n" +
			    "	    System.out.println(\"MyTeamC.MyStackedRole.doC()\");\n" +
			    "	}\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	doC <- replace doB;\n" +
			    "    }\n" +
			    "}\n" +
			    "    	\n",
		"test3/MyBaseX132.java",
			    "\n" +
			    "package test3;\n" +
			    "\n" +
			    "public class MyBaseX132 {\n" +
			    "    public void doBase(String s) {\n" +
			    "        System.out.println(\"MyBase.doBase(s)\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamAX132.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamAX132 {\n" +
			    "    public class LowerRoleX132 playedBy MyBaseX132 {\n" +
			    "    	void doA1() {\n" +
			    "	    System.out.println(\"MyTeamA.LowerRole.doA1()\");\n" +
			    "	}\n" +
			    "	void doA1() <- before void doBase(String s);\n" +
			    "	\n" +
			    "        callin void doA(String s) {\n" +
			    "            System.out.println(\"MyTeamA.LowerRole.doA(s)\");\n" +
			    "            base.doA(s);\n" +
			    "        }\n" +
			    "        void doA(String sa) <- replace void doBase(String sb);\n" +
			    "\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamBX132.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamBX132 {\n" +
			    "\n" +
			    "    final MyTeamAX132 thatTeam = new MyTeamAX132();\n" +
			    "    public MyTeamBX132() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    public class MyStackedRoleX132 playedBy LowerRoleX132<@thatTeam> {\n" +
			    "	callin void doB() {\n" +
			    "	    base.doB();\n" +
			    "	    System.out.println(\"MyTeamB.MyStackedRole.doB()\");\n" +
			    "	}\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	void doB() <- replace void doA(String s);\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	void doB() <- replace void doA1();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeamA.LowerRole.doA1()\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()\n" +
			"MyTeamA.LowerRole.doA(s)\n" +
			"MyBase.doBase(s)\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // one callin to a callin method and to a non-callin method - compress to one binding
    // X.1.3-otjld-callin-to-callin-2a
    public void testX13_callinToCallin2a() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"test3/MainX132a.java",
			    "\n" +
			    "package test3;\n" +
			    "public class MainX132a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamCX132a mtc = new MyTeamCX132a();\n" +
			    "        mtc.activate();\n" +
			    "        MyBaseX132a mb = new MyBaseX132a();\n" +
			    "        mb.doBase(\"test\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamBX132a.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamBX132a {\n" +
			    "\n" +
			    "    final MyTeamAX132a thatTeam = new MyTeamAX132a();\n" +
			    "    public MyTeamBX132a() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    public class MyStackedRoleX132a playedBy LowerRoleX132a<@thatTeam> {\n" +
			    "	callin void doB() {\n" +
			    "	    base.doB();\n" +
			    "	    System.out.println(\"MyTeamB.MyStackedRole.doB()\");\n" +
			    "	}\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	void doB() <- replace void doA(String s);\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	void doB() <- replace void doA1();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamCX132a.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamCX132a {\n" +
			    "\n" +
			    "    final MyTeamBX132a thatTeam = new MyTeamBX132a();\n" +
			    "    public MyTeamCX132a() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    \n" +
			    "    protected class MyStackedRoleX132a playedBy MyStackedRoleX132a<@thatTeam> {\n" +
			    "	callin void doC() {\n" +
			    "	    base.doC();\n" +
			    "	    System.out.println(\"MyTeamC.MyStackedRole.doC()\");\n" +
			    "	}\n" +
			    "	@SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "	doC <- replace doB;\n" +
			    "    }\n" +
			    "}\n" +
			    "    	\n",
		"test3/MyBaseX132a.java",
			    "\n" +
			    "package test3;\n" +
			    "\n" +
			    "public class MyBaseX132a {\n" +
			    "    public void doBase(String s) {\n" +
			    "        System.out.println(\"MyBase.doBase(s)\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamAX132a.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamAX132a {\n" +
			    "    public class LowerRoleX132a playedBy MyBaseX132a {\n" +
			    "    	void doA1() {\n" +
			    "	    System.out.println(\"MyTeamA.LowerRole.doA1()\");\n" +
			    "	}\n" +
			    "	void doA1() <- before void doBase(String s);\n" +
			    "	\n" +
			    "        callin void doA(String s) {\n" +
			    "            System.out.println(\"MyTeamA.LowerRole.doA(s)\");\n" +
			    "            base.doA(s);\n" +
			    "        }\n" +
			    "        void doA(String sa) <- replace void doBase(String sb);\n" +
			    "\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeamA.LowerRole.doA1()\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()\n" +
			"MyTeamA.LowerRole.doA(s)\n" +
			"MyBase.doBase(s)\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // callin from a nested team to a callin method
    // X.1.3-otjld-callin-to-callin-3
    public void testX13_callinToCallin3() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"test3/MainX133.java",
			    "\n" +
			    "package test3;\n" +
			    "public class MainX133 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamBX133 mtb = new MyTeamBX133();\n" +
			    "        mtb.activate();\n" +
			    "        MyBaseX133 mb = new MyBaseX133();\n" +
			    "        mb.doBase();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamBX133.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamBX133 {\n" +
			    "    public MyTeamBX133() {\n" +
			    "        new MyInnerTeamX133(new MyTeamAX133());\n" +
			    "    }\n" +
			    "\n" +
			    "    protected team class MyInnerTeamX133 playedBy MyTeamAX133 {\n" +
			    "\n" +
			    "        public final MyTeamAX133 base_ref;\n" +
			    "\n" +
			    "        public MyInnerTeamX133(final MyTeamAX133 br) {\n" +
			    "                base_ref = br;\n" +
			    "                activate();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected class MyStackedRoleX133 playedBy LowerRoleX133<@base_ref> {\n" +
			    "            void printMessage() {\n" +
			    "		System.out.println(\"MyTeamB.MyInnerTeam.MyStackedRole.printMessage()\");\n" +
			    "	    }\n" +
			    "	    printMessage <- after doA;\n" +
			    "	}\n" +
			    "\n" +
			    "\n" +
			    "\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test3/MyBaseX133.java",
			    "\n" +
			    "package test3;\n" +
			    "\n" +
			    "public class MyBaseX133 {\n" +
			    "    public void doBase() {\n" +
			    "        System.out.println(\"MyBase.doBase()\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test3/MyTeamAX133.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamAX133 {\n" +
			    "    public class LowerRoleX133 playedBy MyBaseX133 {\n" +
			    "    	callin void doA() {\n" +
			    "	    base.doA();\n" +
			    "	    System.out.println(\"MyTeamA.LowerRole.doA()\");\n" +
			    "	}\n" +
			    "	void doA() <- replace void doBase();\n" +
			    "    }\n" +
			    "    MyTeamAX133 () {\n" +
			    "    	activate();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyBase.doBase()\n" +
			"MyTeamA.LowerRole.doA()\n" +
			"MyTeamB.MyInnerTeam.MyStackedRole.printMessage()",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // static callin to callin to callin to base method with base calls
    // X.1.3-otjld-static-callin-to-callin-1
    public void testX13_staticCallinToCallin1() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"test3/MainX13sctc1.java",
			    "\n" +
			    "package test3;\n" +
			    "public class MainX13sctc1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamCX13sctc1 mtc = new MyTeamCX13sctc1();\n" +
			    "        mtc.activate();\n" +
			    "        MyBaseX13sctc1.doBase(\"test\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"test3/MyTeamCX13sctc1.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamCX13sctc1 {\n" +
			    "\n" +
			    "    final MyTeamBX13sctc1 thatTeam = new MyTeamBX13sctc1();\n" +
			    "    public MyTeamCX13sctc1() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    \n" +
			    "    protected class MyStackedRoleX13sctc1 playedBy MyStackedRoleX13sctc1<@thatTeam> {\n" +
			    "      static callin void doC() {\n" +
			    "        base.doC();\n" +
			    "        System.out.println(\"MyTeamC.MyStackedRole.doC()\");\n" +
			    "      }\n" +
			    "      @SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "      doC <- replace doB;\n" +
			    "    }\n" +
			    "}\n" +
			    "        \n",
		"test3/MyBaseX13sctc1.java",
			    "\n" +
			    "package test3;\n" +
			    "\n" +
			    "public class MyBaseX13sctc1 {\n" +
			    "    public static void doBase(String s) {\n" +
			    "        System.out.println(\"MyBase.doBase(s)\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"test3/MyTeamAX13sctc1.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamAX13sctc1 {\n" +
			    "    public class LowerRoleX13sctc1 playedBy MyBaseX13sctc1 {\n" +
			    "         static callin void doA(String s) {\n" +
			    "            System.out.println(\"MyTeamA.LowerRole.doA(s)\");\n" +
			    "            base.doA(s);\n" +
			    "        }\n" +
			    "        doA <- replace doBase;\n" +
			    "\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"test3/MyTeamBX13sctc1.java",
			    "\n" +
			    "package test3;\n" +
			    "public team class MyTeamBX13sctc1 {\n" +
			    "\n" +
			    "    final MyTeamAX13sctc1 thatTeam = new MyTeamAX13sctc1();\n" +
			    "    public MyTeamBX13sctc1() {\n" +
			    "      thatTeam.activate();\n" +
			    "    }\n" +
			    "    public class MyStackedRoleX13sctc1 playedBy LowerRoleX13sctc1<@thatTeam> {\n" +
			    "      static callin void doB() {\n" +
			    "        base.doB();\n" +
			    "        System.out.println(\"MyTeamB.MyStackedRole.doB()\");\n" +
			    "      }\n" +
			    "      @SuppressWarnings(\"decapsulation\") // wrt base call\n" +
			    "      doB <- replace doA;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeamA.LowerRole.doA(s)\n" +
			"MyBase.doBase(s)\n" +
			"MyTeamB.MyStackedRole.doB()\n" +
			"MyTeamC.MyStackedRole.doC()",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // (replace) callin from one role method to two base methods with the same name but different signatures
    // X.1.4-otjld-multiple-signatures-1
    public void testX14_multipleSignatures1() {
       
       runConformTest(
            new String[] {
		"test4/MainX141.java",
			    "\n" +
			    "package test4;\n" +
			    "public class MainX141 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyTeamAX141 mta = new MyTeamAX141();\n" +
			    "        mta.activate();\n" +
			    "        MyBaseX141 mb = new MyBaseX141();\n" +
			    "        mb.doBase(\"test\");\n" +
			    "	mb.doBase();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test4/MyBaseX141.java",
			    "\n" +
			    "package test4;\n" +
			    "\n" +
			    "public class MyBaseX141 {\n" +
			    "    public void doBase() {\n" +
			    "        System.out.println(\"MyBase.doBase()\");\n" +
			    "    }\n" +
			    "    \n" +
			    "    public void doBase(String s) {\n" +
			    "        System.out.println(\"MyBase.doBase(s)\");\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test4/MyTeamAX141.java",
			    "\n" +
			    "package test4;\n" +
			    "public team class MyTeamAX141 {\n" +
			    "    public class LowerRoleX141 playedBy MyBaseX141 {\n" +
			    "        callin void doA() {\n" +
			    "            System.out.println(\"MyTeamA.LowerRole.doA()\");\n" +
			    "            base.doA();\n" +
			    "        }\n" +
			    "        void doA() <- replace void doBase(String sb);\n" +
			    "        void doA() <- replace void doBase();\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeamA.LowerRole.doA()\n" +
			"MyBase.doBase(s)\n" +
			"MyTeamA.LowerRole.doA()\n" +
			"MyBase.doBase()");
    }

    // after and before callin to a static role method
    // X.1.5-otjld-callin-to-static-role-method-1
    public void testX15_callinToStaticRoleMethod1() {
       
       runConformTest(
            new String[] {
		"test5/MainX151.java",
			    "\n" +
			    "package test5;\n" +
			    "public class MainX151 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX151 b = new MyBaseX151();\n" +
			    "        MyTeamX151 t = new MyTeamX151();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test5/MyTeamX151.java",
			    "\n" +
			    "package test5;\n" +
			    "public team class MyTeamX151 {\n" +
			    "    public class MyRoleX151 playedBy MyBaseX151 {\n" +
			    "        public static void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "	rm <- after bm;\n" +
			    "	rm <- before bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test5/MyBaseX151.java",
			    "\n" +
			    "package test5;\n" +
			    "\n" +
			    "public class MyBaseX151 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // after and before callin to the same role method
    // X.1.5-otjld-after-and-before-callin-1a
    public void testX15_afterAndBeforeCallin1a() {
       
       runConformTest(
            new String[] {
		"test5/MainX151a.java",
			    "\n" +
			    "package test5;\n" +
			    "public class MainX151a {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX151a b = new MyBaseX151a();\n" +
			    "        MyTeamX151a t = new MyTeamX151a();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test5/MyTeamX151a.java",
			    "\n" +
			    "package test5;\n" +
			    "public team class MyTeamX151a {\n" +
			    "    public class MyRoleX151a playedBy MyBaseX151a {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "	rm <- after bm;\n" +
			    "	rm <- before bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test5/MyBaseX151a.java",
			    "\n" +
			    "package test5;\n" +
			    "\n" +
			    "public class MyBaseX151a {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()");
    }

    // replace callin to a static (callin) role method
    // X.1.5-otjld-callin-to-static-role-method-2
    public void testX15_callinToStaticRoleMethod2() {
        runNegativeTestMatching(
            new String[] {
		"test5/MyTeamX152.java",
			    "\n" +
			    "package test5;\n" +
			    "public team class MyTeamX152 {\n" +
			    "    public class MyRoleX152 playedBy MyBaseX152 {\n" +
			    "        static callin void rm() {\n" +
			    "            base.rm();\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "	rm <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test5/MyBaseX152.java",
			    "\n" +
			    "package test5;\n" +
			    "\n" +
			    "public class MyBaseX152 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "4.7(d)");
    }

    // after callin to a static role method
    // X.1.5-otjld-callin-to-static-role-method-3
    public void testX15_callinToStaticRoleMethod3() {
       
       runConformTest(
            new String[] {
		"test5/MainX153.java",
			    "\n" +
			    "package test5;\n" +
			    "public class MainX153 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        MyBaseX153 b = new MyBaseX153();\n" +
			    "        MyTeamX153 t = new MyTeamX153();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "	\n",
		"test5/MyTeamX153.java",
			    "\n" +
			    "package test5;\n" +
			    "public team class MyTeamX153 {\n" +
			    "    public class MyRoleX153 playedBy MyBaseX153 {\n" +
			    "        public static void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "	rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n",
		"test5/MyBaseX153.java",
			    "\n" +
			    "package test5;\n" +
			    "\n" +
			    "public class MyBaseX153 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "	\n"
            },
            "MyBase.bm()\n" +
       		"MyTeam.MyRole.rm()");
    }

    // after callin from a static base method to a non-static role method
    // X.1.5-otjld-callin-from-static-base-method-4
    public void testX15_callinFromStaticBaseMethod4() {
        runNegativeTest(
            new String[] {
		"test5/TeamX15cfsbm4.java",
			    "\n" +
			    "package test5;\n" +
			    "public team class TeamX15cfsbm4 {\n" +
			    "    public class MyRole playedBy TX15cfsbm4 {\n" +
			    "        public void rm() { }\n" +
			    "    rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"test5/TX15cfsbm4.java",
			    "\n" +
			    "package test5;\n" +
			    "\n" +
			    "public class TX15cfsbm4 {\n" +
			    "    public static void bm() { }\n" +
			    "}\n" +
			    "    \n",
		"test5/TX15cfsbm4Main.java",
			    "\n" +
			    "package test5;\n" +
			    "public class TX15cfsbm4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        TeamX15cfsbm4 t = new TeamX15cfsbm4();\n" +
			    "        t.activate();\n" +
			    "        TX15cfsbm4.bm();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a replace-callin binds a static base method to a static role method without arguments
    // X.1.5-otjld-callin-from-static-base-method-5
    public void testX15_callinFromStaticBaseMethod5() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm5Main.java",
			    "\n" +
			    "        public class TX15cfsbm5Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm5 t = new TeamX15cfsbm5();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm5.sm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm5.java",
			    "\n" +
			    "        public class TX15cfsbm5 {\n" +
			    "			public static void sm(){}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm5.java",
			    "\n" +
			    "		public team class TeamX15cfsbm5 {\n" +
			    "			public class RX15cfsbm5 playedBy TX15cfsbm5{\n" +
			    "				static callin void srm(){\n" +
			    "					base.srm();\n" +
			    "					System.out.print(\"OK\");\n" +
			    "				}\n" +
			    "				srm <- replace sm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a replace-callin binds a static base method with a declared result to a static role method that does not declare a result
    // X.1.5-otjld-callin-from-static-base-method-6
    public void testX15_callinFromStaticBaseMethod6() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm6Main.java",
			    "\n" +
			    "        public class TX15cfsbm6Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm6 t = new TeamX15cfsbm6();\n" +
			    "				t.activate();\n" +
			    "				System.out.print(TX15cfsbm6.bm());\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm6.java",
			    "\n" +
			    "        public class TX15cfsbm6 {\n" +
			    "			public static int bm(){\n" +
			    "				return 7;\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm6.java",
			    "\n" +
			    "		public team class TeamX15cfsbm6 {\n" +
			    "			public class RX15cfsbm6 playedBy TX15cfsbm6{\n" +
			    "				static callin void srm(){\n" +
			    "					base.srm();\n" +
			    "				}\n" +
			    "				void srm() <- replace int bm() with { \n" +
			    "					3 -> result\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "3");
    }

    // replace-callin binding to a static role method with argument mapping
    // X.1.5-otjld-callin-from-static-base-method-7
    public void testX15_callinFromStaticBaseMethod7() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm7Main.java",
			    "\n" +
			    "        public class TX15cfsbm7Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm7 t = new TeamX15cfsbm7();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm7.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm7.java",
			    "\n" +
			    "        public class TX15cfsbm7 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm7.java",
			    "\n" +
			    "		public team class TeamX15cfsbm7 {\n" +
			    "			public class RX15cfsbm7 playedBy TX15cfsbm7{\n" +
			    "				static callin void srm(String arg){\n" +
			    "					base.srm(arg);\n" +
			    "					System.out.print(arg);\n" +
			    "				}\n" +
			    "				void srm(String arg) <- replace void bm() with { \n" +
			    "					arg <- \"OK\"\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a replace-callin binding cannot bind a non-static base method to a static role method
    // X.1.5-otjld-callin-from-static-base-method-8
    public void testX15_callinFromStaticBaseMethod8() {
        runNegativeTestMatching(
            new String[] {
		"TeamX15cfsbm8.java",
			    "\n" +
			    "		public team class TeamX15cfsbm8 {\n" +
			    "			public class RX15cfsbm8 playedBy TX15cfsbm8{\n" +
			    "				static callin void srm(){\n" +
			    "					base.srm();\n" +
			    "				}\n" +
			    "				srm <- replace bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm8.java",
			    "\n" +
			    "        public class TX15cfsbm8 {\n" +
			    "			public void bm(){}\n" +
			    "		}\n" +
			    "    \n"
            },
            "4.7(d)");
    }

    // a replace-callin binding of a static base method to a non-static role method is illegal
    // X.1.5-otjld-callin-from-static-base-method-9
    public void testX15_callinFromStaticBaseMethod9() {
        runNegativeTestMatching(
            new String[] {
		"TeamX15cfsbm9.java",
			    "\n" +
			    "		public team class TeamX15cfsbm9 {\n" +
			    "			public class RX15cfsbm9 playedBy TX15cfsbm9{\n" +
			    "				callin void srm(){\n" +
			    "					base.srm();\n" +
			    "				}\n" +
			    "				srm <- replace bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm9.java",
			    "\n" +
			    "        public class TX15cfsbm9 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n"
            },
            "4.7(b)");
    }

    // a before-callin binding of a static base method to a non-static role method is illegal
    // X.1.5-otjld-callin-from-static-base-method-10
    public void testX15_callinFromStaticBaseMethod10() {
        runNegativeTestMatching(
            new String[] {
		"TeamX15cfsbm10.java",
			    "\n" +
			    "		public team class TeamX15cfsbm10 {\n" +
			    "			public class RX15cfsbm10 playedBy TX15cfsbm10{\n" +
			    "				public void srm(){}\n" +
			    "				srm <- before bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm10.java",
			    "\n" +
			    "        public class TX15cfsbm10 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n"
            },
            "4.7(b)");
    }

    // an after-callin binding of a static base method to a non-static role method is illegal
    // X.1.5-otjld-callin-from-static-base-method-11
    public void testX15_callinFromStaticBaseMethod11() {
        runNegativeTestMatching(
            new String[] {
		"TeamX15cfsbm11.java",
			    "\n" +
			    "		public team class TeamX15cfsbm11 {\n" +
			    "			public class RX15cfsbm11 playedBy TX15cfsbm11{\n" +
			    "				public void srm(){}\n" +
			    "				srm <- after bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm11.java",
			    "\n" +
			    "        public class TX15cfsbm11 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n"
            },
            "4.7(b)");
    }

    // a replace-callin binds an inherited static role method to a static base method
    // X.1.5-otjld-callin-from-static-base-method-12
    public void testX15_callinFromStaticBaseMethod12() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm12Main.java",
			    "\n" +
			    "        public class TX15cfsbm12Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm12_2 t = new TeamX15cfsbm12_2();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm12.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm12.java",
			    "\n" +
			    "        public class TX15cfsbm12 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm12_1.java",
			    "\n" +
			    "		public team class TeamX15cfsbm12_1 {\n" +
			    "			public class RX15cfsbm12_1 {\n" +
			    "				static callin void srm() {\n" +
			    "					base.srm();\n" +
			    "					System.out.print(\"OK\");\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm12_2.java",
			    "\n" +
			    "		public team class TeamX15cfsbm12_2 extends TeamX15cfsbm12_1{\n" +
			    "			public class RX15cfsbm12_2 extends RX15cfsbm12_1 playedBy TX15cfsbm12{\n" +
			    "				srm <- replace bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a replace-callin binds an inherited static role method to a static base method - same team
    // X.1.5-otjld-callin-from-static-base-method-12a
    public void testX15_callinFromStaticBaseMethod12a() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm12aMain.java",
			    "\n" +
			    "        public class TX15cfsbm12aMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm12a_1 t = new TeamX15cfsbm12a_1();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm12a.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm12a.java",
			    "\n" +
			    "        public class TX15cfsbm12a {\n" +
			    "            public static void bm(){}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm12a_1.java",
			    "\n" +
			    "        public team class TeamX15cfsbm12a_1 {\n" +
			    "            public class RX15cfsbm12a_1 {\n" +
			    "                static callin void srm() {\n" +
			    "                    base.srm();\n" +
			    "                    System.out.print(\"OK\");\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public class RX15cfsbm12a_2 extends RX15cfsbm12a_1 playedBy TX15cfsbm12a {\n" +
			    "                srm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // a replace-callin binds an inherited static role method to a static base method - method has a role type parameter
    // X.1.5-otjld-callin-from-static-base-method-12b
    public void testX15_callinFromStaticBaseMethod12b() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm12bMain.java",
			    "\n" +
			    "        public class TX15cfsbm12bMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm12b_2 t = new TeamX15cfsbm12b_2();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm12b.bm(new TX15cfsbm12b(\"!\"));\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm12b.java",
			    "\n" +
			    "        public class TX15cfsbm12b {\n" +
			    "            String v;\n" +
			    "            public void k() { \n" +
			    "                System.out.print(v);\n" +
			    "            }\n" +
			    "            public TX15cfsbm12b(String v) { this.v = v; }\n" +
			    "            public static void bm(TX15cfsbm12b b){ System.out.print(\"O\");}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm12b_1.java",
			    "\n" +
			    "        public abstract team class TeamX15cfsbm12b_1 {\n" +
			    "            public abstract class RX15cfsbm12b_1 {\n" +
			    "                static callin void srm(RX15cfsbm12b_2 r) {\n" +
			    "                    base.srm(r);\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                    r.co();\n" +
			    "                }\n" +
			    "                abstract void co();\n" +
			    "            }\n" +
			    "            public abstract class RX15cfsbm12b_2 extends RX15cfsbm12b_1 playedBy TX15cfsbm12b {}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm12b_2.java",
			    "\n" +
			    "        public team class TeamX15cfsbm12b_2 extends TeamX15cfsbm12b_1{\n" +
			    "            public class RX15cfsbm12b_2 {\n" +
			    "                void srm(RX15cfsbm12b_2 r) <- replace void bm(TX15cfsbm12b b);\n" +
			    "                co -> k;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK!");
    }

    // a replace-callin binds an inherited static role method to a static base method - method has a role type parameter -- double compile
    // X.1.5-otjld-callin-from-static-base-method-12c
    public void testX15_callinFromStaticBaseMethod12c() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm12cMain.java",
			    "\n" +
			    "        public class TX15cfsbm12cMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm12c_2 t = new TeamX15cfsbm12c_2();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm12c.bm(new TX15cfsbm12c(\"!\"));\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm12c.java",
			    "\n" +
			    "        public class TX15cfsbm12c {\n" +
			    "            String v;\n" +
			    "            public void k() {\n" +
			    "                System.out.print(v);\n" +
			    "            }\n" +
			    "            public TX15cfsbm12c(String v) { this.v = v; }\n" +
			    "            public static void bm(TX15cfsbm12c b){ System.out.print(\"O\");}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm12c_1.java",
			    "\n" +
			    "        public abstract team class TeamX15cfsbm12c_1 {\n" +
			    "            public abstract class RX15cfsbm12c_1 {\n" +
			    "                static callin void srm(RX15cfsbm12c_2 r) {\n" +
			    "                    base.srm(r);\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                    r.co();\n" +
			    "                }\n" +
			    "                abstract void co();\n" +
			    "            }\n" +
			    "            public abstract class RX15cfsbm12c_2 extends RX15cfsbm12c_1 playedBy TX15cfsbm12c {}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm12c_2.java",
			    "\n" +
			    "        public team class TeamX15cfsbm12c_2 extends TeamX15cfsbm12c_1{\n" +
			    "            public class RX15cfsbm12c_2 {\n" +
			    "                void srm(RX15cfsbm12c_2 r) <- replace void bm(TX15cfsbm12c b);\n" +
			    "                co -> k;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK!");
    }

    // an after-callin binds an inherited static role method to a static base method
    // X.1.5-otjld-callin-from-static-base-method-13
    public void testX15_callinFromStaticBaseMethod13() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm13Main.java",
			    "\n" +
			    "        public class TX15cfsbm13Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm13_2 t = new TeamX15cfsbm13_2();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm13.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm13.java",
			    "\n" +
			    "        public class TX15cfsbm13 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm13_1.java",
			    "\n" +
			    "		public team class TeamX15cfsbm13_1 {\n" +
			    "			public class RX15cfsbm13_1{\n" +
			    "				public static void srm() {\n" +
			    "					System.out.print(\"OK\");\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm13_2.java",
			    "\n" +
			    "		public team class TeamX15cfsbm13_2 extends TeamX15cfsbm13_1{\n" +
			    "			public class RX15cfsbm13_2 extends RX15cfsbm13_1 playedBy TX15cfsbm13{\n" +
			    "				srm <- after bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a before-callin binds an inherited static role method to a static base method
    // X.1.5-otjld-callin-from-static-base-method-14
    public void testX15_callinFromStaticBaseMethod14() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm14Main.java",
			    "\n" +
			    "        public class TX15cfsbm14Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm14_2 t = new TeamX15cfsbm14_2();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm14.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm14.java",
			    "\n" +
			    "        public class TX15cfsbm14 {\n" +
			    "			public static void bm(){}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm14_1.java",
			    "\n" +
			    "		public team class TeamX15cfsbm14_1 {\n" +
			    "			public class RX15cfsbm14_1{\n" +
			    "				public static void srm() {\n" +
			    "					System.out.print(\"OK\");\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm14_2.java",
			    "\n" +
			    "		public team class TeamX15cfsbm14_2 extends TeamX15cfsbm14_1{\n" +
			    "			public class RX15cfsbm14_2 extends RX15cfsbm14_1 playedBy TX15cfsbm14{\n" +
			    "				srm <- after bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a replace-callin binds an inherited method (implemented within the super-super team) to a static base method
    // X.1.5-otjld-callin-from-static-base-method-15
    public void testX15_callinFromStaticBaseMethod15() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm15Main.java",
			    "\n" +
			    "        public class TX15cfsbm15Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm15_3 t = new TeamX15cfsbm15_3();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm15_2.bm2();	\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm15_1.java",
			    "\n" +
			    "        public class TX15cfsbm15_1 {\n" +
			    "			public static void bm1() {}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm15_2.java",
			    "\n" +
			    "		public class TX15cfsbm15_2 {\n" +
			    "			public static void bm2() {}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm15_1.java",
			    "\n" +
			    "		public team class TeamX15cfsbm15_1 {\n" +
			    "			public class RX15cfsbm15_1 {\n" +
			    "				static callin void rm() {\n" +
			    "					base.rm();\n" +
			    "					System.out.print(\"OK\");\n" +
			    "				}\n" +
			    "			} \n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm15_2.java",
			    "\n" +
			    "		public team class TeamX15cfsbm15_2 extends TeamX15cfsbm15_1{\n" +
			    "			public class RX15cfsbm15_2_1 extends RX15cfsbm15_1 playedBy TX15cfsbm15_1{\n" +
			    "				rm <- replace bm1;\n" +
			    "			}\n" +
			    "	\n" +
			    "			public class RX15cfsbm15_2_2 extends RX15cfsbm15_1 playedBy TX15cfsbm15_2{\n" +
			    "				rm <- replace bm2;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm15_3.java",
			    "\n" +
			    "		public team class TeamX15cfsbm15_3 extends TeamX15cfsbm15_2{\n" +
			    "			public class RX15cfsbm15_3 extends RX15cfsbm15_1 playedBy TX15cfsbm15_2{\n" +
			    "			        rm <- replace bm2;\n" +
			    "			}\n" +
			    "			precedence RX15cfsbm15_2_2, RX15cfsbm15_3;\n" +
			    "		}\n" +
			    "	\n"
            },
            "OKOK");
    }

    // static base call requires a result lifting
    // X.1.5-otjld-callin-from-static-base-method-16
    public void testX15_callinFromStaticBaseMethod16() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm16Main.java",
			    "\n" +
			    "		public class TX15cfsbm16Main {\n" +
			    "    		public static void main(String[] args) {\n" +
			    "        		TeamX15cfsbm16 t = new TeamX15cfsbm16();\n" +
			    "        		t.activate();\n" +
			    "        		TX15cfsbm16.bm();\n" +
			    "    		}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm16.java",
			    "\n" +
			    "		public class TX15cfsbm16 {	\n" +
			    "			public static TX15cfsbm16 bm() {\n" +
			    "        		System.out.println(\"TX15cfsbm16.bm()\");\n" +
			    "        		return new TX15cfsbm16();        \n" +
			    "    		}\n" +
			    "    	}\n" +
			    "	\n",
		"TeamX15cfsbm16.java",
			    "\n" +
			    "		public team class TeamX15cfsbm16 {\n" +
			    "    		public class RX15cfsbm16 playedBy TX15cfsbm16 {\n" +
			    "        		static callin RX15cfsbm16 rm() {\n" +
			    "            		System.out.println(\"TeamX15cfsbm16.RX15cfsbm16.rm()\");\n" +
			    "            		return base.rm();\n" +
			    "        		}\n" +
			    "        		rm <- replace bm;\n" +
			    "    		}\n" +
			    "		}\n" +
			    "	\n"
            },
            "TeamX15cfsbm16.RX15cfsbm16.rm()\n" +
       		"TX15cfsbm16.bm()");
    }

    // static base call requires a result lifting; parameter involved
    // X.1.5-otjld-callin-from-static-base-method-16a
    public void testX15_callinFromStaticBaseMethod16a() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm16aMain.java",
			    "\n" +
			    "        public class TX15cfsbm16aMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm16a t = new TeamX15cfsbm16a();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm16a.bm(\"\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm16a.java",
			    "\n" +
			    "        public class TX15cfsbm16a {  \n" +
			    "            public static TX15cfsbm16a bm(String s) {\n" +
			    "                System.out.println(\"TX15cfsbm16a.bm()\");\n" +
			    "                return new TX15cfsbm16a();        \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm16a.java",
			    "\n" +
			    "        public team class TeamX15cfsbm16a {\n" +
			    "            public class RX15cfsbm16a playedBy TX15cfsbm16a {\n" +
			    "                static callin RX15cfsbm16a rm(String s) {\n" +
			    "                    System.out.println(\"TeamX15cfsbm16a.RX15cfsbm16a.rm()\");\n" +
			    "                    return base.rm(s);\n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "TeamX15cfsbm16a.RX15cfsbm16a.rm()\n" +
       		"TX15cfsbm16a.bm()");
    }

    // replace-callin-binding with parameter mapping (constant value)
    // X.1.5-otjld-callin-from-static-base-method-17
    public void testX15_callinFromStaticBaseMethod17() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm17Main.java",
			    "\n" +
			    "    	public class TX15cfsbm17Main {\n" +
			    "        	public static void main(String[] args) {\n" +
			    "            	TeamX15cfsbm17 t = new TeamX15cfsbm17();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm17.bm(42);\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TX15cfsbm17.java",
			    "\n" +
			    "		public class TX15cfsbm17 {\n" +
			    "			static void bm(int a) {}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm17.java",
			    "\n" +
			    "		public team class TeamX15cfsbm17 {\n" +
			    "			public class RX15cfsbm17 playedBy TX15cfsbm17{\n" +
			    "				static callin void rm(int b) {\n" +
			    "					base.rm(b);\n" +
			    "					System.out.print(b);\n" +
			    "				}\n" +
			    "	\n" +
			    "				void rm(int b) <- replace void bm(int a) with {\n" +
			    "					b <- 7\n" +
			    "				}\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "7");
    }

    // an inherited static role method is bound in different roles to different static base methods
    // X.1.5-otjld-callin-from-static-base-method-18
    public void testX15_callinFromStaticBaseMethod18() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm18Main.java",
			    "\n" +
			    "        public class TX15cfsbm18Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm18_2 t = new TeamX15cfsbm18_2();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm18_1.bm();\n" +
			    "				TX15cfsbm18_2.bm();\n" +
			    "	\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm18_1.java",
			    "\n" +
			    "        public class TX15cfsbm18_1 {\n" +
			    "			public static void bm() {\n" +
			    "				System.out.println(\"OK1\");\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm18_2.java",
			    "\n" +
			    "		public class TX15cfsbm18_2 {\n" +
			    "			public static void bm() {\n" +
			    "				System.out.println(\"OK2\");\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm18_1.java",
			    "\n" +
			    "		public team class TeamX15cfsbm18_1 {\n" +
			    "			public class RX15cfsbm18_1 {\n" +
			    "				static callin void rm() {\n" +
			    "					base.rm();\n" +
			    "				}\n" +
			    "			} \n" +
			    "		}\n" +
			    "	\n",
		"TeamX15cfsbm18_2.java",
			    "\n" +
			    "		public team class TeamX15cfsbm18_2 extends TeamX15cfsbm18_1{\n" +
			    "			public class RX15cfsbm18_2_1 extends RX15cfsbm18_1 playedBy TX15cfsbm18_1{\n" +
			    "				rm <- replace bm;\n" +
			    "			}\n" +
			    "	\n" +
			    "			public class RX15cfsbm18_2_2 extends RX15cfsbm18_1 playedBy TX15cfsbm18_2{\n" +
			    "				rm <- replace bm;\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK1\n" +
       		"OK2");
    }

    // static role methods are bound to same base method
    // X.1.5-otjld-callin-from-static-base-method-19
    public void testX15_callinFromStaticBaseMethod19() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm19Main.java",
			    "\n" +
			    "        public class TX15cfsbm19Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm19 t = new TeamX15cfsbm19();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm19.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm19.java",
			    "\n" +
			    "    	public class TX15cfsbm19 {\n" +
			    "			public static void bm() {}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm19.java",
			    "\n" +
			    "		public team class TeamX15cfsbm19 {	\n" +
			    "			public class RX15cfsbm19 playedBy TX15cfsbm19 {	\n" +
			    "				static callin void rm1() {\n" +
			    "					System.out.println(\"rm1()\");\n" +
			    "					base.rm1();\n" +
			    "				}\n" +
			    "		\n" +
			    "				static callin void rm2() {\n" +
			    "					System.out.println(\"rm2()\");\n" +
			    "					base.rm2();\n" +
			    "				}\n" +
			    "		\n" +
			    "				c1: rm1 <- replace bm;\n" +
			    "				c2: rm2 <- replace bm;\n" +
			    "				precedence c1,c2;\n" +
			    "			} \n" +
			    "		}\n" +
			    "	\n"
            },
            "rm1()\n" +
       		"rm2()");
    }

    // static role methods of different roles are bound to same base method - no precedence needed due to different static base classes
    // X.1.5-otjld-callin-from-static-base-method-19r
    public void testX15_callinFromStaticBaseMethod19r() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm19rMain.java",
			    "\n" +
			    "        public class TX15cfsbm19rMain {\n" +
			    "                        public static void main(String[] args) {\n" +
			    "                                TeamX15cfsbm19r t = new TeamX15cfsbm19r();\n" +
			    "                                t.activate();\n" +
			    "                                TX15cfsbm19r_2.bm();\n" +
			    "                        }\n" +
			    "                }\n" +
			    "    \n",
		"TX15cfsbm19r_1.java",
			    "\n" +
			    "        public class TX15cfsbm19r_1 {\n" +
			    "                        public static void bm() {}\n" +
			    "                }\n" +
			    "    \n",
		"TX15cfsbm19r_2.java",
			    "\n" +
			    "        public class TX15cfsbm19r_2 extends TX15cfsbm19r_1 {}\n" +
			    "    \n",
		"TeamX15cfsbm19r.java",
			    "\n" +
			    "                public team class TeamX15cfsbm19r {\n" +
			    "                        public class RX15cfsbm19r1 playedBy TX15cfsbm19r_1 {\n" +
			    "                                static callin void rm1() {\n" +
			    "                                        System.out.println(\"rm1()\");\n" +
			    "                                        base.rm1();\n" +
			    "                                }\n" +
			    "                                c1: rm1 <- replace bm;\n" +
			    "                        }\n" +
			    "                        public class RX15cfsbm19r2 playedBy TX15cfsbm19r_2 {\n" +
			    "                                static callin void rm2() {\n" +
			    "                                        System.out.println(\"rm2()\");\n" +
			    "                                        base.rm2();\n" +
			    "                                }\n" +
			    "                                c2: rm2 <- replace bm;\n" +
			    "                        }\n" +
			    "                }\n" +
			    "        \n"
            },
            "rm2()\n" +
       		"rm1()");
    }

    // static replace binding to a base method, base- and team classes are placed in a package
    // X.1.5-otjld-callin-from-static-base-method-20
    public void testX15_callinFromStaticBaseMethod20() {
       
       runConformTest(
            new String[] {
		"cfsbm20/TX15cfsbm20Main.java",
			    "\n" +
			    "    	package cfsbm20;\n" +
			    "        public class TX15cfsbm20Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm20 t = new TeamX15cfsbm20();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm20.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"cfsbm20/TX15cfsbm20.java",
			    "\n" +
			    "    	package cfsbm20;\n" +
			    "		public class TX15cfsbm20 {\n" +
			    "			public static void bm() {\n" +
			    "				System.out.print(\"OK\");\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"cfsbm20/TeamX15cfsbm20.java",
			    "\n" +
			    "		package cfsbm20;\n" +
			    "		public team class TeamX15cfsbm20 {	\n" +
			    "			public class RX15cfsbm20 playedBy TX15cfsbm20 {	\n" +
			    "				static callin void rm() {\n" +
			    "					base.rm();\n" +
			    "				}		\n" +
			    "				rm <- replace bm;\n" +
			    "			} \n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // a static and a non-static role methods are bound to same base method
    // X.1.5-otjld-callin-from-static-base-method-21
    public void testX15_callinFromStaticBaseMethod21() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm21Main.java",
			    "\n" +
			    "        public class TX15cfsbm21Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm21 t = new TeamX15cfsbm21();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm21 b = new TX15cfsbm21();\n" +
			    "				b.bm();\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm21.java",
			    "\n" +
			    "    	public class TX15cfsbm21 {\n" +
			    "			public void bm() {}\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm21.java",
			    "\n" +
			    "		public team class TeamX15cfsbm21 {	\n" +
			    "			public class RX15cfsbm21 playedBy TX15cfsbm21 {	\n" +
			    "				static void rm1() {\n" +
			    "					System.out.println(\"OK1\");			\n" +
			    "				}\n" +
			    "				void rm2() {\n" +
			    "					System.out.println(\"OK2\");			\n" +
			    "				}\n" +
			    "				c1: rm1 <- after bm;\n" +
			    "				c2: rm2 <- after bm;\n" +
			    "				precedence after c2, c1;\n" +
			    "			} \n" +
			    "		}\n" +
			    "	\n"
            },
            "OK1\n" +
            "OK2");
    }

    // an overridden static base method is bound to methods of different role classes
    // X.1.5-otjld-callin-from-static-base-method-22
    public void testX15_callinFromStaticBaseMethod22() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm22Main.java",
			    "\n" +
			    "        public class TX15cfsbm22Main {\n" +
			    "			public static void main(String[] args) {\n" +
			    "				TeamX15cfsbm22 t = new TeamX15cfsbm22();\n" +
			    "				t.activate();\n" +
			    "				TX15cfsbm22_2.bm();	\n" +
			    "			}\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm22_1.java",
			    "\n" +
			    "    	public class TX15cfsbm22_1 {\n" +
			    "			public static void bm(){\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "		}\n" +
			    "    \n",
		"TX15cfsbm22_2.java",
			    "\n" +
			    "    	public class TX15cfsbm22_2 extends TX15cfsbm22_1 {\n" +
			    "			public static void bm(){\n" +
			    "				System.out.print(\"O\");\n" +
			    "			}			\n" +
			    "		}\n" +
			    "    \n",
		"TeamX15cfsbm22.java",
			    "\n" +
			    "		public team class TeamX15cfsbm22 {\n" +
			    "			public class RX15cfsbm22_1 playedBy TX15cfsbm22_1 {\n" +
			    "				static callin void rm(){\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");\n" +
			    "				}		\n" +
			    "				c1: rm <- replace bm;\n" +
			    "			}\n" +
			    "	\n" +
			    "			public class RX15cfsbm22_2 playedBy TX15cfsbm22_2 {\n" +
			    "				static callin void rm(){\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "				}		\n" +
			    "				c2: rm <- replace bm;\n" +
			    "			}			\n" +
			    "			precedence RX15cfsbm22_2.c2, RX15cfsbm22_1.c1;\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // an inherited static base method is bound (after) in a sub base class
    // X.1.5-otjld-callin-from-static-base-method-23
    public void testX15_callinFromStaticBaseMethod23() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm23Main.java",
			    "\n" +
			    "        public class TX15cfsbm23Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm23 t = new TeamX15cfsbm23();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm23_1.bm();\n" +
			    "                TX15cfsbm23_2.bm(); \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm23_1.java",
			    "\n" +
			    "        public class TX15cfsbm23_1 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm23_2.java",
			    "\n" +
			    "        public class TX15cfsbm23_2 extends TX15cfsbm23_1 {        \n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm23.java",
			    "\n" +
			    "        public team class TeamX15cfsbm23 {\n" +
			    "            public class RX15cfsbm23_1 playedBy TX15cfsbm23_2 {\n" +
			    "                static void rm(){\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }       \n" +
			    "                rm <- after bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            (this.weavingScheme == WeavingScheme.OTRE 
            ? "OOK"
            : "KK")); // see restriction https://bugs.eclipse.org/435136#c1
    }

    // an inherited static base method is bound (replace) in a sub base class
    // X.1.5-otjld-callin-from-static-base-method-23a
    public void testX15_callinFromStaticBaseMethod23a() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm23aMain.java",
			    "\n" +
			    "        public class TX15cfsbm23aMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm23a t = new TeamX15cfsbm23a();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm23a_1.bm();\n" +
			    "                TX15cfsbm23a_2.bm(); \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm23a_1.java",
			    "\n" +
			    "        public class TX15cfsbm23a_1 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm23a_2.java",
			    "\n" +
			    "        public class TX15cfsbm23a_2 extends TX15cfsbm23a_1 {        \n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm23a.java",
			    "\n" +
			    "        public team class TeamX15cfsbm23a {\n" +
			    "            public class RX15cfsbm23a_1 playedBy TX15cfsbm23a_2 {\n" +
			    "                static callin void rm(){\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }       \n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            (this.weavingScheme == WeavingScheme.OTRE 
            ? "OOK"
            : "KK")); // see restriction https://bugs.eclipse.org/435136#c1
    }

    // a static base method is redefined and rebound (after) to a static (also redefined) role method
    // X.1.5-otjld-callin-from-static-base-method-24
    public void testX15_callinFromStaticBaseMethod24() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm24Main.java",
			    "\n" +
			    "        public class TX15cfsbm24Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm24 t = new TeamX15cfsbm24();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm24_1.bm();\n" +
			    "                System.out.print(\"|\");\n" +
			    "                TX15cfsbm24_2.bm(); \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm24_1.java",
			    "\n" +
			    "        public class TX15cfsbm24_1 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm24_2.java",
			    "\n" +
			    "        public class TX15cfsbm24_2 extends TX15cfsbm24_1 {\n" +
			    "        public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm24.java",
			    "\n" +
			    "        public team class TeamX15cfsbm24 {\n" +
			    "            public class RX15cfsbm24_1 playedBy TX15cfsbm24_1 {\n" +
			    "                static void rm(){\n" +
			    "                    System.out.print(\"X\");\n" +
			    "                }       \n" +
			    "                rm <- after bm;\n" +
			    "            }\n" +
			    "\n" +
			    "            public class RX15cfsbm24_2 extends RX15cfsbm24_1 playedBy TX15cfsbm24_2 {\n" +
			    "                static void rm(){\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }       \n" +
			    "                rm <- after bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "NOTOX|OK");
    }

    // a static base method is redefined and rebound (replace) to a static (also redefined) role method
    // X.1.5-otjld-callin-from-static-base-method-24a
    public void testX15_callinFromStaticBaseMethod24a() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm24aMain.java",
			    "\n" +
			    "        public class TX15cfsbm24aMain {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm24a t = new TeamX15cfsbm24a();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm24a_1.bm();\n" +
			    "                System.out.print(\"|\");\n" +
			    "                TX15cfsbm24a_2.bm(); \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm24a_1.java",
			    "\n" +
			    "        public class TX15cfsbm24a_1 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm24a_2.java",
			    "\n" +
			    "        public class TX15cfsbm24a_2 extends TX15cfsbm24a_1 {\n" +
			    "        public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm24a.java",
			    "\n" +
			    "        public team class TeamX15cfsbm24a {\n" +
			    "            public class RX15cfsbm24a_1 playedBy TX15cfsbm24a_1 {\n" +
			    "                static callin void rm(){\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");\n" +
			    "                }       \n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "\n" +
			    "            public class RX15cfsbm24a_2 extends RX15cfsbm24a_1 playedBy TX15cfsbm24a_2 {\n" +
			    "                static callin void rm(){\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }       \n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "NOTOX|OK");
    }

    // if a static base method is bound in a super base class, a call to this (not redefined!) method at the subclass 'inherits' the callin
    // X.1.5-otjld-callin-from-static-base-method-25
    public void testX15_callinFromStaticBaseMethod25() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm25Main.java",
			    "\n" +
			    "        public class TX15cfsbm25Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm25 t = new TeamX15cfsbm25();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm25_1.bm();\n" +
			    "                TX15cfsbm25_2.bm(); \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm25_1.java",
			    "\n" +
			    "        public class TX15cfsbm25_1 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm25_2.java",
			    "\n" +
			    "        public class TX15cfsbm25_2 extends TX15cfsbm25_1 {        \n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm25.java",
			    "\n" +
			    "        public team class TeamX15cfsbm25 {\n" +
			    "            public class RX15cfsbm25_1 playedBy TX15cfsbm25_1 {\n" +
			    "                static void rm(){\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }       \n" +
			    "                rm <- after bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OKOK");
    }

    // replace binding of static methods - additional binding to the same role method in a sub role
    // X.1.5-otjld-callin-from-static-base-method-26
    public void testX15_callinFromStaticBaseMethod26() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm26Main.java",
			    "\n" +
			    "        public class TX15cfsbm26Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm26_1 t = new TeamX15cfsbm26_1();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm26.bm1();\n" +
			    "                System.out.print(\"|\");\n" +
			    "                TX15cfsbm26.bm2();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm26.java",
			    "\n" +
			    "        public class TX15cfsbm26 {\n" +
			    "            public static void bm1(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "            \n" +
			    "            public static void bm2() {\n" +
			    "                System.out.print(\"OKA\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm26_1.java",
			    "\n" +
			    "        public team class TeamX15cfsbm26_1 {\n" +
			    "            public class RX15cfsbm26_1 playedBy TX15cfsbm26 {\n" +
			    "                static callin void srm() {\n" +
			    "                    base.srm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                srm <- replace bm1;\n" +
			    "            }\n" +
			    "            public class RX15cfsbm26_2 extends RX15cfsbm26_1 {\n" +
			    "                srm <- replace bm2;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK|OKAK");
    }

    // replace binding of static methods - additional binding to the same base method in a sub role
    // X.1.5-otjld-callin-from-static-base-method-27
    public void testX15_callinFromStaticBaseMethod27() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm27Main.java",
			    "\n" +
			    "        public class TX15cfsbm27Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm27_1 t = new TeamX15cfsbm27_1();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm27.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm27.java",
			    "\n" +
			    "        public class TX15cfsbm27 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm27_1.java",
			    "\n" +
			    "        public team class TeamX15cfsbm27_1 {\n" +
			    "            precedence RX15cfsbm27_1.b1, RX15cfsbm27_2.b2;\n" +
			    "            public class RX15cfsbm27_1 playedBy TX15cfsbm27 {\n" +
			    "                static callin void srm1() {\n" +
			    "                    base.srm1();\n" +
			    "                    System.out.print(\"Y\");\n" +
			    "                }\n" +
			    "                b1: srm1 <- replace bm;\n" +
			    "\n" +
			    "                static callin void srm2() {\n" +
			    "                    base.srm2();\n" +
			    "                    System.out.print(\"KA\");\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public class RX15cfsbm27_2 extends RX15cfsbm27_1 {\n" +
			    "                b2: srm2 <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OKAY");
    }

    // replace binding of static methods - redefined role method in a sub role
    // X.1.5-otjld-callin-from-static-base-method-28
    public void testX15_callinFromStaticBaseMethod28() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm28Main.java",
			    "\n" +
			    "        public class TX15cfsbm28Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm28_1 t = new TeamX15cfsbm28_1();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm28.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm28.java",
			    "\n" +
			    "        public class TX15cfsbm28 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm28_1.java",
			    "\n" +
			    "        public team class TeamX15cfsbm28_1 {\n" +
			    "            public class RX15cfsbm28_1 playedBy TX15cfsbm28 {\n" +
			    "                static callin void srm() {\n" +
			    "                    base.srm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                srm <- replace bm;\n" +
			    "            }\n" +
			    "            public class RX15cfsbm28_2 extends RX15cfsbm28_1 {\n" +
			    "                static callin void srm() {\n" +
			    "                    base.srm();\n" +
			    "                    System.out.print(\"X\");\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // a redefined static base method does not inherit a callin binding from its version of the super class
    // X.1.5-otjld-callin-from-static-base-method-29
    public void testX15_callinFromStaticBaseMethod29() {
       
       runConformTest(
            new String[] {
		"TX15cfsbm29Main.java",
			    "\n" +
			    "        public class TX15cfsbm29Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX15cfsbm29_1 t = new TeamX15cfsbm29_1();\n" +
			    "                t.activate();\n" +
			    "                TX15cfsbm29_2.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm29.java",
			    "\n" +
			    "        public class TX15cfsbm29 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"X\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX15cfsbm29_2.java",
			    "\n" +
			    "        public class TX15cfsbm29_2 extends TX15cfsbm29 {\n" +
			    "            public static void bm(){\n" +
			    "                System.out.print(\"OK\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX15cfsbm29_1.java",
			    "\n" +
			    "        public team class TeamX15cfsbm29_1 {\n" +
			    "            public class RX15cfsbm29_1 playedBy TX15cfsbm29 {\n" +
			    "                static callin void srm() {\n" +
			    "                    System.out.print(\"NOT\");\n" +
			    "                    base.srm(); \n" +
			    "                }\n" +
			    "                srm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // we had a problem with signature weakening in team methods across several levels of inheritance, resulting in an "abstract method" error at run-time
    // X.1.6-otjld-abstract-team-method-1
    public void testX16_abstractTeamMethod1() {
       
       runConformTest(
            new String[] {
		"X16atmMain1.java",
			    "\n" +
			    "import p16.*;\n" +
			    "public class X16atmMain1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        X16atm1 b = new X16atm1();\n" +
			    "        TeamX16atm1_2 t = new TeamX16atm1_3();\n" +
			    "        t.activate();\n" +
			    "        b.bm();\n" +
			    "        t.doIt();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p16/X16atm1.java",
			    "\n" +
			    "package p16;    \n" +
			    "public class X16atm1 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p16/TeamX16atm1_1.java",
			    "\n" +
			    "package p16;\n" +
			    "public abstract team class TeamX16atm1_1 {\n" +
			    "    abstract MyRole getRole();\n" +
			    "    abstract protected class MyRole {}\n" +
			    "    public void doIt() {\n" +
			    "      MyRole mr = getRole();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p16/TeamX16atm1_2.java",
			    "\n" +
			    "package p16;\n" +
			    "public abstract team class TeamX16atm1_2 extends TeamX16atm1_1 {\n" +
			    "    @Override\n" +
			    "    public void doIt() {\n" +
			    "        MyRole mr = getRole();\n" +
			    "    }\n" +
			    "    @Override\n" +
			    "    public class MyRole playedBy X16atm1 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p16/TeamX16atm1_3.java",
			    " \n" +
			    "package p16;\n" +
			    "public team class TeamX16atm1_3 extends TeamX16atm1_2 {\n" +
			    "    @Override\n" +
			    "    public MyRole getRole() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // if a team is referenced only indirect it has to be loaded before its bases too
    // X.1.7-otjld-referenced-teams-1
    public void testX17_referencedTeams1() {
       
       runConformTest(
            new String[] {
		"X17rtMain1.java",
			    "\n" +
			    "import p17.*;\n" +
			    "public class X17rtMain1 {\n" +
			    "  public static void main(String[] args) {\n" +
			    "    TeamReferencerX17rt1 tr = null;\n" +
			    "    MyBaseX17rt1 b = new MyBaseX17rt1();\n" +
			    "    tr = new TeamReferencerX17rt1();\n" +
			    "    tr.doIt();\n" +
			    "    b.bm();\n" +
			    "  }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"p17/MyBaseX17rt1.java",
			    "\n" +
			    "package p17;\n" +
			    "public class MyBaseX17rt1 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p17/TeamReferencerX17rt1.java",
			    "\n" +
			    "package p17;\n" +
			    "public class TeamReferencerX17rt1 {\n" +
			    "  public void doIt() {\n" +
			    "    TeamX17rt1 mt = new TeamX17rt1();\n" +
			    "    try {\n" +
			    "        mt.activate();\n" +
			    "    } catch (org.objectteams.UnsupportedFeatureException ufe) {\n" +
			    "        System.out.print(\"N\");\n" +
			    "    }\n" +
			    "  }\n" +
			    "}\n" +
			    "    \n",
		"p17/TeamX17rt1.java",
			    "\n" +
			    "package p17;    \n" +
			    "public team class TeamX17rt1 {\n" +
			    "    public class MyRole playedBy MyBaseX17rt1 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            (this.weavingScheme == WeavingScheme.OTDRE ? "OK" : "NO"));
    }

    // an after callin passes the base result to the team
    // X.1.8-otjld-after-result-passing-1
    public void testX18_afterResultPassing1() {
       
       runConformTest(
            new String[] {
		"X18arpMain1.java",
			    "\n" +
			    "import p18.*;\n" +
			    "public class X18arpMain1 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX18arp1 b = new MyBaseX18arp1();\n" +
			    "        TeamX18arp1 t = new TeamX18arp1();\n" +
			    "        t.activate();\n" +
			    "        b.bm(\"seven\", \"unused\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p18/MyBaseX18arp1.java",
			    "\n" +
			    "package p18;\n" +
			    "public class MyBaseX18arp1 {\n" +
			    "    public int bm(String arg, String arg2) {\n" +
			    "        System.out.println(\"MyBase.bm(\"+arg+\")\");\n" +
			    "        return 77;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p18/TeamX18arp1.java",
			    "\n" +
			    "package p18;    \n" +
			    "public team class TeamX18arp1 {\n" +
			    "    public class MyRole playedBy MyBaseX18arp1 {\n" +
			    "        public void rm(int i) {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm(\"+i+\")\");\n" +
			    "        }\n" +
			    "\n" +
			    "        void rm(int i) <- after int bm(String s, String s2) with {\n" +
			    "            i <- result\n" +
			    "        }\n" +
			    "\n" +
			    "        void rm(int i) <- before int bm(String s, String s2) with {\n" +
			    "            i <- s.length()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm(5)\n" +
			"MyBase.bm(seven)\n" +
			"MyTeam.MyRole.rm(77)");
    }

    // a team is instantiated and activated via a teamconfig file
    // X.1.9-otjld-teamconfig-file-1
    public void testX19_teamconfigFile1() {
       myWriteFiles(
    		new String[] {
        "p19/config1.txt",
			    "\n" +
			    "p19.TeamX19tcf1\n"});	
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"X19tcfMain1.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain1 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf1 b = new MyBaseX19tcf1();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf1.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf1 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf1.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf1 {\n" +
			    "    public class MyRole playedBy MyBaseX19tcf1 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()",
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config1.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a team is instantiated and activated via a teamconfig file - the main method contains an exception handler
    // X.1.9-otjld-teamconfig-file-2
    public void testX19_teamconfigFile2() {
       myWriteFiles(
    		new String[] {
		"p19/config2.txt",
			    "\n" +
			    "p19.TeamX19tcf2\n" +
			    "\n"		
    		});
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"X19tcfMain2.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain2 {\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf2 b = new MyBaseX19tcf2();\n" +
			    "        b.bm(true);\n" +
			    "        try {\n" +
			    "          b.bm(false);\n" +
			    "        } catch (NullPointerException npe) {\n" +
			    "          System.out.println(\"NullPointerException caught!\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf2.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf2 {\n" +
			    "    public void bm(boolean ok) {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "        if (!ok)\n" +
			    "            throw new NullPointerException();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf2.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf2 {\n" +
			    "    public class MyRole playedBy MyBaseX19tcf2 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm()\n" + 
    		"MyBase.bm()\n" + 
    		"MyTeam.MyRole.rm()\n" + 
    		"MyTeam.MyRole.rm()\n" + 
    		"MyBase.bm()\n" + 
    		"NullPointerException caught!",
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config2.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a team is instantiated and activated via a teamconfig file, bindings are in the super team
    // X.1.9-otjld-teamconfig-file-3
    public void testX19_teamconfigFile3() {
       myWriteFiles(new String[] {
		"configX19tcf3.txt",
			    "\n" +
			    "TeamX19tcf3_2    \n" +
			    "    \n"});
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"TX19tcf3Main.java",
			    "\n" +
			    "public class TX19tcf3Main {\n" +
			    "    static TX19tcf3 b;\n" +
			    "    public static void main(String[] args) {\n" +
			    "        b = new TX19tcf3();\n" +
			    "        b.nop();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"TX19tcf3.java",
			    "\n" +
			    "public team class TX19tcf3 {\n" +
			    "    void nop() {}\n" +
			    "}    \n" +
			    "    \n",
		"TeamX19tcf3_1.java",
			    "\n" +
			    "public team class TeamX19tcf3_1 {\n" +
			    "    protected class RX19tcf3_1 {\n" +
			    "        void out() {}\n" +
			    "    }\n" +
			    "    protected class RX19tcf3_2 extends RX19tcf3_1 playedBy TX19tcf3 {\n" +
			    "        out <- after nop;\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"TeamX19tcf3_2.java",
			    "\n" +
			    "public team class TeamX19tcf3_2 extends TeamX19tcf3_1 {\n" +
			    "    protected class RX19tcf3_1 {\n" +
			    "        void out() { System.out.print(\"OK\"); }\n" +
			    "    }    \n" +
			    "}    \n" +
			    "    \n"
            },
            "OK",
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("configX19tcf3.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // trying to instantiate and activate a team without a default constructor via a teamconfig file
    // X.1.9-otjld-teamconfig-file-4
    public void testX19_teamconfigFile4() {
       Map customOptions = getCompilerOptions();
       myWriteFiles(new String[] {
		"p19/config4.txt",
			    "\n" +
			    "p19.TeamX19tcf4\n" +
			    "\n"
       });
       runTest(
            new String[] {
		"X19tcfMain4.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain4 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf4 b = new MyBaseX19tcf4();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf4.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf4 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf4.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf4 {\n" +
			    "    public TeamX19tcf4(String anArg) {\n" +
			    "    }\n" +
			    "\n" +
			    "    public class MyRole playedBy MyBaseX19tcf4 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            false/*expectingCompileErrors*/,
            null/*expectedCompilerLog*/,
            "MyBase.bm()"/*expectedOutputString*/,
            "Activation failed: Team class 'p19.TeamX19tcf4' has no default constuctor!",
            false/*forceExecution*/,
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config4.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/,
            true/*skipJavac*/);
    }

    // trying to add a nonexisting team via a teamconfig file
    // X.1.9-otjld-teamconfig-file-5
    public void testX19_teamconfigFile5() {
       Map customOptions = getCompilerOptions();
       myWriteFiles(new String[] {
		"p19/config5.txt",
			    "\n" +
			    "p19.TeamX19tcf5NOTEXISTING\n" +
			    "\n"});
       runTest(
            new String[] {
		"X19tcfMain5.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain5 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf5 b = new MyBaseX19tcf5();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf5.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf5 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf5.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf5 {\n" +
			    "    public class MyRole playedBy MyBaseX19tcf5 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            false/*expectingCompileErrors*/,
            null/*expectedCompilerLog*/,
            "MyBase.bm()"/*expectedOutputString*/,
    		"Config error: Team class \'p19.TeamX19tcf5NOTEXISTING\' in config file \'"+OUTPUT_DIR+"/p19/config5.txt\' can not be found!" /*exepectedErrorString*/,
            false/*forceExecution*/,
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config5.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/,
            true/*skipJavac*/);
    }

    // trying to add a nonexisting team via a teamconfig file, also adding an existing team
    // X.1.9-otjld-teamconfig-file-5a
    public void testX19_teamconfigFile5a() {
       Map customOptions = getCompilerOptions();
       myWriteFiles(new String[] {
		"p19/config5a.txt",
			    "\n" +
			    "p19.TeamX19tcf5aNOTEXISTING\n" +
			    "p19.TeamX19tcf5a\n" +
			    "\n"});
       runTest(
            new String[] {
		"X19tcfMain5a.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain5a {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf5a b = new MyBaseX19tcf5a();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf5a.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf5a {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf5a.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf5a {\n" +
			    "    public class MyRole playedBy MyBaseX19tcf5a {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            false/*expectingCompileErrors*/,
            null/*expectedCompilerLog*/,
            "MyTeam.MyRole.rm()\n" +
            "MyBase.bm()\n" +
            "MyTeam.MyRole.rm()"/*expectedOutputString*/,
            "Config error: Team class 'p19.TeamX19tcf5aNOTEXISTING' in config file '"+OUTPUT_DIR+"/p19/config5a.txt' can not be found!",
            false/*forceExecution*/,
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config5a.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/,
            true/*skipJavac*/);
    }

    // trying to use a nonexisting teamconfig file
    // X.1.9-otjld-teamconfig-file-6
    public void testX19_teamconfigFile6() {
       Map customOptions = getCompilerOptions();
       runTest(
            new String[] {
		"X19tcfMain6.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain6 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf6 b = new MyBaseX19tcf6();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf6.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf6 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            false/*expectingCompileErrors*/,
            null/*expectedCompilerLog*/,
            "MyBase.bm()"/*expectedOutputString*/,
            "File input error: config file '"+OUTPUT_DIR+"/p19/config6.txt' can not be found!"/*expectedErrorString*/,
            false/*forceExecution*/,
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config6.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/,
            true/*skipJavac*/);
    }

    // using comments in a teamconfig file
    // X.1.9-otjld-teamconfig-file-7
    public void testX19_teamconfigFile7() {
       Map customOptions = getCompilerOptions();
       myWriteFiles(new String[] {
		"p19/config7.txt",
			    "\n" +
			    "# this is a comment \n" +
			    "p19.TeamX19tcf7\n" +
			    "# here is another comment\n" +
			    "\n"});
       runConformTest(
            new String[] {
		"X19tcfMain7.java",
			    "\n" +
			    "import p19.*;\n" +
			    "public class X19tcfMain7 {\n" +
			    "\n" +
			    " public static void main(String[] args) {\n" +
			    "        MyBaseX19tcf7 b = new MyBaseX19tcf7();\n" +
			    "        b.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/MyBaseX19tcf7.java",
			    "\n" +
			    "package p19;\n" +
			    "public class MyBaseX19tcf7 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.println(\"MyBase.bm()\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p19/TeamX19tcf7.java",
			    "\n" +
			    "package p19;    \n" +
			    "public team class TeamX19tcf7 {\n" +
			    "    public class MyRole playedBy MyBaseX19tcf7 {\n" +
			    "        public void rm() {\n" +
			    "            System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "        }\n" +
			    "        rm <- before bm;\n" +
			    "        rm <- after bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "MyTeam.MyRole.rm()\n" +
			"MyBase.bm()\n" +
			"MyTeam.MyRole.rm()",
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            getTeamActivationVMArgs("p19/config7.txt")/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a sub base is bound to another team
    // X.2.1-otjld-callin-with-base-inheritance-1
    public void testX21_callinWithBaseInheritance1() {
       
       runConformTest(
            new String[] {
		"TX21cwbi1Main.java",
			    "\n" +
			    "        public class TX21cwbi1Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX21cwbi1_1 t1 = new TeamX21cwbi1_1();\n" +
			    "                TeamX21cwbi1_2 t2 = new TeamX21cwbi1_2();\n" +
			    "                t1.activate();\n" +
			    "                t2.activate();\n" +
			    "                TX21cwbi1_2 b = new TX21cwbi1_2();\n" +
			    "                b.bm2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi1.java",
			    "\n" +
			    "        public class TX21cwbi1 {\n" +
			    "            public void bm(){}\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi1_2.java",
			    "\n" +
			    "        public class TX21cwbi1_2 extends TX21cwbi1 {\n" +
			    "            public void bm2(){}\n" +
			    "        }\n" +
			    "    \n",
		"TeamX21cwbi1_1.java",
			    "\n" +
			    "        public team class TeamX21cwbi1_1 {\n" +
			    "            public class RX21cwbi1_1 playedBy TX21cwbi1 {\n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX21cwbi1_2.java",
			    "\n" +
			    "        public team class TeamX21cwbi1_2 {\n" +
			    "            public class RX21cwbi1_2 playedBy TX21cwbi1_2 {\n" +
			    "                public void rm() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                }                \n" +
			    "                rm <- before bm2;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // a sub base is bound to another role, the super base should not be affected
    // X.2.1-otjld-callin-with-base-inheritance-2
    public void testX21_callinWithBaseInheritance2() {
       
       runConformTest(
            new String[] {
		"TX21cwbi2Main.java",
			    "\n" +
			    "        public class TX21cwbi2Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX21cwbi2_2 t2 = new TeamX21cwbi2_2();\n" +
			    "                t2.activate();\n" +
			    "                TX21cwbi2 b = new TX21cwbi2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi2.java",
			    "\n" +
			    "        public class TX21cwbi2 {\n" +
			    "            public void bm(){\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi2_2.java",
			    "\n" +
			    "        public class TX21cwbi2_2 extends TX21cwbi2 {}\n" +
			    "    \n",
		"TeamX21cwbi2_1.java",
			    "\n" +
			    "        public team class TeamX21cwbi2_1 {\n" +
			    "            public class RX21cwbi2_1 playedBy TX21cwbi2 {\n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX21cwbi2_2.java",
			    "\n" +
			    "        public team class TeamX21cwbi2_2 extends TeamX21cwbi2_1 {\n" +
			    "            public class RX21cwbi2_2 extends RX21cwbi2_1 playedBy TX21cwbi2_2 {}\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // object creation (invokespecial != super) in overridden callin bound base method
    // X.2.1-otjld-callin-with-base-inheritance-3
    public void testX21_callinWithBaseInheritance3() {
       
       runConformTest(
            new String[] {
		"TX21cwbi3Main.java",
			    "\n" +
			    "        public class TX21cwbi3Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX21cwbi3_1 t = new TeamX21cwbi3_1();\n" +
			    "                t.activate();\n" +
			    "                TX21cwbi3_2 b = new TX21cwbi3_2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi3.java",
			    "\n" +
			    "        public class TX21cwbi3 {\n" +
			    "            public void bm(){\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi3_2.java",
			    "\n" +
			    "        public class TX21cwbi3_2 extends TX21cwbi3 {\n" +
			    "            public void bm() {\n" +
			    "                String s = new String(\"O\");\n" +
			    "                System.out.print(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX21cwbi3_1.java",
			    "\n" +
			    "        public team class TeamX21cwbi3_1 {\n" +
			    "            public class RX21cwbi3_1 playedBy TX21cwbi3 {\n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // overridden callin bound base method with arguments
    // X.2.1-otjld-callin-with-base-inheritance-4
    public void testX21_callinWithBaseInheritance4() {
       
       runConformTest(
            new String[] {
		"TX21cwbi4Main.java",
			    "\n" +
			    "        public class TX21cwbi4Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX21cwbi4_1 t = new TeamX21cwbi4_1();\n" +
			    "                t.activate();\n" +
			    "                TX21cwbi4_2 b = new TX21cwbi4_2();\n" +
			    "                b.bm(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi4.java",
			    "\n" +
			    "        public class TX21cwbi4 {\n" +
			    "            public void bm(String s){\n" +
			    "                System.out.print(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX21cwbi4_2.java",
			    "\n" +
			    "        public class TX21cwbi4_2 extends TX21cwbi4 {\n" +
			    "            public void bm(String s) {\n" +
			    "                if (s.startsWith(\"NOT\")) {\n" +
			    "                    s = s.substring(3, s.length());\n" +
			    "                }\n" +
			    "                super.bm(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX21cwbi4_1.java",
			    "\n" +
			    "        public team class TeamX21cwbi4_1 {\n" +
			    "            public class RX21cwbi4_1 playedBy TX21cwbi4 {\n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // static replace binding with refinement of 'playedBy' in the same team + base method redefinition
    // X.2.2-otjld-played-by-refinement-1
    public void testX22_playedByRefinement1() {
       
       runConformTest(
            new String[] {
		"TX22pbr1Main.java",
			    "\n" +
			    "        public class TX22pbr1Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX22pbr1 t = new TeamX22pbr1();\n" +
			    "                t.activate();\n" +
			    "                TX22pbr1.bm();\n" +
			    "                TX22pbr1_2.bm();\n" +
			    "               \n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr1.java",
			    "\n" +
			    "        public class TX22pbr1 {\n" +
			    "            public static void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr1_2.java",
			    "\n" +
			    "        public class TX22pbr1_2 extends TX22pbr1 {\n" +
			    "            public static void bm() {\n" +
			    "                System.out.print(\"AY\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr1.java",
			    "\n" +
			    "        public team class TeamX22pbr1 {  \n" +
			    "            public class RX22pbr1 playedBy TX22pbr1 { \n" +
			    "                static callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "            public class RX122br1_2 extends RX22pbr1 playedBy TX22pbr1_2 { \n" +
			    "                static callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"NOT\");          \n" +
			    "                }\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "OKAY");
    }

    // static replace binding with refinement of 'playedBy' in a subteam + base method redefinition
    // X.2.2-otjld-played-by-refinement-2
    public void testX22_playedByRefinement2() {
       
       runConformTest(
            new String[] {
		"TX22pbr2Main.java",
			    "\n" +
			    "        public class TX22pbr2Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX22pbr2_2 t = new TeamX22pbr2_2();\n" +
			    "                t.activate();\n" +
			    "                TX22pbr2.bm();\n" +
			    "                TX22pbr2_2.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr2.java",
			    "\n" +
			    "        public class TX22pbr2 {\n" +
			    "            public static void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr2_2.java",
			    "\n" +
			    "        public class TX22pbr2_2 extends TX22pbr2 {\n" +
			    "            public static void bm() {\n" +
			    "                System.out.print(\"AY\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr2.java",
			    "\n" +
			    "        public team class TeamX22pbr2 {  \n" +
			    "            public class RX22pbr2 playedBy TX22pbr2 { \n" +
			    "                static callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr2_2.java",
			    "\n" +
			    "        public team class TeamX22pbr2_2 extends TeamX22pbr2 {  \n" +
			    "            public class RX22pbr2_2 extends RX22pbr2 playedBy TX22pbr2_2 { \n" +
			    "                static callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"NOT\");          \n" +
			    "                }\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "OKAY");
    }

    // replace binding with refinement of 'playedBy' in the same team + base method redefinition
    // X.2.2-otjld-played-by-refinement-3
    public void testX22_playedByRefinement3() {
       
       runConformTest(
            new String[] {
		"TX22pbr3Main.java",
			    "\n" +
			    "        public class TX22pbr3Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX22pbr3 t = new TeamX22pbr3();\n" +
			    "                t.activate();\n" +
			    "                TX22pbr3_2 b = new TX22pbr3_2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr3.java",
			    "\n" +
			    "        public class TX22pbr3 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr3_2.java",
			    "\n" +
			    "        public class TX22pbr3_2 extends TX22pbr3 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr3.java",
			    "\n" +
			    "        public team class TeamX22pbr3 {  \n" +
			    "            public class RX22pbr3 playedBy TX22pbr3 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "            public class RX22pbr3_2 extends RX22pbr3 playedBy TX22pbr3_2 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // replace binding with refinement of 'playedBy' in a subteam + base method redefinition
    // X.2.2-otjld-played-by-refinement-4
    public void testX22_playedByRefinement4() {
       
       runConformTest(
            new String[] {
		"TX22pbr4Main.java",
			    "\n" +
			    "        public class TX22pbr4Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX22pbr4_2 t = new TeamX22pbr4_2();\n" +
			    "                t.activate();\n" +
			    "                TX22pbr4_2 b = new TX22pbr4_2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr4.java",
			    "\n" +
			    "        public class TX22pbr4 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr4_2.java",
			    "\n" +
			    "        public class TX22pbr4_2 extends TX22pbr4 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr4.java",
			    "\n" +
			    "        public team class TeamX22pbr4 {  \n" +
			    "            public class RX22pbr4 playedBy TX22pbr4 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr4_2.java",
			    "\n" +
			    "        public team class TeamX22pbr4_2 extends TeamX22pbr4 {  \n" +
			    "            public class RX22pbr4_2 extends RX22pbr4 playedBy TX22pbr4_2 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // replace binding with refinement of 'playedBy' in a subteam, in an implicit subrole + base method redefinition
    // X.2.2-otjld-played-by-refinement-5
    public void testX22_playedByRefinement5() {
       
       runConformTest(
            new String[] {
		"TX22pbr5Main.java",
			    "\n" +
			    "        public class TX22pbr5Main {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                TeamX22pbr5_2 t = new TeamX22pbr5_2();\n" +
			    "                t.activate();\n" +
			    "                TX22pbr5_2 b = new TX22pbr5_2();\n" +
			    "                b.bm();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr5.java",
			    "\n" +
			    "        public class TX22pbr5 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX22pbr5_2.java",
			    "\n" +
			    "        public class TX22pbr5_2 extends TX22pbr5 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr5.java",
			    "\n" +
			    "        public team class TeamX22pbr5 {  \n" +
			    "            public class RX22pbr5 playedBy TX22pbr5 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TeamX22pbr5_2.java",
			    "\n" +
			    "        public team class TeamX22pbr5_2 extends TeamX22pbr5 {  \n" +
			    "            public class RX22pbr5 { \n" +
			    "                callin void rm() {\n" +
			    "                    tsuper.rm();\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n"
            },
            "OXOK");
    }

    // mulitple replace bindings from different roles in the same team to one base method -> compiler should enforce precedence declaration
    // X.2.3-otjld-multiple-bindings-1
    public void testX23_multipleBindings1() {
        runNegativeTestMatching(
            new String[] {
		"TeamX23mb1.java",
			    "\n" +
			    "        public team class TeamX23mb1 {  \n" +
			    "            public class RX23mb1 playedBy TX23mb1 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"X\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "            public class RX23mb1_2 playedBy TX23mb1_2 { \n" +
			    "                callin void rm() {\n" +
			    "                    base.rm();\n" +
			    "                    System.out.print(\"K\");          \n" +
			    "                }\n" +
			    "                rm <- replace bm;\n" +
			    "            } \n" +
			    "        }\n" +
			    "    \n",
		"TX23mb1.java",
			    "\n" +
			    "        public class TX23mb1 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"NOTO\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"TX23mb1_2.java",
			    "\n" +
			    "        public class TX23mb1_2 extends TX23mb1 {\n" +
			    "            public void bm() {\n" +
			    "                System.out.print(\"O\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "4.8");
    }

    
    String[] getBookingClassLibraries() {
		if (this.verifier != null)
			this.verifier.shutDown();
        this.verifier = getTestVerifier(false);
        this.createdVerifier = true;

    	String jarFilename = "booking.jar";
    	String destPath = this.outputRootDirectoryPath+"/regression";
    	createOutputTestDirectory("/regression");
    	// upload the jar:
		Util.copy(getTestResourcePath(jarFilename), destPath);
    	// setup classpath:
    	String[] classPaths = getDefaultClassPaths();
    	int l = classPaths.length;
    	System.arraycopy(classPaths, 0, classPaths=new String[l+1], 0, l);
		classPaths[l] = this.outputRootDirectoryPath+"/regression/"+jarFilename;
		return classPaths;
    }

    // the flightbonus sample as shipped with the OTDT, compilation only (interactive GUI application!)
    // X.3.1-otjld-flightbonus-sample
    public void testX31_flightbonusSample() {
        runConformTest(
            new String[] {
        "fbapplication/DummyMain.java",
        		"package fbapplication;\n" +
        		"public class DummyMain {\n" +
        		"	public static void main(String... args) {\n" +
        		"		new GUIConnector().activate();\n" +
        		"		System.out.print(\"OK\");\n" +
        		"	}\n" +
        		"}\n",
		"fbapplication/FlightBonus.java",
			    "\n" +
			    "package fbapplication;\n" +
			    "import bonussystem.Bonus;\n" +
			    "@SuppressWarnings(\"bindingconventions\")\n" +
			    "public team class FlightBonus extends Bonus {\n" +
			    "    public FlightBonus(flightbooking.model.Passenger as Subscriber s) {\n" +
			    "        activate();\n" +
			    "    }\n" +
			    "   @Override\n" +
			    "	public class Subscriber playedBy flightbooking.model.Passenger \n" +
			    "		base when (FlightBonus.this.hasRole(base)) \n" +
			    "	{\n" +
			    "		buy     <- replace book;\n" +
			    "		String getName() -> String getName();\n" +
			    "		public Subscriber(flightbooking.model.Passenger p) {\n" +
			    "			tsuper();\n" +
			    "		}\n" +
			    "	};\n" +
			    "   @Override\n" +
			    "	public class Item playedBy flightbooking.model.Segment {\n" +
			    "       @Override\n" +
			    "		public int calculateCredit () {\n" +
			    "			int miles = getDistance();\n" +
			    "			int credit = (((miles+999)/1000)) * 1000;\n" +
			    "			return credit;\n" +
			    "		};\n" +
			    "		earnCredit     <- after book;\n" +
			    "		String getDestination() -> String getDestination();\n" +
			    "		int getDistance() -> int getDistance();\n" +
			    "		String getStart() -> String getStart();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"fbapplication/BonusGUI.java",
			    "\n" +
			    "package fbapplication;\n" +
			    "import java.awt.Component;\n" +
			    "import javax.swing.JOptionPane;\n" +
			    "import flightbooking.model.Passenger;\n" +
			    "public team class BonusGUI {\n" +
			    "	View view = null;\n" +
			    "    abstract protected class View  {\n" +
			    "    	void registerView() {\n" +
			    "    		BonusGUI.this.view = this;\n" +
			    "    	}\n" +
			    "    	protected boolean queryRegisterDialog() {\n" +
			    "    		int choice = JOptionPane.showConfirmDialog(\n" +
			    "			    				getComponent(), \n" +
			    "								\"Register for Flight Bonus?\", \n" +
			    "			    				\"OT Bonus System\", \n" +
			    "								JOptionPane.YES_NO_OPTION);\n" +
			    "    		return choice == JOptionPane.YES_OPTION;\n" +
			    "    	}\n" +
			    "    	protected abstract Component getComponent();\n" +
			    "    }\n" +
			    "    protected class Controller \n" +
			    "		when (BonusGUI.this.view != null) \n" +
			    "	{\n" +
			    "		void queryRegisterForBonus (Passenger p) {\n" +
			    "			if (BonusGUI.this.view.queryRegisterDialog()) \n" +
			    "				new FlightBonusDialog(new FlightBonus(p));\n" +
			    "		};\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"bonussystem/Bonus.java",
			    "\n" +
			    "package bonussystem;\n" +
			    "public abstract team class Bonus {\n" +
			    "        protected Subscriber subscriber = null;\n" +
			    "        public abstract class Subscriber {\n" +
			    "            private int collectedCredits = 0;\n" +
			    "            protected int getCollectedCredits() {\n" +
			    "                return collectedCredits;\n" +
			    "            }\n" +
			    "            protected void collectCredit (int credit) {\n" +
			    "                        collectedCredits += credit;\n" +
			    "            }\n" +
			    "            callin void buy ()\n" +
			    "            {\n" +
			    "                        Bonus.this.subscriber = this;\n" +
			    "                        base.buy();\n" +
			    "                        Bonus.this.subscriber = null;\n" +
			    "\n" +
			    "                        log(\"Sub. has collected  \"+getCollectedCredits()+\" credit points.\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public abstract class Item {\n" +
			    "            protected abstract int calculateCredit ();\n" +
			    "            protected void earnCredit () {\n" +
			    "						 @SuppressWarnings(\"hiding\")\n" +
			    "                        Subscriber subscriber = Bonus.this.subscriber;\n" +
			    "                        if (subscriber == null)\n" +
			    "                                return;\n" +
			    "\n" +
			    "                        int tmpCredits = calculateCredit();\n" +
			    "                        log(\"buying an item that yields \"+tmpCredits+\" credit points.\");\n" +
			    "                        subscriber.collectCredit(tmpCredits);\n" +
			    "            }\n" +
			    "        }\n" +
			    "        protected void log (String msg) {\n" +
			    "                System.out.println(\">>Bonus>> \"+msg);\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"fbapplication/GUIConnector.java",
			    "\n" +
			    "package fbapplication;\n" +
			    "import java.awt.Component;\n" +
			    "@SuppressWarnings(\"bindingconventions\")\n" +
			    "public team class GUIConnector extends BonusGUI {\n" +
			    "    @Override\n" +
			    "    protected class View playedBy flightbooking.gui.FlightBookingGUI {\n" +
			    "       @Override\n" +
			    "    	protected Component getComponent () {\n" +
			    "    		return this;\n" +
			    "    	}\n" +
			    "    	registerView                 <- after  initComponents;\n" +
			    "    }\n" +
			    "    @Override\n" +
			    "    protected class Controller playedBy flightbooking.model.PassengerDB {\n" +
			    "		queryRegisterForBonus        <- after  add;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
				"fbapplication/GUIConnector/FlightBonusDialog.java",
			    "\n" +
			    "team package fbapplication.GUIConnector;\n" +
			    "@SuppressWarnings(\"roletypesyntax\")\n" +
			    "protected team class FlightBonusDialog playedBy FlightBonus {\n" +
			    "	protected class Collector playedBy base.Item {\n" +
			    "		recordCredits            <- replace calculateCredit;\n" +
			    "		getDestination           ->         getDestination;\n" +
			    "		getStart                 ->         getStart;\n" +
			    "   	}\n" +
			    "	@SuppressWarnings(\"decapsulation\") // getCollectedCredits\n" +
			    "	protected class Message playedBy base.Subscriber {\n" +
			    "		getTotalCollectedCredits ->        getCollectedCredits;\n" +
			    "		getName                  ->        getName;\n" +
			    "		showBonusDialog          <- after  buy;\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"fbapplication/BonusGUI/FlightBonusDialog.java",
			    "\n" +
			    "team package fbapplication.BonusGUI;\n" +
			    "protected team class FlightBonusDialog playedBy FlightBonus {\n" +
			    "        String message;	\n" +
			    "        public FlightBonusDialog(FlightBonus fb) {\n" +
			    "                this.initializeMessage(0);\n" +
			    "                this.activate();\n" +
			    "                System.out.println(\"FBDialog \");\n" +
			    "        }\n" +
			    "        void initializeMessage(int credits) {\n" +
			    "                this.message = new String(\"Collected credits in the past: \"+credits+\"\\n\");\n" +
			    "        }\n" +
			    "        protected abstract class Collector {\n" +
			    "                public abstract String getStart();\n" +
			    "                public abstract String getDestination();\n" +
			    "                callin int recordCredits() {\n" +
			    "                        int credits = base.recordCredits();\n" +
			    "                        FlightBonusDialog.this.message += \"FlightSegment: \\n\";\n" +
			    "                        FlightBonusDialog.this.message += \"    \"+this.getStart()+\"-->\"+this.getDestination()+\"\\n\";\n" +
			    "                        FlightBonusDialog.this.message += \"    earning credit: \"+credits+\"\\n\";\n" +
			    "                        return credits;\n" +
			    "                }\n" +
			    "        }\n" +
			    "        protected abstract class Message {\n" +
			    "                abstract int getTotalCollectedCredits();\n" +
			    "                abstract String getName();\n" +
			    "                public void showBonusDialog() {\n" +
			    "                        int currentCredits = this.getTotalCollectedCredits();\n" +
			    "                        String title = \"Bonus message for Passenger \"+this.getName(); \n" +
			    "                        FlightBonusDialog.this.message += new String (\"Collected credits now: \"+currentCredits);\n" +
			    "                        JOptionPane.showMessageDialog(\n" +
			    "                                                        BonusGUI.this.view.getComponent(), \n" +
			    "                                                        FlightBonusDialog.this.message, \n" +
			    "                                                        title, \n" +
			    "                                                        JOptionPane.INFORMATION_MESSAGE);\n" +
			    "                        FlightBonusDialog.this.initializeMessage(currentCredits);\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK",
            getBookingClassLibraries(),
            false/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/);
    }

    // the flightbonus sample as shipped with the OTDT, compilation only (interactive GUI application!) - parameterized type syntax
    // X.3.1-otjld-flightbonus-sample-2
    public void testX31_flightbonusSample2() {
        runConformTest(
            new String[] {
        "fbapplication2/DummyMain.java",
        		"package fbapplication2;\n" +
        		"public class DummyMain {\n" +
        		"	public static void main(String... args) {\n" +
        		"		new GUIConnector().activate();\n" +
        		"		System.out.print(\"OK\");\n" +
        		"	}\n" +
        		"}\n",
		"fbapplication2/FlightBonus.java",
			    "\n" +
			    "package fbapplication2;\n" +
			    "import bonussystem2.Bonus;\n" +
			    "import base flightbooking.model.Passenger;\n" +
			    "import base flightbooking.model.Segment;\n" +
			    "public team class FlightBonus extends Bonus {\n" +
			    "    public FlightBonus(Passenger as Subscriber s) {\n" +
			    "        activate();\n" +
			    "    }\n" +
			    "   @Override\n" +
			    "	public class Subscriber playedBy Passenger \n" +
			    "		base when (FlightBonus.this.hasRole(base)) \n" +
			    "	{\n" +
			    "		buy     <- replace book;\n" +
			    "		String getName() -> String getName();\n" +
			    "		public Subscriber(Passenger p) {\n" +
			    "			tsuper();\n" +
			    "		}\n" +
			    "	};\n" +
			    "   @Override\n" +
			    "	public class Item playedBy Segment {\n" +
			    "       @Override\n" +
			    "		int calculateCredit () {\n" +
			    "			int miles = getDistance();\n" +
			    "			int credit = (((miles+999)/1000)) * 1000;\n" +
			    "			return credit;\n" +
			    "		};\n" +
			    "		earnCredit     <- after book;\n" +
			    "		String getDestination() -> String getDestination();\n" +
			    "		int getDistance() -> int getDistance();\n" +
			    "		String getStart() -> String getStart();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"fbapplication2/BonusGUI.java",
			    "\n" +
			    "package fbapplication2;\n" +
			    "import java.awt.Component;\n" +
			    "import javax.swing.JOptionPane;\n" +
			    "import flightbooking.model.Passenger;\n" +
			    "public team class BonusGUI {\n" +
			    "	View view = null;\n" +
			    "    abstract protected class View  {\n" +
			    "    	void registerView() {\n" +
			    "    		BonusGUI.this.view = this;\n" +
			    "    	}\n" +
			    "    	protected boolean queryRegisterDialog() {\n" +
			    "    		int choice = JOptionPane.showConfirmDialog(\n" +
			    "			    				getComponent(), \n" +
			    "								\"Register for Flight Bonus?\", \n" +
			    "			    				\"OT Bonus System\", \n" +
			    "								JOptionPane.YES_NO_OPTION);\n" +
			    "    		return choice == JOptionPane.YES_OPTION;\n" +
			    "    	}\n" +
			    "    	protected abstract Component getComponent();\n" +
			    "    }\n" +
			    "    protected class Controller \n" +
			    "		when (BonusGUI.this.view != null) \n" +
			    "	{\n" +
			    "		void queryRegisterForBonus (Passenger p) {\n" +
			    "			if (BonusGUI.this.view.queryRegisterDialog()) \n" +
			    "				new FlightBonusDialog(new FlightBonus(p));\n" +
			    "		};\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"bonussystem2/Bonus.java",
			    "\n" +
			    "package bonussystem2;\n" +
			    "public abstract team class Bonus {\n" +
			    "        protected Subscriber subscriber = null;\n" +
			    "        public abstract class Subscriber {\n" +
			    "            private int collectedCredits = 0;\n" +
			    "            protected int getCollectedCredits() {\n" +
			    "                return collectedCredits;\n" +
			    "            }\n" +
			    "            protected void collectCredit (int credit) {\n" +
			    "                        collectedCredits += credit;\n" +
			    "            }\n" +
			    "            callin void buy ()\n" +
			    "            {\n" +
			    "                        Bonus.this.subscriber = this;\n" +
			    "                        base.buy();\n" +
			    "                        Bonus.this.subscriber = null;\n" +
			    "\n" +
			    "                        log(\"Sub. has collected  \"+getCollectedCredits()+\" credit points.\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public abstract class Item {\n" +
			    "            abstract int calculateCredit ();\n" +
			    "            protected void earnCredit () {\n" +
			    "                        Subscriber subscriber = Bonus.this.subscriber;\n" +
			    "                        if (subscriber == null)\n" +
			    "                                return;\n" +
			    "\n" +
			    "                        int tmpCredits = calculateCredit();\n" +
			    "                        log(\"buying an item that yields \"+tmpCredits+\" credit points.\");\n" +
			    "                        subscriber.collectCredit(tmpCredits);\n" +
			    "            }\n" +
			    "        }\n" +
			    "        protected void log (String msg) {\n" +
			    "                System.out.println(\">>Bonus>> \"+msg);\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"fbapplication2/GUIConnector.java",
			    "\n" +
			    "package fbapplication2;\n" +
			    "import java.awt.Component;\n" +
			    "import base flightbooking.gui.FlightBookingGUI;\n" +
			    "import base flightbooking.model.PassengerDB;\n" +
			    "public team class GUIConnector extends BonusGUI {\n" +
			    "    @Override\n" +
			    "    protected class View playedBy FlightBookingGUI {\n" +
			    "    	protected Component getComponent () {\n" +
			    "    		return this;\n" +
			    "    	}\n" +
			    "    	registerView                 <- after  initComponents;\n" +
			    "    }\n" +
			    "    @Override\n" +
			    "    protected class Controller playedBy PassengerDB {\n" +
			    "		queryRegisterForBonus        <- after  add;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"fbapplication2/GUIConnector/FlightBonusDialog.java",
			    "\n" +
			    "team package fbapplication2.GUIConnector;\n" +
			    "@Override\n" +
			    "protected team class FlightBonusDialog playedBy FlightBonus {\n" +
			    "	@Override @SuppressWarnings(\"decapsulation\") // base calls to callin\n" +
			    "	protected class Collector playedBy Item<@FlightBonusDialog.base> {\n" +
			    "		recordCredits            <- replace calculateCredit;\n" +
			    "		getDestination           ->         getDestination;\n" +
			    "		getStart                 ->         getStart;\n" +
			    "   	}\n" +
			    "	@Override @SuppressWarnings(\"decapsulation\") // getCollectedCredits\n" +
			    "	protected class Message playedBy Subscriber<@FlightBonusDialog.base> {\n" +
			    "		getTotalCollectedCredits ->        getCollectedCredits;\n" +
			    "		getName                  ->        getName;\n" +
			    "		showBonusDialog          <- after  buy;\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"fbapplication2/BonusGUI/FlightBonusDialog.java",
			    "\n" +
			    "team package fbapplication2.BonusGUI;\n" +
			    "protected team class FlightBonusDialog playedBy FlightBonus {\n" +
			    "        String message;	\n" +
			    "        public FlightBonusDialog(FlightBonus fb) {\n" +
			    "                this.initializeMessage(0);\n" +
			    "                this.activate();\n" +
			    "                System.out.println(\"FBDialog \");\n" +
			    "        }\n" +
			    "        void initializeMessage(int credits) {\n" +
			    "                this.message = new String(\"Collected credits in the past: \"+credits+\"\\n\");\n" +
			    "        }\n" +
			    "        protected abstract class Collector {\n" +
			    "                public abstract String getStart();\n" +
			    "                public abstract String getDestination();\n" +
			    "                callin int recordCredits() {\n" +
			    "                        int credits = base.recordCredits();\n" +
			    "                        FlightBonusDialog.this.message += \"FlightSegment: \\n\";\n" +
			    "                        FlightBonusDialog.this.message += \"    \"+this.getStart()+\"-->\"+this.getDestination()+\"\\n\";\n" +
			    "                        FlightBonusDialog.this.message += \"    earning credit: \"+credits+\"\\n\";\n" +
			    "                        return credits;\n" +
			    "                }\n" +
			    "        }\n" +
			    "        protected abstract class Message {\n" +
			    "                abstract int getTotalCollectedCredits();\n" +
			    "                abstract String getName();\n" +
			    "                public void showBonusDialog() {\n" +
			    "                        int currentCredits = this.getTotalCollectedCredits();\n" +
			    "                        String title = \"Bonus message for Passenger \"+this.getName(); \n" +
			    "                        FlightBonusDialog.this.message += new String (\"Collected credits now: \"+currentCredits);\n" +
			    "                        JOptionPane.showMessageDialog(\n" +
			    "                                                        BonusGUI.this.view.getComponent(), \n" +
			    "                                                        FlightBonusDialog.this.message, \n" +
			    "                                                        title, \n" +
			    "                                                        JOptionPane.INFORMATION_MESSAGE);\n" +
			    "                        FlightBonusDialog.this.initializeMessage(currentCredits);\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK",
            getBookingClassLibraries(),
            false/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/);
    }

    // a team extends a non-team class - note: "-g" caused an overflow of non-wide constants
    // X.4.1-otjld-team-extends-regular-1
    public void testX41_teamExtendsRegular1() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
       customOptions.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
       customOptions.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);        
       runConformTest(
            new String[] {
		"TeamX41ter1.java",
			    "\n" +
			    "public team class TeamX41ter1 extends TX41ter1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        org.objectteams.ITeam t = new TeamX41ter1();\n" +
			    "        t.activate(org.objectteams.Team.ALL_THREADS);\n" +
			    "        if (t.isActive());\n" +
			    "            System.out.print(t);\n" +
			    "        t.deactivate();\n" +
			    "        System.out.print(t.isActive());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"TX41ter1.java",
			    "\n" +
			    "public class TX41ter1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OKfalse",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    public void testBug487037() {
    	runConformTest(
    		new String[] {
		"Main.java",
				"public class Main {\n" +
				"	public static void main(String... args) {\n" +
				"		new TeamFoo().activate();\n" +
				"		new b.B1().test();\n" +
				"		new b.B1b();\n" +
				"		new b.B2().test();\n" +
				"	}\n" +
				"}\n",
		"b/B1.java",
    			"package b;\n" +
    			"public class B1 {\n" +
    			"	void bm1() {}\n" +
    			"	void bm2() {}\n" +
    			"	void bm3() {}\n" +
    			"	void bm4() {}\n" +
    			"	void bm5() {}\n" +
    			"	void bm6() {}\n" +
    			"	void bm7() {}\n" +
    			"	void bm8() {}\n" +
    			"	void bm9() {}\n" +
    			"	void bmA() {}\n" +
    			"	private void bm() {\n" +
    			"		System.out.print(\"bm\");\n" +
    			"	}\n" +
    			"	public void test() { bm(); }\n" +
    			"}\n",
    	"b/B1b.java",
    			"package b;\n" +
    			"public class B1b extends B1 {}\n", // stuffing to test transfer of binding tasks accross multiple anonymous sub classes
    	"b/B2.java",
    			"package b;\n" +
    			"public class B2 extends B1 {\n" +
    			"	public void test() { super.test(); }\n" +
    			"}\n",
    	"TeamFoo.java",
    			"import base b.B1;\n" +
    			"public team class TeamFoo {\n" +
    			"	protected class R playedBy B1 {\n" +
    			"		void rm() { System.out.print(\"rm\"); }\n" +
    			"		rm <- after bm1;\n" + // stuffing to create more interesting method & joinpoint ids
    			"		rm <- after bm2;\n" +
    			"		rm <- after bm3;\n" +
    			"		rm <- after bm4;\n" +
    			"		rm <- after bm5;\n" +
    			"		rm <- after bm6;\n" +
    			"		rm <- after bm7;\n" +
    			"		rm <- after bm8;\n" +
    			"		rm <- after bm;\n" +
    			"	}\n" +
    			"}\n"
    		},
    		"bmrmbmrm");
    }
}
