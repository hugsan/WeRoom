package com.itcom202.weroom.account.profiles;

public enum DataBasePath {

    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOM("room"),
    TENANT("tenant");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
