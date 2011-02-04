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
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.jboss.invocation.InvocationMessages.msg;

/**
 * An interceptor which passes invocations through a series of nested interceptors.
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ChainedInterceptor implements Interceptor, Serializable {

    private static final long serialVersionUID = 7951017996430287249L;

    private final List<Interceptor> interceptors;

    /**
     * Construct a new instance.
     *
     * @param interceptors the child interceptors
     */
    ChainedInterceptor(final Interceptor... interceptors) {
        if (interceptors == null) {
            throw msg.nullParameter("interceptors");
        }
        this.interceptors = Arrays.asList(interceptors);
    }

    /** {@inheritDoc} */
    public Object processInvocation(final InterceptorContext context) throws Exception {
        final ListIterator<Interceptor> old = context.getInterceptorIterator();
        context.setInterceptorIterator(new ConcatenatedIterator<Interceptor>(interceptors.listIterator(), old));
        try {
            return context.proceed();
        } finally {
            context.setInterceptorIterator(old);
        }
    }
}
