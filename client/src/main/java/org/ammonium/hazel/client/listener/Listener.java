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

package org.ammonium.hazel.client.listener;

import discord4j.core.event.domain.Event;
import org.checkerframework.checker.nullness.qual.NonNull;
import reactor.core.publisher.Mono;

/**
 * Represents a listener.
 *
 * @param <T> the type.
 */
public interface Listener<T extends Event> {

  /**
   * Get the event class.
   *
   * @return the class.
   */
  @NonNull Class<T> getEventClass();

  /**
   * Execute the listener.
   *
   * @param event the event.
   * @return a {@link Mono} containing a void.
   */
  Mono<?> execute(T event);

  /**
   * Handle an error given by the execution of the listener.
   *
   * @param throwable the error.
   * @return a {@link Mono} voiding the result and handling it.
   */
  default Mono<Void> handleError(Throwable throwable) {
    return Mono.error(throwable);
  }

}
