package com3001.at00672;

import org.alicebot.ab.Chat;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ChatRepository extends CrudRepository<Message, Long> {
}
