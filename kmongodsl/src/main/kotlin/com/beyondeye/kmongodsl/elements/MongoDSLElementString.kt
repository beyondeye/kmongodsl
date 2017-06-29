package com.beyondeye.kmongodsl.elements

import org.bson.BsonElement
import org.bson.BsonString

/**
 * Created by daely on 6/27/2017.
 */



class MongoDSLElementString(name:String, val text: String) : MongoDSLElementBase(name) {
    override fun render(): BsonElement = BsonElement(name, BsonString(text))
}