package org.gms.melon;

import java.util.Map;

public class MelonConfig {

    // buff 时间
    public static final Integer MULTI_BUFF_TIME = 3;
    // 新角色 人气值
    public static final Integer INIT_FAME = 999;
    // 禁止掉落技能书
    public static final Boolean DISABLE_BOOK_DROP = true;

    public static final Map<String, Integer> MY_INT_CONFIG = Map.ofEntries(
            Map.entry("item_limit_on_map", 500), // 地图上允许的最大物品数量
            Map.entry("exp_split_level_interval", 200), // 组队非攻击玩家必须在怪物N级之内才能获取到怪物经验
            Map.entry("exp_split_leech_interval", 200), // 组队非攻击玩家必须在队友N级之内才能获取到怪物经验
            Map.entry("block_npc_race_condition", 100), // 与NPC对话的时间间隔，毫秒
            Map.entry("scroll_chance_rolls", 3), // 对失败概率取幂，如60%成功的卷轴，失败概率0.4，如果这里设置成2，失败概率则为0.4的平方
            Map.entry("create_guild_min_partners", 1), // 创建家族最少要多少人
            Map.entry("pet_exhaust_count", 10), // 宠物每几分钟饥饿一次
            Map.entry("level_up_sp_gain", 0), // 升级获得的技能点，新手不受此影响
            Map.entry("trade_limit_meso_under_level", -1), // 小于等于该等级的角色每天允许交易的金币将有金额限制，值为-1时则不限制等级
            Map.entry("trade_limit_meso_max", -1), // 限制特定等级以下的角色每天允许交易的金币额度。值为-1时则不限制金额。

            Map.entry("test", 0) // 占位符
    );

    public static final Map<String, Boolean> MY_BOOL_CONFIG = Map.ofEntries(
            Map.entry("use_party_for_starters", true), // 10级以下的玩家可以进行组队邀请
            Map.entry("use_enforce_novice_exp_rate", false), // 10级以下的新手固定1倍经验
            Map.entry("use_enforce_mob_level_range", false), // 击杀低于N级的怪物不会获得经验
            Map.entry("use_enable_solo_expeditions", true), // 副本任务允许单人进入
            Map.entry("use_announce_nx_coupon_loot", true), // 玩家获得点券时是否展示浮动文本
            Map.entry("use_enhanced_chaos_scroll", true), // 混沌卷轴不会使装备属性变差
            Map.entry("use_fast_reuse_hero_will", true), // 大大减少勇士的意志的冷却
            Map.entry("use_anti_immunity_crash", true), // 防御崩坏能够移除怪物的物理免疫和魔法免疫buff
            Map.entry("use_undispel_holy_shield", true), // 圣灵之盾能够防止怪物驱散技能
            Map.entry("use_full_holy_symbol", true), // 神圣祈祷不需要队友也能获取经验加成
            Map.entry("use_add_slots_by_level", true), // 每20级增加背包栏位
            Map.entry("use_fast_dojo_upgrade", true), // 减少道场腰带升级需要的点数
            Map.entry("use_enable_party_level_limit_lift", true), // 是否解除组队副本默认等级范围限制，开启时允许任意等级的玩家进入副本

            Map.entry("test", true) // 占位符
    );

    public static final Map<String, Float> MY_FLOAT_CONFIG = Map.ofEntries(
            Map.entry("boss_respawn_mob_time_rate", 0.001f), // BOSS刷新时间速率

            Map.entry("test", 0.0f) // 占位符
    );

    public static Integer getInt(String key) {
        return MY_INT_CONFIG.get(key);
    }

    public static Boolean getBool(String key) {
        return MY_BOOL_CONFIG.get(key);
    }

    public static Float getFloat(String key) {
        return MY_FLOAT_CONFIG.get(key);
    }
}
