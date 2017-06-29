package com.beyondeye.kmongodsl.project

import com.beyondeye.kmongodsl.elements.MongoDSLElement
import org.bson.BsonDocument
import org.bson.BsonElement

class MongoProjectComputedField(val fieldName:String): MongoDSLElement, MongoProjectComputedExpression() {

    //METHOD FROM MongoDSLElementBase
    override fun render(): BsonElement {
        val name=fieldName
        val renderedChildren = children.list.map { it.render() }
        return BsonElement(name, BsonDocument(renderedChildren))
    }

    //METHOD FROM MongoDSLElementBase
    override fun toString(): String {
        val be = render()
        return BsonDocument(be.name, be.value).toString()
    }

    //the following definition is redundant, because with have "from" operator in [AggregateStageProject]
    //     fun srcField(srcFieldName:String) :String = "\$$srcFieldName"

    /**
     * when defining computed field that is an object
     */
    fun subfield(subfieldName: String, initFn: MongoProjectComputedField.() -> Unit) =
        children.addChild(MongoProjectComputedField(subfieldName),initFn)
}