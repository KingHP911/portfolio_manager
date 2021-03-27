package ru.kinghp.portfolio_manager.service;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kinghp.portfolio_manager.models.News;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface NewsService {

    List<News> findAllPagingIsViewed(Boolean isViewed, Pageable pageable);

    List<News> findAllNotViewed();

    Iterable<News> findAll();

    News add(@Validated News news);

    News update(News news);

    News openById(@NotNull Long id);

    News markAsNotViewedById(@NotNull Long id);

    boolean existsById(@NotNull Long id);

    Optional<News> findById(@NotNull Long id);

    void deleteById(@NotNull Long id);

    String processActonButtonById(@NotNull Long id);
}
