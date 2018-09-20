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

package com.ivianuu.compass.playground

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ivianuu.compass.Destination
import com.ivianuu.compass.RouteFactory
import com.ivianuu.compass.android.ActivityRouteFactory

/**
 * @author Manuel Wrage (IVIanuu)
 */
@RouteFactory(WebsiteRouteFactory::class)
@Destination
data class WebsiteDestination(val url : String)

object WebsiteRouteFactory : ActivityRouteFactory<WebsiteDestination> {
    override fun createActivityIntent(context: Context, destination: WebsiteDestination): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(destination.url)
        }
    }
}