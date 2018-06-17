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

package com.ivianuu.compass.serializer

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.VariableElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

data class SerializerAttributeDescriptor(
    val typeMapping: String,
    val castTo: String? = null,
    val typeParameter: String? = null,
    val wrapInArrayList: Boolean = false
)

class SupportedTypes(private val processingEnv: ProcessingEnvironment) {

    private val TYPES: MutableMap<String, SerializerAttributeDescriptor> = mutableMapOf(
        "boolean" to SerializerAttributeDescriptor("Boolean"),
        "boolean[]" to SerializerAttributeDescriptor("BooleanArray"),
        "byte" to SerializerAttributeDescriptor("Byte"),
        "byte[]" to SerializerAttributeDescriptor("ByteArray"),
        "char" to SerializerAttributeDescriptor("Char"),
        "char[]" to SerializerAttributeDescriptor("CharArray"),
        "double" to SerializerAttributeDescriptor("Double"),
        "double[]" to SerializerAttributeDescriptor("DoubleArray"),
        "float" to SerializerAttributeDescriptor("Float"),
        "float[]" to SerializerAttributeDescriptor("FloatArray"),
        "int" to SerializerAttributeDescriptor("Int"),
        "int[]" to SerializerAttributeDescriptor("IntArray"),
        "long" to SerializerAttributeDescriptor("Long"),
        "long[]" to SerializerAttributeDescriptor("LongArray"),
        "short" to SerializerAttributeDescriptor("Short"),
        "short[]" to SerializerAttributeDescriptor("ShortArray"),
        "java.lang.CharSequence" to SerializerAttributeDescriptor("CharSequence"),
        "java.lang.CharSequence[]" to SerializerAttributeDescriptor("CharSequenceArray"),
        "java.lang.String" to SerializerAttributeDescriptor("String"),
        "java.lang.String[]" to SerializerAttributeDescriptor("StringArray"),
        "java.util.ArrayList<java.lang.CharSequence>" to SerializerAttributeDescriptor("CharSequenceArrayList"),
        "java.util.ArrayList<java.lang.Integer>" to SerializerAttributeDescriptor("IntegerArrayList"),
        "java.util.ArrayList<java.lang.String>" to SerializerAttributeDescriptor("StringArrayList"),
        "android.os.Bundle" to SerializerAttributeDescriptor("Bundle"),
        "android.os.parcelable[]" to SerializerAttributeDescriptor("ParcelableArray")
    )

    fun isSupported(
        attribute: VariableElement
    ): Boolean {
        return TYPES.containsKey(attribute.asType().toString())
                || checkExtraTypes(attribute)
    }

    fun get(attribute: VariableElement): SerializerAttributeDescriptor {
        return TYPES[attribute.asType().toString()]!!
    }

    private fun checkExtraTypes(attribute: VariableElement): Boolean {
        // parcelable
        if (processingEnv.typeUtils.isAssignable(
                attribute.asType(),
                processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
            )) {
            TYPES[attribute.asType().toString()] =
                    SerializerAttributeDescriptor("Parcelable", null,
                        attribute.asType().toString())
            return true
        }

        // parcelable arrays
        val arrayType = getArrayType(attribute)
        if (arrayType != null &&
            processingEnv.typeUtils.isAssignable(
                arrayType,
                processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
            )) {
            TYPES[attribute.asType().toString()] =
                    SerializerAttributeDescriptor("ParcelableArray", "Array<$arrayType>")
            return true
        }

        // parcelable list
        val declaredType = attribute.asType() as? DeclaredType
        if (declaredType != null) {
            val typeArguments = declaredType.typeArguments

            if (typeArguments != null && typeArguments.size >= 1
                && processingEnv.typeUtils.isAssignable(
                    typeArguments[0],
                    processingEnv.elementUtils.getTypeElement("android.os.Parcelable").asType()
                )) {

                if (processingEnv.typeUtils.isAssignable(
                        processingEnv.typeUtils.erasure(declaredType),
                        processingEnv.elementUtils.getTypeElement("java.util.List").asType()
                    )) {
                    TYPES[attribute.asType().toString()] =
                            SerializerAttributeDescriptor("ParcelableArrayList", null,
                                typeArguments[0].toString(), true)
                    return true
                }

                if (processingEnv.typeUtils.isAssignable(
                        processingEnv.typeUtils.erasure(declaredType),
                        processingEnv.elementUtils.getTypeElement("android.util.SparseArray").asType()
                    )) {
                    TYPES[attribute.asType().toString()] =
                            SerializerAttributeDescriptor("SparseParcelableArray", null,
                                typeArguments[0].toString())
                    return true
                }
            }
        }

        // serializable
        if (processingEnv.typeUtils.isAssignable(
                attribute.asType(),
                processingEnv.elementUtils.getTypeElement("java.io.Serializable").asType()
            )) {
            TYPES[attribute.asType().toString()] =
                    SerializerAttributeDescriptor("Serializable", attribute.asType().toString())
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