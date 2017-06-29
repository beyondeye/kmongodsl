[![Kotlin 1.1.3](https://img.shields.io/badge/Kotlin-1.1.3-blue.svg)](http://kotlinlang.org)
[![](https://jitpack.io/v/beyondeye/kmongosql.svg)](https://jitpack.io/#beyondeye/kmongodsl)
# kmongoDSL: A Kotlin DSL for MongoDB

 
##### Table of Contents 
- [Gradle dependencies](#gradledeps)
- [Introduction to KMongoDSL](#kmongodsl_intro)
- [Examples](#kmongodsl_examples)
- [Disclaimer and issues](#kmongodsl_issues)
- [Licence](#kmongodsl_licence)

<a name="gradledeps"></a>
## dependencies for gradle
```groovy
// First, add JitPack to your repositories
repositories {
    //...
    maven { url "https://jitpack.io" }
}

// main kmongodsl package
compile 'com.github.beyondeye.kmongodsl:kmongodsl:0.1.0'
```

<a name="kmongodsl_intro"></a>
# An introduction to KMongoDSL
MongoDB [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/) is very powerful. But writing complex multistage
aggregations is error-prone. Also  the extensive use of the "$" sign in MongoDB command syntax
create many problems in Kotlin where the same symbol is used for triggering string interpolation.
KMongo DSL  solve this problem and has also have several other advantages:
- Auto completion of name of aggregation stages, operators and operator parameters
- auto indenting and code folding like regular Kotlin code
- Directly output BSON without the need to parse a JSON string.

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
# Disclaimer and known issues
The code is still in development and the syntax is not yet final


<a name="kmongodsl_licence"></a>
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
