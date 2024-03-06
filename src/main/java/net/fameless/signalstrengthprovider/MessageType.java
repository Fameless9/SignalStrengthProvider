package net.fameless.signalstrengthprovider;

public enum MessageType {

    RECEIVED("message.received"),
    NO_PERMISSION("message.no-permission"),
    NOT_A_PLAYER("message.not-a-player"),
    COMMAND_USAGE("message.command-usage"),
    NOT_A_NUMBER("message.not-a-number"),
    NOT_IN_RANGE("message.not-in-range");

    private final String location;

    MessageType(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}