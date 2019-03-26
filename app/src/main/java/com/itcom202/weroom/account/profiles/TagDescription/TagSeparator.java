package com.itcom202.weroom.account.profiles.TagDescription;


public enum TagSeparator {

    COMMA_SEPARATOR(","),
    PLUS_SEPARATOR("+"),
    MINUS_SEPARATOR("-"),
    SPACE_SEPARATOR(" "),
    AT_SEPARATOR("@"),
    HASH_SEPARATOR("#"),
    ENTER_SEPARATOR("\n");

    private final String name;

    TagSeparator(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }
}

