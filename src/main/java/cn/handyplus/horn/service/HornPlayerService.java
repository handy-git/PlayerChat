package cn.handyplus.horn.service;

import cn.handyplus.horn.enter.HornPlayerEnter;
import cn.handyplus.lib.db.Db;

/**
 * 玩家喇叭
 *
 * @author handy
 */
public class HornPlayerService {
    private HornPlayerService() {
    }

    private static class SingletonHolder {
        private static final HornPlayerService INSTANCE = new HornPlayerService();
    }

    public static HornPlayerService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     */
    public void add(HornPlayerEnter enter) {
        Db.use(HornPlayerEnter.class).execution().insert(enter);
    }

    /**
     * 根据uid和类型查询
     *
     * @param playerUuid uid
     * @param type       类型
     * @return 数据
     */
    public HornPlayerEnter findByUidAndType(String playerUuid, String type) {
        Db<HornPlayerEnter> use = Db.use(HornPlayerEnter.class);
        use.where().eq(HornPlayerEnter::getPlayerUuid, playerUuid)
                .eq(HornPlayerEnter::getPlayerUuid, type);
        return use.execution().selectOne();
    }

    /**
     * 根据id 减少数量
     *
     * @param id ID
     */
    public void subtractNumber(Integer id, int number) {
        Db<HornPlayerEnter> db = Db.use(HornPlayerEnter.class);
        db.update().subtract(HornPlayerEnter::getNumber, HornPlayerEnter::getNumber, number);
        db.execution().updateById(id);
    }

    /**
     * 根据id 增加数量
     *
     * @param id ID
     */
    public void addNumber(Integer id, int number) {
        Db<HornPlayerEnter> db = Db.use(HornPlayerEnter.class);
        db.update().add(HornPlayerEnter::getNumber, HornPlayerEnter::getNumber, number);
        db.execution().updateById(id);
    }

}