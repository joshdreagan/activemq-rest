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

import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.examples.activemq.jaxrs.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsHeaderProcessor implements Processor {

  private static final Logger log = LoggerFactory.getLogger(JmsHeaderProcessor.class);

  @Override
  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn().getBody(Message.class);
    for (Map.Entry<String, Object> header : message.getHeaders().entrySet()) {
      log.debug(String.format("Adding JMS header: key=[%s], value=[%s].", header.getKey(), header.getValue()));
      exchange.getIn().setHeader(header.getKey(), header.getValue());
    }
  }
}
