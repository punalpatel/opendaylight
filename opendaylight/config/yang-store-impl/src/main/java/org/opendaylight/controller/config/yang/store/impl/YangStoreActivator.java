/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.config.yang.store.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.opendaylight.controller.config.yang.store.api.YangStoreService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YangStoreActivator implements BundleActivator {

    private ExtenderYangTracker extender;
    private ServiceRegistration<YangStoreService> registration;
    private static final Logger logger = LoggerFactory
            .getLogger(YangStoreActivator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        extender = new ExtenderYangTracker(context);
        extender.open();

        Dictionary<String, ?> properties = new Hashtable<>();
        registration = context.registerService(YangStoreService.class,
                extender, properties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        try {
            extender.close();
        } catch (Exception e) {
            logger.warn("Exception while closing extender", e);
        }

        if (registration != null)
            try {
                registration.unregister();
            } catch (Exception e) {
                logger.warn("Exception while unregistring yang store service",
                        e);
            }
    }
}
