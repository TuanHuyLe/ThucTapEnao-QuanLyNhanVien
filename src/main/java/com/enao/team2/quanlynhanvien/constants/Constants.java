package com.enao.team2.quanlynhanvien.constants;

import java.util.regex.Pattern;

public class Constants {

    //regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_FULL_NAME_REGEX =
            Pattern.compile("[a-zA-Z0-9\\s]+", Pattern.CASE_INSENSITIVE);

    //role
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";

    //action
    public static final String ACTION_VIEW = "VIEW";
    public static final String ACTION_ADD = "ADD";
    public static final String ACTION_EDIT = "EDIT";
    public static final String ACTION_REMOVE = "REMOVE";

    //module
    public static final String MODULE_GROUP = "GROUP";
    public static final String MODULE_POSITION = "POSITION";
    public static final String MODULE_ROLE = "ROLE";
    public static final String MODULE_USER = "USER";
}
