/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.flavour.mp.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Set;
import org.teavm.flavour.mp.ReflectClass;
import org.teavm.flavour.mp.reflect.ReflectField;
import org.teavm.flavour.mp.reflect.ReflectMethod;
import org.teavm.model.ClassReader;
import org.teavm.model.ElementModifier;
import org.teavm.model.ValueType;

/**
 *
 * @author Alexey Andreev
 */
public class ReflectClassImpl<T> implements ReflectClass<T> {
    private ValueType type;
    private ReflectContext context;
    private ClassReader classReader;
    private boolean resolved;
    private Class<?> cls;

    public ReflectClassImpl(ValueType type, ReflectContext context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public boolean isPrimitive() {
        return type instanceof ValueType.Primitive;
    }

    @Override
    public boolean isInterface() {
        resolve();
        return classReader != null && classReader.readModifiers().contains(ElementModifier.INTERFACE);
    }

    @Override
    public boolean isArray() {
        return type instanceof ValueType.Array;
    }

    @Override
    public boolean isAnnotation() {
        resolve();
        return classReader != null && classReader.readModifiers().contains(ElementModifier.ANNOTATION);
    }

    @Override
    public boolean isEnum() {
        resolve();
        return classReader != null && classReader.readModifiers().contains(ElementModifier.ENUM);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getEnumConstants() {
        resolve();
        if (classReader == null) {
            return null;
        }
        if (cls == null) {
            try {
                cls = Class.forName(classReader.getName(), true, context.getClassLoader());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return (T[]) cls.getEnumConstants();
    }

    @Override
    public int getModifiers() {
        resolve();
        if (classReader == null) {
            return 0;
        }
        int modifiers = 0;
        switch (classReader.getLevel()) {
            case PUBLIC:
                modifiers |= Modifier.PUBLIC;
                break;
            case PROTECTED:
                modifiers |= Modifier.PROTECTED;
                break;
            case PRIVATE:
                modifiers |= Modifier.PRIVATE;
                break;
            case PACKAGE_PRIVATE:
                break;
        }
        Set<ElementModifier> modifierSet = classReader.readModifiers();
        if (modifierSet.contains(ElementModifier.ABSTRACT)) {
            modifiers |= Modifier.ABSTRACT;
        }
        if (modifierSet.contains(ElementModifier.FINAL)) {
            modifiers |= Modifier.FINAL;
        }
        if (modifierSet.contains(ElementModifier.INTERFACE)) {
            modifiers |= Modifier.INTERFACE;
        }
        if (modifierSet.contains(ElementModifier.NATIVE)) {
            modifiers |= Modifier.NATIVE;
        }
        if (modifierSet.contains(ElementModifier.STATIC)) {
            modifiers |= Modifier.STATIC;
        }
        if (modifierSet.contains(ElementModifier.STRICT)) {
            modifiers |= Modifier.STRICT;
        }
        if (modifierSet.contains(ElementModifier.SYNCHRONIZED)) {
            modifiers |= Modifier.SYNCHRONIZED;
        }
        if (modifierSet.contains(ElementModifier.TRANSIENT)) {
            modifiers |= Modifier.TRANSIENT;
        }
        if (modifierSet.contains(ElementModifier.VOLATILE)) {
            modifiers |= Modifier.VOLATILE;
        }
        return modifiers;
    }

    @Override
    public ReflectClass<?> getComponentType() {
        if (!(type instanceof ValueType.Array)) {
            return null;
        }
        ValueType componentType = ((ValueType.Array) type).getItemType();
        return context.getClass(componentType);
    }

    @Override
    public String getName() {
        if (type instanceof ValueType.Object) {
            return ((ValueType.Object) type).getClassName();
        } else if (type instanceof ValueType.Void) {
            return "void";
        } else if (type instanceof ValueType.Primitive) {
            switch (((ValueType.Primitive) type).getKind()) {
                case BOOLEAN:
                    return "boolean";
                case BYTE:
                    return "byte";
                case SHORT:
                    return "short";
                case CHARACTER:
                    return "char";
                case INTEGER:
                    return "int";
                case LONG:
                    return "long";
                case FLOAT:
                    return "float";
                case DOUBLE:
                    return "double";
                default:
                    return "";
            }
        } else if (type instanceof ValueType.Array) {
            return type.toString().replace('/', '.');
        } else {
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReflectClass<? super T> getSuperclass() {
        resolve();
        if (classReader == null || classReader.getParent() == null
                || classReader.getName().equals(classReader.getParent())) {
            return null;
        }
        return (ReflectClass<? super T>) context.getClass(new ValueType.Object(classReader.getParent()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReflectClass<? super T>[] getInterfaces() {
        resolve();
        if (classReader == null) {
            return (ReflectClass<? super T>[]) Array.newInstance(ReflectClassImpl.class, 0);
        }
        return classReader.getInterfaces().stream()
                .map(iface -> context.getClass(new ValueType.Object(iface)))
                .toArray(sz -> (ReflectClass<? super T>[]) Array.newInstance(ReflectClassImpl.class, sz));
    }

    @Override
    public boolean isInstance(Object obj) {
        throw new IllegalStateException("Can call this method only from runtime domain");
    }

    @Override
    public <U> ReflectClass<U> asSubclass(Class<U> cls) {
        return null;
    }

    @Override
    public ReflectMethod[] getDeclaringMethods() {
        return null;
    }

    @Override
    public ReflectMethod[] getMethods() {
        return null;
    }

    @Override
    public ReflectMethod getDeclaringMethod(String name, ReflectClass<?>... parameterTypes) {
        return null;
    }

    @Override
    public ReflectMethod getMethod(String name, ReflectClass<?>... parameterTypes) {
        return null;
    }

    @Override
    public ReflectField[] getDeclaringFields() {
        return null;
    }

    @Override
    public ReflectField[] getFields() {
        return null;
    }

    @Override
    public ReflectField getDeclaringField(String name) {
        return null;
    }

    @Override
    public ReflectField getField(String name) {
        return null;
    }

    private void resolve() {
        if (resolved) {
            return;
        }
        resolved = true;
        if (!(type instanceof ValueType.Object)) {
            return;
        }

        String className = ((ValueType.Object) type).getClassName();
        classReader = context.getClassSource().get(className);
    }
}