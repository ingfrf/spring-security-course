package com.enmivida.app.security.service;

import com.enmivida.app.security.entity.PartnerEntity;
import com.enmivida.app.security.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerRegisteredClientService implements RegisteredClientRepository {

    private final PartnerRepository partnerRepository;

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<PartnerEntity> parterOpt = this.partnerRepository.findByClientId(clientId);
        return parterOpt.map(p -> {
            List<AuthorizationGrantType> authorizationGrantTypes = Arrays.stream(p.getGrantTypes().split(","))
                    .map(AuthorizationGrantType::new)
                    .collect(Collectors.toList());

            List<ClientAuthenticationMethod> authenticationMethods = Arrays.stream(p.getGrantTypes().split(","))
                    .map(ClientAuthenticationMethod::new)
                    .collect(Collectors.toList());

            List<String> scopes = Arrays.stream(p.getGrantTypes().split(",")).toList();

            return RegisteredClient
                    .withId(p.getId().toString())
                    .clientSecret(p.getClientSecret())
                    .clientName(p.getClientName())
                    .redirectUri(p.getRedirectUri())
                    .postLogoutRedirectUri(p.getRedirectUriLogout())
                    .clientAuthenticationMethod(authenticationMethods.get(0))
                    .clientAuthenticationMethod(authenticationMethods.get(1))
                    .scope(scopes.get(0))
                    .scope(scopes.get(1))
                    .authorizationGrantType(authorizationGrantTypes.get(0))
                    .authorizationGrantType(authorizationGrantTypes.get(1))
                    .tokenSettings(this.tokenSettings())
                    .build();
        }).orElseThrow(() -> new BadCredentialsException("Client doesn't exist"));
    }

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    private TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(8))
                .build();
    }
}
