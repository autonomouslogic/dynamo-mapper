# Dynamo Mapper

![GitHub release (latest by date)](https://img.shields.io/github/v/release/autonomouslogic/dynamo-mapper)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/autonomouslogic/dynamo-mapper/Test/main)
![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/autonomouslogic/dynamo-mapper?style=flat-square)
![Libraries.io dependency status for latest release](https://img.shields.io/librariesio/release/maven/com.autonomouslogic.dynamomapper:dynamo-mapper)
![GitHub](https://img.shields.io/github/license/autonomouslogic/dynamo-mapper)

A simple mapper for converting to and from DynamoDB AttributeValues and POJOs using Jackson.

## Comparison with DynamoDBMapper and DynamoDB Enhanced
The DynamoDBMapper in the v1 AWS SDK and the DynamoDB Enhanced Client in the v2 AWS SDK both provide similar mapping
functionality.

This idea for this library was originally created to provide the same mapping as the v1 mapper, but for the v2 SDK.
The v2 SDK now provides this mapping functionality officially, but this library addresses a few short-comings of both
of the implementations by AWS:

* The annotation must be added to methods and not properties. This makes libraries such as Lombok useless.
* The AWS mappers don't work well with builder classes, which again makes Lombok's `@Value`s useless.
* Java 17 records?

To address all of these, this library uses a well-known JSON parser: Jackson.
However, there are some cons to doing it this way:

* It's not possible to have a different schema in the DynamoDB and JSON versions of an object.
* Speed may be an issue (verify).
