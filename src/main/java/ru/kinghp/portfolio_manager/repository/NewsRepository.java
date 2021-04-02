package ru.kinghp.portfolio_manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.DBUser;
import ru.kinghp.portfolio_manager.models.News;

import java.awt.print.Pageable;
import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long>, JpaSpecificationExecutor<News> {

    List<News> findAllByIsViewedIsFalse();

    Boolean existsByIdAndDbUser(Long id, DBUser dbUser);

}
