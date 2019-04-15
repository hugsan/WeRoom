package com.itcom202.weroom.account.profiles;

public enum DataBasePath {
    TENANT("tenant"),
    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOM_ONE("mRoomOne"),
    ROOM_TWO("mRoomTwo"),
    ROOM_THREE("mRoomThree");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
