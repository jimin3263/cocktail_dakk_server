package com.cocktail_dakk.src.domain.cocktail;

import com.cocktail_dakk.src.domain.mixingMethod.MixingMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class CocktailMixingMethod {

    @Id @GeneratedValue
    private Long cocktailMixingMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mixingMethodId")
    private MixingMethod mixingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cocktailInfoId")
    private CocktailInfo cocktailInfo;

}
