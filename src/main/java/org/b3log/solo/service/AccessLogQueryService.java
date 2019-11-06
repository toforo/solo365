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
package org.b3log.solo.service;

import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.solo.repository.AccessLogRepository;
import org.json.JSONObject;

/**
 * Access log query service.
 * 
 * @author zhuangyilian
 */
@Service
public class AccessLogQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AccessLogQueryService.class);

    /**
     * Access log repository.
     */
    @Inject
    private AccessLogRepository accessLogRepository;

    /**
     * Gets a access log by the specified access address.
     *
     * @param accessAddress the specified access address
     * @return accesslog, returns {@code null} if not found
     */
    public JSONObject getAccessLogByAddress(final String accessAddress) {
        try {
            return accessLogRepository.getByAccessAddress(accessAddress);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets a access log by access address [" + accessAddress + "] failed", e);

            return null;
        }
    }
}
