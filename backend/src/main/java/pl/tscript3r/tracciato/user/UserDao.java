package pl.tscript3r.tracciato.user;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.infrastructure.db.Dao;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

public final class UserDao extends Dao<UserEntity, UserDto> {

    private final UserRepositoryAdapter userRepositoryAdapter;

    public UserDao(ModelMapper modelMapper, UserRepositoryAdapter repositoryAdapter, FailureResponse notFoundFailureResponse) {
        super(modelMapper, repositoryAdapter, notFoundFailureResponse);
        this.userRepositoryAdapter = repositoryAdapter;
    }

    public InternalResponse<UserDto> getByUsername(String username, FailureResponse failureResponse) {
        return InternalResponse.ofOption(userRepositoryAdapter.findByUsername(username)
                .map(this::map), failureResponse);
    }

    public InternalResponse<UserDto> getByUsername(String username) {
        return InternalResponse.ofOption(userRepositoryAdapter.findByUsername(username)
                .map(this::map), notFoundFailureResponse);
    }

}
