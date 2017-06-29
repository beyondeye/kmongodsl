package com.beyondeye.kmongodsl.elements

import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import org.bson.BsonDocument

/**
 * Created by daely on 6/27/2017.
 */
class MongoDSLElementList {
    val list = arrayListOf<MongoDSLElement>()

    /**
     * helper function used to implement all DSL tags that require a subscope: it simply handle the subscope (i.e. the lambda
     * passed as argument is called) and also add the tag to the list of children
     */
    fun <T : MongoDSLElement> addChild(child: T, childSubTreeInitFn: T.() -> Unit): T {
        child.childSubTreeInitFn()
        list.add(child)
        return child
    }
    fun <T : MongoDSLExpression> addExprChild(childElementName:String, childExpr: T, childSubTreeInitFn: T.() -> Unit): T {
        childExpr.childSubTreeInitFn()
        list.add(MongoDSLElementExpr(childElementName, childExpr))
        return childExpr
    }
    fun <T : MongoDSLElement> addLeafChild(childLeaf: T): T {
        list.add(childLeaf)
        return childLeaf
    }

    fun  isEmpty(): Boolean=list.isEmpty()

    override fun toString(): String {
        return renderValue().toJson()
    }
    //note that MongoDSLElementList is not technically a MongoDSLExpression, so although the renderValue is defined,
    //this class does not inherit from MongoDSLExpression
    fun renderValue(): BsonDocument {
        return BsonDocument(list.map { it.render() })
    }

}