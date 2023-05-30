package com.chat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.application.ChatApplication;
import com.chat.application.ChatRoomApplication;
import com.chat.application.ChatRoomLogApplication;
import com.chat.core.commons.code.error.AdviceErrorCode;
import com.chat.core.commons.exception.BusinessException;
import com.chat.core.commons.response.ResponseResult;
import com.chat.domain.assistant.BO.ChatParams;
import com.chat.domain.assistant.DO.AssistantParams;
import com.chat.domain.assistant.DTO.ChatMessage;
import com.chat.domain.assistant.VO.ChatRoomLogVO;
import com.chat.domain.assistant.VO.ChatRoomVO;
import com.chat.domain.assistant.VO.ChatVO;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.exception.CommonException;
import com.chat.infrastructure.po.ChatRoom;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * chat 聊天相关接口
 */
@Slf4j
@RestController
@Api(tags = "聊天相关接口")
@RequestMapping("/openai")
@RequiredArgsConstructor
public class ChatController {
    private final ChatApplication chatApplication;
    private final ChatRoomLogApplication chatRoomLogApplication;
    private final ChatRoomApplication chatRoomApplication;

    @PostMapping("/chat/room")
    @ApiOperation(value = "创建新聊天室")
    public ResponseResult<AssistantParams> createChatRoom(@RequestBody ChatRoomVO chatRoomVO) {
        //检查并减少聊天室创建次数
        Long id = UserContext.getUser().getId();
        chatRoomLogApplication.checkRoomTicket(String.valueOf(id));
        AssistantParams assistantParams = new AssistantParams().init();
        ChatRoom chatRoom = chatRoomApplication.saveChatRoom(chatRoomVO);
        assistantParams.setChatRoom(chatRoom);
        chatRoomLogApplication.reduceRoomTicket(String.valueOf(id));
        return ResponseResult.success(assistantParams);
    }

    @PostMapping("/chat/room/room-name")
    @ApiOperation(value = "修改聊天室名称")
    public ResponseResult<Boolean> createChatRoom(@RequestParam Long roomId,
                                                  @RequestParam String roomName) {
        Boolean aBoolean = chatRoomApplication.updateRoomName(roomId, roomName);
        return ResponseResult.success(aBoolean);
    }

    @DeleteMapping("/chat/room/{roomId}")
    @ApiOperation(value = "删除聊天室")
    public ResponseResult<Boolean> createChatRoom(@PathVariable Long roomId) {
        Boolean aBoolean = chatRoomApplication.delRoom(roomId);
        return ResponseResult.success(aBoolean);
    }

    @GetMapping("/chat/room/list")
    @ApiOperation(value = "查询用户聊天室列表")
    public ResponseResult<Page<ChatRoomVO>> chatRoomList(@RequestParam Integer current,
                                                         @RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        Page<ChatRoomVO> chatRoomVOPage = chatRoomApplication.listRoom(String.valueOf(id), current, size);
        return ResponseResult.success(chatRoomVOPage);
    }

    @GetMapping("/chat/{roomId}/history")
    @ApiOperation(value = "查询该聊天室聊天记录")
    public ResponseResult<Page<ChatRoomLogVO>> chatRoomHistory(@PathVariable String roomId,
                                                               @RequestParam Integer current,
                                                               @RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        Page<ChatRoomLogVO> chatRoomLogVOPage = chatRoomLogApplication.listChatHistory(String.valueOf(id), roomId, current, size);
        return ResponseResult.success(chatRoomLogVOPage);
    }

    @PostMapping("/chat")
    @ApiOperation(value = "连续对话，发起请求，最多携带10条上下文")
    public ResponseResult<String> doChat(@RequestBody ChatVO chatVO) {
        //检查聊天室是否存在
        Long id = UserContext.getUser().getId();
        boolean roomExist = chatRoomApplication.existsChatRoom(String.valueOf(id), chatVO.getChatId());
        if (!roomExist) {
            throw BusinessException.build(AdviceErrorCode.CONSTRAINT_EX, "该聊天室不存在");
        }
        if (StringUtils.isNotBlank(chatVO.getContent()) && chatVO.getContent().length() > CacheKey.MAX_CHAR) {
            throw BusinessException.build(CommonException.CHAR_ERROR);
        }
        ChatParams params = new ChatParams();
        List<ChatMessage> messages = chatApplication.getContext(chatVO.getChatId(), chatVO.getWithContext());
        messages.add(chatApplication.buildUserMessage(chatVO.getContent()));
        params.setMessages(messages);
        String res = chatApplication.doChat(params, chatVO.getChatId(), chatVO.getContent());
        Optional.ofNullable(res)
                .orElseThrow(() ->
                        BusinessException.build(AdviceErrorCode.OTHER_EX, "请求错误"));
        return ResponseResult.success(res);
    }

}
