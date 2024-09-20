package eu.avalonya.api.player;

import com.google.common.base.Converter;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.http.models.AbstractModel;
import eu.avalonya.api.models.Rank;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import eu.avalonya.api.utils.UuidHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player extends AbstractModel {
    private static final PlayerConverter CONVERTER = new PlayerConverter();

    private String uuid;
    private String pseudo;
    private int rankId;
    private long firstLogin;
    private String lastIp;
    private double money;

    public Rank getRank() {
        return Rank.values()[this.rankId];
    }

    public UUID getFormattedUuid() {
        return UUID.fromString(uuid);
    }

    public static Player create(org.bukkit.entity.Player player) {
        Map<String, Object> data = Map.of(
            "uuid", player.getUniqueId().toString(),
            "pseudo", player.getName(),
            "firstLogin", player.getFirstPlayed(),
            "lastIp", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress()
        );

        return Backend.post(Endpoint.PLAYER, AvalonyaAPI.getGson().toJson(data)).to(Player.class);
    }
    public static Player get(String uuid) {
        Endpoint endpoint = Endpoint.bind(Endpoint.PLAYERS, uuid);

        return Backend.get(endpoint, null).to(Player.class);
    }

    public static List<Player> getAll() {
        return Backend.get(Endpoint.PLAYERS, null).toList(Player.class);
    }

    public static PlayerConverter converter() { return CONVERTER; }

    public static class PlayerConverter extends Converter<eu.avalonya.api.protobuf.Player, Player> {

        private PlayerConverter() {}

        @Override
        protected Player doForward(eu.avalonya.api.protobuf.Player proto) {
            UUID id = UuidHelper.fromByteString(proto.getUuid());

            return new Player(id.toString(), proto.getName(), proto.getRankId(), proto.getFirstLogin(), proto.getLastIp(), proto.getMoney());
        }

        @Override
        protected eu.avalonya.api.protobuf.Player doBackward(Player player) {
            var builder = eu.avalonya.api.protobuf.Player.newBuilder()
                    .setUuid(UuidHelper.toByteString(player.getFormattedUuid()))
                    .setName(player.getPseudo())
                    .setRankId(player.getRankId())
                    .setFirstLogin(player.getFirstLogin())
                    .setLastIp(player.getLastIp())
                    .setMoney(player.getMoney());

            return builder.build();
        }
    }
}
