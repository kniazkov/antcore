/*
 * Copyright (C) 2020 Ivan Kniazkov
 *
 * This file is part of Antcore.
 *
 * Antcore is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Antcore is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Antcore.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.kniazkov.antcore.lib;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The draft class for periodic tasks
 */
public abstract class Periodic
{
    /**
     * Method that will executed periodically
     * @return if false, the periodical execution will stopped
     */
    protected abstract boolean tick();

    /**
     * Starts the periodic execution
     * @param period the period, in milliseconds
     */
    public void start(long period) {
        stop();
        task = new PeriodicTask(this, period);
    }

    /**
     * Stops the periodic execution
     */
    public void stop() {
        if (task != null) {
            task.stop();
        }
    }

    /**
     * @return a count of cases of executions
     */
    public long getTicks() {
        return ticks;
    }

    /**
     * @return whether periodical execution is started
     */
    public boolean isStarted() {
        return task != null;
    }

    /**
     * @return period, in milliseconds
     */
    public long getPeriod() {
        return task != null ? task.period : 0;
    }

    private long ticks;
    private PeriodicTask task;

    private static class PeriodicTask extends TimerTask {

        private Timer timer;
        private Periodic owner;
        private long period;

        public PeriodicTask(Periodic owner, long period) {
            this.owner = owner;
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(this, 0, period);
            this.period = period;
        }

        public void stop() {
            timer.cancel();
            owner.task = null;
        }

        public void run() {
            owner.ticks++;
            if (!owner.tick()) {
                stop();
            }
        }
    }
}