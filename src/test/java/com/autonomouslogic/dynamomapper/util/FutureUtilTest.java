package com.autonomouslogic.dynamomapper.util;

import com.autonomouslogic.dynamomapper.function.ThrowingFutureSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FutureUtilTest {
	ThrowingFutureSupplier<CompletableFuture<String>> supplier;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setup() {
		supplier = mock(ThrowingFutureSupplier.class);
	}

	@Test
	@SneakyThrows
	public void shouldWrapFuture() {
		var f = CompletableFuture.completedFuture("test");
		when(supplier.get()).thenReturn(f);
		assertSame(f, FutureUtil.wrapFuture(() -> f));
	}

	@Test
	@SneakyThrows
	public void shouldCatchExceptionsInSupplier() {
		when(supplier.get()).thenThrow(new RuntimeException("test"));
		var f = FutureUtil.wrapFuture(supplier);
		assertThrows(RuntimeException.class, () -> f.join());
	}

	@Test
	@SneakyThrows
	public void shouldWrapFailedExceptions() {
		var f = new CompletableFuture<String>();
		f.completeExceptionally(new RuntimeException("test"));
		when(supplier.get()).thenReturn(f);
		assertSame(f, FutureUtil.wrapFuture(() -> f));
	}
}
