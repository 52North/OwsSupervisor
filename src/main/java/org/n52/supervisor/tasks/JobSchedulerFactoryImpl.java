/**
 * ﻿Copyright (C) 2013 52°North Initiative for Geospatial Open Source Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.supervisor.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Factory for creating instances of {@link IJobScheduler} that work with the timer given in the constructor.
 * 
 * @author Daniel Nüst (daniel.nuest@uni-muenster.de)
 * 
 */
public class JobSchedulerFactoryImpl implements IJobSchedulerFactory {

    private static Logger log = LoggerFactory.getLogger(JobSchedulerFactoryImpl.class);

    private TaskServlet timerServlet;

    /**
     * 
     * @param timer
     */
    public JobSchedulerFactoryImpl(TaskServlet timer) {
        this.timerServlet = timer;
        log.info("NEW " + this);
    }

    @Override
    public IJobScheduler getJobScheduler() {
        return new JobSchedulerImpl(this.timerServlet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JobSchedulerFactory [timerServlet=");
        sb.append(this.timerServlet);
        sb.append("]");
        return sb.toString();
    }
}