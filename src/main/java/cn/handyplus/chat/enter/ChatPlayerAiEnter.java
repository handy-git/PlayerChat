package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.IndexEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * 玩家消息AI审核
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_ai", comment = "玩家消息AI审核")
public class ChatPlayerAiEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private UUID playerUuid;

    @TableField(value = "original_message", length = 255, comment = "玩家消息")
    private String originalMessage;

    @TableField(value = "ai_message", length = 500, comment = "AI消息")
    private String aiMessage;

    @TableField(value = "voting_enabled", comment = "本次是否触发投票")
    private Boolean votingEnabled;

    @TableField(value = "vote_number", comment = "投票人数")
    private Integer voteNumber;

    @TableField(value = "create_time", comment = "创建时间")
    private Date createTime;

}
