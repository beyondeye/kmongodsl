package com.beyondeye.kmongodsl.elements

import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import org.bson.BsonElement

/**
 * Created by daely on 6/27/2017.
 */



class MongoDSLElementExpr(val name:String, val expr: MongoDSLExpression) : MongoDSLElement {
    override fun render(): BsonElement = BsonElement(name, expr.renderValue())
}