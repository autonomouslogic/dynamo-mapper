package com.autonomouslogic.dynamomapper.test;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;

public class IntegrationTestUtil {
	public static DynamoDbClient client() {
		return DynamoDbClient.builder()
			.credentialsProvider(credentialsProvider())
			.region(regionProvider().getRegion())
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
