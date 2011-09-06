/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.jdt.annotation;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.*;
 
/**
 * <blockquote>
 * This annotation is intended for use by the Eclipse Java Compiler in order to
 * support intra-procedural null analysis. Please see the original 
 * <a href="http://bugs.eclipse.org/bugs/186342">Bug 186342- [compiler][null] Using annotations for null checking</a> 
 * and the <a href="http://wiki.eclipse.org/JDT_Core/Null_Analysis">Wiki page</a>
 * for status and availability of the implementation of these analyses.
 * </blockquote> 
 * <p>
 * This annotation can be applied to a package or a type in order to define that
 * all contained entities for which a null annotation is otherwise lacking
 * should be considered as @{@link Nullable}.
 * <dl>
 * <dt>Interaction with inheritance</dt>
 * <dd>This annotation has lower precedence than null contract inheritance,
 * i.e., for a method with no explicit null annotations first inheritance
 * from the super-method (overridden or implemented) are considered.
 * Only if that search yields no null annotation the default defined using
 * <code>@NullableByDefault</code> is applied.</dd>
 * <dt>Nested defaults</dt>
 * <dd>If a <code>@NullableByDefault</code>
 * annotation is used within the scope of a <code>@NonNullByDefault</code>
 * annotation the inner most annotation defines the default applicable at 
 * any given position.</dd>
 * </dl>
 * Note that for applying an annotation to a package a file by the name
 * <code>package-info.java</code> is used.
 * 
 * @author stephan
 */
@Retention(RetentionPolicy.CLASS)
@Target({PACKAGE,TYPE})
public @interface NullableByDefault {

}
