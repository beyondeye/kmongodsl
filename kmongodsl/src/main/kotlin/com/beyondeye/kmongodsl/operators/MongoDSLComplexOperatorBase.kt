package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.elements.MongoDSLElementBase
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionConst
import com.beyondeye.kmongodsl.internal.*

/**
 * Created by daely on 6/27/2017.
 */
/**
 * base class for operators that taks a  bson object as argument,
 */
//TODO refactor common code and unify syntax between MongoDslOperatorComplexArg and AggregateStageProject and MongoDslComplexOperator
abstract class MongoDSLComplexOperatorBase(name:String) : MongoDSLElementBase(name) , MongoDSLExpression{
    val mfld= MongoVarType.SourceField
    val mvar= MongoVarType.Variable
    val mval = MConstant
    /**
     * local (to class scope) operators definitions
     */
    operator fun MongoVarType.rangeTo(varName: String)= labelToMExpr(this, varName)
    operator fun MConstant.rangeTo(value: Any): MongoDSLExpressionConst = valueToMExpr(value)
//    operator fun String.rangeTo(arg: MongoDSLExpression) {
//        children.addLeafChild(MongoDSLElementExpr(this, arg))
//    }
//   operator fun String.rangeTo(argfn: MongoProjectComputedExpression.()->Unit){
//        val f= MongoProjectComputedField(this)
//        children.addChild(f,argfn)
//    }


}


