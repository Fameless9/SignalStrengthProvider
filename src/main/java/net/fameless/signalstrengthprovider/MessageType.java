package net.fameless.signalstrengthprovider;

public enum MessageType {

    RECEIVED("message.received"),
    NO_PERMISSION("message.no-permission"),
    NOT_A_PLAYER("message.not-a-player"),
    COMMAND_USAGE("message.command-usage"),
    NOT_A_NUMBER("message.not-a-number"),
    NUMBER_TOO_HIGH("message.number-too-high"),
    NOT_AVAILABLE("message.signal-strength-not-available"),
    INVALID_CONTAINER("message.invalid-container");

    private final String path;

    MessageType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
