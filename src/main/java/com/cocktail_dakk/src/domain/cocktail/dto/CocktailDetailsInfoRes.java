package com.cocktail_dakk.src.domain.cocktail.dto;


import com.cocktail_dakk.src.domain.cocktail.CocktailInfo;
import com.cocktail_dakk.src.domain.drink.Drink;
import com.cocktail_dakk.src.domain.drink.dto.DrinkRes;
import com.cocktail_dakk.src.domain.keyword.dto.KeywordRes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CocktailDetailsInfoRes {

    private Long cocktailInfoId;
    private String englishName;
    private String koreanName;
    private String description;
    private String nukkiImgUrl;
    private String todayImgUrl;
    private List<MixingMethodRes> cocktailMixingMethod;
    private List<KeywordRes> cocktailKeyword;
    private List<DrinkRes> cocktailDrink;
    private Integer alcoholLevel;
    private String ingredient;
    private BigDecimal ratingAvg;

    public CocktailDetailsInfoRes(CocktailInfo cocktailInfo){
        this.cocktailInfoId = cocktailInfo.getCocktailInfoId();
        this.englishName = cocktailInfo.getEnglishName();
        this.koreanName = cocktailInfo.getKoreanName();
        this.description = cocktailInfo.getDescription();
        this.nukkiImgUrl = cocktailInfo.getCocktailBackgroundImageURL();
        this.todayImgUrl = cocktailInfo.getRecommendImageURL();
        this.cocktailMixingMethod = cocktailInfo.getCocktailMixingMethods()
                .stream()
                .map(MixingMethodRes::new)
                .collect(Collectors.toList());
        this.cocktailKeyword = cocktailInfo.getCocktailKeywords()
                .stream()
                .map(KeywordRes::new)
                .collect(Collectors.toList());
        this.cocktailDrink = cocktailInfo.getCocktailDrinks()
                .stream()
                .map(DrinkRes::new)
                .collect(Collectors.toList());
        this.alcoholLevel = cocktailInfo.getAlcoholLevel();
        this.ingredient = cocktailInfo.getIngredient();
        this.ratingAvg = cocktailInfo.getRatingAvg();
    }
}
