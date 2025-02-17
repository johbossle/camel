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
package org.apache.camel.impl;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.FailedToCreateRouteException;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteNoOutputTest extends ContextTestSupport {

    @Override
    @BeforeEach
    public void setUp() {

        Exception e = assertThrows(Exception.class, super::setUp,
                "Should have thrown exception");

        FailedToCreateRouteException failed = assertIsInstanceOf(FailedToCreateRouteException.class, e);
        assertTrue(failed.getRouteId().matches("route[0-9]+"));
        assertIsInstanceOf(IllegalArgumentException.class, e.getCause());
        assertTrue(e.getCause().getMessage().matches(
                "Route route[0-9]+\\Q has no output processors. You need to add outputs to the route such as to(\"log:foo\").\\E"));
    }

    @Test
    public void testDummy() {
        // noop
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start");
            }
        };
    }
}
