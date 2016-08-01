# ActiveMQ REST

## Requirements

* Apache Maven 3.x ([http://maven.apache.org](http://maven.apache.org))

## Building the example

```Shell
$ cd $PROJECT_ROOT
$ mvn clean install
```

## Running the example

Open a terminal window and start the controller.

```Shell
$ cd $PROJECT_ROOT/activemq-rest-controller
$ mvn camel:run
```

Open a terminal window and start the sample consumer A.

```Shell
$ cd $PROJECT_ROOT/activemq-rest-consumer-a
$ mvn camel:run
```

Open a terminal window and start the sample consumer B.

```Shell
$ cd $PROJECT_ROOT/activemq-rest-consumer-b
$ mvn camel:run
```

All interaction with the controller (ie, managing subscriptions, publishing messages, ...) can be done using simple REST calls. So you can, for example, use something as simple as _curl_. Here are some example commands:

__Register subscriber:__
```Shell
$ curl -X POST -H 'Content-Type: application/json' -d '{"url":"http://localhost:8081"}' 'http://localhost:9090/subscriptions/A/TEST.FOO'
```

__Register subscriber with selector:__
```Shell
$ curl -X POST -H 'Content-Type: application/json' -d '{"selector":"foo = '"'"'bar'"'"'","url":"http://localhost:8081"}' 'http://localhost:9090/subscriptions/A/TEST.FOO'
```

__Delete subscriber:__
```Shell
$ curl -X DELETE -H 'Content-Type: application/json' 'http://localhost:9090/subscriptions/A/TEST.FOO'
```

__Get subscription info:__
```Shell
$ curl -X GET -H 'Content-Type: application/json' 'http://localhost:9090/subscriptions/A/TEST.FOO'
```

__Get subscription info (all):__
```Shell
$ curl -X GET -H 'Content-Type: application/json' 'http://localhost:9090/subscriptions/A'
```

__Publish a message:__
```Shell
$ curl -X POST -H 'Content-Type: application/json' -d '{"headers":{"foo": "bar"},"body":"Hello World!"}' 'http://localhost:8080/messages/TEST.FOO'
```

## Load testing

_Note: This is a JMS-like implementation over REST. The HTTP protocol was not meant for messaging (since it has to constantly open/close connections). So don't expect the performance you would get with a native JMS messaging solution (ie, using A-MQ directly via a protocol like OpenWire)._

Because this is a REST implementation, you can use any HTTP load testing tool that you'd like. If you don't have a favorite, there is a sample Gatling [[http://gatling.io/](http://gatling.io/)] test located in the `$PROJECT_ROOT/activemq-rest-jaxrs-api/src/test/gatling` folder.
