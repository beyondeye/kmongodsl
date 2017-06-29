package com.beyondeye.kmongodsl

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by daely on 6/25/2017.
 * see https://docs.mongodb.com/manual/reference/operator/aggregation/project/
 */

class MongoAggregationDSLTests {
    @Test
    fun testProject_UnaryPlusMeansFieldToInclude() {
        val stage= mongoAggregateStage {
            project {
                +"fieldToInclude"
            }
        }

        val gstr=stage.toString()
        //expected: { "$project" : { "fieldToInclude" : 1 } }
        val expected = "{ \"\$project\" : { \"fieldToInclude\" : 1 } }"
        assertEquals(expected,gstr)
    }
    @Test
    fun testProject_UnaryPlusForIncludingTwoFields() {
        val stage= mongoAggregateStage {
            project {
                +"fieldToInclude1"
                +"fieldToInclude2"
            }
        }

        val gstr=stage.toString()
        //expected: { "$project" : { "fieldToInclude1" : 1, "fieldToInclude2" : 1 } }
        val expected = "{ \"\$project\" : { \"fieldToInclude1\" : 1, \"fieldToInclude2\" : 1 } }"
        assertEquals(expected,gstr)
    }
    @Test
    fun testProject_UnaryMinusMeansFieldToExclude() {
        val stage= mongoAggregateStage {
            project {
                -"fieldToExclude"
            }
        }

        val gstr=stage.toString()
        //expected: { "$project" : { "fieldToExclude" : 0 } }
        val expected = "{ \"\$project\" : { \"fieldToExclude\" : 0 } }"
        assertEquals(expected,gstr)
    }

    @Test
    fun testProject_IncludeFieldButRenamed() {
        val stage= mongoAggregateStage {
            project {
                "fieldNewName".."fieldToInclude"
            }
        }

        val gstr=stage.toString()
        //expected: { "$project" : { "fieldNewName" : "fieldToInclude" } }
        val expected = "{ \"\$project\" : { \"fieldNewName\" : \"fieldToInclude\" } }"
        assertEquals(expected,gstr)
    }
    @Test
    fun testProject_computed_field_with_slice() {
        val stage= mongoAggregateStage {
            project {
                "the_sliced_field"..{
                    slice("fieldtoslice", 1, 10)
                }
            }
        }

        val gstr=stage.toString()
        //expected: { "$project" : { "the_sliced_field" : { "$slice" : ["$fieldtoslice", 1, 10] } } }
        val expected = "{ \"\$project\" : { \"the_sliced_field\" : { \"\$slice\" : [\"\$fieldtoslice\", 1, 10] } } }"
        assertEquals(expected,gstr)
    }
    @Test
    fun testProject_computed_field_with_slice_explicit_arg_fields() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_sliced_field"..{
                    slice{  mfld.."fieldtoslice";mval..1;mval..10 }
                }
            }
        }

        val stage4= mongoAggregateStage {
            project {
                "the_sliced_field"..{
                    slice{
                        marg..{
                            sum { mfld.."a";mfld.."b" }
                        }
                        mval..1
                        mval..10
                    }
                }
            }
        }


        //expected: { "$project" : { "the_sliced_field" : { "$slice" : ["$fieldtoslice", 1, 10] } } }
        val expected="{ \"\$project\" : { \"the_sliced_field\" : { \"\$slice\" : [\"\$fieldtoslice\", 1, 10] } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)


        //expected: { "$project" : { "the_sliced_field" : { "$slice" : [{ "$sum" : ["$a", "$b"] }, 1, 10] } } }
        val expected_nested="{ \"\$project\" : { \"the_sliced_field\" : { \"\$slice\" : [{ \"\$sum\" : [\"\$a\", \"\$b\"] }, 1, 10] } } }"
        val gstr4=stage4.toString()
        assertEquals(expected_nested,gstr4)

    }

    @Test
    fun testProject_let_operator() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_computed_field"..{
                    let{
                        vars {
                            "var1"..(mfld.."field1")
                            "var2"..(mfld.."field2")
                        }
                        in_ { multiply { mvar.."var1";mvar.."var2" } }
                    }
                }
            }
        }


        //expected: { "$project" : { "the_computed_field" : { "$let" : { "vars" : { "var1" : "$field1", "var2" : "$field2" }, "in" : { "$multiply" : ["$$var1", "$$var2"] } } } } }
        val expected="{ \"\$project\" : { \"the_computed_field\" : { \"\$let\" : { \"vars\" : { \"var1\" : \"\$field1\", \"var2\" : \"\$field2\" }, \"in\" : { \"\$multiply\" : [\"\$\$var1\", \"\$\$var2\"] } } } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)
    }
    @Test
    fun testProject_map_operator() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_computed_field"..{
                    map{
                        input(mfld.."quizzes") //beware of the pitfall of writing input {mfld.."quizzes"}
                        as_("grade")
                        in_ { sum { mvar.."grade";mval..2 } }
                    }
                }
            }
        }


        //expected: { "$project" : { "the_computed_field" : { "$map" : { "input" : "$quizzes", "as" : "grade", "in" : { "$sum" : ["$$grade", 2] } } } } }
        val expected="{ \"\$project\" : { \"the_computed_field\" : { \"\$map\" : { \"input\" : \"\$quizzes\", \"as\" : \"grade\", \"in\" : { \"\$sum\" : [\"\$\$grade\", 2] } } } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)
    }


    @Test
    fun testProject_let_operator_computed_var_defs() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_computed_field"..{
                    let{
                        vars {
                            "var1"..{
                                sum { mfld.."field1"; mfld.."field2" }
                            }
                            "var2"..(mfld.."field2")
                        }
                        in_ { multiply { mvar.."var1";mvar.."var2" } }
                    }
                }
            }
        }


        //expected: { "$project" : { "the_computed_field" : { "$let" : { "vars" : { "var1" : { "$sum" : ["$field1", "$field2"] }, "var2" : "$field2" }, "in" : { "$multiply" : ["$$var1", "$$var2"] } } } } }
        val expected="{ \"\$project\" : { \"the_computed_field\" : { \"\$let\" : { \"vars\" : { \"var1\" : { \"\$sum\" : [\"\$field1\", \"\$field2\"] }, \"var2\" : \"\$field2\" }, \"in\" : { \"\$multiply\" : [\"\$\$var1\", \"\$\$var2\"] } } } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)
    }
    @Test
    fun testProject_zip_operator() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_computed_field"..{
                    zip{
                        inputs {
                            marg..{arrayElementAt { mfld.."matrix"; mval..0 }}
                            marg..{arrayElementAt { mfld.."matrix"; mval..1 }}
                            marg..{arrayElementAt { mfld.."matrix"; mval..2 }}
                        }
                        useLongestLength(true)
                        defaults(mvar.."defaults")
                    }
                }
            }
        }


        //expected: { "$project" : { "the_computed_field" : { "$zip" : { "inputs" : [{ "$arrayElementAt" : ["$matrix", 0] }, { "$arrayElementAt" : ["$matrix", 1] }, { "$arrayElementAt" : ["$matrix", 2] }], "useLongestLength" : true, "defaults" : "$$defaults" } } } }
        val expected="{ \"\$project\" : { \"the_computed_field\" : { \"\$zip\" : { \"inputs\" : [{ \"\$arrayElementAt\" : [\"\$matrix\", 0] }, { \"\$arrayElementAt\" : [\"\$matrix\", 1] }, { \"\$arrayElementAt\" : [\"\$matrix\", 2] }], \"useLongestLength\" : true, \"defaults\" : \"\$\$defaults\" } } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)
    }
    @Test
    fun testProject_reduce_operator() {
        //test various ways to express operator arguments
        val stage1= mongoAggregateStage {
            project {
                "the_computed_field"..{
                    reduce{
                        input(mfld.."discounts")
                        initialValue(mfld.."price")
                        in_expr {
                            multiply {
                                mvar.."value"
                                marg..{ subtract { mval..1;mvar.."this" }
                                }
                            }
                        }
                    }
                }
            }
        }


        //expected: { "$project" : { "the_computed_field" : { "$reduce" : { "input" : "$discounts", "initialValue" : "$price", "in" : { "$multiply" : ["$$value", { "$subtract" : [1, "$$this"] }] } } } } }
        val expected="{ \"\$project\" : { \"the_computed_field\" : { \"\$reduce\" : { \"input\" : \"\$discounts\", \"initialValue\" : \"\$price\", \"in\" : { \"\$multiply\" : [\"\$\$value\", { \"\$subtract\" : [1, \"\$\$this\"] }] } } } } }"

        val gstr1=stage1.toString()
        assertEquals(expected,gstr1)
    }


}
