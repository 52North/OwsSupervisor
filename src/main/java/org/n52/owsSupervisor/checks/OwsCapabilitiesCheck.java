/**
 * ﻿Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.owsSupervisor.checks;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import net.opengis.ows.x11.CapabilitiesBaseType;
import net.opengis.ows.x11.GetCapabilitiesDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.owsSupervisor.ICheckResult.ResultType;
import org.n52.owsSupervisor.util.XmlTools;

/**
 * @author Daniel Nüst
 * 
 */
public class OwsCapabilitiesCheck extends AbstractServiceCheck {

    protected static final String DEFAULT_OWS_VERSION = "1.1";

    private static Logger log = LoggerFactory.getLogger(OwsCapabilitiesCheck.class);

    protected static final String NEGATIVE_TEXT = "Request for capabilities document FAILED.";

    protected static final String POSITIVE_TEXT = "Successfully requested capabilities document.";

    protected String serviceVersion;

    /**
     * 
     * @param owsVersion
     * @param serviceURL
     * @param notifyEmail
     * @param checkIntervalMillis
     */
    public OwsCapabilitiesCheck(String owsVersion, URL serviceURL, String notifyEmail, long checkIntervalMillis) {
        super(notifyEmail, serviceURL, checkIntervalMillis);
        this.serviceVersion = owsVersion;
    }

    /**
     * 
     * @param serviceURL
     * @param notifyEmail
     */
    public OwsCapabilitiesCheck(URL serviceURL, String notifyEmail) {
        super(notifyEmail, serviceURL);
        this.serviceVersion = DEFAULT_OWS_VERSION;
    }

    /**
     * 
     * @param service
     * @param notifyEmail
     * @param checkIntervalMillis
     */
    public OwsCapabilitiesCheck(URL service, String notifyEmail, long checkIntervalMillis) {
        this(DEFAULT_OWS_VERSION, service, notifyEmail, checkIntervalMillis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.owsSupervisor.IServiceChecker#check()
     */
    @Override
    public boolean check() {
        URL sUrl = getServiceURL();
        if (log.isDebugEnabled()) {
            log.debug("Checking Capabilities for " + sUrl);
        }

        if (this.serviceVersion != "1.1") {
            log.error("OWS Version not supported: " + this.serviceVersion);
            addResult(new ServiceCheckResult(new Date(), sUrl.toString(), NEGATIVE_TEXT
                    + " ... OWS Version not supported: " + this.serviceVersion, ResultType.NEGATIVE));
            return false;
        }

        clearResults();

        // create get capabilities document
        GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance(XmlTools.DEFAULT_OPTIONS);
        getCapDoc.addNewGetCapabilities();

        // send the document
        try {
            XmlObject response = this.client.xSendPostRequest(sUrl.toString(), getCapDoc);
            getCapDoc = null;

            // parse response - this is the test!
            CapabilitiesBaseType caps = CapabilitiesBaseType.Factory.parse(response.getDomNode());
            log.debug("Parsed caps with serviceVersion " + caps.getVersion());
        }
        catch (IOException e) {
            log.error("Could not send request", e);
            addResult(new ServiceCheckResult(new Date(), sUrl.toString(), NEGATIVE_TEXT
                    + " ... Could not send request!", ResultType.NEGATIVE));
            return false;
        }
        catch (XmlException e) {
            log.error("Could not send request", e);
            addResult(new ServiceCheckResult(new Date(), sUrl.toString(), NEGATIVE_TEXT
                    + " ... Could not parse response to CapabilitiesBaseType!", ResultType.NEGATIVE));
            return false;
        }

        // save the good result
        addResult(new ServiceCheckResult(new Date(), sUrl.toString(), POSITIVE_TEXT, ResultType.POSITIVE));

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OwsCapabilitiesCheck [" + getService() + ", check interval=" + getCheckIntervalMillis() + "]";
    }

}