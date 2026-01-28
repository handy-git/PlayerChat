package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.IndexEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * 玩家禁言
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_mute", comment = "玩家禁言")
public class ChatPlayerMuteEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private UUID playerUuid;

    @TableField(value = "reason", length = 255, comment = "禁言原因")
    private String reason;

    @TableField(value = "operator_name", comment = "操作者名称")
    private String operatorName;

    @TableField(value = "mute_time", comment = "禁言时长(秒)")
    private Integer muteTime;

    @TableField(value = "create_time", comment = "创建时间")
    private Date createTime;

    @TableField(value = "expire_time", comment = "过期时间")
    private Date expireTime;

}
