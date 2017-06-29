package com.beyondeye.kmongodsl

/**
 * see http://kotlinlang.org/docs/reference/type-safe-builders.html#scope-control-dslmarker-since-11
 * After we've added this annotation to the superclass of tags, the Kotlin compiler knows which implicit receivers are part of the same DSL and allows to call members of the nearest receivers only:
 */
@DslMarker
annotation class MongoDSLTagMarker