package com.ecommerce.neighboursnackbe.controller;

import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPasswordDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserRequestDTO;
import com.ecommerce.neighboursnackbe.service.UnVerifiedUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.BASE_API_PATH;

@RestController
@RequestMapping(BASE_API_PATH + "/unverified-users")
@RequiredArgsConstructor
public class UnVerifiedUserRestController {

    private final UnVerifiedUserService unVerifiedUserService;

    @PostMapping
    public ResponseEntity<UnVerifiedUserPublicResponseDTO> createUnverifiedUser(@RequestBody @Valid UnVerifiedUserRequestDTO unVerifiedUserRequestDTO) {
        UnVerifiedUserPublicResponseDTO unVerifiedUserPublicResponseDTO = unVerifiedUserService.registerUnVerifiedUser(unVerifiedUserRequestDTO);

        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}/verify")
                                .buildAndExpand(unVerifiedUserPublicResponseDTO.uuid())
                                .toUri()
                )
                .body(unVerifiedUserPublicResponseDTO);
    }

    @GetMapping("/{id}/verify")
    public UnVerifiedUserPublicResponseDTO getUnverifiedUser(@PathVariable UUID id) {
        return unVerifiedUserService.verifyLink(id);
    }

    @PostMapping("/{id}/verify")
    public void verifyAndSaveUser(@PathVariable UUID id, @Valid @RequestBody UnVerifiedUserPasswordDTO unVerifiedUserPasswordDTO) {
        unVerifiedUserService.verifyAndSaveUser(id, unVerifiedUserPasswordDTO);
    }

}
