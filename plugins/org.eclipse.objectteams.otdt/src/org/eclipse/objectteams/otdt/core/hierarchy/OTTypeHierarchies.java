/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.hierarchy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.core.JavaModelStatus;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;

// both base type and directly used:
import org.eclipse.jdt.core.IType;

import base org.eclipse.jdt.core.ITypeHierarchy;
import base org.eclipse.jdt.internal.core.BinaryType;
import base org.eclipse.jdt.internal.core.SourceType;
import base org.eclipse.jdt.internal.core.hierarchy.HierarchyBuilder;
import base org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import base org.eclipse.objectteams.otdt.core.PhantomType;
import base org.eclipse.objectteams.otdt.internal.core.OTType;

/**
 * Adapt the regular Java TypeHierarchy as to handle implicit inheritance, too.
 * 
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class OTTypeHierarchies {
	
	/** How does a given type relate to the focus type? */
	enum FocusRelation {
		/** a type is a subtype of the focus type. */
		BELOW, 
		/** a type is the focus type itself. */
		EQUAL, 
		/** a type is a supertype of the focus type. */
		ABOVE, 
		/** a type is neither sub- nor supertype of the focus type. */
		UNRELATED;

		public static FocusRelation compute(ReferenceBinding focusType, ReferenceBinding typeBinding) {
			if (focusType == null) // happens for region based hierarchy
				return UNRELATED;
			if (focusType.isRole())
				focusType = focusType.getRealType();
			if (typeBinding.isRole())
				typeBinding = typeBinding.getRealType();
			if (focusType.equals(typeBinding))				return EQUAL;
			if (focusType.isCompatibleWith(typeBinding))	return ABOVE;
			if (typeBinding.isCompatibleWith(focusType))	return BELOW;
			return UNRELATED;
		}
	}
	
	private static final IType[] NO_TYPE = new IType[0];

	// === singleton infrastructure: ===
	
	private static OTTypeHierarchies instance;
	public OTTypeHierarchies() {
		instance = this;
	}
	/** Get the singleton instance of this team, which was created and activated by OT/Equinox. */
	public static OTTypeHierarchies getInstance() { return instance; }
	
	/**
	 * This role serves as a lift target for API parameters of type ITypeHierarchy.
	 * All details are found in subclass {@link OTTypeHierarchyImpl}. 
	 */
	@SuppressWarnings("abstractrelevantrole")
	protected abstract team class OTTypeHierarchy playedBy ITypeHierarchy 
	{		
		/** Should the hierarchy show phantom roles? */
		protected boolean phantomMode;

		protected abstract IType[] getAllSuperclasses(IType type);
		protected abstract IType[] getAllSuperInterfaces(IType type);
		protected abstract IType[] getAllTSuperTypes(IType type);
		protected abstract IType[] getDirectTSupers(IType type) throws JavaModelException;

		protected abstract IType getPlainSuperclass(IType type) throws JavaModelException;
		
		protected abstract IType[] getAllTSubTypes(IType type);
		protected abstract IType[] getSuperInterfaces(IType type);
	}

	/** 
	 * The stateful part of this team, implemented as a nested team.
	 */
	protected team class OTTypeHierarchyImpl extends OTTypeHierarchy playedBy TypeHierarchy 
	{
		// === "Imports" (callout) from plain TypeHierarchy: ===
		
		@SuppressWarnings("decapsulation") IType getFocusType() 	-> get IType focusType;
		ConnectedType getSuperclass(IType type) 					-> IType getSuperclass(IType type);
		ConnectedType[] getAllSuperclassesUnfiltered(IType type) 	-> IType[] getAllSuperclasses(IType type);
		IType[] getSuperInterfaces(IType type) 						-> IType[] getSuperInterfaces(IType type);
		IType[] getAllSuperInterfaces(IType type) 					-> IType[] getAllSuperInterfaces(IType type);
		
		
		// === Adapt TypeHierarchy (callin bindings): ===

		precedence unwrapping, filtering1, superlinearize; // order: outer callins unwrap and filter, inner callin performs computation
		precedence filtering2, tsuperadding;

		// --- considure implicit inheritance for some queries: ----
		
		// make getSuperclass() traverse the superclass linearization of the focus type:
	superlinearize: 
		ConnectedType getSuperclassLinearized(ConnectedType type) <- replace IType getSuperclass(IType type);
		
		// augment original queries to capture tsub-types, too:
		@SuppressWarnings("decapsulation")
		addTSubs <- replace getSubtypesForType; // adjust internal method as to capture calls from getAllSubtypes(IType), too.
	tsuperadding:
		addTSubs <- replace getSubclasses;

		// --- filtering (duplicates and phantoms) and unwrapping (OTTypes): ---
		
		// make some methods aware of our phantom mode:
	filtering1: 
		ConnectedType[] phantomFilterWrapper() 
		<- replace 
			IType[] getAllClasses(), 
			IType[] getImplementingClasses(IType type), 
			IType[] getAllSuperclasses(IType type) 
					when (!this.phantomMode);
		
		// filter intermediate results only at these top-level queries:
	filtering2: 	
		filterDuplicatesAndPhantoms <- replace getSubclasses, getSubtypes, getAllSubtypes, getImplementingClasses; // includes unwrapping of OTTypes
		
		// all top-level queries unwrap and given OTTypes:
	unwrapping:
		ensureJavaType <- replace getAllSuperclasses, getAllSuperInterfaces, getAllSupertypes,
							      getCachedFlags, getExtendingInterfaces, 
								  getSuperclass, getSuperInterfaces, getSupertypes;

		/**
		 * This role adds to ITypes the capability of connecting to all direct and indirect tsuper types,
		 * which includes linearization of all super types (implicit & explicit). 
		 */
		@SuppressWarnings("abstractrelevantrole")
		protected abstract class ConnectedType  playedBy IType 
		{
			ConnectedType getParent() -> IJavaElement getParent() 
				with { result <- (IType)result } // TODO(SH): make safer, consider role files

			/** Is this type a phantom role? */
			protected boolean isPhantom;

			/** The direct tsuper types of this type. */
			protected ConnectedType[] directTSupers;

			/** All direct & indirect tsuper classes in a linearized form. All classes in this list have the same simple name. */
			protected List<ConnectedType> allTSupersLinearized;

			/** This type's relation to the focus type. */
			private FocusRelation focusRelation;
			
			// the last item in allTSupersLinearized, used as a token for switching from implicit to explicit supers
			private ConnectedType lastTSuper = null;
			
			/** the root of the tsuper chain through which this connected type was reached. */
			protected ConnectedType tsuperChainRoot;
			
			/** All known direct tsub types of this type. */
			protected Set<ConnectedType> knownTSubTypes;

					
			protected ConnectedType(IType type) {
				this.tsuperChainRoot = this;
			}

			public void init(boolean isPhantom, ConnectedType[] tsuperclassHandles, FocusRelation focusRelation) {
				this.isPhantom = isPhantom;
				this.directTSupers = tsuperclassHandles;
				this.focusRelation = focusRelation;			
			}
			
			/** Combine indirect tsuper classes into the list of all linearized tsuper types. */
			@SuppressWarnings("ambiguouslowering") // method contains takes an Object argument
			public void combineTSupers(ConnectedType focusLayerType, ConnectedType[] tsuperclassHandles) {
				this.tsuperChainRoot = focusLayerType.tsuperChainRoot;
				if (this.allTSupersLinearized == null)
					this.allTSupersLinearized = new LinkedList<ConnectedType>();
				for (ConnectedType type : tsuperclassHandles)
					if (!this.allTSupersLinearized.contains(type))
						this.allTSupersLinearized.add(type);
			}

			/** Get the next type in the tsuper linearization. */
			protected ConnectedType getTSuperType() {
				if (this.focusRelation == FocusRelation.BELOW) {
					// only follow the path towards the focus type:
					if (this.directTSupers != null)
						for (ConnectedType directTSuper : this.directTSupers)
							if (directTSuper.focusRelation == FocusRelation.EQUAL || directTSuper.focusRelation == FocusRelation.BELOW)
								return directTSuper;
					return null; // no tsuper leading to focus
				}
				// shortcut if no tsupers:
				if (this.allTSupersLinearized == null || this.allTSupersLinearized.size() == 0)
					return null;
				// when starting from the focus layer return the first tsuper role:
				if (isInFocusLayer(this))
					return this.allTSupersLinearized.get(0);
				// when type is contained in the list of tsuperRoles return the next tsuper role from the list
				for (int i = 0; i < this.allTSupersLinearized.size()-1; i++)
					if (this.allTSupersLinearized.get(i) == this)
						return this.allTSupersLinearized.get(i+1);
				return null; // end reached
			}
			
			/** Is this the last element in the list of tsuper roles? */
			protected boolean isLastTSuper() {
				if (this.allTSupersLinearized == null || this.allTSupersLinearized.size() == 0) // optimize: don's store empty list?
					return false;
				if (this.lastTSuper == null)
					this.lastTSuper = this.allTSupersLinearized.get(this.allTSupersLinearized.size()-1);
				return this.lastTSuper == this;
			}
			
			/** If this is a phantom type, get the real tsuper class that this is derived (copied) from. */
			protected ConnectedType getRealTSuper() throws JavaModelException {
				if (!isPhantom)
					return this;
				if (this.allTSupersLinearized != null) {
					for(ConnectedType other : this.allTSupersLinearized)
						if (!other.isPhantom)
							return other;
				} else if (this.directTSupers != null) {
					for (ConnectedType direct : this.directTSupers) { // TODO(SH): check whether we need breadth-first search
						ConnectedType candidate = direct.getRealTSuper();
						if (candidate != null)
							return candidate;
					}						
				}
				throw new JavaModelException(new JavaModelStatus(JavaModelStatus.ERROR, this, "no non-phantom type found in allTSupersLinearized")); //$NON-NLS-1$
			}
			
			/** Get all tsuper types yet respecting phantom mode. */
			protected ConnectedType[] getAllTSuperTypes() 
			{
				if (this.focusRelation == FocusRelation.BELOW && this.directTSupers != null) {
					// special handling for types below the focus: transitively collect tsupers:
					ConnectedType direct = getTSuperType(); // only the one path towards focus
					if (direct == null) 
						return new ConnectedType[0];
					ConnectedType[] indirect = direct.getAllTSuperTypes(); // NB: recursion may enter the focus cone
					if (indirect == null) 
						return new ConnectedType[] {direct};
					// merge direct and indirect:
					int len = indirect.length;
					ConnectedType[] result = new ConnectedType[len+1];
					result[0] = direct;
					System.arraycopy(indirect, 0, result, 1, len);
					return result;
				}

				int size;
				if (this.allTSupersLinearized != null && (size = this.allTSupersLinearized.size()) > 0) 
				{
					// focus level => all types from the linearization:
					if (isInFocusLayer(this))
						return this.allTSupersLinearized.toArray(new ConnectedType[size]);
					
					// not focus, search in linearization for this type ...
					int skip = 0;
					boolean found = false;
					while (skip < size)
						if (this.allTSupersLinearized.get(skip++) == this) {
							found = true;
							break;
						}
					if (found) { // .. and take the tail after this type: 
						if (skip < size) { // if found at last pos, there's no tail
							ConnectedType[] result = new ConnectedType[size-skip];
							for (int i=0; skip<size;)
								result[i++] = this.allTSupersLinearized.get(skip++);
							return result;
						}
					}
				}
				return new ConnectedType[0];
			}
			
			/** Register a known tsub type of this type. */
			protected void addTSubType(ConnectedType tsub) {
				if (this.knownTSubTypes == null)
					this.knownTSubTypes = new HashSet<ConnectedType>();
				this.knownTSubTypes.add(tsub);
			}

			// let roles reuse equality of their bases:
			@Override
			public boolean equals(Object other) {
				if (other instanceof ConnectedType) {
					return baseEquals((ConnectedType) other); // TODO(SH): bogus warning re unnecessary cast
				} else if (other instanceof IType) {
					return baseEquals((IType)other);
				}
				return false;
			}
			// consistently compare IType to IType:
			boolean baseEquals(IType other) -> boolean equals(Object other);
			hashCode => hashCode;

			// for debugging:
			toString => toString; 

		}
		// binding source variant of IType
		protected class ConnectedSourceType extends ConnectedType playedBy SourceType { /*just class binding, no details*/ }
		
		// binding binary variant of IType
		protected class ConnectedBinaryType extends ConnectedType playedBy BinaryType { /*just class binding, no details*/ }
		
		// binding binary variant of IType
		protected class ConnectedPhantomType extends ConnectedType playedBy PhantomType { /* mainly class binding */
			/** Create a new phantom type (role and base) */
			protected ConnectedPhantomType(IType enclosingTeam, IType realTSuper) {
				// super(base(enclosingTeam, realTSuper)); // FIXME(SH): syntax err??
				super(new org.eclipse.objectteams.otdt.core.PhantomType(enclosingTeam, realTSuper));
			}
//			protected ConnectedPhantomType(ConnectedType original) throws JavaModelException { // FIXME(SH): ctor cannot declare exception
//				this(original.getParent(), original.getRealTSuper());
//			}
		}

		// binding binary variant of IType
		protected class ConnectedOTType extends ConnectedType playedBy OTType { /*just class binding, no details*/ }

		// === start main part of nested team OTTypeHierarchyImpl ===

		/**
		 * Connect the found tsuper classes into the connected type for the given type. 
		 */
		protected void connectTSupers(IType as ConnectedType connectedType, 
									  boolean isPhantom, 
									  IType as ConnectedType tsuperclassHandles[], 
									  boolean[] arePhantoms,
									  FocusRelation focusRelation) 
		{
			connectedType.init(isPhantom, tsuperclassHandles, focusRelation);
			
			// connect tsuperclassHandles into the appropriate ConnectedType:
			switch (focusRelation) {
			case EQUAL:
				connectedType.allTSupersLinearized = new LinkedList<ConnectedType>();
				for (int i = 0; i < tsuperclassHandles.length; i++)
					connectedType.allTSupersLinearized.add(tsuperclassHandles[i]);
				break;
			case ABOVE:
				ConnectedType focusLayerType = connectedType.tsuperChainRoot;
				focusLayerType.combineTSupers(focusLayerType, tsuperclassHandles);
				connectedType.allTSupersLinearized = focusLayerType.allTSupersLinearized; // share the same list
				break;
			case BELOW:
				// don't merge into focus' up-chain
				break;
			default: // UNRELATED
				// not relevant for tsuper linkage
			}
			// connect each in tsuperclassHandles back to the tsuperChainRoot:
			for (int i = 0; i < tsuperclassHandles.length; i++) {
				if (focusRelation == FocusRelation.EQUAL || focusRelation == FocusRelation.ABOVE)
					tsuperclassHandles[i].tsuperChainRoot = connectedType.tsuperChainRoot;
				tsuperclassHandles[i].isPhantom = arePhantoms[i];
				// and remember the type-tsub link:
				tsuperclassHandles[i].addTSubType(connectedType);
			}
		}

		private boolean isInFocusLayer(IType type) {
			IType focusType = getFocusType();
			if (type.equals(focusType))
				return true;
			return focusType.getParent().equals(type.getParent()); // TODO(SH) elaborate more
		}
		
		// ==== adjust original queries for respecting implicit inheritance, too: ===

		/** Given that 'type' is element of a superclass linearization return the next super in the chain. */
		@SuppressWarnings("basecall")
		callin ConnectedType getSuperclassLinearized(ConnectedType type) {
			// check for tsuper type in the computed list:
			ConnectedType tsuperType = type.getTSuperType();
			if (tsuperType != null)
				return tsuperType;
			// if type is the last in the list ... 
			if (type.isLastTSuper()) {
				// ...we're done with tsupers, start over with super of the focus level type:
				return base.getSuperclassLinearized(type.tsuperChainRoot);
			}
			return base.getSuperclassLinearized(type);
		}
		
		/** When querying direct subtypes of type include tsub types, too, respecting phantom mode. */
		callin ConnectedType[] addTSubs(ConnectedType type) {
			ConnectedType[] res1 = base.addTSubs(type);
			Set<ConnectedType> tsubs = type.knownTSubTypes;
			
			if (tsubs == null || tsubs.size() == 0)
				return this.phantomMode ? res1 : filterDupsAndPhants(res1);
		
			Set<ConnectedType> result = new HashSet<ConnectedType>();
			result.addAll(tsubs);
			for (ConnectedType t1 : res1)
				result.add(t1);
			return result.toArray(new ConnectedType[result.size()]);
		}

		// ==== handling for phantom mode: ====

		// wrappers for use in multiple callin bindings
		callin ConnectedType[] phantomFilterWrapper() {
			return filterPhantomRoles(base.phantomFilterWrapper());
		}
		
		callin ConnectedType[] filterDuplicatesAndPhantoms(IType type) {
			if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
			return filterDupsAndPhants(base.filterDuplicatesAndPhantoms(type));
		}

		callin void ensureJavaType(IType type) {
			if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
			base.ensureJavaType(type);
		}

		// the following helpers should be private (compiler error forbids)
		ConnectedType[] maybeSubstitutePhantoms(ConnectedType[] roles) throws JavaModelException {
			if (this.phantomMode)
				return roles;
			ConnectedType[] substituted = new ConnectedType[roles.length];
			for (int i = 0; i < roles.length; i++)
				substituted[i] = roles[i].getRealTSuper();
			return substituted;
		}

		ConnectedType[] filterPhantomRoles(ConnectedType[] roles) {
			ConnectedType[] substituted = new ConnectedType[roles.length];
			int n = 0;
			for (int i = 0; i < roles.length; i++)
				if (!roles[i].isPhantom)
					substituted[n++] = roles[i];
			if (n<roles.length)
				System.arraycopy(substituted, 0, substituted=new ConnectedType[n], 0, n);
			return substituted;
		}

		ConnectedType[] filterDupsAndPhants(ConnectedType[] unfiltered) {
			if (unfiltered == null)
				return unfiltered;
			Set<ConnectedType> filtered = new HashSet<ConnectedType>();
			for(ConnectedType type : unfiltered) {
				if (type.isPhantom && !this.phantomMode)
					continue; // skip
				type = maybeAdjustPhantom(type);
				if (type != null)
					filtered.add(type);
			}
			return filtered.toArray(new ConnectedType[filtered.size()]);
		}
		
		ConnectedType maybeAdjustPhantom(ConnectedType type) {
			if (!type.isPhantom || (type instanceof ConnectedPhantomType))
				return type;
			try {
				return new ConnectedPhantomType(type.getParent(), type.getRealTSuper());
			} catch (JavaModelException e) {
				OTDTPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, OTDTPlugin.PLUGIN_ID, "Failed to find original tsuper role.", e)); //$NON-NLS-1$
				return null;
			}
		}

		// ==== queries for clients (wrapped by methods of enclosing team, see there for documentation): ====
		
		protected IType[] getAllTSuperTypes(IType as ConnectedType type) {
			ConnectedType[] result = type.getAllTSuperTypes();
			return this.phantomMode ? result : filterPhantomRoles(result);  
				// FIXME(SH) return without intermediate local var -> CCE
				// like: return type.getAllTSuperTypes();
		}

		protected IType[] getDirectTSupers(IType as ConnectedType type) throws JavaModelException {
			if (type.directTSupers == null)
				return NO_TYPE;
			return maybeSubstitutePhantoms(type.directTSupers);
		}
		
		protected IType getPlainSuperclass(IType type) throws JavaModelException {
			try {
				OTTypeHierarchies.this.deactivate();
				ConnectedType result = getSuperclass(type);
				if (this.phantomMode || result == null)
					return result;
				return result.getRealTSuper();
			} finally {
				OTTypeHierarchies.this.activate();
			}
		}
		
		// sole purpose of this wrapper: array-lowering
		protected IType[] getAllSuperclasses(IType type) {
			return getAllSuperclassesUnfiltered(type);
		}
		
		protected IType[] getAllTSubTypes(IType as ConnectedType type) {
			Set<ConnectedType> tsubs = new HashSet<ConnectedType>();
			internalGetAllTSubTypes(type, tsubs);
			ConnectedType[] result = tsubs.toArray(new ConnectedType[tsubs.size()]);
			return result;// FIXME(SH) return without intermediate local var -> CCE
		}
		protected void internalGetAllTSubTypes(ConnectedType type, Set<ConnectedType> tsubs) { // FIXME(SH) private gives type error (in bridge?)
			if (type.knownTSubTypes != null) {
				for (ConnectedType tsub : type.knownTSubTypes) {
					if (tsub.isPhantom && !this.phantomMode)
						continue;
					internalGetAllTSubTypes(tsub, tsubs); // use the originally connected type not a mostly empty phantom wrapper
					tsub = maybeAdjustPhantom(tsub);
					if (tsub != null)  // if we failed to adjust, so better skip this one.
						tsubs.add(tsub);
				}
			}
		}
	}
	
	/**
	 * This role adapts the HierarchyBuilder in order to record information about implicit inheritance, too. 
	 */
	protected class OTHierarchyBuilder playedBy HierarchyBuilder {

		@SuppressWarnings("decapsulation")
		OTTypeHierarchyImpl getHierarchy() -> get TypeHierarchy hierarchy;

		connectTSupers  <- before hookableConnect;

		private void connectTSupers(ReferenceBinding focusType, ReferenceBinding typeBinding, IGenericType type, IType typeHandle, boolean isPhantom, 
									IType superclassHandle, IType[] tsuperclassHandles, boolean[] arePhantoms, IType[] superinterfaceHandles) 
		{
			if (typeHandle != null && tsuperclassHandles!= null) {
				FocusRelation focusRelation = FocusRelation.compute(focusType, typeBinding);
				getHierarchy().connectTSupers(typeHandle, isPhantom, tsuperclassHandles, arePhantoms, focusRelation);
			}
		}
	}
	
	/** Lookup of role types requires the OT TypeHierarchy, connect here. */
	protected class OTType playedBy OTType {

		IType getCorrespondingJavaElement() -> IJavaElement getCorrespondingJavaElement()
			with { result <- (IType)result }

		@SuppressWarnings("decapsulation")
		getTypesToSearchForRoles <- replace getTypesToSearchForRoles;

		@SuppressWarnings("basecall")
		callin IType[] getTypesToSearchForRoles(OTTypeHierarchy hierarchy, int which) throws JavaModelException {
	        switch (which) {
	        	case IOTType.EXPLICITLY_INHERITED:
	        	    return new IType[] { hierarchy.getPlainSuperclass(this.getCorrespondingJavaElement()) };
	    	    case IOTType.IMPLICTLY_INHERITED:
	        	    return hierarchy.getAllTSuperTypes(this);
	        	default:
	        		return base.getTypesToSearchForRoles(hierarchy, which);
	        }
		}
		
	}
	
	/**
	 * API: Query all direct and indirect implicit superclasses of the given type.
	 * If phantomMode is set to <code>false</code> any phantom roles will be filtered from the result.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return a non-null array of ordered implicit super classes
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType[] getAllTSuperTypes(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		return otHierarchy.getAllTSuperTypes(type);
	}
	
	/**
	 * API: Query all direct implicit superclasses of the given type.
	 * If phantomMode is set to <code>false</code> any phantom roles will be translated to its real origin.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return a non-null array of ordered implicit super classes
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType[] getTSuperTypes(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		return otHierarchy.getDirectTSupers(type);
	}
	
	/**
	 * API: Query all direct, implicit and explicit superclasses of the given type.
	 * If phantomMode is set to <code>false</code> any phantom roles will be substituted with their real origin.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return a non-null array of ordered super classes.
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType[] getSuperclasses(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		// have superclass (otherwise type == java.lang.Object?)
		IType superclass = otHierarchy.getPlainSuperclass(type);
		if (superclass == null)
			return NO_TYPE;
		
		// check direct tsupers:
		IType[] directTSupers = getTSuperTypes(otHierarchy, type);
		if (directTSupers == null)
			return new IType[] { superclass };
		
		// found both => merge
		int count = directTSupers.length;
		IType[] merged = new IType[count+1];
		System.arraycopy(directTSupers, 0, merged, 0, count);
		merged[count] = superclass;
		return merged;
	}
	
	/**
	 * API: Query all direct and indirect, implicit and explicit superclasses of the given type.
	 * If phantomMode is set to <code>false</code> any phantom roles will be filtered from the result.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return a non-null array of ordered super classes.
	 * @throws JavaModelException if accessing type failed
	 */
	public IType[] getAllSuperclasses(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type.isInterface())
			return NO_TYPE;
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		return otHierarchy.getAllSuperclasses(type);
	}
	
	/**
	 * API: Query the explicit superclass of the given type.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super type is queried
	 * @return the superclass
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType getExplicitSuperclass(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		// have superclass (otherwise type == java.lang.Object?)
		return otHierarchy.getPlainSuperclass(type);
	}

	/** Get ALL super types: classes and interfaces, explicit and implicit, direct and indirect. */
	public IType[] getAllSupertypes(ITypeHierarchy  as OTTypeHierarchy otHierarchy, IType type) {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		IType[] superclasses = otHierarchy.getAllSuperclasses(type);
		IType[] superinterfaces = otHierarchy.getAllSuperInterfaces(type);
		int l1 = superclasses.length, l2 = superinterfaces.length;
		IType[] result = new IType[l1+l2];
		System.arraycopy(superclasses, 0, result, 0, l1);
		System.arraycopy(superinterfaces, 0, result, l1, l2);
		return result;
	}

	/**
	 * API: Query the superinterfaces of the given type, plus if it's a role interface the tsuper interfaces.
	 * tsuper's direct superinterfaces are also counted as direct interfaces.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return the superinterfaces
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType[] getSuperInterfaces(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		IType[] superinterfaces = otHierarchy.getSuperInterfaces(type);
		if (!OTModelManager.isRole(type))
			return superinterfaces;
		Set<IType> all = new HashSet<IType>();
		for (IType iType : superinterfaces)
			all.add(iType);
		for (IType tsuperinterface : otHierarchy.getDirectTSupers(type)) {
			if (type.isInterface()) // otherwise just traverse tsuper to find more super interfaces
				all.add(tsuperinterface);
			for (IType tsupersuper : otHierarchy.getSuperInterfaces(tsuperinterface))
				all.add(tsupersuper);
		}
		return all.toArray(new IType[all.size()]);
	}

	/**
	 * API: Query the superinterfaces of the given type: explicit & implicit, direct & indirect.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose super types are queried
	 * @return the superinterfaces
	 * @throws JavaModelException If the tsuper linearization is corrupt
	 */
	public IType[] getAllSuperInterfaces(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) throws JavaModelException {
		if (!OTModelManager.isRole(type))
			return otHierarchy.getAllSuperInterfaces(type);
		if (type instanceof IOTType) type = (IType) ((IOTType)type).getCorrespondingJavaElement();
		Set<IType> all = new HashSet<IType>();
		getAllSuperInterfaces2(otHierarchy, type, all);
		return all.toArray(new IType[all.size()]);
	}
	// recursive helper for above
	private void getAllSuperInterfaces2(OTTypeHierarchy otHierarchy, IType seed, Set<IType> collected) throws JavaModelException {
		for (IType tsuperinterface : otHierarchy.getAllTSuperTypes(seed)) {
			if (seed.isInterface()) { // otherwise just traverse tsuper to find more super interfaces
				collected.add(tsuperinterface);
			}
			getAllSuperInterfaces2(otHierarchy, tsuperinterface, collected);
		}
		for (IType superinterface : otHierarchy.getAllSuperInterfaces(seed)) {
			collected.add(superinterface);
			getAllSuperInterfaces2(otHierarchy, superinterface, collected);
		}
	}
	
	/**
	 * API: Query all direct and indirect implicit subtypes of the given type.
	 * 
	 * @param otHierarchy a hierarchy that has been focused on the given type
	 * @param type        the focus type whose subtypes are queried
	 * @return the tsub types
	 */
	public IType[] getAllTSubTypes(ITypeHierarchy as OTTypeHierarchy otHierarchy, IType type) {
		return otHierarchy.getAllTSubTypes(type);
	}
	
	/**
	 * Configure whether the given hierarchy should consider phantom roles or not.
	 * Depending on the query used, phantom roles will either be filtered out or replaced with their real origins.
	 * In order for the phantom modes to be respected, the hierarchy must not be directly consulted but only
	 * via the fassade methods of this team. 
	 */
	public void setPhantomMode(ITypeHierarchy as OTTypeHierarchy otHierarchy, boolean mode) {
		otHierarchy.phantomMode = mode;		
	}
}
