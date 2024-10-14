package eu.avalonya.api.models;

import java.util.Map;

import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
public class Player extends AbstractModel {

    private String uuid;
    private String pseudo;
    private int rankId;
    private long firstLogin;
    private String lastIp;
    private double money;

    public Rank getRank() {
        return Rank.values()[this.rankId];
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public Pair<String, String> getId() {
        return Pair.of("id", this.uuid);
    }

    @Override
    public Map<String, String> getRepositoryAttributes() {
        return Map.of();
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "uuid", this.uuid,
                "pseudo", this.pseudo,
                "rankId", this.rankId,
                "firstLogin", this.firstLogin,
                "lastIp", this.lastIp,
                "money", this.money
        );
    }
}
