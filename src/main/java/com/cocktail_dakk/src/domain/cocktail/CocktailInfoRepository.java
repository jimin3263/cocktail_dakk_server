package com.cocktail_dakk.src.domain.cocktail;

import com.cocktail_dakk.src.domain.cocktail.dto.SearchCocktailInfoRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocktailInfoRepository extends JpaRepository<CocktailInfo, Long> {

    Page<CocktailInfo> findAllByKoreanNameContainingOrEnglishNameContainingOrIngredientContaining(Pageable pageable, String koreanName, String englishName, String ingredients);
}
