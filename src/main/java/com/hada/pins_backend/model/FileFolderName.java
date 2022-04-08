package com.hada.pins_backend.model;

/*
 * Created by parksuho on 2022/04/05.
 */
public enum FileFolderName {
    profileImage,
    meetingImage,
    communityImage;

    public static String folderNameToString(FileFolderName name) {
        return name.toString();
    }
}
