
package com.cbs.oukasystem.entity.login;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "login_token") // JPA-Hibernate
public class TokenEntity extends NormalEntity {

    @Schema(name = "userId", description = "ユーザー ID", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String userId;

    @Schema(name = "userName", description = "ユーザー")
    private String userName;

    @Schema(name = "token", description = "トークン")
    private String token;

    @Schema(name = "expiration", description = "トークン 有効時間")
    private Date expiration;

    @Schema(name = "token_refresh", description = "リフレッシュのトークン")
    private String tokenRefresh;

    @Schema(name = "expiration_refresh", description = "リフレッシュのトークン 有効時間")
    private Date expirationRefresh;

}
