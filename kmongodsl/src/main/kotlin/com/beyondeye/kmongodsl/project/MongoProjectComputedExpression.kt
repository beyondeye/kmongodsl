package com.beyondeye.kmongodsl.project

import com.beyondeye.kmongodsl.elements.MongoDSLElement
import com.beyondeye.kmongodsl.elements.MongoDSLElementList
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.internal.fieldnameToMExpr
import com.beyondeye.kmongodsl.operators.*
import org.bson.BsonType
import org.bson.BsonValue

// see https://docs.mongodb.com/manual/reference/operator/aggregation/project/#include-computed-fields
// that define the type of operators that are supported and the type of operators that are supported inside
open class MongoProjectComputedExpression: MongoDSLExpression {
    protected val children = MongoDSLElementList()
    protected fun <T: MongoDSLElement> addChild(child: T, childSubTreeInitFn: T.() -> Unit):T {
        return children.addChild(child,childSubTreeInitFn)
    }
    protected fun <T: MongoDSLElement> addLeafChild(child: T): T {
        return children.addLeafChild(child)
    }
    protected fun checkNoChildren() {
        if (!children.isEmpty()) throw IllegalArgumentException("trying to redefine value already defined as ${renderValue()}: if you want multiple field, use subfield instead! ")
    }
    //TODO move to base class
    protected fun <T : MongoDSLElement> addSingleChild(child: T, childSubTreeInitFn: T.() -> Unit): T {
        checkNoChildren()
        if(child is MongoDSLExpression) {
            computedType_=child.computedType
        }
        return addChild(child,childSubTreeInitFn)
    }
    protected fun <T : MongoDSLElement> addSingleLeafChild(child: T): T {
        checkNoChildren()
        return addLeafChild(child)
    }
    protected var computedType_: BsonType?=null
    override val computedType: BsonType? get() =computedType_
    override fun renderValue(): BsonValue = children.renderValue()


    //----------------------------------------------------------------------------------------
    //Project stage allowed operators
    //TODO define some inline functions to reduce boilerplate for adding here operators
    //----------------------------------------------------------------------------------------
    fun slice(argsInitFun: SliceOperatorDSLElement.()->Unit) =
            addSingleChild(SliceOperatorDSLElement(), argsInitFun)
    fun slice(srcfieldName:String, firstIndex:Int, nIndexes:Int) =
            addSingleLeafChild(SliceOperatorDSLElement(srcfieldName, firstIndex, nIndexes))
    //--------
    fun sum(argsInitFun: SumOperatorDSLElement.()->Unit) =
            addSingleChild(SumOperatorDSLElement(), argsInitFun)
    //--------
    fun subtract(argsInitFun: SubtractOperatorDSLElement.()->Unit) =
            addSingleChild(SubtractOperatorDSLElement(), argsInitFun)
    //--------
    fun multiply(argsInitFun: MultiplyOperatorDSLElement.()->Unit) =
            addSingleChild(MultiplyOperatorDSLElement(), argsInitFun)
    //--------
    fun concatArrays(argsInitFun: ConcatArraysOperatorDSLElement.()->Unit) =
            addSingleChild(ConcatArraysOperatorDSLElement(), argsInitFun)
    //-----------------------------------------------------------------------------
    fun arrayElementAt(argsInitFun: ArrayElementAtOperatorDSLElement.()->Unit) =
            addSingleChild(ArrayElementAtOperatorDSLElement(), argsInitFun)
    fun arrayElementAt(srcfieldName:String, index:Int) =
            addSingleLeafChild(ArrayElementAtOperatorDSLElement(fieldnameToMExpr(srcfieldName), index))
    //-----------------------------------------------------------------------------
    fun let(argsInitFun: LetOperatorDSLElement.()->Unit) =
            addSingleChild(LetOperatorDSLElement(), argsInitFun)
    //-----------------------------------------------------------------------------
    fun zip(argsInitFun: ZipOperatorDSLElement.()->Unit) =
            addSingleChild(ZipOperatorDSLElement(), argsInitFun)
    //-----------------------------------------------------------------------------
    fun map(argsInitFun: MapOperatorDSLElement.()->Unit) =
            addSingleChild(MapOperatorDSLElement(), argsInitFun)
    //-----------------------------------------------------------------------------
    fun reduce(argsInitFun: ReduceOperatorDSLElement.()->Unit) =
            addSingleChild(ReduceOperatorDSLElement(), argsInitFun)
}