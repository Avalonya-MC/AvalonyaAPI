package eu.avalonya.api.player;

import com.google.protobuf.InvalidProtocolBufferException;
import eu.avalonya.api.AvalonyaAPI;
import eu.avalonya.api.Manager;
import eu.avalonya.api.http.Backend;
import eu.avalonya.api.http.Endpoint;
import eu.avalonya.api.utils.UuidHelper;

import java.util.UUID;

public class PlayerManager extends Manager {
    private final byte[] playerKey;
    public PlayerManager(AvalonyaAPI api, String playerKey) {
        super(api);

        this.playerKey = playerKey.getBytes();
    }

    public Player get(UUID uuid) {

        byte[] uuidBytes = UuidHelper.toByteArray(uuid);
        byte[] data = api().getRedisDatabase().execute(jedis -> {
            if (!jedis.hexists(playerKey, uuidBytes)) {
                return null;
            }

            return jedis.hget(playerKey, uuidBytes);
        });

        //If no cache data fetch from backend - need to improve sync to put in cache
        if (data == null) {
            Endpoint endpoint = Endpoint.bind(Endpoint.PLAYERS, uuid.toString());

            return Backend.get(endpoint, null).to(Player.class);
        }

        try {
            var protobufPlayer = eu.avalonya.api.protobuf.Player.parseFrom(data);
            return Player.converter().convert(protobufPlayer);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    //No need to save with http - cache handler should send to backend and save from there
    public void save(Player player) {

        byte[] uuidBytes = UuidHelper.toByteArray(player.getFormattedUuid());
        byte[] data = Player.converter().reverse().convert(player).toByteArray();

        api().getRedisDatabase().execute(jedis -> {
            jedis.hset(playerKey, uuidBytes, data);
        });
    }
}
