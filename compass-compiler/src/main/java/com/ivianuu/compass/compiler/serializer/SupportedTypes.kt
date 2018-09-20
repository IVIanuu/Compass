/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.compass.compiler.serializer

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.VariableElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

data class SerializerAttributeDescriptor(
    val getFunction: BundleFunctionDescriptor,
    val getOrThrowFunction: BundleFunctionDescriptor,
    val putFunction: BundleFunctionDescriptor
)

data class BundleFunctionDescriptor(
    val name: String,
    val typeParameter: TypeName? = null,
    val import: Boolean = false
)

// todo support binder, size and size f
// todo support booleanarraylist, bytearraylist etc

class SupportedTypes(private val processingEnv: ProcessingEnvironment) {

    private val types = mutableMapOf(
        "boolean" to SerializerAttributeDescriptor(
            getFun("BooleanOrNull", import = true),
            getOrThrowFun("Boolean"),
            putFun("Boolean")
        ),
        "boolean[]" to SerializerAttributeDescriptor(
            getFun("BooleanArray"),
            getOrThrowFun("BooleanArray"),
            putFun("BooleanArray")
        ),
        "java.lang.Boolean" to SerializerAttributeDescriptor(
            getFun("BooleanOrNull", import = true),
            getOrThrowFun("Boolean"),
            putFun("Boolean")
        ),
        "java.util.List<java.lang.Boolean>" to SerializerAttributeDescriptor(
            getFun("BooleanList", import = true),
            getOrThrowFun("BooleanList"),
            putFun("BooleanList", import = true)
        ),
        "byte" to SerializerAttributeDescriptor(
            getFun("ByteOrNull", import = true),
            getOrThrowFun("Byte"),
            putFun("Byte")
        ),
        "byte[]" to SerializerAttributeDescriptor(
            getFun("ByteArray"),
            getOrThrowFun("ByteArray"),
            putFun("ByteArray")
        ),
        "java.lang.Byte" to SerializerAttributeDescriptor(
            getFun("ByteOrNull", import = true),
            getOrThrowFun("Byte"),
            putFun("Byte")
        ),
        "java.util.List<java.lang.Byte>" to SerializerAttributeDescriptor(
            getFun("ByteList", import = true),
            getOrThrowFun("ByteList"),
            putFun("ByteList", import = true)
        ),
        "char" to SerializerAttributeDescriptor(
            getFun("CharOrNull", import = true),
            getOrThrowFun("Char"),
            putFun("Char")
        ),
        "char[]" to SerializerAttributeDescriptor(
            getFun("CharArray"),
            getOrThrowFun("CharArray"),
            putFun("CharArray")
        ),
        "java.lang.Character" to SerializerAttributeDescriptor(
            getFun("CharOrNull", import = true),
            getOrThrowFun("Char"),
            putFun("Char")
        ),
        "java.util.List<java.lang.Character>" to SerializerAttributeDescriptor(
            getFun("CharList", import = true),
            getOrThrowFun("CharList"),
            putFun("CharList", import = true)
        ),
        "double" to SerializerAttributeDescriptor(
            getFun("DoubleOrNull", import = true),
            getOrThrowFun("Double"),
            putFun("Double")
        ),
        "double[]" to SerializerAttributeDescriptor(
            getFun("DoubleArray"),
            getOrThrowFun("DoubleArray"),
            putFun("DoubleArray")
        ),
        "java.lang.Double" to SerializerAttributeDescriptor(
            getFun("DoubleOrNull", import = true),
            getOrThrowFun("Double"),
            putFun("Double")
        ),
        "java.util.List<java.lang.Double>" to SerializerAttributeDescriptor(
            getFun("DoubleList", import = true),
            getOrThrowFun("DoubleList"),
            putFun("DoubleList", import = true)
        ),
        "float" to SerializerAttributeDescriptor(
            getFun("FloatOrNull", import = true),
            getOrThrowFun("Float"),
            putFun("Float")
        ),
        "float[]" to SerializerAttributeDescriptor(
            getFun("FloatArray"),
            getOrThrowFun("FloatArray"),
            putFun("FloatArray")
        ),
        "java.lang.Float" to SerializerAttributeDescriptor(
            getFun("FloatOrNull", import = true),
            getOrThrowFun("Float"),
            putFun("Float")
        ),
        "java.util.List<java.lang.Float>" to SerializerAttributeDescriptor(
            getFun("FloatList", import = true),
            getOrThrowFun("FloatList"),
            putFun("FloatList", import = true)
        ),
        "int" to SerializerAttributeDescriptor(
            getFun("IntOrNull", import = true),
            getOrThrowFun("Int"),
            putFun("Int")
        ),
        "int[]" to SerializerAttributeDescriptor(
            getFun("IntArray"),
            getOrThrowFun("IntArray"),
            putFun("IntArray")
        ),
        "java.lang.Integer" to SerializerAttributeDescriptor(
            getFun("IntOrNull", import = true),
            getOrThrowFun("Int"),
            putFun("Int")
        ),
        "java.util.List<java.lang.Integer>" to SerializerAttributeDescriptor(
            getFun("IntList", import = true),
            getOrThrowFun("IntList"),
            putFun("IntList", import = true)
        ),
        "long" to SerializerAttributeDescriptor(
            getFun("LongOrNull", import = true),
            getOrThrowFun("Long"),
            putFun("Long")
        ),
        "long[]" to SerializerAttributeDescriptor(
            getFun("LongArray"),
            getOrThrowFun("LongArray"),
            putFun("LongArray")
        ),
        "java.lang.Long" to SerializerAttributeDescriptor(
            getFun("LongOrNull", import = true),
            getOrThrowFun("Long"),
            putFun("Long")
        ),
        "java.util.List<java.lang.Long>" to SerializerAttributeDescriptor(
            getFun("LongList", import = true),
            getOrThrowFun("LongList"),
            putFun("LongList", import = true)
        ),
        "short" to SerializerAttributeDescriptor(
            getFun("ShortOrNull", import = true),
            getOrThrowFun("Short"),
            putFun("Short")
        ),
        "short[]" to SerializerAttributeDescriptor(
            getFun("ShortArray"),
            getOrThrowFun("ShortArray"),
            putFun("ShortArray")
        ),
        "java.lang.Short" to SerializerAttributeDescriptor(
            getFun("ShortOrNull", import = true),
            getOrThrowFun("Short"),
            putFun("Short")
        ),
        "java.util.List<java.lang.Short>" to SerializerAttributeDescriptor(
            getFun("ShortList", import = true),
            getOrThrowFun("ShortList"),
            putFun("ShortList", import = true)
        ),
        "java.lang.CharSequence" to SerializerAttributeDescriptor(
            getFun("CharSequence"),
            getOrThrowFun("CharSequence"),
            putFun("CharSequence")
        ),
        "java.lang.CharSequence[]" to SerializerAttributeDescriptor(
            getFun("CharSequenceArray"),
            getOrThrowFun("CharSequenceArray"),
            putFun("CharSequenceArray")
        ),
        "java.util.List<java.lang.CharSequence>" to SerializerAttributeDescriptor(
            getFun("CharSequenceList", import = true),
            getOrThrowFun("CharSequenceList", import = true),
            putFun("CharSequenceList", import = true)
        ),
        "java.lang.String" to SerializerAttributeDescriptor(
            getFun("String"),
            getOrThrowFun("String"),
            putFun("String")
        ),
        "java.lang.String[]" to SerializerAttributeDescriptor(
            getFun("StringArray"),
            getOrThrowFun("StringArray"),
            putFun("StringArray")
        ),
        "java.util.List<java.lang.String>" to SerializerAttributeDescriptor(
            getFun("StringList", import = true),
            getOrThrowFun("StringList", import = true),
            putFun("StringList", import = true)
        ),
        "java.util.ArrayList<java.lang.CharSequence>" to SerializerAttributeDescriptor(
            getFun("CharSequenceArrayList"),
            getOrThrowFun("CharSequenceArrayList"),
            putFun("CharSequenceArrayList")
        ),
        "java.util.ArrayList<java.lang.Integer>" to SerializerAttributeDescriptor(
            getFun("IntegerArrayList"),
            getOrThrowFun("IntegerArrayList"),
            putFun("IntegerArraylist")
        ),
        "java.util.ArrayList<java.lang.String>" to SerializerAttributeDescriptor(
            getFun("StringArrayList"),
            getOrThrowFun("StringArrayList"),
            putFun("StringArrayList")
        ),
        "android.os.Bundle" to SerializerAttributeDescriptor(
            getFun("Bundle"),
            getOrThrowFun("Bundle"),
            putFun("Bundle")
        ),
        "android.os.parcelable[]" to SerializerAttributeDescriptor(
            getFun("ParcelableArray"),
            getOrThrowFun("ParcelableArray"),
            putFun("ParcelableArray")
        )
    )

    fun isSupported(
        attribute: VariableElement
    ): Boolean = (types.containsKey(attribute.asType().toString())
            || checkExtraTypes(attribute))

    fun get(attribute: VariableElement): SerializerAttributeDescriptor =
        types[attribute.asType().toString()]!!

    private fun checkExtraTypes(attribute: VariableElement): Boolean {
        // parcelable
        if (processingEnv.typeUtils.isAssignable(
                attribute.asType(),
                processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
            )) {
            val typeName = attribute.asType().asTypeName()
            types[attribute.asType().toString()] =
                    SerializerAttributeDescriptor(
                        getFun("Parcelable", typeName),
                        getOrThrowFun("Parcelable", typeName),
                        putFun("Parcelable", typeName)
                    )
            return true
        }

        // parcelable arrays
        val arrayType = getArrayType(attribute)
        if (arrayType != null &&
            processingEnv.typeUtils.isAssignable(
                arrayType,
                processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
            )) {

            val arrayTypeName = arrayType.asTypeName()

            types[attribute.asType().toString()] =
                    SerializerAttributeDescriptor(
                        getFun("ParcelableArrayTyped", arrayTypeName, true),
                        getOrThrowFun("ParcelableArrayTyped", arrayTypeName),
                        putFun("ParcelableArrayTyped", arrayTypeName, true)
                    )
            return true
        }

        // parcelable list types
        val declaredType = attribute.asType() as? DeclaredType
        if (declaredType != null) {
            val typeArguments = declaredType.typeArguments

            if (typeArguments != null && typeArguments.size >= 1
                && processingEnv.typeUtils.isAssignable(
                    typeArguments[0],
                    processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
                )) {

                val listType = typeArguments[0]
                val listTypeName = listType.asTypeName()

                if (processingEnv.typeUtils.isAssignable(
                        processingEnv.typeUtils.erasure(declaredType),
                        processingEnv.elementUtils.getTypeElement("java.util.ArrayList").asType()
                    )
                ) {
                    types[attribute.asType().toString()] =
                            SerializerAttributeDescriptor(
                                getFun("ParcelableArrayList", listTypeName),
                                getOrThrowFun("ParcelableArrayList", listTypeName),
                                putFun("ParcelableArrayList", listTypeName)
                            )
                    return true
                } else if (processingEnv.typeUtils.isAssignable(
                        processingEnv.typeUtils.erasure(declaredType),
                        processingEnv.elementUtils.getTypeElement("java.util.List").asType()
                    )) {
                    types[attribute.asType().toString()] =
                            SerializerAttributeDescriptor(
                                getFun("ParcelableList", listTypeName, true),
                                getOrThrowFun("ParcelableList", listTypeName),
                                putFun("ParcelableList", listTypeName, true)
                            )
                    return true
                }

                if (processingEnv.typeUtils.isAssignable(
                        processingEnv.typeUtils.erasure(declaredType),
                        processingEnv.elementUtils.getTypeElement("android.util.SparseArray").asType()
                    )) {
                    types[attribute.asType().toString()] =
                            SerializerAttributeDescriptor(
                                getFun("SparseParcelableArray", listTypeName),
                                getOrThrowFun("SparseParcelableArray", listTypeName),
                                putFun("SparseParcelableArray", listTypeName)
                            )
                    return true
                }
            }
        }

        // serializable
        if (processingEnv.typeUtils.isAssignable(
                attribute.asType(),
                processingEnv.elementUtils.getTypeElement("java.io.Serializable").asType()
            )) {
            val typeName = attribute.asType().asTypeName()
            types[attribute.asType().toString()] =
                    SerializerAttributeDescriptor(
                        getFun("SerializableTyped", typeName, true),
                        getOrThrowFun("SerializableTyped", typeName),
                        putFun("SerializableTyped", typeName, true)
                    )
            return true
        }

        return false
    }

    private fun getArrayType(field: Element): TypeMirror? {
        val typeMirror = field.asType()
        return if (typeMirror is ArrayType) {
            typeMirror.componentType
        } else {
            null
        }
    }
}

private fun getFun(
    typeName: String,
    typeParameter: TypeName? = null,
    import: Boolean = false
) =
    BundleFunctionDescriptor("get$typeName", typeParameter, import)

private fun getOrThrowFun(
    typeName: String,
    typeParameter: TypeName? = null,
    import: Boolean = true
) =
    BundleFunctionDescriptor("get${typeName}OrThrow", typeParameter, import)

private fun putFun(
    typeName: String,
    typeParameter: TypeName? = null,
    import: Boolean = false
) =
    BundleFunctionDescriptor("put$typeName", typeParameter, import)