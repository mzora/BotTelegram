public enum Comando {
    SALUTA("ciao"),MONOPATTINO("🛴"),SCOOTER("\uD83D\uDEF5"),MOTO("\uD83C\uDFCD"),TASTIERA("Tastiera"),ERRORE("");

    private String str;
    Comando(String s){
        this.str=s;
    }

    public static Comando fromString(String text) {
        if (text != null) {
            for (Comando c : Comando.values()) {
                if (text.equals(c.str)) {
                    return c;
                }
            }
        }
        return Comando.ERRORE;
    }
}
