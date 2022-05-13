package com.autonomouslogic.dynamomapper.util;

import com.autonomouslogic.dynamomapper.function.ThrowingFutureSupplier;

import java.util.concurrent.CompletableFuture;

public class FutureUtil {
	public static <T> CompletableFuture<T> wrapFuture(ThrowingFutureSupplier<CompletableFuture<T>> function) {
		try {
			return function.get();
		}
		catch (Exception e) {
			CompletableFuture<T> future = new CompletableFuture<>();
			future.completeExceptionally(e);
			return future;
		}
	}
}
