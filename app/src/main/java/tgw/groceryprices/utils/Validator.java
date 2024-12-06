package tgw.groceryprices.utils;

public enum Validator {
    OK("", true),
    DUPLICATE_MARKET("Mercado já existe.", false),
    DUPLICATE_PRODUCT("Tipo de Produto já existe.", false),

    INVALID_NAME("Nome inválido.", false),
    INVALID_AMOUNT("Quantia inválida.", false),
    INVALID_PRICE("Preço inválido.", false),
    INVALID_QUANTITY("Quantidade inválida.", false),
    INVALID_MARKET("Mercado inválido.", false);


    public final String error;
    public final boolean valid;

    Validator(String error, boolean valid) {
        this.error = error;
        this.valid = valid;
    }
}
