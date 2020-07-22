package com.ukrainianboyz.nearly.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ukrainianboyz.nearly.exceptions.MessageNotDeliveredException;
import com.ukrainianboyz.nearly.db.entity.DatabaseUser;
import com.ukrainianboyz.nearly.model.requestdata.FirebaseCommandRequest;
import com.ukrainianboyz.nearly.model.requestdata.RelationshipAnswer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class FirebaseService {

    public final String FRIEND_REQUEST = "friendRequest";
    public final String FRIEND_RESPONSE = "friendResponse";
    private final String COMMAND = "command";
    public final String ACCEPT = "accept";
    private final String SENDER = "sender";
    private final String PICTURE = "picture";
    private final String NAME = "name";
    private final String BIO = "bio";


    public void init() {
        if (FirebaseApp.getApps().size() == 0) {
            try {
                InputStream firebaseConfig = this.getClass().getClassLoader().getResourceAsStream("nearly-firebase-adminsdk.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(firebaseConfig))
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanup() {
        if (FirebaseApp.getApps().size() > 0)
            FirebaseApp.getInstance().delete();
    }

    // data is needed to saved incoming request straight to the db
    public void sendFriendRequestMessage(String receiverId, DatabaseUser sender) {
        Message message = buildMessageTemplate(false, FRIEND_REQUEST, receiverId, sender.getUserId())
                .putData(PICTURE, sender.getImageUrl())
                .putData(NAME, sender.getUserName())
                .putData(BIO, sender.getUserBio())
                .build();
        sendFirebaseMessage(message);
    }


    public void sendFriendResponse(RelationshipAnswer answer) {
        Message message = buildMessageTemplate(false,
                FRIEND_RESPONSE,
                answer.getRequesterId(),
                answer.getRequestedId())
                .putData(ACCEPT,answer.getIsAccepted().toString())
                .build();

        sendFirebaseMessage(message);
    }

    public void sendCommandMessage(FirebaseCommandRequest request) {
        Message message = buildMessageTemplate(request.getIsUrgent(),
                request.getCommand(),
                request.getReceiverId(),
                request.getSenderId())
                .build();

        sendFirebaseMessage(message);
    }

    private Message.Builder buildMessageTemplate(boolean urgent, String command, String receiverId, String senderId) {
        Message.Builder builder = Message.builder()
                .setTopic(receiverId)
                .putData(COMMAND, command)
                .putData(SENDER, senderId);

        if (urgent) {
            builder.setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .putData("direct_boot_ok",String.valueOf(true))
                            .build());
        }

        return builder;
    }

    private void sendFirebaseMessage(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new MessageNotDeliveredException();
        }
    }

}
