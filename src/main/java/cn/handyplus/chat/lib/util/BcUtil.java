package cn.handyplus.chat.lib.util;

/**
 * BC工具类
 *
 * @author handy
 */
public class BcUtil {

    /**
     * BC消息参数
     */
    public static class BcMessageParam {

        /**
         * 插件名
         */
        private String pluginName;

        /**
         * 类型
         */
        private String type;

        /**
         * 消息内容
         */
        private String message;

        /**
         * 发送人
         */
        private String playerName;

        /**
         * 发送时间
         */
        private Long timestamp;

        /**
         * 获取插件名
         *
         * @return 插件名
         */
        public String getPluginName() {
            return pluginName;
        }

        /**
         * 设置插件名
         *
         * @param pluginName 插件名
         */
        public void setPluginName(String pluginName) {
            this.pluginName = pluginName;
        }

        /**
         * 获取类型
         *
         * @return 类型
         */
        public String getType() {
            return type;
        }

        /**
         * 设置类型
         *
         * @param type 类型
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * 获取消息内容
         *
         * @return 消息内容
         */
        public String getMessage() {
            return message;
        }

        /**
         * 设置消息内容
         *
         * @param message 消息内容
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * 获取发送人
         *
         * @return 发送人
         */
        public String getPlayerName() {
            return playerName;
        }

        /**
         * 设置发送人
         *
         * @param playerName 发送人
         */
        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        /**
         * 获取发送时间
         *
         * @return 发送时间
         */
        public Long getTimestamp() {
            return timestamp;
        }

        /**
         * 设置发送时间
         *
         * @param timestamp 发送时间
         */
        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }

}
