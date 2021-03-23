package ru.kinghp.portfolio_manager.service.impl;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.repository.PaperRepository;
import ru.kinghp.portfolio_manager.service.PaperService;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Data
@Service
@Transactional
public class PaperServiceImpl implements PaperService {

    //инжектит через @Data не через @Autowired
    private final PaperRepository paperRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Paper> findAll() {
        //todo получать тек цены
        return paperRepository.findAll();
    }

    @Override
    public Paper add(@Validated Paper paper) {
        //todo получать тек цену?
        //todo не добавлять бумаги с уже имеющимися тикерами
        paper.setCurrentPrice(new BigDecimal(0));
        return paperRepository.save(paper);
    }

    @Override
    public Paper update(Paper paper) {

        Paper editingPaper = paperRepository.findById(paper.getId()).orElseThrow();
        editingPaper.setName(paper.getName());
        editingPaper.setTicker(paper.getTicker());
        editingPaper.setType(paper.getType());
        editingPaper.setCurrency(paper.getCurrency());

        return paperRepository.save(editingPaper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(@NotNull Long id) {
        return paperRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paper> findById(@NotNull Long id) {
        //todo получать тек цены
        return paperRepository.findById(id);
    }

    @Override
    public void deleteById(@NotNull Long id) {

        Paper paper = paperRepository.findById(id).orElseThrow();
        try {
            paperRepository.delete(paper);
        }catch (Exception e){
            //todo отправить пользователю сообщение о не возможности удаления
        }

    }
}
