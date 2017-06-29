package com.beyondeye.kmongodsl.internal

import com.beyondeye.kmongodsl.expression.MongoDSLExpression
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionConst
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionSourceField
import com.beyondeye.kmongodsl.expression.MongoDSLExpressionVariable
import org.bson.*

/**
 * Created by daely on 6/27/2017.
 */

enum class MongoVarType {
    SourceField,
    Variable
}
object MConstant

object Margument

fun fieldnameToMExpr(fieldName:String)= MongoDSLExpressionSourceField(fieldName)
fun varnameToMExpr(varName:String)= MongoDSLExpressionVariable(varName)
fun labelToMExpr(varType: MongoVarType, varName: String): MongoDSLExpression = when(varType) {
    MongoVarType.SourceField -> MongoDSLExpressionSourceField(varName)
    MongoVarType.Variable -> MongoDSLExpressionVariable(varName)
}

//    fun Int.toBson() =MongoDSLExpressionConst(BsonInt32(this))
//    fun Long.toBson() =MongoDSLExpressionConst(BsonInt64(this))
//    fun String.toBson() =MongoDSLExpressionConst(BsonString(this))
//    fun Double.toBson() =MongoDSLExpressionConst(BsonDouble(this))

fun valueToMExpr(value: Any): MongoDSLExpressionConst = when(value) {
    is Int -> MongoDSLExpressionConst(BsonInt32(value))
    is Long -> MongoDSLExpressionConst(BsonInt64(value))
    is String -> MongoDSLExpressionConst(BsonString(value))
    is Double -> MongoDSLExpressionConst(BsonDouble(value))
    else -> TODO()
}



