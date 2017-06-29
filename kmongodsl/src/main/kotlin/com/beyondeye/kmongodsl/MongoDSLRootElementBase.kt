package com.beyondeye.kmongodsl

import com.beyondeye.kmongodsl.elements.MongoDSLElementList
import org.bson.BsonDocument

/**
 * base class for all MongoDslRootElement's, with common code and dsl marker
 */
@MongoDSLTagMarker
abstract class MongoDSLRootElementBase : MongoDSLRootElement {
    protected val children = MongoDSLElementList()

    override fun render(): BsonDocument {
        val renderedChildren = children.list.map { it.render() }
        return BsonDocument(renderedChildren)
    }

    override fun toString(): String {
        return render().toString()
    }
}