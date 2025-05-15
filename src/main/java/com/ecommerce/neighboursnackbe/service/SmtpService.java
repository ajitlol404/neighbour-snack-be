package com.ecommerce.neighboursnackbe.service;


import com.ecommerce.neighboursnackbe.dto.EmailRequestDTO;
import com.ecommerce.neighboursnackbe.dto.SmtpDTO.SmtpRequestDTO;
import com.ecommerce.neighboursnackbe.dto.SmtpDTO.SmtpResponseDTO;
import com.ecommerce.neighboursnackbe.dto.SmtpDTO.SmtpToggleRequestDTO;
import com.ecommerce.neighboursnackbe.entity.Smtp;

import java.util.List;
import java.util.UUID;

public interface SmtpService {

    SmtpResponseDTO saveSmtp(SmtpRequestDTO smtpRequestDTO);

    List<SmtpResponseDTO> getAllSmtpConfigs();

    SmtpResponseDTO getSmtpByUuid(UUID uuid);

    SmtpResponseDTO updateSmtp(UUID uuid, SmtpRequestDTO smtpRequestDTO);

    SmtpResponseDTO toggleSmtpStatus(UUID uuid, SmtpToggleRequestDTO toggleRequestDTO);

    void deleteSmtp(UUID uuid);

    SmtpResponseDTO getActiveSmtp();

    Smtp getActiveSmtpEntity();

    void sendEmail(EmailRequestDTO emailRequestDTO);

    void sendEmail(String toEmail, String subject, String bodyHtml);

}
