/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.core.UserId;
import io.spine.server.aggregate.AggregateRepository;

/**
 * The repository for {@linkplain UserAggregate User aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserAggregateRepository extends AggregateRepository<UserId, UserAggregate> {
}
