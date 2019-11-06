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

import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.solo.model.AccessLog;
import org.b3log.solo.repository.AccessLogRepository;
import org.json.JSONObject;


/**
 * Access log management service.
 * 
 * @author zhuangyilian
 */
@Service
public class AccessLogMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AccessLogMgmtService.class);

    /**
     * Access log repository.
     */
    @Inject
    private AccessLogRepository accessLogRepository;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Updates access log by the specified access address.
     *
     * @param accessAddress the specified access address.
     * @throws ServiceException service exception
     */
    public void updateAccessLogByAddress(final String accessAddress) throws ServiceException {
        final Transaction transaction = accessLogRepository.beginTransaction();

        try {
        	final long now = System.currentTimeMillis();
        	final JSONObject accessLog = accessLogRepository.getByAccessAddress(accessAddress);
        	if (null == accessLog) {
        		final JSONObject addAccessLog = new JSONObject();
        		addAccessLog.put(AccessLog.ACCESS_ADDRESS, accessAddress);
        		addAccessLog.put(AccessLog.ACCESS_COUNT, 1);
        		addAccessLog.put(AccessLog.ACCESS_LAST_TIME, now);
        		accessLogRepository.add(addAccessLog);
        	} else {
        		final String accessLogId = accessLog.optString(Keys.OBJECT_ID);
        		Integer accessCount = accessLog.optInt(AccessLog.ACCESS_COUNT);
        		accessLog.put(AccessLog.ACCESS_COUNT, accessCount + 1);
        		accessLog.put(AccessLog.ACCESS_LAST_TIME, now);
        		accessLogRepository.update(accessLogId, accessLog, AccessLog.ACCESS_COUNT);
        	}
        	transaction.commit();
        } catch (final RepositoryException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Updates a access log failed", e);
            throw new ServiceException(e);
        }
    }
}
