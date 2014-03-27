/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: AllTests.java 23529 2010-02-18 23:06:04Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.core.tests.compiler.regression.RegressionTestSetup;
import org.eclipse.jdt.core.tests.junit.extension.TestCase;
import org.eclipse.jdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.objectteams.otdt.tests.otjld.api.Reflection;
import org.eclipse.objectteams.otdt.tests.otjld.callinbinding.BaseCalls;
import org.eclipse.objectteams.otdt.tests.otjld.callinbinding.CallinMethodBinding;
import org.eclipse.objectteams.otdt.tests.otjld.callinbinding.CallinParameterMapping_LiftingAndLowering;
import org.eclipse.objectteams.otdt.tests.otjld.callinbinding.CallinWithTranslation;
import org.eclipse.objectteams.otdt.tests.otjld.calloutbinding.CalloutMethodBinding;
import org.eclipse.objectteams.otdt.tests.otjld.calloutbinding.CalloutParameterBinding_LiftingAndLowering;
import org.eclipse.objectteams.otdt.tests.otjld.calloutbinding.CalloutToField;
import org.eclipse.objectteams.otdt.tests.otjld.calloutbinding.OverridingAccessRestrictions;
import org.eclipse.objectteams.otdt.tests.otjld.liftlower.AllSmartLiftingTests;
import org.eclipse.objectteams.otdt.tests.otjld.liftlower.DeclaredLifting;
import org.eclipse.objectteams.otdt.tests.otjld.other.AccessModifiers;
import org.eclipse.objectteams.otdt.tests.otjld.other.Exceptions;
import org.eclipse.objectteams.otdt.tests.otjld.other.Java5;
import org.eclipse.objectteams.otdt.tests.otjld.other.Java7;
import org.eclipse.objectteams.otdt.tests.otjld.other.Misc;
import org.eclipse.objectteams.otdt.tests.otjld.other.Modifiers;
import org.eclipse.objectteams.otdt.tests.otjld.regression.CompilationOrder;
import org.eclipse.objectteams.otdt.tests.otjld.regression.ComplexStructures;
import org.eclipse.objectteams.otdt.tests.otjld.regression.DevelopmentExamples;
import org.eclipse.objectteams.otdt.tests.otjld.regression.ReportedBugs;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.AllBindingAmbiguitiesTests;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.BaseClassVisibility;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.ExplicitRoleCreation;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.GC;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.LiftingAndLowering;
import org.eclipse.objectteams.otdt.tests.otjld.roleplaying.PlayedByRelation;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.AcquisitionAndInheritanceOfRoleClasses;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.Confinement;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.Covariance;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.ExternalizedRoles;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.FileStructure;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.ImplicitInheritance;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.InheritanceHierarchyOfTeams;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.OldExternalizedRoles;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.RegularRoleInheritance;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.RelevantRole;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.RoleObjectContainment;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.TeamNesting;
import org.eclipse.objectteams.otdt.tests.otjld.rolesandteams.ValueParameters;
import org.eclipse.objectteams.otdt.tests.otjld.syntax.Syntax;
import org.eclipse.objectteams.otdt.tests.otjld.teamactivation.ExplicitTeamActivation;
import org.eclipse.objectteams.otdt.tests.otjld.teamactivation.ImplicitTeamActivation;

/**
 * @author stephan
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All OTJLD Tests");
		
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();

		// 1. rolesandteams
		/*1.1*/testClasses.add(RoleObjectContainment.testClass());
		/*1.1.10-*/testClasses.add(TeamNesting.testClass());
		/*1.2*/testClasses.add(InheritanceHierarchyOfTeams.testClass());
		/*1.3*/testClasses.add(AcquisitionAndInheritanceOfRoleClasses.testClass());
		/*1.4*/testClasses.add(RegularRoleInheritance.testClass());
		/*1.5*/testClasses.add(FileStructure.testClass());
		/*1.6*/testClasses.add(ExternalizedRoles.testClass());
		
		/*6.2*/testClasses.add(OldExternalizedRoles.testClass());
		
		/*1.7*/testClasses.add(Confinement.testClass());
		/*1.8*/testClasses.add(RelevantRole.testClass());
		/*1.9*/testClasses.add(ValueParameters.testClass());
		
		/*0.c*/testClasses.add(ImplicitInheritance.testClass());
		/*6.3*/testClasses.add(Covariance.testClass());
		
		// 2. roleplaying
		/*2.1*/testClasses.add(PlayedByRelation.testClass());
		/*2.2*/testClasses.add(LiftingAndLowering.testClass());
		/*2.3*/testClasses.add(ExplicitRoleCreation.testClass());
		/*2.4*/testClasses.add(BaseClassVisibility.testClass());
		/*2.5*/testClasses.add(GC.testClass());
		
		// 3. calloutbinding
		/*3.1*/testClasses.add(CalloutMethodBinding.testClass()); // includes a few from 7.2.1
		/*3.2*/testClasses.add(CalloutParameterBinding_LiftingAndLowering.testClass());
		/*3.3*/testClasses.add(CalloutToField.testClass());
		
		/*7.4*/testClasses.add(OverridingAccessRestrictions.testClass());
		
		// 4. callinbinding
		/*4.1*/testClasses.add(CallinMethodBinding.testClass()); // includes a few from 7.2.[45]
		/*4.3*/testClasses.add(CallinParameterMapping_LiftingAndLowering.testClass());
		/*4.4*/testClasses.add(CallinWithTranslation.testClass());
		/*4.5*/testClasses.add(BaseCalls.testClass());
		
		// 5. teamactivation
		/*5.2*/testClasses.add(ExplicitTeamActivation.testClass());
		/*5.3*/testClasses.add(ImplicitTeamActivation.testClass());
		
		// 6.[14] liftlower
		/*6.1*/testClasses.add(DeclaredLifting.testClass());
		//6.4: see below
		
		// 7. see below

		// 8. syntax
		/*8.1*/testClasses.add(Syntax.testClass());
		
		// api:
		/*9.2*/testClasses.add(Reflection.testClass());
		
		// other:
		/*0.a*/testClasses.add(AccessModifiers.testClass());
		/*7.1*/testClasses.add(Modifiers.testClass());
		/*7.5*/testClasses.add(Exceptions.testClass());
		/*A.1*/testClasses.add(Java5.testClass());
		/*A.2*/testClasses.add(Java7.testClass());
		/*0.m*/testClasses.add(Misc.testClass());
		
		// regression:
		/*B.1*/testClasses.add(ReportedBugs.testClass());
		/*B.2*/testClasses.add(CompilationOrder.testClass());
		/*X.2*/testClasses.add(DevelopmentExamples.testClass());
		
		testClasses.add(ComplexStructures.testClass());
		
		// Reset forgotten subsets tests
		TestCase.TESTS_PREFIX = null;
		TestCase.TESTS_NAMES = null;
		TestCase.TESTS_NUMBERS= null;
		TestCase.TESTS_RANGE = null;
		TestCase.RUN_ONLY_ID = null;
		
		suite = (TestSuite) AbstractCompilerTest.buildAllCompliancesTestSuite(AllTests.class, RegressionTestSetup.class, testClasses);

		/*7.3*/suite.addTest(AllBindingAmbiguitiesTests.suite());
		/*6.4*/suite.addTest(AllSmartLiftingTests.suite());

		return suite;
	}
}
