/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.IdAware;
import org.apache.camel.spi.RouteIdAware;
import org.apache.camel.support.service.ServiceSupport;
import org.junit.jupiter.api.Test;

public class RouteAwareProcessorTest extends ContextTestSupport {

    private final MyProcessor processor = new MyProcessor();

    @Test
    public void testRouteIdAware() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("Hello route foo from processor myProcessor");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").routeId("foo")
                        .process(processor).id("myProcessor")
                        .to("mock:result");
            }
        };
    }

    private static class MyProcessor extends ServiceSupport implements Processor, RouteIdAware, IdAware {

        private String id;
        private String routeId;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getRouteId() {
            return routeId;
        }

        @Override
        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        @Override
        public void process(Exchange exchange) {
            exchange.getMessage().setBody("Hello route " + routeId + " from processor " + id);
        }

        @Override
        protected void doStart() {
            // noop
        }

        @Override
        protected void doStop() {
            // noop
        }
    }

}
