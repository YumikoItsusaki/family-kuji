package kuji.controller.admin;

import static org.slim3.datastore.Datastore.query;

import java.util.List;

import kuji.model.Member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class ResultController extends Controller {

    @Override
    public Navigation run() throws Exception {
        List<Member> members = query(Member.class).asList();
        for (Member m : members) {
            if (m.hasRecipient()) {
                for (Member r : members) {
                    if (m.getRecipientId() == r.getId()) {
                        m.setRecipient(r);
                        break;
                    }
                }
            }
        }
        requestScope("members", members);
        return forward("result.jsp");
    }
}
