package com.cocktail_dakk.src.controller;

import com.cocktail_dakk.config.auth.dto.UserInfoDto;
import com.cocktail_dakk.src.domain.Status;
import com.cocktail_dakk.src.domain.cocktail.*;
import com.cocktail_dakk.src.domain.drink.Drink;
import com.cocktail_dakk.src.domain.drink.DrinkRepository;
import com.cocktail_dakk.src.domain.keyword.Keyword;
import com.cocktail_dakk.src.domain.keyword.KeywordRepository;
import com.cocktail_dakk.src.domain.user.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserInfoRepository userInfoRepository;

    @BeforeEach
    private void beforeEach(@Autowired CocktailInfoRepository cocktailInfoRepository,
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

        cocktailInfoRepository.save(cocktailInfo1);
        cocktailInfoRepository.save(cocktailInfo2);
        cocktailInfoRepository.save(cocktailInfo3);

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

        cocktailDrinkRepository.save(cocktailDrink1);
        cocktailDrinkRepository.save(cocktailDrink2);
        cocktailDrinkRepository.save(cocktailDrink3);

        //칵테일 키워드
        Keyword keyword=Keyword.builder()
                .keywordName("과일향")
                .build();
        keywordRepository.save(keyword);

        CocktailKeyword cocktailKeyword1 = new CocktailKeyword(cocktailInfo1, keyword);
        CocktailKeyword cocktailKeyword2 = new CocktailKeyword(cocktailInfo2, keyword);
        CocktailKeyword cocktailKeyword3 = new CocktailKeyword(cocktailInfo3, keyword);

        cocktailKeywordRepository.save(cocktailKeyword1);
        cocktailKeywordRepository.save(cocktailKeyword2);
        cocktailKeywordRepository.save(cocktailKeyword3);

        //유저
        UserInfo userInfo = createUser("test", "minnie",23,"F",14,Status.ACTIVE);
        UserInfo save = userInfoRepository.save(userInfo);

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .email(save.getEmail())
                .build();

        Authentication authentication = getAuthentication(userInfoDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //유저 키워드
        UserKeyword userKeyword=new UserKeyword(userInfo, keyword);
        userKeywordRepository.save(userKeyword);
        userInfoRepository.flush();

        //유저 기주
        UserDrink userDrink = new UserDrink(userInfo, drink1);
        userDrinkRepository.save(userDrink);

    }

    @Test
    @WithMockUser(roles = "USER")
    @Transactional
    public void userInfoStatusTest() throws Exception{
        mockMvc.perform(get("/users/status")
                .with(request -> {
                    request.setScheme("http");
                    request.setServerName("localhost");
                    request.setServerPort(8080);

                    return request;
                }))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.email").value("test"))
                .andExpect(jsonPath("$.result.doInit").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    @Transactional
    public void userInfoTest() throws Exception {
        mockMvc.perform(get("/users/info")
                .with(request -> {
                    request.setScheme("http");
                    request.setServerName("localhost");
                    request.setServerPort(8080);

                    return request;
                }))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.email").value("test"));
    }

    private Authentication getAuthentication(UserInfoDto member){
        return new UsernamePasswordAuthenticationToken(member, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private CocktailInfo createCocktail(String englishName, String koreanName, String description, String url1,
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

    private UserInfo createUser(String email, String nickname, Integer age, String sex, Integer alcoholLevel, Status status){
        UserInfo userInfo = UserInfo.builder()
                .email(email)
                .role(Role.USER)
                .build();

        userInfo.initUserInfo(nickname, age, sex, alcoholLevel, status);

        return userInfo;
    }

}
