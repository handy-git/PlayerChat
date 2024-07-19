package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.enums.IndexEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 玩家频道
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_channel", comment = "玩家频道")
public class ChatPlayerChannelEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private String playerUuid;

    @TableField(value = "channel", comment = "频道")
    private String channel;

    @TableField(value = "is_api", comment = "是否api频道", fieldDefault = "0")
    private Boolean isApi;

}