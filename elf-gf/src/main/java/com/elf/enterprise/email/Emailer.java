/*
 * Emailer.java
 *
 * Created on October 6, 2007, 6:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.enterprise.email;

import com.elf.enterprise.web.Globals;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author bnevins
 */
public class Emailer
{
    public Emailer()
    {
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public void setBcc(String bcc)
    {
        this.bcc = bcc;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void send() throws EmailerException, AddressException, MessagingException
    {
        // at least make sure the essential 5 are here --
        if(!ok(body) || !ok(to) || !ok(from) | !ok(subject) | !ok(host))
            throw new EmailerException("Internal Error: the email params aren't all set.");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        //props.put("mail.debug", "true");
        Session session = Session.getInstance(props);
        // Instantiatee a message
        Message msg = new MimeMessage(session);

        //Set message attributes
        InternetAddress fromIA = new InternetAddress(from);
        msg.setFrom(fromIA);
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        
        if(bcc != null)
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // Set message content
        msg.setText(body);

        //Send the message
        Transport.send(msg);
    }

    private boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }
    
    private String body;
    private String to;
    private String bcc;
    private String subject;
    private String from;
    private String host = Globals.MAIL_SMTP_HOST;
    
    public static void main(String[] args)
    {
        try
        {
            Emailer em = new Emailer();
            em.setTo("byron.nevins@gmail.com");
            em.setFrom("Registration@bnevins.com");
            em.setSubject("To:byron From: biz");
            em.setBody("TESTING");
            em.send();
        } 
        catch (AddressException ex)
        {
            ex.printStackTrace();
        } 
        catch (EmailerException ex)
        {
            ex.printStackTrace();
        } 
        catch (MessagingException ex)
        {
            ex.printStackTrace();
        }
    }
}
