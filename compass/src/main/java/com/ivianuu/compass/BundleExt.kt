package com.ivianuu.compass

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.lang.IllegalStateException

fun Bundle.getBooleanOrNull(key: String): Boolean? {
    if (!containsKey(key)) return null
    return getBoolean(key)
}

fun Bundle.getBooleanOrThrow(key: String) =
    getBooleanOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getByteOrNull(key: String): Byte? {
    if (!containsKey(key)) return null
    return getByte(key)
}

fun Bundle.getByteOrThrow(key: String) =
    getByteOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getCharOrNull(key: String): Char? {
    if (!containsKey(key)) return null
    return getChar(key)
}

fun Bundle.getCharOrThrow(key: String) =
    getCharOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getDoubleOrNull(key: String): Double? {
    if (!containsKey(key)) return null
    return getDouble(key)
}

fun Bundle.getDoubleOrThrow(key: String) =
    getDoubleOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getFloatOrNull(key: String): Float? {
    if (!containsKey(key)) return null
    return getFloat(key)
}

fun Bundle.getFloatOrThrow(key: String) =
    getFloatOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getIntOrNull(key: String): Int? {
    if (!containsKey(key)) return null
    return getInt(key)
}

fun Bundle.getIntOrThrow(key: String) =
    getIntOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getLongOrNull(key: String): Long? {
    if (!containsKey(key)) return null
    return getLong(key)
}

fun Bundle.getLongOrThrow(key: String) =
    getLongOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getShortOrNull(key: String): Short? {
    if (!containsKey(key)) return null
    return getShort(key)
}

fun Bundle.getShortOrThrow(key: String) =
    getShortOrNull(key) ?: throw missingKeyException(key)

fun Bundle.getBooleanArrayOrThrow(key: String) =
    getBooleanArray(key) ?: throw missingKeyException(key)

fun Bundle.getByteArrayOrThrow(key: String) =
    getByteArray(key) ?: throw missingKeyException(key)

fun Bundle.getCharArrayOrThrow(key: String) =
    getCharArray(key) ?: throw missingKeyException(key)

fun Bundle.getDoubleArrayOrThrow(key: String) =
    getDoubleArray(key) ?: throw missingKeyException(key)

fun Bundle.getFloatArrayOrThrow(key: String) =
    getFloatArray(key) ?: throw missingKeyException(key)

fun Bundle.getIntArrayOrThrow(key: String) =
    getIntArray(key) ?: throw missingKeyException(key)

fun Bundle.getLongArrayOrThrow(key: String) =
    getLongArray(key) ?: throw missingKeyException(key)

fun Bundle.getShortArrayOrThrow(key: String) =
    getShortArray(key) ?: throw missingKeyException(key)

fun Bundle.getCharSequenceArrayOrThrow(key: String) =
    getCharSequenceArray(key) ?: throw missingKeyException(key)

fun Bundle.getStringArrayOrThrow(key: String) =
    getStringArray(key) ?: throw missingKeyException(key)

fun Bundle.getParcelableArrayOrThrow(key: String) =
    getParcelableArray(key) ?: throw missingKeyException(key)

fun <T : Parcelable> Bundle.getParcelableArrayTyped(key: String) =
    getParcelableArray(key) as? Array<T>

fun <T : Parcelable> Bundle.getParcelableArrayTypedOrThrow(key: String) =
    getParcelableArrayTyped<T>(key) ?: throw missingKeyException(key)

fun <T : Parcelable> Bundle.putParcelableArrayTyped(key: String, value: Array<T>) {
    putParcelableArray(key, value)
}

fun <T : Parcelable> Bundle.getSparseParcelableArrayOrThrow(key: String) =
    getSparseParcelableArray<T>(key) ?: throw missingKeyException(key)

fun Bundle.getBooleanList(key: String) = getBooleanArray(key)?.toList()

fun Bundle.getBooleanListOrThrow(key: String) =
    getBooleanList(key) ?: throw missingKeyException(key)

fun Bundle.putBooleanList(key: String, value: List<Boolean>?) {
    if (value == null) return
    putBooleanArray(key, value.toBooleanArray())
}

fun Bundle.getByteList(key: String) = getByteArray(key)?.toList()

fun Bundle.getByteListOrThrow(key: String) =
    getByteList(key) ?: throw missingKeyException(key)

fun Bundle.putByteList(key: String, value: List<Byte>?) {
    if (value == null) return
    putByteArray(key, value.toByteArray())
}

fun Bundle.getCharList(key: String) = getCharArray(key)?.toList()

fun Bundle.getCharListOrThrow(key: String) =
    getCharList(key) ?: throw missingKeyException(key)

fun Bundle.putCharList(key: String, value: List<Char>?) {
    if (value == null) return
    putCharArray(key, value.toCharArray())
}

fun Bundle.getDoubleList(key: String) = getDoubleArray(key)?.toList()

fun Bundle.getDoubleListOrThrow(key: String) =
    getDoubleList(key) ?: throw missingKeyException(key)

fun Bundle.putDoubleList(key: String, value: List<Double>?) {
    if (value == null) return
    putDoubleArray(key, value.toDoubleArray())
}

fun Bundle.getFloatList(key: String) = getFloatArray(key)?.toList()

fun Bundle.getFloatListOrThrow(key: String) =
    getFloatList(key) ?: throw missingKeyException(key)

fun Bundle.putFloatList(key: String, value: List<Float>?) {
    if (value == null) return
    putFloatArray(key, value.toFloatArray())
}

fun Bundle.getIntList(key: String) = getIntegerArrayList(key)?.toList()

fun Bundle.getIntListOrThrow(key: String) =
    getIntList(key) ?: throw missingKeyException(key)

fun Bundle.putIntList(key: String, value: List<Int>?) {
    if (value == null) return
    putIntegerArrayList(key, ArrayList(value))
}

fun Bundle.getLongList(key: String) = getLongArray(key)?.toList()

fun Bundle.getLongListOrThrow(key: String) =
    getLongList(key) ?: throw missingKeyException(key)

fun Bundle.putLongList(key: String, value: List<Long>?) {
    if (value == null) return
    putLongArray(key, value.toLongArray())
}

fun Bundle.getShortList(key: String) = getShortArray(key)?.toList()

fun Bundle.getShortListOrThrow(key: String) =
    getShortList(key) ?: throw missingKeyException(key)

fun Bundle.putShortList(key: String, value: List<Short>?) {
    if (value == null) return
    putShortArray(key, value.toShortArray())
}

fun Bundle.getStringList(key: String) = getStringArrayList(key)?.toList()

fun Bundle.getStringListOrThrow(key: String) =
    getStringList(key) ?: throw missingKeyException(key)

fun Bundle.putStringList(key: String, value: List<String>?) {
    if (value == null) return
    putStringArrayList(key, ArrayList(value))
}

fun Bundle.getCharSequenceList(key: String) = getCharSequenceArrayList(key)?.toList()

fun Bundle.getCharSequenceListOrThrow(key: String) =
    getCharSequenceList(key) ?: throw missingKeyException(key)

fun Bundle.putCharSequenceList(key: String, value: List<CharSequence>?) {
    if (value == null) return
    putCharSequenceArrayList(key, ArrayList(value))
}

fun <T : Parcelable> Bundle.getParcelableList(key: String) =
    getParcelableArrayList<T>(key)?.toList()

fun <T : Parcelable> Bundle.getParcelableListOrThrow(key: String) =
    getParcelableList<T>(key) ?: throw missingKeyException(key)

fun <T : Parcelable> Bundle.putParcelableList(key: String, value: List<T>?) {
    if (value == null) return
    putParcelableArrayList(key, ArrayList(value))
}

fun <T : Parcelable> Bundle.getParcelableArrayListOrThrow(key: String) =
    getParcelableArrayList<T>(key) ?: throw missingKeyException(key)

fun Bundle.getBundleOrThrow(key: String) =
    getBundle(key) ?: throw missingKeyException(key)

fun Bundle.getCharSequenceOrThrow(key: String) =
    getCharSequence(key) ?: throw missingKeyException(key)

fun Bundle.getStringOrThrow(key: String) =
    getString(key) ?: throw missingKeyException(key)

fun <T : Parcelable> Bundle.getParcelableOrThrow(key: String) =
    getParcelable<T>(key) ?: throw missingKeyException(key)

fun <T : Serializable> Bundle.getSerializableTyped(key: String) =
    getSerializable(key) as? T

fun <T : Serializable> Bundle.getSerializableTypedOrThrow(key: String) =
    getSerializableTyped<T>(key) ?: throw missingKeyException(key)

fun <T : Serializable> Bundle.putSerializableTyped(key: String, value: T?) {
    putSerializable(key, value)
}

private fun missingKeyException(key: String) =
    IllegalStateException("missing value for $key")