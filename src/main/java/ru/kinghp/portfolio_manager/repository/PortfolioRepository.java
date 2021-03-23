package ru.kinghp.portfolio_manager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kinghp.portfolio_manager.models.Portfolio;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
}
