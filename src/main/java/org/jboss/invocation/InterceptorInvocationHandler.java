/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.invocation;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A {@link Proxy} {@code InvocationHandler} which delegates invocations to an {@code Interceptor}.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class InterceptorInvocationHandler implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -7550306900997519378L;

    /**
     * The invocation dispatcher which should handle the invocation.
     *
     * @serial
     */
    private final Interceptor interceptor;

    /**
     * Construct a new instance.
     *
     * @param interceptor the interceptor to send invocations through
     */
    public InterceptorInvocationHandler(final Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * Handle a proxy method invocation.
     *
     * @param proxy the proxy instance
     * @param method the invoked method
     * @param args the method arguments
     * @return the result of the method call
     * @throws Throwable the exception to thrown from the method invocation on the proxy instance, if any
     */
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        InterceptorContext context = new InterceptorContext();
        context.setParameters(args);
        context.setMethod(method);
        return interceptor.processInvocation(context);
    }

    /** {@inheritDoc} */
    public String toString() {
        return "interceptor invocation handler";
    }
}
