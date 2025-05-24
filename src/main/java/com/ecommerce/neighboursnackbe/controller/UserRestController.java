package com.ecommerce.neighboursnackbe.controller;

import com.ecommerce.neighboursnackbe.security.UserDetailsImpl;
import com.ecommerce.neighboursnackbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.BASE_API_PATH;

@RestController
@RequestMapping(BASE_API_PATH + "/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/image")
    public ResponseEntity<Resource> getUserImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userUuid = userDetails.getUuid();
        Resource image = userService.getUserImage(userUuid);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }


}
