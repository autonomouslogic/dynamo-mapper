package com.autonomouslogic.dynamomapper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ListUtilTest {
	@Test
	void shouldReturnElementClass() {
		assertEquals(String.class, ListUtil.elementClass(List.of("a", "b")));
	}

	@Test
	void shouldReturnElementClassForSingleElement() {
		assertEquals(Integer.class, ListUtil.elementClass(List.of(42)));
	}

	@Test
	void shouldThrowOnEmptyList() {
		assertThrows(IllegalArgumentException.class, () -> ListUtil.elementClass(List.of()));
	}

	@Test
	void shouldThrowOnNullList() {
		assertThrows(IllegalArgumentException.class, () -> ListUtil.elementClass(null));
	}
}
