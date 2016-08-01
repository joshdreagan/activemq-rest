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

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQHelper {

  private static final Logger log = LoggerFactory.getLogger(ActiveMQHelper.class);

  private BrokerService brokerService;

  private NamingHelper namingHelper;

  public BrokerService getBrokerService() {
    return brokerService;
  }

  public void setBrokerService(BrokerService brokerService) {
    this.brokerService = brokerService;
  }

  public NamingHelper getNamingHelper() {
    return namingHelper;
  }

  public void setNamingHelper(NamingHelper namingHelper) {
    this.namingHelper = namingHelper;
  }

  public void removeConsumerQueues(String subscriberId, String destination) {
    try {
      brokerService.getAdminView().removeQueue(namingHelper.calculateFinalDestinationName(subscriberId, destination));
      brokerService.getAdminView().removeQueue(namingHelper.calculateDLQDestinationName(subscriberId, destination));
    } catch (Exception e) {
      throw new RuntimeCamelException(String.format("Unable to remove route for subscriberId: [%s], and destination: [%s].", subscriberId, destination), e);
    }
  }
}
