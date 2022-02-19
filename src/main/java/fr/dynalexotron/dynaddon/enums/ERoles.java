package fr.dynalexotron.dynaddon.enums;

public enum ERoles {
    BOUFFON("dynaddon.roles.bouffon.name");

    private final String key;

    ERoles(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
