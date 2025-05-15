package com.ecommerce.neighboursnackbe.service;

import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPasswordDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserRequestDTO;

import java.util.UUID;

public interface UnVerifiedUserService {

    UnVerifiedUserPublicResponseDTO registerUnVerifiedUser(UnVerifiedUserRequestDTO unVerifiedUserRequestDTO);

    UnVerifiedUserPublicResponseDTO verifyLink(UUID uuid);

    void verifyAndSaveUser(UUID uuid, UnVerifiedUserPasswordDTO unVerifiedUserPasswordDTO);

}