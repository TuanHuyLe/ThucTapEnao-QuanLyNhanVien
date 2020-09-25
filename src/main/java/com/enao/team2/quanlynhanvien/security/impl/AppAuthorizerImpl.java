package com.enao.team2.quanlynhanvien.security.impl;

import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.security.IAppAuthorizer;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("appAuthorizer")
public class AppAuthorizerImpl implements IAppAuthorizer {

    private final Logger logger = LoggerFactory.getLogger(AppAuthorizerImpl.class);

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    @Override
    public boolean authorize(Authentication authentication, String permission) {
        try {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authentication;
            if (user == null) {
                throw new UnauthorizedException(new MessageResponse("User not exists"));
            }
            UserDetailsImpl userDetails = (UserDetailsImpl) user.getPrincipal();
            if (userDetails == null || userDetails.getAuthorities().size() == 0) {
                throw new UnauthorizedException(new MessageResponse("User not exists authorities"));
            }
            Optional<PermissionEntity> permissionEntity = permissionService.findByName(permission);
            if (!permissionEntity.isPresent()) {
                throw new UnauthorizedException(new MessageResponse("Permissions not exists"));
            }
            //Nếu có quyền thì cho qua
            List<String> rolesName = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            Set<RoleEntity> roleEntities = new HashSet<>();
            Set<PermissionEntity> permissionEntities = new HashSet<>();
            for (String roleName : rolesName) {
                Optional<RoleEntity> role = roleService.findByName(roleName);
                role.ifPresent(roleEntities::add);
            }
            for (RoleEntity role : roleEntities) {
                permissionEntities.addAll(role.getPermissions());
            }
            if (!permissionEntities.contains(permissionEntity.get())){
                throw new UnauthorizedException(new MessageResponse("Forbidden! Not permission"));
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
