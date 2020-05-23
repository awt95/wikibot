/**
 * MessageItemRepository
 * - Interface between MessageItem and database
 * @author Alex Turner
 */
package com3001.at00672.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageItemRepository extends CrudRepository<MessageItem, Long> {
}
