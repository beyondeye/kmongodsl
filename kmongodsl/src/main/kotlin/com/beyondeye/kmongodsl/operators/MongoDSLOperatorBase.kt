package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.MongoDSLTagMarker
import com.beyondeye.kmongodsl.elements.MongoDSLElement
import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionList
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionConst
import com.beyondeye.kmongodsl.internal.*
import com.beyondeye.kmongodsl.project.MongoProjectComputedExpression
import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonValue

//TODO rename this MongoDSLProjectOperatorBase and make several version for base classes of operators for each aggregate stage
//TODO refactor common code between MongoDSLOperatorArrayArg and MongoDSLOperatorBase

/**
 * base class for operator that as input an array of bson objects
 */
@MongoDSLTagMarker
abstract class MongoDSLOperatorBase(val operatorName: String) : MongoDSLElement, MongoDSLExpression {
    protected val operatorArgs = MongoDSLExpressionList()

    override fun renderValue(): BsonValue = BsonDocument(listOf(render()))
    /**
     * null if undefined
     */
    abstract val argSpecs: MongoAggregationOperatorArgSpecs?

    override fun toString(): String {
        return render().toString()
    }

    override fun render(): BsonElement {
        argSpecs?.let {
            it.checkIfArgListIsValid(operatorArgs)
        }
        val args = BsonArray(operatorArgs.list.map { it.renderValue() })
        return BsonElement(operatorName, args)
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

    /**
     * the name of field in the source document
     * val someField = mfld.."fieldName"
     */
    val marg= Margument
    val mfld= MongoVarType.SourceField
    /**
     * the name of mongo variable
     * val someVar = mvar.."variableName"
    // https://docs.mongodb.com/manual/reference/operator/aggregation/let/
    // https://docs.mongodb.com/manual/reference/aggregation-variables/
     */
    val mvar= MongoVarType.Variable

    /**
     * a constant of some elemtary bson type
     * val someConst=mval..1
     */
    val mval = MConstant


    /**
     * generating functions for MongoDSLExpressionSourceField and MongoDSLExpressionVariable
    val someField = mfld.."fieldName"
    val someVar = mvar.."variableName"
    val someConst=mval..1
     */


//    fun arg(arg: List<Any>) {
//        val arg_ = MongoDSLExpressionConst(arg.toBsonArray())
//        operatorArgs.addArgument(arg_)
//    }
}