/*
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1997-2000 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 * Igor Bukanov
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the GNU Public License (the "GPL"), in which case the
 * provisions of the GPL are applicable instead of those above.
 * If you wish to allow use of your version of this file only
 * under the terms of the GPL and not to allow others to use your
 * version of this file under the NPL, indicate your decision by
 * deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL.  If you do not delete
 * the provisions above, a recipient may use your version of this
 * file under either the NPL or the GPL.
 */

package org.mozilla.javascript.optimizer;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.mozilla.javascript.*;


public class ClassCompiler
{
    public ClassCompiler(CompilerEnvirons compilerEnv)
    {
        if (compilerEnv == null) throw new IllegalArgumentException();
        this.compilerEnv = compilerEnv;
    }

    public CompilerEnvirons getCompilerEnv()
    {
        return compilerEnv;
    }

    public Class getTargetExtends()
    {
        return targetExtends;
    }

    public void setTargetExtends(Class extendsClass)
    {
        targetExtends = extendsClass;
    }

    public Class[] getTargetImplements()
    {
        return targetImplements;
    }

    public void setTargetImplements(Class[] implementsClasses)
    {
        targetImplements = implementsClasses;
    }

    protected String makeAuxiliaryClassName(String mainClassName,
                                            String auxMarker)
    {
        return mainClassName+auxMarker;
    }

    /**
     * Compile JavaScript source into one or more Java class files.
     * The first compiled class will have name mainClassName.
     * If the results of {@link #getTargetExtends()} or
     * {@link getTargetImplements()} are not null, then the first compiled
     * class will extend the specified super class and implement
     * specified interfaces.
     *
     * @return array where elements with even indexes specifies class name
     *         and the followinf odd index gives class file body as byte[]
     *         array. The initial elemnt of the array always holds
     *         mainClassName and array[1] holds its byte code.
     */
    public Object[] compileToClassFiles(String source,
                                        String sourceLocation,
                                        int lineno,
                                        String mainClassName)
    {
        Parser p = new Parser(compilerEnv);
        ScriptOrFnNode tree = p.parse(source, sourceLocation, lineno);
        int syntaxErrorCount = compilerEnv.getSyntaxErrorCount();
        if (syntaxErrorCount == 0) {
            String encodedSource = p.getEncodedSource();

            Class superClass = getTargetExtends();
            Class[] interfaces = getTargetImplements();
            String scriptClassName;
            boolean isPrimary = (interfaces == null && superClass == null);
            if (isPrimary) {
                scriptClassName = mainClassName;
            } else {
                scriptClassName = makeAuxiliaryClassName(mainClassName, "1");
            }

            Codegen codegen = new Codegen();
            byte[] scriptClassBytes
                = codegen.compileToClassFile(compilerEnv, scriptClassName,
                                             tree, encodedSource,
                                             false);

            syntaxErrorCount = compilerEnv.getSyntaxErrorCount();
            if (syntaxErrorCount == 0) {
                if (isPrimary) {
                    return new Object[] { scriptClassName, scriptClassBytes };
                }
                int functionCount = tree.getFunctionCount();
                ObjToIntMap functionNames = new ObjToIntMap(functionCount);
                for (int i = 0; i != functionCount; ++i) {
                    FunctionNode ofn = tree.getFunctionNode(i);
                    String name = ofn.getFunctionName();
                    if (name != null && name.length() != 0) {
                        functionNames.put(name, ofn.getParamCount());
                    }
                }
                if (superClass == null) {
                    superClass = ScriptRuntime.ObjectClass;
                }
                byte[] mainClassBytes
                    = JavaAdapter.createAdapterCode(
                        functionNames, mainClassName,
                        superClass, interfaces, scriptClassName);

                return new Object[] { mainClassName, mainClassBytes,
                                      scriptClassName, scriptClassBytes };
            }
        }
        String msg = ScriptRuntime.getMessage1(
            "msg.got.syntax.errors", String.valueOf(syntaxErrorCount));
        throw compilerEnv.getErrorReporter().
            runtimeError(msg, sourceLocation, lineno, null, 0);
    }

    private CompilerEnvirons compilerEnv;
    private Class targetExtends;
    private Class[] targetImplements;

}

