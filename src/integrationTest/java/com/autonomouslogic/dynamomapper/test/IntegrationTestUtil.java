package com.autonomouslogic.dynamomapper.test;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

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
		return DefaultCredentialsProvider.builder()
			.profileName(profileName())
			.build();
	}

	public static AWSCredentialsProvider credentialsProviderV1() {
		return new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(),
			new ProfileCredentialsProvider(profileName()));
	}

	public static AwsRegionProvider regionProvider() {
		return DefaultAwsRegionProviderChain.builder()
			.profileName(profileName())
			.build();
	}

	public static String profileName() {
		return Optional.ofNullable(System.getenv("AWS_PROFILE"))
			.orElseThrow(() -> new RuntimeException("AWS_PROFILE not set"));
	}
}
