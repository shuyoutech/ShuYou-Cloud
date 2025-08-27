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

    AIGC_CHAT_COMPLETION("aigcChatCompletion", "AI-对话"),

    AIGC_TEXT_TO_IMAGE("textToImage", "AI-文生图"),

    AIGC_IMAGE_TO_IMAGE("imageToImage", "AI-图生图"),

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

    AIGC_AUDIO_SPEECH("audioSpeech", "文本转音频/语音合成"),

    AIGC_AUDIO_TRANSCRIPTION("audioTranscription", "音频转文本/语音识别"),

    AIGC_AUDIO_TRANSLATION("audioTranslation", "音频/语音翻译"),

    AIGC_EMBEDDING("embedding", "向量"),

    ;

    private final String value;
    private final String label;

}
