package microservice.user.reactive.login.infraestructure.driven.jpa.adapter;

import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.out.UserPersistencePort;
import microservice.user.reactive.login.infraestructure.driven.jpa.mapper.UserJpaMapper;
import microservice.user.reactive.login.infraestructure.driven.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserPersistencePort {

    private final UserJpaRepository userRepository;
    private final UserJpaMapper userMapper;

    @Override
    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id).map(userMapper::toModel);
    }

    @Override
    public Optional<UserModel> findByDni(String dni) {
        return userRepository.findByDni(dni).map(userMapper::toModel);
    }

    @Override
    public List<UserModel> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(name, name)
                .stream().map(userMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll().stream().map(userMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public UserModel update(UserModel user) {
        return userMapper.toModel(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
