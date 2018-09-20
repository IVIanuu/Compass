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

package com.ivianuu.compass.compiler

import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.service.AutoService
import com.ivianuu.compass.compiler.detour.DetourProviderProcessingStep
import com.ivianuu.compass.compiler.extension.ExtensionProcessingStep
import com.ivianuu.compass.compiler.route.RouteFactoryProcessingStep
import com.ivianuu.compass.compiler.route.RouteProviderProcessingStep
import com.ivianuu.compass.compiler.serializer.SerializerProcessingStep
import com.ivianuu.compass.compiler.serializer.SerializerProviderProcessingStep
import com.ivianuu.compass.compiler.serializer.SupportedTypes
import javax.annotation.processing.Processor

@AutoService(Processor::class)
class CompassProcessor : BasicAnnotationProcessor() {

    private val supportedTypes by lazy { SupportedTypes(processingEnv) }

    override fun initSteps(): MutableIterable<ProcessingStep> =
        mutableSetOf(
            SerializerProcessingStep(processingEnv, supportedTypes),
            SerializerProviderProcessingStep(processingEnv),
            RouteFactoryProcessingStep(processingEnv),
            RouteProviderProcessingStep(processingEnv),
            DetourProviderProcessingStep(processingEnv),
            ExtensionProcessingStep(processingEnv)
        )
}