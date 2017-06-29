package com.beyondeye.kmongodsl.elements

import org.bson.BsonBoolean
import org.bson.BsonElement

class MongoDSLElementBoolean(name:String, val value: Boolean) : MongoDSLElementBase(name) {
    override fun render(): BsonElement = BsonElement(name, BsonBoolean(value))
}

