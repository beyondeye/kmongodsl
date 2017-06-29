package com.beyondeye.kmongodsl.elements

import org.bson.BsonElement
import org.bson.BsonValue

/**
 * Created by daely on 6/27/2017.
 */



class MongoDSLElementBson(name:String, val value: BsonValue) : MongoDSLElementBase(name) {
    override fun render(): BsonElement = BsonElement(name, value)
}