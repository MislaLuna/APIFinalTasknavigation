package tasknavigation.demo.domain.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tasknavigation.demo.domain.enums.Permission.*;


public enum NivelAcesso {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    ),
    USUARIO(
            Set.of(
                    USUARIO_READ,
                    USUARIO_UPDATE,
                    USUARIO_DELETE,
                    USUARIO_CREATE
            )

    );

    private final Set<Permission> permissions;

    NivelAcesso(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
