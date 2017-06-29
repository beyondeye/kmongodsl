package com.beyondeye.kmongodsl.expression

class MongoDSLExpressionList{
    val list = arrayListOf<MongoDSLExpression>()

    /**
     * helper function used to implement all DSL tags that require a subscope: it simply handle the subscope (i.e. the lambda
     * passed as argument is called) and also add the tag to the list of children
     */
    fun <T : MongoDSLExpression> addComputedArgument(arg: T, argSubTreeInitFn: T.() -> Unit): T {
        arg.argSubTreeInitFn()
        list.add(arg)
        return arg
    }
    fun <T : MongoDSLExpression> addArgument(arg: T): T {
        list.add(arg)
        return arg
    }
    fun <T : MongoDSLExpression> addArguments(vararg args: T): Array<out T> {
        list.addAll(args)
        return args
    }
    override fun toString(): String {
        return list.map { it.renderValue().toString() }.reduce { acc, s -> acc + "$s, " }
    }
}
