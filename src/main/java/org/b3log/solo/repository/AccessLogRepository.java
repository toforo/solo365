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
package org.b3log.solo.repository;

import org.b3log.latke.repository.*;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.solo.model.AccessLog;
import org.json.JSONObject;

/**
 * Access Log repository.
 * 
 * @author zhuangyilian
 */
@Repository
public class AccessLogRepository extends AbstractRepository {

    /**
     * Public constructor.
     */
    public AccessLogRepository() {
        super(AccessLog.ACCESS_LOG.toLowerCase());
    }

    @Override
    public void update(final String id, final JSONObject accessLog, final String... propertyNames) throws RepositoryException {
        super.update(id, accessLog, propertyNames);
    }

    /**
     * Gets a accesslog by the specified access address.
     *
     * @param accessAddress the specified access address
     * @return accesslog, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getByAccessAddress(final String accessAddress) throws RepositoryException {
        return getFirst(new Query().setFilter(new PropertyFilter(AccessLog.ACCESS_ADDRESS, FilterOperator.EQUAL, accessAddress)));
    }
}
