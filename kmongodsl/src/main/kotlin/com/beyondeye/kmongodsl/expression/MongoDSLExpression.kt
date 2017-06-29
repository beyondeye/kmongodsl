package com.beyondeye.kmongodsl.expression

import org.bson.BsonType
import org.bson.BsonValue

/**
 * abstraction for all MongoDB expressions see: https://docs.mongodb.com/manual/meta/aggregation-quick-reference/#expressions
 */
interface MongoDSLExpression {
    fun renderValue(): BsonValue
    /**
     * null if unknown
     */
    val  computedType: BsonType?
}