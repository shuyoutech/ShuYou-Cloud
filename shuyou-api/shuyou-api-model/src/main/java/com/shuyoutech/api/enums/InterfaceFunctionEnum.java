package com.shuyoutech.api.enums;

import com.shuyoutech.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口功能枚举
 *
 * @author YangChao
 * @date 2025-05-12 12:00
 **/
@Getter
@AllArgsConstructor
public enum InterfaceFunctionEnum implements BaseEnum<String, String> {

    AIGC_CHAT_COMPLETION("aigcChatCompletionService", "AI-对话"),

    AIGC_BETA_COMPLETION("aigcBetaCompletionService", "AI-补全"),

    AIGC_AUDIO_SPEECH("aigcAudioSpeechService", "AI-文本转音频"),

    AIGC_AUDIO_TRANSCRIPTION("aigcAudioTranscriptionService", "AI-音频转文本"),

    AIGC_AUDIO_TRANSLATION("aigcAudioTranslationService", "AI-音频翻译"),

    AIGC_EMBEDDING("aigcEmbeddingService", "AI-文本向量化"),

    AIGC_MULTIMODAL_EMBEDDING("aigcMultimodalEmbeddingService", "AI-图像向量化"),

    AIGC_MODERATION("aigcModerationService", "AI-内容审核"),

    AIGC_TEXT_TO_IMAGE("aigcTextToImageService", "AI-文生图"),

    AIGC_IMAGE_TO_IMAGE("aigcImageToImageService", "AI-图生图"),

    AIGC_MULTI_IMAGE_TO_IMAGE("aigcMultiImageToImageService", "AI-多图生图"),

    AIGC_IMAGE_EDIT("imageEdit", "图像编辑"),

    AIGC_IMAGE_VARIATION("imageVariation", "图像变形"),

    AIGC_IMAGE_VIRTUAL_TRY_ON("imageVirtualTryOn", "虚拟试穿"),

    AIGC_IMAGE_BACKGROUND("imageBackground", "图像背景"),

    AIGC_WORDART_SEMANTIC("wordartSemantic", "文字变形"),

    AIGC_WORDART_TEXTURE("wordartTexture", "文字纹理"),

    AIGC_TEXT_TO_VIDEO("textToVideo", "文生视频"),

    AIGC_IMAGE_TO_VIDEO("imageToVideo", "图生视频"),

    AIGC_MULTI_IMAGE_TO_VIDEO("multiImageToVideo", "多图参考生视频"),

    AIGC_VIDEO_EXTEND("videoExtend", "视频-延长"),

    AIGC_VIDEO_LIP_SYNC("videoLipSync", "视频-对口型"),

    AIGC_VIDEO_EFFECTS("videoEffects", "视频-特效"),

    AIGC_UPSCALE_VIDEO("upscaleVideo", "视频-升级高清"),

    AIGC_VIDEO_CHARACTER_PERFORMANCE("videoCharacterPerformance", "视频-角色表演"),


    ;

    private final String value;
    private final String label;

}
