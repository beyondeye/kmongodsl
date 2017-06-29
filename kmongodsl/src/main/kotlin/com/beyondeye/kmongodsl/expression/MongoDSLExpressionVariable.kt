package com.beyondeye.kmongodsl.expression

import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

/**
 * Created by daely on 6/27/2017.
 */

/**
 * see https://docs.mongodb.com/manual/reference/operator/aggregation/let/
 * and https://docs.mongodb.com/manual/reference/aggregation-variables/
 */
class MongoDSLExpressionVariable(val variableName: String): MongoDSLExpression {
    override fun renderValue(): BsonValue {
        return BsonString("\$\$$variableName")
    }
    override val computedType: BsonType?=null
}