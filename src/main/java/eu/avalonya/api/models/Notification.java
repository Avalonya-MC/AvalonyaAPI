package eu.avalonya.api.models;

import eu.avalonya.api.AvalonyaAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;

public class Notification {

    private final String message;
    private String expireMessage;
    private final int delay;
    private final Date createdAt = new Date();
    private BukkitTask task;

    /**
     * Crée une notification sans delay de reponse
     * @param message message à envoyer
     */
    public Notification(String message) {
        this(message, 0);
    }

    /**
     * Crée une notification avec un delay de reponse
     * @param message message à envoyer
     * @param delay delay de reponse en secondes
     */
    public Notification(String message, int delay) {
        this(message, null, delay);
    }

    /**
     * Crée une notification avec un message d'expiration
     * @param message message à envoyer
     * @param expireMessage message à envoyer si la notification est expirée
     * @param delay delay de reponse en secondes
     */
    public Notification(String message, String expireMessage, int delay) {
        this.message = message;
        this.expireMessage = expireMessage;
        this.delay = delay;
    }

    public String getMessage() {
        return message;
    }

    public String getExpireMessage() {
        return expireMessage;
    }

    public String setExpireMessage(String expireMessage) {
        return this.expireMessage = expireMessage;
    }

    public int getDelay() {
        return delay;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Vérifie si la notification est expirée
     * @return true si la notification est expirée
     */
    public boolean isExpired() {
        // Si le delay est supérieur à 0 et que le temps actuel moins le temps de création est supérieur au delay
        return delay > 0 && new Date().getTime() - createdAt.getTime() > delay * 1000L;
    }

    public void sendTo(Player player) {
        if (!isExpired()) {
            task = Bukkit.getScheduler().runTaskLater(AvalonyaAPI.getInstance(), () -> player.sendMessage(expireMessage), delay * 20L);
            player.sendMessage(message);
        }
    }

    public void sendTo(Iterable<Player> players) {
        for (Player player : players) {
            sendTo(player);
        }
    }

    public void destroy() {
        if (task != null) {
            task.cancel();
        }
    }

}
