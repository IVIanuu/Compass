package com.ivianuu.compass

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.lang.IllegalStateException

fun Bundle.getBooleanOrNull(key: String): Boolean? {
    if (!containsKey(key)) return null
    return getBoolean(key)
}

fun Bundle.getByteOrNull(key: String): Byte? {
    if (!containsKey(key)) return null
    return getByte(key)
}

fun Bundle.getCharOrNull(key: String): Char? {
    if (!containsKey(key)) return null
    return getChar(key)
}

fun Bundle.getShortOrNull(key: String): Short? {
    if (!containsKey(key)) return null
    return getShort(key)
}

fun Bundle.getIntOrNull(key: String): Int? {
    if (!containsKey(key)) return null
    return getInt(key)
}

fun Bundle.getLongOrNull(key: String): Long? {
    if (!containsKey(key)) return null
    return getLong(key)
}

fun Bundle.getFloatOrNull(key: String): Float? {
    if (!containsKey(key)) return null
    return getFloat(key)
}

fun Bundle.getDoubleOrNull(key: String): Double? {
    if (!containsKey(key)) return null
    return getDouble(key)
}

fun Bundle.getBooleanList(key: String) = getBooleanArray(key).toList()

fun Bundle.putBooleanList(key: String, value: List<Boolean>?) {
    if (value == null) return
    putBooleanArray(key, value.toBooleanArray())
}

fun Bundle.getByteList(key: String) = getByteArray(key).toList()

fun Bundle.putByteList(key: String, value: List<Byte>?) {
    if (value == null) return
    putByteArray(key, value.toByteArray())
}

fun Bundle.getCharList(key: String) = getCharArray(key).toList()

fun Bundle.putCharList(key: String, value: List<Char>?) {
    if (value == null) return
    putCharArray(key, value.toCharArray())
}

fun Bundle.getDoubleList(key: String) = getDoubleArray(key).toList()

fun Bundle.putDoubleList(key: String, value: List<Double>?) {
    if (value == null) return
    putDoubleArray(key, value.toDoubleArray())
}

fun Bundle.getFloatList(key: String) = getFloatArray(key).toList()

fun Bundle.putFloatList(key: String, value: List<Float>?) {
    if (value == null) return
    putFloatArray(key, value.toFloatArray())
}

fun Bundle.getIntList(key: String) = getIntegerArrayList(key).toList()

fun Bundle.putIntList(key: String, value: List<Int>?) {
    if (value == null) return
    putIntegerArrayList(key, value.toArrayList())
}

fun Bundle.getLongList(key: String) = getLongArray(key).toList()

fun Bundle.putLongList(key: String, value: List<Long>?) {
    if (value == null) return
    putLongArray(key, value.toLongArray())
}

fun Bundle.getShortList(key: String) = getShortArray(key).toList()

fun Bundle.putShortList(key: String, value: List<Short>?) {
    if (value == null) return
    putShortArray(key, value.toShortArray())
}

fun Bundle.getStringList(key: String) = getStringArrayList(key).toList()

fun Bundle.putStringList(key: String, value: List<String>?) {
    if (value == null) return
    putStringArrayList(key, value.toArrayList())
}

fun Bundle.getCharSequenceList(key: String) = getCharSequenceArrayList(key).toList()

fun Bundle.putCharSequenceList(key: String, value: List<CharSequence>?) {
    if (value == null) return
    putCharSequenceArrayList(key, value.toArrayList())
}

fun <T : Parcelable> Bundle.getParcelableList(key: String) = getParcelableArrayList<T>(key).toList()

fun <T : Parcelable> Bundle.putParcelableList(key: String, value: List<T>?) {
    if (value == null) return
    putParcelableArrayList(key, value.toArrayList())
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayTyped(key: String): Array<T> =
    getParcelableArray(key) as Array<T>

inline fun <reified T : Parcelable> Bundle.putParcelableArrayTyped(key: String, value: Array<T>) {
    putParcelableArray(key, value)
}

inline fun <reified T : Serializable> Bundle.getSerializableTyped(key: String) =
    getSerializable(key) as T

inline fun <reified T : Serializable> Bundle.putSerializableTyped(key: String, value: T) {
    putSerializable(key, value)
}

private fun <T> List<T>.toArrayList(): ArrayList<T> {
    val arrayList = ArrayList<T>(size)
    arrayList.addAll(this)
    return arrayList
}

@PublishedApi
internal fun missingKeyException(key: String) =
    IllegalStateException("missing value for $key")