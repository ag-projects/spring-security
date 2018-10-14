package com.agharibi.springsecurity;

import java.util.HashSet;
import java.util.Set;

public final class LockedUsers {

    private static final Set<String> lockedUsersSet = new HashSet<>();

    public LockedUsers() {
    }

    public static final boolean isLocked(final String username) {
        return lockedUsersSet.contains(username);
    }

    public static final void lock(final String username) {
        lockedUsersSet.add(username);
    }
}
