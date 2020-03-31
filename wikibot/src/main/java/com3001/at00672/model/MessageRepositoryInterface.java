package com3001.at00672.model;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

//@NoRepositoryBean
public interface MessageRepositoryInterface<T extends Message> extends CrudRepository<T, Long> {
}