package dev.projectfitness.admin.daos;

import java.util.Optional;

import dev.projectfitness.admin.daos.paging.Page;
import dev.projectfitness.admin.daos.paging.Pageable;

public interface PageableDao<T, ID> extends Dao<T, ID> {
	public Pageable<T> getAll(Page page);

	Optional<T> getById(ID id);

	void save(T t);

	boolean update(T t);

	boolean delete(ID id);
}
