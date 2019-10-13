package com.kyle.support.activitystarter.compiler.model;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * The support type when we use bundle.putExtra(key, value);
 */
public enum BundleExtraType {

    Boolean,
    Byte,
    Short,
    Int,
    Long,
    Char,
    Float,
    Double,
    String,
    CharSequence,
    Parcelable,
    Serializable,
    Bundle,

    BooleanArray,
    ByteArray,
    ShortArray,
    CharArray,
    IntArray,
    LongArray,
    FloatArray,
    DoubleArray,
    StringArray,
    CharSequenceArray,
    ParcelableArray,

    IntegerArrayList,
    StringArrayList,
    CharSequenceArrayList,
    ParcelableArrayList,

    Unknown;

    public TypeName getTypeName(TypeMirror typeMirror) {
        switch (this) {
            case Boolean:
            case Byte:
            case Short:
            case Int:
            case Long:
            case Char:
            case Float:
            case Double:
                return TypeName.get(typeMirror);
            case BooleanArray:
            case ByteArray:
            case ShortArray:
            case CharArray:
            case IntArray:
            case LongArray:
            case FloatArray:
            case DoubleArray:
            case StringArray:
            case CharSequenceArray:
            case ParcelableArray:
                return ArrayTypeName.get(typeMirror);
            case IntegerArrayList:
            case StringArrayList:
            case CharSequenceArrayList:
            case ParcelableArrayList:
                return ParameterizedTypeName.get(ClassName.bestGuess("java.util.ArrayList"), getRawType(typeMirror));
            default:
                return ClassName.bestGuess(typeMirror.toString());
        }
    }

    public static BundleExtraType get(VariableElement variableElement) {
        TypeMirror typeMirror = variableElement.asType();
        switch (typeMirror.getKind()) {
            // Primitive Type
            case BOOLEAN:
                return Boolean;
            case BYTE:
                return Byte;
            case SHORT:
                return Short;
            case INT:
                return Int;
            case LONG:
                return Long;
            case CHAR:
                return Char;
            case FLOAT:
                return Float;
            case DOUBLE:
                return Double;
            // Array Type
            case ARRAY:
                return getArrayType((ArrayType) typeMirror);
            // Declared Type
            case DECLARED:
                return getDeclaredType((DeclaredType) typeMirror);
            default:
                return Unknown;
        }
    }

    private static BundleExtraType getArrayType(ArrayType arrayType) {
        TypeMirror componentType = arrayType.getComponentType();
        switch (componentType.toString()) {
            case "boolean":
            case "java.lang.Boolean":
                return BooleanArray;
            case "byte":
            case "java.lang.Byte":
                return ByteArray;
            case "short":
            case "java.lang.Short":
                return ShortArray;
            case "int":
            case "java.lang.Integer":
                return IntArray;
            case "long":
            case "java.lang.Long":
                return LongArray;
            case "char":
            case "java.lang.Character":
                return CharArray;
            case "float":
            case "java.lang.Float":
                return FloatArray;
            case "double":
            case "java.lang.Double":
                return DoubleArray;
            case "java.lang.String":
                return StringArray;
            case "java.lang.CharSequence":
                return CharSequenceArray;
        }
        if (getBySupertype((DeclaredType) componentType) == Parcelable) {
            return ParcelableArray;
        }
        return Unknown;
    }

    private static BundleExtraType getDeclaredType(DeclaredType declaredType) {
        switch (declaredType.toString()) {
            case "java.lang.Boolean":
                return Boolean;
            case "java.lang.Byte":
                return Byte;
            case "java.lang.Short":
                return Short;
            case "java.lang.Integer":
                return Int;
            case "java.lang.Long":
                return Long;
            case "java.lang.Character":
                return Char;
            case "java.lang.Float":
                return Float;
            case "java.lang.Double":
                return Double;
            case "java.lang.String":
                return String;
            case "java.lang.CharSequence":
                return CharSequence;
            case "android.os.Bundle":
                return Bundle;
            case "java.util.ArrayList<java.lang.Integer>":
                return IntegerArrayList;
            case "java.util.ArrayList<java.lang.String>":
                return StringArrayList;
            case "java.util.ArrayList<java.lang.CharSequence>":
                return CharSequenceArrayList;
            default:
                return getBySupertype(declaredType);
        }
    }

    private static BundleExtraType getBySupertype(DeclaredType declaredType) {
        if (!declaredType.toString().startsWith("java.util.ArrayList<")) {
            List<? extends TypeMirror> interfaces = ((TypeElement) declaredType.asElement()).getInterfaces();
            if (interfaces != null && interfaces.size() > 0) {
                for (TypeMirror anInterface : interfaces) {
                    switch (anInterface.toString()) {
                        case "android.os.Parcelable":
                            return Parcelable;
                        case "java.io.Serializable":
                            return Serializable;
                    }
                }
            }
        } else {
            BundleExtraType genericType = getBySupertype((DeclaredType) declaredType.getTypeArguments().get(0));
            System.out.println("genericType : " + genericType.toString());
            if (genericType == Parcelable) {
                return ParcelableArrayList;
            }
        }
        return Unknown;
    }

    public static TypeName getRawType(TypeMirror typeMirror) {
        String type = typeMirror.toString();
        if (!type.contains("<")) {
            return null;
        }
        String rawType = type.split("<")[1];
        rawType = rawType.substring(0, rawType.lastIndexOf(">"));
        return ClassName.bestGuess(rawType);
    }
}
