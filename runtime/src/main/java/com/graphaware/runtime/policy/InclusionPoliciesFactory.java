/*
 * Copyright (c) 2015 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.runtime.policy;

import com.graphaware.common.policy.InclusionPolicies;
import com.graphaware.runtime.policy.all.IncludeAllBusinessNodeProperties;
import com.graphaware.runtime.policy.all.IncludeAllBusinessNodes;
import com.graphaware.runtime.policy.all.IncludeAllBusinessRelationshipProperties;
import com.graphaware.runtime.policy.all.IncludeAllBusinessRelationships;

/**
 * Factory for {@link com.graphaware.common.policy.InclusionPolicies}.
 */
public final class InclusionPoliciesFactory {

    private InclusionPoliciesFactory() {
    }

    /**
     * Produce {@link com.graphaware.common.policy.InclusionPolicies} that do not include internal nodes, relationships, and properties.
     *
     * @return a policy that includes all nodes, relationships, and properties, except framework internal ones.
     */
    public static InclusionPolicies allBusiness() {
        return new InclusionPolicies(
                IncludeAllBusinessNodes.getInstance(),
                IncludeAllBusinessNodeProperties.getInstance(),
                IncludeAllBusinessRelationships.getInstance(),
                IncludeAllBusinessRelationshipProperties.getInstance()
        );
    }
}
