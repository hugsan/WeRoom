package com.itcom202.weroom.account.profiles;

public enum DataBasePath {
    TENANT("tenant"),
    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOMS("rooms"),
    PROFILE_PICTURE("profile_picture"),
    ROOM_PICTURE("room_picture"),
    IMAGE("images");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
