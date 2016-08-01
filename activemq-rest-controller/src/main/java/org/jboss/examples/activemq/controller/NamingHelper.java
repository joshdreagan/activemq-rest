/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.examples.activemq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamingHelper {

  private static final Logger log = LoggerFactory.getLogger(NamingHelper.class);
  
  public String calculateRouteId(String subscriberId, String destination) {
    return String.format("__%s_%s_ConsumerRoute", subscriberId, destination);
  }

  public String calculateFinalDestinationName(String subscriberId, String destination) {
    return String.format("Consumer.%s.VirtualTopic.%s", subscriberId, destination);
  }

  public String calculateDLQDestinationName(String subscriberId, String destination) {
    return String.format("ActiveMQ.DLQ.Topic.%s", calculateFinalDestinationName(subscriberId, destination));
  }
}
