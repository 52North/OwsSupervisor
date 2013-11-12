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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class can be used to execute {@link TimerTask} instances. It runs as a servlet and can be accessed by
 * other servlets for task scheduling and cancelling. The actual service method for GET and POST requests are
 * not implemented. It also provides methods to access the appropriate instances of
 * {@link ICatalogStatusHandler} and {@link ICatalogFactory} for tasks that run within this servlet.
 * 
 * @author Daniel Nüst (daniel.nuest@uni-muenster.de)
 * 
 */
public class TaskServlet extends GenericServlet {

    /**
     * Inner class to handle storage and cancelling of tasks at runtime.
     */
    private class TaskElement {
        protected Date date;
        protected long delay;
        protected String id;
        protected long period;
        protected TimerTask task;

        /**
         * 
         * @param identifier
         * @param task
         * @param delay
         * @param period
         */
        protected TaskElement(String identifier, TimerTask taskP, long delayP, long periodP) {
            this.id = identifier;
            this.task = taskP;
            this.delay = delayP;
            this.period = periodP;
            this.date = new Date(0l);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("TaskElement [");
            sb.append(this.task);
            sb.append(", delay=");
            sb.append(this.delay);
            sb.append(", period=");
            sb.append(this.period);
            sb.append(", date=");
            sb.append(this.date);
            sb.append("]");
            return sb.toString();
        }
    }

    private static final String CHECK_THREAD_COUNT = "CHECK_THREAD_COUNT";

    private static final String EMAIL_SENDER_TASK_ID = "EmailSenderTask";

    private static ScheduledThreadPoolExecutor executor;

    private static final String INIT_PARAM_CONFIG_FILE = "configFile";

    private static Logger log = LoggerFactory.getLogger(TaskServlet.class);

    public static final String NAME_IN_CONTEXT = "TimerServlet";

    private static final long serialVersionUID = -7342914044857243635L;

    private static final String EMAIL_SEND_PERIOD_MINDS = "supervisor.tasks.email.sendPeriodMins";

    private static final String EMAIL_ADMIN_EMAIL = "supervisor.admin.email";

    private static final String SEND_EMAILS = "supervisor.tasks.email.send";

    private Properties props;

    /**
     * List that holds all repeated task during run-time.
     */
    private ArrayList<TaskElement> tasks;

    public TaskServlet() {
        log.info("NEW {}", this);
    }

    public void cancel(String identifier) {
        for (TaskElement te : this.tasks) {
            if (te.id.equals(identifier)) {
                te.task.cancel();
                log.info("CANCELED " + te);
            }

        }
    }

    @Override
    public void destroy() {
        super.destroy();
        log.info("Destroy " + this.toString());
        executor.shutdownNow();
        executor = null;
        this.tasks.clear();
        this.tasks = null;
    }

    public ArrayList<TaskElement> getTasks() {
        return this.tasks;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        log.info(" * Initializing Timer ... ");

        this.tasks = new ArrayList<TaskServlet.TaskElement>();

        // get configFile as Inputstream
        String file = getInitParameter(INIT_PARAM_CONFIG_FILE);
        InputStream configStream = TaskServlet.class.getResourceAsStream(file);
        if (configStream == null) {
            log.error("Could not open the config file " + file);
            throw new UnavailableException("Could not open the config file.");
        }

        // load properties file
        try {
            this.props = loadProperties(configStream);
        }
        catch (IOException e) {
            log.error("Could not load properties file!", e);
            throw new UnavailableException("Could not load properties file!");
        }

        // init executor
        int threadCount = Integer.parseInt(this.props.getProperty(CHECK_THREAD_COUNT));
        executor = new ScheduledThreadPoolExecutor(threadCount);

        boolean sendEmails = Boolean.parseBoolean(props.getProperty(SEND_EMAILS));

        if (sendEmails) {
            // add task for email notifications, with out without admin email
            String adminEmail = this.props.getProperty(EMAIL_ADMIN_EMAIL);

            long emailSendInterval = Long.valueOf(this.props.getProperty(EMAIL_SEND_PERIOD_MINDS));
            if ( !adminEmail.contains("@ADMIN_EMAIL@")) {
                log.info("Found admin email address for send email task.");
                submit(EMAIL_SENDER_TASK_ID, new SendEmailTask(adminEmail), emailSendInterval, emailSendInterval);
            }
        }
        else
            log.debug("Not sending emails, not starting email tasks.");

        ServletContext context = getServletContext();
        context.setAttribute(NAME_IN_CONTEXT, this);

        log.info(" ***** Timer initiated successfully! ***** ");
    }

    private Properties loadProperties(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);

        return properties;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) {
        throw new UnsupportedOperationException("Not supperted by TimerServlet!");
    }

    public void submit(String identifier, TimerTask task, long delay) {
        executor.schedule(task, delay, TimeUnit.MILLISECONDS);
        if (log.isDebugEnabled()) {
            log.debug("Submitted: " + task + " with delay = " + delay);
        }
        this.tasks.add(new TaskElement(identifier, task, delay, 0l));
    }

    /**
     * 
     * " Finally, fixed-rate execution is appropriate for scheduling multiple repeating timer tasks that must
     * remain synchronized with respect to one another." See
     * {@link Timer#scheduleAtFixedRate(TimerTask, long, long)} for details.
     * 
     * @param task
     * @param delay
     * @param period
     */
    public void submit(String identifier, TimerTask task, long delay, long period) {
        executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
        if (log.isDebugEnabled()) {
            log.debug("Submitted: " + task + " with period = " + period + ", delay = " + delay);
        }
        this.tasks.add(new TaskElement(identifier, task, delay, period));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskServlet [");
        if (props != null) {
            builder.append("props=");
            builder.append(props);
            builder.append(", ");
        }
        if (tasks != null) {
            builder.append("tasks=");
            builder.append(tasks);
        }
        builder.append("]");
        return builder.toString();
    }

}
