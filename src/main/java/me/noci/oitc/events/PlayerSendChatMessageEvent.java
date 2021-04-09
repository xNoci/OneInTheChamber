package me.noci.oitc.events;

import lombok.Getter;
import lombok.Setter;
import me.noci.noclib.events.core.CorePlayerCancellableEvent;
import org.bukkit.entity.Player;

public class PlayerSendChatMessageEvent extends CorePlayerCancellableEvent {

    @Getter @Setter private String message;

    public PlayerSendChatMessageEvent(Player player, String message, String format, boolean cancelled) {
        super(player, cancelled);
        this.message = message;
    }

}
