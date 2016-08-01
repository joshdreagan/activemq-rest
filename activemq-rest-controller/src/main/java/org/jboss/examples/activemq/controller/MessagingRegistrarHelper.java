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

import java.util.List;
import org.jboss.examples.activemq.jaxrs.ListAllResponse;
import org.jboss.examples.activemq.jaxrs.ListResponse;
import org.jboss.examples.activemq.jaxrs.SubscriptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingRegistrarHelper {

  private static final Logger log = LoggerFactory.getLogger(MessagingRegistrarHelper.class);

  public Object createSubscribeResponse() {
    return null;
  }

  public Object createUnsubscribeResponse() {
    return null;
  }
  
  public ListResponse createListResponse(SubscriptionInfo subscriptionInfo) {
    ListResponse response = new ListResponse();
    response.setSubscription(subscriptionInfo);
    return response;
  }
  
  public ListAllResponse createListAllResponse(List<SubscriptionInfo> subscriptionInfos) {
    ListAllResponse response = new ListAllResponse();
    for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
      response.getSubscriptions().put(subscriptionInfo.getDestination(), subscriptionInfo);
    }
    return response;
  }
}
