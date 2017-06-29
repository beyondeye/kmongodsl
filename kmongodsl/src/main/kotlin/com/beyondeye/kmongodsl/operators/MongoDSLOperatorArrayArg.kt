package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.MongoDSLTagMarker
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionList
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionConst
import com.beyondeye.kmongodsl.internal.*
import com.beyondeye.kmongodsl.project.MongoProjectComputedExpression
import org.bson.*

//TODO rename this MongoDSLProjectOperatorArrayArg  and make several version for base classes of operators for each aggregate stage
//TODO refactor common code between MongoDSLOperatorArrayArg and MongoDSLOperatorBase
/**
 * base class for operator that as input an array of bson objects
 */
@MongoDSLTagMarker
class MongoDSLOperatorArrayArg(val argSpecs:MongoAggregationOperatorArgSpecs?=null) :  MongoDSLExpression {
    protected val operatorArgs = MongoDSLExpressionList()

    override val computedType: BsonType? get() = BsonType.ARRAY

    override fun renderValue(): BsonValue {
        argSpecs?.let {
            it.checkIfArgListIsValid(operatorArgs)
        }
        val args = BsonArray(operatorArgs.list.map { it.renderValue() })
        return args

    }

    override fun toString(): String {
        return renderValue().toString()
    }

    operator fun MongoVarType.rangeTo(varName: String): MongoDSLExpression {
        val res= labelToMExpr(this, varName)
        operatorArgs.addArgument(res)
        return res
    }
    operator fun MConstant.rangeTo(value: Any): MongoDSLExpressionConst {
        val res= valueToMExpr(value)
        operatorArgs.addArgument(res)
        return res
    }
    operator fun Margument.rangeTo(arg: MongoDSLExpression): MongoDSLExpression {
        operatorArgs.addArgument(arg)
        return arg
    }
    operator fun Margument.rangeTo(argfn: MongoProjectComputedExpression.()->Unit): MongoDSLExpression {
        val o= MongoProjectComputedExpression()
        operatorArgs.addComputedArgument<MongoProjectComputedExpression>(o,argfn)
        return o
    }
    val marg= Margument
    val mfld= MongoVarType.SourceField
    val mvar= MongoVarType.Variable
    val mval = MConstant


}