package tgw.groceryprices.utils;

public enum Unit {
    UN("un"),
    KG("kg"),
    G("g"),
    MG("mg"),
    L("L"),
    ML("mL"),
    M("m"),
    CM("cm"),
    MM("mm");

    public static final Unit[] VALUES = values();
    public static final String[] VALUE_NAMES = new String[VALUES.length];

    static {
        for (int i = 0; i < VALUES.length; i++) {
            VALUE_NAMES[i] = VALUES[i].name;
        }
    }

    public final String name;

    Unit(String name) {
        this.name = name;
    }

    public static Unit find(String name) {
        switch (name) {
            case "un": {
                return UN;
            }
            case "kg": {
                return KG;
            }
            case "g": {
                return G;
            }
            case "mg": {
                return MG;
            }
            case "L": {
                return L;
            }
            case "mL": {
                return ML;
            }
            case "m": {
                return M;
            }
            case "cm": {
                return CM;
            }
            case "mm": {
                return MM;
            }
        }
        throw new IncompatibleClassChangeError();
    }
}
