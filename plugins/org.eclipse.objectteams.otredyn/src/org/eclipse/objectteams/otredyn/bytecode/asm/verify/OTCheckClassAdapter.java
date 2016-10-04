/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2016 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm.verify;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class OTCheckClassAdapter extends org.objectweb.asm.util.CheckClassAdapter {

	static final int AccTeam = 0x8000;

	/**
	 * A print writer that captures the last argument to {@link PrintWriter#println(String)}.
	 */
	static class CapturingPrintWriter extends PrintWriter {
		String errorText; 

		private CapturingPrintWriter(OutputStream out) {
			super(out);
		}

		public void println(String x) {
			super.println(x);
			this.errorText = x;
		}
	}
	
	
	/**
	 * A class loader that can return already loaded classes,
	 * but instead of loading new classes it throws {@link LoadAttempted}.
	 */
	static class ShyLoader extends ClassLoader {

		@SuppressWarnings("serial")
		static class LoadAttempted extends RuntimeException { }

		Method findLoadedClass;
		
		public ShyLoader(ClassLoader parent) {
			super(parent);
		}
		
		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			try {
				Class<?> loaded = findLoadedFromParent(name); // don't attempt real class loading!
				if (loaded != null)
					return loaded;
			} catch (Exception e) {
			} 
			throw new LoadAttempted();
		}

		Class<?> findLoadedFromParent(String name) throws Exception {
			if (findLoadedClass == null) {
				findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
				findLoadedClass.setAccessible(true);
			}
			ClassLoader parent = getParent();
			while (parent != null) {
				Class<?> c = (Class<?>) findLoadedClass.invoke(parent, name);
				if (c != null)
					return c;
				parent = parent.getParent();
			}
			return null;
		}
	}

	/**
	 * Use the correct class loader for verification (wrapped in a ShyLoader to only find loaded classes).
	 * Short-cut verification that would need real class loading.
	 */
	static class OTVerifier extends SimpleVerifier {
		
		ClassLoader loader;
		
		public OTVerifier(Type objectType, Type syperType, List<Type> interfaces, boolean isInterface, ClassLoader loader) {
			super(objectType, syperType, interfaces, isInterface);
			this.loader = new ShyLoader(loader);
		}

		@Override
		protected boolean isAssignableFrom(Type t, Type u) {
			try {
				return super.isAssignableFrom(t, u);
			} catch (ShyLoader.LoadAttempted e) {
				return true; // if we cannot verify without loading classes, just answer 'true' for now
			}
		}

		/** Same as super, but use our local ClassLoader. */
		@Override
	    protected Class<?> getClass(final Type t) {
	        try {
	            if (t.getSort() == Type.ARRAY) {
	                return Class.forName(t.getDescriptor().replace('/', '.'),
	                        false, loader);
	            }
	            return Class.forName(t.getClassName(), false, loader);
	        } catch (ClassNotFoundException e) {
	            throw new RuntimeException(e.toString());
	        }
	    }
	}

	public OTCheckClassAdapter(ClassVisitor cv, boolean checkDataFlow) {
		super(Opcodes.ASM5, cv, checkDataFlow);
	}

	/**
	 * Invoke {@link CheckClassAdapter#verify(ClassReader, ClassLoader, boolean, PrintWriter)}
	 * set up to use an instance of {@link OTCheckClassAdapter}.
	 */
	public static void verify(ClassNode node, byte[] bytes, ClassLoader loader) throws VerifyError {
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		try (CapturingPrintWriter printWriter = new CapturingPrintWriter(out)) {
			verify(new ClassReader(bytes), loader, false, printWriter);
			if (printWriter.errorText != null) {
				StringBuilder message = new StringBuilder();
				message.append(node.getClass().getSimpleName());
				message.append(" caused a verify error on ");
				message.append(node.name).append('.').append(printWriter.errorText);
				message.append('\n');
				message.append(out.toString());
				throw new VerifyError(message.toString());
			}
		}
	}

    public static void verify(final ClassReader cr, final ClassLoader loader,
            final boolean dump, final PrintWriter pw) {
        ClassNode cn = new ClassNode();
//{ObjectTeams: instantiate OTCheckClassAdapter:
        cr.accept(new OTCheckClassAdapter(cn, false), ClassReader.SKIP_DEBUG);
// SH}
        
        Type syperType = cn.superName == null ? null : Type
                .getObjectType(cn.superName);
        List<MethodNode> methods = cn.methods;

        List<Type> interfaces = new ArrayList<Type>();
        for (Iterator<String> i = cn.interfaces.iterator(); i.hasNext();) {
            interfaces.add(Type.getObjectType(i.next()));
        }

        for (int i = 0; i < methods.size(); ++i) {
            MethodNode method = methods.get(i);
//{ObjectTeams: instantiate OTVerifier and pass loader:
            SimpleVerifier verifier = new OTVerifier(
                    Type.getObjectType(cn.name), syperType, interfaces,
                    (cn.access & Opcodes.ACC_INTERFACE) != 0,
                    loader);
// SH}
            Analyzer<BasicValue> a = new Analyzer<BasicValue>(verifier);
            if (loader != null) {
                verifier.setClassLoader(loader);
            }
            try {
                a.analyze(cn.name, method);
                if (!dump) {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace(pw);
            }
            printAnalyzerResult(method, a, pw);
        }
        pw.flush();
    }
    

    static void printAnalyzerResult(MethodNode method, Analyzer<BasicValue> a, final PrintWriter pw) {
        Frame<BasicValue>[] frames = a.getFrames();
        Textifier t = new Textifier();
        TraceMethodVisitor mv = new TraceMethodVisitor(t);

        pw.println(method.name + method.desc);
        for (int j = 0; j < method.instructions.size(); ++j) {
            method.instructions.get(j).accept(mv);

            StringBuilder sb = new StringBuilder();
            Frame<BasicValue> f = frames[j];
            if (f == null) {
                sb.append('?');
            } else {
                for (int k = 0; k < f.getLocals(); ++k) {
                    sb.append(getShortName(f.getLocal(k).toString()))
                            .append(' ');
                }
                sb.append(" : ");
                for (int k = 0; k < f.getStackSize(); ++k) {
                    sb.append(getShortName(f.getStack(k).toString()))
                            .append(' ');
                }
            }
            while (sb.length() < method.maxStack + method.maxLocals + 1) {
                sb.append(' ');
            }
            pw.print(Integer.toString(j + 100000).substring(1));
            pw.print(" " + sb + " : " + t.text.get(t.text.size() - 1));
        }
        for (int j = 0; j < method.tryCatchBlocks.size(); ++j) {
            method.tryCatchBlocks.get(j).accept(mv);
            pw.print(" " + t.text.get(t.text.size() - 1));
        }
        pw.println();
    }

    private static String getShortName(final String name) {
        int n = name.lastIndexOf('/');
        int k = name.length();
        if (name.charAt(k - 1) == ';') {
            k--;
        }
        return n == -1 ? name : name.substring(n + 1, k);
    }

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// tolerate AccTeam in a class's access flags:
		super.visit(version, adjustClassFlags(access), name, signature, superName, interfaces);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		// tolerate AccTeam in a class's access flags:
		super.visitInnerClass(name, outerName, innerName, adjustClassFlags(access));
	}

	protected int adjustClassFlags(int access) {
		return access & ~AccTeam;
	}
}
