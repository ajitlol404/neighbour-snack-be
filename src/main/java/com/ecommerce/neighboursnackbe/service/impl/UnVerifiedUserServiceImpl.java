package com.ecommerce.neighboursnackbe.service.impl;

import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPasswordDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.UnVerifiedUserDTO.UnVerifiedUserRequestDTO;
import com.ecommerce.neighboursnackbe.entity.UnVerifiedUser;
import com.ecommerce.neighboursnackbe.exception.SmtpException;
import com.ecommerce.neighboursnackbe.repository.UnVerifiedUserRepository;
import com.ecommerce.neighboursnackbe.service.SmtpService;
import com.ecommerce.neighboursnackbe.service.UnVerifiedUserService;
import com.ecommerce.neighboursnackbe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.*;

@Service
@RequiredArgsConstructor
public class UnVerifiedUserServiceImpl implements UnVerifiedUserService {

    private final UnVerifiedUserRepository unVerifiedUserRepository;
    private final UserService userService;
    private final SmtpService smtpService;

    @Override
    public UnVerifiedUserPublicResponseDTO registerUnVerifiedUser(UnVerifiedUserRequestDTO unVerifiedUserRequestDTO) {
        if (unVerifiedUserRepository.existsByEmail(unVerifiedUserRequestDTO.email())) {
            throw new SmtpException("The email you have chosen is currently pending verification. Please check your email for the verification link or choose a different email.");
        }

        if (userService.userExistsByEmail(unVerifiedUserRequestDTO.email())) {
            throw new SmtpException("The email you have entered is already associated with a verified account. Please use a different email.");
        }

        UnVerifiedUser entity = unVerifiedUserRequestDTO.toEntity();
        UnVerifiedUser saved = unVerifiedUserRepository.save(entity);

        // Send verification email
        String link = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + saved.getUuid() + "/verify";

        smtpService.sendEmail(
                unVerifiedUserRequestDTO.email(),
                "Complete Your Registration - " + BRAND_NAME,
                """
                        <html>
                            <body>
                                <div style="text-align: center;">
                                    <p>Hi <b>%s</b>, Welcome to <b>%s</b></p>
                                    <p>We are thrilled to have you on board. To complete your registration, please set your password by clicking the button below:</p>
                                    <p><a style="font-size: 16px;padding: 10px 20px;background-color: #007BFF;color: white;text-decoration: none;border-radius: 5px;" class="button" href="%s">Set Your Password</a></p>
                                    <p>This link will expire in %s minutes, so please set your password as soon as possible.</p>
                                    <p>
                                        Regards, <br />
                                        <b>%s</b>
                                    </p>
                                </div>
                            </body>
                        </html>
                        """.formatted(StringUtils.capitalize(unVerifiedUserRequestDTO.name()), BRAND_NAME, link, VERIFICATION_EXPIRATION_MINUTES, SUPPORT_TEAM)
        );

        return UnVerifiedUserPublicResponseDTO.fromEntity(saved);
    }

    @Override
    public UnVerifiedUserPublicResponseDTO verifyLink(UUID uuid) {
        UnVerifiedUser unVerifiedUser = unVerifiedUserRepository.findUnVerifiedUserByUuid(uuid);
        if (isExpired(unVerifiedUser.getCreatedAt())) {
            unVerifiedUserRepository.delete(unVerifiedUser);
            throw new SmtpException("Verification link has expired");
        }
        return UnVerifiedUserPublicResponseDTO.fromEntity(unVerifiedUser);
    }

    @Override
    public void verifyAndSaveUser(UUID uuid, UnVerifiedUserPasswordDTO unVerifiedUserPasswordDTO) {
        UnVerifiedUser unVerifiedUser = unVerifiedUserRepository.findUnVerifiedUserByUuid(uuid);
        if (isExpired(unVerifiedUser.getCreatedAt())) {
            unVerifiedUserRepository.delete(unVerifiedUser);
            throw new SmtpException("Verification link has expired");
        }

        userService.createUser(
                unVerifiedUser.getName(),
                unVerifiedUser.getEmail(),
                unVerifiedUserPasswordDTO.password()
        );

        unVerifiedUserRepository.delete(unVerifiedUser);

        smtpService.sendEmail(
                unVerifiedUser.getEmail(),
                "Account Created Successfully - " + BRAND_NAME,
                """
                        <html>
                            <body>
                                <div style="text-align: center;">
                                    <p>Hi <b>%s</b>,</p>
                                    <p>Welcome to <b>%s</b>!</p>
                                    <p>Your account has been successfully created. You can now log in using the following credentials:</p>
                                    <p><b>Email:</b> %s</p>
                                    <p><b>Password:</b> Set at the time of account creation.</p>
                                    <p>If you have any questions, feel free to contact us.</p>
                                    <p>
                                        Regards, <br />
                                        <b>%s</b>
                                    </p>
                                </div>
                            </body>
                        </html>
                        """.formatted(
                        StringUtils.capitalize(unVerifiedUser.getName()),
                        BRAND_NAME,
                        unVerifiedUser.getEmail(),
                        SUPPORT_TEAM
                )
        );
    }

    private boolean isExpired(ZonedDateTime createdAt) {
        return createdAt
                .plusMinutes(VERIFICATION_EXPIRATION_MINUTES)
                .isBefore(ZonedDateTime.now());
    }
}
