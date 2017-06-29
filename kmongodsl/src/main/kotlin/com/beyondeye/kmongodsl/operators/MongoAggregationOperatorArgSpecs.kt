package com.beyondeye.kmongodsl.operators

import com.beyondeye.kmongodsl.expression.MongoDSLExpressionList
import org.bson.BsonType

/**
 * specification of type and number of arguments of operators that an array of bson objects as argument
 */
class MongoAggregationOperatorArgSpecs(val opName:String,
                                       /**
                                * //for each number of args, the type for each arg
                                */
                               val argSpecList:Map<Int,List<BsonType>>,
                                       /**
                                * minimum number of arguments
                                */
                               val nMinArgs:Int,
                                       /**
                                * max number of arguments or <0 if variable number of arguments
                                */
                               val nMaxArgs:Int

) {
    fun checkIfArgListIsValid(argList: MongoDSLExpressionList) {
        val nActualArgs=argList.list.size
        if(nActualArgs<nMinArgs)
            throw IllegalArgumentException("not enough arguments for operation $opName: need at least $nMinArgs, while specified arguments are  $nActualArgs:  $argList")
        if(nMaxArgs>=0 && nActualArgs>nMaxArgs)
            throw IllegalArgumentException("too many arguments for operation $opName: need max of $nMaxArgs, while specified arguments are $nActualArgs:    $argList")

        val isVariableArgs=nMaxArgs<0
        var specList:List<BsonType>?=argSpecList[nActualArgs]
        if (specList == null && isVariableArgs) {
            val argspec: BsonType? = argSpecList[-1]?.get(0)
            if (argspec != null) {
                specList = Array<BsonType>(nActualArgs) { argspec }.toList()
            }
        }
        if (specList == null) return //no spec for this number of args:
        argList.list.forEachIndexed{i,arg->
            val expectedType=specList!![i]
            arg.computedType?.let { actualType ->
                if(actualType!=expectedType)
                throw  IllegalArgumentException("mismatched type for argument $i, for op $opName: expected type $expectedType but it is $actualType:   $argList")
            }
        }

    }
}