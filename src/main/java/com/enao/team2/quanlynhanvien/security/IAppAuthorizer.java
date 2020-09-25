package com.enao.team2.quanlynhanvien.security;

import org.springframework.security.core.Authentication;

public interface IAppAuthorizer {
    boolean authorize(Authentication authentication, String permission);
}
