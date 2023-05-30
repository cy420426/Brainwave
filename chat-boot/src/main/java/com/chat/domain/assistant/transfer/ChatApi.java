package com.chat.domain.assistant.transfer;

import com.chat.domain.assistant.BO.ChatParams;
import com.dtflys.forest.annotation.HTTPProxy;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.http.ForestResponse;

import java.util.Map;

/**
 * 聊天相关API
 */
public interface ChatApi {
    @Post("#{openai.chat.host}/v1/chat/completions")
    ForestResponse<ChatResult> ChatCompletions(@Header Map<String, String> headers, @JSONBody ChatParams params);

}
