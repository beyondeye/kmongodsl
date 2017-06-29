package com.beyondeye.kmongodsl.expression

import org.bson.BsonType
import org.bson.BsonValue

/**
 * reference to a field in collection being aggregated
 */
class MongoDSLExpressionConst(val value: BsonValue): MongoDSLExpression {
    override fun renderValue(): BsonValue {
        return value
    }
    override val computedType: BsonType?=value.bsonType
}