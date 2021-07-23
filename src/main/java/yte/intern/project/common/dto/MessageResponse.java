package yte.intern.project.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.common.enums.MessageType;


@Getter
@RequiredArgsConstructor
public class MessageResponse {
    private final MessageType messageType;
    private final String message;
}
