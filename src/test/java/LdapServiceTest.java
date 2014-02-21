import com.tr.ldap.LdapConfig;
import com.tr.ldap.LdapService;
import com.unboundid.ldap.sdk.LDAPConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class LdapServiceTest {

    @Autowired private LdapConfig ldapConfig;
    @Autowired private LdapService ldapService;

    @Test
    public void canCreateLdapConnection() {
        LDAPConnection ldapConnection = ldapService.createConnection(ldapConfig);
        assertNotNull(ldapConnection);
    }
}
