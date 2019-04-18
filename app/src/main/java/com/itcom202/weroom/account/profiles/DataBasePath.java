package com.itcom202.weroom.account.profiles;

public enum DataBasePath {
    TENANT("tenant"),
    USERS("users"),
    PROFILE("profile"),
    LANDLORD("landlord"),
    ROOM_ONE("mRoomOne"),
    ROOM_TWO("mRoomTwo"),
    ROOM_THREE("mRoomThree"),
    PROFILE_PICTURE("profile_picture"),
    ROOM_PICTURE("profile_picture"),
    IMAGE("images");

    private final String name;

    DataBasePath(String s) {
        name = s;
    }

    public String getValue() {
        return name;
    }

}
