package com.cocktail_dakk.src.service.recommend;

import com.cocktail_dakk.src.domain.Status;
import com.cocktail_dakk.src.domain.cocktail.*;
import com.cocktail_dakk.src.domain.cocktail.dto.GetRecommendationListRes;
import com.cocktail_dakk.src.domain.drink.Drink;
import com.cocktail_dakk.src.domain.drink.DrinkRepository;
import com.cocktail_dakk.src.domain.keyword.Keyword;
import com.cocktail_dakk.src.domain.keyword.KeywordRepository;
import com.cocktail_dakk.src.domain.user.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class KeywordCocktailServiceTest {

    @Autowired
    private KeywordCocktailService keywordCocktailService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @BeforeAll
    private static void beforeAll(@Autowired CocktailInfoRepository cocktailInfoRepository,
                                  @Autowired UserInfoRepository userInfoRepository,
                                  @Autowired UserKeywordRepository userKeywordRepository,
                                  @Autowired KeywordRepository keywordRepository,
                                  @Autowired DrinkRepository drinkRepository,
                                  @Autowired CocktailDrinkRepository cocktailDrinkRepository,
                                  @Autowired CocktailKeywordRepository cocktailKeywordRepository,
                                  @Autowired UserDrinkRepository userDrinkRepository) {

        //칵테일
        CocktailInfo cocktailInfo1 = createCocktail("Golden Dream", "골든 드림", "달콤하고 부드러운 맛 덕분에 주로 식후주로 사용되며, 이전 IBA 공식 칵테일에서도 식후주로 분류된 바 있다.",
                "url111", "url222", "url333", 2, Status.ACTIVE);
        CocktailInfo cocktailInfo2 = createCocktail("Woo Woo", "우우", "두 번째 테스트용 칵테일 데이터",
                "url222", "url2222", "url22222", 14, Status.ACTIVE);
        CocktailInfo cocktailInfo3 = createCocktail("Trinidad Sour", "트리니다드 사워", "두 번째 테스트용 칵테일 데이터",
                "url222", "url2222", "url22222", 10, Status.INACTIVE);
        CocktailInfo cocktailInfo4 = createCocktail("test cocktail4", "테스트 칵테일4", "네 번째 테스트용 칵테일 데이터",
                "url222", "url2222", "url22222", 10, Status.INACTIVE);
        CocktailInfo cocktailInfo5 = createCocktail("test cocktail5", "테스트 칵테일5", "다섯 번째 테스트용 칵테일 데이터",
                "url222", "url2222", "url22222", 10, Status.INACTIVE);

        cocktailInfoRepository.save(cocktailInfo1);
        cocktailInfoRepository.save(cocktailInfo2);
        cocktailInfoRepository.save(cocktailInfo3);
        cocktailInfoRepository.save(cocktailInfo4);
        cocktailInfoRepository.save(cocktailInfo5);

        //칵테일 기주
        Drink drink1 = Drink.builder()
                .drinkName("보드카")
                .build();

        Drink drink2 = Drink.builder()
                .drinkName("위스키")
                .build();

        drinkRepository.save(drink1);
        drinkRepository.save(drink2);


        CocktailDrink cocktailDrink1 = new CocktailDrink(cocktailInfo2, drink1);
        CocktailDrink cocktailDrink2 = new CocktailDrink(cocktailInfo1, drink2);
        CocktailDrink cocktailDrink3 = new CocktailDrink(cocktailInfo3, drink1);
        CocktailDrink cocktailDrink4= new CocktailDrink(cocktailInfo4, drink1);
        CocktailDrink cocktailDrink5 = new CocktailDrink(cocktailInfo5, drink1);

        cocktailDrinkRepository.save(cocktailDrink1);
        cocktailDrinkRepository.save(cocktailDrink2);
        cocktailDrinkRepository.save(cocktailDrink3);
        cocktailDrinkRepository.save(cocktailDrink4);
        cocktailDrinkRepository.save(cocktailDrink5);

        //칵테일 키워드
        Keyword keyword=Keyword.builder()
                .keywordName("우유")
                .build();
        keywordRepository.save(keyword);

        Keyword keyword2=Keyword.builder()
                .keywordName("상쾌한")
                .build();
        keywordRepository.save(keyword2);

        Keyword keyword3=Keyword.builder()
                .keywordName("레이디킬러")
                .build();
        keywordRepository.save(keyword3);

        CocktailKeyword cocktailKeyword1 = new CocktailKeyword(cocktailInfo1, keyword);
        CocktailKeyword cocktailKeyword2 = new CocktailKeyword(cocktailInfo2, keyword2);
        CocktailKeyword cocktailKeyword3 = new CocktailKeyword(cocktailInfo3, keyword3);
        CocktailKeyword cocktailKeyword4 = new CocktailKeyword(cocktailInfo4, keyword2);
        CocktailKeyword cocktailKeyword5 = new CocktailKeyword(cocktailInfo5, keyword3);

        cocktailKeywordRepository.save(cocktailKeyword1);
        cocktailKeywordRepository.save(cocktailKeyword2);
        cocktailKeywordRepository.save(cocktailKeyword3);
        cocktailKeywordRepository.save(cocktailKeyword4);
        cocktailKeywordRepository.save(cocktailKeyword5);

        //유저
        UserInfo userInfo = createUser("test1", "minnie",23,"F",12,Status.ACTIVE);
        userInfoRepository.save(userInfo);

        //유저 키워드
        UserKeyword userKeyword=new UserKeyword(userInfo, keyword);
        userKeywordRepository.save(userKeyword);
        userInfoRepository.flush();

        //유저 기주
        UserDrink userDrink = new UserDrink(userInfo, drink1);
        userDrinkRepository.save(userDrink);

    }

    @AfterAll
    private static void afterAll(@Autowired CocktailInfoRepository cocktailInfoRepository,
                                 @Autowired UserInfoRepository userInfoRepository,
                                 @Autowired UserKeywordRepository userKeywordRepository,
                                 @Autowired KeywordRepository keywordRepository,
                                 @Autowired DrinkRepository drinkRepository,
                                 @Autowired CocktailDrinkRepository cocktailDrinkRepository,
                                 @Autowired CocktailKeywordRepository cocktailKeywordRepository,
                                 @Autowired UserDrinkRepository userDrinkRepository){
        System.out.println("afterAll");
        userKeywordRepository.deleteAll();
        cocktailDrinkRepository.deleteAll();
        cocktailKeywordRepository.deleteAll();
        userDrinkRepository.deleteAll();

        cocktailInfoRepository.deleteAll();
        userInfoRepository.deleteAll();
        keywordRepository.deleteAll();
        drinkRepository.deleteAll();
    }

    @Test
    @DisplayName("유저가 설정하지 않은 키워드에서 추천한다.")
    @Transactional
    public void getKeywordRecommendation() throws Exception{
        //when
        Optional<UserInfo> byEmail = userInfoRepository.findByEmail("test1");
        List<String> userKeywords = byEmail.get().getUserKeywords()
                .stream().map(UserKeyword::getKeyword)
                .map(Keyword::getKeywordName)
                .collect(Collectors.toList());

        System.out.println("====서비스 코드 확인===");

        GetRecommendationListRes keywordRecommendation = keywordCocktailService.getKeywordRecommendation(byEmail.get());

        //then
        assertThat(userKeywords).doesNotContain(keywordRecommendation.getDescription());
    }

    private static CocktailInfo createCocktail(String englishName, String koreanName, String description, String url1,
                                               String url2, String url3, int level, Status status) {
        return CocktailInfo.builder()
                .englishName(englishName)
                .koreanName(koreanName)
                .description(description)
                .cocktailImageURL(url1)
                .cocktailBackgroundImageURL(url2)
                .recommendImageURL(url3)
                .alcoholLevel(level)
                .status(status)
                .build();
    }

    private static UserInfo createUser(String email, String nickname, Integer age, String sex, Integer alcoholLevel, Status status){
        UserInfo userInfo = UserInfo.builder()
                .email(email)
                .role(Role.USER)
                .build();

        userInfo.initUserInfo(nickname, age, sex, alcoholLevel, status);

        return userInfo;
    }

}