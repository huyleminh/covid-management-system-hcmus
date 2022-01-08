package shared.db;

import shared.exceptions.DBConnectionException;

import java.util.ArrayList;
import java.util.Optional;

// Data Access Object Pattern
public interface DAO<EntityType, IdType> {
	ArrayList<EntityType> getAll() throws DBConnectionException;
	Optional<EntityType> get(IdType id) throws DBConnectionException;
	void create(EntityType entity) throws DBConnectionException;
	void update(EntityType entity) throws DBConnectionException;
	void delete(EntityType entity) throws DBConnectionException;
}
