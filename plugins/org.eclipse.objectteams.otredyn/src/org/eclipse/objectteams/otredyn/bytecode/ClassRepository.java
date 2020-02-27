/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2015 Oliver Frank and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.objectteams.otredyn.bytecode.asm.AsmBoundClass;
import org.eclipse.objectteams.otredyn.bytecode.asm.AsmClassRepository;
import org.eclipse.objectteams.otredyn.runtime.IClassRepository;
import org.eclipse.objectteams.otredyn.runtime.TeamManager;

/**
 * This class creates and provides instances of
 * AbstractBoundClass and AbstractTeam. It is the only
 * instance that should do this. It is a singelton, thats
 * why the constructor should not called directly.
 * The instance is provided by getInstance. 
 * @author Oliver Frank
 */
public abstract class ClassRepository implements IClassRepository {
	private static ClassRepository instance;

	private static ThreadLocal<Class<?>> classBeingRedefined = new ThreadLocal<>();

	/**
	 * Reflectively invoked by org.eclipse.objectteams.otequinox.OTEquinoxAgent:
	 * During HCR the agent informs as about a class about to be hot-swapped.
	 */
	public static void registerClassBeingRedefined(Class<?> clazz) {
		classBeingRedefined.set(clazz);
	}
	public static Class<?> popClassBeingRedefined(String className) {
		Class<?> clazz = classBeingRedefined.get();
		classBeingRedefined.set(null);
		if (clazz != null && clazz.getName().equals(className))
			return clazz;
		return null;
	}
	
	protected ClassRepository() {
		
	}
	
	/**
	 * Returns a singleton instance of the ClassRepository.
	 * @return
	 */
	public static synchronized ClassRepository getInstance() {
		if (instance == null) {
			instance = new AsmClassRepository();
			TeamManager.setup(instance);
		}
		
		return instance;
	}
	
	protected static final String ANONYMOUS_SUBCLASS_NAME = "AnonymousSubclass";
	
	private Map<String, AbstractTeam> boundClassMap = new HashMap<String, AbstractTeam>();
	private Map<AbstractBoundClass, AbstractBoundClass> anonymousSubclassMap = new IdentityHashMap<AbstractBoundClass, AbstractBoundClass>();
	
	/**
	 * Returns a instance of AbstractBoundClass for the
	 * given FQN and id. If there is no instance it
	 * is created. The class have not to be loaded to get
	 * an instance of AbstractBoundClass representing this class.
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getBoundClass(..., id1) == getBoundClass(id2) 
	 * @param className the name of the class
	 * @param id a globally unique identifier for the class 
	 * @return
	 */
	public synchronized AbstractBoundClass getBoundClass(String className, String id, ClassLoader loader) {
		AbstractTeam clazz = boundClassMap.get(id);
		if (clazz == null) {
			clazz = createClass(className, id, BytecodeProviderFactory.getBytecodeProvider(), loader);
			boundClassMap.put(id, clazz);
		}
		
		return clazz;
	}

	/**
	 * Similar to {@link getBoundClass(String, String, ClassLoader)},
	 * but don't create a new bound class if none has been registered before.
	 * @param id a globally unique identifier for the class 
	 */
	public synchronized AbstractBoundClass peekBoundClass(String id) {
		return boundClassMap.get(id);
	}

	/**
	 * Returns a instance of AbstractBoundClass for the
	 * given FQN and id and sets the bytecode for this class. 
	 * If there is no instance it is created. 
	 * This class should be called while loading the class.
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getBoundClass(..., id1) == getBoundClass(..., id2) 
	 * @param className the name of the class
	 * @param id a globally unique identifier for the class 
	 * @param classBytes bytecode before weaving, possibly for hotswapping
	 * @param loader class loader for this class
	 * @param isHCR true if invoked during hot code replace, in which case transformation must restart using the new bytes
	 * @return
	 */
	public synchronized AbstractBoundClass getBoundClass(String className, String id, byte[] classBytes, ClassLoader loader, boolean isHCR) 
	{
		AbstractTeam clazz = boundClassMap.get(id);
		// set the bytecode in the BytecodeProvider
		IBytecodeProvider bytecodeProvider = BytecodeProviderFactory.getBytecodeProvider();
		bytecodeProvider.setBytecode(id, classBytes);
		if (clazz == null) {
			clazz = createClass(className, id, bytecodeProvider, loader);
		} else if (isHCR) {
			clazz.setBytecode(classBytes);
			clazz.restartTransformation();
		}
		
		boundClassMap.put(id, clazz);

		clazz.setLoaded();
		
		return clazz;
	}

	/**
	 * This method links a class with its superclass.
	 * It checks if the superclass was already loaded.
	 * If it was already loaded it merges this class with
	 * the anonymous subclass of its superclass
	 * @param clazz the subclass
	 */
	public void linkClassWithSuperclass(AbstractBoundClass clazz) {
		// FIXME(SH): also link with tsuper classes??
		AbstractBoundClass superclass = clazz.getSuperclass();
		if (superclass == null) {
			return;
		}
		if (superclass instanceof AsmBoundClass && !((AsmBoundClass) superclass).parsed) {
			linkClassWithSuperclass(superclass); // reduce deadlock-prone need for on-demand parsing (with lock)
		}

		AbstractBoundClass anonymousSubclass = anonymousSubclassMap.get(superclass);
		
		//Is there an anonmous subclass, that corresponds with this class
		if (anonymousSubclass != null) {
			//Yes, so merge the tasks
			AbstractBoundClass newAnonymousSubclass = createClass(ANONYMOUS_SUBCLASS_NAME, 
															      ANONYMOUS_SUBCLASS_NAME, 
															      BytecodeProviderFactory.getBytecodeProvider(),
															      clazz.getClassLoader());
			superclass.removeSubclass(anonymousSubclass);
			superclass.addSubclass(newAnonymousSubclass);
			superclass.addSubclass(clazz);
			newAnonymousSubclass.mergeTasks(anonymousSubclass);
			clazz.mergeTasks(anonymousSubclass);
			anonymousSubclass.performWiringTasks(superclass, clazz);
			anonymousSubclassMap.put(superclass, newAnonymousSubclass);
		}
	}

	/**
	 * Returns a instance of AbstractTeam for the
	 * given FQN and id. If there is no instance it
	 * is created. 
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getTeam(..., id1) == getTeam(..., id2).
	 * This method should only be called, if it is known
	 * that the name identifies a team and not a class.
	 * Otherwise call getBoundClass().isTeam() to check this. 
	 * @param teamName the name of the team
	 * @param id a globally unique identifier for the team 
	 * @return
	 */
	public AbstractTeam getTeam(String teamName, String id, ClassLoader loader) {
		return (AbstractTeam) getBoundClass(teamName, id, loader);
	}
	
	/**
	 * Returns a instance of AbstractBoundClass representing
	 * a anonymous subclass. A anonymous subclass stands for all subclasses,
	 * that are not known yet and stores the WeavingTasks inherited 
	 * from the superclass. This class could not have got direct bindings.
	 * If a real subclass of its superclass is loaded all WeavingTasks are
	 * merged with the real subclass 
	 * @param abstractBoundClass
	 * @return
	 */
	protected AbstractBoundClass getAnonymousSubclass(
			AbstractBoundClass abstractBoundClass) {
		AbstractBoundClass anonymousSubclass = anonymousSubclassMap.get(abstractBoundClass);
		if (anonymousSubclass == null) {
			anonymousSubclass = createClass(ANONYMOUS_SUBCLASS_NAME, 
										    ANONYMOUS_SUBCLASS_NAME, 
										    BytecodeProviderFactory.getBytecodeProvider(),
										    abstractBoundClass.getClassLoader());
			anonymousSubclass.setSuperClassName(abstractBoundClass.getName());
			anonymousSubclassMap.put((AbstractTeam) abstractBoundClass, anonymousSubclass);
		}
		return anonymousSubclass;
	}
	
	/**
	 * Creates a instance of AbstractBoundClass and AbstracTeam
	 * @param name
	 * @param id
	 * @param bytecodeProvider
	 * @return
	 */
	protected abstract AbstractTeam createClass(String name, String id, IBytecodeProvider bytecodeProvider, ClassLoader loader);
}
