package fr.dynalexotron.dynaddon.enums;

public enum EScenarios {
    SAFEMINERS("dynaddon.scenarios.safeminers.name"),
    TIMBER("dynaddon.scenarios.timber.name"),
    TIMBERPVP("dynaddon.scenarios.timber_pvp.name");

    private final String key;

    EScenarios(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
