package com.chaos.mail;

/**
 * @author : wzq
 * @since : 2024-05-19 17:59
 **/
public class MailMessageTemplate {
    public static String groupApplyKey(Long groupId ,Long applyUserId){
        return "user:" +
                applyUserId +
                " apply join in group:" +
                groupId;
    }


}
