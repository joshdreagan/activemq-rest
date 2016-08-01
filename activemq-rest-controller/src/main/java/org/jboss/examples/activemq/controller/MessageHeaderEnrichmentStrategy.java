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

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHeaderEnrichmentStrategy implements AggregationStrategy {

  private static final Logger log = LoggerFactory.getLogger(MessageHeaderEnrichmentStrategy.class);
  
  private String headerName;

  public String getHeaderName() {
    return headerName;
  }

  public void setHeaderName(String headerName) {
    this.headerName = headerName;
  }

  @Override
  public Exchange aggregate(Exchange original, Exchange resource) {
    if (original == null) {
      return resource;
    }
    
    original.getIn().setHeader(headerName, resource.getIn().getBody());
    return original;
  }
}
