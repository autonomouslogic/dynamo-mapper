package com.autonomouslogic.dynamomapper.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class PublisherUtil {
	public static <T> List<T> collectBlocking(Publisher<T> publisher) {
		var items = new ArrayList<T>();
		var latch = new CountDownLatch(1);
		var error = new AtomicReference<Throwable>();
		publisher.subscribe(new Subscriber<>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(T t) {
				items.add(t);
			}

			@Override
			public void onError(Throwable t) {
				error.set(t);
				latch.countDown();
			}

			@Override
			public void onComplete() {
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
		if (error.get() != null) {
			throw new RuntimeException(error.get());
		}
		return items;
	}
}
