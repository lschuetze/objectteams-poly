/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany, and others.
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
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.samples.ordersystem.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public abstract class AbstractTableModelTemplate<T> extends AbstractTableModel {
    
    protected abstract String[] getColumnNames();

    public abstract Vector<T> getElements();
    
    public abstract Object getValueAt(int aRowIndex, int aColumnIndex);
    
    /**
     * @param aRowIndex an index
     * @return the element (row) at given index
     */
    public Object getElementAt(int aRowIndex) {
        if (aRowIndex >= 0) {
            return getElements().elementAt(aRowIndex);
        }
        else {
            return null;
        }
    }
    
    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return getElements().size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return getColumnNames().length;
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int aColumn) {
        return getColumnNames()[aColumn];
    }


}
