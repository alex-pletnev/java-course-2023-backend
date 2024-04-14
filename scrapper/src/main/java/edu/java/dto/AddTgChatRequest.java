package edu.java.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddTgChatRequest(@Positive long tgChatId, @NotBlank String tag) {

}
