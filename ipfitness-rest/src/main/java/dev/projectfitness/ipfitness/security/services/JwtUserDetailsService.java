package dev.projectfitness.ipfitness.security.services;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FitnessUserEntity entity = fitnessUserEntityRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        return modelMapper.map(entity, JwtUser.class);
    }
}
