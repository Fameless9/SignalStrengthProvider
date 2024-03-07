package net.fameless.signalstrengthprovider;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.Component;

public class Message {

    private final SignalStrengthProvider signalStrengthProvider;

    public Message(SignalStrengthProvider signalStrengthProvider) {
        this.signalStrengthProvider = signalStrengthProvider;
    }

    public Component getMessage(MessageType messageType) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation()))
            return Component.text(messageType.getLocation());
        String toMineDown = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMineDown == null) return Component.text(messageType.getLocation());
        return new MineDown(toMineDown).toComponent();
    }

    public Component getMessage(MessageType messageType, int signalStrength) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation()))
            return Component.text(messageType.getLocation());
        String toMineDown = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMineDown == null) return Component.text(messageType.getLocation());
        String updated = toMineDown.replace("${strength}", String.valueOf(signalStrength));
        return new MineDown(updated).toComponent();
    }

    public Component getMessage(MessageType messageType, String permission) {
        if (!signalStrengthProvider.getLanguageFile().contains(messageType.getLocation()))
            return Component.text(messageType.getLocation());
        String toMineDown = signalStrengthProvider.getLanguageFile().getString(messageType.getLocation());
        if (toMineDown == null) return Component.text(messageType.getLocation());
        String updated = toMineDown.replace("${permission}", permission);
        return new MineDown(updated).toComponent();
    }
}
