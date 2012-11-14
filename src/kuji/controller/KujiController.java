package kuji.controller;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;
import static org.slim3.datastore.Datastore.createKey;
import static org.slim3.datastore.Datastore.getOrNull;

import java.util.List;
import java.util.Random;

import kuji.meta.MemberMeta;
import kuji.model.Member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

public class KujiController extends Controller {

    @Override
    public Navigation run() throws Exception {

        Validators v = new Validators(request);
        v.add("id", v.required());
        v.add("pass", v.required());

        if (!v.validate()) {
            return forward("index");
        }

        long id = asLong("id");
        String pass = asString("pass");

        TransactionOptions options = TransactionOptions.Builder.withXG(true);
        Transaction tx = getDatastoreService().beginTransaction(options);

        Member me = getOrNull(tx, Member.class, createKey(Member.class, id));
        if (me == null) {
            tx.rollback();
            requestScope("error", "指定されたメンバーが見つかりませんでした: " + id);
            return forward("index");
        }
        requestScope("me", me);

        if (me.hasRecipient()) {
            tx.rollback();
            if (pass.equals(me.getPass())) {
                Member recipient =
                    getOrNull(
                        null,
                        Member.class,
                        createKey(Member.class, me.getRecipientId()));
                requestScope("recipient", recipient);
                return forward("kuji.jsp");
            } else {
                requestScope("error", "パスワードが間違ってます");
                return forward("error.jsp");
            }
        }

        MemberMeta m = MemberMeta.get();
        List<Member> members =
            Datastore
                .query(m)
                .filter(m.available.equal(true), m.key.notEqual(me.getKey()))
                .asList();
        if (members.isEmpty()) {
            tx.rollback();
            requestScope("error", "もう全員プレゼントをくれる相手が決まってます");
            return forward("error.jsp");
        }

        int i = new Random().nextInt(members.size());
        Member recipient = members.get(i);
        recipient = Datastore.get(tx, Member.class, recipient.getKey());

        me.setPass(pass);
        me.setRecipientId(recipient.getId());
        recipient.setAvailable(false);
        Datastore.put(tx, me, recipient);
        tx.commit();

        requestScope("recipient", recipient);
        return forward("kuji.jsp");
    }
}
