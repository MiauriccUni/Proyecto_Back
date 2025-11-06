package com.mahou.mahouback.rest.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mahou.mahouback.logic.entity.message.Message;
import com.mahou.mahouback.logic.entity.message.MessageRepository;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody Message message) {
        Message existing = messageRepository.findById(id).orElseThrow();
        existing.setTitulo(message.getTitulo());
        existing.setSubtitulo(message.getSubtitulo());
        existing.setTextoGrande(message.getTextoGrande());
        return messageRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageRepository.deleteById(id);
    }
}
