package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;

public class Message {

    private final SignalStrengthProvider signalStrengthProvider;

    public Message(SignalStrengthProvider signalStrengthProvider) {
        this.signalStrengthProvider = signalStrengthProvider;
    }

    public Component getMessage(MessageType messageType) {
        if (!signalStrengthProvider.getConfig().contains(messageType.getPath()))
            return Component.text(messageType.getPath());
        String toMineDown = signalStrengthProvider.getConfig().getString(messageType.getPath());
        if (toMineDown == null) return Component.text(messageType.getPath());
        return new MineDown(toMineDown).toComponent();
    }

    public Component getMessage(MessageType messageType, int signalStrength) {
        if (!signalStrengthProvider.getConfig().contains(messageType.getPath()))
            return Component.text(messageType.getPath());
        String toMineDown = signalStrengthProvider.getConfig().getString(messageType.getPath());
        if (toMineDown == null) return Component.text(messageType.getPath());
        String updated = toMineDown.replace("${strength}", String.valueOf(signalStrength));
        return new MineDown(updated).toComponent();
    }

    public Component getMessage(MessageType messageType, String permission) {
        if (!signalStrengthProvider.getConfig().contains(messageType.getPath()))
            return Component.text(messageType.getPath());
        String toMineDown = signalStrengthProvider.getConfig().getString(messageType.getPath());
        if (toMineDown == null) return Component.text(messageType.getPath());
        String updated = toMineDown.replace("${permission}", permission);
        return new MineDown(updated).toComponent();
    }
}
