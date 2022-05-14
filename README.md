# Dynamo Mapper

![GitHub release (latest by date)](https://img.shields.io/github/v/release/autonomouslogic/dynamo-mapper)
[![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/autonomouslogic/dynamo-mapper/Test/main)](https://github.com/autonomouslogic/dynamo-mapper/actions)
[![GitHub](https://img.shields.io/github/license/autonomouslogic/dynamo-mapper)](https://spdx.org/licenses/MIT-0.html)

A simple mapper for converting to and from DynamoDB AttributeValues and POJOs using Jackson.

## In Development
Dynamo Mapper is still in development. Breaking changes may be introduced without warning.

## Usage
* ObjectMapper constructions, JSR timestamps, big decimals
* `@JsonInclude(JsonInclude.Include.NON_NULL)`

## Comparison with DynamoDBMapper and DynamoDB Enhanced
The DynamoDBMapper in the v1 AWS SDK and the DynamoDB Enhanced Client in the v2 AWS SDK both provide similar mapping
functionality.

This idea for this library was originally created to provide the same mapping as the v1 mapper, but for the v2 SDK.
The v2 SDK now provides this mapping functionality officially, but this library addresses a few short-comings of both
of the implementations by AWS:

* The annotation must be added to methods and not properties. This makes libraries such as Lombok useless.
* The AWS mappers don't work well with builder classes, which again makes Lombok's `@Value`s useless.
* Java 17 records?
* Custom serializers.

To address all of these, this library uses a well-known JSON parser: Jackson.
However, there are some cons to doing it this way:

* It's not possible to have a different schema in the DynamoDB and JSON versions of an object.
* Speed may be an issue (verify).

## Resources
* https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html
* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
* https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/dynamodb-enhanced-client.html

## Versioning
Dynamo Mapper follows [semantic versioning](https://semver.org/).

## License
Dynamo Mapper is licensed under the [MIT-0 license](https://spdx.org/licenses/MIT-0.html).

## External
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/autonomouslogic/dynamo-mapper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/autonomouslogic/dynamo-mapper/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/autonomouslogic/dynamo-mapper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/autonomouslogic/dynamo-mapper/alerts/)

[![Maintainability](https://api.codeclimate.com/v1/badges/04243b52f38c8cecf66c/maintainability)](https://codeclimate.com/github/autonomouslogic/dynamo-mapper/maintainability)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=autonomouslogic_dynamo-mapper&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=autonomouslogic_dynamo-mapper)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=autonomouslogic_dynamo-mapper&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=autonomouslogic_dynamo-mapper)

![Libraries.io dependency status for latest release](https://img.shields.io/librariesio/release/maven/com.autonomouslogic.dynamomapper:dynamo-mapper)

[![Known Vulnerabilities](https://snyk.io/test/github/autonomouslogic/dynamo-mapper/badge.svg)](https://snyk.io/test/github/autonomouslogic/dynamo-mapper)
