package org.apereo.cas.authentication;

import org.apereo.cas.authentication.principal.PrincipalFactoryUtils;
import org.apereo.cas.clouddirectory.CloudDirectoryRepository;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.util.CollectionUtils;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This is {@link CloudDirectoryAuthenticationHandlerTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@SpringBootTest(classes = RefreshAutoConfiguration.class,
    properties = {
    "cas.authn.cloudDirectory.usernameAttributeName=username",
    "cas.authn.cloudDirectory.passwordAttributeName=password"
})
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CloudDirectoryAuthenticationHandlerTests {
    @Autowired
    private CasConfigurationProperties casProperties;

    @Test
    public void verifyAction() throws Exception {
        val repository = mock(CloudDirectoryRepository.class);
        when(repository.getUser(anyString())).thenReturn(CollectionUtils.wrap("username",
            List.of("casuser"), "password", List.of("Mellon")));
        val h = new CloudDirectoryAuthenticationHandler(StringUtils.EMPTY, mock(ServicesManager.class),
            PrincipalFactoryUtils.newPrincipalFactory(), repository, casProperties.getAuthn().getCloudDirectory());
        assertNotNull(h.authenticate(CoreAuthenticationTestUtils.getCredentialsWithDifferentUsernameAndPassword("casuser", "Mellon")));
    }
}
