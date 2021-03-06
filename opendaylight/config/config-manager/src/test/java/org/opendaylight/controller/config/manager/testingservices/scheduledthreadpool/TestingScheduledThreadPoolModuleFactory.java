/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.config.manager.testingservices.scheduledthreadpool;

import java.util.Arrays;
import java.util.List;

import org.opendaylight.controller.config.api.DependencyResolver;
import org.opendaylight.controller.config.api.DynamicMBeanWithInstance;
import org.opendaylight.controller.config.api.ModuleIdentifier;
import org.opendaylight.controller.config.api.annotations.AbstractServiceInterface;
import org.opendaylight.controller.config.manager.testingservices.seviceinterface
        .TestingScheduledThreadPoolServiceInterface;
import org.opendaylight.controller.config.manager.testingservices.seviceinterface.TestingThreadPoolServiceInterface;
import org.opendaylight.controller.config.spi.Module;
import org.opendaylight.controller.config.spi.ModuleFactory;

public class TestingScheduledThreadPoolModuleFactory implements ModuleFactory {
    public static final String NAME = "scheduled";

    private static List<Class<? extends TestingThreadPoolServiceInterface>> ifc = Arrays
            .asList(TestingScheduledThreadPoolServiceInterface.class, TestingThreadPoolServiceInterface.class);

    @Override
    public boolean isModuleImplementingServiceInterface(
            Class<? extends AbstractServiceInterface> serviceInterface) {
        return ifc.contains(serviceInterface);
    }

    @Override
    public String getImplementationName() {
        return NAME;
    }

    @Override
    public Module createModule(String instanceName,
            DependencyResolver dependencyResolver) {
        return new TestingScheduledThreadPoolModule(new ModuleIdentifier(NAME,
                instanceName), null, null);
    }

    @Override
    public Module createModule(String instanceName,
            DependencyResolver dependencyResolver, DynamicMBeanWithInstance old)
            throws Exception {
        TestingScheduledThreadPoolImpl oldInstance;
        try {
            oldInstance = (TestingScheduledThreadPoolImpl) old.getInstance();
        } catch (ClassCastException e) {// happens after OSGi update
            oldInstance = null;
        }

        TestingScheduledThreadPoolModule configBean = new TestingScheduledThreadPoolModule(
                new ModuleIdentifier(NAME, instanceName), old.getInstance(),
                oldInstance);
        // copy attributes
        configBean.setRecreate((Boolean) old.getAttribute("Recreate"));
        return configBean;
    }

}
