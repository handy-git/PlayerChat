package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.enums.IndexEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 玩家喇叭
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_horn", comment = "玩家喇叭")
public class ChatPlayerHornEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private String playerUuid;

    @TableField(value = "type", comment = "类型")
    private String type;

    @TableField(value = "number", comment = "数量")
    private Integer number;

}