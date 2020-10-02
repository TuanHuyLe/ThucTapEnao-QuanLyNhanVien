package com.enao.team2.quanlynhanvien.constants;

import java.util.regex.Pattern;

public class Constants {

    //regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);

    //role
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";

    //permission
    public static final String PERMISSION_LIST = "list";
    public static final String PERMISSION_ADD = "add";
    public static final String PERMISSION_UPDATE = "update";
    public static final String PERMISSION_DELETE = "delete";

    //group
    public static final String PERMISSION_GROUP = "group";
    public static final String PERMISSION_GROUP2 = "group2";
}
