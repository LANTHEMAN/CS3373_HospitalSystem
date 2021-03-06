package edu.wpi.cs3733d18.teamF.notifications;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioHandlerSingleton {
    // Find your Account Sid and Token at twilio.com/user/account
    private static TwilioHandlerSingleton instance = new TwilioHandlerSingleton();
    public static final String ACCOUNT_SID = "AC41fed04d7534e89a79c20ed083bccde6";
    public static final String AUTH_TOKEN = "4db66deaa1cdfe44fb1e5a2d279c0b88";

    private TwilioHandlerSingleton(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static TwilioHandlerSingleton getInstance(){return instance;}

    public void sendMessage(String messageString) {
        sendMessage("+15087622648", messageString);

    }

    public void sendMessage(String phoneNumber, String messageString) {
        try{
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber("+17742955624"),
                    messageString).create();
        }catch(com.twilio.exception.ApiException exception){
            System.out.println("Did not send message");
        }
    }
}
