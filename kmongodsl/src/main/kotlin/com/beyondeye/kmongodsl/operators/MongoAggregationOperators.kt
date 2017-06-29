package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.elements.*
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.project.MongoProjectComputedExpression
import org.bson.*

/**
 * Created by daely on 6/27/2017.
 */



/*
  see https://docs.mongodb.com/manual/reference/operator/aggregation/multiply/
 */
class MultiplyOperatorDSLElement() : MongoDSLOperatorBase("\$multiply") {
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null
}

/**
 * see https://docs.mongodb.com/manual/reference/operator/aggregation/slice
 */
class SliceOperatorDSLElement(): MongoDSLOperatorBase("\$slice"){
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null

    constructor(fieldName: String, firstIndex: Int, nIndexes: Int):this() {
        mfld..fieldName
        mval..firstIndex
        mval..nIndexes
    }
}


/**
 * see https://docs.mongodb.com/manual/reference/operator/aggregation/arrayElemAt/
 */
class ArrayElementAtOperatorDSLElement(): MongoDSLOperatorBase("\$arrayElementAt"){
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null
    constructor(arrayExpr: MongoDSLExpression, idx: Int):this() {
        marg..arrayExpr
        mval..idx
    }
}

/*
  see https://docs.mongodb.com/manual/reference/operator/aggregation/sum
 */
class SumOperatorDSLElement() : MongoDSLOperatorBase("\$sum") {
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null
}
/*
  see https://docs.mongodb.com/manual/reference/operator/aggregation/subtract
 */
class SubtractOperatorDSLElement() : MongoDSLOperatorBase("\$subtract") {
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null
}

/*
  see https://docs.mongodb.com/manual/reference/operator/aggregation/concatArrays/
 */
class ConcatArraysOperatorDSLElement() : MongoDSLOperatorBase("\$concatArrays") {
    override val computedType: BsonType?=null
    override val argSpecs: MongoAggregationOperatorArgSpecs?=null
}


/*
  https://docs.mongodb.com/manual/reference/operator/aggregation/let/
 */
class LetOperatorDSLElement() : MongoDSLComplexOperatorBase("\$let") {
    override val computedType: BsonType?=null
    //TODO define argspecs for complex operator
    //override val argSpecs: MongoAggregationOperatorArgSpecs?=null
    //TODO write some helper function to reduce boilerplate when defining complex operators
    fun vars(varsInitFun: MongoDslOperatorComplexArg.()->Unit)  {
        val vars= MongoDslOperatorComplexArg("vars")
        children.addChild(vars, varsInitFun)
    }
    fun in_(argfn: MongoProjectComputedExpression.()->Unit) {
        val o= MongoProjectComputedExpression()
        children.addExprChild("in",o,argfn)
    }
}


/*
  https://docs.mongodb.com/manual/reference/operator/aggregation/zip
 */
class ZipOperatorDSLElement() : MongoDSLComplexOperatorBase("\$zip") {
    override val computedType: BsonType?=null
    //override val argSpecs: MongoAggregationOperatorArgSpecs?=null
    //TODO write some helper function to reduce boilerplate when defining complex operators
    fun inputs(argsInitFun: MongoDSLOperatorArrayArg.()->Unit)  {
        val args= MongoDSLOperatorArrayArg()
        children.addExprChild("inputs",args,argsInitFun)
    }
    fun useLongestLength(value:Boolean) {
        children.addLeafChild(MongoDSLElementBoolean("useLongestLength", value))
    }
    fun defaults(arrayExpr:MongoDSLExpression) {
        children.addLeafChild(MongoDSLElementExpr("defaults", arrayExpr))
    }
}


/*
  https://docs.mongodb.com/manual/reference/operator/aggregation/map/
 */
class MapOperatorDSLElement() : MongoDSLComplexOperatorBase("\$map") {
    override val computedType: BsonType?=null
    //TODO define argspecs for complex operator
    //override val argSpecs: MongoAggregationOperatorArgSpecs?=null
    //TODO write some helper function to reduce boilerplate when defining complex operators
    fun input( expr:MongoDSLExpression) {
        children.addLeafChild(MongoDSLElementExpr("input",expr))
    }
    fun input(argfn: MongoProjectComputedExpression.()->Unit) {
        val o= MongoProjectComputedExpression()
        children.addExprChild("input",o,argfn)
    }
    fun as_(varName:String)  {
        children.addLeafChild(MongoDSLElementString("as",varName))
    }
    fun in_(argfn: MongoProjectComputedExpression.()->Unit) {
        val o= MongoProjectComputedExpression()
        children.addExprChild("in",o,argfn)
    }
}


/*
  https://docs.mongodb.com/manual/reference/operator/aggregation/reduce
 */
class ReduceOperatorDSLElement() : MongoDSLComplexOperatorBase("\$reduce") {
    override val computedType: BsonType?=null
    //override val argSpecs: MongoAggregationOperatorArgSpecs?=null
    //TODO write some helper function to reduce boilerplate when defining complex operators
    fun input( expr:MongoDSLExpression) {
        children.addLeafChild(MongoDSLElementExpr("input",expr))
    }
    //TODO add more initialValue methods for different types
    fun initialValue(value:Double) {
        children.addLeafChild(MongoDSLElementDouble("initialValue", value))
    }
    fun initialValue(expr:MongoDSLExpression) {
        children.addLeafChild(MongoDSLElementExpr("initialValue", expr))
    }

    /**
     * for the case where the reduce operation works on a single expression accumulator
     */
    fun in_expr(argfn: MongoProjectComputedExpression.()->Unit) {
        val o= MongoProjectComputedExpression()
        children.addExprChild("in",o,argfn)
    }
    /**
     * for the case where the reduce operation works on a object accumulator
     */
    fun in_obj(argfn: MongoDslOperatorComplexArg.()->Unit) {
        val vars= MongoDslOperatorComplexArg("in")
        children.addChild(vars, argfn)
    }
}