package dev.projectfitness.admin.daos;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> extends Dao<T, ID> {
	List<T> getAll();

	Optional<T> getById(ID id);

	void save(T t);

	boolean update(T t);

	boolean delete(ID id);
}
