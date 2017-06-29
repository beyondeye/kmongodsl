package com.beyondeye.kmongodsl.internal

import org.bson.BsonArray

/**
 * Created by daely on 6/27/2017.
 */

fun  List<Any>.toBsonArray(): BsonArray {
    if(this.isEmpty()) return BsonArray()
    val e0=this.get(0)
    return when(e0) {
        is Int -> BsonArray(this.map { org.bson.BsonInt32(it as Int) })
        is Long -> BsonArray(this.map { org.bson.BsonInt64(it as Long) })
        is Double -> BsonArray(this.map { org.bson.BsonDouble(it as Double) })
        is String -> BsonArray(this.map { org.bson.BsonString(it as String) })
        else -> throw  NotImplementedError()
    }
}
