/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2009 Stephan Herrmann
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id: AnchorUsageRanksAttribute.java 23417 2010-02-03 20:13:55Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.classfmt.FieldInfo;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.FieldModel;

/**
 * This attribute stores interdependencies between value and type parameters.
 * Consider, e.g., a generic class
 * <pre>class MyClass&lt;MyTeam t, U, R1&lt;@t&gt;, R2&lt;@t&gt;&gt;</pre>
 * 
 * From this a final field <code>t</code> is generated to which this attribute
 * is attached. The attribute value is a list of ranks (zero-based), here <code>1,2</code>
 * referring to the second and third type parameter (not counting the value
 * parameter <code>t</code>), saying that those parameters <code>R1</code>
 * and <code>R2</code> are anchored to the field <code>t</code>.
 * 
 * Format of this attribute: list of unsigned short
 * 
 * @author stephan
 * @since 1.3.2
 */
public class AnchorUsageRanksAttribute extends ListValueAttribute {

	FieldBinding field;
	List<Integer> ranks;
	
	/** Create a new instance during compiling the generic type. */
	public AnchorUsageRanksAttribute(FieldBinding field) {
		super(IOTConstants.ANCHOR_USAGE_RANKS, 0/*still unknown*/, 2);
		this.field = field;
	}
	
	/** Create a new attribute from bytecode. */
	public AnchorUsageRanksAttribute(FieldInfo info, int readOffset, int structOffset, int[] constantPoolOffsets) {
		super(ANCHOR_USAGE_RANKS, 0, 2);
		readList(info, readOffset, structOffset, constantPoolOffsets);
	}

	@Override
	public boolean setupForWriting() {
		if (this.ranks != null)
			this._count = this.ranks.size();
		return true;
	}

	/** During resolve add a rank, saying that the rank'th type parameter is anchored to this field. */
	public void addUsageRank(int rank) {
		if (this.ranks == null)
			this.ranks = new ArrayList<Integer>();
		this.ranks.add(rank);
	}

	@Override
	void writeElementValue(int i) {
		writeUnsignedShort(this.ranks.get(i));
	}
	
	@Override
	void read(int i) {
		if (i == 0)
			this.ranks = new ArrayList<Integer>();
		this.ranks.add(consumeShort());
	}

	@Override
	public void evaluate(Binding binding, LookupEnvironment environment, char[][][] missingTypeNames) {
		// nop / not used
	}
	
	@Override
	public boolean evaluate(FieldBinding binding) {
		binding.modifiers |= ExtraCompilerModifiers.AccValueParam;
		this.field = binding;
		FieldModel.getModel(binding).addAttribute(this);
		return true;
	}

	/** Retrieve the array of ranks as read from bytecode. */
	public int[] getRanks() {
		int[] newRanks = new int[this._count];
		for (int i=0; i<this._count; i++)
			newRanks[i] = this.ranks.get(i);
		return newRanks;
	}

	@Override
	String toString(int i) {
		return this.ranks.get(i).toString();
	}
}
