package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.repository.PortfoliosPaperRepository;
import ru.kinghp.portfolio_manager.service.PortfoliosPaperService;

import javax.validation.constraints.NotNull;

@Data
@Service
public class PortfoliosPaperServiceImpl implements PortfoliosPaperService {

    private final PortfoliosPaperRepository portfoliosPaperRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(@NotNull Long id) {
        return portfoliosPaperRepository.existsById(id);
    }

}
