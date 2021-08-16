package com.webtooni.webtooniverse.domain.user.dto.response;

import com.webtooni.webtooniverse.domain.user.domain.User;
import com.webtooni.webtooniverse.domain.user.domain.UserGrade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    private String userName;
    private int userImg;
    private UserGrade userGrade;
    private List<String> genres = new ArrayList<>();

    public UserInfoResponseDto(User user, List<String> userGenre) {
        this.userName = user.getUserName();
        this.userImg = user.getUserImg();
        this.userGrade = user.getUserGrade();
        this.genres.addAll(userGenre);
    }
}
