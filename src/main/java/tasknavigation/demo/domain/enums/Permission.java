package tasknavigation.demo.domain.enums;

public enum Permission {

    ADMIN_READ("ADMIN_READ"),
    ADMIN_UPDATE("ADMIN_UPDATE"),
    ADMIN_CREATE("ADMIN_CREATE"),
    ADMIN_DELETE("ADMIN_DELETE"),
    USUARIO_READ("USUARIO_READ"),
    USUARIO_UPDATE("USUARIO_UPDATE"),
    USUARIO_CREATE("USUARIO_CREATE"),
    USUARIO_DELETE("USUARIO_DELETE")
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
    public String getPermission() {return permission;}
}
