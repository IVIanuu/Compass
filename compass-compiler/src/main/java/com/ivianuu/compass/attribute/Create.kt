package com.ivianuu.compass.attribute

import com.ivianuu.compass.attribute.array.*
import com.ivianuu.compass.attribute.boxedprimitives.*
import com.ivianuu.compass.attribute.list.*

fun attributeSerializers(): List<AttributeSerializeLogic> = listOf(
    PrimitiveAttributeSerializeLogic(),
    ParcelableAttributeSerializeLogic(),
    StringAttributeSerializeLogic(),
    IntListAttributeSerializeLogic(),
    FloatListAttributeSerializeLogic(),
    DoubleListAttributeSerializeLogic(),
    CharListAttributeSerializeLogic(),
    BooleanListAttributeSerializeLogic(),
    LongListAttributeSerializeLogic(),
    ShortListAttributeSerializeLogic(),
    IntArrayAttributeSerializeLogic(),
    ShortArrayAttributeSerializeLogic(),
    FloatArrayAttributeSerializeLogic(),
    DoubleArrayAttributeSerializeLogic(),
    LongArrayAttributeSerializeLogic(),
    BooleanArrayAttributeSerializeLogic(),
    CharArrayAttributeSerializeLogic(),
    ByteArrayAttributeSerializeLogic(),
    ParcelableArrayAttributeSerializeLogic(),
    StringArrayAttributeSerializeLogic(),
    StringListAttributeSerializeLogic(),
    ParcelableListAttributeSerializeLogic(),
    BoxedIntegerAttributeSerializeLogic(),
    BoxedFloatAttributeSerializeLogic(),
    BoxedDoubleAttributeSerializeLogic(),
    BoxedShortAttributeSerializeLogic(),
    BoxedBooleanAttributeSerializeLogic()
)