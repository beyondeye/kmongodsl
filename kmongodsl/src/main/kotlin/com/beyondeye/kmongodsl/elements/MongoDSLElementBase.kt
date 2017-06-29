package com.beyondeye.kmongodsl.elements

import com.beyondeye.kmongodsl.MongoDSLTagMarker
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonValue

/**
 * Created by daely on 6/27/2017.
 */


/**
 * base class for all MongoDslElement's, with common code and dsl marker
 */
@MongoDSLTagMarker
abstract class MongoDSLElementBase(val name: String) : MongoDSLElement {
    protected val children = MongoDSLElementList()

    /**
     * in case that the Dslelement it also derive from MongoDSLExpression, here is the
     * predefined renderValue()
     */
    open fun renderValue(): BsonValue = BsonDocument(listOf(render()))

    override fun render(): BsonElement {
        val renderedChildren = children.list.map { it.render() }
        return BsonElement(name, BsonDocument(renderedChildren))
    }

    override fun toString(): String {
        val be = render()
        return BsonDocument(be.name, be.value).toString()
    }
}


