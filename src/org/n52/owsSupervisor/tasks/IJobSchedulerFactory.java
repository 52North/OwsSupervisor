/**********************************************************************************
 Copyright (C) 2010
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License serviceVersion 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 14.01.2010
 *********************************************************************************/

package org.n52.owsSupervisor.tasks;

/**
 * 
 * Factory class for encapsulation of {@link IJobScheduler} instances.
 * 
 * @author Daniel Nüst (daniel.neust@uni-muenster.de)
 * 
 */
public interface IJobSchedulerFactory {

    /**
     * 
     * @return An instance of an {@link IJobScheduler} to schedule new or cancel existing tasks.
     */
    public abstract IJobScheduler getJobScheduler();

}
