package kuji.controller.admin;

import static org.slim3.datastore.Datastore.query;

import java.util.ArrayList;
import java.util.List;

import kuji.Config;
import kuji.model.Member;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.StringUtil;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;

public class MailController extends Controller {

    @Override
    public Navigation run() throws Exception {

        List<String> emails = new ArrayList<String>();
        for (Member member : query(Member.class).asList()) {
            if (!StringUtil.isEmpty(member.getEmail())) {
                emails.add(member.getEmail());
            }
        }

        StringBuilder buf = new StringBuilder();
        buf.append("下記URLにアクセスしてくじをひいてください。\n");
        buf.append("http://itsusaki-family-kuji.appspot.com/ \n");

        Message msg = new Message();
        msg.setSubject("★いつさきファミリーくじのご案内★");
        msg.setTextBody(buf.toString());
        msg.setSender(Config.GAE_ADMIN_EMAIL);
        msg.setTo(emails);

        MailService serv = MailServiceFactory.getMailService();
        serv.send(msg);

        return forward("mail.jsp");
    }
}
