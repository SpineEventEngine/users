/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePartRepository;

/**
 * The repository for {@linkplain UserPart User} aggregate part.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserPartRepository extends AggregatePartRepository<UserId, UserPart, UserRoot> {
}
