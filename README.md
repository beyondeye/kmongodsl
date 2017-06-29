[![Kotlin 1.1.3](https://img.shields.io/badge/Kotlin-1.1.3-blue.svg)](http://kotlinlang.org)
[![](https://jitpack.io/v/beyondeye/kmongoDSL.svg)](https://jitpack.io/#beyondeye/kmongodsl)
# kmongoDSL: A Kotlin DSL for MongoDB

 
##### Table of Contents 
- [Gradle dependencies](#gradledeps)
- [Introduction to KMongoDSL](#kmongodsl_intro)
- [Examples](#kmongodsl_examples)
- [Disclaimer and issues](#kmongodsl_issues)
- [License](#kmongodsl_license)

<a name="gradledeps"></a>
## dependencies for gradle
```groovy
// First, add JitPack to your repositories
repositories {
    //...
    maven { url "https://jitpack.io" }
}

// main kmongodsl package: dependens only from org.mongodb:bson
    compile 'com.github.beyondeye:kmongodsl:0.1.0'
```

<a name="kmongodsl_intro"></a>
# An introduction to KMongoDSL
MongoDB [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/) is very powerful. But writing complex multistage
aggregations is error-prone. Also  the extensive use of the "$" sign in MongoDB command syntax
create many problems in Kotlin where the same symbol is used for triggering string interpolation.
KMongo DSL  solve this problem and has also several other advantages:
- Auto completion of name of aggregation stages, operators and operator parameters
- auto indenting and code folding like regular Kotlin code
- Directly output BSON without the need to parse a JSON string.

The library

<a name="kmongodsl_examples"></a>
# Examples
Let's see some example of increasing complexity
## Project Stage: select fields to include

in json
```json
{
  "$project": {
    "fieldToInclude": 1
  }
}
```


with kmongodsl
```kotlin
val stage= mongoAggregateStage
        {
            project {
                +"fieldToInclude"
            }
        }
```

<a name="kmongodsl_issues"></a>

## Project Stage: select fields to exclude

in json
```json
{
  "$project": {
    "fieldToExclude": 0
  }
}
```


with kmongodsl
```kotlin
 val stage= mongoAggregateStage
        {
            project {
                -"fieldToExclude"
            }
        }
```

## Project Stage: select renamed fields

in json
```json
{
  "$project": {
    "fieldNewName": "fieldToInclude"
  }
}
```


with kmongodsl
```kotlin
  val stage= mongoAggregateStage
         {
             project {
                 "fieldNewName".."fieldToInclude"
             }
         }
```

## Project Stage: select a computed field

in json
```json
{
  "$project": {
    "the_sliced_field": {
      "$slice": ["$fieldtoslice", 1, 10]
    }
  }
}
```


with kmongodsl
```kotlin
        val stage= mongoAggregateStage {
            project {
                "the_sliced_field"..{
                    slice("fieldtoslice", 1, 10)
                }
            }
        }
```

## Project Stage: select a computed field, with more general form for operator arguments
In the previous example we saw a short form definition for the `slice` operator. Not all operators
support this. Now we will see the most general form for specifying arguments for
operators that take an array of arguments as input

in json
```json
{
  "$project": {
    "the_sliced_field": {
      "$slice": [
        {"$sum": ["$a", "$b"]},
        1,
        10
      ]
    }
  }
}
```


with kmongodsl
```kotlin
        val stage4= mongoAggregateStage {
            project {
                "the_sliced_field"..{
                    slice{
                        marg..{ sum { mfld.."a";mfld.."b" } }
                        mval..1
                        mval..10
                    }
                }
            }
        }
```
In the code above  we see some inportant conventions used in the DSL
- __marg..[expression]__ is used to specify a general expression as argument
- __fld..[fieldName]__  is used to specify a field in the source document as argument
- __mval..[constant]__ is used to specify a literal constant as argument
- __mvar..[variableName]__ is used to specify a mongodb variable as argument (not in the example)

Also another important thing to note is that in the DSL is possible to specify more than one argument per line by separating them with a semicolumn

## Project Stage: $let operator

in json
```json
{
  "$project": {
    "the_computed_field": {
      "$let": {
        "vars": {
          "var1": "$field1",
          "var2": "$field2"
        },
        "in": {
          "$multiply": [
            "$$var1",
            "$$var2"
          ]
        }
      }
    }
  }
}
```


with kmongodsl
```kotlin
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
```

Notice that `in` was renamed `in_` because this is a reserved keyword in Kotlin

## Project Stage: $map operator

in json
```json
{
  "$project": {
    "the_computed_field": {
      "$map": {
        "input": "$quizzes",
        "as": "grade",
        "in": {
          "$sum": ["$$grade",2 ]
        }
      }
    }
  }
}
```


with kmongodsl
```kotlin
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
```


## Project Stage: $let operator with computed variables definition

in json
```json
{
  "$project": {
    "the_computed_field": {
      "$let": {
        "vars": {
          "var1": {
            "$sum": ["$field1", "$field2"]
          },
          "var2": "$field2"
        },
        "in": {
          "$multiply": ["$$var1", "$$var2"]
        }
      }
    }
  }
}
```


with kmongodsl
```kotlin
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
```

## Project Stage: $zip operator

in json
```json
{
  "$project": {
    "the_computed_field": {
      "$zip": {
        "inputs": [
          {"$arrayElementAt": ["$matrix", 0]},
          {"$arrayElementAt": ["$matrix", 1]},
          {"$arrayElementAt": ["$matrix", 2]}
        ],
        "useLongestLength": true,
        "defaults": "$$defaults"
      }
    }
  }
}
```


with kmongodsl
```kotlin
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
```

A very important thing to notice here is that for input fields  that are actually
an array of expressions (like `inputs` in the example above), the syntax is to put one expression per line with the syntax `marg..<expression>`
(or also on the same line separated by a semicolumn).
This is also true if the expression in the array is not a complex expression, but instead a field in the source document. Also
in this case you need to use the `marg..<>` syntax
This is not very intuitive.
This is even more confusing because for simple operators (operators that takes a
simple array of expressions (like `$arrayElementAt`, `$multiply`, etc..) and not an object containing a field that is
an array of expressions (like `$let`,`$map`,`$reduce`, etc..) , the syntax is much simpler (as shown in the examples above
This complex syntax using `marg..`  will probably change in the future.

Anyway the autocompletion features of the DSL will help a lot in such cases for remembering the correct syntax
Notice that `as` was also renamed as `as_` because it is a reserved keyword in Kotlin

## Project Stage: $reduce operator
in json
```json
{
  "$project": {
    "the_computed_field": {
      "$reduce": {
        "input": "$discounts",
        "initialValue": "$price",
        "in": {
          "$multiply": ["$$value", {"$subtract": [1, "$$this"]}
          ]
        }
      }
    }
  }
}
```


with kmongodsl
```kotlin
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
```



<a name="kmongodsl_issues"></a>
# Disclaimer and known issues
The code is still in development and incomplete (not all mongodb operators and aggregation stages are implemented)
 and the syntax is not yet final


<a name="kmongodsl_license"></a>
## License
~~~
Copyright 2017 Dario Elyasy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
~~~
