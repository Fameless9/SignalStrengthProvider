package net.fameless.signalstrengthprovider;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Message {

    private final SignalStrengthProvider signalStrengthProvider;

    public Message(SignalStrengthProvider signalStrengthProvider) {
        this.signalStrengthProvider = signalStrengthProvider;
    }

    public Component getMessage(MessageType messageType) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation())) return Component.text(messageType.getLocation());
        String toMiniMessage = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMiniMessage == null) return Component.text(messageType.getLocation());
        return MiniMessage.miniMessage().deserialize(toMiniMessage);
    }

    public Component getMessage(MessageType messageType, int signalStrength) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation())) return Component.text(messageType.getLocation());
        String toMiniMessage = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMiniMessage == null) return Component.text(messageType.getLocation());
        String updated = toMiniMessage.replace("${strength}", String.valueOf(signalStrength));
        return MiniMessage.miniMessage().deserialize(updated);
    }

    public Component getMessage(MessageType messageType, String permission) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation())) return Component.text(messageType.getLocation());
        String toMiniMessage = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMiniMessage == null) return Component.text(messageType.getLocation());
        String updated = toMiniMessage.replace("${permission}", permission);
        return MiniMessage.miniMessage().deserialize(updated);
    }

}