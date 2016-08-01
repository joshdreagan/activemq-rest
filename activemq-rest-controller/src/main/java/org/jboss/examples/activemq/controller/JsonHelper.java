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

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.examples.activemq.jaxrs.SubscribeRequest;
import org.jboss.examples.activemq.jaxrs.SubscriptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper {

  private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);

  private ObjectMapper jsonMapper = new ObjectMapper();

  public ObjectMapper getJsonMapper() {
    return jsonMapper;
  }

  public void setJsonMapper(ObjectMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  public String marshalSubscriptionInfo(SubscriptionInfo subscriptionInfo) throws IOException {
    return jsonMapper.writeValueAsString(subscriptionInfo);
  }

  public SubscriptionInfo unmarshalSubscriptionInfo(String subscriptionInfoText) throws IOException {
    return jsonMapper.readValue(subscriptionInfoText, SubscriptionInfo.class);
  }

  public String marshalSubscribeRequest(SubscribeRequest subscribeRequest) throws IOException {
    return jsonMapper.writeValueAsString(subscribeRequest);
  }

  public SubscribeRequest unmarshalSubscribeRequest(String subscribeRequestText) throws IOException {
    return jsonMapper.readValue(subscribeRequestText, SubscribeRequest.class);
  }
}
