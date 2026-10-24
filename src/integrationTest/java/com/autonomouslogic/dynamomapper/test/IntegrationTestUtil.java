package com.autonomouslogic.dynamomapper.test;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import tools.jackson.databind.json.JsonMapper;

public class IntegrationTestUtil {
	public static final RandomGenerator RNG = new ISAACRandom();

	public static String partitionKey(String prefix) {
		return prefix + "-" + Math.abs(IntegrationTestUtil.RNG.nextLong());
	}

	public static long ttl() {
		return Instant.now().plus(Duration.ofMinutes(1)).getEpochSecond();
	}

	public static DynamoDbClient client() {
		return DynamoDbClient.builder()
				.credentialsProvider(credentialsProvider())
				.region(regionProvider().getRegion())
				.build();
	}

	public static AmazonDynamoDB clientV1() {
		return AmazonDynamoDBClient.builder()
				.withCredentials(credentialsProviderV1())
				.withRegion(regionProvider().getRegion().id())
				.build();
	}

	public static DynamoDbAsyncClient asyncClient() {
		return DynamoDbAsyncClient.builder()
				.credentialsProvider(credentialsProvider())
				.region(regionProvider().getRegion())
				.build();
	}

	public static AwsCredentialsProvider credentialsProvider() {
		return DefaultCredentialsProvider.builder().profileName(profileName()).build();
	}

	public static AWSCredentialsProvider credentialsProviderV1() {
		// Bridges the v2 provider so assumed-role profiles (role_arn/source_profile in ~/.aws/config) resolve via
		// STS. The v1 SDK's own profile/env providers don't support CLI-style assume-role profiles:
		// https://github.com/aws/aws-sdk-java/issues/803
		var v2Provider = credentialsProvider();
		return new AWSCredentialsProvider() {
			@Override
			public AWSCredentials getCredentials() {
				var credentials = v2Provider.resolveCredentials();
				if (credentials instanceof AwsSessionCredentials sessionCredentials) {
					return new BasicSessionCredentials(
							sessionCredentials.accessKeyId(),
							sessionCredentials.secretAccessKey(),
							sessionCredentials.sessionToken());
				}
				return new BasicAWSCredentials(credentials.accessKeyId(), credentials.secretAccessKey());
			}

			@Override
			public void refresh() {}
		};
	}

	public static AwsRegionProvider regionProvider() {
		return DefaultAwsRegionProviderChain.builder()
				.profileName(profileName())
				.build();
	}

	public static String profileName() {
		return System.getenv("AWS_PROFILE");
	}

	@SneakyThrows
	public static List<IntegrationTestObject> loadIntegrationTestObjects() {
		try (var in =
				Objects.requireNonNull(IntegrationTestUtil.class.getResourceAsStream("/integration-tests.jsonl"))) {
			return new JsonMapper()
					.readerFor(IntegrationTestObject.class)
					.<IntegrationTestObject>readValues(in)
					.readAll();
		}
	}
}
