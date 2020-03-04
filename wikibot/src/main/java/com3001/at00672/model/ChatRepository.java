package com3001.at00672.model;

import com3001.at00672.model.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ChatRepository extends CrudRepository<Message, Long> {
}
