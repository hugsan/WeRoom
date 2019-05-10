package com.itcom202.weroom.framework;

public enum DataBasePath {
    TENANT("tenant"),
    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOMS("rooms"),
    PROFILE_PICTURE("profile_picture"),
    ROOM_PICTURE("room_picture"),
    IMAGE("images"),
    CHAT("chat");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
