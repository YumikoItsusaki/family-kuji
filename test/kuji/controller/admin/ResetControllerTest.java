package kuji.controller.admin;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ResetControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/admin/reset");
        ResetController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/admin/reset.jsp"));
    }
}
