package com.dao;

import java.util.List;
import java.util.Optional;

// Data Access Object Pattern
public interface DAO<EntityType, IdType> {
	List<EntityType> getAll();
	Optional<EntityType> get(IdType id);
	void create(EntityType entity);
	void update(EntityType entity);
	void delete(EntityType entity);
}
