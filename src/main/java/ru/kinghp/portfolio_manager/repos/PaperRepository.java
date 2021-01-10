package ru.kinghp.portfolio_manager.repos;

import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.Paper;

import java.util.List;

public interface PaperRepository extends CrudRepository<Paper, Long> {

}
