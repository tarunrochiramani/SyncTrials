import com.tr.ldap.LdapConfig;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class AppConfigurationTest {
    private static Logger logger = Logger.getLogger(AppConfigurationTest.class);

    @Autowired
    private LdapConfig ldapConfig;

    @Test
    public void canSetUpLdapConfig() {
        assertNotNull(ldapConfig);
        assertNotNull(ldapConfig.getHost());
        assertNotNull(ldapConfig.getPort());
        assertNotNull(ldapConfig.getBindDn());
        assertNotNull(ldapConfig.getPassword());
        assertNotNull(ldapConfig.getGroupDNs());
        assertFalse(ldapConfig.getGroupDNs().isEmpty());
        logger.warn(ldapConfig);
    }
}
