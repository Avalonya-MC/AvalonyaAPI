package eu.avalonya.api.models;

import org.bukkit.entity.Player;

public class Notification {

    private final String message;
    private final int delay;
    private final long createdAt = System.currentTimeMillis();

    public Notification(String message) {
        this(message, 0);
    }

    /**
     * Crée une notification avec un delay de reponse
     * @param message message à envoyer
     * @param delay delay de reponse en secondes
     */
    public Notification(String message, int delay) {
        this.message = message;
        this.delay = delay;
    }

    public String getMessage() {
        return message;
    }

    public int getDelay() {
        return delay;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > delay && delay > 0;
    }

    public void sendTo(Player player) {
        if (!isExpired())
            player.sendMessage(message);
    }

    public void sendTo(Iterable<Player> players) {
        for (Player player : players) {
            sendTo(player);
        }
    }

}
