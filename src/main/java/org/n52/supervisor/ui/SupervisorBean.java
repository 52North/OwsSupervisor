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

package org.n52.supervisor.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.supervisor.ICheckResult;
import org.n52.supervisor.Supervisor;
import org.n52.supervisor.SupervisorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * @author Daniel Nüst
 * 
 */
public class SupervisorBean {

    private static Logger log = LoggerFactory.getLogger(SupervisorBean.class);

    private SupervisorProperties props;

    @Inject
    public SupervisorBean(SupervisorProperties props) {
        this.props = props;
        log.info("NEW " + this.toString());
    }

    public void clearCheckResults() {
        Supervisor.clearResults();
    }

    public String getAdminEmail() {
        return props.getAdminEmail();
    }

    public Collection<ICheckResult> getCheckResults() {
        return Supervisor.getLatestResults();
    }

    public Collection<ICheckResult> getCheckResultsReversed() {
        List<ICheckResult> reversed = new ArrayList<>(Supervisor.getLatestResults());
        Collections.reverse(reversed);
        return reversed;
    }

}
