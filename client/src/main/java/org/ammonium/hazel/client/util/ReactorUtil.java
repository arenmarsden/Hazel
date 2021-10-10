package org.ammonium.hazel.client.util;

import java.util.function.Function;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public final class ReactorUtil {

  public static <T> Function<T, Mono<Boolean>> filterWhenOrExecute(
    Function<? super T, ? extends Publisher<Boolean>> asyncPredicate, Mono<?> switchMono) {
    return value -> Mono.from(asyncPredicate.apply(value))
      .flatMap(bool -> {
        if (!bool) {
          return switchMono
            .thenReturn(false);
        }
        return Mono.just(true);
      });
  }

  public static <T> Function<T, Mono<Boolean>> filterOrExecute(Predicate<? super T> tester,
    Mono<?> switchMono) {
    return filterWhenOrExecute(
      value -> Mono.just(tester.test(value)),
      switchMono);
  }

  private ReactorUtil() {
  }

}
