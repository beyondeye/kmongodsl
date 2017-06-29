package com.beyondeye.kmongodsl.expression

import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

/**
 * reference to a field in collection being aggregated
 */
class MongoDSLExpressionSourceField(val fieldName: String): MongoDSLExpression {
    override fun renderValue(): BsonValue {
        return BsonString("\$$fieldName")
    }
    override val computedType: BsonType?=null
}