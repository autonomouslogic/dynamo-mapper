package com.autonomouslogic.dynamomapper.util;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.autonomouslogic.dynamomapper.function.ThrowingFutureSupplier;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FutureUtilTest {
	ThrowingFutureSupplier<CompletableFuture<String>> supplier;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setup() {
		supplier = mock(ThrowingFutureSupplier.class);
	}

	@Test
	@SneakyThrows
	void shouldWrapFuture() {
		var f = CompletableFuture.completedFuture("test");
		when(supplier.get()).thenReturn(f);
		assertSame(f, FutureUtil.wrapFuture(() -> f));
	}

	@Test
	@SneakyThrows
	void shouldCatchExceptionsInSupplier() {
		when(supplier.get()).thenThrow(new RuntimeException("test"));
		var f = FutureUtil.wrapFuture(supplier);
		assertThrows(RuntimeException.class, () -> f.join());
	}

	@Test
	@SneakyThrows
	void shouldWrapFailedExceptions() {
		var f = new CompletableFuture<String>();
		f.completeExceptionally(new RuntimeException("test"));
		when(supplier.get()).thenReturn(f);
		assertSame(f, FutureUtil.wrapFuture(() -> f));
	}
}
