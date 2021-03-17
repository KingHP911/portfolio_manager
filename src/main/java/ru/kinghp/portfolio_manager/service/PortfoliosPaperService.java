package ru.kinghp.portfolio_manager.service;

import javax.validation.constraints.NotNull;

public interface PortfoliosPaperService {

    boolean existsById(@NotNull Long id);

}
