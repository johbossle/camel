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

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.support.service.ServiceSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that data format receives callback if its a service / CamelContextAware
 */
public class DataFormatServiceTest extends ContextTestSupport {

    protected final MyDataFormat my = new MyDataFormat();

    @Test
    public void testMarshal() throws Exception {
        assertTrue(my.isStarted());
        assertSame(context, my.getCamelContext());

        getMockEndpoint("mock:a").expectedBodiesReceived("Hi Camel");

        template.sendBody("direct:a", "Hello World");

        assertMockEndpointsSatisfied();

        context.stop();

        assertTrue(my.isStopped());
        assertSame(context, my.getCamelContext());
    }

    @Test
    public void testUnmarshal() throws Exception {
        assertTrue(my.isStarted());
        assertSame(context, my.getCamelContext());

        getMockEndpoint("mock:b").expectedBodiesReceived("Bye World");

        template.sendBody("direct:b", "Hello World");

        assertMockEndpointsSatisfied();

        context.stop();

        assertTrue(my.isStopped());
        assertSame(context, my.getCamelContext());
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // START SNIPPET: e1
                // marshal using our custom data format. (my is an instance of
                // MyDataFormat)
                from("direct:a").marshal(my).to("mock:a");

                // unmarshal using our custom data format.
                from("direct:b").unmarshal(my).to("mock:b");
                // END SNIPPET: e1
            }
        };
    }

    // START SNIPPET: e2
    public static class MyDataFormat extends ServiceSupport implements DataFormat, CamelContextAware {

        private CamelContext camelContext;

        @Override
        public CamelContext getCamelContext() {
            return camelContext;
        }

        @Override
        public void setCamelContext(CamelContext camelContext) {
            this.camelContext = camelContext;
        }

        @Override
        public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
            stream.write("Hi Camel".getBytes());
        }

        @Override
        public Object unmarshal(Exchange exchange, InputStream stream) {
            return "Bye World";
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
    // END SNIPPET: e2
}
