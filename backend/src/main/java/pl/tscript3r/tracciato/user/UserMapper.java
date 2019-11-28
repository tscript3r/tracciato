package pl.tscript3r.tracciato.user;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.user.api.UserDto;

public final class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDto map(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    public static UserEntity map(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

}
