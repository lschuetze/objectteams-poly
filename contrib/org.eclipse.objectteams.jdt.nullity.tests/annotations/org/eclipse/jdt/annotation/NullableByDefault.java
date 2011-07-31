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
