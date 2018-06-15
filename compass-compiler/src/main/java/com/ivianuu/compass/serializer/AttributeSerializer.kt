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

import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement


class AttributeSerializer(private val processingEnv: ProcessingEnvironment) {

    fun createBundlePut(
        builder: FunSpec.Builder,
        attribute: VariableElement,
        baseElement: TypeElement,
        valueName: String
    ) {
        val typeMapping = SupportedTypes.get(attribute)

        builder.addStatement(
            "val $valueName = destination.$valueName"
        )

        builder.addStatement(
            "bundle.put${typeMapping.typeMapping}(\"${bundleArgumentName(attribute, baseElement)}\", " +
                    (if (typeMapping.wrapInArrayList) "ArrayList($valueName))" else "$valueName)")
        )
    }

    fun createBundleGet(
        builder: FunSpec.Builder,
        attribute: VariableElement,
        baseElement: TypeElement,
        valueName: String
    ) {
        val typeMapping = SupportedTypes.get(attribute)

        builder.addStatement(
            "val $valueName = " +
                    "bundle" + ".get${typeMapping.typeMapping}" +
                    (if (typeMapping.typeParameter != null) "<${typeMapping.typeParameter}>" else "") +
                    "(\"${bundleArgumentName(attribute, baseElement)}\") " +
                    (if (typeMapping.castTo != null) "as ${typeMapping.castTo}" else "")
        )
    }

    private fun bundleArgumentName(element: VariableElement, destination: TypeElement) =
        destination.asType().toString() + ".${element.simpleName}"
}