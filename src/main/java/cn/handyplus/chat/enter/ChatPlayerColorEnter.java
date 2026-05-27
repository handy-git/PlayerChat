package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.IndexEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * 玩家聊天颜色
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_color", comment = "玩家聊天颜色")
public class ChatPlayerColorEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称", notNull = true)
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private UUID playerUuid;

    @TableField(value = "type", comment = "类型", notNull = true)
    private String type;

    @TableField(value = "color", comment = "颜色", notNull = true)
    private String color;

}
