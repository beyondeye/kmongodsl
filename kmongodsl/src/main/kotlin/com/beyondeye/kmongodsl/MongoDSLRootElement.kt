package com.beyondeye.kmongodsl

import org.bson.BsonDocument

/**
 * Created by daely on 6/27/2017.
 */
interface MongoDSLRootElement {
    fun render(): BsonDocument
}