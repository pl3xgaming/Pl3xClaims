package net.pl3x.bukkit.claims.claim;

import net.pl3x.bukkit.claims.claim.flag.FlagType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Claim {
    private final long id;
    private final UUID owner;
    private final Claim parent;
    private final Coordinates coordinates;
    private final boolean isAdminClaim;
    private final Map<FlagType, Boolean> flags = new HashMap<>();
    private final Map<UUID, TrustType> trusts = new HashMap<>();
    private final Collection<UUID> managers = new HashSet<>();
    private final Collection<Claim> children = new HashSet<>();

    public Claim(long id, UUID owner, Claim parent, Coordinates coordinates, boolean isAdminClaim) {
        this.id = id;
        this.owner = owner;
        this.parent = parent;
        this.coordinates = coordinates;
        this.isAdminClaim = isAdminClaim;
    }

    public long getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwner(Player player) {
        return isOwner(player.getUniqueId());
    }

    public boolean isOwner(UUID uuid) {
        return Objects.equals(owner, uuid);
    }

    public Claim getParent() {
        return parent;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean isAdminClaim() {
        return isAdminClaim;
    }

    public Boolean getFlag(FlagType flag) {
        return flags.get(flag);
    }

    public void setFlag(FlagType flag, Boolean value) {
        if (value == null) {
            flags.remove(flag);
            return;
        }
        flags.put(flag, value);
    }

    public Map<UUID, TrustType> getTrusts() {
        return trusts;
    }

    public TrustType getTrust(Player player) {
        return getTrust(player.getUniqueId());
    }

    public void setTrust(Player player, TrustType trustType) {
        setTrust(player.getUniqueId(), trustType);
    }

    public TrustType getTrust(UUID uuid) {
        return trusts.get(uuid);
    }

    public void setTrust(UUID uuid, TrustType trustType) {
        if (trustType == null) {
            trusts.remove(uuid);
            return;
        }
        trusts.put(uuid, trustType);
    }

    public Collection<UUID> getManagers() {
        return managers;
    }

    public void addManager(UUID uuid) {
        managers.add(uuid);
    }

    public void removeManager(UUID uuid) {
        managers.remove(uuid);
    }

    public Collection<Claim> getChildren() {
        return children;
    }

    public void addChild(Claim claim) {
        children.add(claim);
    }

    public void removeChild(Claim claim) {
        children.remove(claim);
    }

    public boolean allowAccess(UUID uuid) {
        if (owner == uuid) {
            return true;
        }
        switch (trusts.get(uuid)) {
            case ACCESS:
            case CONTAINER:
            case BUILDER:
                return true;
            default:
                return false;
        }
    }

    public boolean allowContainers(UUID uuid) {
        if (owner == uuid) {
            return true;
        }
        switch (trusts.get(uuid)) {
            case CONTAINER:
            case BUILDER:
                return true;
            default:
                return false;
        }
    }

    public boolean allowBuild(UUID uuid) {
        if (owner == uuid) {
            return true;
        }
        switch (trusts.get(uuid)) {
            case BUILDER:
                return true;
            default:
                return false;
        }
    }

    public boolean allowEdit(Player player) {
        if (player == null) {
            return false;
        }

        if (isAdminClaim) {
            if (player.hasPermission("claims.adminclaims")) {
                return true;
            }
        } else {
            if (player.hasPermission("claims.deleteclaims")) {
                return true;
            }
        }

        return parent != null ? parent.isOwner(player) : isOwner(player);
    }

    public boolean allowManage(UUID uuid) {
        return owner == uuid || managers.contains(uuid);
    }
}
