package kopis.k_backend.openai;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompletionDto {

    // 사용할 모델
    private String model;

    // 사용할 명령어
    private String prompt;

    // 다양성 조정 수치(default : 1)
    private float temperature = 0f;

    // 최대 사용할 토큰(default : 16)
    private int max_tokens = 16;

    @Builder
    public CompletionDto(String model, String prompt, float temperature, int max_tokens) {
        this.model = model;
        this.prompt = prompt;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }
}

/*
{
  "model": "gpt-3.5-turbo-instruct",
  "prompt": "시카고 뮤지컬 장르를 알려줘.",
  "temperature": 0,
  "max_tokens": 100
}
*/