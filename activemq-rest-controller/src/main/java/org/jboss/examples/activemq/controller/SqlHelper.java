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
import java.util.Map;
import org.jboss.examples.activemq.jaxrs.SubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlHelper {

  private static final Logger log = LoggerFactory.getLogger(SqlHelper.class);

  public static final String SUBSCRIBER_ID_KEY = "subscriber_id";
  public static final String DESTINATION_KEY = "destination";
  public static final String SUBSCRIPTION_REQUEST_KEY = "subscription_request";

  private JsonHelper jsonHelper;

  public JsonHelper getJsonHelper() {
    return jsonHelper;
  }

  public void setJsonHelper(JsonHelper jsonHelper) {
    this.jsonHelper = jsonHelper;
  }

  public Object[] createUnsubscribeRequest(Map<String, Object> record) {
    Object[] objects = new Object[2];
    objects[0] = record.get(SUBSCRIBER_ID_KEY);
    objects[1] = record.get(DESTINATION_KEY);
    return objects;
  }

  public Object[] createSubscribeRequest(Map<String, Object> record) throws Exception {
    Object[] objects = new Object[3];
    objects[0] = record.get(SUBSCRIBER_ID_KEY);
    objects[1] = record.get(DESTINATION_KEY);
    objects[2] = jsonHelper.unmarshalSubscribeRequest((String) record.get(SUBSCRIPTION_REQUEST_KEY));
    return objects;
  }
    
  public Object[] createInsertRecordRequest(String subscriberId, String destination, SubscribeRequest request) throws IOException {
    Object[] objects = new Object[3];
    objects[0] = subscriberId;
    objects[1] = destination;
    objects[2] = jsonHelper.marshalSubscribeRequest(request);
    return objects;
  }

  public Object[] createDeleteRecordRequest(String subscriberId, String destination) {
    Object[] objects = new Object[2];
    objects[0] = subscriberId;
    objects[1] = destination;
    return objects;
  }

  public Object[] createCountRecordRequest(String subscriberId, String destination) {
    Object[] objects = new Object[2];
    objects[0] = subscriberId;
    objects[1] = destination;
    return objects;
  }
}
