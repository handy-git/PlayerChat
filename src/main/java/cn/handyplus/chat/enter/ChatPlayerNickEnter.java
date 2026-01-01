package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.IndexEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * 玩家昵称
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_nick", comment = "玩家昵称")
public class ChatPlayerNickEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.UNIQUE)
    private UUID playerUuid;

    @TableField(value = "nick_name", comment = "玩家昵称")
    private String nickName;

}