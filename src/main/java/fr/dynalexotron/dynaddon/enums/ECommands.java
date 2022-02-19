package fr.dynalexotron.dynaddon.enums;

public enum ECommands {
    TPLOIN("dynaddon.commands.tploin.name"),
    NEAR("dynaddon.commands.near.name");

    private final String key;

    ECommands(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
