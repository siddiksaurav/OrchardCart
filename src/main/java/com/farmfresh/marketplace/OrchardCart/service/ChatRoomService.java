package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.model.ChatRoom;
import com.farmfresh.marketplace.OrchardCart.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public String getChatRoomId(Integer senderId, Integer recipientId, boolean createNewRoomIfNotExists) {
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get().getChatId();
        } else {
            String chatId = createChatId(senderId, recipientId);
            return chatId;
        }
    }

    @Transactional
    public String createChatId(Integer senderId, Integer recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setSenderId(senderId);
        senderRecipient.setRecipientId(recipientId);
        ChatRoom recipientSender = new ChatRoom();
        recipientSender.setChatId(chatId);
        recipientSender.setSenderId(recipientId);
        recipientSender.setRecipientId(senderId);
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
