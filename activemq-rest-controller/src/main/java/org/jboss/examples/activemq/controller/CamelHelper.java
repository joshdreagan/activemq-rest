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

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jms.ConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.cxf.jaxrs.CxfRsComponent;
import org.apache.camel.component.cxf.jaxrs.CxfRsEndpoint;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.examples.activemq.jaxrs.Message;
import org.jboss.examples.activemq.jaxrs.MessageConsumer;
import org.jboss.examples.activemq.jaxrs.SubscriptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelHelper {

  private static final Logger log = LoggerFactory.getLogger(CamelHelper.class);

  private CamelContext camelContext;
  private ConnectionFactory connectionFactory;
  
  private NamingHelper namingHelper;
  private JsonHelper jsonHelper;
  
  public void setCamelContext(CamelContext camelContext) {
    this.camelContext = camelContext;
  }

  public CamelContext getCamelContext() {
    return camelContext;
  }

  public ConnectionFactory getConnectionFactory() {
    return connectionFactory;
  }

  public void setConnectionFactory(ConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  public NamingHelper getNamingHelper() {
    return namingHelper;
  }

  public void setNamingHelper(NamingHelper namingHelper) {
    this.namingHelper = namingHelper;
  }

  public JsonHelper getJsonHelper() {
    return jsonHelper;
  }

  public void setJsonHelper(JsonHelper jsonHelper) {
    this.jsonHelper = jsonHelper;
  }
  
  public boolean consumerCamelRouteExists(String subscriberId, String destination) {
    String routeId = namingHelper.calculateRouteId(subscriberId, destination);
    RouteDefinition routeDefinition = camelContext.getRouteDefinition(routeId);
    return (routeDefinition != null);
  }

  public void addConsumerCamelRoutes(String subscriberId, String destination, String selector, URL url) {
    String routeId = namingHelper.calculateRouteId(subscriberId, destination);
    RouteDefinition routeDefinition = camelContext.getRouteDefinition(routeId);
    if (routeDefinition == null) {
      try {
        camelContext.addRoutes(createRouteBuilder(subscriberId, destination, selector, url));
      } catch (Exception e) {
        throw new RuntimeCamelException(String.format("Unable to create route for subscriberId: [%s], and destination: [%s].", subscriberId, destination), e);
      }
    } else {
      log.debug(String.format("Subscription already exists for subscriberId: [%s], and destination: [%s].", subscriberId, destination));
    }
  }

  public void removeConsumerCamelRoutes(String subscriberId, String destination) {
    String routeId = namingHelper.calculateRouteId(subscriberId, destination);
    RouteDefinition routeDefinition = camelContext.getRouteDefinition(routeId);
    if (routeDefinition != null) {
      try {
        camelContext.removeRouteDefinition(routeDefinition);
      } catch (Exception e) {
        throw new RuntimeCamelException(String.format("Unable to remove route for subscriberId: [%s], and destination: [%s].", subscriberId, destination), e);
      }
    } else {
      log.debug(String.format("No subscription exists for subscriberId: [%s], and destination: [%s].", subscriberId, destination));
    }
  }

  public SubscriptionInfo getSubscriptionInfo(String subscriberId, String destination) {
    SubscriptionInfo subscriptionInfo = null;
    String routeId = namingHelper.calculateRouteId(subscriberId, destination);
    RouteDefinition routeDefinition = camelContext.getRouteDefinition(routeId);
    if (routeDefinition != null) {
      try {
        subscriptionInfo = jsonHelper.unmarshalSubscriptionInfo(routeDefinition.getDescriptionText());
      } catch (Exception e) {
        throw new RuntimeCamelException(String.format("Unable to retrieve subscription info for subscriberId: [%s], and destination: [%s].", subscriberId, destination), e);
      }
    } else {
      log.debug(String.format("No subscription exists for subscriberId: [%s], and destination: [%s].", subscriberId, destination));
    }
    return subscriptionInfo;
  }

  public List<SubscriptionInfo> getSubscriptionInfo(String subscriberId) {
    List<SubscriptionInfo> subscriptionInfos = new ArrayList<>();
    String routeIdPattern = String.format("\\Q%s\\E", namingHelper.calculateRouteId(subscriberId, "\\E.+\\Q"));
    for (RouteDefinition routeDefinition : camelContext.getRouteDefinitions()) {
      if (routeDefinition.getId() != null && routeDefinition.getId().matches(routeIdPattern)) {
        try {
          SubscriptionInfo subscriptionInfo = jsonHelper.unmarshalSubscriptionInfo(routeDefinition.getDescriptionText());
          subscriptionInfos.add(subscriptionInfo);
        } catch (Exception e) {
          throw new RuntimeCamelException(String.format("Unable to retrieve subscription info for subscriberId: [%s].", subscriberId), e);
        }
      }
    }
    return subscriptionInfos;
  }

  public List<SubscriptionInfo> getSubscriptionInfo() {
    return getSubscriptionInfo("\\E.+\\Q");
  }

  protected JmsEndpoint generateJmsConsumerEndpoint(String subscriberId, String destination, String selector) throws Exception {
    ActiveMQComponent component = camelContext.getComponent("activemq", ActiveMQComponent.class);
    JmsEndpoint endpoint = (JmsEndpoint) component.createEndpoint(String.format("activemq:queue:%s", namingHelper.calculateFinalDestinationName(subscriberId, destination)));
    endpoint.setConnectionFactory(connectionFactory);
    endpoint.setDisableReplyTo(true);
    endpoint.setAcknowledgementModeName("CLIENT_ACKNOWLEDGE");
    endpoint.setSelector(selector);
    return endpoint;
  }
  
  protected CxfRsEndpoint generateCxfProducerEndpoint(URL url) throws Exception {
    CxfRsComponent component = camelContext.getComponent("cxfrs", CxfRsComponent.class);
    CxfRsEndpoint endpoint = (CxfRsEndpoint) component.createEndpoint(String.format("cxfrs:%s", url));
    endpoint.setResourceClasses(MessageConsumer.class);
    endpoint.setHttpClientAPI(false);
    endpoint.setProviders(Arrays.asList(new JacksonJsonProvider()));
    endpoint.setSkipFaultLogging(true);
    return endpoint;
  }
  
  protected SubscriptionInfo createSubscriptionInfo(String subscriberId, String destination, String selector, URL url) {
    SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
    subscriptionInfo.setSubscriberId(subscriberId);
    subscriptionInfo.setDestination(destination);
    subscriptionInfo.setSelector(selector);
    subscriptionInfo.setUrl(url);
    return subscriptionInfo;
  }
  
  protected RouteBuilder createRouteBuilder(String subscriberId, String destination, String selector, URL url) {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from(generateJmsConsumerEndpoint(subscriberId, destination, selector))
          .id(namingHelper.calculateRouteId(subscriberId, destination))
          .description(jsonHelper.marshalSubscriptionInfo(createSubscriptionInfo(subscriberId, destination, selector, url)))
          .errorHandler(loggingErrorHandler(log, LoggingLevel.TRACE))
          .onException(ConnectException.class)
            .handled(true)
            .log(String.format("Failed to deliver message ${headers[JMSMessageID]} to endpoint [%s].", url))
            .markRollbackOnly()
          .end()
          .unmarshal().json(JsonLibrary.Jackson, Message.class)
          .setHeader(CxfConstants.OPERATION_NAME).constant("onMessage")
          .setBody().spel("#{new Object[]{request.body}}")
          .to(generateCxfProducerEndpoint(url));
      }
    };
  }
}
