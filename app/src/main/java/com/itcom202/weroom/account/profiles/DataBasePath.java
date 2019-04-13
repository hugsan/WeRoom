package com.itcom202.weroom.account.profiles;

public enum DataBasePath {
    TENANT("tenant"),
    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOM("room"),
    ROOM_ONE("room 1"),
    ROOM_TWO("room 2"),
    TENANT_THREE("tenant 3");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
