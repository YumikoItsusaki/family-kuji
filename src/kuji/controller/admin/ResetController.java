package kuji.controller.admin;

import java.util.ArrayList;
import java.util.List;

import kuji.model.Member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class ResetController extends Controller {

    @Override
    public Navigation run() throws Exception {

        Datastore.delete(Datastore.query(Member.class).asKeyList());

        String[] names =
            { "s—Y", "˜a¬", "—T”üq", "“O", "˜a‰À", "–«", "‰ÀŒb", "—TŠó", "‹Iq", "ˆÇØ", "“Ö‹X" };
        List<Member> members = new ArrayList<Member>();
        for (String name : names) {
            Member m = new Member();
            m.setName(name);
            m.setAvailable(true);
            members.add(m);
        }
        Datastore.put(members);

        return forward("reset.jsp");
    }
}
