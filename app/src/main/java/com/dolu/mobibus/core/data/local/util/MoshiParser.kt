package com.dolu.mobibus.core.data.local.util

import com.squareup.moshi.Moshi
import java.lang.reflect.Type
import javax.inject.Inject

class MoshiParser
@Inject constructor(
    private val moshi: Moshi): JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? {
        return moshi.adapter<T>(type).fromJson(json)
    }

    override fun <T> toJson(obj: T, type: Type): String? {
        return moshi.adapter<T>(type).toJson(obj)
    }
}