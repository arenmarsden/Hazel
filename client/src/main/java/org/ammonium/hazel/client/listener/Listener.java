/*
 * Ammonium Studios, Inc. ("Ammonium") CONFIDENTIAL
 * Unpublished Copyright (c) 2021 Ammonium, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of Ammonium.
 * The intellectual and technical concepts contained herein are proprietary to Ammonium and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from Ammonium. Access to the source code
 * contained herein is hereby forbidden to anyone except current Ammonium employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure of
 * this source code, which includes information that is confidential and/or proprietary, and is a
 * trade secret, of  Ammonium.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT
 * OF Ammonium IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS AND
 * INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION
 * DOES NOT CONVEY OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR
 * TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.
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
  Mono<Void> execute(T event);

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
