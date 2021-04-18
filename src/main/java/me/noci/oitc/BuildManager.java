package me.noci.oitc;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public class BuildManager {

    private static final Set<UUID> CAN_BUILD = Sets.newHashSet();

    public static boolean canBuild(UUID uuid) {
        return CAN_BUILD.contains(uuid);
    }

    public static boolean toggleBuild(UUID uuid) {
        if (CAN_BUILD.remove(uuid)) return false;
        return CAN_BUILD.add(uuid);
    }

}
