package tasknavigation.demo.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tasknavigation.demo.domain.Usuario;
import tasknavigation.demo.jwt.JwtService;
import tasknavigation.demo.repository.UsuarioRepository;
import tasknavigation.demo.token.Token;
import tasknavigation.demo.token.TokenRepository;
import tasknavigation.demo.token.TokenType;

import java.io.IOException;

@Service
public class AuthenticationService {
	private final UsuarioRepository repository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;  // FAZER O BEAN ANTES (ApplicationConfig)
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager; // FAZER O BEAN ANTES (ApplicationConfig)

	public AuthenticationService(UsuarioRepository repository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.tokenRepository = tokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public Usuario register(RegisterRequest request) {

			var usuarioDb = repository.findByEmail(request.getEmail());
			if (usuarioDb.isPresent()) {
				throw new RuntimeException("Já existe este email cadastrado em nossa base de dados");
			}
             var usuario = new Usuario();
			usuario.setCodStatus(true);
			usuario.setNome(request.getNome());
			usuario.setEmail(request.getEmail());
			usuario.setSenha(passwordEncoder.encode(request.getPassword()));
			usuario.setNivelAcesso(request.getNivelAcesso());
			var savedUser = repository.save(usuario);
			var jwtToken = jwtService.generateToken(savedUser);
			var refreshToken = jwtService.generateRefreshToken(savedUser);

			saveUserToken(savedUser, jwtToken);
			AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, refreshToken);
             savedUser.setAuthenticationResponse(authenticationResponse);
			//return new AuthenticationResponse(jwtToken, refreshToken);
		    return savedUser;
	}

	public Usuario authenticate(AuthenticationRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getPassword()
					)
			);
		} catch (Exception e) {
			throw new RuntimeException("Email ou Password Incorreto");
		}
		var user = repository.findByEmail(request.getEmail()).get();
		if(!user.isCodStatus()){
			throw new RuntimeException("Conta inativa, por favor procurar o administrador da conta");
		}
				//.orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, refreshToken);
		user.setAuthenticationResponse(authenticationResponse);
		return user;
	}

	private void saveUserToken(Usuario usuario, String jwtToken) {

		var token = new Token();
		token.setUsuario(usuario);
		token.setToken(jwtToken);
		token.setTokenType(TokenType.BEARER);
		token.setExpired(false);
		token.setRevoked(false);
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(Usuario usuario) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(usuario.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail)
					.orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = new AuthenticationResponse(accessToken, refreshToken);
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
}

