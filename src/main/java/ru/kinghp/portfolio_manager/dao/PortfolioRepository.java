package ru.kinghp.portfolio_manager.dao;

import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.Portfolio;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
}
