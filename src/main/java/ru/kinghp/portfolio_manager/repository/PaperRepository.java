package ru.kinghp.portfolio_manager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.Paper;


public interface PaperRepository extends CrudRepository<Paper, Long> {

}
