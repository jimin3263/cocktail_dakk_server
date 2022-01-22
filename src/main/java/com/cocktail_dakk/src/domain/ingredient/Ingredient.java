package com.cocktail_dakk.src.domain.ingredient;

import com.cocktail_dakk.src.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.cocktail_dakk.src.domain.cocktail.CocktailIngredient;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Ingredient {

    @Id @GeneratedValue
    private Long ingredientId;

    private String ingredientName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "ingredient")
    List<CocktailIngredient> cocktailIngredients=new ArrayList<>();

    @Builder
    public Ingredient(String ingredientName){
        this.ingredientName=ingredientName;
    }

}
