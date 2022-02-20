/*
 * This file is licensed under the MIT License.
 *
 * Copyright (c) 2022 Aren Marsden
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ammonium.hazel.client.util;

import java.util.function.Function;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Various utilities for reactor utilities.
 */
public final class ReactorUtil {

  /**
   * Filter or execute when a condition is met.
   *
   * @param asyncPredicate the predicate publisher.
   * @param switchMono     the switch.
   * @param <T>            the value passed in
   * @return a function of mono.
   */
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

  /**
   * Filter or execute when a condition is met.
   *
   * @param tester     the tester
   * @param switchMono the executor.
   * @param <T>        the type.
   * @return the function and mono result.
   */
  public static <T> Function<T, Mono<Boolean>> filterOrExecute(Predicate<? super T> tester,
    Mono<?> switchMono) {
    return filterWhenOrExecute(
      value -> Mono.just(tester.test(value)),
      switchMono);
  }

  private ReactorUtil() {
  }

}
