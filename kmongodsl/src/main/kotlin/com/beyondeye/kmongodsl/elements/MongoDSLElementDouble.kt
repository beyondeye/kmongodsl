package com.beyondeye.kmongodsl.elements

import org.bson.BsonDouble
import org.bson.BsonElement

class MongoDSLElementDouble(name:String, val num: Double) : MongoDSLElementBase(name) {
    override fun render(): BsonElement = BsonElement(name, BsonDouble(num))
}

