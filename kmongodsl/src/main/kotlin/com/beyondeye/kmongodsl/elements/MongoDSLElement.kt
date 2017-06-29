package com.beyondeye.kmongodsl.elements

import org.bson.BsonElement

/**
 * an MongoDSLElement is a pair of (name,value), similar to BsonElement which is its actual representation in Bson
 */
interface MongoDSLElement {
    fun render(): BsonElement
}