package pl.eticket.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.eticket.app.dto.auth.UserInfo;
import pl.eticket.app.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.name")
    UserInfo userToUserInfo(User user);
}
