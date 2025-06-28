package cn.handyplus.chat.util;

import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.MessageUtil;

import java.util.Date;

/**
 * 清理过期数据
 *
 * @author handy
 */
public class ClearItemJob {

    private ClearItemJob() {
    }

    /**
     * 初始化定时任务
     */
    public static void init() {
        // 每12小时检查一次清理数据
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            Date date = new Date();
            int num = ChatPlayerItemService.getInstance().clearItemData();
            if (num > 0) {
                MessageUtil.sendConsoleMessage("清理过期数据成功,清理时间:" + DateUtil.format(date, DateUtil.YYYY_HH) + ",本次清理:" + num + "数据");
            }
        }, 20 * 60 * 60, 20 * 60 * 60 * 12);
    }

}