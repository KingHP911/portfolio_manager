package ru.kinghp.portfolio_manager.service;

import org.springframework.validation.annotation.Validated;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.PortfoliosPaper;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PortfolioService {

    Iterable<Portfolio> findAll();

    Portfolio add(@Validated Portfolio portfolio);

    boolean existsById(@NotNull Long id);

    Optional<Portfolio> findById(@NotNull Long id);

    Portfolio addPaper(@NotNull Long portfolioId, @NotNull Long addingPaperId, @NotNull PortfoliosPaper portfoliosPaper, Integer vol);

    Portfolio removePaper(@NotNull Long portfolioId, @NotNull Long removingPaperId);

    //todo реализовать позже
//    void deleteById(@NotNull Long id);
//    Portfolio update(Portfolio portfolio);

}
