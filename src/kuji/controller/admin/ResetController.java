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

        List<Member> members = new ArrayList<Member>();
        members.add(new Member("s—Y", "y-itsusaki@docomo.ne.jp"));
        members.add(new Member("˜a¬", "k-istusaki6311@docomo.ne.jp"));
        members.add(new Member("—T”üq", "u-.-u_reia-love@docomo.ne.jp"));
        members.add(new Member("“O", "to--ru@softbank.ne.jp"));
        members.add(new Member("˜a‰À", "waka_0713@ezweb.ne.jp"));
        members.add(new Member("–«", "chise-fuya-koun-no-megami@docomo.ne.jp"));
        members.add(new Member("‰ÀŒb", "y39425715@docomo.ne.jp"));
        members.add(new Member("—TŠó", "bravery0917@docomo.ne.jp"));
        members.add(new Member("‹Iq", "con-tan0117@ezweb.ne.jp"));
        members.add(new Member("ˆÇØ", "oyr-sman.dksn-k8oo.y0n-j@docomo.ne.jp"));
        members.add(new Member("“Ö‹X", "y27de74fabu38n@k.vodafone.ne.jp"));
        Datastore.put(members);

        return forward("reset.jsp");
    }
}
