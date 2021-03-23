package ru.kinghp.portfolio_manager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.DBUser;

public interface DBUserRepository extends CrudRepository<DBUser, Long> {

    DBUser findByLogin(String login);

}
