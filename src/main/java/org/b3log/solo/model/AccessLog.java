/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.model;

/**
 * This class defines all access log model relevant keys.
 * 
 * @author zhuangyilian
 */
public final class AccessLog {

    /**
     * Access log.
     */
    public static final String ACCESS_LOG = "accessLog";

    /**
     * Access logs.
     */
    public static final String ACCESS_LOGS = "accessLogs";

    /**
     * Key of address.
     */
    public static final String ACCESS_ADDRESS = "accessAddress";

    /**
     * Key of count.
     */
    public static final String ACCESS_COUNT = "accessCount";
    
    /**
     * Key of last time.
     */
    public static final String ACCESS_LAST_TIME = "accessLastTime";

    /**
     * Private constructor.
     */
    private AccessLog() {
    }
}
