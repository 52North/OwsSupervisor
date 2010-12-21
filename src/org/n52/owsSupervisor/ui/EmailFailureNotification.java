/*******************************************************************************
Copyright (C) 2010
by 52 North Initiative for Geospatial Open Source Software GmbH

Contact: Andreas Wytzisk
52 North Initiative for Geospatial Open Source Software GmbH
Martin-Luther-King-Weg 24
48155 Muenster, Germany
info@52north.org

This program is free software; you can redistribute and/or modify it under 
the terms of the GNU General Public License serviceVersion 2 as published by the 
Free Software Foundation.

This program is distributed WITHOUT ANY WARRANTY; even without the implied
WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program (see gnu-gpl v2.txt). If not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
visit the Free Software Foundation web page, http://www.fsf.org.

Author: Daniel Nüst
 
 ******************************************************************************/
package org.n52.owsSupervisor.ui;

import java.util.Collection;

import org.n52.owsSupervisor.checks.ICheckResult;

/**
 * @author Daniel Nüst
 * 
 */
public class EmailFailureNotification implements IFailureNotification {

	private Collection<ICheckResult> checkResults;

	private String recipientEmail;

	private String serviceUrl;

	/**
	 * 
	 * @param checkResults
	 * @param recipientEmail
	 * @param serviceUrl
	 */
	public EmailFailureNotification(String serviceUrlP,
			String recipientEmailP, Collection<ICheckResult> failuresP) {
		this.checkResults = failuresP;
		this.recipientEmail = recipientEmailP;
		this.serviceUrl = serviceUrlP;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Collection<ICheckResult> getCheckResults() {
		return this.checkResults;
	}

	/**
	 * 
	 * @return
	 */
	public String getRecipientEmail() {
		return this.recipientEmail;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getServiceUrl() {
		return this.serviceUrl;
	}

}
