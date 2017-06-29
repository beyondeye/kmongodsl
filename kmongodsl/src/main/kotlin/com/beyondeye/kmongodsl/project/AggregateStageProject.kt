package com.beyondeye.kmongodsl.project

import com.beyondeye.kmongodsl.elements.MongoDSLElementBase
import com.beyondeye.kmongodsl.elements.MongoDSLElementInt
import com.beyondeye.kmongodsl.elements.MongoDSLElementString

/**
 * Created by daely on 6/27/2017.
 */
/**
 * https://docs.mongodb.com/manual/reference/operator/aggregation/project/
 */
class AggregateStageProject : MongoDSLElementBase("\$project") {
    /**
     * note that the scope of this extension function is are the class member functions!, not outside!
     */
    operator fun String.unaryPlus() {
        children.addLeafChild(MongoDSLElementInt(this, 1))
    }
    /**
     * note that the scope of this extension function is are the class member functions!, not outside!
     */
    operator fun String.unaryMinus() {
        children.addLeafChild(MongoDSLElementInt(this, 0))
    }
    /**
     * note that the scope of this extension function is are the class member functions!, not outside!
     */
    operator fun String.rangeTo(srcFieldName:String) {
        children.addLeafChild(MongoDSLElementString(this, srcFieldName))
    }
    /**
     * note that the scope of this extension function is are the class member functions!, not outside!
     */
    operator fun String.rangeTo(initFn: MongoProjectComputedField.()->Unit) {
        val f= MongoProjectComputedField(this)
        children.addChild(f,initFn)
    }
}


