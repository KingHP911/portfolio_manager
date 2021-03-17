package ru.kinghp.portfolio_manager.service;

import org.springframework.validation.annotation.Validated;
import ru.kinghp.portfolio_manager.models.Paper;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PaperService {

    Iterable<Paper> findAll();

    Paper add(@Validated Paper paper);

    Paper update(Paper paper);

    boolean existsById(@NotNull Long id);

    Optional<Paper> findById(@NotNull Long id);

    void deleteById(@NotNull Long id);
}
