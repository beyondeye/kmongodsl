package com.beyondeye.kmongodsl.elements

import org.bson.BsonElement
import org.bson.BsonInt32

class MongoDSLElementInt(name:String, val num: Int) : MongoDSLElementBase(name) {
    override fun render(): BsonElement = BsonElement(name, BsonInt32(num))
}

