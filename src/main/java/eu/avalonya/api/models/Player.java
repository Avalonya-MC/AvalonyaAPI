package eu.avalonya.api.models;

import java.util.Map;

import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
@NoArgsConstructor
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
        return Pair.of("player_id", this.uuid);
    }

    @Override
    public Map<String, String> getRepositoryAttributes() {
        return Map.of();
    }

    public static Player deserialize(Map<String, Object> data) {
        final Player player = new Player();

        player.setUuid((String) data.get("uuid"));
        player.setPseudo((String) data.get("pseudo"));
        player.setRankId((int) data.get("rank"));
        player.setFirstLogin((long) data.get("first_login"));
        player.setLastIp((String) data.get("last_ip"));
        player.setMoney((double) data.get("money"));

        return player;
    }

}
