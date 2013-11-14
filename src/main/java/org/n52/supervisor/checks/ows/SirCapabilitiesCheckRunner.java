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

package org.n52.supervisor.checks.ows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Nüst
 * 
 */
public class SirCapabilitiesCheckRunner extends OwsCapabilitiesCheckRunner {

    private static Logger log = LoggerFactory.getLogger(SirCapabilitiesCheckRunner.class);

    public SirCapabilitiesCheckRunner(OwsCapabilitiesCheck check) {
        super(check);
    }

    @Override
    public boolean check() {
        log.debug("Checking SIR Capabilities for {}", this.c.getServiceUrl());

        clearResults();

        // create get capabilities document
        // GetCapabilitiesDocument getCapDoc =
        // GetCapabilitiesDocument.Factory.newInstance(XmlTools.DEFAULT_OPTIONS);
        // getCapDoc.addNewGetCapabilities();

        // send the document
        return runGetRequestParseDocCheck();
    }

}
