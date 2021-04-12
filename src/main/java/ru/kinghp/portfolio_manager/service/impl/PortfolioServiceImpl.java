package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.PortfoliosPaper;
import ru.kinghp.portfolio_manager.repository.PortfolioRepository;
import ru.kinghp.portfolio_manager.repository.PortfoliosPaperRepository;
import ru.kinghp.portfolio_manager.service.FinnHubService;
import ru.kinghp.portfolio_manager.service.PaperService;
import ru.kinghp.portfolio_manager.service.PortfolioService;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    //инжектит через @Data не через @Autowired
    private final PortfolioRepository portfolioRepository;
    private final PortfoliosPaperRepository portfoliosPaperRepository;
    private final PaperService paperService;
    private final FinnHubService finnHubService;

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasRole('ADMIN') or filterObject.ownerName == authentication.name")
    public Iterable<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public Portfolio add(Portfolio portfolio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        portfolio.setOwnerName(auth.getName());
        return portfolioRepository.save(portfolio);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(@NotNull Long id) {
        return portfolioRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Portfolio> findById(@NotNull Long id) {
        return portfolioRepository.findById(id);
    }

    @Override
    public Portfolio addPaper(@NotNull Long portfolioId, @NotNull Long addingPaperId, PortfoliosPaper portfoliosPaper, Integer vol) {

        Paper paper = paperService.findById(addingPaperId).get();
        Portfolio portfolio = portfolioRepository.findById(portfolioId).get();

        portfoliosPaper.setName(paper.getName());
        portfoliosPaper.setPaper(paper);
        portfoliosPaper.setPurchasePrice(finnHubService.getCurrentPrice(paper.getTicker()));
        portfoliosPaper.setPurchaseDate(LocalDateTime.now());
        portfoliosPaper.setVol(vol);

        //todo м.б. вынести в отдельный метод
        List<PortfoliosPaper> papers = portfolio.getPapers();
        papers.add(portfoliosPaper);
        portfolio.setPapers(papers);
        portfoliosPaper.setPortfolio(portfolio);

        return portfolioRepository.save(portfolio);

    }

    @Override
    public Portfolio removePaper(@NotNull Long portfolioId, @NotNull Long removingPaperId) {

        PortfoliosPaper paper = portfoliosPaperRepository.findById(removingPaperId).get();
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        //todo м.б. вынести в отдельный метод
        List<PortfoliosPaper> papers = portfolio.getPapers();
        papers.remove(paper);
        portfolio.setPapers(papers);
        paper.setPortfolio(null);

        return portfolioRepository.save(portfolio);
    }

}
