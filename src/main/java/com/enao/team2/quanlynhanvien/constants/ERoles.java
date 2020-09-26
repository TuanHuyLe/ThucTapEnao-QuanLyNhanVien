package com.enao.team2.quanlynhanvien.constants;

public enum ERoles {
    GUEST, //can only modify posts created by him
    MANAGER, //can modify any posts, but no admin privileges to the Users management
    SUPER_ADMIN; //can do just about anything
}
