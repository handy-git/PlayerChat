package cn.handyplus.chat.enter;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import cn.handyplus.lib.db.IndexEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 玩家展示
 *
 * @author handy
 */
@Getter
@Setter
@TableName(value = "chat_player_item", comment = "玩家展示")
public class ChatPlayerItemEnter {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "player_name", comment = "玩家名称")
    private String playerName;

    @TableField(value = "player_uuid", comment = "玩家uuid", notNull = true, indexEnum = IndexEnum.INDEX)
    private String playerUuid;

    @TableField(value = "version", comment = "服务器版本")
    private Integer version;

    @TableField(value = "item", length = 20000, comment = "展示物品")
    private String item;

    @TableField(value = "create_time", comment = "创建时间")
    private Date createTime;

}
