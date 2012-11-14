package kuji.controller;

import java.util.List;

import kuji.model.Member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        List<Member> members = Datastore.query(Member.class).asList();
        requestScope("members", members);
        return forward("index.jsp");
    }
}
