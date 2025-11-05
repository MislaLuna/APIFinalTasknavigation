package tasknavigation.demo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import tasknavigation.demo.domain.Usuario;

public class AuthenticationResponse {

    @JsonProperty("token")
    private final String token;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("usuario")
    private final Usuario usuario;

    public AuthenticationResponse(String token, String refreshToken, Usuario usuario) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
