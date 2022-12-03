# Dynamo Mapper

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/autonomouslogic/dynamo-mapper)](https://github.com/autonomouslogic/dynamo-mapper/releases)
[![javadoc](https://javadoc.io/badge2/com.autonomouslogic.dynamomapper/dynamo-mapper/javadoc.svg)](https://javadoc.io/doc/com.autonomouslogic.dynamomapper/dynamo-mapper)
[![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/autonomouslogic/dynamo-mapper/Test/main)](https://github.com/autonomouslogic/dynamo-mapper/actions)
[![GitHub](https://img.shields.io/github/license/autonomouslogic/dynamo-mapper)](https://spdx.org/licenses/MIT-0.html)

A simple mapper for converting to and from DynamoDB [AttributeValues](https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html)
and POJOs using [Jackson](https://github.com/FasterXML/jackson).

## In development
Dynamo Mapper is still in development.
Currently, only single gets, puts, updates, and deletes are supported.
More is planned in the future and submissions are welcome.

## Installation
Dynamo Mapper is available from [Maven Central](https://search.maven.org/search?q=g:com.autonomouslogic.dynamomapper%20AND%20a:dynamo-mapper).

### Gradle

```groovy
implementation 'com.autonomouslogic.dynamomapper:dynamo-mapper:version'
```

### Maven

```xml
<dependency>
    <groupId>com.autonomouslogic.dynamomapper</groupId>
    <artifactId>dynamo-mapper</artifactId>
    <version>version</version>
</dependency>
```

## Usage
Dynamo Mapper functions as a wrapper around the existing AWS DynamoDB v2 SDK.
First, you need to construct a DynamoDB client object and a Jackson ObjectMapper, and then pass those to the
Dynamo Mapper builder.
If you do not provide a DynamoDB client or Jackson ObjectMapper, defaults will be used.

```java
var dynamoMapper = DynamoMapper.builder()
    .client(DynamoDbClient.create())
    .objectMapper(new ObjectMapper())
    .build();
```

Or, for the asynchronous API:
```java
var asyncDynamoMapper = DynamoAsyncMapper.builder()
    .client(DynamoDbAsyncClient.create())
    .objectMapper(new ObjectMapper())
    .build();
```

### Defining schemas
Dynamo Mapper relies on Jackson for its serialization.
Generally, anything that works in Jackson will work in Dynamo Mapper.
Only a few extra annotations are required.

```java
@DynamoTableName("table-name")
public class ModelObject {
	@JsonProperty
	@DynamoPrimaryKey
	private String primaryKey;
	
	@JsonProperty
	private String otherValue;
	
	/* etc. */
}
```

### Getting items
Using the synchronous API:
```java
var item = dynamoMapper.getItemFromPrimaryKey("key-value", ModelObject.class).item();
```

Using the asynchronous API:
```java
dynamoMapper.getItemFromPrimaryKey("key-value", ModelObject.class).thenAccept(response -> {
	var item = response.item();
});
```

### Putting items
Using the synchronous API:
```java
var item = new ModelObject().setPrimaryKey("key-value");
var response = dynamoMapper.putItemFromKeyObject(item);
```

Using the asynchronous API:
```java
var item = new ModelObject().setPrimaryKey("key-value");
dynamoMapper.putItemFromKeyObject(item).thenAccept(response -> {
	// etc.
});
```

## Best practices
* Jackson will include all null values, to prevent this taking up space in DynamoDB, use `@JsonInclude(JsonInclude.Include.NON_NULL)`
* To properly serialize BigDecimals, use `objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)`
* To encode `java.time` objects properly, use `jackson-datatype-jsr310`

## Comparison with DynamoDBMapper and DynamoDB Enhanced
The [DynamoDBMapper in the v1 AWS SDK](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html)
and the [DynamoDB Enhanced Client in the v2 AWS SDK](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/dynamodb-enhanced-client.html)
both provide similar mapping functionality.

This idea for this library was originally created to provide the same mapping as the v1 mapper, but for the v2 SDK,
before the enhanced client was released.
The v2 SDK now provides this mapping functionality officially, but has a few short-comings.
For instance, the annotations must be added to methods and not properties.
This makes [Lombok](https://projectlombok.org/) models useless.
Jackson is a widely used and mature library. with advanced features that will never, and shouldn't, be implemented in
the enhanced client.

There are some cons to doing it this way.
For instance, it's not possible to have a different schema in the DynamoDB and JSON versions of an object.

| Feature           | Dynamo Mapper | DynamoDBMapper (v1) | DynamoDb Enhanced Client (v2) |
|-------------------|---------------|---------------------|-------------------------------|
| Synchronous API   | Yes           | Yes                 | Yes                           |
| Asynchronous API  | Yes           | No                  | Yes                           |
| Lombok compatible | Yes           | Partial?            | No?                           |

## Resources
* https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html
* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
* https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/dynamodb-enhanced-client.html
* https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/package-summary.html
* https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/dynamodb/package-summary.html

## Versioning
Dynamo Mapper follows [semantic versioning](https://semver.org/).

## Code Style
Dynamo Mapper follows Palantir with tabs.
Automatic code formatting can be done by running `./gradlew spotlessApply`.

## License
Dynamo Mapper is licensed under the [MIT-0 license](https://spdx.org/licenses/MIT-0.html).

## Status
| Type          | Status                                                                                                                                                                                                                                                                                                                                                                                                                |
|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| LGTM          | [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/autonomouslogic/dynamo-mapper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/autonomouslogic/dynamo-mapper/context:java) [![Total alerts](https://img.shields.io/lgtm/alerts/g/autonomouslogic/dynamo-mapper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/autonomouslogic/dynamo-mapper/alerts/)                              |
| CodeClimate   | [![Maintainability](https://api.codeclimate.com/v1/badges/04243b52f38c8cecf66c/maintainability)](https://codeclimate.com/github/autonomouslogic/dynamo-mapper/maintainability)                                                                                                                                                                                                                                        |
| SonarCloud    | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=autonomouslogic_dynamo-mapper&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=autonomouslogic_dynamo-mapper) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=autonomouslogic_dynamo-mapper&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=autonomouslogic_dynamo-mapper) |
| Libraries.io  | ![Libraries.io dependency status for latest release](https://img.shields.io/librariesio/release/maven/com.autonomouslogic.dynamomapper:dynamo-mapper)                                                                                                                                                                                                                                                                 |
| Snyk          | [![Known Vulnerabilities](https://snyk.io/test/github/autonomouslogic/dynamo-mapper/badge.svg)](https://snyk.io/test/github/autonomouslogic/dynamo-mapper)                                                                                                                                                                                                                                                            |
| Codecov       | [![codecov](https://codecov.io/gh/autonomouslogic/dynamo-mapper/branch/main/graph/badge.svg?token=C5CO3GPGV3)](https://codecov.io/gh/autonomouslogic/dynamo-mapper)                                                                                                                                                                                                                                                   |
| Synatype Lift | [link](https://lift.sonatype.com/)                                                                                                                                                                                                                                                                                                                                                                                    |

