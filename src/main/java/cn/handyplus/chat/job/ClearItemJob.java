package cn.handyplus.chat.job;

import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.MessageUtil;

import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * 清理过期数据
 *
 * @author handy
 */
public class ClearItemJob {

    private ClearItemJob() {
    }

    private static final Semaphore CLEAR_ITEM_LOCK = new Semaphore(1);

    /**
     * 初始化定时任务
     */
    public static void init() {
        // 每12小时检查一次清理数据
        HandySchedulerUtil.runTaskTimerAsynchronously(ClearItemJob::clearWeekMoney, 0, 20 * 60 * 60 * 12);
    }

    /**
     * 清理周贡献
     * 每日 00:00
     *
     * @since 1.10.9
     */
    private static void clearWeekMoney() {
        if (!CLEAR_ITEM_LOCK.tryAcquire()) {
            return;
        }
        try {
            Date date = new Date();
            int num = ChatPlayerItemService.getInstance().clearWeekData();
            MessageUtil.sendConsoleMessage("清理过期数据成功,清理时间:" + DateUtil.format(date, DateUtil.YYYY_HH) + ",本次清理:" + num + "数据");
        } finally {
            CLEAR_ITEM_LOCK.release();
        }
    }

}