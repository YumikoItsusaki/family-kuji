package kuji.controller;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;
import static kuji.Config.ADMIN_EMAIL;
import static kuji.Config.GAE_ADMIN_EMAIL;
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
import org.slim3.util.StringUtil;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;

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
        me.setRecipient(recipient);
        recipient.setAvailable(false);
        Datastore.put(tx, me, recipient);
        tx.commit();

        if (!StringUtil.isEmpty(me.getEmail())) {
            Message msg =
                new Message(
                    GAE_ADMIN_EMAIL,
                    me.getEmail(),
                    "いつさきファミリーくじ",
                    createNotifyMailMessage(me));
            MailServiceFactory.getMailService().send(msg);
        }

        Message msg =
            new Message(
                GAE_ADMIN_EMAIL,
                ADMIN_EMAIL,
                "いつさきファミリーくじ",
                me.getName() + "さんがくじをひきました。");
        MailServiceFactory.getMailService().send(msg);

        return forward("kuji.jsp");
    }

    private String createNotifyMailMessage(Member m) {
        StringBuilder buf = new StringBuilder();
        buf.append(m.getName() + "さま\n");
        buf.append("\n");
        buf.append("あなたがプレゼントを贈るのは　");
        buf.append(m.getRecipient().getName() + "さん　");
        buf.append("です。\n");
        buf.append("\n");
        buf.append("楽しいクリスマス会にしましょう！\n");
        return buf.toString();
    }
}
