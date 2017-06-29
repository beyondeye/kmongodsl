package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.elements.MongoDSLElementBase
import com.beyondeye.kmongodsl.elements.MongoDSLElementExpr
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionConst
import com.beyondeye.kmongodsl.internal.*
import com.beyondeye.kmongodsl.project.MongoProjectComputedExpression
import com.beyondeye.kmongodsl.project.MongoProjectComputedField

/**
 * Created by daely on 6/27/2017.
 */
/**
 * https://docs.mongodb.com/manual/reference/operator/aggregation/project/
 */
//TODO refactor common code and unify syntax between MongoDslOperatorComplexArg and AggregateStageProject and MongoDslComplexOperator
class MongoDslOperatorComplexArg(name:String) : MongoDSLElementBase(name) {
    val mfld= MongoVarType.SourceField
    val mvar= MongoVarType.Variable
    val mval = MConstant
    operator fun MongoVarType.rangeTo(varName: String): MongoDSLExpression {
        return labelToMExpr(this, varName)
    }
    operator fun MConstant.rangeTo(value: Any): MongoDSLExpressionConst {
        return valueToMExpr(value)
    }
    operator fun String.rangeTo(arg: MongoDSLExpression) {
        children.addLeafChild(MongoDSLElementExpr(this, arg))

    }
    operator fun String.rangeTo(argfn: MongoProjectComputedExpression.()->Unit){
        val f= MongoProjectComputedField(this)
        children.addChild(f,argfn)
    }


}


