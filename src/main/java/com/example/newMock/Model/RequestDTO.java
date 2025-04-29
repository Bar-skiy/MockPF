package com.example.newMock.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
    public class RequestDTO {
        private String rqUID;
        private String clientId;
        private String account;
        private String openDate;
        private String closeDate;


    }
