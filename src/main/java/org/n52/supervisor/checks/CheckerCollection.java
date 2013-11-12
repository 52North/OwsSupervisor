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
package org.n52.supervisor.checks;

import java.util.ArrayList;
import java.util.Collection;

import org.n52.supervisor.ICheckResult;
import org.n52.supervisor.IServiceChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Nüst
 * 
 */
public class CheckerCollection implements IServiceChecker {

    private static Logger log = LoggerFactory.getLogger(CheckerCollection.class);

    private Collection<IServiceChecker> checkers = new ArrayList<IServiceChecker>();

    /**
     * 
     * @param checkers
     */
    public CheckerCollection(Collection<IServiceChecker> checkersP) {
        this.checkers = checkersP;
    }

    /*
     * (non-Javadoc)
     * @see org.n52.owsSupervisor.checks.IServiceChecker#addResult(org.n52.owsSupervisor.checks.ICheckResult)
     */
    @Override
    public void addResult(ICheckResult r) {
        throw new UnsupportedOperationException("Collection of checkers, which can contain different intervals!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.IServiceChecker#check()
     */
    @Override
    public boolean check() {
        log.debug("Checking collection of " + this.checkers.size());

        boolean b = true;
        int success = 0;
        int failure = 0;
        for (IServiceChecker c : this.checkers) {
            if ( !c.check()) {
                b = false;
                failure++;
            }
            else {
                success++;
            }
        }

        log.debug("Checked collection: " + success + " successful and " + failure + " failed checks.");

        return b;
    }

    /**
     * 
     * @return
     */
    public Collection<IServiceChecker> getCheckers() {
        return this.checkers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.IServiceChecker#getCheckIntervalMillis()
     */
    @Override
    public long getCheckIntervalMillis() {
        throw new UnsupportedOperationException("Collection of checkers, which can contain different intervals!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.IServiceChecker#getResults()
     */
    @Override
    public Collection<ICheckResult> getResults() {
        ArrayList<ICheckResult> results = new ArrayList<ICheckResult>();
        for (IServiceChecker c : this.checkers) {
            results.addAll(c.getResults());
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.n52.owsSupervisor.checks.IServiceChecker#getService()
     */
    @Override
    public String getService() {
        throw new UnsupportedOperationException("Collection of checkers, which can multiple services!");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.IServiceChecker#notifyFailure()
     */
    @Override
    public void notifyFailure() {
        for (IServiceChecker c : this.checkers) {
            c.notifyFailure();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.checks.IServiceChecker#notifySuccess()
     */
    @Override
    public void notifySuccess() {
        for (IServiceChecker c : this.checkers) {
            c.notifySuccess();
        }
    }

}